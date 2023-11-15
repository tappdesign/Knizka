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


import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_CATEGORY;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_NAVIGATION;

import static java.lang.Integer.parseInt;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.pixplicity.easyprefs.library.Prefs;

import de.greenrobot.event.EventBus;
import pk.tappdesign.knizka.async.bus.CategoriesUpdatedEvent;
import pk.tappdesign.knizka.databinding.ActivityCategoryBinding;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.models.Category;
import it.feio.android.simplegallery.util.BitmapUtils;
import java.util.Calendar;
import java.util.Random;

import pk.tappdesign.knizka.utils.Display;
import pk.tappdesign.knizka.utils.ThemeHelper;


public class CategoryActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback{

  private ActivityCategoryBinding binding;

    Category category;
    private int selectedColor;

    private String currentTheme = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentTheme = ThemeHelper.trySetTheme(this);
       binding = ActivityCategoryBinding.inflate(getLayoutInflater());
    	View view = binding.getRoot();
    	setContentView(view);
       

        //category = getIntent().getParcelableExtra(INTENT_CATEGORY);
       category = savedInstanceState != null
               ? savedInstanceState.getParcelable("category")
               : getIntent().getParcelableExtra(INTENT_CATEGORY);

        if (category == null) {
            LogDelegate.d("Adding new category");
            category = new Category();
            category.setColor(String.valueOf(getRandomPaletteColor()));
        } else {
            LogDelegate.d("Editing category " + category.getName());
        }
        selectedColor = parseInt(category.getColor());
        populateViews();
       resetWindowSize();
    }

   @Override
   public void onSaveInstanceState(Bundle outState) {
      outState.putParcelable("category", category);
      super.onSaveInstanceState(outState);
   }

   private void resetWindowSize() {
      Point screen = Display.getScreenDimensions(this);
      Window window = getWindow();
      WindowManager.LayoutParams params = window.getAttributes();
      params.width = (int) (screen.x * 0.8);
      params.height = WindowManager.LayoutParams.WRAP_CONTENT;
      window.setAttributes(params);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ThemeHelper.needResetTheme(this, currentTheme))
        {
            recreate();
        }
    }

    private int getRandomPaletteColor() {
        int[] paletteArray = getResources().getIntArray(R.array.material_colors);
        return paletteArray[new Random().nextInt((paletteArray.length))];
    }

    public void showColorChooserCustomColors() {

        new ColorChooserDialog.Builder(this, R.string.colors)
                .theme( ThemeHelper.isDarkTheme(currentTheme) ? Theme.DARK: Theme.LIGHT)
                .dynamicButtonColor(false)
                .preselect(selectedColor)
                .show(this);
    }

  @Override
  public void onColorSelection (@NonNull ColorChooserDialog colorChooserDialog, int color) {
    BitmapUtils.changeImageViewDrawableColor(binding.colorChooser, color);
    selectedColor = color;
  }

    @Override
    public void onColorChooserDismissed (@NonNull ColorChooserDialog dialog) {
        // Nothing to do
    }

    @Override
    public void onPointerCaptureChanged (boolean hasCapture) {
        // Nothing to do
    }

  private void populateViews () {
    binding.categoryTitle.setText(category.getName());
    binding.categoryDescription.setText(category.getDescription());
    // Reset picker to saved color
    String color = category.getColor();
    if (color != null && color.length() > 0) {
      binding.colorChooser.getDrawable().mutate().setColorFilter(parseInt(color), PorterDuff.Mode.SRC_ATOP);
    }
    binding.delete.setVisibility(TextUtils.isEmpty(category.getName()) ? View.INVISIBLE : View.VISIBLE);

    binding.save.setOnClickListener(v -> saveCategory());
    binding.delete.setOnClickListener(v -> deleteCategory());
    binding.colorChooser.setOnClickListener(v -> showColorChooserCustomColors());
  }

  public void saveCategory () {

    if (binding.categoryTitle.getText().toString().length() == 0) {
      binding.categoryTitle.setError(getString(R.string.category_missing_title));
      return;
    }

    Long id = category.getId() != null ? category.getId() : Calendar.getInstance().getTimeInMillis();
    category.setId(id);
    category.setName(binding.categoryTitle.getText().toString());
    category.setDescription(binding.categoryDescription.getText().toString());
    if (selectedColor != 0 || category.getColor() == null) {
      category.setColor(String.valueOf(selectedColor));
    }

    // Saved to DB and new ID or update result catched
    DbHelper db = DbHelper.getInstance();
    category = db.updateCategory(category);

    // Sets result to show proper message
    getIntent().putExtra(INTENT_CATEGORY, category);
    setResult(RESULT_OK, getIntent());
    finish();
  }

    public void deleteCategory() {

        new MaterialDialog.Builder(this)
				.title(R.string.delete_unused_category_confirmation)
                .content(R.string.delete_category_confirmation)
                .positiveText(R.string.confirm)
                .positiveColorRes(R.color.colorAccent)
                .onPositive((dialog, which) -> {
                    // Changes navigation if actually are shown notes associated with this category

                    String navNotes = getResources().getStringArray(R.array.navigation_list_codes)[0];
                    String navigation = Prefs.getString(PREF_NAVIGATION, navNotes);
                    if (String.valueOf(category.getId()).equals(navigation)) {
                        Prefs.edit().putString(PREF_NAVIGATION, navNotes).apply();
                    }
                    // Removes category and edit notes associated with it
                    DbHelper db = DbHelper.getInstance();
                    db.deleteCategory(category);

                    EventBus.getDefault().post(new CategoriesUpdatedEvent());
                    BaseActivity.notifyAppWidgets(Knizka.getAppContext());


                    setResult(RESULT_FIRST_USER);
                    finish();
                }).build().show();
    }



}
