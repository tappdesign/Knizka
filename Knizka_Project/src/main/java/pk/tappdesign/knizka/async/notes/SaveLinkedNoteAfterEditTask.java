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

package pk.tappdesign.knizka.async.notes;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import de.greenrobot.event.EventBus;
import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.async.bus.NotesUpdatedEvent;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.NoteLink;
import pk.tappdesign.knizka.models.listeners.OnLinkedNoteAdded;
import pk.tappdesign.knizka.models.listeners.OnNoteSaved;

public class SaveLinkedNoteAfterEditTask extends AsyncTask<Note, Void, Note> {

   private Context context;
   private OnNoteSaved mOnNoteSaved;
   private List<NoteLink> noteList;

   public SaveLinkedNoteAfterEditTask(List<NoteLink> noteList) {
      this(null, noteList);
   }


   public SaveLinkedNoteAfterEditTask(OnNoteSaved mOnNoteSaved, List<NoteLink> noteList) {
      super();
      this.context = Knizka.getAppContext();
      this.mOnNoteSaved = mOnNoteSaved;
      this.noteList = noteList;
   }


   @Override
   protected Note doInBackground(Note... params) {
      Note note = params[0];

      if (noteList != null)
      {
         DbHelper.getInstance().deleteNoteFromPrayerSets(note.getHandleID());
         DbHelper.getInstance().insertLinkedNotesToPrayerSet(noteList, note.getHandleID());
      }

      DbHelper.getInstance().updatePrayerSetNoteContent(note, note.getHandleID());

      return note;
   }

   @Override
   protected void onPostExecute(Note note) {
      super.onPostExecute(note);

      if (this.mOnNoteSaved != null) {
         mOnNoteSaved.onNoteSaved(note);
      }

      EventBus.getDefault().post(new NotesUpdatedEvent(null)); // here refresh navigation drawer
   }

}
