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

package pk.tappdesign.knizka.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import it.feio.android.checklistview.utils.DensityUtil;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.helpers.LogDelegate;
import java.util.Arrays;


public class Fonts {

  /**
   * Overrides all the fonts set to TextView class descendants found in the view passed as parameter
   */
  public static void overrideTextSize (Context context, View v) {
    Context privateContext = context.getApplicationContext();
    try {
      if (v instanceof ViewGroup) {
        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {
          View child = vg.getChildAt(i);
          overrideTextSize(privateContext, child);
        }
      } else if (v instanceof TextView) {
        float currentSize = DensityUtil.pxToDp(((TextView) v).getTextSize(), privateContext);
        int index = Arrays
            .asList(privateContext.getResources().getStringArray(
                R.array.text_size_values))
            .indexOf(
                Prefs.getString("settings_text_size", "default"));
        float offset = privateContext.getResources().getIntArray(
            R.array.text_size_offset)[index == -1 ? 0 : index];
        ((TextView) v).setTextSize(currentSize + offset);
        //((TextView) v).setTextSize(currentSize + offset + 5); // BUG:01 @pk: sample how to make bigger size of text
      }
    } catch (Exception e) {
      LogDelegate.e("Error setting font size", e);
    }
  }
}
