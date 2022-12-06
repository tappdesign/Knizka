package pk.tappdesign.knizka.async;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.List;

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.SnoozeActivity;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.utils.ReminderHelper;
import pk.tappdesign.knizka.utils.date.DateUtils;

public class RestoreRemindersTask extends AsyncTask<Void, Void, Void> {
   @Override
   protected Void doInBackground(Void... voids) {

      List<Note> notes = DbHelper.getInstance().getNotesWithReminderNotFired();
      LogDelegate.d("Found " + notes.size() + " reminders");

      for (Note note : notes) {
         ReminderHelper.removeReminder(Knizka.getAppContext(), note);
         if (!DateUtils.isFuture(note.getAlarm())) {
            if (!TextUtils.isEmpty(note.getRecurrenceRule())) {
               SnoozeActivity.setNextRecurrentReminder(note, false);
            }
         }

         ReminderHelper.addReminder(Knizka.getAppContext(), note);
      }


      return null;
   }
}
