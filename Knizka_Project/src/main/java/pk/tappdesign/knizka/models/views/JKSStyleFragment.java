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

package pk.tappdesign.knizka.models.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pk.tappdesign.knizka.DetailFragment;
import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.utils.HTMLProducer;

import static pk.tappdesign.knizka.utils.ConstantsBase.LAYOUT_JKS_PREFIX;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_WEBVIEW_ZOOM;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_WEBVIEW_ZOOM_DEFAULT;
import com.pixplicity.easyprefs.library.Prefs;

public class JKSStyleFragment extends Fragment {
   public static final String ARG_OBJECT = "object";

   private WebView vw;
   private String layoutCSS;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_for_jks_viewpager, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      Bundle args = getArguments();
      ((TextView) view.findViewById(R.id.jksLayoutName)).setText(getContext().getResources().getString(R.string.jks_layout_number) + " " + args.getInt(ARG_OBJECT));
      layoutCSS = LAYOUT_JKS_PREFIX + String.format("%02d", args.getInt(ARG_OBJECT)) + ".css";
      initViewWebView(view);
   }

   private void initViewWebView(View view) {
      vw = view.findViewById(R.id.webViewForlayout);

      vw.setWebChromeClient(new WebChromeClient());
      vw.getSettings().setLoadsImagesAutomatically(true);
      vw.getSettings().setJavaScriptEnabled(true);
      vw.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

      loadNoteToWebView();
   }

   private void loadNoteToWebView() {
      vw.loadDataWithBaseURL("file:///android_asset/", HTMLProducer.getLoremIpsumHTML(layoutCSS), null, null, null);
   }

}
