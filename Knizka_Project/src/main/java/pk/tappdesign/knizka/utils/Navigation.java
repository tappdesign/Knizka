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

import static android.content.Context.MODE_MULTI_PROCESS;
import static pk.tappdesign.knizka.utils.Constants.PREFS_NAME;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_NAVIGATION;



import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.models.Category;

import java.util.ArrayList;
import java.util.Arrays;


public class Navigation {

    private Navigation() {
        // hides public constructor
    }

    public static final int NOTES = 0;
    public static final int ARCHIVE = 1;
    public static final int REMINDERS = 2;
    public static final int TRASH = 3;
    public static final int UNCATEGORIZED = 4;
    public static final int FAVORITES = 5;
    public static final int LAST_SHOWN = 6;
    public static final int RANDOM = 7;
    public static final int PRAYER_SETS = 8;
    public static final int JKS = 9;
    public static final int JKS_NUMBER_SEARCH = 10;
    public static final int INTENTIONS = 11;
    public static final int CATEGORY = 12;

    /**
     * Returns actual navigation status
     */
    public static int getNavigation() {
        String[] navigationListCodes = Knizka.getAppContext().getResources().getStringArray(R.array.navigation_list_codes);
        String navigation = getNavigationText();

        if (navigationListCodes[NOTES].equals(navigation)) {
            return NOTES;
        } else if (navigationListCodes[ARCHIVE].equals(navigation)) {
            return ARCHIVE;
        } else if (navigationListCodes[REMINDERS].equals(navigation)) {
            return REMINDERS;
        } else if (navigationListCodes[TRASH].equals(navigation)) {
            return TRASH;
        } else if (navigationListCodes[UNCATEGORIZED].equals(navigation)) {
            return UNCATEGORIZED;
        } else if (navigationListCodes[FAVORITES].equals(navigation)) {
            return FAVORITES;
        } else if (navigationListCodes[LAST_SHOWN].equals(navigation)) {
            return LAST_SHOWN;
        } else if (navigationListCodes[RANDOM].equals(navigation)) {
            return RANDOM;
        } else if (navigationListCodes[PRAYER_SETS].equals(navigation)) {
            return PRAYER_SETS;
        } else if (navigationListCodes[JKS].equals(navigation)) {
            return JKS;
        } else if (navigationListCodes[JKS_NUMBER_SEARCH].equals(navigation)) {
            return JKS_NUMBER_SEARCH;
        } else if (navigationListCodes[INTENTIONS].equals(navigation)) {
            return INTENTIONS;
        } else {
            return CATEGORY;
        }
    }


    public static String getNavigationText() {
        Context mContext = Knizka.getAppContext();
        String[] navigationListCodes = mContext.getResources().getStringArray(R.array.navigation_list_codes);
        return mContext.getSharedPreferences(PREFS_NAME, MODE_MULTI_PROCESS).getString(PREF_NAVIGATION, navigationListCodes[0]);
    }


    /**
     * Retrieves category currently shown
     *
     * @return ID of category or null if current navigation is not a category
     */
    public static Long getCategory() {
        if (getNavigation() == CATEGORY) {
            return Long.valueOf(Knizka.getAppContext().getSharedPreferences(PREFS_NAME, MODE_MULTI_PROCESS).getString(PREF_NAVIGATION, ""));
		} else {
            return null;
        }
    }


    /**
     * Checks if passed parameters is the actual navigation status
     */
    public static boolean checkNavigation(int navigationToCheck) {
        return checkNavigation(new Integer[]{navigationToCheck});
    }


    public static boolean checkNavigation(Integer[] navigationsToCheck) {
        boolean res = false;
        int navigation = getNavigation();
        for (int navigationToCheck : new ArrayList<>(Arrays.asList(navigationsToCheck))) {
            if (navigation == navigationToCheck) {
                res = true;
                break;
            }
        }
        return res;
    }


    /**
     * Checks if passed parameters is the category user is actually navigating in
     */
    public static boolean checkNavigationCategory(Category categoryToCheck) {
        Context mContext = Knizka.getAppContext();
        String[] navigationListCodes = mContext.getResources().getStringArray(R.array.navigation_list_codes);
        String navigation = mContext.getSharedPreferences(PREFS_NAME, MODE_MULTI_PROCESS).getString(PREF_NAVIGATION, navigationListCodes[0]);
        return (categoryToCheck != null && navigation.equals(String.valueOf(categoryToCheck.getId())));
    }

}
