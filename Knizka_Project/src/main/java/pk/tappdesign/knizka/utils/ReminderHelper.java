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

package pk.tappdesign.knizka.utils;

import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_NOTE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.helpers.date.DateHelper;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.receiver.AlarmReceiver;
import pk.tappdesign.knizka.utils.date.DateUtils;

import java.util.Calendar;


public class ReminderHelper {

	private ReminderHelper() {
		// hides public constructor
	}

	public static void addReminder(Context context, Note note) {
		if (note.getAlarm() != null) {
			addReminder(context, note, Long.parseLong(note.getAlarm()));
		}
	}


	public static void addReminder(Context context, Note note, long reminder) {
		if (DateUtils.isFuture(reminder)) {
			Intent intent = new Intent(context, AlarmReceiver.class);
			intent.putExtra(INTENT_NOTE, ParcelableUtil.marshall(note));
			PendingIntent sender = PendingIntent.getBroadcast(context, getRequestCode(note), intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			am.setExact(AlarmManager.RTC_WAKEUP, reminder, sender);
		}
	}


	/**
	 * Checks if exists any reminder for given note
	 */
	public static boolean checkReminder(Context context, Note note) {
		return PendingIntent.getBroadcast(context, getRequestCode(note), new Intent(context, AlarmReceiver
				.class), PendingIntent.FLAG_NO_CREATE) != null;
	}


	static int getRequestCode(Note note) {
		long longCode = note.getCreation() != null ? note.getCreation() : Calendar.getInstance().getTimeInMillis() / 1000L;
		return (int) longCode;
	}


	public static void removeReminder(Context context, Note note) {
		if (!TextUtils.isEmpty(note.getAlarm())) {
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(context, AlarmReceiver.class);
			PendingIntent p = PendingIntent.getBroadcast(context, getRequestCode(note), intent, 0);
			if (p != null)
			{
				am.cancel(p);
				p.cancel();
			}
		}
	}

  public static void showReminderMessage (String reminderString) {
    if (reminderString != null) {
      long reminder = Long.parseLong(reminderString);
      if (reminder > Calendar.getInstance().getTimeInMillis()) {
        new Handler(Knizka.getAppContext().getMainLooper()).post(() ->
            Toast.makeText(Knizka.getAppContext(),
            Knizka.getAppContext().getString(R.string.alarm_set_on) + " " + DateHelper.getDateTimeShort
                (Knizka.getAppContext(), reminder), Toast.LENGTH_LONG).show());
      }
    }
  }

}
