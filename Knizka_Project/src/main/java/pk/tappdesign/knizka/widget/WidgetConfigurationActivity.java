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

package pk.tappdesign.knizka.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.models.Category;
import pk.tappdesign.knizka.models.adapters.CategoryBaseAdapter;
import pk.tappdesign.knizka.utils.ThemeHelper;

import static pk.tappdesign.knizka.db.DbHelper.WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;


public class WidgetConfigurationActivity extends Activity {

    private Spinner categorySpinner;
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private String sqlCondition;
    private RadioGroup mRadioGroup;
    private String currentTheme = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);
        currentTheme = ThemeHelper.trySetTheme(this);
        setContentView(R.layout.activity_widget_configuration);

        mRadioGroup = (RadioGroup) findViewById(R.id.widget_config_radiogroup);
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.widget_config_notes:
                    categorySpinner.setEnabled(false);
                    break;

                    case R.id.widget_config_categories:
                        categorySpinner.setEnabled(true);
                        break;

					default:
						LogDelegate.e("Wrong element choosen: " + checkedId);
                }
        });

        categorySpinner = (Spinner) findViewById(R.id.widget_config_spinner);
        categorySpinner.setEnabled(false);
        DbHelper db = DbHelper.getInstance();
        ArrayList<Category> categories = db.getCategories();
        categorySpinner.setAdapter(new CategoryBaseAdapter(this, categories));

        Button configOkButton = (Button) findViewById(R.id.widget_config_confirm);
        configOkButton.setOnClickListener(v -> {

            if (mRadioGroup.getCheckedRadioButtonId() == R.id.widget_config_notes) {
                sqlCondition = " WHERE " + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;
            } else {
                Category tag = (Category) categorySpinner.getSelectedItem();
                sqlCondition = " WHERE " + DbHelper.COL_MERGED_CATEGORY + " = " + tag.getId() + " AND " + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;
            }

            CheckBox showThumbnailsCheckBox = (CheckBox) findViewById(R.id.show_thumbnails);
            CheckBox showTimestampsCheckBox = (CheckBox) findViewById(R.id.show_timestamps);

            // Updating the ListRemoteViewsFactory parameter to get the list
            // of notes
            ListRemoteViewsFactory.updateConfiguration(getApplicationContext(), mAppWidgetId,
                    sqlCondition, showThumbnailsCheckBox.isChecked(), showTimestampsCheckBox.isChecked());

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    mAppWidgetId);
            setResult(RESULT_OK, resultValue);

            finish();
        });

        // Checks if no tags are available and then disable that option
        if (categories.size() == 0) {
            mRadioGroup.setVisibility(View.GONE);
            categorySpinner.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget ID, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ThemeHelper.needResetTheme(this, currentTheme))
        {
            recreate();
        }
    }
}
