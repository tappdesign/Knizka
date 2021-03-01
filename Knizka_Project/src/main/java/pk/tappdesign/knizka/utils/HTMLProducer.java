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

import android.content.SharedPreferences;

import pk.tappdesign.knizka.helpers.ReadFileAssetsHelper;

import static pk.tappdesign.knizka.utils.ConstantsBase.HTML_DIV_END_TAG;
import static pk.tappdesign.knizka.utils.ConstantsBase.HTML_TEXT_TITLE_CLASS;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_VALUE_DARK;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_LAYOUT_JKS_CSS;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_LAYOUT_JKS_CSS_DEFAULT;

public class HTMLProducer {

    private static String getColorFromSetting(SharedPreferences prefs)
    {
        String result = "bright";

        switch (prefs.getString(PREF_HTML_COLOR_SCHEME, PREF_HTML_COLOR_SCHEME_DEFAULT)) {
            case PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT:
                result = "bright";
                break;

            case PREF_HTML_COLOR_SCHEME_VALUE_DARK:
                result = "dark";
                break;

        }
        return result;
    }


    private static String getJKSLayoutFromSetting(SharedPreferences prefs, long noteHandleID)
    {
        String result;

        if ((noteHandleID >= 12000000) && (noteHandleID <= 12999999))
        {
            result = prefs.getString(PREF_LAYOUT_JKS_CSS, PREF_LAYOUT_JKS_CSS_DEFAULT);

        } else {
            result = "format1.css";
        }

        return result;
    }



    public static String getHTML(SharedPreferences prefs, long noteHandleID, String caption, String htmlText)
    {
        String retVal;

        retVal = "<html><head> " +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css_layout/" + getJKSLayoutFromSetting(prefs, noteHandleID) +"\" /> " +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css_color/" + getColorFromSetting(prefs) + ".css\" /> " +
                ReadFileAssetsHelper.getInstance().getTDJSUtils() +
                "</head><body onload=\"assignAccordions()\">"+
                HTML_TEXT_TITLE_CLASS + caption + HTML_DIV_END_TAG + htmlText +
                "</body></html>";

        return retVal;
    }

    public static String getLoremIpsumHTML(SharedPreferences prefs, String layoutCSS)
    {
        String retVal;

        retVal = "<html><head> " +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css_layout/" + layoutCSS +  "\" /> " +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css_color/" + getColorFromSetting(prefs) + ".css\" /> " +
                ReadFileAssetsHelper.getInstance().getTDJSUtils() +
                "</head><body onload=\"assignAccordions()\">"+
                ReadFileAssetsHelper.getInstance().getLoremIpsumJKS() +
                "</body></html>";

        return retVal;
    }
}
