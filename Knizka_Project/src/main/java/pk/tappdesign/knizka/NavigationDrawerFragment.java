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

import static pk.tappdesign.knizka.async.bus.SwitchFragmentEvent.Direction.CHILDREN;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.color.MaterialColors;

import de.greenrobot.event.EventBus;
import pk.tappdesign.knizka.async.CategoryMenuTask;
import pk.tappdesign.knizka.async.MainMenuTask;
import pk.tappdesign.knizka.async.bus.CategoriesUpdatedEvent;
import pk.tappdesign.knizka.async.bus.DynamicNavigationReadyEvent;
import pk.tappdesign.knizka.async.bus.NavigationUpdatedEvent;
import pk.tappdesign.knizka.async.bus.NavigationUpdatedNavDrawerClosedEvent;
import pk.tappdesign.knizka.async.bus.NotesLoadedEvent;
import pk.tappdesign.knizka.async.bus.NotesUpdatedEvent;
import pk.tappdesign.knizka.async.bus.SwitchFragmentEvent;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.models.Category;
import pk.tappdesign.knizka.models.NavigationItem;
import pk.tappdesign.knizka.models.ONStyle;
import pk.tappdesign.knizka.utils.Display;
import pk.tappdesign.knizka.utils.ThemeHelper;


public class NavigationDrawerFragment extends Fragment {

  static final int BURGER = 0;
  static final int ARROW = 1;

  ActionBarDrawerToggle mDrawerToggle;
  DrawerLayout mDrawerLayout;
  ImageView mDarkBrightToggle;
  private MainActivity mActivity;
  private boolean alreadyInitialized;


  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }


  @Override
  public void onStart () {
    super.onStart();
    EventBus.getDefault().register(this);
  }


  @Override
  public void onStop () {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }


  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
  }


  @Override
  public void onActivityCreated (Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mActivity = (MainActivity) getActivity();

    init();
  }


  private MainActivity getMainActivity () {
    return (MainActivity) getActivity();
  }


  public void onEventMainThread (DynamicNavigationReadyEvent event) {
  /*
    if (alreadyInitialized) {
      alreadyInitialized = false;
    } else {
      refreshMenus();
    }
    */

    // todo: @pk: 08.01.2020 - always refresh menus...
    //  because alreadyInitialized seems to be not reliable??? (menu is not refreshed properly) ...find some better solution later
    //  maybe could be something like this:  EventBus.getDefault().post(new NotesUpdatedEvent(notes)); for resetting the "alreadyInitialized"
   refreshMenus();
  }


  public void onEvent (CategoriesUpdatedEvent event) {
    refreshMenus();
  }


  public void onEventAsync (NotesUpdatedEvent event) {
    alreadyInitialized = false;
  }


  public void onEvent (NotesLoadedEvent event) {
    if (mDrawerLayout != null) {
      if (!isDoublePanelActive()) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
      }
    }
    if (getMainActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
      init();
    }
    refreshMenus();
    alreadyInitialized = true;
  }


  public void onEvent (SwitchFragmentEvent event) {
    if (CHILDREN.equals(event.getDirection())) {
      animateBurger(ARROW);
    } else {
      animateBurger(BURGER);
    }
  }


  public void onEvent (NavigationUpdatedEvent navigationUpdatedEvent) {
    if (navigationUpdatedEvent.navigationItem.getClass().isAssignableFrom(NavigationItem.class)) {
      mActivity.getSupportActionBar().setTitle(((NavigationItem) navigationUpdatedEvent.navigationItem).getText());
    } else {
      mActivity.getSupportActionBar().setTitle(((Category) navigationUpdatedEvent.navigationItem).getName());
    }
    if (mDrawerLayout != null) {
      if (!isDoublePanelActive()) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
      }
      new Handler().postDelayed(() -> EventBus.getDefault().post(new NavigationUpdatedNavDrawerClosedEvent
          (navigationUpdatedEvent.navigationItem)), 400);
    }
  }

  public void init () {
    LogDelegate.v("Started navigation drawer initialization");

    mDrawerLayout = mActivity.findViewById(R.id.drawer_layout);
    mDrawerLayout.setFocusableInTouchMode(false);

    mDarkBrightToggle = mActivity.findViewById(R.id.dark_bright_toggle_icon);
    mDarkBrightToggle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mActivity.showMessage("ok", ONStyle.ALERT);
        ThemeHelper.toggleTheme(mActivity);
        mActivity.recreate();
      }
    });


    View leftDrawer = getView().findViewById(R.id.left_drawer);
    int leftDrawerBottomPadding = Display.getNavigationBarHeightKitkat(getActivity());
    leftDrawer.setPadding(leftDrawer.getPaddingLeft(), leftDrawer.getPaddingTop(), leftDrawer.getPaddingRight(),
        leftDrawerBottomPadding);

    // ActionBarDrawerToggle± ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    mDrawerToggle = new ActionBarDrawerToggle(mActivity,
        mDrawerLayout,
        getMainActivity().getToolbar(),
        R.string.drawer_open,
        R.string.drawer_close
    ) {
      public void onDrawerClosed (View view) {
        mActivity.supportInvalidateOptionsMenu();
      }


      public void onDrawerOpened (View drawerView) {
        mActivity.commitPending();
        mActivity.finishActionMode();
      }
    };

    if (isDoublePanelActive()) {
      mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    // Styling options
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    mDrawerLayout.addDrawerListener(mDrawerToggle);
    mDrawerToggle.setDrawerIndicatorEnabled(true);
    LogDelegate.v("Finished navigation drawer initialization");
  }


  private void refreshMenus () {
    buildMainMenu();
    LogDelegate.v("Finished main menu initialization");
    buildCategoriesMenu();
    LogDelegate.v("Finished categories menu initialization");
    mDrawerToggle.syncState();
  }


  private void buildCategoriesMenu () {
    CategoryMenuTask task = new CategoryMenuTask(this);
    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }


  private void buildMainMenu () {
    MainMenuTask task = new MainMenuTask(this);
    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }


  void animateBurger (int targetShape) {
    if (mDrawerToggle != null) {
      if (targetShape != BURGER && targetShape != ARROW) {
        return;
      }
      ValueAnimator anim = ValueAnimator.ofFloat((targetShape + 1) % 2, targetShape);
      anim.addUpdateListener(valueAnimator -> {
        float slideOffset = (Float) valueAnimator.getAnimatedValue();
        mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset);
      });
      anim.setInterpolator(new DecelerateInterpolator());
      anim.setDuration(500);
      anim.start();
    }
  }


  public static boolean isDoublePanelActive () {
//		Resources resources = Knizka.getAppContext().getResources();
//		return resources.getDimension(R.dimen.navigation_drawer_width) == resources.getDimension(R.dimen
//				.navigation_drawer_reserved_space);
    return false;

  }

  public void OnClickDarkBrightToggle(View view) {
    //Implement image click function
  }

}
