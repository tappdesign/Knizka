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

import android.text.Html;

import androidx.core.text.HtmlCompat;

import pk.tappdesign.knizka.models.Note;
import rx.Observable;

public class DefaultWordCounter implements WordCounter {

    private String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
        }
    }

    @Override
    public int countWords(Note note) {
        int count = 0;
        String[] fields = {note.getTitle(), stripHtml(note.getHTMLContent())};
        for (String field : fields) {
            field = sanitizeTextForWordsAndCharsCount(note, field);
            boolean word = false;
            int endOfLine = field.length() - 1;
            for (int i = 0; i < field.length(); i++) {
                // if the char is a letter, word = true.
                if (Character.isLetter(field.charAt(i)) && i != endOfLine) {
                    word = true;
                    // if char isn't a letter and there have been letters before, counter goes up.
                } else if (!Character.isLetter(field.charAt(i)) && word) {
                    count++;
                    word = false;
                    // last word of String; if it doesn't end with a non letter, it  wouldn't count without this.
                } else if (Character.isLetter(field.charAt(i)) && i == endOfLine) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int countChars(Note note) {
        String titleAndContent = note.getTitle() + "\n" + stripHtml(note.getHTMLContent());
        return Observable
                .from(sanitizeTextForWordsAndCharsCount(note, titleAndContent).split(""))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .count().toBlocking().single();
    }

}
