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

import android.os.SystemClock;
import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {


    private static final long OPTIONS_ITEM_CLICK_DELAY_TIME = 1000;
    private long mLastClickTime;

    @Override
    public void onStart() {
        super.onStart();
        ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected boolean isOptionsItemFastClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < OPTIONS_ITEM_CLICK_DELAY_TIME){
            return true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return false;
    }
}
