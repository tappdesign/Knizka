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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pk.tappdesign.knizka.BrowseTextsActivity;
import pk.tappdesign.knizka.MainActivity;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.utils.HTMLProducer;

import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_PICKED_FROM_BROWSE_TEXTS;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_KEY;
import static pk.tappdesign.knizka.utils.ConstantsBase.LAYOUT_JKS_PREFIX;

public class BrowseTextsFragment  extends Fragment {
   public static final String ARG_OBJECT = "object";
   public static final String ARG_NOTE_ID = "noteID";
   public static final String ARG_NOTE_MAX = "noteMax";
   public static final String ARG_NOTE_CATEGORY_TITLE = "categoryTitle";

   private WebView vw;
   private String formatedCategoryTitle;
   private long noteIDForShow;
   private TextView tvTitle;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_for_browse_texts_in_viewpager, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      Bundle args = getArguments();

      tvTitle = view.findViewById(R.id.browseTextsLayoutName);
      //formatedCategoryTitle = String.format("<b>%s</b> (%d/%d)", args.getString(ARG_NOTE_CATEGORY_TITLE), args.getInt(ARG_OBJECT), args.getInt(ARG_NOTE_MAX));
      //tvTitle.setText(Html.fromHtml(formatedCategoryTitle).toString());
      formatedCategoryTitle = String.format("%s (%d/%d)", args.getString(ARG_NOTE_CATEGORY_TITLE), args.getInt(ARG_OBJECT), args.getInt(ARG_NOTE_MAX));
      tvTitle.setText(formatedCategoryTitle);

      noteIDForShow = new Long(args.getString(ARG_NOTE_ID)).longValue();
      initViewWebView(view);
   }

   private void initViewWebView(View view) {
      vw = view.findViewById(R.id.webViewForBrowseTexts);

      vw.setWebChromeClient(new WebChromeClient());
      vw.getSettings().setLoadsImagesAutomatically(true);
      vw.getSettings().setJavaScriptEnabled(true);
      vw.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
      vw.getSettings().setUseWideViewPort(true);
      vw.setInitialScale(93); // need to be zoomed out a little bit, otherwise is page width wider then device screen width

      vw.setOnTouchListener(new View.OnTouchListener() {
          public boolean onTouch(View v, MotionEvent event) {
             //WebView.HitTestResult hr = ((WebView)v).getHitTestResult();
             //  Log.i(TAG, "getExtra = "+ hr.getExtra() + "\t\t Type=" + hr.getType());
             return gestureDetector.onTouchEvent(event);
         }
      });

      loadNoteToWebView();
   }

   private void loadNoteToWebView() {
      Note note = DbHelper.getInstance().getNote(noteIDForShow);
      vw.loadDataWithBaseURL("file:///android_asset/", HTMLProducer.getHTML(note.getHandleID(), note.getTitle(), note.getHTMLContent()), null, null, null);
   }

   final GestureDetector gestureDetector = new GestureDetector( getContext(), new GestureDetector.SimpleOnGestureListener() {
      @Override
      public boolean onSingleTapConfirmed(MotionEvent e) {

         ((BrowseTextsActivity) getActivity()).finish();

         Intent shortcutIntent = new Intent(getContext(), MainActivity.class);
         shortcutIntent.putExtra(INTENT_KEY, noteIDForShow);
         shortcutIntent.setAction(ACTION_PICKED_FROM_BROWSE_TEXTS);
         startActivity(shortcutIntent);

         return true;
      }

      @Override
      public void onLongPress(MotionEvent e) {
         super.onLongPress(e);
      }

      @Override
      public boolean onDoubleTap(MotionEvent e) {
         return super.onDoubleTap(e);
      }
   });

}
