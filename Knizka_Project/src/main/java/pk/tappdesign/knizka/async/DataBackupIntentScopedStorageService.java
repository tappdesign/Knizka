package pk.tappdesign.knizka.async;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.MainActivity;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.BackupHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.helpers.SpringImportHelper;
import pk.tappdesign.knizka.helpers.notifications.NotificationChannels;
import pk.tappdesign.knizka.helpers.notifications.NotificationsHelper;
import pk.tappdesign.knizka.models.Attachment;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.data.ExportImportResult;
import pk.tappdesign.knizka.models.listeners.OnAttachingFileListener;
import pk.tappdesign.knizka.utils.KeyboardUtils;
import pk.tappdesign.knizka.utils.ReminderHelper;
import pk.tappdesign.knizka.utils.StorageHelper;
import pk.tappdesign.knizka.utils.TextHelper;

import static pk.tappdesign.knizka.db.DBConst.DB_ATTACHED;
import static pk.tappdesign.knizka.db.DBConst.DB_USER_DATA;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_RESTART_APP;
import static pk.tappdesign.knizka.utils.ConstantsBase.DATE_FORMAT_EXPORT;

import com.pixplicity.easyprefs.library.Prefs;

public class DataBackupIntentScopedStorageService  extends IntentService implements OnAttachingFileListener {

   public static final String INTENT_BACKUP_URI = "backup_name";
   public static final String INTENT_BACKUP_INCLUDE_SETTINGS = "backup_include_settings";
   public static final String ACTION_DATA_EXPORT_SCOPED_STORAGE = "action_data_export";
   public static final String ACTION_DATA_EXPORT_RAW_DATABASE = "action_data_export_raw_database";
   public static final String ACTION_DATA_IMPORT_SCOPED_STORAGE = "action_data_import_scoped_storage";
   public static final String ACTION_DATA_IMPORT_RAW_SCOPED_STORAGE = "action_data_import_legacy";
   public static final String ACTION_DATA_DELETE = "action_data_delete";

   private NotificationsHelper mNotificationsHelper;


   public DataBackupIntentScopedStorageService() {
      super("DataBackupIntentScopedStorageService");
   }

   @Override
   protected void onHandleIntent(Intent intent) {

      mNotificationsHelper = new NotificationsHelper(this).start(NotificationChannels.NotificationChannelNames.BACKUPS,
              R.drawable.ic_content_save_white_24dp, getNotificationText(intent));


      // If an alarm has been fired a notification must be generated
      if (ACTION_DATA_EXPORT_SCOPED_STORAGE.equals(intent.getAction())) {
         exportData(intent);
      } else if (ACTION_DATA_EXPORT_RAW_DATABASE.equals(intent.getAction()) ) {
         exportDataRawDatabase(intent);
      } else if (ACTION_DATA_IMPORT_SCOPED_STORAGE.equals(intent.getAction()) ) {
         importData(intent);
      } else if (ACTION_DATA_IMPORT_RAW_SCOPED_STORAGE.equals(intent.getAction())) {
         importRAWDBData(intent);
      } else if (SpringImportHelper.ACTION_DATA_IMPORT_SPRINGPAD.equals(intent.getAction())) {
         importDataFromSpringpad(intent, mNotificationsHelper);
      } else if (ACTION_DATA_DELETE.equals(intent.getAction())) {
         deleteData(intent);
      }
   }

   private String getNotificationText(Intent intent)
   {
      String result = getString(R.string.working);

      if (ACTION_DATA_EXPORT_SCOPED_STORAGE.equals(intent.getAction()) || ACTION_DATA_EXPORT_RAW_DATABASE.equals(intent.getAction()) ) {
        result = getString(R.string.working_export);
      } else if (ACTION_DATA_IMPORT_SCOPED_STORAGE.equals(intent.getAction())) {
         result = getString(R.string.working_import);
      }

      return result;
   }

   private void importDataFromSpringpad(Intent intent, NotificationsHelper mNotificationsHelper) {
      new SpringImportHelper(Knizka.getAppContext()).importDataFromSpringpad(intent, mNotificationsHelper);
      String title = getString(R.string.data_import_completed);
      String text = getString(R.string.click_to_refresh_application);
      createNotification(intent, this, title, text, null);
   }

   private synchronized void exportData(Intent intent) {

      // Gets backup folder
      Uri baseBackupDir = Uri.parse(intent.getStringExtra(INTENT_BACKUP_URI));

      // Create subfolder with date and time
      SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_EXPORT);
      String folderName = sdf.format(Calendar.getInstance().getTime());
      DocumentFile documentDir = DocumentFile.fromTreeUri(Knizka.getAppContext(), baseBackupDir);
      DocumentFile folderWithDateTime = documentDir.createDirectory(folderName);

      // export notes
      ExportImportResult  exportNotesResult = BackupHelper.exportNotesScopedStorage(folderWithDateTime, mNotificationsHelper);

      // export attachments
      ExportImportResult exportAttachmentsResult = BackupHelper.exportAttachmentsScopedStorage(folderWithDateTime, mNotificationsHelper);

      // export settings
      BackupHelper.exportSettingsScopedStorage(folderWithDateTime);

      String notificationMessage =  getString(R.string.exported_texts) + ": " + exportNotesResult.getSuccessfully() + "/" + exportNotesResult.getAll()  +
              ", " + getString(R.string.exported_attachments) + ": " + exportAttachmentsResult.getSuccessfully() + "/" + exportAttachmentsResult.getAll() ;

      // Some delay required, otherwise notification may be not correctly updated after export is finished
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      mNotificationsHelper.finishScopedStorageExport(intent, getString(R.string.data_export_completed), notificationMessage);
   }


   private synchronized void exportDataRawDatabase(Intent intent) {

      // Gets backup folder
      Uri baseBackupDir = Uri.parse(intent.getStringExtra(INTENT_BACKUP_URI));

      DocumentFile folderWithDateTime = createDirectoryWithTimestamp(baseBackupDir);

      // export database files
      BackupHelper.exportRawDBScopedStorage(this, folderWithDateTime);

      // export attachments
      BackupHelper.exportAttachmentsScopedStorage(folderWithDateTime, mNotificationsHelper);

      // export settings
      BackupHelper.exportSettingsScopedStorage(folderWithDateTime);

      // Some delay required, otherwise notification may be not correctly updated after export is finished
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      mNotificationsHelper.finishScopedStorageExport(intent, getString(R.string.data_export_completed), "");
   }

   private synchronized boolean copyRAWDBFile(String databaseFileName, DocumentFile databaseDocuFile)
   {
      boolean result = false;

      File database = Knizka.getAppContext().getDatabasePath(databaseFileName);
      if (database.exists())
      {
         DocumentFile destinationFile = DocumentFile.fromFile(database);

         // first delete old database file
         if (database.delete())
         {
            StorageHelper.copyDocumentFile(databaseDocuFile, destinationFile);
            result = true;
         }
      }

      return result;
   }

   private synchronized void importRAWDBData(Intent intent) {

      // Gets backup folder
      Uri baseBackupDir = Uri.parse(intent.getStringExtra(INTENT_BACKUP_URI));
      boolean importsuccess = true;
      DocumentFile documentDir = DocumentFile.fromTreeUri(Knizka.getAppContext(), baseBackupDir);

      for(DocumentFile databaseDocuFile:documentDir.listFiles()){

            if (databaseDocuFile.getName().equalsIgnoreCase(DB_ATTACHED)) {
               importsuccess = importsuccess | copyRAWDBFile(DB_ATTACHED, databaseDocuFile);
            }
            if (databaseDocuFile.getName().equalsIgnoreCase(DB_USER_DATA)) {
               importsuccess = importsuccess | copyRAWDBFile(DB_USER_DATA, databaseDocuFile);
            }
      }

      // todo: no settings import is implemented now, maybe later
      //BackupHelper.importSettings(backupDir);

      BackupHelper.importAttachmentsScopedStorage(documentDir, mNotificationsHelper);

      resetReminders();

      mNotificationsHelper.cancel();

      createNotification(intent, this, importsuccess ? getString(R.string.data_import_completed) : getString(R.string.data_import_failed), getString(R.string.click_to_refresh_application), null);

   }

   private synchronized void importData(Intent intent) {

      // Gets backup folder
      Uri baseBackupDir = Uri.parse(intent.getStringExtra(INTENT_BACKUP_URI));

      // todo: no settings import is implemented now, maybe later
      //BackupHelper.importSettings(backupDir);

      DocumentFile documentDir = DocumentFile.fromTreeUri(Knizka.getAppContext(), baseBackupDir);
      BackupHelper.importNotesScopedStorage(documentDir, mNotificationsHelper);

      BackupHelper.importAttachmentsScopedStorage(documentDir, mNotificationsHelper);

      resetReminders();

      mNotificationsHelper.cancel();

      createNotification(intent, this, getString(R.string.data_import_completed), getString(R.string.click_to_refresh_application), null);

   }

   private synchronized void deleteData(Intent intent) {

      // Gets backup folder
      String backupName = intent.getStringExtra(INTENT_BACKUP_URI);
      File backupDir = StorageHelper.getOrCreateBackupDir(backupName);

      // Backups directory removal
      StorageHelper.delete(this, backupDir.getAbsolutePath());

      String title = getString(R.string.data_deletion_completed);
      String text = backupName + " " + getString(R.string.deleted);
      createNotification(intent, this, title, text, backupDir);
   }


   /**
    * Creation of notification on operations completed
    */
   private void createNotification(Intent intent, Context mContext, String title, String message,
                                   File backupDir) {

      // The behavior differs depending on intent action
      Intent intentLaunch;
      if (DataBackupIntentScopedStorageService.ACTION_DATA_IMPORT_SCOPED_STORAGE.equals(intent.getAction())
              || SpringImportHelper.ACTION_DATA_IMPORT_SPRINGPAD.equals(intent.getAction())) {
         intentLaunch = new Intent(mContext, MainActivity.class);
         intentLaunch.setAction(ACTION_RESTART_APP);
      } else {
         intentLaunch = new Intent();
      }
      // Add this bundle to the intent
      intentLaunch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intentLaunch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      // Creates the PendingIntent
      PendingIntent notifyIntent = PendingIntent.getActivity(mContext, 0, intentLaunch,
              PendingIntent.FLAG_UPDATE_CURRENT);

      NotificationsHelper notificationsHelper = new NotificationsHelper(mContext);
      notificationsHelper.createStandardNotification(NotificationChannels.NotificationChannelNames.BACKUPS, R.drawable.ic_content_save_white_24dp, title, notifyIntent)
              .setMessage(message)
              .setRingtone(Prefs.getString("settings_notification_ringtone", null))
              .setLedActive();
      if (Prefs.getBoolean("settings_notification_vibration", true)) notificationsHelper.setVibration();
      notificationsHelper.show();
   }


   /**
    * Schedules reminders
    */
   private void resetReminders() {
      LogDelegate.d("Resettings reminders");
      for (Note note : DbHelper.getInstance().getNotesWithReminderNotFired()) {
         ReminderHelper.addReminder(Knizka.getAppContext(), note);
      }
   }

   private DocumentFile createDirectoryWithTimestamp(Uri baseBackupDir)
   {
      // Create subfolder with date and time
      SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_EXPORT);
      String folderName = sdf.format(Calendar.getInstance().getTime());

      DocumentFile documentDir = DocumentFile.fromTreeUri(Knizka.getAppContext(), baseBackupDir);
      return documentDir.createDirectory(folderName);
   }

   @Override
   public void onAttachingFileErrorOccurred(Attachment mAttachment) {
      // TODO Auto-generated method stub
   }


   @Override
   public void onAttachingFileFinished(Attachment mAttachment) {
      // TODO Auto-generated method stub
   }

}

