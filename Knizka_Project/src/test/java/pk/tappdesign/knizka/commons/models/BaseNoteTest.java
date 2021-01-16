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

package pk.tappdesign.knizka.commons.models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;


public class BaseNoteTest {

	private BaseNote baseNote1;
	private BaseNote baseNote2;


	@Before
	public void setUp() {
		baseNote1 = new BaseNote();
		baseNote1.setTitle("new title");
		baseNote1.setContent("some random content");
		baseNote1.setCreation(Calendar.getInstance().getTimeInMillis() - 10000);
		baseNote1.setLastModification(Calendar.getInstance().getTimeInMillis() - 10000);
		baseNote1.setLocked(true);

		baseNote2 = new BaseNote();
		baseNote2.setTitle("another title");
		baseNote2.setContent("some more random different content");
		baseNote2.setCreation(Calendar.getInstance().getTimeInMillis());
		baseNote2.setLastModification(Calendar.getInstance().getTimeInMillis());
		baseNote2.setCategory(new BaseCategory());
	}


	@Test
	public void equivalence() {
		BaseNote newBaseNote = new BaseNote(baseNote1);
		assertEquals(baseNote1, newBaseNote);
		newBaseNote.setContent(baseNote1.getContent());
		assertEquals(baseNote1, newBaseNote);
	}


	@Test
	public void equivalenceByCategory() {
		BaseNote newBaseNote = new BaseNote(baseNote1);
		newBaseNote.setCategory(new BaseCategory());
		assertFalse(baseNote1.equals(newBaseNote));
	}


	@Test
	public void difference() {
		assertNotEquals(baseNote1, baseNote2);
	}


	@Test
	public void listContainsNote() {
		List<BaseNote> baseNotes = new ArrayList<>();
		baseNotes.add(baseNote1);
		baseNotes.add(baseNote2);
		assertTrue(baseNotes.contains(baseNote1));
		assertTrue(baseNotes.contains(baseNote2));
		assertFalse(baseNotes.contains(new BaseNote()));
	}
}
