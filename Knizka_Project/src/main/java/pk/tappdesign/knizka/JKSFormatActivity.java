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
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;
import de.keyboardsurfer.android.widget.crouton.Style;
import pk.tappdesign.knizka.models.ONStyle;
import pk.tappdesign.knizka.utils.ThemeHelper;

public class JKSFormatActivity extends BaseActivity {

   public final static String FRAGMENT_PAGER = "fragment_pager";

   private ViewGroup croutonHandle;
   private JKSFormatActivity mActivity;
   private String currentTheme = "";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      currentTheme = ThemeHelper.trySetTheme(this);
      DisplayMetrics metrics = getResources().getDisplayMetrics();
      int screenWidth = (int) (metrics.widthPixels * 1.0);
      int screenHeight = (int) (metrics.heightPixels * 1.0);
      setContentView(R.layout.activity_jks_format);
      getWindow().setLayout(screenWidth, screenHeight);
      mActivity = this;
      setActionBarTitle(getString(R.string.title_activity_jks_song_format));
      initViews();
   }

   @Override
   protected void onResume() {
      super.onResume();

      if (ThemeHelper.needResetTheme(this, currentTheme))
      {
         recreate();
      }
   }

   private void initViews() {

      FragmentManager mFragmentManager = getSupportFragmentManager();;

      if (mFragmentManager.findFragmentByTag(FRAGMENT_PAGER) == null) {
         FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
         fragmentTransaction.add(R.id.frame_layout_for_pager, new ViewPagerFragment(), FRAGMENT_PAGER)
                 .commit();
      }

      croutonHandle = findViewById(R.id.crouton_handle);
   }

   public void showExitMessageAndFinish(int messageId, Style style) {
      Crouton crouton = Crouton.makeText(mActivity, messageId, style, croutonHandle);
      crouton.setLifecycleCallback(new LifecycleCallback() {
         @Override
         public void onDisplayed() {
            // Does nothing!
         }
         @Override
         public void onRemoved() {
            onBackPressed();
         }
      });
      crouton.show();
   }

   @Override
   public void onBackPressed() {
      setResult(RESULT_OK);
      finish();
   }
}
