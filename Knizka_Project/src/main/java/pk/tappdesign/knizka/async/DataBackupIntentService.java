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

package pk.tappdesign.knizka.async;


import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_RESTART_APP;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.pixplicity.easyprefs.library.Prefs;

import pk.tappdesign.knizka.MainActivity;
import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.BackupHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.helpers.SpringImportHelper;
import pk.tappdesign.knizka.helpers.notifications.NotificationChannels.NotificationChannelNames;
import pk.tappdesign.knizka.helpers.notifications.NotificationsHelper;
import pk.tappdesign.knizka.models.Attachment;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.listeners.OnAttachingFileListener;
import pk.tappdesign.knizka.utils.ReminderHelper;
import pk.tappdesign.knizka.utils.StorageHelper;
import java.io.File;

public class DataBackupIntentService extends IntentService implements OnAttachingFileListener {

    public static final String INTENT_BACKUP_NAME = "backup_name";
    public static final String INTENT_BACKUP_INCLUDE_SETTINGS = "backup_include_settings";
    public static final String ACTION_DATA_EXPORT = "action_data_export_scoped_storage";
   public static final String ACTION_DATA_EXPORT_RAW_DATABASE = "action_data_export_raw_database";
    public static final String ACTION_DATA_IMPORT = "action_data_import";
    public static final String ACTION_DATA_IMPORT_LEGACY = "action_data_import_legacy";
    public static final String ACTION_DATA_DELETE = "action_data_delete";

    private NotificationsHelper mNotificationsHelper;

//    {
//        File autoBackupDir = StorageHelper.getBackupDir(Constants.AUTO_BACKUP_DIR);
//        BackupHelper.exportNotes(autoBackupDir);
//        BackupHelper.exportAttachments(autoBackupDir);
//    }


    public DataBackupIntentService() {
        super("DataBackupIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mNotificationsHelper = new NotificationsHelper(this).start(NotificationChannelNames.BACKUPS,
                R.drawable.ic_content_save_white_24dp, getString(R.string.working));


        // If an alarm has been fired a notification must be generated
        if (ACTION_DATA_EXPORT.equals(intent.getAction())) {
            exportData(intent);
        } else if (ACTION_DATA_EXPORT_RAW_DATABASE.equals(intent.getAction()) ) {
           exportDataRawDatabase(intent);
        } else if (ACTION_DATA_IMPORT.equals(intent.getAction()) || ACTION_DATA_IMPORT_LEGACY.equals(intent.getAction())) {
            importData(intent);
        } else if (SpringImportHelper.ACTION_DATA_IMPORT_SPRINGPAD.equals(intent.getAction())) {
            importDataFromSpringpad(intent, mNotificationsHelper);
        } else if (ACTION_DATA_DELETE.equals(intent.getAction())) {
            deleteData(intent);
        }
    }

	private void importDataFromSpringpad(Intent intent, NotificationsHelper mNotificationsHelper) {
		new SpringImportHelper(Knizka.getAppContext()).importDataFromSpringpad(intent, mNotificationsHelper);
		String title = getString(R.string.data_import_completed);
		String text = getString(R.string.click_to_refresh_application);
		createNotification(intent, this, title, text, null);
	}

  private synchronized void exportData(Intent intent) {

    boolean result = true;

    // Gets backup folder
    String backupName = intent.getStringExtra(INTENT_BACKUP_NAME);
    File backupDir = StorageHelper.getOrCreateBackupDir(backupName);

    // Directory clean in case of previously used backup name
    StorageHelper.delete(this, backupDir.getAbsolutePath());

    // Directory is re-created in case of previously used backup name (removed above)
    backupDir = StorageHelper.getOrCreateBackupDir(backupName);

    BackupHelper.exportNotes(backupDir);

    result = BackupHelper.exportAttachments(backupDir, mNotificationsHelper);

        if (intent.getBooleanExtra(INTENT_BACKUP_INCLUDE_SETTINGS, true)) {
			BackupHelper.exportSettings(backupDir);
        }

        String notificationMessage = result ? getString(R.string.data_export_completed) : getString(R.string.data_export_failed);
        mNotificationsHelper.finish(intent, notificationMessage);
    }


   private synchronized void exportDataRawDatabase(Intent intent) {

      boolean result = true;

      // Gets backup folder
      String backupName = intent.getStringExtra(INTENT_BACKUP_NAME);
      File backupDir = StorageHelper.getOrCreateBackupDir(backupName);

      // Directory clean in case of previously used backup name
      StorageHelper.delete(this, backupDir.getAbsolutePath());

      // Directory is re-created in case of previously used backup name (removed above)
      backupDir = StorageHelper.getOrCreateBackupDir(backupName);

      BackupHelper.exportRawDB(this, backupDir);

      result = BackupHelper.exportAttachments(backupDir, mNotificationsHelper);

      String notificationMessage = result ? getString(R.string.data_export_completed) : getString(R.string.data_export_failed);
      mNotificationsHelper.finish(intent, notificationMessage);
   }



    private synchronized void importData(Intent intent) {

		boolean importLegacy = ACTION_DATA_IMPORT_LEGACY.equals(intent.getAction());

    // Gets backup folder
    String backupName = intent.getStringExtra(INTENT_BACKUP_NAME);
    File backupDir = importLegacy ? new File(backupName) : StorageHelper.getOrCreateBackupDir(backupName);

        BackupHelper.importSettings(backupDir);

		if (importLegacy) {
			BackupHelper.importDB(this, backupDir);
		} else {
			BackupHelper.importNotes(backupDir);
		}

		BackupHelper.importAttachments(backupDir, mNotificationsHelper);

		resetReminders();

		mNotificationsHelper.cancel();

    createNotification(intent, this, getString(R.string.data_import_completed),
        getString(R.string.click_to_refresh_application), backupDir);

        // Performs auto-backup filling after backup restore
//        if (prefs.getBoolean(Constants.PREF_ENABLE_AUTOBACKUP, false)) {
//            File autoBackupDir = StorageHelper.getBackupDir(Constants.AUTO_BACKUP_DIR);
//            BackupHelper.exportNotes(autoBackupDir);
//            BackupHelper.exportAttachments(autoBackupDir);
//        }
	}

    private synchronized void deleteData(Intent intent) {

    // Gets backup folder
    String backupName = intent.getStringExtra(INTENT_BACKUP_NAME);
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
        if (DataBackupIntentService.ACTION_DATA_IMPORT.equals(intent.getAction())
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
//        PendingIntent notifyIntent = PendingIntent.getActivity(mContext, 0, intentLaunch, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent notifyIntent = NotificationsHelper.createPendingIntent(mContext, 0, intentLaunch, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationsHelper notificationsHelper = new NotificationsHelper(mContext);
        notificationsHelper.createStandardNotification(NotificationChannelNames.BACKUPS, R.drawable.ic_content_save_white_24dp, title, notifyIntent)
                .setMessage(message).setRingtone(Prefs.getString("settings_notification_ringtone", null))
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


    @Override
    public void onAttachingFileErrorOccurred(Attachment mAttachment) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onAttachingFileFinished(Attachment mAttachment) {
        // TODO Auto-generated method stub
    }

}
