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

package pk.tappdesign.knizka.intro;

import android.graphics.Color;
import android.os.Bundle;
import pk.tappdesign.knizka.R;


public class IntroSlide3 extends IntroFragment {

  @Override
  public void onActivityCreated (Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    binding.introBackground.setBackgroundColor(Color.parseColor("#8bc34a"));
    binding.introTitle.setText(R.string.app_tour_listactivity_category_title);
    binding.introImage.setImageResource(R.drawable.slide3);
    binding.introDescription.setText(R.string.app_tour_listactivity_category_detail);
  }
}
