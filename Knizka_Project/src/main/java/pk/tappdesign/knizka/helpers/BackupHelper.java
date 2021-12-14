/*
 * Copyright (C) 2020 TappDesign Studios
 * Copyright (C) 2013-2020 Federico Iosue (federico@iosue.it)
 *
 * This software is based on Omni-Notes project developed by Federico Iosue
 * https://github.com/federicoiosue/Omni-Notes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pk.tappdesign.knizka.helpers;

import static pk.tappdesign.knizka.db.DBConst.DB_ATTACHED;
import static pk.tappdesign.knizka.db.DBConst.DB_USER_DATA;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_PASSWORD;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.async.DataBackupIntentScopedStorageService;
import pk.tappdesign.knizka.async.DataBackupIntentService;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.exceptions.checked.BackupAttachmentException;
import pk.tappdesign.knizka.exceptions.checked.BackupNoteException;
import pk.tappdesign.knizka.helpers.notifications.NotificationsHelper;
import pk.tappdesign.knizka.models.Attachment;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.data.ExportImportResult;
import pk.tappdesign.knizka.utils.FileProviderHelper;
import pk.tappdesign.knizka.utils.Security;
import pk.tappdesign.knizka.utils.StorageHelper;
import pk.tappdesign.knizka.utils.TextHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import android.os.Environment;

import com.pixplicity.easyprefs.library.Prefs;

import rx.Observable;

public class BackupHelper {

	private BackupHelper() {
		// hides public constructor
	}

	public static void exportRawDB(Context context, File backupDir)
	{
		try {
			File sd = Environment.getExternalStorageDirectory();

			if (sd.canWrite()) {
				File database = context.getDatabasePath(DB_ATTACHED);
				if (database.exists()) {
					StorageHelper.copyFile(database, new File(backupDir, DB_ATTACHED));
				}
				database = context.getDatabasePath(DB_USER_DATA);
				if (database.exists()) {
					StorageHelper.copyFile(database, new File(backupDir, DB_USER_DATA));
				}
			}

		} catch (Exception e) {
			LogDelegate.e("Error backupping DB file. " + e.getStackTrace());
		}
	}

  public static void exportNotes(File backupDir) {
    for (Note note : DbHelper.getInstance(true).getAllNotes(false)) {
      exportNote(backupDir, note);
    }
  }

	public static void exportNote(File backupDir, Note note) {
		if (Boolean.TRUE.equals(note.isLocked())) {
			note.setContent(Security.encrypt(note.getContent(), Prefs.getString(PREF_PASSWORD, "")));
		}
		File noteFile = getBackupNoteFile(backupDir, note);
		try {
			FileUtils.write(noteFile, note.toJSON());
		} catch (IOException e) {
			LogDelegate.e(String.format("Error on note %s backup: %s",  note.get_id(), e.getMessage()));
		}
	}

	public static ExportImportResult exportNotesScopedStorage(DocumentFile backupDir, NotificationsHelper notificationsHelper) {
		ExportImportResult result = new ExportImportResult();

		String failedString = "";

		List<Note> list = DbHelper.getInstance(true).getAllNotes(false);
		for (Note note : list) {
			try {
				exportNoteScopedStorage(backupDir, note);
				result.addSuccessfully();
			} catch (BackupNoteException e) {
				result.addFailed();
				failedString = " (" + result.getFailed() + " " + Knizka.getAppContext().getString(R.string.failed) + ")";
			}
			notifyNoteBackup(notificationsHelper, list.size(), result.getSuccessfully(), failedString);
		}
		return result;
	}

	public static void exportNoteScopedStorage(DocumentFile backupDir, Note note)
			throws BackupNoteException {

		try {
			if (Boolean.TRUE.equals(note.isLocked())) {
				note.setContent(Security.encrypt(note.getContent(), Prefs.getString(PREF_PASSWORD, "")));
			}

			DocumentFile noteFile = backupDir.createFile("application/json", "" + note.get_id());

			try {
				OutputStream outputStream = Knizka.getAppContext().getContentResolver().openOutputStream(noteFile.getUri());
				outputStream.write(note.toJSON().getBytes());
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				LogDelegate.e(String.format("Error on note %s backup: %s", note.get_id(), e.getMessage()));
				throw new BackupNoteException(e);
			}
		} catch (Exception e) {
			LogDelegate.e("Error during note backup: " + note.get_id(), e);
			throw new BackupNoteException(e);
		}
	}


	@NonNull
	public static File getBackupNoteFile(File backupDir, Note note) {
		return new File(backupDir, note.get_id() + ".json");
	}


	public static boolean duplicateAttachments(File baseDirForAttachments, Note sourceNote, Note duplicatedNote) {
		boolean result = true;

		if (baseDirForAttachments == null)
		{
			return result;
		}

		List<Attachment> list = new ArrayList<>(sourceNote.getAttachmentsList());

		for (Attachment attachment : list) {
			try {
				File f = new File(baseDirForAttachments, getUniqueFileName(attachment.getUriPath()));
				if (f == null)
				{
					result = false; // something wrong, do not copy attachment
				} else {
					Uri attachmentUri = FileProviderHelper.getFileProvider(f);
					InputStream fileInputStream = Knizka.getAppContext().getContentResolver().openInputStream(attachment.getUri());
					StorageHelper.duplicateFile(f, fileInputStream);
					attachment.setUri(attachmentUri);
					attachment.setId(null);
				}
			} catch (FileNotFoundException e) {
				LogDelegate.w("Attachment not found during backup: " + attachment.getUriPath());
				result = false;
			}
		}
		duplicatedNote.setAttachmentsListOld(list);
		duplicatedNote.setAttachmentsList(new ArrayList<>(list));
		return result;
	}

	private static String getUniqueFileName(String uriPath)
	{
		String result = "";

		result =  FilenameUtils.removeExtension(FilenameUtils.getName(uriPath)) + "_" + Calendar.getInstance().getTimeInMillis() + "."  + FilenameUtils.getExtension(uriPath) ;

		return result;
	}
	/**
	 * Export attachments to backup folder
	 *
	 * @return True if success, false otherwise
	 */
	public static boolean exportAttachments(File backupDir) {
		return exportAttachments(backupDir, null);
	}

	public static void exportSettingsScopedStorage(DocumentFile backupDir) {

		try
		{
			// create directory for settings
			DocumentFile settingsFolder = backupDir.createDirectory("Settings");

			File preferences = StorageHelper.getSharedPreferencesFile(Knizka.getAppContext());
			DocumentFile sourceFile = DocumentFile.fromFile(preferences);
			String fileName = StorageHelper.getFileName(Knizka.getAppContext(), sourceFile.getUri());
			DocumentFile destinationFile = settingsFolder.createFile(sourceFile.getType(), fileName);

			StorageHelper.copyDocumentFile(sourceFile, destinationFile);
		} catch (Exception e) {
			LogDelegate.e("Exception during settings backup: ", e);
		}

		return;
	}


	public static ExportImportResult exportAttachmentsScopedStorage(DocumentFile backupDir, NotificationsHelper notificationsHelper) {

		ExportImportResult result;

		// create directory for attachments
		DocumentFile attachmentFolder = backupDir.createDirectory("Attachments");

		ArrayList<Attachment> list = DbHelper.getInstance().getAllAttachments();

		result = exportAllAttachmentsScopedStorage(attachmentFolder, list, notificationsHelper);

		return result;
	}


	public static ExportImportResult exportAllAttachmentsScopedStorage(DocumentFile attachmentFolder, List<Attachment> list, NotificationsHelper notificationsHelper) {

		ExportImportResult result = new ExportImportResult();

		String failedString = "";

		for (Attachment attachment : list) {
			try {
				exportAttachmentScopedStorage(attachmentFolder, attachment);
				result.addSuccessfully();
			} catch (BackupAttachmentException e) {
				result.addFailed();
				failedString = " (" + result.getFailed() + " " + Knizka.getAppContext().getString(R.string.failed) + ")";
			}
			notifyAttachmentBackup(notificationsHelper, list, result.getSuccessfully(), failedString);
		}

		return result;
	}


	private static void exportAttachmentScopedStorage(DocumentFile attachmentFolder, Attachment attachment)
			throws BackupAttachmentException {
		try {

			String fileName = StorageHelper.getFileName(Knizka.getAppContext(), attachment.getUri());
			DocumentFile fileToBackup = DocumentFile.fromSingleUri(Knizka.getAppContext(), attachment.getUri());
			DocumentFile destinationFile = attachmentFolder.createFile(attachment.getMime_type(), fileName);

			if (StorageHelper.copyDocumentFile(fileToBackup, destinationFile) == false)
			{
				LogDelegate.e("Exception by copying file: " + attachment.getUri());
				throw new BackupAttachmentException();
			}
		} catch (Exception e) {
			LogDelegate.e("Error during attachment backup: " + attachment.getUriPath(), e);
			throw new BackupAttachmentException(e);
		}
	}


	/**
   * Export attachments to backup folder notifying for each attachment copied
   *
   * @return True if success, false otherwise
   */
  public static boolean exportAttachments(File backupDir, NotificationsHelper notificationsHelper) {
    File destinationattachmentsDir = new File(backupDir,
        StorageHelper.getAttachmentDir().getName());
    ArrayList<Attachment> list = DbHelper.getInstance().getAllAttachments();
    exportAttachments(notificationsHelper, destinationattachmentsDir, list, null);
    return true;
  }

  public static boolean exportAttachments(NotificationsHelper notificationsHelper,
      File destinationattachmentsDir, List<Attachment> list, List<Attachment> listOld) {
    boolean result = true;
    listOld = listOld == null ? Collections.emptyList() : listOld;
    int exported = 0;
    int failed = 0;
    String failedString = "";

    for (Attachment attachment : list) {
      try {
        exportAttachment(destinationattachmentsDir, attachment);
        ++exported;
      } catch (BackupAttachmentException e) {
        ++failed;
        result = false;
        failedString = " (" + failed + " " + Knizka.getAppContext().getString(R.string.failed) + ")";
      }

      notifyAttachmentBackup(notificationsHelper, list, exported, failedString);
    }

    Observable.from(listOld)
        .filter(attachment -> !list.contains(attachment))
        .forEach(attachment -> StorageHelper.delete(Knizka.getAppContext(), new File
            (destinationattachmentsDir.getAbsolutePath(),
                attachment.getUri().getLastPathSegment()).getAbsolutePath()));

    return result;
  }

  private static String notifyAttachmentBackup(NotificationsHelper notificationsHelper,
      List<Attachment> list, int exported, String failedString) {
  	 String notificationMessage = "";

  	 if (notificationsHelper != null) {
       notificationMessage =
          TextHelper.capitalize(Knizka.getAppContext().getString(R.string.attachment)) + " "
              + exported + "/" + list.size() + failedString;
      notificationsHelper.updateMessage(notificationMessage);
    }
	  return notificationMessage;
  }

	private static String notifyNoteBackup(NotificationsHelper notificationsHelper,
															 int AllNotes, int exported, String failedString) {
		String notificationMessage = "";

		if (notificationsHelper != null) {
			 notificationMessage =
					TextHelper.capitalize(Knizka.getAppContext().getString(R.string.note)) + " "
							+ exported + "/" + AllNotes + failedString;
			notificationsHelper.updateMessage(notificationMessage);
		}

		return notificationMessage;
	}

  private static void exportAttachment(File attachmentsDestination, Attachment attachment)
      throws BackupAttachmentException {
    try {
      StorageHelper.copyToBackupDir(attachmentsDestination,
          FilenameUtils.getName(attachment.getUriPath()),
          Knizka.getAppContext().getContentResolver().openInputStream(attachment.getUri()));
    } catch (Exception e) {
      LogDelegate.e("Error during attachment backup: " + attachment.getUriPath(), e);
      throw new BackupAttachmentException(e);
    }
  }

  public static List<Note> importNotes(File backupDir) {
    List<Note> notes = new ArrayList<>();
    for (File file : FileUtils
        .listFiles(backupDir, new RegexFileFilter("\\d{13}.json"), TrueFileFilter.INSTANCE)) {
      notes.add(importNote(file));
    }
    return notes;
  }

  public static Note importNote(File file) {
    Note note = getImportNote(file);
    if (note.getCategory() != null) {
      DbHelper.getInstance().updateCategory(note.getCategory());
    }
    if (Boolean.TRUE.equals(note.isLocked())) {
      note.setContent(Security.decrypt(note.getContent(), Prefs.getString(PREF_PASSWORD, "")));
    }
    DbHelper.getInstance().updateNote(note, false);
    return note;
  }


	/**
	 * Retrieves single note from its file
	 */
	public static Note getImportNote(File file) {
		try {
			Note note = new Note();
			String jsonString = FileUtils.readFileToString(file);
			if (!TextUtils.isEmpty(jsonString)) {
				note.buildFromJson(jsonString);
				//note.setAttachmentsListOld(DbHelper.getInstance().getNoteAttachments(note)); // @pk: bug? is this a bug? we check database while import looks ok, but in .updateNote ?
				//note.setAttachmentsListOld(new ArrayList<>(note.getAttachmentsList()));   //@pk: probably this will be the correct one, before .updateNote() we need set old attachments... otherwise  "deletedAttachments.remove(attachment)"; will not work correctly
			}
			return note;
		} catch (IOException e) {
			LogDelegate.e("Error parsing note json");
			return new Note();
		}
	}

  /**
   * Import attachments from backup folder notifying for each imported item
   */
  public static boolean importAttachments(File backupDir, NotificationsHelper notificationsHelper) {
    AtomicBoolean result = new AtomicBoolean(true);
    File attachmentsDir = StorageHelper.getAttachmentDir();
    File backupAttachmentsDir = new File(backupDir, attachmentsDir.getName());
    if (!backupAttachmentsDir.exists()) {
      return false;
    }

    AtomicInteger imported = new AtomicInteger();
    ArrayList<Attachment> attachments = DbHelper.getInstance().getAllAttachments();
    rx.Observable.from(attachments)
        .forEach(attachment -> {
          try {
            importAttachment(backupAttachmentsDir, attachmentsDir, attachment);
            if (notificationsHelper != null) {
              notificationsHelper.updateMessage(TextHelper.capitalize(Knizka.getAppContext().getString(R.string.attachment)) + " "
                      + imported.incrementAndGet() + "/" + attachments.size());
            }
          } catch (BackupAttachmentException e) {
            result.set(false);
          }
        });
    return result.get();
  }

  static void importAttachment(File backupAttachmentsDir, File attachmentsDir,
      Attachment attachment)
      throws BackupAttachmentException {
    try {
      File attachmentFile = new File(backupAttachmentsDir.getAbsolutePath(),
          attachment.getUri().getLastPathSegment());
      FileUtils.copyFileToDirectory(attachmentFile, attachmentsDir, true);
    } catch (Exception e) {
      LogDelegate.e("Error importing the attachment " + attachment.getUri().getPath(), e);
      throw new BackupAttachmentException(e);
    }
  }


	/**
	 * Starts backup service
	 * @param backupFolderName subfolder of the app's external sd folder where notes will be stored
	 */
	public static void startBackupService(String backupFolderName) {
		Intent service = new Intent(Knizka.getAppContext(), DataBackupIntentService.class);
		service.setAction(DataBackupIntentService.ACTION_DATA_EXPORT);
		service.putExtra(DataBackupIntentService.INTENT_BACKUP_NAME, backupFolderName);
		Knizka.getAppContext().startService(service);
	}

	/**
	 * Starts backup service for android versions above 9
	 * @param backupFolderName folder which was chosen by user
	 */
	public static void startBackupServiceScopedStorage(Uri backupFolderName) {
		Intent service = new Intent(Knizka.getAppContext(), DataBackupIntentScopedStorageService.class);
		service.setAction(DataBackupIntentScopedStorageService.ACTION_DATA_EXPORT_SCOPED_STORAGE);
		service.putExtra(DataBackupIntentScopedStorageService.INTENT_BACKUP_URI, backupFolderName.toString());
		Knizka.getAppContext().startService(service);
	}

	/**
	 * Starts backup service
	 * @param backupFolderName subfolder of the app's external sd folder where notes will be stored
	 */
	public static void startRAWBackupService(String backupFolderName) {
		Intent service = new Intent(Knizka.getAppContext(), DataBackupIntentService.class);
		service.setAction(DataBackupIntentService.ACTION_DATA_EXPORT_RAW_DATABASE);
		service.putExtra(DataBackupIntentService.INTENT_BACKUP_NAME, backupFolderName);
		Knizka.getAppContext().startService(service);
	}

	/**
	 * Exports settings if required
	 */
	public static boolean exportSettings(File backupDir) {
		File preferences = StorageHelper.getSharedPreferencesFile(Knizka.getAppContext());
		return (StorageHelper.copyFile(preferences, new File(backupDir, preferences.getName())));
	}


	/**
	 * Imports settings
	 */
	public static boolean importSettings(File backupDir) {
		File preferences = StorageHelper.getSharedPreferencesFile(Knizka.getAppContext());
		File preferenceBackup = new File(backupDir, preferences.getName());
		return (StorageHelper.copyFile(preferenceBackup, preferences));
	}


	public static boolean deleteNoteBackup(File backupDir, Note note) {
		File noteFile = getBackupNoteFile(backupDir, note);
		boolean result = noteFile.delete();
		File attachmentBackup = new File(backupDir, StorageHelper.getAttachmentDir().getName());
		for (Attachment attachment : note.getAttachmentsList()) {
			result = result && new File(attachmentBackup, FilenameUtils.getName(attachment.getUri().getPath()))
					.delete();
		}
		return result;
	}


	public static void deleteNote(File file) {
		try {
			Note note = new Note();
			note.buildFromJson(FileUtils.readFileToString(file));
			DbHelper.getInstance().deleteNote(note);
		} catch (IOException e) {
			LogDelegate.e("Error parsing note json");
		}
	}


	/**
	 * Import database from backup folder. Used ONLY to restore legacy backup
	 *
	 */
	public static boolean importDB(Context context, File backupDir) {
	//	File database = context.getDatabasePath(DATABASE_NAME);
		boolean result = false;
		File database = context.getDatabasePath(DB_ATTACHED);
		File database_user = context.getDatabasePath(DB_USER_DATA);

		DbHelper.closeDB();

		if (database.exists()) {
			database.delete();
		}
		if (database_user.exists()) {
			database_user.delete();
		}
		result = StorageHelper.copyFile(new File(backupDir, DB_ATTACHED), database);
		result = result && StorageHelper.copyFile(new File(backupDir, DB_USER_DATA), database_user);

		DbHelper.forceOpenDB();

		return result;

	}


	public static List<LinkedList<DiffMatchPatch.Diff>> integrityCheck(File backupDir) {
		List<LinkedList<DiffMatchPatch.Diff>> errors = new ArrayList<>();
		for (Note note : DbHelper.getInstance(true).getAllNotes(false)) {
			File noteFile = getBackupNoteFile(backupDir, note);
			try {
				String noteString = note.toJSON();
				String noteFileString = FileUtils.readFileToString(noteFile);
				if (noteString.equals(noteFileString)) {
					File backupAttachmentsDir = new File(backupDir, StorageHelper.getAttachmentDir().getName());
					for (Attachment attachment : note.getAttachmentsList()) {
						if (!new File(backupAttachmentsDir, FilenameUtils.getName(attachment.getUriPath())).exists()) {
							addIntegrityCheckError(errors, new FileNotFoundException("Attachment " + attachment
									.getUriPath() + " missing"));
						}
					}
				} else {
					errors.add(new DiffMatchPatch().diffMain(noteString, noteFileString));
				}
			} catch (IOException e) {
				LogDelegate.e(e.getMessage(), e);
				addIntegrityCheckError(errors, e);
			}
		}
		return errors;
	}

  private static void addIntegrityCheckError(List<LinkedList<DiffMatchPatch.Diff>> errors,
      IOException e) {
    LinkedList<DiffMatchPatch.Diff> l = new LinkedList<>();
    l.add(new DiffMatchPatch.Diff(DiffMatchPatch.Operation.DELETE, e.getMessage()));
    errors.add(l);
  }

}
