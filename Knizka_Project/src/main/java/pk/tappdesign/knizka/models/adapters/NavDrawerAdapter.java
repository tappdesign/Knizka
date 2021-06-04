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
package pk.tappdesign.knizka.models.adapters;


import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_NAVIGATION;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

import com.google.android.material.color.MaterialColors;
import com.neopixl.pixlui.components.textview.TextView;
import com.pixplicity.easyprefs.library.Prefs;

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.MainActivity;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.models.NavigationItem;

import java.util.Arrays;

import pk.tappdesign.knizka.utils.Constants;
import pk.tappdesign.knizka.utils.Fonts;
import java.util.List;


public class NavDrawerAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<NavigationItem> items;
    private LayoutInflater inflater;


    public NavDrawerAdapter(Activity mActivity, List<NavigationItem> items) {
        this.mActivity = mActivity;
        this.items = items;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        NoteDrawerAdapterViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);

            // Overrides font sizes with the one selected from user
            Fonts.overrideTextSize(mActivity, convertView); // BUG01: @pk: not finished, check every appearance of overrideTextSize



            holder = new NoteDrawerAdapterViewHolder();

            holder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (NoteDrawerAdapterViewHolder) convertView.getTag();
        }

        // Set the results into TextViews	
        holder.txtTitle.setText(items.get(position).getText());

        if (isSelected(position)) {
            holder.imgIcon.setImageResource(items.get(position).getIconSelected());
            holder.txtTitle.setTypeface(null, Typeface.BOLD);
            //int color = mActivity.getResources().getColor(R.color.colorPrimaryDark);
            int color = MaterialColors.getColor(holder.txtTitle, R.attr.themeDrawerTextSelected);
            holder.txtTitle.setTextColor(color);
            holder.imgIcon.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.imgIcon.setImageResource(items.get(position).getIcon());
            holder.txtTitle.setTypeface(null, Typeface.NORMAL);
            int color = MaterialColors.getColor(holder.txtTitle, R.attr.themeDrawerText);
            holder.txtTitle.setTextColor(color);
            holder.imgIcon.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }

        return convertView;
    }


    private boolean isSelected(int position) {

        // Getting actual navigation selection
        String[] navigationListCodes = mActivity.getResources().getStringArray(R.array.navigation_list_codes);

        // Managing temporary navigation indicator when coming from a widget
        String navigationTmp = MainActivity.class.isAssignableFrom(mActivity.getClass()) ? ((MainActivity) mActivity)
                .getNavigationTmp() : null;

        String navigation = navigationTmp != null ? navigationTmp : Prefs.getString(PREF_NAVIGATION, navigationListCodes[0]);

        // Finding selected item from standard navigation items or tags
        int index = Arrays.asList(navigationListCodes).indexOf(navigation);

        if (index == -1)
            return false;

        String navigationLocalized = mActivity.getResources().getStringArray(R.array.navigation_list)[index];
        return navigationLocalized.equals(items.get(position).getText());
    }

}


/**
 * Holder object
 *
 * @author fede
 */
class NoteDrawerAdapterViewHolder {

    ImageView imgIcon;
    TextView txtTitle;
}
