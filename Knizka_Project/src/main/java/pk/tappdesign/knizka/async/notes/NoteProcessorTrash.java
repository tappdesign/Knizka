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

import java.util.List;

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.utils.ReminderHelper;
import pk.tappdesign.knizka.utils.ShortcutHelper;


public class NoteProcessorTrash extends NoteProcessor {

    boolean trash;


    public NoteProcessorTrash(List<Note> notes, boolean trash) {
        super(notes);
        this.trash = trash;
    }


    @Override
    protected void processNote(Note note) {
        if (trash) {
            ShortcutHelper.removeShortcut(Knizka.getAppContext(), note);
            ReminderHelper.removeReminder(Knizka.getAppContext(), note);
        } else {
            ReminderHelper.addReminder(Knizka.getAppContext(), note);
        }
        DbHelper.getInstance().trashNote(note, trash);
    }
}
