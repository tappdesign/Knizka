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

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.List;

import pk.tappdesign.knizka.BaseActivity;
import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.utils.ReminderHelper;


public class AlarmRestoreOnRebootService extends JobIntentService {

	public static final int JOB_ID = 0x01;

	public static void enqueueWork(Context context, Intent work) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			enqueueWork(context, AlarmRestoreOnRebootService.class, JOB_ID, work);
		} else {
			Intent jobIntent = new Intent(context, AlarmRestoreOnRebootService.class);
			context.startService(jobIntent);
		}
	}

	@Override
	protected void onHandleWork(@NonNull Intent intent) {
		LogDelegate.i("System rebooted: service refreshing reminders");
		Context mContext = getApplicationContext();

		BaseActivity.notifyAppWidgets(mContext);

		List<Note> notes = DbHelper.getInstance().getNotesWithReminderNotFired();
		LogDelegate.d("Found " + notes.size() + " reminders");
		for (Note note : notes) {
			ReminderHelper.addReminder(Knizka.getAppContext(), note);
		}
	}

}
