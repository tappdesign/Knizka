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

import static androidx.core.view.ViewCompat.animate;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_FAB_TAKE_PHOTO;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_MERGE;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_POSTPONE;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_SEARCH_UNCOMPLETE_CHECKLISTS;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_SHORTCUT_WIDGET;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_WIDGET_SHOW_LIST;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_CATEGORY;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_LIST_VIEW_POSITION_OFFSET_FOR_VIEWPAGER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_MAX_PAGES_IN_BROWSER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_NOTE;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_WIDGET;
import static pk.tappdesign.knizka.utils.ConstantsBase.JKS_SORTING_TYPE_NAME;
import static pk.tappdesign.knizka.utils.ConstantsBase.JKS_SORTING_TYPE_NUMBER;
import static pk.tappdesign.knizka.utils.ConstantsBase.MENU_SORT_GROUP_ID;
import static pk.tappdesign.knizka.utils.ConstantsBase.PACKAGE_SYSTEM;
import static pk.tappdesign.knizka.utils.ConstantsBase.PRAYER_MERGED_LINKED_SET;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_ENABLE_SWIPE;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_ENABLE_SWIPE_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_EXPANDED_VIEW;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_FAB_EXPANSION_BEHAVIOR;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_FILTER_ARCHIVED_IN_CATEGORIES;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_FILTER_PAST_REMINDERS;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_JKS_SORTING_TYPE;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_JKS_SORTING_TYPE_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_KEEP_SCREEN_ON;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_KEEP_SCREEN_ON_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_NAVIGATION;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_NAVIGATION_JKS_CATEGORY_ID;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_SORTING_COLUMN;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_WIDGET_PREFIX;
import static pk.tappdesign.knizka.utils.ConstantsBase.PACKAGE_USER_ADDED;
import static pk.tappdesign.knizka.utils.ConstantsBase.PACKAGE_USER_INTENT;
import static pk.tappdesign.knizka.utils.Navigation.CATEGORY;
import static pk.tappdesign.knizka.utils.Navigation.FAVORITES;
import static pk.tappdesign.knizka.utils.Navigation.INTENTIONS;
import static pk.tappdesign.knizka.utils.Navigation.JKS;
import static pk.tappdesign.knizka.utils.Navigation.JKS_CATEGORIES;
import static pk.tappdesign.knizka.utils.Navigation.JKS_NUMBER_SEARCH;
import static pk.tappdesign.knizka.utils.Navigation.LAST_SHOWN;
import static pk.tappdesign.knizka.utils.Navigation.NOTES;
import static pk.tappdesign.knizka.utils.Navigation.PRAYER_LINKED_SET;
import static pk.tappdesign.knizka.utils.Navigation.PRAYER_MERGED;
import static pk.tappdesign.knizka.utils.Navigation.RANDOM;
import static pk.tappdesign.knizka.utils.Navigation.REMINDERS;
import static pk.tappdesign.knizka.utils.Navigation.UNCATEGORIZED;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import pk.tappdesign.knizka.async.bus.CategoriesUpdatedEvent;
import pk.tappdesign.knizka.async.bus.NavigationUpdatedNavDrawerClosedEvent;
import pk.tappdesign.knizka.async.bus.NotesDuplicatedEvent;
import pk.tappdesign.knizka.async.bus.NotesLoadedEvent;
import pk.tappdesign.knizka.async.bus.NotesMergeEvent;
import pk.tappdesign.knizka.async.bus.PasswordRemovedEvent;
import pk.tappdesign.knizka.async.notes.NoteLoaderTask;
import pk.tappdesign.knizka.async.notes.NoteProcessorArchive;
import pk.tappdesign.knizka.async.notes.NoteProcessorCategorize;
import pk.tappdesign.knizka.async.notes.NoteProcessorDelete;
import pk.tappdesign.knizka.async.notes.NoteProcessorDuplicate;
import pk.tappdesign.knizka.async.notes.NoteProcessorTrash;
import pk.tappdesign.knizka.async.notes.SaveLinkedNoteTask;
import pk.tappdesign.knizka.databinding.FragmentListBinding;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.helpers.NotesHelper;
import pk.tappdesign.knizka.models.Category;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.ONStyle;
import pk.tappdesign.knizka.models.PasswordValidator;
import pk.tappdesign.knizka.models.Tag;
import pk.tappdesign.knizka.models.UndoBarController;
import pk.tappdesign.knizka.models.adapters.CategoryRecyclerViewAdapter;
import pk.tappdesign.knizka.models.adapters.NoteAdapter;
import pk.tappdesign.knizka.models.listeners.OnLinkedNoteAdded;
import pk.tappdesign.knizka.models.listeners.OnViewTouchedListener;
import pk.tappdesign.knizka.models.listeners.RecyclerViewItemClickSupport;
import pk.tappdesign.knizka.models.views.Fab;
import pk.tappdesign.knizka.utils.AnimationsHelper;
import pk.tappdesign.knizka.utils.Display;
import pk.tappdesign.knizka.utils.IntentChecker;
import pk.tappdesign.knizka.utils.KeyboardUtils;
import pk.tappdesign.knizka.utils.Navigation;
import pk.tappdesign.knizka.utils.PasswordHelper;
import pk.tappdesign.knizka.utils.ReminderHelper;
import pk.tappdesign.knizka.utils.TagsHelper;
import pk.tappdesign.knizka.utils.TextHelper;
import it.feio.android.pixlui.links.UrlCompleter;
import it.feio.android.simplegallery.util.BitmapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.ObjectUtils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import pk.tappdesign.knizka.async.bus.NotesUpdatedEvent;

public class ListFragment extends BaseFragment implements OnViewTouchedListener, UndoBarController.UndoListener, OnLinkedNoteAdded {

  private static final int REQUEST_CODE_CATEGORY = 1;
  private static final int REQUEST_CODE_CATEGORY_NOTES = 2;
  private static final int REQUEST_CODE_ADD_ALARMS = 3;
  public static final String LIST_VIEW_POSITION = "listViewPosition";
  public static final String LIST_VIEW_POSITION_OFFSET = "listViewPositionOffset";

  private FragmentListBinding binding;

  private List<Note> selectedNotes = new ArrayList<>();
  private SearchView searchView;
  private MenuItem searchMenuItem;
  private Menu menu;
  private AnimationDrawable jinglesAnimation;
  private int listViewPosition;
  private int listViewPositionOffset = 16;
  private boolean sendToArchive;
  private ListFragment mFragment;
  private ActionMode actionMode;
  private boolean keepActionMode = false;

  // Undo archive/trash
  private boolean undoTrash = false;
  private boolean undoArchive = false;
  private boolean undoCategorize = false;
  private Category undoCategorizeCategory = null;
  private SortedMap<Integer, Note> undoNotesMap = new TreeMap<>();
  // Used to remember removed categories from notes
  private Map<Note, Category> undoCategoryMap = new HashMap<>();
  // Used to remember archived state from notes
  private Map<Note, Boolean> undoArchivedMap = new HashMap<>();

  // Search variables
  private String searchQuery;
  private String searchQueryInstant;
  private String searchTags;
  private boolean searchUncompleteChecklists;
  private boolean goBackOnToggleSearchLabel = false;
  private boolean searchLabelActive = false;

  private NoteAdapter listAdapter;
  private UndoBarController ubc;
  private Fab fab;
  private MainActivity mainActivity;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFragment = this;
    setHasOptionsMenu(true);
    setRetainInstance(true);
    EventBus.getDefault().register(this, 1);
  }


  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }


  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      if (savedInstanceState.containsKey(LIST_VIEW_POSITION)) {
        listViewPosition = savedInstanceState.getInt(LIST_VIEW_POSITION);
        listViewPositionOffset = savedInstanceState.getInt(LIST_VIEW_POSITION_OFFSET);
        searchQuery = savedInstanceState.getString("searchQuery");
        searchTags = savedInstanceState.getString("searchTags");
      }
      keepActionMode = false;
    }
    binding = FragmentListBinding.inflate(inflater, container, false);
    View view = binding.getRoot();

    binding.list.setHasFixedSize(true);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    binding.list.setLayoutManager(linearLayoutManager);

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.list.getContext(),
        linearLayoutManager.getOrientation());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      dividerItemDecoration.setDrawable(Knizka.getAppContext().getDrawable(R.drawable.fragment_list_item_divider));
    }
    //dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.fragment_list_item_divider));

    binding.list.addItemDecoration(dividerItemDecoration);

    RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
    itemAnimator.setAddDuration(1000);
    itemAnimator.setRemoveDuration(1000);
    binding.list.setItemAnimator(itemAnimator);

    // Replace listview with Mr. Jingles if it is empty
    binding.list.setEmptyView(binding.emptyList);

    return view;
  }

  @Override
  public void onActivityCreated (Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mainActivity = (MainActivity) getActivity();
    
    if (savedInstanceState != null) {
      mainActivity.navigationTmp = savedInstanceState.getString("navigationTmp");
    }
    init();
  }


  private void init () {
    initEasterEgg();
    initListView();
    ubc = new UndoBarController(binding.undobar.getRoot(), this);

    initNotesList(mainActivity.getIntent());
    initFab();
    initTitle();
    
    Display.setKeepScreenOn(mainActivity);
    //SetKeepScreenOn();
  }

  private void SetKeepScreenOn()
  {
    boolean isKeepScreenOn = Prefs.getBoolean(PREF_KEEP_SCREEN_ON, PREF_KEEP_SCREEN_ON_DEFAULT);

    if (isKeepScreenOn)
    {
      mainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    } else {
      mainActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
  }

  private void initFab () {
    fab = new Fab(binding.fab.getRoot(), binding.list, Prefs.getBoolean(PREF_FAB_EXPANSION_BEHAVIOR, false));
    fab.setOnFabItemClickedListener(id -> {
      View v = mainActivity.findViewById(id);
      switch (id) {
        case R.id.fab_expand_menu_button:
        case R.id.fab_note:
          Note noteNew = new Note();
          noteNew.setPackageID(PACKAGE_USER_ADDED);
          editNote(noteNew, v);
          break;
        case R.id.fab_intent:
          Note noteIntent = new Note();
          noteIntent.setPackageID((long)PACKAGE_USER_INTENT);
          editNote(noteIntent, v);
          break;
        case R.id.fab_camera:
          Intent i = mainActivity.getIntent();
          i.setAction(ACTION_FAB_TAKE_PHOTO);
          mainActivity.setIntent(i);
          Note notePhoto = new Note();
          notePhoto.setPackageID(PACKAGE_USER_ADDED);
          editNote(notePhoto, v);
          break;
       // case R.id.fab_checklist:
       //   Note note = new Note();
       //   note.setChecklist(true);
       //  editNote(note, v);
       //  break;

      }
    });
  }


  boolean closeFab () {
    if (fab != null && fab.isExpanded()) {
      fab.performToggle();
      return true;
    }
    return false;
  }


  /**
   * Activity title initialization based on navigation
   */
  private void initTitle () {
    String[] navigationListActivityCaptions = getResources().getStringArray(R.array.navigation_list_activity_caption);
  //  String[] navigationList = getResources().getStringArray(R.array.navigation_list);
    String[] navigationListCodes = getResources().getStringArray(R.array.navigation_list_codes);
    String navigation = mainActivity.navigationTmp != null ? mainActivity.navigationTmp : Prefs.getString
        (PREF_NAVIGATION, navigationListCodes[0]);
    int index = Arrays.asList(navigationListCodes).indexOf(navigation);
    String title;
    // If is a traditional navigation item
    if (index >= 0 && index < navigationListCodes.length) {
      title = navigationListActivityCaptions[index];
    } else {
      Category category = DbHelper.getInstance().getCategory(Long.parseLong(navigation));
      title = category != null ? category.getName() : "";
    }
    title = title == null ? getString(R.string.title_activity_list) : title;
    mainActivity.setActionBarTitle(title);
  }


  /**
   * Starts a little animation on Mr.Jingles!
   */
  private void initEasterEgg () {
    binding.emptyList.setOnClickListener(v -> {
      if (jinglesAnimation == null) {
        jinglesAnimation = (AnimationDrawable) binding.emptyList.getCompoundDrawables()[1];
        binding.emptyList.post(() -> {
          if (jinglesAnimation != null) {
            jinglesAnimation.start();
          }
        });
      } else {
        stopJingles();
      }
    });
  }


  private void stopJingles () {
    if (jinglesAnimation != null) {
      jinglesAnimation.stop();
      jinglesAnimation = null;
      binding.emptyList.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.jingles_animation, 0, 0);

    }
  }


  @Override
  public void onPause() {
    super.onPause();
    searchQueryInstant = searchQuery;
    stopJingles();
    Crouton.cancelAllCroutons();
    closeFab();
    if (!keepActionMode) {
      commitPending();
//      list.clearChoices();
      if (getActionMode() != null) {
        getActionMode().finish();
      }
    }
  }


  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    refreshListScrollPosition();
    outState.putInt("listViewPosition", listViewPosition);
    outState.putInt(LIST_VIEW_POSITION_OFFSET, listViewPositionOffset);
    outState.putString("searchQuery", searchQuery);
    outState.putString("searchTags", searchTags);
  }


  private void refreshListScrollPosition () {
    if ((binding != null) && (binding.list != null)) {
      listViewPosition = ((LinearLayoutManager) binding.list.getLayoutManager())
          .findFirstVisibleItemPosition();
      View v = binding.list.getChildAt(0);
      listViewPositionOffset =
          (v == null) ? (int) getResources().getDimension(R.dimen.vertical_margin) : v.getTop();
    }
  }


  @Override
  public void onResume () {
    super.onResume();
    if (mainActivity.prefsChanged) {
      mainActivity.prefsChanged = false;
      init();
    } else if (Intent.ACTION_SEARCH.equals(mainActivity.getIntent().getAction())) {
      initNotesList(mainActivity.getIntent());
    }
  }


  private final class ModeCallback implements ActionMode.Callback {

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
      // Inflate the menu for the CAB
      MenuInflater inflater = mode.getMenuInflater();
      inflater.inflate(R.menu.menu_list, menu);
      actionMode = mode;
      fab.setAllowed(isFabAllowed());
      fab.hideFab();
      return true;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {
      // Here you can make any necessary updates to the activity when
      // the CAB is removed. By default, selected items are
      // deselected/unchecked.
      for (int i = 0; i < listAdapter.getSelectedItems().size(); i++) {
        int key = listAdapter.getSelectedItems().keyAt(i);
//        View v = list.getChildAt(key - list.getFirstVisiblePosition());
//        if (listAdapter.getCount() > key && listAdapter.getItem(key) != null && v != null) {
//          listAdapter.restoreDrawable(listAdapter.getItem(key), v.findViewById(R.id.card_layout));
//        }
      }

      selectedNotes.clear();
      listAdapter.clearSelectedItems();
//      list.clearChoices();
      listAdapter.notifyDataSetChanged();

      fab.setAllowed(isFabAllowed(true));
      if (undoNotesMap.size() == 0) {
        fab.showFab();
      }

      actionMode = null;
      LogDelegate.d("Closed multiselection contextual menu");
    }


    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
      prepareActionModeMenu();
      return true;
    }


    @Override
    public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
      Integer[] protectedActions = {R.id.menu_select_all, R.id.menu_merge};
      if (!Arrays.asList(protectedActions).contains(item.getItemId())) {
        mainActivity.requestPassword(mainActivity, getSelectedNotes(),
            passwordConfirmed -> {
              if (passwordConfirmed.equals(PasswordValidator.Result.SUCCEED)) {
                performAction(item, mode);
              }
            });
      } else {
        performAction(item, mode);
      }
      return true;
    }
  }


  public void finishActionMode() {
    if (getActionMode() != null) {
      getActionMode().finish();
    }
  }


  /**
   * Manage check/uncheck of notes in list during multiple selection phase
   */
  private void toggleListViewItem(View view, int position) {
    Note note = listAdapter.getItem(position);
    LinearLayout cardLayout = view.findViewById(R.id.card_layout);
    if (!getSelectedNotes().contains(note)) {
      getSelectedNotes().add(note);
      listAdapter.addSelectedItem(position);
      cardLayout.setBackgroundColor(getResources().getColor(R.color.list_bg_selected));
    } else {
      getSelectedNotes().remove(note);
      listAdapter.removeSelectedItem(position);
      listAdapter.restoreDrawable(note, cardLayout);
    }
    prepareActionModeMenu();

    if (getSelectedNotes().isEmpty()) {
      finishActionMode();
    }

  }


  /**
   * Notes list initialization. Data, actions and callback are defined here.
   */
  private void initListView() {
    // Note long click to start CAB mode
    RecyclerViewItemClickSupport.addTo(binding.list)
        // Note single click listener managed by the activity itself
        .setOnItemClickListener((recyclerView, position, view) -> {
          if (getActionMode() == null) {
            editNote(listAdapter.getItem(position), view);
            return;
          }
          // If in CAB mode
          toggleListViewItem(view, position);
          setCabTitle();
        }).setOnItemLongClickListener((recyclerView, position, view) -> {
      if (getActionMode() != null) {
        return false;
      }
      // Start the CAB using the ActionMode.Callback defined above
      mainActivity.startSupportActionMode(new ModeCallback());
      toggleListViewItem(view, position);
      setCabTitle();
      return true;
    });

    binding.listRoot.setOnViewTouchedListener(this);
  }


  /**
   * Retrieves from the single listview note item the element to be zoomed when opening a note
   */
  private ImageView getZoomListItemView(View view, Note note) {
    View targetView = null;
    if (!note.getAttachmentsList().isEmpty()) {
      targetView = view.findViewById(R.id.attachmentThumbnail);
    }
    if (targetView == null && note.getCategory() != null) {
      targetView = view.findViewById(R.id.category_marker);
    }
    if (targetView == null) {
      targetView = new ImageView(mainActivity);
      targetView.setBackgroundColor(Color.WHITE);
    }
    targetView.setDrawingCacheEnabled(true);
    targetView.buildDrawingCache();
    Bitmap bmp = targetView.getDrawingCache();
    binding.expandedImage.setBackgroundColor(BitmapUtils.getDominantColor(bmp));

    return binding.expandedImage;
  }


  /**
   * Listener that fires note opening once the zooming animation is finished
   */
  private AnimatorListenerAdapter buildAnimatorListenerAdapter(final Note note) {
    return new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        editNote2(note);
      }
    };
  }


  @Override
  public void onViewTouchOccurred(MotionEvent ev) {
    LogDelegate.v("Notes list: onViewTouchOccurred " + ev.getAction());
    commitPending();
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_list, menu);
    super.onCreateOptionsMenu(menu, inflater);
    this.menu = menu;
    initSearchView(menu);
  }


  private void initSortingSubmenu() {
    final String[] arrayDb = getResources().getStringArray(R.array.sortable_columns);
    final String[] arrayDialog = getResources().getStringArray(R.array.sortable_columns_human_readable);
    int selected = Arrays.asList(arrayDb).indexOf(Prefs.getString(PREF_SORTING_COLUMN, arrayDb[0]));

    SubMenu sortMenu = this.menu.findItem(R.id.menu_sort).getSubMenu();
    for (int i = 0; i < arrayDialog.length; i++) {
      if (sortMenu.findItem(i) == null) {
        sortMenu.add(MENU_SORT_GROUP_ID, i, i, arrayDialog[i]);
      }
      if (i == selected) {
        sortMenu.getItem(i).setChecked(true);
      }
    }
    sortMenu.setGroupCheckable(MENU_SORT_GROUP_ID, true, true);
  }


  @Override
  public void onPrepareOptionsMenu(Menu menu) {
    setActionItemsVisibility(menu, false);
    if ( Navigation.getNavigation() == JKS_NUMBER_SEARCH)
    {
      if (!Prefs.getBoolean("settings_always_show_JKS_number_search", false))
      {
        String navigationText = getResources().getStringArray(R.array.navigation_list_codes)[JKS];
        Prefs.edit().putString(PREF_NAVIGATION, navigationText).apply();
        mainActivity.setActionBarTitle(navigationText);
      }
      showDialogForNumbers();
    }
  }

  private boolean isAchievable(int navigation)
  {
    boolean result ;
    switch (navigation)
    {
      case NOTES:
      case REMINDERS:
      case UNCATEGORIZED:
      case CATEGORY:
      case FAVORITES:
      case LAST_SHOWN:
      case RANDOM:
      case PRAYER_MERGED:
      case JKS:
      case JKS_NUMBER_SEARCH:
      case JKS_CATEGORIES:
      case INTENTIONS:
      case PRAYER_LINKED_SET:
        result = true;
        break;
      default:
        result = false;
        break;
    }
    return result;
  }

  private void prepareActionModeMenu() {
    Menu menu = getActionMode().getMenu();
    int navigation = Navigation.getNavigation();
    boolean showArchive = isAchievable(navigation);
    boolean showUnarchive = navigation == Navigation.ARCHIVE || navigation == UNCATEGORIZED ||
        navigation == CATEGORY;

    if (navigation == Navigation.TRASH) {
      menu.findItem(R.id.menu_untrash).setVisible(true);
      menu.findItem(R.id.menu_delete).setVisible(true);
    } else {
      if (getSelectedCount() == 1) {
        menu.findItem(R.id.menu_share_list).setVisible(true);
        menu.findItem(R.id.menu_merge).setVisible(false);
        menu.findItem(R.id.menu_archive)
            .setVisible(showArchive && !getSelectedNotes().get(0).isArchived
                ());
        menu.findItem(R.id.menu_unarchive)
            .setVisible(showUnarchive && getSelectedNotes().get(0).isArchived
                ());
      } else {
        menu.findItem(R.id.menu_share_list).setVisible(false);
        menu.findItem(R.id.menu_merge).setVisible(true);
        menu.findItem(R.id.menu_archive).setVisible(showArchive);
        menu.findItem(R.id.menu_unarchive).setVisible(showUnarchive);

      }
      menu.findItem(R.id.menu_add_reminder).setVisible(true);
      menu.findItem(R.id.menu_category_list).setVisible(true);
      menu.findItem(R.id.menu_uncomplete_checklists).setVisible(false);
      menu.findItem(R.id.menu_tags).setVisible(true);
      menu.findItem(R.id.menu_trash).setVisible(true);
      menu.findItem(R.id.menu_duplicate).setVisible(true);
      menu.findItem(R.id.menu_add_to_linked_set).setVisible(true);
    }
    menu.findItem(R.id.menu_select_all).setVisible(true);
    setCabTitle();
  }


  private int getSelectedCount() {
    return getSelectedNotes().size();
  }


  private void setCabTitle() {
    if (getActionMode() != null) {
      int title = getSelectedCount();
      getActionMode().setTitle(String.valueOf(title));
    }
  }


  /**
   * SearchView initialization. It's a little complex because it's not using SearchManager but is implementing on its
   * own.
   */
  @SuppressLint("NewApi")
  private void initSearchView (final Menu menu) {

    // Prevents some mysterious NullPointer on app fast-switching
    if (mainActivity == null) {
      return;
    }

    // Save item as class attribute to make it collapse on drawer opening
    searchMenuItem = menu.findItem(R.id.menu_search);

    Bundle args = getArguments();
    if (args != null) {
      Boolean setSearchFocus = args.getBoolean("setSearchFocus");
      if (setSearchFocus == true) {
        searchMenuItem.expandActionView();
        KeyboardUtils.hideKeyboard(this.getView());
      }
    }

    // Associate searchable configuration with the SearchView
    SearchManager searchManager = (SearchManager) mainActivity.getSystemService(Context.SEARCH_SERVICE);
    searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
    searchView.setSearchableInfo(searchManager.getSearchableInfo(mainActivity.getComponentName()));
    searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

    // Expands the widget hiding other actionbar icons
    searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> setActionItemsVisibility(menu, hasFocus));

    MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {

          boolean searchPerformed = false;


          @Override
          public boolean onMenuItemActionCollapse(MenuItem item) {
            // Reinitialize notes list to all notes when search is collapsed
            searchQuery = null;
            if (binding.searchLayout.getVisibility() == View.VISIBLE) {
              toggleSearchLabel(false);
            }
            mainActivity.getIntent().setAction(Intent.ACTION_MAIN);
            initNotesList(mainActivity.getIntent());
            mainActivity.supportInvalidateOptionsMenu();
            return true;
          }


      @Override
      public boolean onMenuItemActionExpand (MenuItem item) {

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit (String arg0) {

            return Prefs.getBoolean("settings_instant_search", false);
          }


          @Override
          public boolean onQueryTextChange (String pattern) {

            if (Prefs.getBoolean("settings_instant_search", false) && binding.searchLayout != null &&
                searchPerformed && mFragment.isAdded()) {
              searchTags = null;
              searchQuery = pattern;
              NoteLoaderTask.getInstance().execute("getNotesByPattern", pattern);
              return true;
            } else {
              searchPerformed = true;
              return false;
            }
          }
        });
        return true;
      }
    });
  }

  private void setJKSSortingMenuVisibility(Menu menu, boolean isJKSNavigation)
  {

    menu.findItem(R.id.menu_sort_jks_by_number).setVisible(false);
    menu.findItem(R.id.menu_sort_jks_by_name).setVisible(false);

    if (isJKSNavigation)
    {
      String jksSortingType = Prefs.getString(PREF_JKS_SORTING_TYPE, PREF_JKS_SORTING_TYPE_DEFAULT);

      switch (jksSortingType)
      {
        default:
        case JKS_SORTING_TYPE_NUMBER:
          menu.findItem(R.id.menu_sort_jks_by_name).setVisible(true);
          break;
        case JKS_SORTING_TYPE_NAME:
          menu.findItem(R.id.menu_sort_jks_by_number).setVisible(true);
          break;
      }
    }

  }
   private void setJKSCategoriesTextVisibility(boolean isJKSNavigation)
   {
      if (isJKSNavigation)
      {
         int jksCatId = Prefs.getInt(PREF_NAVIGATION_JKS_CATEGORY_ID, PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT);
         if (jksCatId <= PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT)
         {
            binding.categoryLayout.setVisibility(View.GONE);
         } else {
            binding.categoryLayout.setVisibility(View.VISIBLE);
            binding.categoryCancel.setOnClickListener(v -> clearCategories(false));
         }
         setCategoryIdText(jksCatId);

      } else {
         binding.categoryLayout.setVisibility(View.GONE);
      }
   }

  private void setActionItemsVisibility (Menu menu, boolean searchViewHasFocus) {

    boolean drawerOpen = mainActivity.getDrawerLayout() != null && mainActivity.getDrawerLayout().isDrawerOpen
        (GravityCompat.START);
    boolean expandedView = Prefs.getBoolean(PREF_EXPANDED_VIEW, true);

    int navigation = Navigation.getNavigation();
    boolean navigationReminders = navigation == REMINDERS;
    boolean navigationArchive = navigation == Navigation.ARCHIVE;
    boolean navigationTrash = navigation == Navigation.TRASH;
    boolean navigationCategory = navigation == CATEGORY;
    boolean navigationJKS = (navigation == Navigation.JKS) || (navigation == Navigation.JKS_NUMBER_SEARCH) || (navigation == JKS_CATEGORIES);

    boolean filterPastReminders = Prefs.getBoolean(PREF_FILTER_PAST_REMINDERS, true);
    boolean filterArchivedInCategory = navigationCategory && Prefs.getBoolean(PREF_FILTER_ARCHIVED_IN_CATEGORIES + Navigation.getCategory(), false);

    if (isFabAllowed()) {
      fab.setAllowed(true);
      fab.showFab();
    } else {
      fab.setAllowed(false);
      fab.hideFab();
    }
    menu.findItem(R.id.menu_search).setVisible(!drawerOpen);
    menu.findItem(R.id.menu_filter).setVisible(!drawerOpen && !filterPastReminders && navigationReminders &&
        !searchViewHasFocus);
    menu.findItem(R.id.menu_filter_remove).setVisible(!drawerOpen && filterPastReminders && navigationReminders
        && !searchViewHasFocus);
    menu.findItem(R.id.menu_filter_category).setVisible(!drawerOpen && !filterArchivedInCategory &&
        navigationCategory && !searchViewHasFocus);
    menu.findItem(R.id.menu_filter_category_remove).setVisible(!drawerOpen && filterArchivedInCategory &&
        navigationCategory && !searchViewHasFocus);
	//menu.findItem(R.ID.menu_sort).setVisible(!drawerOpen && !navigationReminders && !searchViewHasFocus);
    menu.findItem(R.id.menu_sort).setVisible(false); //@pk: always hide sort button, todo: maybe we can enable sorting later

    menu.findItem(R.id.menu_expanded_view).setVisible(!drawerOpen && !expandedView && !searchViewHasFocus);
    menu.findItem(R.id.menu_contracted_view).setVisible(!drawerOpen && expandedView && !searchViewHasFocus);
    menu.findItem(R.id.menu_empty_trash).setVisible(!drawerOpen && navigationTrash);
    //menu.findItem(R.id.menu_uncomplete_checklists).setVisible(searchViewHasFocus);
    menu.findItem(R.id.menu_uncomplete_checklists).setVisible(false); // @pk: always hide uncomplete check list

    menu.findItem(R.id.menu_tags).setVisible(searchViewHasFocus);
    menu.findItem(R.id.menu_search_jks_number).setVisible(!drawerOpen && navigationJKS && !searchViewHasFocus);
    menu.findItem(R.id.menu_jks_categories).setVisible(!drawerOpen && navigationJKS && !searchViewHasFocus);


    menu.findItem(R.id.menu_browse_through_texts).setVisible(true);


    setJKSSortingMenuVisibility(menu, navigationJKS);
    setJKSCategoriesTextVisibility(navigationJKS);
  }


  @Override
  public boolean onOptionsItemSelected (final MenuItem item) {
    Integer[] protectedActions = {R.id.menu_empty_trash};
    if (Arrays.asList(protectedActions).contains(item.getItemId())) {
      mainActivity.requestPassword(mainActivity, getSelectedNotes(), passwordConfirmed -> {
        if (passwordConfirmed.equals(PasswordValidator.Result.SUCCEED)) {
          performAction(item, null);
        }
      });
    } else {
      performAction(item, null);
    }
    return super.onOptionsItemSelected(item);
  }


  /**
   * Performs one of the ActionBar button's actions after checked notes protection
   */
  public void performAction(MenuItem item, ActionMode actionMode) {

    if (isOptionsItemFastClick()) {
      return;
    }

    if (actionMode == null) {
      switch (item.getItemId()) {
        case android.R.id.home:
          if (mainActivity.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            mainActivity.getDrawerLayout().closeDrawer(GravityCompat.START);
          } else {
            mainActivity.getDrawerLayout().openDrawer(GravityCompat.START);
          }
          break;
        case R.id.menu_filter:
          filterReminders(true);
          break;
        case R.id.menu_filter_remove:
          filterReminders(false);
          break;
        case R.id.menu_filter_category:
          filterCategoryArchived(true);
          break;
        case R.id.menu_filter_category_remove:
          filterCategoryArchived(false);
          break;
        case R.id.menu_uncomplete_checklists:
          filterByUncompleteChecklists();
          break;
        case R.id.menu_tags:
          filterByTags();
          break;
        case R.id.menu_sort:
          initSortingSubmenu();
          break;
        case R.id.menu_expanded_view:
          switchNotesView();
          break;
        case R.id.menu_contracted_view:
          switchNotesView();
          break;
        case R.id.menu_sort_jks_by_name:
          setJKSSortingType(JKS_SORTING_TYPE_NAME);
          break;
        case R.id.menu_sort_jks_by_number:
          setJKSSortingType(JKS_SORTING_TYPE_NUMBER);
          break;
        case R.id.menu_browse_through_texts:
          showBrowseTextsDialog();
          break;
        case R.id.menu_empty_trash:
          emptyTrash();
          break;
        case R.id.menu_search_jks_number:
          showDialogForNumbers();
          break;
        case R.id.menu_jks_categories:
          showDialogForCategories();
          break;
        default:
          LogDelegate.e("Wrong element choosen: " + item.getItemId());
      }
    } else {
      switch (item.getItemId()) {
        case R.id.menu_category_list:
          categorizeNotes();
          break;
        case R.id.menu_tags:
          tagNotes("");
          break;
        case R.id.menu_share_list:
          share();
          break;
        case R.id.menu_merge:
          merge();
          break;
        case R.id.menu_archive:
          archiveNotes(true);
          break;
        case R.id.menu_unarchive:
          archiveNotes(false);
          break;
        case R.id.menu_trash:
          trashNotes(true);
          break;
        case R.id.menu_untrash:
          trashNotes(false);
          break;
        case R.id.menu_delete:
          deleteNotes();
          break;
        case R.id.menu_select_all:
          selectAllNotes();
          break;
        case R.id.menu_add_reminder:
          addReminders();
          break;
        case R.id.menu_duplicate:
          duplicateNotes();
          break;
        case R.id.menu_add_to_linked_set:
          addNotesToLinkedSet();
          break;
        case R.id.menu_search_jks_number:
          showDialogForNumbers();
          break;
//                case R.ID.menu_synchronize:
//                    synchronizeSelectedNotes();
//                    break;
        default:
          LogDelegate.e("Wrong element choosen: " + item.getItemId());
      }
    }

    checkSortActionPerformed(item);
  }


  private void duplicateNote(List<Note> notes) {

    new NoteProcessorDuplicate(notes).process();

    finishActionMode();

    getSelectedNotes().clear();
  }

  private void duplicateNotes () {
    LogDelegate.d("Start duplicate notes");
    duplicateNote(getSelectedNotes());
  }

  private void addNotesToLinkedSet () {
    LogDelegate.d("Start additing notes to linked set");
    showDialogForPrayerSetSelecting();
  }

  private void saveToLinkedTask(Note note)
  {
    if (note.getPackageID() == PACKAGE_SYSTEM)
    {
      note = DbHelper.getInstance().duplicateNote(note);
      mainActivity.showWarningDialogForSystemEditing(getActivity());
    }
    new SaveLinkedNoteTask(this, getSelectedNotes()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, note.getHandleID());
  }

  private void getPrayerSetName(AlertDialog dialogParent)
  {
    MaterialAlertDialogBuilder chooseLinkedSetDialog = new MaterialAlertDialogBuilder(getActivity())
            .setTitle(R.string.choose_prayer_set_name)
            .setView(R.layout.dialog_edit_text)
            .setPositiveButton(R.string.ok, (dialog, which) -> {

            })
            .setNegativeButton(R.string.btn_dlg_cancel, (dialog, which) -> {

            });

    AlertDialog dialogRef = chooseLinkedSetDialog.show();

    EditText edtText = dialogRef.findViewById(R.id.edt_dialog_edit_text);
    new Handler().postDelayed(() -> KeyboardUtils.showKeyboard(edtText), 100);

    dialogRef.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
    dialogRef.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Note noteNew = new Note();
        noteNew.setPackageID(PACKAGE_USER_ADDED);
        noteNew.setPrayerMerged(PRAYER_MERGED_LINKED_SET);
        noteNew.setTitle(edtText.getText().toString());
        Note noteNewSaved = DbHelper.getInstance().updateNote(noteNew, false);
        saveToLinkedTask(noteNewSaved);
        dialogRef.dismiss();
        if (dialogParent != null)
        {
          dialogParent.dismiss();
        }
      }

    });
    edtText.setHint(R.string.edt_dlg_hint);
    edtText.addTextChangedListener(new TextWatcher() {

      @Override
      public void afterTextChanged(Editable s) {

      }

      @Override
      public void beforeTextChanged(CharSequence s, int start,
                                    int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start,
                                int before, int count) {
        if(s.length() == 0)
        {
          dialogRef.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        } else {
          dialogRef.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
        }
      }
    });

  }

  private void showDialogForPrayerSetSelecting() {

    final ArrayList<Note> linkedSets = DbHelper.getInstance().getLinkedSets();

    List<String> listOfPrayerSets = new ArrayList<>();

    for (Note note : linkedSets) {
      listOfPrayerSets.add(note.getTitle());
    }
    if (listOfPrayerSets.isEmpty())
    {
      getPrayerSetName(null);
    } else {
      MaterialAlertDialogBuilder chooseLinkedSetDialog = new MaterialAlertDialogBuilder(getActivity())
              .setTitle(R.string.choose_prayer_set)
              .setSingleChoiceItems(listOfPrayerSets.toArray(new CharSequence[listOfPrayerSets.size()]), -1, (dialog, position) -> {
                ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
              })
              .setPositiveButton(R.string.btn_add_to_prayer_set, (dialog, which) -> {
                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                if (position >= 0) {
                  Note note = linkedSets.get(position);

                  saveToLinkedTask(note);
                }
              })
              .setNegativeButton(R.string.btn_new_prayer_set, (dialog, which) -> {
                // onClickListener will be added later, to avoid autodismiss of dialog
              });

      AlertDialog dialogRef = chooseLinkedSetDialog.show();
      dialogRef.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
      dialogRef.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          getPrayerSetName((AlertDialog) dialogRef);
        }
      });
    }
  }


  private void addReminders () {
    Intent intent = new Intent(Knizka.getAppContext(), SnoozeActivity.class);
    intent.setAction(ACTION_POSTPONE);
    intent.putExtra(INTENT_NOTE, selectedNotes.toArray(new Note[selectedNotes.size()]));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivityForResult(intent, REQUEST_CODE_ADD_ALARMS);
  }

  private boolean loadSongDirect(String songNumber)
  {
    boolean result = false;
    List<Note> notes = DbHelper.getInstance().getNotesByJKSNumber(songNumber);
    if (!notes.isEmpty())
    {
      mainActivity.switchToDetail(notes.get(0));
      result = true;
    } else {
      mainActivity.showMessage(R.string.song_not_found, ONStyle.ALERT);
    }
    return result;
  }


  private void showDialogForCategories () {

    MaterialAlertDialogBuilder importDialog = new MaterialAlertDialogBuilder(getActivity())
            .setTitle(R.string.dialog_JKS_categories_caption)
            .setItems(getResources().getStringArray(R.array.jks_songs_category), (dialog, position) -> {

              int[] categoryValues = getResources().getIntArray(R.array.jks_songs_category_values);
              NoteLoaderTask.getInstance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getJKSByCategories",
                      String.valueOf(categoryValues[position]));
              Prefs.edit().putInt(PREF_NAVIGATION_JKS_CATEGORY_ID, categoryValues[position]).commit();
              setJKSCategoriesTextVisibility(true);
            });

    importDialog.show();

  }

  private void showDialogForNumbers () {

    LayoutInflater inflater = mainActivity.getLayoutInflater();
    final View v = inflater.inflate(R.layout.dialog_jks_numbers, null);
    final EditText textNumber = (EditText) v.findViewById(R.id.password_field);

    MaterialDialog dialog = new MaterialDialog.Builder(mainActivity)
            .autoDismiss(false)
            .title(R.string.insert_jks_number)
            .customView(v, false)
            .positiveText(R.string.ok)
            .positiveColorAttr(R.attr.themeDialogNormalButtonColor)
            .onPositive((dialog12, which) -> {
              String songNumber = textNumber.getText().toString();
              if (!songNumber.isEmpty())
              {
                if (loadSongDirect(songNumber))
                {
                  dialog12.dismiss();
                }
              } else {
                //dialog12.dismiss();
              }
            })
            .build();

    textNumber.addTextChangedListener(new TextWatcher() {

      @Override
      public void afterTextChanged(Editable s) {}

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(textNumber.getText(). length() == 3) {
          String txtNum = textNumber.getText().toString();
          if (loadSongDirect(txtNum))
          {
            dialog.dismiss();
          }
        }
      }
    });

    dialog.show();
  }

  private void switchNotesView() {
    boolean expandedView = Prefs.getBoolean(PREF_EXPANDED_VIEW, true);
    Prefs.edit().putBoolean(PREF_EXPANDED_VIEW, !expandedView).commit();
    // Change list view
    initNotesList(mainActivity.getIntent());
    // Called to switch menu voices
    mainActivity.supportInvalidateOptionsMenu();
  }

  private void setJKSSortingType(String jksSortingType)
  {
    Prefs.edit().putString(PREF_JKS_SORTING_TYPE, jksSortingType).apply();
    // Change list view
    initNotesList(mainActivity.getIntent());
    // Called to switch menu voices
    mainActivity.supportInvalidateOptionsMenu();
  }

  private void showBrowseTextsDialog()
  {
    String actTitle = "";
    ArrayList<String> notesIds = new ArrayList<>();
    for (int i = 0; i < listAdapter.getItemCount(); i++) {
      notesIds.add(String.valueOf(listAdapter.getItem(i).getHandleID().longValue()));
    }

    if (notesIds.isEmpty())
    {
      mainActivity.showMessage(R.string.jks_list_is_empty, ONStyle.ALERT);
    } else {
     // int CurrListViewPos = ((LinearLayoutManager)binding.list.getLayoutManager()).findFirstVisibleItemPosition();
      listViewPosition = ((LinearLayoutManager)binding.list.getLayoutManager()).findFirstVisibleItemPosition();;
      Intent browseTextsFormatIntent = new Intent(getActivity(), BrowseTextsActivity.class);
      browseTextsFormatIntent.putExtra(INTENT_EXTRA_MAX_PAGES_IN_BROWSER, listAdapter.getItemCount());
      browseTextsFormatIntent.putExtra(INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER, notesIds);
      browseTextsFormatIntent.putExtra(INTENT_EXTRA_LIST_VIEW_POSITION_OFFSET_FOR_VIEWPAGER, listViewPosition);

      if (mainActivity.getSupportActionBar() != null) {
        actTitle = mainActivity.getSupportActionBar().getTitle().toString();
      }
      browseTextsFormatIntent.putExtra(INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER, actTitle);

      startActivity(browseTextsFormatIntent);
    }

  }

  void editNote(final Note note, final View view) {
    if (note.isLocked() && !Prefs.getBoolean("settings_password_access", false)) {
      PasswordHelper.requestPassword(mainActivity, passwordConfirmed -> {
        if (passwordConfirmed.equals(PasswordValidator.Result.SUCCEED)) {
          note.setPasswordChecked(true);
          AnimationsHelper.zoomListItem(mainActivity, view, getZoomListItemView(view, note),
              binding.listRoot, buildAnimatorListenerAdapter(note));
        }
      });
    } else {
      AnimationsHelper.zoomListItem(mainActivity, view, getZoomListItemView(view, note),
          binding.listRoot, buildAnimatorListenerAdapter(note));
    }
  }


  void editNote2(Note note) {
    if (note.get_id() == null) {
      LogDelegate.d("Adding new note");
      // if navigation is a category it will be set into note
      try {
        if (Navigation.checkNavigation(CATEGORY) || !TextUtils.isEmpty(mainActivity.navigationTmp)) {
          if (note.getPackageID() != PACKAGE_USER_INTENT) // do not set category to intent directly, could be assigned later
          {
            String categoryId = ObjectUtils.defaultIfNull(mainActivity.navigationTmp,
                    Navigation.getCategory().toString());
            note.setCategory(DbHelper.getInstance().getCategory(Long.parseLong(categoryId)));
          }
        }
      } catch (NumberFormatException e) {
        LogDelegate.v("Maybe was not a category!");
      }
    } else {
      LogDelegate.d("Editing note with ID: " + note.get_id());
    }

    // Current list scrolling position is saved to be restored later
    refreshListScrollPosition();

    // Fragments replacing
    mainActivity.switchToDetail(note);
  }


  @Override
  public// Used to show a Crouton dialog after saved (or tried to) a note
  void onActivityResult(int requestCode, final int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    switch (requestCode) {
      case REQUEST_CODE_CATEGORY:
        // Dialog retarded to give time to activity's views of being completely initialized
        // The dialog style is choosen depending on result code
        switch (resultCode) {
          case Activity.RESULT_OK:
            mainActivity.showMessage(R.string.category_saved, ONStyle.CONFIRM);
            EventBus.getDefault().post(new CategoriesUpdatedEvent());
            break;
          case Activity.RESULT_FIRST_USER:
            mainActivity.showMessage(R.string.category_deleted, ONStyle.ALERT);
            break;
          default:
            break;
        }

        break;

      case REQUEST_CODE_CATEGORY_NOTES:
        if (intent != null) {
          Category tag = intent.getParcelableExtra(INTENT_CATEGORY);
          categorizeNotesExecute(tag);
        }
        break;

      case REQUEST_CODE_ADD_ALARMS:
        selectedNotes.clear();
        finishActionMode();
        break;

      default:
        break;
    }

  }


  private void checkSortActionPerformed(MenuItem item) {
    if (item.getGroupId() == MENU_SORT_GROUP_ID) {
      final String[] arrayDb = getResources().getStringArray(R.array.sortable_columns);
      Prefs.edit().putString(PREF_SORTING_COLUMN, arrayDb[item.getOrder()]).apply();
      initNotesList(mainActivity.getIntent());
      // Resets list scrolling position
      listViewPositionOffset = 16;
      listViewPosition = 0;
      restoreListScrollPosition();
      toggleSearchLabel(false);
      // Updates app widgets
      mainActivity.updateWidgets();
    } else {
      ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackActionFromResourceId(getActivity(),
          item.getItemId());
    }
  }


  /**
   * Empties trash deleting all the notes
   */
  private void emptyTrash() {
    new MaterialDialog.Builder(mainActivity)
        .content(R.string.empty_trash_confirmation)
        .positiveText(R.string.ok)
        .onPositive((dialog, which) -> {
          boolean mustDeleteLockedNotes = false;
          for (int i = 0; i < listAdapter.getItemCount(); i++) {
            selectedNotes.add(listAdapter.getItem(i));
            mustDeleteLockedNotes = mustDeleteLockedNotes || listAdapter.getItem(i).isLocked();
          }
          if (mustDeleteLockedNotes) {
            mainActivity.requestPassword(mainActivity, getSelectedNotes(),
                passwordConfirmed -> {
                  if (passwordConfirmed.equals(PasswordValidator.Result.SUCCEED)) {
                    deleteNotesExecute();
                  }
                });
          } else {
            deleteNotesExecute();
          }
        }).build().show();
  }

  private void setCategoryIdText(int jksCatId)
  {
    if (jksCatId <= PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT)
    {
      binding.categoryText.setText(getResources().getString(R.string.jks_category_all));
    } else {
      binding.categoryText.setText( Html.fromHtml(getResources().getString(R.string.jks_category_prefix) + " <B>" + getResources().getStringArray(R.array.jks_songs_category)[jksCatId-1000-1] + "</B>"));
    }
  }

  /**
   * Notes list adapter initialization and association to view
   *
   * @FIXME: This method is a divine disgrace and MUST be refactored. I'm ashamed by myself.
   */
  void initNotesList (Intent intent) {
    LogDelegate.d("initNotesList intent: " + intent.getAction());

    binding.progressWheel.setAlpha(1);
    binding.list.setAlpha(0);

    // Search for a tag
    // A workaround to simplify it's to simulate normal search
    if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getCategories() != null
        && intent.getCategories().contains(Intent.CATEGORY_BROWSABLE)) {
      searchTags = intent.getDataString().replace(UrlCompleter.HASHTAG_SCHEME, "");
      goBackOnToggleSearchLabel = true;
    }

    if (ACTION_SHORTCUT_WIDGET.equals(intent.getAction())) {
      return;
    }

    if (Navigation.getNavigation() == RANDOM) {
      mainActivity.switchToDetail(DbHelper.getInstance().getRandom());
      //change navigation from random to "all notes"
      mainActivity.updateNavigation(mainActivity.getResources().getStringArray(R.array.navigation_list_codes)[NOTES]);
    }

    if (Navigation.getNavigation() == JKS_CATEGORIES) {
      int jksCatId = Prefs.getInt(PREF_NAVIGATION_JKS_CATEGORY_ID, PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT);
      if (jksCatId <= PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT)
      {
        showDialogForCategories();
      }
      setCategoryIdText(jksCatId);
    }

    // Searching
    searchQuery = searchQueryInstant;
    searchQueryInstant = null;
    if (searchTags != null || searchQuery != null || searchUncompleteChecklists
        || IntentChecker.checkAction(intent, Intent.ACTION_SEARCH, ACTION_SEARCH_UNCOMPLETE_CHECKLISTS)) {

      // Using tags
      if (searchTags != null && intent.getStringExtra(SearchManager.QUERY) == null) {
        searchQuery = searchTags;
        NoteLoaderTask.getInstance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getNotesByTag",
            searchQuery);
      } else if (searchUncompleteChecklists || ACTION_SEARCH_UNCOMPLETE_CHECKLISTS.equals(
          intent.getAction())) {
        searchQuery = getContext().getResources().getString(R.string.uncompleted_checklists);
        searchUncompleteChecklists = true;
        NoteLoaderTask.getInstance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getNotesByUncompleteChecklist");
      } else {
        // Get the intent, verify the action and get the query
        if (intent.getStringExtra(SearchManager.QUERY) != null) {
          searchQuery = intent.getStringExtra(SearchManager.QUERY);
          searchTags = null;
        }
        NoteLoaderTask.getInstance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getNotesByPattern",
            searchQuery);
      }

      toggleSearchLabel(true);

    } else {
      // Check if is launched from a widget with categories
      if ((ACTION_WIDGET_SHOW_LIST.equals(intent.getAction()) && intent.hasExtra(INTENT_WIDGET))
          || !TextUtils.isEmpty(mainActivity.navigationTmp)) {
        String widgetId = intent.hasExtra(INTENT_WIDGET) ? intent.getExtras().get(INTENT_WIDGET).toString()
            : null;
        if (widgetId != null) {
          String sqlCondition = Prefs.getString(PREF_WIDGET_PREFIX + widgetId, "");
          String categoryId = TextHelper.checkIntentCategory(sqlCondition);
          mainActivity.navigationTmp = !TextUtils.isEmpty(categoryId) ? categoryId : null;
        }
        intent.removeExtra(INTENT_WIDGET);
        if (mainActivity.navigationTmp != null) {
          Long categoryId = Long.parseLong(mainActivity.navigationTmp);
          NoteLoaderTask.getInstance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
              "getNotesByCategory", categoryId);
        } else {
          NoteLoaderTask.getInstance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getAllNotes", true);
        }

      } else {
        NoteLoaderTask.getInstance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "getAllNotes", true);
      }
    }
  }

   public void clearCategories (boolean activate) {
      Prefs.edit().putInt(PREF_NAVIGATION_JKS_CATEGORY_ID, PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT).commit();
      initNotesList(mainActivity.getIntent());
      setJKSCategoriesTextVisibility(false);
   }

  public void toggleSearchLabel (boolean activate) {
    if (activate) {
      binding.searchQuery.setText(Html.fromHtml(getString(R.string.search) + ":<b> " + searchQuery + "</b>"));
      binding.searchLayout.setVisibility(View.VISIBLE);
      binding.searchCancel.setOnClickListener(v -> toggleSearchLabel(false));
      searchLabelActive = true;
      binding.categoryLayout.setVisibility(View.GONE);
    } else {
      if (searchLabelActive) {
        searchLabelActive = false;
        AnimationsHelper.expandOrCollapse(binding.searchLayout, false);
        searchTags = null;
        searchQuery = null;
        searchUncompleteChecklists = false;
        if (!goBackOnToggleSearchLabel) {
          mainActivity.getIntent().setAction(Intent.ACTION_MAIN);
          if (searchView != null) {
            searchMenuItem.collapseActionView();
          }
          initNotesList(mainActivity.getIntent());
        } else {
          mainActivity.onBackPressed();
        }
        goBackOnToggleSearchLabel = false;
        if (Intent.ACTION_VIEW.equals(mainActivity.getIntent().getAction())) {
          mainActivity.getIntent().setAction(null);
        }
      }
    }
  }

  private void resetJKSCategoriesIfNeeded()
  {
    if (Navigation.getNavigation() == JKS_CATEGORIES) {
      // user clicked JKS categories on navigation drawer menu, reset it to show JKS categories dialog
      Prefs.edit().putInt(PREF_NAVIGATION_JKS_CATEGORY_ID, PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT).commit();
    }
  }

  public void onEvent(NavigationUpdatedNavDrawerClosedEvent navigationUpdatedNavDrawerClosedEvent) {
    listViewPosition = 0;
    listViewPositionOffset = 16;
    initNotesList(mainActivity.getIntent());
    setActionItemsVisibility(menu, false);
  }


  public void onEvent(CategoriesUpdatedEvent categoriesUpdatedEvent) {
    initNotesList(mainActivity.getIntent());
  }


  public void onEvent(NotesLoadedEvent notesLoadedEvent) {
    listAdapter = new NoteAdapter(mainActivity, Prefs.getBoolean(PREF_EXPANDED_VIEW, true),
        notesLoadedEvent.getNotes());

    mainActivity.setNotesList(notesLoadedEvent.getNotes());
    if (mainActivity.getSupportActionBar() != null) {
      mainActivity.setNotesListCaption(mainActivity.getSupportActionBar().getTitle().toString());// = "br";//
    }


    initSwipeGesture();

    binding.list.setAdapter(listAdapter);

    // Restores listview position when turning back to list or when navigating reminders
    if (!notesLoadedEvent.getNotes().isEmpty()) {
      if (Navigation.checkNavigation(Navigation.REMINDERS)) {
        listViewPosition = listAdapter.getClosestNotePosition();
      }
      restoreListScrollPosition();
    }

    animateListView();

    closeFab();
  }

  private void initSwipeGesture() {
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView,
          @NonNull RecyclerView.ViewHolder viewHolder,
          @NonNull RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
        int swipedPosition = viewHolder.getAdapterPosition();
        finishActionMode();
        swipeNote(swipedPosition);
      }
    };
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

    if (Navigation.getNavigation() != Navigation.UNCATEGORIZED && Prefs.getBoolean(PREF_ENABLE_SWIPE, PREF_ENABLE_SWIPE_DEFAULT)) {
      itemTouchHelper.attachToRecyclerView(binding.list);
    } else {
      itemTouchHelper.attachToRecyclerView(null);
    }
  }

  private void swipeNote(int swipedPosition) {
    try {
      Note note = listAdapter.getItem(swipedPosition);
      if (note.isLocked()) {
        PasswordHelper.requestPassword(mainActivity, passwordConfirmed -> {
          if (passwordConfirmed.equals(PasswordValidator.Result.SUCCEED)) {
            onNoteSwipedPerformAction(note);
          } else {
            onUndo(null);
          }
        });
      } else {
        onNoteSwipedPerformAction(note);
      }
    } catch (IndexOutOfBoundsException e) {
      LogDelegate.d("Please stop swiping in the zone beneath the last card");
    }
  }

  private void onNoteSwipedPerformAction(Note note) {
    getSelectedNotes().add(note);

    // Depending on settings and note status this action will...
    // ...restore
    if (Navigation.checkNavigation(Navigation.TRASH)) {
      trashNotes(false);
    }
    // ...removes category
          // remark: @pk: if we are in category list, do not remove category as it is in omninotes
//          else if (Navigation.checkNavigation(CATEGORY)) {
//            categorizeNotesExecute(null);
//          } else {

          else { // @pk: added
            // ...trash
             if (Prefs.getBoolean("settings_swipe_to_trash", false)
                || Navigation.checkNavigation(Navigation.ARCHIVE)) {
              trashNotes(true);
              // ...archive
            } else {
              archiveNotes(true);
            }
    }
  }

  public void onEvent (PasswordRemovedEvent passwordRemovedEvent) {
    initNotesList(mainActivity.getIntent());
  }

  private void animateListView () {
    if (!Knizka.isDebugBuild()) {
      animate(binding.progressWheel).setDuration(getResources().getInteger(R.integer.list_view_fade_anim)).alpha(0);
      animate(binding.list).setDuration(getResources().getInteger(R.integer.list_view_fade_anim)).alpha(1);
    } else {
      binding.progressWheel.setVisibility(View.INVISIBLE);
      binding.list.setAlpha(1);
    }
  }


  private void restoreListScrollPosition() {
    if (listAdapter.getItemCount() > listViewPosition) {
      binding.list.getLayoutManager().scrollToPosition(listViewPosition);
      new Handler().postDelayed(fab::showFab, 150);
    } else {
      binding.list.getLayoutManager().scrollToPosition(0);
    }
  }


  /**
   * Batch note trashing
   */
  public void trashNotes(boolean trash) {
    int selectedNotesSize = getSelectedNotes().size();

    // Restore is performed immediately, otherwise undo bar is shown
    if (trash) {
      trackModifiedNotes(getSelectedNotes());
      for (Note note : getSelectedNotes()) {
        listAdapter.remove(note);
        ReminderHelper.removeReminder(Knizka.getAppContext(), note);
      }
    } else {
      trashNote(getSelectedNotes(), false);
    }

    listAdapter.notifyDataSetChanged();
    finishActionMode();

    // Advice to user
    if (trash) {
      mainActivity.showMessage(R.string.note_trashed, ONStyle.WARN);
    } else {
      mainActivity.showMessage(R.string.note_untrashed, ONStyle.INFO);
    }

    // Creation of undo bar
    if (trash) {
      ubc.showUndoBar(false, selectedNotesSize + " " + getString(R.string.trashed), null);
      fab.hideFab();
      undoTrash = true;
    } else {
      getSelectedNotes().clear();
    }
  }


  private ActionMode getActionMode() {
    return actionMode;
  }


  private List<Note> getSelectedNotes() {
    return selectedNotes;
  }


  /**
   * Single note logical deletion
   */
  @SuppressLint("NewApi")
  protected void trashNote(List<Note> notes, boolean trash) {
    listAdapter.remove(notes);
    new NoteProcessorTrash(notes, trash).process();
  }


  /**
   * Selects all notes in list
   */
  private void selectAllNotes() {
    for (int i = 0; i < binding.list.getChildCount(); i++) {
      LinearLayout v = binding.list.getChildAt(i).findViewById(R.id.card_layout);
      v.setBackgroundColor(getResources().getColor(R.color.list_bg_selected));
    }
    selectedNotes.clear();
    for (int i = 0; i < listAdapter.getItemCount(); i++) {
      selectedNotes.add(listAdapter.getItem(i));
      listAdapter.addSelectedItem(i);
    }
    prepareActionModeMenu();
    setCabTitle();
  }


  /**
   * Batch note permanent deletion
   */
  private void deleteNotes() {
    new MaterialDialog.Builder(mainActivity)
        .content(R.string.delete_note_confirmation)
        .positiveText(R.string.ok)
        .onPositive((dialog, which) -> mainActivity.requestPassword(mainActivity, getSelectedNotes(),
            passwordConfirmed -> {
              if (passwordConfirmed.equals(PasswordValidator.Result.SUCCEED)) {
                deleteNotesExecute();
              }
            }))
        .build()
        .show();
  }


  /**
   * Performs notes permanent deletion after confirmation by the user
   */
  private void deleteNotesExecute () {
    listAdapter.remove(getSelectedNotes());
    new NoteProcessorDelete(getSelectedNotes()).process();
    selectedNotes.clear();
    finishActionMode();
    mainActivity.showMessage(R.string.note_deleted, ONStyle.ALERT);
    EventBus.getDefault().post(new NotesUpdatedEvent(Collections.singletonList(new Note()))); // todo: @pk check if cannot be done better, send event that menu has to be refreshed
  }


  /**
   * Batch note archiviation
   */
  public void archiveNotes(boolean archive) {
    int selectedNotesSize = getSelectedNotes().size();
    // Used in undo bar commit
    sendToArchive = archive;

    if (!archive) {
      archiveNote(getSelectedNotes(), false);
    } else {
      trackModifiedNotes(getSelectedNotes());
    }

    for (Note note : getSelectedNotes()) {
      // If is restore it will be done immediately, otherwise the undo bar will be shown
      if (archive) {
        // Saves archived state to eventually undo
        undoArchivedMap.put(note, note.isArchived());
      }

      // If actual navigation is not "Notes" the item will not be removed but replaced to fit the new state
      int navigation = Navigation.getNavigation();
      if ( isAchievable(navigation)
          || (Navigation.checkNavigation(Navigation.ARCHIVE) && !archive)
          || (Navigation.checkNavigation(CATEGORY) && Prefs.getBoolean(
          PREF_FILTER_ARCHIVED_IN_CATEGORIES + Navigation.getCategory(), false))) {
        listAdapter.remove(note);
      } else {
        note.setArchived(archive);
        listAdapter.replace(note, listAdapter.getPosition(note));
      }
    }

    listAdapter.notifyDataSetChanged();
    finishActionMode();

    // Advice to user
    int msg = archive ? R.string.note_archived : R.string.note_unarchived;
    Style style = archive ? ONStyle.WARN : ONStyle.INFO;
    mainActivity.showMessage(msg, style);

    // Creation of undo bar
    if (archive) {
      ubc.showUndoBar(false, selectedNotesSize + " " + getString(R.string.archived), null);
      fab.hideFab();
      undoArchive = true;
    } else {
      getSelectedNotes().clear();
    }
  }


  /**
   * Saves notes to be eventually restored at right position
   */
  private void trackModifiedNotes(List<Note> modifiedNotesToTrack) {
    for (Note note : modifiedNotesToTrack) {
      undoNotesMap.put(listAdapter.getPosition(note), note);
    }
  }


  private void archiveNote(List<Note> notes, boolean archive) {
    new NoteProcessorArchive(notes, archive).process();
    if (!Navigation.checkNavigation(CATEGORY)) {
      listAdapter.remove(notes);
    }
    LogDelegate.d("Notes" + (archive ? "archived" : "restored from archive"));
  }


  /**
   * Categories addition and editing
   */
  void editCategory(Category category) {
    Intent categoryIntent = new Intent(mainActivity, CategoryActivity.class);
    categoryIntent.putExtra(INTENT_CATEGORY, category);
    startActivityForResult(categoryIntent, REQUEST_CODE_CATEGORY);
  }


  /**
   * Associates to or removes categories
   */
  private void categorizeNotes() {
    // Retrieves all available categories
    final ArrayList<Category> categories = DbHelper.getInstance().getCategories();

    final MaterialDialog dialog = new MaterialDialog.Builder(mainActivity)
        .title(R.string.categorize_as)
        .adapter(new CategoryRecyclerViewAdapter(mainActivity, categories), null)
        .backgroundColorAttr(R.attr.themeDialogBackgroundColor)
        .positiveText(R.string.add_category)
        .positiveColorAttr(R.attr.themeAccent)
        .negativeText(R.string.remove_category)
        .negativeColorAttr(R.attr.themeDialogNormalButtonColor)
        .onPositive((dialog1, which) -> {
          keepActionMode = true;
          Intent intent = new Intent(mainActivity, CategoryActivity.class);
          intent.putExtra("noHome", true);
          startActivityForResult(intent, REQUEST_CODE_CATEGORY_NOTES);
        }).onNegative((dialog12, which) -> categorizeNotesExecute(null)).build();

    RecyclerViewItemClickSupport.addTo(dialog.getRecyclerView()).setOnItemClickListener((recyclerView, position, v) -> {
      dialog.dismiss();
      categorizeNotesExecute(categories.get(position));
    });

    dialog.show();
  }


  private void categorizeNotesExecute (Category category) {
    if (category != null) {
      categorizeNote(getSelectedNotes(), category);
    } else {
      trackModifiedNotes(getSelectedNotes());
    }
    for (Note note : getSelectedNotes()) {
      // If is restore it will be done immediately, otherwise the undo bar
      // will be shown
      if (category == null) {
        // Saves categories associated to eventually undo
        undoCategoryMap.put(note, note.getCategory());
      }
      // Update adapter content if actual navigation is the category
      // associated with actually cycled note
      if ((Navigation.checkNavigation(CATEGORY) && !Navigation.checkNavigationCategory(category)) ||
          Navigation.checkNavigation(UNCATEGORIZED)) {
        listAdapter.remove(note);
      } else {
        note.setCategory(category);
        listAdapter.replace(note, listAdapter.getPosition(note));
      }
    }

    finishActionMode();

    // Advice to user
    String msg;
    if (category != null) {
      msg = getResources().getText(R.string.notes_categorized_as) + " '" + category.getName() + "'";
    } else {
      msg = getResources().getText(R.string.notes_category_removed).toString();
    }
    mainActivity.showMessage(msg, ONStyle.INFO);

    // Creation of undo bar
    if (category == null) {
      ubc.showUndoBar(false, getString(R.string.notes_category_removed), null);
      fab.hideFab();
      undoCategorize = true;
      undoCategorizeCategory = null;
    } else {
      getSelectedNotes().clear();
    }
  }


  private void categorizeNote (List<Note> notes, Category category) {
    new NoteProcessorCategorize(notes, category).process();
  }



  private void showAddNewTagsDialog(MaterialDialog dialogToDismiss)
  {
    MaterialDialog dialog = new MaterialDialog.Builder(mainActivity)
            .title(R.string.add_new_tag_caption)
            .input(getString(R.string.add_new_tag_hint), "",
                    new MaterialDialog.InputCallback() {
                      @Override
                      public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (dialogToDismiss != null)
                        {
                          dialogToDismiss.dismiss();
                        }
                        tagNotes(input.toString());
                      }
                    })
            .build();
    dialog.show();
  }

  /**
   * Bulk tag selected notes
   */
  private void tagNotes (String newTags) {

    // Retrieves all available tags
    final List<Tag> tags = DbHelper.getInstance().getTags();

    TagsHelper.AddAdditionalTags(tags, newTags);

    // If there is no tag a message will be shown
    if (tags.isEmpty()) {
      showAddNewTagsDialog(null);
      return;
    }

    final Integer[] preSelectedTags = TagsHelper.getPreselectedTagsArray(selectedNotes, tags);

    // Dialog and events creation
    MaterialDialog dialog = new MaterialDialog.Builder(mainActivity)
            .title(R.string.select_tags)
            .positiveText(R.string.ok)
            .negativeText(R.string.add_new_tag)
            .negativeColorRes(R.color.colorPrimary)
            .items(TagsHelper.getTagsArray(tags))

            .autoDismiss(false)
            .itemsCallbackMultiChoice(preSelectedTags, (dialog1, which, text) -> {
              dialog1.dismiss();
              tagNotesExecute(tags, which, preSelectedTags);
              return false;
            })
            .onNegative((dialog12, which) -> {
              showAddNewTagsDialog(dialog12);
            })
            .build();
    dialog.show();
  }


  private void tagNotesExecute (List<Tag> tags, Integer[] selectedTags, Integer[] preSelectedTags) {

    // Retrieves selected tags
    for (Note note : getSelectedNotes()) {
      tagNote(tags, selectedTags, note);
    }

    if (getActionMode() != null) {
      getActionMode().finish();
    }

    mainActivity.showMessage(R.string.tags_added, ONStyle.INFO);
  }


  private void tagNote (List<Tag> tags, Integer[] selectedTags, Note note) {

    Pair<String, List<Tag>> taggingResult = TagsHelper.addTagToNote(tags, selectedTags, note);
    /*
    if (note.isChecklist()) {
      note.setTitle(note.getTitle() + System.getProperty("line.separator") + taggingResult.first);
    } else {
      StringBuilder sb = new StringBuilder(note.getContent());
      if (sb.length() > 0) {
        sb.append(System.getProperty("line.separator"))
          .append(System.getProperty("line.separator"));
      }
      sb.append(taggingResult.first);
      note.setContent(sb.toString());
    }

    // Removes unchecked tags
    Pair<String, String> titleAndContent = TagsHelper.removeTag(note.getTitle(), note.getContent(),
        taggingResult.second);
    note.setTitle(titleAndContent.first);
    note.setContent(titleAndContent.second);
*/
    note.setTagList(taggingResult.first);

    DbHelper.getInstance().updateNote(note, false);
  }

//	private void synchronizeSelectedNotes() {
//		new DriveSyncTask(mainActivity).execute(new ArrayList<Note>(getSelectedNotes()));
//		// Clears data structures
//		listAdapter.clearSelectedItems();
//		list.clearChoices();
//		finishActionMode();
//	}


  @Override
  public void onUndo (Parcelable undoToken) {
    int navigation = Navigation.getNavigation();
    // Cycles removed items to re-insert into adapter
    for (Integer notePosition : undoNotesMap.keySet()) {
      Note currentNote = undoNotesMap.get(notePosition);
      //   Manages uncategorize or archive  undo
      if ((undoCategorize && !Navigation.checkNavigationCategory(undoCategoryMap.get(currentNote)))
          || undoArchive && !isAchievable(navigation)) {
        if (undoCategorize) {
          currentNote.setCategory(undoCategoryMap.get(currentNote));
        } else if (undoArchive) {
          currentNote.setArchived(undoArchivedMap.get(currentNote));
        }
        listAdapter.replace(currentNote, listAdapter.getPosition(currentNote));
        // Manages trash undo
      } else {
        listAdapter.add(notePosition, currentNote);
      }
    }

    listAdapter.notifyDataSetChanged();

    selectedNotes.clear();
    undoNotesMap.clear();

    undoTrash = false;
    undoArchive = false;
    undoCategorize = false;
    undoNotesMap.clear();
    undoCategoryMap.clear();
    undoArchivedMap.clear();
    undoCategorizeCategory = null;
    Crouton.cancelAllCroutons();

    if (getActionMode() != null) {
      getActionMode().finish();
    }
    ubc.hideUndoBar(false);
    fab.showFab();
  }


  void commitPending() {
    if (undoTrash || undoArchive || undoCategorize) {

      List<Note> notesList = new ArrayList<>(undoNotesMap.values());
      if (undoTrash) {
        trashNote(notesList, true);
      } else if (undoArchive) {
        archiveNote(notesList, sendToArchive);
      } else if (undoCategorize) {
        categorizeNote(notesList, undoCategorizeCategory);
      }

      undoTrash = false;
      undoArchive = false;
      undoCategorize = false;
      undoCategorizeCategory = null;

      // Clears data structures
      selectedNotes.clear();
      undoNotesMap.clear();
      undoCategoryMap.clear();
      undoArchivedMap.clear();
//      list.clearChoices();

      ubc.hideUndoBar(false);
      fab.showFab();

      LogDelegate.d("Changes committed");
    }
    mainActivity.updateWidgets();
  }


  /**
   * Shares the selected note from the list
   */
  private void share() {
    // Only one note should be selected to perform sharing but they'll be cycled anyhow
    for (final Note note : getSelectedNotes()) {
      mainActivity.shareNote(note);
    }

    getSelectedNotes().clear();
    if (getActionMode() != null) {
      getActionMode().finish();
    }
  }


  public void merge() {
    EventBus.getDefault().post(new NotesMergeEvent(false));
  }


  /**
   * Merges all the selected notes
   */
  public void onEventAsync(NotesMergeEvent notesMergeEvent) {

    final Note finalMergedNote = NotesHelper.mergeNotes(getSelectedNotes(), notesMergeEvent.keepMergedNotes);
    new Handler(Looper.getMainLooper()).post(() -> {

      if (!notesMergeEvent.keepMergedNotes) {
        ArrayList<String> notesIds = new ArrayList<>();
        for (Note selectedNote : getSelectedNotes()) {
          notesIds.add(String.valueOf(selectedNote.get_id()));
        }
        mainActivity.getIntent().putExtra("merged_notes", notesIds);
      }

      getSelectedNotes().clear();
      if (getActionMode() != null) {
        getActionMode().finish();
      }

      mainActivity.getIntent().setAction(ACTION_MERGE);
      mainActivity.switchToDetail(finalMergedNote);
    });
  }

  /**
   * Merges all the selected notes
   */
  public void onEventAsync(NotesDuplicatedEvent notesDuplicatedEvent) {

    // Advice to user
    int msg =  R.string.note_duplicated;
    Style style = ONStyle.INFO;
    mainActivity.showMessage(msg, style);

    initNotesList(mainActivity.getIntent());
    LogDelegate.d("notes duplicated");
  }

    /**
     * Excludes past reminders
     */
  private void filterReminders (boolean filter) {
    Prefs.edit().putBoolean(PREF_FILTER_PAST_REMINDERS, filter).apply();
    // Change list view
    initNotesList(mainActivity.getIntent());
    // Called to switch menu voices
    mainActivity.supportInvalidateOptionsMenu();
  }


  /**
   * Excludes archived notes in categories navigation
   */
  private void filterCategoryArchived (boolean filter) {
    if (filter) {
      Prefs.edit().putBoolean(PREF_FILTER_ARCHIVED_IN_CATEGORIES + Navigation.getCategory(), true).apply();
    } else {
      Prefs.edit().remove(PREF_FILTER_ARCHIVED_IN_CATEGORIES + Navigation.getCategory()).apply();
    }
    // Change list view
    initNotesList(mainActivity.getIntent());
    // Called to switch menu voices
    mainActivity.supportInvalidateOptionsMenu();
  }


  private void filterByUncompleteChecklists() {
    initNotesList(new Intent(ACTION_SEARCH_UNCOMPLETE_CHECKLISTS));
  }

  private void filterByTags() {

    final List<Tag> tags = TagsHelper.getAllTags();

    if (tags.isEmpty()) {
      mainActivity.showMessage(R.string.no_tags_created, ONStyle.WARN);
      return;
    }

    // Dialog and events creation
    new MaterialDialog.Builder(mainActivity)
        .title(R.string.select_tags)
        .items(TagsHelper.getTagsArray(tags))
        .positiveText(R.string.ok)
        .itemsCallbackMultiChoice(new Integer[]{}, (dialog, which, text) -> {
          // Retrieves selected tags
          List<String> selectedTags = new ArrayList<>();
          for (Integer aWhich : which) {
            selectedTags.add(tags.get(aWhich).getText());
          }

          // Saved here to allow persisting search
          searchTags = selectedTags.toString().substring(1, selectedTags.toString().length() - 1);
             // .replace(" ", "");
          Intent intent = mainActivity.getIntent();

          // Hides keyboard
          searchView.clearFocus();
          KeyboardUtils.hideKeyboard(searchView);

          intent.removeExtra(SearchManager.QUERY);
          initNotesList(intent);
          return false;
        }).build().show();
  }


  public MenuItem getSearchMenuItem() {
    return searchMenuItem;
  }


  private boolean isFabAllowed() {
    return isFabAllowed(false);
  }


  private boolean isFabAllowed(boolean actionModeFinishing) {

    boolean isAllowed = true;

    // Actionmode check
    isAllowed = isAllowed && (getActionMode() == null || actionModeFinishing);
    // Navigation check
    int navigation = Navigation.getNavigation();
    isAllowed = isAllowed && navigation != Navigation.ARCHIVE && navigation != REMINDERS && navigation
        != Navigation.TRASH;
    // Navigation drawer check
    isAllowed = isAllowed && mainActivity.getDrawerLayout() != null && !mainActivity.getDrawerLayout().isDrawerOpen
        (GravityCompat.START);

    return isAllowed;
  }

  @Override
  public void onLinkedNoteAdded()
  {
    getSelectedNotes().clear();
    finishActionMode();
    initNotesList(mainActivity.getIntent()); // refresh note list
    mainActivity.showMessage(R.string.note_added_to_set, ONStyle.INFO);
  }

}
