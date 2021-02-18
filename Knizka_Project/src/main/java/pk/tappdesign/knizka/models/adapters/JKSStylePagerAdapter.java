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

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import pk.tappdesign.knizka.models.views.JKSStyleFragment;


public class JKSStylePagerAdapter extends FragmentStatePagerAdapter {
   public JKSStylePagerAdapter(FragmentManager fm) {
      super(fm);
   }

   @Override
   public Fragment getItem(int i) {
      Fragment fragment = new JKSStyleFragment();
      Bundle args = new Bundle();
      args.putInt(JKSStyleFragment.ARG_OBJECT, i + 1);
      fragment.setArguments(args);
      return fragment;
   }

   @Override
   public int getCount() {
      return 100;
   }

   @Override
   public CharSequence getPageTitle(int position) {
      return "OBJECT " + (position + 1);
   }
}
