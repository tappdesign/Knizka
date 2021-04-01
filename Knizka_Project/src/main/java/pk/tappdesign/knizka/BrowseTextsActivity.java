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

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;
import de.keyboardsurfer.android.widget.crouton.Style;
import pk.tappdesign.knizka.utils.ThemeHelper;

import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_MAX_PAGES_IN_BROWSER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER;


public class BrowseTextsActivity extends BaseActivity {
   public final static String FRAGMENT_PAGER_BROWSE_TEXTS = "fragment_pager_browse_texts";

   private ViewGroup croutonHandle;
   private BrowseTextsActivity mActivity;
   private String currentTheme = "";
   private int maxPages = 0;
   private ArrayList<String> notesIds;
   private String categoryTitleForBrowser;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      currentTheme = ThemeHelper.trySetTheme(this);
      DisplayMetrics metrics = getResources().getDisplayMetrics();
      int screenWidth = (int) (metrics.widthPixels * 1.0);
      int screenHeight = (int) (metrics.heightPixels * 1.0);
      setContentView(R.layout.activity_browse_texts);
      getWindow().setLayout(screenWidth, screenHeight);
      mActivity = this;
      setActionBarTitle(getString(R.string.title_activity_browse_texts));

      parseIntent();

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

   private void parseIntent(){
      Intent i = getIntent();
      if (i.hasExtra(INTENT_EXTRA_MAX_PAGES_IN_BROWSER))
      {
         maxPages = i.getExtras().getInt(INTENT_EXTRA_MAX_PAGES_IN_BROWSER);
         notesIds = i.getExtras().getStringArrayList(INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER);
         categoryTitleForBrowser = i.getExtras().getString(INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER);
      }
   }

   private void initViews() {

      FragmentManager mFragmentManager = getSupportFragmentManager();;

      if (mFragmentManager.findFragmentByTag(FRAGMENT_PAGER_BROWSE_TEXTS) == null) {
         FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
         fragmentTransaction.add(R.id.frame_layout_for_browse_texts, new ViewPagerFragmentForBrowseTexts(maxPages, notesIds, categoryTitleForBrowser), FRAGMENT_PAGER_BROWSE_TEXTS)
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
