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

import android.app.Activity;

import com.pixplicity.easyprefs.library.Prefs;

import pk.tappdesign.knizka.BrowseTextsActivity;
import pk.tappdesign.knizka.CategoryActivity;
import pk.tappdesign.knizka.GalleryActivity;
import pk.tappdesign.knizka.JKSFormatActivity;
import pk.tappdesign.knizka.NoteInfosActivity;
import pk.tappdesign.knizka.PasswordActivity;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.StatsActivity;
import pk.tappdesign.knizka.widget.WidgetConfigurationActivity;

import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_VALUE_DARK;

public class ThemeHelper {

   private static String getThemeFromPref(Activity activity)
   {
      return Prefs.getString(PREF_HTML_COLOR_SCHEME, PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT);
   }

   private static boolean isDialogActivity(Activity activity)
   {
      if ((activity instanceof CategoryActivity) || (activity instanceof NoteInfosActivity) || (activity instanceof StatsActivity) || (activity instanceof WidgetConfigurationActivity))
      {
         return true;
      }
      return false;
   }

   private static void chooseBrightTheme(Activity activity)
   {
      if (isDialogActivity(activity))
      {
         activity.setTheme(R.style.AppThemeDialogActivity_Bright);
      } else {
         if ((activity instanceof PasswordActivity) || (activity instanceof JKSFormatActivity) || (activity instanceof BrowseTextsActivity))
         {
            activity.setTheme(R.style.AppTheme_Bright_ApiSpec_Dialog);
         } else {
            activity.setTheme(R.style.AppTheme_Bright_ApiSpec);
         }
      }
   }

   private static void chooseDarkTheme(Activity activity)
   {
      if (isDialogActivity(activity))
      {
         activity.setTheme(R.style.AppThemeDialogActivity_Dark);
      } else {
         if ((activity instanceof PasswordActivity) || (activity instanceof JKSFormatActivity) || (activity instanceof BrowseTextsActivity))
         {
            activity.setTheme(R.style.AppTheme_Dark_ApiSpec_Dialog);
         } else {
            activity.setTheme(R.style.AppTheme_Dark_ApiSpec);
         }
      }
   }

   public static boolean needResetTheme(Activity activity, String actualThemeInActivity)
   {
      boolean result = false;

      if (!(getThemeFromPref(activity).equals(actualThemeInActivity) ))
      {
         result = true;
      }

      return result;
   }

   public static String trySetTheme(Activity activity)
   {
      String themeFromPref = getThemeFromPref(activity);
      {
         switch (themeFromPref) {
            case PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT:
               chooseBrightTheme(activity);
               break;

            case PREF_HTML_COLOR_SCHEME_VALUE_DARK:
               chooseDarkTheme(activity);
               break;

            default:
               activity.setTheme(R.style.AppTheme_Bright_ApiSpec);
               break;
         }
      }

      return themeFromPref;
   }

   public static boolean isDarkTheme(String currentTheme)
   {
      boolean result = true;

      switch (currentTheme) {
         case PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT:
            result = false;
            break;
      }

      return result;
   }

   public static void toggleTheme(Activity activity)
   {
      if (isDarkTheme(getThemeFromPref(activity)))
      {
         Prefs.edit().putString (PREF_HTML_COLOR_SCHEME, PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT).commit();
      } else {
         Prefs.edit().putString (PREF_HTML_COLOR_SCHEME, PREF_HTML_COLOR_SCHEME_VALUE_DARK).commit();
      }
   }

}
