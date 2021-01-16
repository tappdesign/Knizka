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

package pk.tappdesign.knizka.ui;



import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;

import pk.tappdesign.knizka.utils.ConstantsBase;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NoteListMenuTest  extends BaseEspressoTest  {

   @Test
   public void switchExpandedColapsedNoteLayoutTest () {

      prefs.edit().putBoolean(Constants.PREF_EXPANDED_VIEW, false).apply();

    createTestNote("A Title", "A content", 0);

      // click overflow menu button
      openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

      // click expanded view menu item
      onView(withText(R.string.expanded_view))
              .perform(click());

      openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

      onView(withText(R.string.contracted_view))
              .perform(click());
   }
}
