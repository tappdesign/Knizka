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
import android.webkit.WebView;

import androidx.appcompat.widget.Toolbar;

import pk.tappdesign.knizka.utils.SystemHelper;
import pk.tappdesign.knizka.utils.ThemeHelper;

import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_RESTART_APP;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_WIDGET;

public class ShowHTMLActivity extends BaseActivity {
   private WebView webview;
   private String currentTheme = "";

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      currentTheme = ThemeHelper.trySetTheme(this);
      setContentView(R.layout.activity_about);

      webview = findViewById(R.id.webview);

      initUI();
   }

   @Override
   protected void onResume() {
      super.onResume();

      if (ThemeHelper.needResetTheme(this, currentTheme))
      {
         recreate();
      }

      updateWebViewAndTitle();
   }

   @Override
   public void onStart() {
      ((Knizka)getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());
      super.onStart();
   }


   @Override
   public boolean onNavigateUp() {
      onBackPressed();
      return true;
   }


   private void initUI() {
      Toolbar toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeButtonEnabled(true);
      toolbar.setNavigationOnClickListener(v -> onNavigateUp());
   }

   private void updateWebViewAndTitle()
   {
      String result = "file:///android_asset/html/ResponsoriumVerses.html";
      String activityTitle =  getResources().getString(R.string.title_activity_resp_verses);
      Intent i = getIntent();

      if (i.hasExtra("show_jks_info") )
      {
         result = "file:///android_asset/html/JKSInfo.html";
         activityTitle = getResources().getString(R.string.title_activity_JKS_origin);
      }

      if (i.hasExtra("show_liturgical_solemnity") )
      {
         result = "file:///android_asset/html/LiturgicalSolemnity.html";
         activityTitle = getResources().getString(R.string.title_activity_liturgical_solemnity);
      }

      webview.loadUrl(result);

      if (getSupportActionBar() != null) {
         getSupportActionBar().setTitle(activityTitle);
         getSupportActionBar().setDisplayShowTitleEnabled(true); // bug-fix: make sure title is visible in actionbar
      }
      return ;
   }


}

