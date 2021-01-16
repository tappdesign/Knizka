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

package pk.tappdesign.knizka.commons.utils;

public class EqualityChecker {
	public static synchronized boolean check(Object a, Object b) {
		boolean res = true;
		if (a != null) {
			res = res && a.equals(b);
		} else if (b != null) {
			res = res && b.equals(a);
		}
		return res;
	}

	public static synchronized boolean check(Object[] aArr, Object[] bArr) {
		boolean res = true;

		// Array size test
		res = res && aArr.length == bArr.length;

		// If arrays have the same length
		if (res) {
			for (int i = 0; i < aArr.length; i++) {
				Object a = aArr[i];
				Object b = bArr[i];

				// Content test on each element
				if (a != null) {
					res = res && a.equals(b);
				} else if (b != null) {
					res = res && b.equals(a);
				}
				
				// Exit if not equals
				if (!res)
					break;
			}
		}

		return res;
	}

}
