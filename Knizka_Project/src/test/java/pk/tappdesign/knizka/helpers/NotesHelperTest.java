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

package pk.tappdesign.knizka.helpers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import android.content.Context;
import android.os.Build;
import pk.tappdesign.knizka.BaseUnitTest;
import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.StatsSingleNote;
import pk.tappdesign.knizka.utils.Constants;
import pk.tappdesign.knizka.utils.date.DateUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Knizka.class, Build.VERSION.class})
@PowerMockIgnore("jdk.internal.reflect.*")
public class NotesHelperTest extends BaseUnitTest {

  @Test
  public void checkUtilityClassWellDefined () throws Exception {
    assertUtilityClassWellDefined(NotesHelper.class);
  }

    @Test
    public void haveSameIdShouldFail() {
        Note note1 = getNote(1L, "test title", "test content");
        Note note2 = getNote(2L, "test title", "test content");
        assertFalse(NotesHelper.haveSameId(note1, note2));
    }

	@Test
	public void haveSameIdShouldSucceed() {
		Note note1 = getNote(3L, "test title", "test content");
		Note note2 = getNote(3L, "different test title", "different test content");
		assertTrue(NotesHelper.haveSameId(note1, note2));
	}

	@Test
	public void mergingNotesDoesntDuplicateFirstTitle() {
    	final String FIRST_NOTE_TITLE = "test title 1";
		Note note1 = getNote(4L, FIRST_NOTE_TITLE, "");
		Note note2 = getNote(5L, "test title 2", "");
		Note mergedNote = NotesHelper.mergeNotes(Arrays.asList(note1, note2), false);
		assertFalse(mergedNote.getContent().contains(FIRST_NOTE_TITLE));
	}

	@Test
	public void mergeNotes() {
		int notesNumber = 3;
		List<Note> notes = new ArrayList<>();
		for (int i = 0; i < notesNumber; i++) {
			Note note = new Note();
			note.setTitle("Merged note " + i + " title");
			note.setContent("Merged note " + i + " content");
			notes.add(note);
		}
		Note mergeNote = NotesHelper.mergeNotes(notes, false);

		assertNotNull(mergeNote);
		Assert.assertEquals("Merged note 0 title", mergeNote.getTitle());
		Assert.assertTrue(mergeNote.getContent().contains("Merged note 0 content"));
		Assert.assertTrue(mergeNote.getContent().contains("Merged note 1 content"));
		Assert.assertTrue(mergeNote.getContent().contains("Merged note 2 content"));
		assertEquals(StringUtils.countMatches(mergeNote.getContent(), Constants.MERGED_NOTES_SEPARATOR), 2);
	}
  @Test
  public void getNoteInfos () {
    Context contextMock = getContextMock();
    Whitebox.setInternalState(Build.VERSION.class, "SDK_INT", 29);
    PowerMockito.stub(PowerMockito.method(Knizka.class, "getAppContext")).toReturn(contextMock);

    StatsSingleNote info = NotesHelper.getNoteInfos(new Note());

    assertEquals(0, info.getChars());
    assertEquals(0, info.getWords());
    assertEquals(0, info.getChecklistCompletedItemsNumber());
  }
}
