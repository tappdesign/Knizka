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


import java.util.Calendar;


public class BaseCategory {

	private Long id;
	private String name;
	private String description;
	private String color;
	private int count;


	public BaseCategory() {
		this.id = Calendar.getInstance().getTimeInMillis();
	}


	public BaseCategory(BaseCategory baseCategory) {
		this(baseCategory.getId(), baseCategory.getName(), baseCategory.getDescription(), baseCategory.getColor());
	}


	public BaseCategory(Long id, String title, String description, String color) {
		this(id, title, description, color, 0);
	}


	public BaseCategory(Long id, String title, String description, String color, int count) {
		this.id = id;
		this.name = title;
		this.description = description;
		this.color = color;
		this.count = count;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String title) {
		this.name = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && obj.getClass().equals(this.getClass())) {
			try {
				BaseCategory c = (BaseCategory) obj;
				result = (getColor() == c.getColor() || getColor().equals(c.getColor()))
						&&  (getDescription() == c.getDescription() || getDescription().equals(c.getDescription()))
						&&  (getName() == c.getName() || getName().equals(c.getName()))
						&&  (getId() == c.getId() || getId().equals(c.getId()));
			} catch (ClassCastException e) {
				result = false;
			}
		}
		return result;
	}
}
