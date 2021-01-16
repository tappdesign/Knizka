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

import org.junit.Test;

import pk.tappdesign.knizka.BaseUnitTest;
import pk.tappdesign.knizka.models.Note;

import static junit.framework.Assert.assertEquals;

public class IdeogramsWordCounterTest extends BaseUnitTest {

    private final String CHECKED_SYM = it.feio.android.checklistview.interfaces.Constants.CHECKED_SYM;
    private final String UNCHECKED_SYM = it.feio.android.checklistview.interfaces.Constants.UNCHECKED_SYM;

    @Test
    public void countChars() {
        Note note = getNote(1L, "这是中文测试", "這是中文測試\n これは日本語のテストです");
        assertEquals(24, new IdeogramsWordCounter().countChars(note));
    }

    @Test
    public void countChecklistChars() {
        String content = CHECKED_SYM + "這是中文測試\n" + UNCHECKED_SYM + "これは日本語のテストです";
        Note note = getNote(1L, "这是中文测试", content);
        note.setChecklist(true);
        assertEquals(24, new IdeogramsWordCounter().countChars(note));
    }

    @Test
    public void getWords() {
        Note note = getNote(1L, "这是中文测试", "這是中文測試\n これは日本語のテストです");
        assertEquals(24, new IdeogramsWordCounter().countWords(note));
        note.setTitle("这");
        assertEquals(19, new IdeogramsWordCounter().countWords(note));
    }

    @Test
    public void getChecklistWords() {
        String content = CHECKED_SYM + "這是中文測試\n" + UNCHECKED_SYM + "これは日本語のテストです";
        Note note = getNote(1L, "这是中文测试", content);
        note.setChecklist(true);
        assertEquals(24, new IdeogramsWordCounter().countWords(note));
    }
}