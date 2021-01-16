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

package pk.tappdesign.knizka.helpers.count;

import java.util.regex.Pattern;

import pk.tappdesign.knizka.models.Note;

public interface WordCounter {

    int countWords(Note note);

    int countChars(Note note);

    default String sanitizeTextForWordsAndCharsCount(Note note, String field) {
        if (note.isChecklist()) {
            String regex = "(" + Pattern.quote(it.feio.android.checklistview.interfaces.Constants.CHECKED_SYM) + "|"
                    + Pattern.quote(it.feio.android.checklistview.interfaces.Constants.UNCHECKED_SYM) + ")";
            field = field.replaceAll(regex, "");
        }
        return field;
    }
}
