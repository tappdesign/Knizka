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

import static org.junit.Assert.assertEquals;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import pk.tappdesign.knizka.BaseAndroidTestCase;
import pk.tappdesign.knizka.Knizka;
import java.util.Locale;
import org.junit.Test;
import org.junit.runner.RunWith;





@RunWith(AndroidJUnit4.class)
public class CountFactoryTest extends BaseAndroidTestCase {

    @Test
  public void getWordCounter_english () {
        Knizka.getAppContext().getResources().getConfiguration().setLocale(Locale.US);
        assertEquals(DefaultWordCounter.class, CountFactory.getWordCounter().getClass());
    }

    @Test
  public void getWordCounter_italian () {
        Knizka.getAppContext().getResources().getConfiguration().setLocale(Locale.ITALY);
        assertEquals(DefaultWordCounter.class, CountFactory.getWordCounter().getClass());
    }

    @Test
  public void getWordCounter_chineseSimplified () {
        Knizka.getAppContext().getResources().getConfiguration().setLocale(Locale.SIMPLIFIED_CHINESE);
        assertEquals(IdeogramsWordCounter.class, CountFactory.getWordCounter().getClass());
    }

    @Test
  public void getWordCounter_chineseTraditional () {
        Knizka.getAppContext().getResources().getConfiguration().setLocale(Locale.TRADITIONAL_CHINESE);
        assertEquals(IdeogramsWordCounter.class, CountFactory.getWordCounter().getClass());
    }

  @Test
  public void getWordCounter_japanese () {
     Knizka.getAppContext().getResources().getConfiguration().setLocale(Locale.JAPAN);
    assertEquals(IdeogramsWordCounter.class, CountFactory.getWordCounter().getClass());
  }

  @Test
  public void getWordCounter_unknowLocale () {
     Knizka.getAppContext().getResources().getConfiguration().setLocale(new Locale("meow"));
    assertEquals(DefaultWordCounter.class, CountFactory.getWordCounter().getClass());
  }

}