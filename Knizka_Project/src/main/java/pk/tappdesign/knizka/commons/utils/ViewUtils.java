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

import android.view.View;

public class ViewUtils {
   public static boolean hitTest(View v, int x, int y) {
      final int tx = (int) (v.getTranslationX() + 0.5f);
      final int ty = (int) (v.getTranslationY() + 0.5f);
      final int left = v.getLeft() + tx;
      final int right = v.getRight() + tx;
      final int top = v.getTop() + ty;
      final int bottom = v.getBottom() + ty;

      return (x >= left) && (x <= right) && (y >= top) && (y <= bottom);
   }

}