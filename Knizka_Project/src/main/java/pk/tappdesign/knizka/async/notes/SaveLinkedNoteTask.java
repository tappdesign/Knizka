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

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.listeners.OnLinkedNoteAdded;

public class SaveLinkedNoteTask extends AsyncTask<Long, Void, Void> {

   private Context context;
   private OnLinkedNoteAdded mOnNoteSaved;
   private List<Note> noteList;

   public SaveLinkedNoteTask(List<Note> noteList) {
      this(null, noteList);
   }


   public SaveLinkedNoteTask(OnLinkedNoteAdded mOnNoteSaved, List<Note> noteList) {
      super();
      this.context = Knizka.getAppContext();
      this.mOnNoteSaved = mOnNoteSaved;
      this.noteList = noteList;
   }


   @Override
   protected Void doInBackground(Long... params) {
      Long noteID = params[0];

      DbHelper.getInstance().addNotesToPrayerSet(noteList, noteID);
      DbHelper.getInstance().updatePrayerSetNoteContent(noteID);
      return null;
   }





   @Override
   protected void onPostExecute(Void note) {
      super.onPostExecute(note);
      if (this.mOnNoteSaved != null) {

         mOnNoteSaved.onLinkedNoteAdded();
      }
   }
}
