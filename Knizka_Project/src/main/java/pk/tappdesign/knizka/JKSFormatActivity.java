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

import pk.tappdesign.knizka.utils.ThemeHelper;

public class JKSFormatActivity extends BaseActivity {

   public final static String FRAGMENT_PAGER = "fragment_pager";

   private ViewGroup croutonHandle;
   private EditText passwordCheck;
   private EditText password;
   private EditText question;
   private EditText answer;
   private EditText answerCheck;
   private JKSFormatActivity mActivity;
   //private FragmentManager mFragmentManager;
   private String currentTheme = "";

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      currentTheme = ThemeHelper.trySetTheme(this);
      DisplayMetrics metrics = getResources().getDisplayMetrics();
      int screenWidth = (int) (metrics.widthPixels * 0.80);
      int screenHeight = (int) (metrics.heightPixels * 0.80);
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
   }

/*

   @SuppressLint("CommitPrefEdits")
   private void updatePassword(String passwordText, String questionText, String answerText) {
      if (passwordText == null) {
         if (prefs.getString(PREF_PASSWORD, "").length() == 0) {
            Crouton.makeText(mActivity, R.string.password_not_set, ONStyle.WARN, croutonHandle).show();
            return;
         }
         new MaterialDialog.Builder(mActivity)
                 .content(R.string.agree_unlocking_all_notes)
                 .positiveText(R.string.ok)
                 .onPositive((dialog, which) -> PasswordHelper.removePassword()).build().show();
      } else if (passwordText.length() == 0) {
         Crouton.makeText(mActivity, R.string.empty_password, ONStyle.WARN, croutonHandle).show();
      } else {
         Observable
                 .from(DbHelper.getInstance().getNotesWithLock(true))
                 .subscribeOn(Schedulers.newThread())
                 .observeOn(AndroidSchedulers.mainThread())
                 .doOnSubscribe(() -> prefs.edit()
                         .putString(PREF_PASSWORD, Security.md5(passwordText))
                         .putString(PREF_PASSWORD_QUESTION, questionText)
                         .putString(PREF_PASSWORD_ANSWER, Security.md5(answerText))
                         .commit())
                 .doOnNext(note -> DbHelper.getInstance().updateNote(note, false))
                 .doOnCompleted(() -> {
                    Crouton crouton = Crouton.makeText(mActivity, R.string.password_successfully_changed, ONStyle
                            .CONFIRM, croutonHandle);
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
                 })
                 .subscribe();
      }
   }

*/


   @Override
   public void onBackPressed() {
      setResult(RESULT_OK);
      finish();
   }
}
