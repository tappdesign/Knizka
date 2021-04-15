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

package pk.tappdesign.knizka;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import pk.tappdesign.knizka.models.adapters.BrowseTextsPageAdapter;

import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_LIST_VIEW_POSITION_OFFSET_FOR_VIEWPAGER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_MAX_PAGES_IN_BROWSER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER;

public class ViewPagerFragmentForBrowseTexts extends Fragment {

   private ViewPager viewPager;
   private Button confirmButton;
   private Button cancelButton;
   private int maxPages;
   private ArrayList<String> notesIds;
   private String categoryTitle;
   private int listViewPositionOffset;

   public ViewPagerFragmentForBrowseTexts()
   {
     // default constructor is called after orientation change, it is required
   }


   public ViewPagerFragmentForBrowseTexts(int maxPages, ArrayList<String> noteIds, String categoryTitle, int listViewPositionOffset)
   {
      this.maxPages = maxPages;
      this.notesIds = noteIds;
      this.categoryTitle = categoryTitle;
      this.listViewPositionOffset = listViewPositionOffset;

      if (this.listViewPositionOffset >= this.maxPages)
      {
         this.listViewPositionOffset = this.maxPages-1;
      }
      if (this.listViewPositionOffset < 0)
      {
         this.listViewPositionOffset = 0;
      }
   }

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
      if (savedInstanceState != null) {
         maxPages = savedInstanceState.getInt(INTENT_EXTRA_MAX_PAGES_IN_BROWSER);
         notesIds = savedInstanceState.getStringArrayList(INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER);
         categoryTitle = savedInstanceState.getString(INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER);
         listViewPositionOffset = savedInstanceState.getInt(INTENT_EXTRA_LIST_VIEW_POSITION_OFFSET_FOR_VIEWPAGER);
      }

      return inflater.inflate(R.layout.fragment_viewpager_for_browse_texts, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

      BrowseTextsPageAdapter browseTextPageAdapt = new BrowseTextsPageAdapter(getChildFragmentManager(), maxPages, notesIds, categoryTitle);
      viewPager = view.findViewById(R.id.pager_for_browse_texts);

      // required to see adjacent pages
      viewPager.setClipToPadding(false);
      viewPager.setPageMargin(12);

      viewPager.setAdapter(browseTextPageAdapt);
      viewPager.setCurrentItem(listViewPositionOffset);

      confirmButton = view.findViewById(R.id.browse_texts_button_layout_confirm);
      confirmButton.setOnClickListener(v -> saveJKSLayout());
      cancelButton = view.findViewById(R.id.browse_texts_button_layout_cancel);
      cancelButton.setOnClickListener(v -> cancelLayoutChoosing());
   }


   @Override
   public void onSaveInstanceState(Bundle outState) {
      
      outState.putInt(INTENT_EXTRA_MAX_PAGES_IN_BROWSER, maxPages);
      outState.putStringArrayList(INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER, notesIds);
      outState.putString(INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER, categoryTitle);
      outState.putInt(INTENT_EXTRA_LIST_VIEW_POSITION_OFFSET_FOR_VIEWPAGER, listViewPositionOffset);
      super.onSaveInstanceState(outState);
   }

   private void saveJKSLayout()
   {
    //  ((JKSFormatActivity) getActivity()).prefs.edit().putString(PREF_LAYOUT_JKS_CSS, LAYOUT_JKS_PREFIX + String.format("%02d", viewPager.getCurrentItem()+1) + ".css").commit();
     // ((JKSFormatActivity) getActivity()).showExitMessageAndFinish(R.string.settings_jks_format_set, ONStyle.INFO);
   }

   private void cancelLayoutChoosing()
   {
    //  ((JKSFormatActivity) getActivity()).finish();
   }
}
