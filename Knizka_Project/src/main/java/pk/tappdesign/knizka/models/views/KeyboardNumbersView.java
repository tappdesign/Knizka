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
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;

import pk.tappdesign.knizka.R;

public class KeyboardNumbersView extends FrameLayout implements View.OnClickListener {

  private EditText mPasswordField;

  public KeyboardNumbersView(Context context) {
    super(context);
    init();
  }

  public KeyboardNumbersView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public KeyboardNumbersView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    inflate(getContext(), R.layout.keyboard_numbers, this);
    initViews();
  }

  private void initViews() {
    mPasswordField = $(R.id.password_field);
    $(R.id.t9_key_0).setOnClickListener(this);
    $(R.id.t9_key_1).setOnClickListener(this);
    $(R.id.t9_key_2).setOnClickListener(this);
    $(R.id.t9_key_3).setOnClickListener(this);
    $(R.id.t9_key_4).setOnClickListener(this);
    $(R.id.t9_key_5).setOnClickListener(this);
    $(R.id.t9_key_6).setOnClickListener(this);
    $(R.id.t9_key_7).setOnClickListener(this);
    $(R.id.t9_key_8).setOnClickListener(this);
    $(R.id.t9_key_9).setOnClickListener(this);
    $(R.id.t9_key_clear).setOnClickListener(this);
    $(R.id.t9_key_backspace).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    // handle number button click
    if (v.getTag() != null && "number_button".equals(v.getTag())) {
      mPasswordField.append(((TextView) v).getText());
      return;
    }
    switch (v.getId()) {
      case R.id.t9_key_clear: { // handle clear button
        mPasswordField.setText(null);
      }
      break;
      case R.id.t9_key_backspace: { // handle backspace button
        // delete one character
        Editable editable = mPasswordField.getText();
        int charCount = editable.length();
        if (charCount > 0) {
          editable.delete(charCount - 1, charCount);
        }
      }
      break;
    }
  }

  public String getInputText() {
    return mPasswordField.getText().toString();
  }

  protected <T extends View> T $(@IdRes int id) {
    return (T) super.findViewById(id);
  }
}