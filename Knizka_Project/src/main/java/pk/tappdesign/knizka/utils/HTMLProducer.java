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

import com.pixplicity.easyprefs.library.Prefs;

import pk.tappdesign.knizka.helpers.ReadFileAssetsHelper;

import static pk.tappdesign.knizka.utils.ConstantsBase.HTML_DIV_END_TAG;
import static pk.tappdesign.knizka.utils.ConstantsBase.HTML_DIV_MUSIC_SCORE_CONTAINER;
import static pk.tappdesign.knizka.utils.ConstantsBase.HTML_TEXT_TITLE_CLASS;
import static pk.tappdesign.knizka.utils.ConstantsBase.MUSIC_LIBRARY_ABCJS;
import static pk.tappdesign.knizka.utils.ConstantsBase.MUSIC_LIBRARY_OSMD;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_VALUE_DARK;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_JKS_SHOW_MUSIC_SCORE;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_JKS_SHOW_MUSIC_SCORE_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_LAYOUT_JKS_CSS;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_LAYOUT_JKS_CSS_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_MUSIC_SCORE_LIBRARY;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_MUSIC_SCORE_LIBRARY_DEFAULT;



public class HTMLProducer {

    private static String getColorFromSetting()
    {
        String result = "bright";

        switch (Prefs.getString(PREF_HTML_COLOR_SCHEME, PREF_HTML_COLOR_SCHEME_DEFAULT)) {
            case PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT:
                result = "bright";
                break;

            case PREF_HTML_COLOR_SCHEME_VALUE_DARK:
                result = "dark";
                break;

        }
        return result;
    }


    private static String getJKSLayoutFromSetting(long noteHandleID)
    {
        String result;

        if (isJKSSong(noteHandleID))
        {
            result = Prefs.getString(PREF_LAYOUT_JKS_CSS, PREF_LAYOUT_JKS_CSS_DEFAULT);

        } else {
            result = "format1.css";
        }

        return result;
    }

    private static boolean isJKSSong(long noteHandleID)
    {
        return (noteHandleID >= 12000000) && (noteHandleID <= 12999999);
    }


    private static boolean isJKSSongAndMusicScoreIsOn(long noteHandleID)
    {
        boolean result = false;

        if (isJKSSong(noteHandleID))
        {
            if (Prefs.getBoolean(PREF_JKS_SHOW_MUSIC_SCORE, PREF_JKS_SHOW_MUSIC_SCORE_DEFAULT))
            {
                result = true;
            }
        }

        return (result);
    }


    private static String getMusicScoreLibrary(long noteHandleID)
    {
        String result = "";
        if (isJKSSongAndMusicScoreIsOn(noteHandleID))
        {
            switch (Prefs.getString(PREF_MUSIC_SCORE_LIBRARY, PREF_MUSIC_SCORE_LIBRARY_DEFAULT))
            {
                case MUSIC_LIBRARY_OSMD:
                    result = ReadFileAssetsHelper.getInstance().getOpenSheetMusicDisplay();
                    break;

                case MUSIC_LIBRARY_ABCJS:
                    result = ReadFileAssetsHelper.getInstance().getABCJSLib();
                    break;

                default:
                    result = ReadFileAssetsHelper.getInstance().getOpenSheetMusicDisplay();
                    break;
            }
        }

        return result;
    }


    private static String getMusicSheetAsJS(long noteHandleID)
    {
        String result = "";
        if (isJKSSongAndMusicScoreIsOn(noteHandleID))
        {
            switch (Prefs.getString(PREF_MUSIC_SCORE_LIBRARY, PREF_MUSIC_SCORE_LIBRARY_DEFAULT))
            {
                case MUSIC_LIBRARY_OSMD:
                    result = ReadFileAssetsHelper.getInstance().getMusicNotes("js/osmd/osmd_" + noteHandleID + ".js");
                    break;

                case MUSIC_LIBRARY_ABCJS:
                    result = ReadFileAssetsHelper.getInstance().getMusicNotes("js/abcjs/abcjs_" + noteHandleID + ".js");
                    break;

                default:
                    result = ReadFileAssetsHelper.getInstance().getMusicNotes("js/osmd/osmd_" + noteHandleID + ".js");
                    break;
            }
        }
        return result;
    }


    private static String getMusicScoreScriptRendererForOSMD(long noteHandleID)
    {
        String result =
                "<script> var osmd = new opensheetmusicdisplay.OpenSheetMusicDisplay(\"MusicScoreContainer\"); " +
                        "osmd.setOptions({ " +
                        "backend: \"svg\", " +
                        //"drawTitle: true, " +
                        "drawingParameters: \"compacttight\" " +
                        "});" +
                        "osmd" +
                        ".load(MusicScoreData)" +
                        ".then( " +
                        "function() { " +
                        "osmd.zoom = 0.50; " +
                        "osmd.render(); " +
                        "} "+
                        "); </script>";
        return result;
    }


    private static String getMusicScoreScriptRendererForABCJS(long noteHandleID)
    {
        String result =
                "<script>  " +
                        "var visualOptions = { staffwidth: 300, scale: 0.75 };" +
                       // "var visualOptions = {responsive: 'resize' , scale: 2.00 };"+
                        " var visualObj = ABCJS.renderAbc(\"MusicScoreContainer\", MusicScoreData, visualOptions); " +
                "</script>";
        return result;
    }


    private static String getMusicScoreScriptRenderer(long noteHandleID)
    {
        String result = "";
        if (isJKSSongAndMusicScoreIsOn(noteHandleID))
        {
            switch (Prefs.getString(PREF_MUSIC_SCORE_LIBRARY, PREF_MUSIC_SCORE_LIBRARY_DEFAULT))
            {
                case MUSIC_LIBRARY_OSMD:
                    result =  getMusicScoreScriptRendererForOSMD(noteHandleID);
                    break;
                case MUSIC_LIBRARY_ABCJS:
                    result =  getMusicScoreScriptRendererForABCJS(noteHandleID);
                    break;
                default:
                    result = getMusicScoreScriptRendererForOSMD(noteHandleID);
                    break;
            }
         }

        return result;
    }

    public static String getHTML(long noteHandleID, String caption, String htmlText)
    {
        String retVal;

        retVal = "<html><head> " +

                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css_layout/" + getJKSLayoutFromSetting(noteHandleID) +"\" /> " +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css_color/" + getColorFromSetting() + ".css\" /> " +
                ReadFileAssetsHelper.getInstance().getTDJSUtils() +
                getMusicScoreLibrary(noteHandleID) +
                "</head><body onload=\"assignAccordions()\">"+
                HTML_TEXT_TITLE_CLASS + caption + HTML_DIV_END_TAG +
                HTML_DIV_MUSIC_SCORE_CONTAINER +
                htmlText +
                getMusicSheetAsJS(noteHandleID) +
                getMusicScoreScriptRenderer(noteHandleID)+
                "</body></html>";

        return retVal;
    }

    public static String getLoremIpsumHTML(String layoutCSS)
    {
        String retVal;

        retVal = "<html><head> " +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css_layout/" + layoutCSS +  "\" /> " +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css_color/" + getColorFromSetting() + ".css\" /> " +
                ReadFileAssetsHelper.getInstance().getTDJSUtils() +
                "</head><body onload=\"assignAccordions()\">"+
                ReadFileAssetsHelper.getInstance().getLoremIpsumJKS() +
                "</body></html>";

        return retVal;
    }
}
