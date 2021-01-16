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
import java.util.List;

import static org.junit.Assert.*;


public class BaseCategoryTest {

	private BaseCategory baseCategory1;
	private BaseCategory baseCategory2;


	@Before
	public void setUp() {
		baseCategory1 = new BaseCategory();
		baseCategory1.setId(1L);
		baseCategory1.setColor("#ffffff");
		baseCategory1.setCount(10);
		baseCategory1.setDescription("first cat description");
		baseCategory1.setName("first cat name");

		baseCategory2 = new BaseCategory();
		baseCategory2.setId(2L);
		baseCategory2.setColor("#000000");
		baseCategory2.setCount(5);
		baseCategory2.setDescription("second cat description");
		baseCategory2.setName("second cat name");
	}


	@Test
	public void equivalence() {
		BaseCategory newBaseCategory = new BaseCategory(baseCategory1);
		assertEquals(baseCategory1, newBaseCategory);
	}


	@Test
	public void difference() {
		assertNotEquals(baseCategory1, baseCategory2);
	}


	@Test
	public void listContainsNote() {
		List<BaseCategory> categories = new ArrayList<>();
		categories.add(baseCategory1);
		categories.add(baseCategory2);
		assertTrue(categories.contains(baseCategory2));
		assertTrue(categories.contains(baseCategory1));

		BaseCategory newBaseCategory = new BaseCategory();
		newBaseCategory.setName("newCat");
		newBaseCategory.setDescription("newCat desc");
		newBaseCategory.setColor("#cccccc");
		assertFalse(categories.contains(newBaseCategory));
	}
}
