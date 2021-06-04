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

import com.pixplicity.easyprefs.library.Prefs;

import pk.tappdesign.knizka.models.ONStyle;
import pk.tappdesign.knizka.models.adapters.JKSStylePagerAdapter;

import static pk.tappdesign.knizka.utils.ConstantsBase.LAYOUT_JKS_PREFIX;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_LAYOUT_JKS_CSS;

public class ViewPagerFragment extends Fragment {

   private ViewPager viewPager;
   private Button confirmButton;
   private Button cancelButton;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_viewpager, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

      JKSStylePagerAdapter jksStylePageAdapter = new JKSStylePagerAdapter(getChildFragmentManager());
      viewPager = view.findViewById(R.id.pager);

      // required to see adjacent pages
      viewPager.setClipToPadding(false);
      viewPager.setPageMargin(12);

      viewPager.setAdapter(jksStylePageAdapter);

    //  confirmButton = view.findViewById(R.id.jks_button_layout_confirm);
     // confirmButton.setOnClickListener(v -> saveJKSLayout());

      confirmButton = view.findViewById(R.id.jks_button_layout_confirm);
      confirmButton.setOnClickListener(v -> saveJKSLayout());
      cancelButton = view.findViewById(R.id.jks_button_layout_cancel);
      cancelButton.setOnClickListener(v -> cancelLayoutChoosing());
   }

   private void saveJKSLayout()
   {
      Prefs.edit().putString(PREF_LAYOUT_JKS_CSS, LAYOUT_JKS_PREFIX + String.format("%02d", viewPager.getCurrentItem()+1) + ".css").commit();
      ((JKSFormatActivity) getActivity()).showExitMessageAndFinish(R.string.settings_jks_format_set, ONStyle.INFO);
   }

   private void cancelLayoutChoosing()
   {
      ((JKSFormatActivity) getActivity()).finish();
   }
}

