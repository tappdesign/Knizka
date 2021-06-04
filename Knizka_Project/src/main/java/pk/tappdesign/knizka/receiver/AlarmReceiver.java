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
package pk.tappdesign.knizka.receiver;

import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_POSTPONE;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_SNOOZE;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_NOTE;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_FILES;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spanned;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.SnoozeActivity;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.IntentHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.helpers.notifications.NotificationChannels.NotificationChannelNames;
import pk.tappdesign.knizka.helpers.notifications.NotificationsHelper;
import pk.tappdesign.knizka.models.Attachment;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.services.NotificationListener;
import pk.tappdesign.knizka.utils.BitmapHelper;
import pk.tappdesign.knizka.utils.ParcelableUtil;
import pk.tappdesign.knizka.utils.TextHelper;
import pk.tappdesign.knizka.helpers.notifications.NotificationChannels.NotificationChannelNames;
import pk.tappdesign.knizka.helpers.notifications.NotificationsHelper;


public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context mContext, Intent intent) {
		try {
			if (intent.hasExtra(INTENT_NOTE)) {
				Note note = ParcelableUtil.unmarshall(intent.getExtras().getByteArray(INTENT_NOTE), Note
						.CREATOR);
				createNotification(mContext, note);
				SnoozeActivity.setNextRecurrentReminder(note);
				updateNote(note);
			}
		} catch (Exception e) {
			LogDelegate.e("Error on receiving reminder", e);
		}
	}

	private void updateNote(Note note) {
		note.setArchived(false);
		if (!NotificationListener.isRunning()) {
			note.setReminderFired(true);
		}
		DbHelper.getInstance().updateNote(note, false);
	}

  private void createNotification (Context mContext, Note note) {

    PendingIntent piSnooze = IntentHelper.getNotePendingIntent(mContext, SnoozeActivity.class, ACTION_SNOOZE, note);
    PendingIntent piPostpone = IntentHelper.getNotePendingIntent(mContext, SnoozeActivity.class, ACTION_POSTPONE, note);
    PendingIntent notifyIntent = IntentHelper.getNotePendingIntent(mContext, SnoozeActivity.class, null, note);

    Spanned[] titleAndContent = TextHelper.parseTitleAndContent(mContext, note);
    String title = TextHelper.getAlternativeTitle(mContext, note, titleAndContent[0]);
    String text = titleAndContent[1].toString();

    NotificationsHelper notificationsHelper = new NotificationsHelper(mContext);
    notificationsHelper.createStandardNotification(NotificationChannelNames.REMINDERS, R.drawable.ic_stat_notification,
        title, notifyIntent).setLedActive().setMessage(text);

    List<Attachment> attachments = note.getAttachmentsList();
    if (!attachments.isEmpty() && !attachments.get(0).getMime_type().equals(MIME_TYPE_FILES)) {
      Bitmap notificationIcon = BitmapHelper.getBitmapFromAttachment(mContext, note.getAttachmentsList().get(0), 128,
          128);
      notificationsHelper.setLargeIcon(notificationIcon);
    }

    String snoozeDelay = Prefs.getString(
        "settings_notification_snooze_delay", "10");

    notificationsHelper.getBuilder()
                       .addAction(R.drawable.ic_material_reminder_time_light,
                           TextHelper.capitalize(mContext.getString(R.string.snooze)) + ": " + snoozeDelay, piSnooze)
                       .addAction(R.drawable.ic_remind_later_light, TextHelper.capitalize(mContext.getString(R.string
                           .add_reminder)), piPostpone);

		setRingtone(notificationsHelper);
		setVibrate(notificationsHelper);

		notificationsHelper.show(note.get_id());
	}


	private void setRingtone(NotificationsHelper notificationsHelper) {
		String ringtone = Prefs.getString("settings_notification_ringtone", null);
		notificationsHelper.setRingtone(ringtone);
	}


  private void setVibrate ( NotificationsHelper notificationsHelper) {
    if (Prefs.getBoolean("settings_notification_vibration", true)) {
      notificationsHelper.setVibration();
    }
  }
}
