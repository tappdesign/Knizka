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

import static pk.tappdesign.knizka.NavigationDrawerFragment.isDoublePanelActive;
import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.view.ViewCompat.animate;
import static pk.tappdesign.knizka.BaseActivity.TRANSITION_HORIZONTAL;
import static pk.tappdesign.knizka.BaseActivity.TRANSITION_VERTICAL;
import static pk.tappdesign.knizka.MainActivity.FRAGMENT_DETAIL_TAG;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_DISMISS;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_FAB_TAKE_PHOTO;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_MERGE;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_NOTIFICATION_CLICK;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_PINNED;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_SHORTCUT;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_SHORTCUT_WIDGET;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_WIDGET;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_WIDGET_SHOW_LIST;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_WIDGET_TAKE_PHOTO;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTIVATE_TEXT_BROWSING_DISPLAY_RATIO;
import static pk.tappdesign.knizka.utils.ConstantsBase.GALLERY_CLICKED_IMAGE;
import static pk.tappdesign.knizka.utils.ConstantsBase.GALLERY_IMAGES;
import static pk.tappdesign.knizka.utils.ConstantsBase.GALLERY_TITLE;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_LIST_VIEW_POSITION_OFFSET_FOR_VIEWPAGER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_MAX_PAGES_IN_BROWSER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_GOOGLE_NOW;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_KEY;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_NOTE;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_WIDGET;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_AUDIO;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_AUDIO_EXT;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_FILES;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_IMAGE;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_IMAGE_EXT;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_SKETCH;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_SKETCH_EXT;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_VIDEO;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_VIDEO_EXT;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIN_X_MOVING_OFFSET_FOR_TEXT_BROWSING;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_ATTACHMENTS_ON_BOTTOM;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_AUTO_LOCATION;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_COLORS_APP_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_KEEP_CHECKED;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_KEEP_CHECKMARKS;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_PASSWORD;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_PRETTIFIED_DATES;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_SHOW_FULLSCREEN;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_SHOW_FULLSCREEN_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_WIDGET_PREFIX;
import static pk.tappdesign.knizka.utils.ConstantsBase.SWIPE_MARGIN;
import static pk.tappdesign.knizka.utils.ConstantsBase.SWIPE_OFFSET;
import static pk.tappdesign.knizka.utils.ConstantsBase.THUMBNAIL_SIZE;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PACKAGE_UNDEFINED;
import static pk.tappdesign.knizka.utils.ConstantsBase.PACKAGE_USER_ADDED;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_WEBVIEW_ZOOM;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_WEBVIEW_ZOOM_DEFAULT;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.neopixl.pixlui.components.edittext.EditText;
import com.pixplicity.easyprefs.library.Prefs;
import com.pushbullet.android.extension.MessagingExtension;
import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Style;
import it.feio.android.checklistview.exceptions.ViewNotSupportedException;
import it.feio.android.checklistview.interfaces.CheckListChangedListener;
import it.feio.android.checklistview.models.CheckListView;
import it.feio.android.checklistview.models.CheckListViewItem;
import it.feio.android.checklistview.models.ChecklistManager;
import pk.tappdesign.knizka.async.AttachmentTask;
import pk.tappdesign.knizka.async.bus.NotesUpdatedEvent;
import pk.tappdesign.knizka.async.bus.PushbulletReplyEvent;
import pk.tappdesign.knizka.async.bus.SwitchFragmentEvent;
import pk.tappdesign.knizka.async.notes.NoteProcessorDelete;
import pk.tappdesign.knizka.async.notes.SaveNoteTask;
import pk.tappdesign.knizka.databinding.FragmentDetailBinding;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.exceptions.checked.UnhandledIntentException;
import pk.tappdesign.knizka.helpers.AttachmentsHelper;
import pk.tappdesign.knizka.helpers.IntentHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.helpers.PermissionsHelper;
import pk.tappdesign.knizka.helpers.TagOpenerHelper;
import pk.tappdesign.knizka.helpers.date.DateHelper;
import pk.tappdesign.knizka.helpers.date.RecurrenceHelper;
import pk.tappdesign.knizka.helpers.notifications.NotificationChannels.NotificationChannelNames;
import pk.tappdesign.knizka.helpers.notifications.NotificationsHelper;
import pk.tappdesign.knizka.models.Attachment;
import pk.tappdesign.knizka.models.Category;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.ONStyle;
import pk.tappdesign.knizka.models.Tag;
import pk.tappdesign.knizka.models.WebViewTouchListener;
import pk.tappdesign.knizka.models.adapters.AttachmentAdapter;
import pk.tappdesign.knizka.models.adapters.CategoryRecyclerViewAdapter;
import pk.tappdesign.knizka.models.adapters.PlacesAutoCompleteAdapter;
import pk.tappdesign.knizka.models.listeners.OnAttachingFileListener;
import pk.tappdesign.knizka.models.listeners.OnGeoUtilResultListener;
import pk.tappdesign.knizka.models.listeners.OnNoteSaved;
import pk.tappdesign.knizka.models.listeners.OnReminderPickedListener;
import pk.tappdesign.knizka.models.listeners.RecyclerViewItemClickSupport;
import pk.tappdesign.knizka.models.views.ExpandableHeightGridView;
import pk.tappdesign.knizka.utils.AlphaManager;
import pk.tappdesign.knizka.utils.BitmapHelper;
import pk.tappdesign.knizka.utils.ConnectionManager;
import pk.tappdesign.knizka.utils.Display;
import pk.tappdesign.knizka.utils.FileHelper;
import pk.tappdesign.knizka.utils.FileProviderHelper;
import pk.tappdesign.knizka.utils.GeocodeHelper;
import pk.tappdesign.knizka.utils.IntentChecker;
import pk.tappdesign.knizka.utils.KeyboardUtils;
import pk.tappdesign.knizka.utils.PasswordHelper;
import pk.tappdesign.knizka.utils.ReminderHelper;
import pk.tappdesign.knizka.utils.ShortcutHelper;
import pk.tappdesign.knizka.utils.StorageHelper;
import pk.tappdesign.knizka.utils.TagsHelper;
import pk.tappdesign.knizka.utils.TextHelper;
import pk.tappdesign.knizka.utils.date.DateUtils;
import pk.tappdesign.knizka.utils.date.ReminderPickers;
import it.feio.android.pixlui.links.TextLinkClickListener;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import rx.Observable;
import pk.tappdesign.knizka.utils.Fonts;
import pk.tappdesign.knizka.utils.HTMLProducer;
import pk.tappdesign.knizka.utils.ConstantsBase;
import pk.tappdesign.knizka.utils.PKStringUtils;

import android.content.res.TypedArray;
import androidx.fragment.app.FragmentManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class DetailFragment extends BaseFragment implements OnReminderPickedListener, OnTouchListener,
        OnAttachingFileListener, TextWatcher, CheckListChangedListener, OnNoteSaved,
        OnGeoUtilResultListener, WebViewTouchListener {

   private static final int TAKE_PHOTO = 1;
   private static final int TAKE_VIDEO = 2;
   private static final int SET_PASSWORD = 3;
   private static final int SKETCH = 4;
   private static final int CATEGORY = 5;
   private static final int DETAIL = 6;
   private static final int FILES = 7;

   private FragmentDetailBinding binding;
   boolean goBack = false;

   private ExpandableHeightGridView mGridView;
   private View toggleChecklistView;
   private Uri attachmentUri;
   private AttachmentAdapter mAttachmentAdapter;
   private MaterialDialog attachmentDialog;
   private Note note;
   private Note noteTmp;
   private Note noteOriginal;
   // Audio recording
   private String recordName;
   private MediaRecorder mRecorder = null;
   private MediaPlayer mPlayer = null;
   private boolean isRecording = false;
   private View isPlayingView = null;
   private Bitmap recordingBitmap;
   private ChecklistManager mChecklistManager;
   // Values to print result
   private String exitMessage;
   private Style exitCroutonStyle = ONStyle.CONFIRM;
   // Flag to check if after editing it will return to ListActivity or not
   // and in the last case a Toast will be shown instead than Crouton
   private boolean afterSavedReturnsToList = true;
   private boolean showKeyboard = false;
   private boolean swiping;
   private int startSwipeX;
   private boolean orientationChanged;
   private long audioRecordingTimeStart;
   private long audioRecordingTime;
   private DetailFragment mFragment;
   private Attachment sketchEdited;
   private int contentLineCounter = 1;
   private int contentCursorPosition;
   private ArrayList<String> mergedNotesIds;
   private ArrayList<String> notesIdsInListView;
   private String titleForBrowsing = "";
   private MainActivity mainActivity;
   TextLinkClickListener textLinkClickListener = new TextLinkClickListener() {
      @Override
      public void onTextLinkClick(View view, final String clickedString, final String url) {
         new MaterialDialog.Builder(mainActivity)
                 .content(clickedString)
                 .negativeColorRes(R.color.colorPrimary)
                 .positiveText(R.string.open)
                 .negativeText(R.string.copy)
                 .onPositive((dialog, which) -> {
            try {
              Intent intent = TagOpenerHelper.openOrGetIntent(getContext(), url);
              if (intent != null) {
                mainActivity.initNotesList(intent);
              }
            } catch (UnhandledIntentException e) {
              mainActivity.showMessage(R.string.no_application_can_perform_this_action,
                  ONStyle.ALERT);
                    }
                 })
                 .onNegative((dialog, which) -> {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                            mainActivity
                                    .getSystemService(CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("text label",
                            clickedString);
                    clipboard.setPrimaryClip(clip);
                 }).build().show();
         View clickedView = noteTmp.isChecklist() ? toggleChecklistView : binding.contentWrapper;
         clickedView.clearFocus();
         KeyboardUtils.hideKeyboard(clickedView);
         new Handler().post(() -> {
            View clickedView1 = noteTmp.isChecklist() ? toggleChecklistView : binding.contentWrapper;
            KeyboardUtils.hideKeyboard(clickedView1);
         });
      }
   };
   private boolean activityPausing;
   private final ViewTreeObserver.OnWindowFocusChangeListener focusHandler = this::onWindowFocusChanged;
   Handler hideUITimeoutHandler = new Handler();

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mFragment = this;
   }

   @Override
   public void onAttach(Context context) {
      super.onAttach(context);
      EventBus.getDefault().post(new SwitchFragmentEvent(SwitchFragmentEvent.Direction.CHILDREN));
   }

   @Override
   public void onStart() {
      ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());
      super.onStart();

   }

   @Override
   public void onStop() {
      super.onStop();
      showSystemUI();
      GeocodeHelper.stop();
   }

   @Override
   public void onResume() {
      super.onResume();
      activityPausing = false;
      hideSystemUI();
      EventBus.getDefault().register(this);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      binding = FragmentDetailBinding.inflate(inflater, container, false);
      View view = binding.getRoot();
      view.getViewTreeObserver().addOnWindowFocusChangeListener(focusHandler);
      return view;
   }

   @Override
   public void onDestroyView() {
      if (binding.getRoot() != null)
      {
         binding.getRoot().getViewTreeObserver().removeOnWindowFocusChangeListener(focusHandler);
      }
      super.onDestroyView();
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      mainActivity = (MainActivity) getActivity();

      mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
      mainActivity.getToolbar().setNavigationOnClickListener(v -> navigateUp());

      // Force the navigation drawer to stay opened if tablet mode is on, otherwise has to stay closed
      if (isDoublePanelActive()) {
         mainActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
      } else {
         mainActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
      }

    restoreTempNoteAfterOrientationChange(savedInstanceState);

    addSketchedImageIfPresent();

    // Ensures that Detail Fragment always have the back Arrow when it's created
    EventBus.getDefault().post(new SwitchFragmentEvent(SwitchFragmentEvent.Direction.CHILDREN));
    init();

    Display.setKeepScreenOn(mainActivity);

    setHasOptionsMenu(true);
    setRetainInstance(false);

      final View decorView = getActivity().getWindow().getDecorView();
      decorView.setOnSystemUiVisibilityChangeListener(
              visibility -> {
                 if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                 {
                    // status bar is shown
                    showSystemUI();
                    hideUITimeoutHandler.postDelayed(hideUIMenuExecutor, 3000); // hide systemUI it after 3 seconds again
                 } else {
                    hideSystemUI();
                 }
              });
  }

   Runnable hideUIMenuExecutor = new Runnable() {
      public void run() {
         hideSystemUI();
      }
   };

   private void hideSystemUI() {
      if (getActivity() != null) {

         boolean isFullScreen = Prefs.getBoolean(PREF_SHOW_FULLSCREEN, PREF_SHOW_FULLSCREEN_DEFAULT);

         // do not set fullscreen if note is edited
         if( (isFullScreen) && (binding.fragmentDetailContent.myweb.getVisibility() != View.GONE))
         {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
            mainActivity.getSupportActionBar().hide();
         }
      }
   }

   private void showSystemUI() {
      if (getActivity() != null) {
         getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
         mainActivity.getSupportActionBar().show();
      }
   }

   public void onWindowFocusChanged(boolean hasFocus) {
      // if we lost focus from fragment, e.g. overflow menu was clicked, we should cancel timer for hiding action bar and system bars
       if (hasFocus) {
         // we have focus, try hide UI if it is configured
          hideSystemUI();
      } else {
         // focust lost, some other menu or dialog is on the top, cancel hide UI menu timer
          hideUITimeoutHandler.removeCallbacks(hideUIMenuExecutor);
      }
   }

  private void addSketchedImageIfPresent() {
    if (mainActivity.getSketchUri() != null) {
      Attachment mAttachment = new Attachment(mainActivity.getSketchUri(), MIME_TYPE_SKETCH);
      addAttachment(mAttachment);
      mainActivity.setSketchUri(null);
      // Removes previous version of edited image
      if (sketchEdited != null) {
        noteTmp.getAttachmentsList().remove(sketchEdited);
        sketchEdited = null;
      }
    }
  }

  private void restoreTempNoteAfterOrientationChange(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      noteTmp = savedInstanceState.getParcelable("noteTmp");
      note = savedInstanceState.getParcelable("note");
      noteOriginal = savedInstanceState.getParcelable("noteOriginal");
      attachmentUri = savedInstanceState.getParcelable("attachmentUri");
      orientationChanged = savedInstanceState.getBoolean("orientationChanged");
      notesIdsInListView = savedInstanceState.getStringArrayList(INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER);
      titleForBrowsing =  savedInstanceState.getString(INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER);
    }
  }

  private ArrayList<String> getNoteIdsFromListView()
  {
     ArrayList<String> result = new ArrayList<String>();
     if (mainActivity.getNotesList() != null)
     {
        for (Note note : mainActivity.getNotesList()) {
           result.add("" + note.getHandleID().longValue());
        }
     } else {
        result = notesIdsInListView;
     }
     return result;
  }

   private String getTitleForBrowsing()
   {
      String result = null;
      if (mainActivity.getNotesListCaption() != null) {
         result = mainActivity.getNotesListCaption();
      }
      if ((result == null) || (result.isEmpty()))
      {
         result = titleForBrowsing;
      }
      return result;
   }

   @Override
   public void onSaveInstanceState(Bundle outState) {
      if (noteTmp != null) {
         noteTmp.setTitle(getNoteTitle());
         noteTmp.setContent(getNoteContent());
         outState.putParcelable("noteTmp", noteTmp);
         outState.putParcelable("note", note);
         outState.putParcelable("noteOriginal", noteOriginal);
         outState.putParcelable("attachmentUri", attachmentUri);
         outState.putBoolean("orientationChanged", orientationChanged);
         outState.putStringArrayList(INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER, getNoteIdsFromListView());
         outState.putString(INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER, getTitleForBrowsing());
      }
      super.onSaveInstanceState(outState);
   }

   @Override
   public void onPause() {
      super.onPause();

      //to prevent memory leak fragment keep refernce of event but until deregister
      EventBus.getDefault().unregister(this);

      activityPausing = true;

      // Checks "goBack" value to avoid performing a double saving
      if (!goBack) {
         saveNote(this);
      }

      if (toggleChecklistView != null) {
         KeyboardUtils.hideKeyboard(toggleChecklistView);
         binding.contentWrapper.clearFocus();
      }
   }

   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      if (getResources().getConfiguration().orientation != newConfig.orientation) {
         orientationChanged = true;
      }
   }

   private void init() {

      // Handling of Intent actions
      handleIntents();

      if (noteOriginal == null) {
         noteOriginal = getArguments().getParcelable(INTENT_NOTE);
      }

      if (note == null) {
         note = new Note(noteOriginal);
         noteOriginal = new Note();
      }

      if (noteTmp == null) {
         noteTmp = new Note(note);
      }

      if (noteTmp.isLocked() && !noteTmp.isPasswordChecked()) {
         checkNoteLock(noteTmp);
         return;
      }

      if (noteTmp.getPackageID() == PACKAGE_UNDEFINED) {
         noteTmp.setPackageID(PACKAGE_USER_ADDED);
      }

      initViews();
   }

   /**
    * Checks note lock and password before showing note content
    */
   private void checkNoteLock(Note note) {
      // If note is locked security password will be requested
      if (note.isLocked()
              && Prefs.getString(PREF_PASSWORD, null) != null
              && !Prefs.getBoolean("settings_password_access", false)) {
         PasswordHelper.requestPassword(mainActivity, passwordConfirmed -> {
            switch (passwordConfirmed) {
               case SUCCEED:
                  noteTmp.setPasswordChecked(true);
                  init();
                  break;
               case FAIL:
                  goBack = true;
                  goHome();
                  break;
               case RESTORE:
                  goBack = true;
                  goHome();
                  PasswordHelper.resetPassword(mainActivity);
                  break;
            }
         });
      } else {
         noteTmp.setPasswordChecked(true);
         init();
      }
   }

   private void handleIntents() {
      Intent i = mainActivity.getIntent();

      if (IntentChecker.checkAction(i, ACTION_MERGE)) {
         noteOriginal = new Note();
         note = new Note(noteOriginal);
         noteTmp = getArguments().getParcelable(INTENT_NOTE);
         if (i.getStringArrayListExtra("merged_notes") != null) {
            mergedNotesIds = i.getStringArrayListExtra("merged_notes");
         }
      }

      // Action called from home shortcut
      if (IntentChecker.checkAction(i, ACTION_SHORTCUT, ACTION_NOTIFICATION_CLICK)) {
         afterSavedReturnsToList = false;

         noteOriginal = DbHelper.getInstance().getNote(i.getLongExtra(INTENT_KEY, 0));
         // Checks if the note pointed from the shortcut has been deleted
         try {
            note = new Note(noteOriginal);
            noteTmp = new Note(noteOriginal);
         } catch (NullPointerException e) {
            mainActivity.showToast(getText(R.string.shortcut_note_deleted), Toast.LENGTH_LONG);
            mainActivity.finish();
         }
      }

      // Check if is launched from a widget
      if (IntentChecker.checkAction(i, ACTION_WIDGET, ACTION_WIDGET_TAKE_PHOTO)) {

         afterSavedReturnsToList = false;
         showKeyboard = true;

         //  with tags to set tag
         if (i.hasExtra(INTENT_WIDGET)) {
            String widgetId = i.getExtras().get(INTENT_WIDGET).toString();
            String sqlCondition = Prefs.getString(PREF_WIDGET_PREFIX + widgetId, "");
            String categoryId = TextHelper.checkIntentCategory(sqlCondition);
            if (categoryId != null) {
               Category category;
               try {
                  category = DbHelper.getInstance().getCategory(parseLong(categoryId));
                  noteTmp = new Note();
                  noteTmp.setPackageID(PACKAGE_USER_ADDED);
                  noteTmp.setCategory(category);
               } catch (NumberFormatException e) {
                  LogDelegate.e("Category with not-numeric value!", e);
               }
            }
         }

         // Sub-action is to take a photo
         if (IntentChecker.checkAction(i, ACTION_WIDGET_TAKE_PHOTO)) {
            takePhoto();
         }
      }

      if (IntentChecker.checkAction(i, ACTION_FAB_TAKE_PHOTO)) {
         takePhoto();
      }

      // Handles third party apps requests of sharing
      if (IntentChecker.checkAction(i, Intent.ACTION_SEND, Intent.ACTION_SEND_MULTIPLE, INTENT_GOOGLE_NOW)
              && i.getType() != null) {

         afterSavedReturnsToList = false;

         if (noteTmp == null) {
            noteTmp = new Note();
         }

         // Text title
         String title = i.getStringExtra(Intent.EXTRA_SUBJECT);
         if (title != null) {
            noteTmp.setTitle(title);
         }

         // Text content
         String content = i.getStringExtra(Intent.EXTRA_TEXT);
         if (content != null) {
            noteTmp.setContent(content);
         }

         importAttachments(i);

      }

      if (IntentChecker.checkAction(i, Intent.ACTION_MAIN, ACTION_WIDGET_SHOW_LIST, ACTION_SHORTCUT_WIDGET, ACTION_WIDGET)) {
         showKeyboard = true;
      }

      i.setAction(null);
   }

   private void importAttachments(Intent i) {

      if (!i.hasExtra(Intent.EXTRA_STREAM)) {
         return;
      }

      if (i.getExtras().get(Intent.EXTRA_STREAM) instanceof Uri) {
         Uri uri = i.getParcelableExtra(Intent.EXTRA_STREAM);
         // Google Now passes Intent as text but with audio recording attached the case must be handled like this
         if (!INTENT_GOOGLE_NOW.equals(i.getAction())) {
            String name = FileHelper.getNameFromUri(mainActivity, uri);
            new AttachmentTask(this, uri, name, this).execute();
         }
      } else {
         ArrayList<Uri> uris = i.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
         for (Uri uriSingle : uris) {
            String name = FileHelper.getNameFromUri(mainActivity, uriSingle);
            new AttachmentTask(this, uriSingle, name, this).execute();
         }
      }
   }

   @SuppressLint("NewApi")
   private void initViews() {

      // Sets onTouchListener to the whole activity to swipe notes
      binding.detailRoot.setOnTouchListener(this);  //we dont't use this for now
      binding.contentWrapper.setOnTouchListener(touchListenerForContentWrapper);
      // another possibility is to set touch listener "OnSwipeWebviewTouchListener" to handle specific gestures
      // e.g. onFling. "OnSwipeWebviewTouchListener" was mainly intended for webView to catch fling events, but we don't need it know
     //  binding.contentWrapper.setOnTouchListener( new OnSwipeWebviewTouchListener( getActivity(), this));


      // Overrides font sizes with the one selected from user
      Fonts.overrideTextSize(mainActivity, binding.detailRoot);

      // Color of tag marker if note is tagged a function is active in preferences
      setTagMarkerColor(noteTmp.getCategory());

      initViewTitle();

      initViewContent();

      initViewWebView();

      showProperItem();

      initViewLocation();

      initViewAttachments();

      initViewReminder();

      initViewFooter();
   }

   private void initViewFooter() {
      String creation = DateHelper.getFormattedDate(noteTmp.getCreation(), Prefs.getBoolean(PREF_PRETTIFIED_DATES, true));
      binding.creation.append(creation.length() > 0 ? getString(R.string.creation) + " " + creation : "");
      if (binding.creation.getText().length() == 0) {
         binding.creation.setVisibility(View.GONE);
      }

      String lastModification = DateHelper.getFormattedDate(noteTmp.getLastModification(), Prefs.getBoolean(
              PREF_PRETTIFIED_DATES, true));
      binding.lastModification.append(lastModification.length() > 0 ? getString(R.string.last_update) + " " +
              lastModification : "");
      if (binding.lastModification.getText().length() == 0) {
         binding.lastModification.setVisibility(View.GONE);
      }
   }


   private Drawable getAlarmIconForTheme(int resourceID) {
      Drawable retVal;
      TypedArray a = mainActivity.getTheme().obtainStyledAttributes(R.style.AppTheme, new int[]{resourceID});
      int attributeResourceId = a.getResourceId(0, 0);
      a.recycle();
      retVal = getResources().getDrawable(attributeResourceId);
      return retVal;
   }


   private void initViewReminder() {
      binding.fragmentDetailContent.reminderLayout.setOnClickListener(v -> {
         ReminderPickers reminderPicker = new ReminderPickers(mainActivity, mFragment);
         reminderPicker.pick(DateUtils.getPresetReminder(noteTmp.getAlarm()), noteTmp
                 .getRecurrenceRule());
      });

      binding.fragmentDetailContent.reminderLayout.setOnLongClickListener(v -> {
         MaterialDialog dialog = new MaterialDialog.Builder(mainActivity)
                 .content(R.string.remove_reminder)
                 .positiveText(R.string.ok)
                 .backgroundColorAttr(R.attr.themeDialogBackgroundColor)
                 .positiveColorAttr(R.attr.themeAccent)
                 .onPositive((dialog1, which) -> {
                    ReminderHelper.removeReminder(Knizka.getAppContext(), noteTmp);
                    noteTmp.setAlarm(null);
                    Drawable d = getAlarmIconForTheme(R.attr.ic_alarm_add_themed);
                    binding.fragmentDetailContent.reminderIcon.setImageDrawable(d);
                    binding.fragmentDetailContent.datetime.setText("");
                 }).build();
         dialog.show();
         return true;
      });

      // Reminder
      String reminderString = initReminder(noteTmp);
      if (!TextUtils.isEmpty(reminderString)) {
         Drawable d = getAlarmIconForTheme(R.attr.ic_alarm_note_layout);
         binding.fragmentDetailContent.reminderIcon.setImageDrawable(d);
         binding.fragmentDetailContent.datetime.setText(reminderString);
      }
   }

   private void initViewLocation() {

      DetailFragment detailFragment = this;

      if (isNoteLocationValid()) {
         if (TextUtils.isEmpty(noteTmp.getAddress())) {
            //FIXME: What's this "sasd"?
            GeocodeHelper.getAddressFromCoordinates(new Location("sasd"), detailFragment);
         } else {
            binding.fragmentDetailContent.location.setText(noteTmp.getAddress());
            binding.fragmentDetailContent.location.setVisibility(View.VISIBLE);
         }
      }

      // Automatic location insertion
      if (Prefs.getBoolean(PREF_AUTO_LOCATION, false) && noteTmp.get_id() == null) {
         getLocation(detailFragment);
      }

      binding.fragmentDetailContent.location.setOnClickListener(v -> {
         String uriString = "geo:" + noteTmp.getLatitude() + ',' + noteTmp.getLongitude()
                 + "?q=" + noteTmp.getLatitude() + ',' + noteTmp.getLongitude();
         Intent locationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
         if (!IntentChecker.isAvailable(mainActivity, locationIntent, null)) {
            uriString = "http://maps.google.com/maps?q=" + noteTmp.getLatitude() + ',' + noteTmp
                    .getLongitude();
            locationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
         }
         startActivity(locationIntent);
      });
      binding.fragmentDetailContent.location.setOnLongClickListener(v -> {
         MaterialDialog.Builder builder = new MaterialDialog.Builder(mainActivity);
         builder.content(R.string.remove_location);
         builder.positiveText(R.string.ok);
         builder.onPositive((dialog, which) -> {
            noteTmp.setLatitude("");
            noteTmp.setLongitude("");
            fade(binding.fragmentDetailContent.location, false);
         });
         MaterialDialog dialog = builder.build();
         dialog.show();
         return true;
      });
   }

   private void getLocation(OnGeoUtilResultListener onGeoUtilResultListener) {
      PermissionsHelper.requestPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, R.string
                      .permission_coarse_location, binding.snackbarPlaceholder,
              () -> GeocodeHelper.getLocation(onGeoUtilResultListener));
   }

   private void initViewAttachments() {
      // Attachments position based on preferences
      if (Prefs.getBoolean(PREF_ATTACHMENTS_ON_BOTTOM, false)) {
         binding.detailAttachmentsBelow.inflate();
      } else {
         binding.detailAttachmentsAbove.inflate();
      }
      mGridView = binding.detailRoot.findViewById(R.id.gridview);

      // Some fields can be filled by third party application and are always shown
      mAttachmentAdapter = new AttachmentAdapter(mainActivity, noteTmp.getAttachmentsList(), mGridView);

      // Initialzation of gridview for images
      mGridView.setAdapter(mAttachmentAdapter);
      mGridView.autoresize();

      // Click events for images in gridview (zooms image)
      mGridView.setOnItemClickListener((parent, v, position, id) -> {
         Attachment attachment = (Attachment) parent.getAdapter().getItem(position);
         Uri sharableUri = FileProviderHelper.getShareableUri(attachment);
         Intent attachmentIntent;
         if (MIME_TYPE_FILES.equals(attachment.getMime_type())) {

            attachmentIntent = new Intent(Intent.ACTION_VIEW);
            attachmentIntent.setDataAndType(sharableUri, StorageHelper.getMimeType(mainActivity,
                    sharableUri));
            attachmentIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent
                    .FLAG_GRANT_WRITE_URI_PERMISSION);
            if (IntentChecker.isAvailable(mainActivity.getApplicationContext(), attachmentIntent, null)) {
               startActivity(attachmentIntent);
            } else {
               mainActivity.showMessage(R.string.feature_not_available_on_this_device, ONStyle.WARN);
            }

            // Media files will be opened in internal gallery
         } else if (MIME_TYPE_IMAGE.equals(attachment.getMime_type())
                 || MIME_TYPE_SKETCH.equals(attachment.getMime_type())
                 || MIME_TYPE_VIDEO.equals(attachment.getMime_type())) {
            // Title
            noteTmp.setTitle(getNoteTitle());
            noteTmp.setContent(getNoteContent());
            String title1 = TextHelper.parseTitleAndContent(mainActivity,
                    noteTmp)[0].toString();
            // Images
            int clickedImage = 0;
            ArrayList<Attachment> images = new ArrayList<>();
            for (Attachment mAttachment : noteTmp.getAttachmentsList()) {
               if (MIME_TYPE_IMAGE.equals(mAttachment.getMime_type())
                       || MIME_TYPE_SKETCH.equals(mAttachment.getMime_type())
                       || MIME_TYPE_VIDEO.equals(mAttachment.getMime_type())) {
                  images.add(mAttachment);
                  if (mAttachment.equals(attachment)) {
                     clickedImage = images.size() - 1;
                  }
               }
            }
            // Intent
            attachmentIntent = new Intent(mainActivity, GalleryActivity.class);
            attachmentIntent.putExtra(GALLERY_TITLE, title1);
            attachmentIntent.putParcelableArrayListExtra(GALLERY_IMAGES, images);
            attachmentIntent.putExtra(GALLERY_CLICKED_IMAGE, clickedImage);
            startActivity(attachmentIntent);

         } else if (MIME_TYPE_AUDIO.equals(attachment.getMime_type())) {
            playback(v, attachment.getUri());
         }

      });

      mGridView.setOnItemLongClickListener((parent, v, position, id) -> {
         // To avoid deleting audio attachment during playback
         if (mPlayer != null) {
            return false;
         }
         List<String> items = Arrays.asList(getResources().getStringArray(R.array.attachments_actions));
         if (!MIME_TYPE_SKETCH.equals(mAttachmentAdapter.getItem(position).getMime_type())) {
            items = items.subList(0, items.size() - 1);
         }
         Attachment attachment = mAttachmentAdapter.getItem(position);
         new MaterialDialog.Builder(mainActivity)
                 .title(attachment.getName() + " (" + AttachmentsHelper.getSize(attachment) + ")")
                 .items(items.toArray(new String[items.size()]))
                 .itemsCallback((materialDialog, view, i, charSequence) ->
                         performAttachmentAction(position, i))
                 .build()
                 .show();
         return true;
      });
   }

   /**
    * Performs an action when long-click option is selected
    *
    * @param i item index
    */
   private void performAttachmentAction(int attachmentPosition, int i) {
      switch (getResources().getStringArray(R.array.attachments_actions_values)[i]) {
         case "share":
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            Attachment attachment = mAttachmentAdapter.getItem(attachmentPosition);
            shareIntent.setType(StorageHelper.getMimeType(Knizka.getAppContext(), attachment.getUri()));
            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProviderHelper.getShareableUri(attachment));
            if (IntentChecker.isAvailable(Knizka.getAppContext(), shareIntent, null)) {
               startActivity(shareIntent);
            } else {
               mainActivity.showMessage(R.string.feature_not_available_on_this_device, ONStyle.WARN);
            }
            break;
         case "delete":
            removeAttachment(attachmentPosition);
            mAttachmentAdapter.notifyDataSetChanged();
            mGridView.autoresize();
            break;
         case "delete all":
            new MaterialDialog.Builder(mainActivity)
                    .title(R.string.delete_all_attachments)
                    .positiveText(R.string.confirm)
                    .onPositive((materialDialog, dialogAction) -> removeAllAttachments())
                    .build()
                    .show();
            break;
         case "edit":
            takeSketch(mAttachmentAdapter.getItem(attachmentPosition));
            break;
         default:
            LogDelegate.w("No action available");
      }
   }

   private void initViewTitle() {
      binding.detailTitle.setText(noteTmp.getTitle());
      binding.detailTitle.gatherLinksForText();
      binding.detailTitle.setOnTextLinkClickListener(textLinkClickListener);
      // To avoid dropping here the  dragged checklist items
      binding.detailTitle.setOnDragListener((v, event) -> {
//					((View)event.getLocalState()).setVisibility(View.VISIBLE);
         return true;
      });
      //When editor action is pressed focus is moved to last character in content field
      binding.detailTitle.setOnEditorActionListener((v, actionId, event) -> {
         binding.fragmentDetailContent.detailContent.requestFocus();
         binding.fragmentDetailContent.detailContent.setSelection(binding.fragmentDetailContent.detailContent.getText().length());
         return false;
      });
      requestFocus(binding.detailTitle);
   }

   private void initViewContent() {

      binding.fragmentDetailContent.detailContent.setText(noteTmp.getHTMLContent());

      binding.fragmentDetailContent.detailContent.gatherLinksForText();
      binding.fragmentDetailContent.detailContent.setOnTextLinkClickListener(textLinkClickListener);
      // Avoids focused line goes under the keyboard
      binding.fragmentDetailContent.detailContent.addTextChangedListener(this);

      // Restore checklist
      toggleChecklistView = binding.fragmentDetailContent.detailContent;
      if (noteTmp.isChecklist()) {
         noteTmp.setChecklist(false);
         AlphaManager.setAlpha(toggleChecklistView, 0);
         toggleChecklist2();
      }
   }

   private void initViewWebView() {
      binding.fragmentDetailContent.myweb.setWebChromeClient(new WebChromeClient());
      binding.fragmentDetailContent.myweb.getSettings().setLoadsImagesAutomatically(true);
      binding.fragmentDetailContent.myweb.getSettings().setJavaScriptEnabled(true);
      binding.fragmentDetailContent.myweb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

      binding.fragmentDetailContent.myweb.setWebViewClient(new ourBrowser());
      binding.fragmentDetailContent.myweb.getSettings().setTextZoom(Prefs.getInt(PREF_WEBVIEW_ZOOM, PREF_WEBVIEW_ZOOM_DEFAULT));
      binding.fragmentDetailContent.myweb.addJavascriptInterface(new JSInterface(), "AndroidHook");
      // do not use this touch listener, is not need for now, we don't react onFling in WebView
      //binding.fragmentDetailContent.myweb.setOnTouchListener( new OnSwipeWebviewTouchListener( getActivity(), this));
      loadNoteToWebView();
   }

   private void showProperItem() {
      boolean newNote = noteTmp.get_id() == null;
      View contentView = binding.detailRoot.findViewById(R.id.detail_content);
      View tileCard = binding.detailRoot.findViewById(R.id.detail_tile_card);

      if (newNote) {
         binding.fragmentDetailContent.myweb.setVisibility(View.GONE);
         contentView.setVisibility(View.VISIBLE);
         tileCard.setVisibility(View.VISIBLE);

      } else {
         binding.fragmentDetailContent.myweb.setVisibility(View.VISIBLE);
         contentView.setVisibility(View.GONE);
         tileCard.setVisibility(View.GONE);

      }
   }

   @Override
   public void onSwipeRight() {
      LogDelegate.i("Webview swipped right");
      activateBrowsingTexts(-1);
   }

   @Override
   public void onSwipeLeft() {
      LogDelegate.i("Webview swipped left");
      activateBrowsingTexts(1);
   }


   /**
    * Force focus and shows soft keyboard. Only happens if it's a new note, without shared content. {@link showKeyboard}
    * is used to check if the note is created from shared content.
    */
   @SuppressWarnings("JavadocReference")
   private void requestFocus(final EditText view) {
      if (note.get_id() == null && !noteTmp.isChanged(note) && showKeyboard) {
         KeyboardUtils.showKeyboard(view);
      }
   }

   /**
    * Colors tag marker in note's title and content elements
    */
   private void setTagMarkerColor(Category tag) {

      String colorsPref = Prefs.getString("settings_colors_app", PREF_COLORS_APP_DEFAULT);

      // Checking preference
      if (!"disabled".equals(colorsPref)) {

         // Choosing target view depending on another preference
         ArrayList<View> target = new ArrayList<>();
         if ("complete".equals(colorsPref)) {
            target.add(binding.titleWrapper);
            target.add(binding.contentWrapper);
         } else {
            target.add(binding.tagMarker);
         }

         // Coloring the target
         if (tag != null && tag.getColor() != null) {
            for (View view : target) {
               view.setBackgroundColor(parseInt(tag.getColor()));
            }
         } else {
            for (View view : target) {
               view.setBackgroundColor(Color.parseColor("#00000000"));
            }
         }
      }
   }

   private void displayLocationDialog() {
      getLocation(new OnGeoUtilResultListenerImpl(mainActivity, mFragment, noteTmp));
   }

   @Override
   public void onLocationRetrieved(Location location) {
      if (location == null) {
         mainActivity.showMessage(R.string.location_not_found, ONStyle.ALERT);
      }
      if (location != null) {
         noteTmp.setLatitude(location.getLatitude());
         noteTmp.setLongitude(location.getLongitude());
         if (!TextUtils.isEmpty(noteTmp.getAddress())) {
            binding.fragmentDetailContent.location.setVisibility(View.VISIBLE);
            binding.fragmentDetailContent.location.setText(noteTmp.getAddress());
         } else {
            GeocodeHelper.getAddressFromCoordinates(location, mFragment);
         }
      }
   }

   @Override
   public void onLocationUnavailable() {
      mainActivity.showMessage(R.string.location_not_found, ONStyle.ALERT);
   }

   @Override
   public void onAddressResolved(String address) {
      if (TextUtils.isEmpty(address)) {
         if (!isNoteLocationValid()) {
            mainActivity.showMessage(R.string.location_not_found, ONStyle.ALERT);
            return;
         }
         address = noteTmp.getLatitude() + ", " + noteTmp.getLongitude();
      }
      if (!GeocodeHelper.areCoordinates(address)) {
         noteTmp.setAddress(address);
      }
      binding.fragmentDetailContent.location.setVisibility(View.VISIBLE);
      binding.fragmentDetailContent.location.setText(address);
      fade(binding.fragmentDetailContent.location, true);
   }

   @Override
   public void onCoordinatesResolved(Location location, String address) {
      if (location != null) {
         noteTmp.setLatitude(location.getLatitude());
         noteTmp.setLongitude(location.getLongitude());
         noteTmp.setAddress(address);
         binding.fragmentDetailContent.location.setVisibility(View.VISIBLE);
         binding.fragmentDetailContent.location.setText(address);
         fade(binding.fragmentDetailContent.location, true);
      } else {
         mainActivity.showMessage(R.string.location_not_found, ONStyle.ALERT);
      }
   }

   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      inflater.inflate(R.menu.menu_detail, menu);
      super.onCreateOptionsMenu(menu, inflater);
   }

   @Override
   public void onPrepareOptionsMenu(Menu menu) {

      // Closes search view if left open in List fragment
      MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
      if (searchMenuItem != null) {
         searchMenuItem.collapseActionView();
      }

      boolean isFullScreen = Prefs.getBoolean(PREF_SHOW_FULLSCREEN, PREF_SHOW_FULLSCREEN_DEFAULT);
      boolean isEditMode = binding.fragmentDetailContent.myweb.getVisibility() == View.GONE;
      boolean newNote = noteTmp.get_id() == null;

      menu.findItem(R.id.menu_fullscreen_on).setVisible((!isFullScreen) && (!(isEditMode)));
      menu.findItem(R.id.menu_fullscreen_off).setVisible((isFullScreen) && (!(isEditMode)));
      menu.findItem(R.id.menu_favorite).setVisible(noteTmp.isFavorite());
      menu.findItem(R.id.menu_favorite_off).setVisible(!noteTmp.isFavorite());
      menu.findItem(R.id.menu_ZoomIn).setVisible(!(isEditMode));
      menu.findItem(R.id.menu_ZoomOut).setVisible(!(isEditMode));
      menu.findItem(R.id.menu_checklist_on).setVisible(!noteTmp.isChecklist());
      menu.findItem(R.id.menu_checklist_off).setVisible(noteTmp.isChecklist());
      menu.findItem(R.id.menu_checklist_moveToBottom).setVisible(noteTmp.isChecklist() && mChecklistManager.getCheckedCount() > 0);
      menu.findItem(R.id.menu_lock).setVisible(!noteTmp.isLocked());
      menu.findItem(R.id.menu_unlock).setVisible(noteTmp.isLocked());

      menu.findItem(R.id.menu_tag).setVisible(false);
      menu.findItem(R.id.menu_checklist_on).setVisible(false);
      menu.findItem(R.id.menu_checklist_off).setVisible(false);
      menu.findItem(R.id.menu_lock).setVisible(false);
      menu.findItem(R.id.menu_unlock).setVisible(false);
      menu.findItem(R.id.menu_edit_note).setVisible(false);


      menu.findItem(R.id.menu_discard_changes).setVisible(false);

      if (noteTmp.isTrashed()) {
         menu.findItem(R.id.menu_untrash).setVisible(true);
         menu.findItem(R.id.menu_delete).setVisible(true);
         // Otherwise all other actions will be available
      } else {
         // Temporary removed until fixed on Oreo and following
         menu.findItem(R.id.menu_add_shortcut).setVisible(!newNote);
         menu.findItem(R.id.menu_pin_note).setVisible(!newNote);
         menu.findItem(R.id.menu_archive).setVisible(!newNote && !noteTmp.isArchived());
         menu.findItem(R.id.menu_unarchive).setVisible(!newNote && noteTmp.isArchived());
         menu.findItem(R.id.menu_trash).setVisible(!newNote);
         menu.findItem(R.id.menu_edit_note).setVisible(!newNote && ((noteTmp.getPackageID() == ConstantsBase.PACKAGE_USER_ADDED) || (noteTmp.getPackageID() == ConstantsBase.PACKAGE_USER_INTENT)));
         if (menu.findItem(R.id.menu_edit_note).isVisible() && (isEditMode)) {
            menu.findItem(R.id.menu_edit_note).setVisible(false);  // note is already editing, we cannot display this menu
            menu.findItem(R.id.menu_discard_changes).setVisible(true);
         }
         menu.findItem(R.id.menu_tag).setVisible(true);
         menu.findItem(R.id.menu_category).setVisible(true);
      }
   }

   @SuppressLint("NewApi")
   private boolean goHome() {
      stopPlaying();

      // The activity has managed a shared intent from third party app and
      // performs a normal onBackPressed instead of returning back to ListActivity
      if (!afterSavedReturnsToList) {
         if (!TextUtils.isEmpty(exitMessage)) {
            mainActivity.showToast(exitMessage, Toast.LENGTH_SHORT);
         }
         mainActivity.finish();

      } else {

         if (!TextUtils.isEmpty(exitMessage) && exitCroutonStyle != null) {
            mainActivity.showMessage(exitMessage, exitCroutonStyle);
         }

         // Otherwise the result is passed to ListActivity
         if (mainActivity != null) {
            mainActivity.getSupportFragmentManager();
            mainActivity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // @pk: required clean all frames on backstack
            if (mainActivity.getSupportFragmentManager().getBackStackEntryCount() == 1) {                                           // @pk: this condition is no more necessary
               mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);
               if (mainActivity.getDrawerToggle() != null) {
                  mainActivity.getDrawerToggle().setDrawerIndicatorEnabled(true);
               }
               EventBus.getDefault().post(new SwitchFragmentEvent(SwitchFragmentEvent.Direction.PARENT));
            }
         }
      }

      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {

      if (isOptionsItemFastClick()) {
         return true;
      }

      switch (item.getItemId()) {

         case R.id.menu_fullscreen_on:
         case R.id.menu_fullscreen_off:
            toggleFullscreen();
            break;
         case R.id.menu_ZoomIn:
            zoomTexIn();
            break;
         case R.id.menu_ZoomOut:
            zoomTexOut();
            break;
         case R.id.menu_attachment:
            showAttachmentsPopup();
            break;
         case R.id.menu_tag:
            addTags("");
            break;
         case R.id.menu_category:
            categorizeNote();
            break;
         case R.id.menu_share:
            shareNote();
            break;
         case R.id.menu_favorite:
         case R.id.menu_favorite_off:
            toggleFavorite();
            break;
         case R.id.menu_checklist_on:
         case R.id.menu_checklist_off:
            toggleChecklist();
            break;
         case R.id.menu_checklist_moveToBottom:
            moveCheckedItemsToBottom();
            break;
         case R.id.menu_lock:
         case R.id.menu_unlock:
            lockNote();
            break;
         case R.id.menu_pin_note:
            pinNote();
            break;
         case R.id.menu_add_shortcut:
            addShortcut();
            break;
         case R.id.menu_archive:
            archiveNote(true);
            break;
         case R.id.menu_unarchive:
            archiveNote(false);
            break;
         case R.id.menu_trash:
            trashNote(true);
            break;
         case R.id.menu_untrash:
            trashNote(false);
            break;
         case R.id.menu_edit_note:
            editNote();
            break;
         case R.id.menu_discard_changes:
            discard();
            break;
         case R.id.menu_delete:
            deleteNote();
            break;
         case R.id.menu_color_theme:
            showDialogColorTheme();
            break;
         case R.id.menu_note_info:
            showNoteInfo();
            break;
         default:
            LogDelegate.w("Invalid menu option selected");
      }

      ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackActionFromResourceId(getActivity(),
              item.getItemId());

      return super.onOptionsItemSelected(item);
   }


   private void loadNoteToWebView() {
       binding.fragmentDetailContent.myweb.loadDataWithBaseURL("file:///android_asset/", HTMLProducer.getHTML(noteTmp.getHandleID(), noteTmp.getTitle(), noteTmp.getHTMLContent()), null, null, null);
   }

   private void changeHtmlColorScheme(int i) {

      Prefs.edit().putString(PREF_HTML_COLOR_SCHEME, getResources().getStringArray(R.array.html_color_schemes_values)[i]).commit();
      loadNoteToWebView();

      mainActivity.recreate();
   }

   private void showDialogColorTheme() {

      int preselectedIndex = Arrays
              .asList(getResources().getStringArray(R.array.html_color_schemes_values))
              .indexOf(Prefs.getString(PREF_HTML_COLOR_SCHEME, PREF_HTML_COLOR_SCHEME_DEFAULT));

      MaterialAlertDialogBuilder importDialog = new MaterialAlertDialogBuilder(getActivity())
              .setTitle(R.string.dialog_html_color_scheme_caption)
              .setSingleChoiceItems(getResources().getStringArray(R.array.html_color_schemes), preselectedIndex, (dialog, position) -> {
              })
              .setPositiveButton(R.string.choose_html_color_scheme, (dialog, which) -> {
                 int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                 changeHtmlColorScheme(position);
              })
              .setNegativeButton(R.string.dialog_button_cancel, (dialog, which) -> {
              });
      importDialog.show();
   }

   private void showNoteInfo() {
      noteTmp.setTitle(getNoteTitle());
      noteTmp.setContent(getNoteContent());
      Intent intent = new Intent(getContext(), NoteInfosActivity.class);
      intent.putExtra(INTENT_NOTE, (android.os.Parcelable) noteTmp);
      startActivity(intent);
   }

   private void navigateUp() {
      afterSavedReturnsToList = true;
      saveAndExit(this);
   }

   private void toggleFavorite() {

      noteTmp.setFavorite(!noteTmp.isFavorite());

      mainActivity.showMessage((noteTmp.isFavorite() == true ? mainActivity.getResources().getString(R.string.added_to_favorites) : mainActivity.getResources().getString(R.string.removed_from_favorites)), ONStyle.INFO);
      mainActivity.supportInvalidateOptionsMenu();
      DbHelper.getInstance().insertNoteToFavorites(noteTmp, (noteTmp.isFavorite()));
   }

   private void toggleFullscreen() {
      boolean isFullScreen = !Prefs.getBoolean(PREF_SHOW_FULLSCREEN, PREF_SHOW_FULLSCREEN_DEFAULT);
      Prefs.edit().putBoolean(PREF_SHOW_FULLSCREEN, isFullScreen).commit();

      if (isFullScreen)
      {
         hideSystemUI();
      } else {
         showSystemUI();
      }
      mainActivity.supportInvalidateOptionsMenu();
   }

   private void zoomTexIn() {
      int textZoom = (int) (Prefs.getInt(PREF_WEBVIEW_ZOOM, PREF_WEBVIEW_ZOOM_DEFAULT) * 1.1);
      Prefs.edit().putInt(PREF_WEBVIEW_ZOOM, textZoom).commit();
      binding.fragmentDetailContent.myweb.getSettings().setTextZoom(textZoom);
   }

   private void zoomTexOut() {
      int textZoom = (int) (Prefs.getInt(PREF_WEBVIEW_ZOOM, PREF_WEBVIEW_ZOOM_DEFAULT) * 1 / 1.1);
      Prefs.edit().putInt(PREF_WEBVIEW_ZOOM, textZoom).commit();
      binding.fragmentDetailContent.myweb.getSettings().setTextZoom(textZoom);
   }

   private void toggleChecklist() {

      // In case checklist is active a prompt will ask about many options
      // to decide hot to convert back to simple text
      if (!noteTmp.isChecklist()) {
         toggleChecklist2();
         return;
      }

      // If checklist is active but no items are checked the conversion in done automatically
      // without prompting user
      if (mChecklistManager.getCheckedCount() == 0) {
         toggleChecklist2(true, false);
         return;
      }

      // Inflate the popup_layout.XML
      LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
      final View layout = inflater.inflate(R.layout.dialog_remove_checklist_layout,
              getView().findViewById(R.id.layout_root));

      // Retrieves options checkboxes and initialize their values
      final CheckBox keepChecked = layout.findViewById(R.id.checklist_keep_checked);
      final CheckBox keepCheckmarks = layout.findViewById(R.id.checklist_keep_checkmarks);
      keepChecked.setChecked(Prefs.getBoolean(PREF_KEEP_CHECKED, true));
      keepCheckmarks.setChecked(Prefs.getBoolean(PREF_KEEP_CHECKMARKS, true));

      new MaterialDialog.Builder(mainActivity)
              .customView(layout, false)
              .positiveText(R.string.ok)
              .onPositive((dialog, which) -> {
                 Prefs.edit()
                         .putBoolean(PREF_KEEP_CHECKED, keepChecked.isChecked())
                         .putBoolean(PREF_KEEP_CHECKMARKS, keepCheckmarks.isChecked())
                         .apply();
                 toggleChecklist2();
              }).build().show();
   }

   /**
    * Toggles checklist view
    */
   private void toggleChecklist2() {
      boolean keepChecked = Prefs.getBoolean(PREF_KEEP_CHECKED, true);
      boolean showChecks = Prefs.getBoolean(PREF_KEEP_CHECKMARKS, true);
      toggleChecklist2(keepChecked, showChecks);
   }

   @SuppressLint("NewApi")
   private void toggleChecklist2(final boolean keepChecked, final boolean showChecks) {
      // Get instance and set options to convert EditText to CheckListView

      mChecklistManager = mChecklistManager == null ? new ChecklistManager(mainActivity) : mChecklistManager;
      int checkedItemsBehavior = Integer.parseInt(Prefs.getString("settings_checked_items_behavior", String.valueOf
              (it.feio.android.checklistview.Settings.CHECKED_HOLD)));
      mChecklistManager
              .showCheckMarks(showChecks)
              .newEntryHint(getString(R.string.checklist_item_hint))
              .keepChecked(keepChecked)
              .undoBarContainerView(binding.contentWrapper)
              .moveCheckedOnBottom(checkedItemsBehavior);

      // Links parsing options
      mChecklistManager.setOnTextLinkClickListener(textLinkClickListener);
      mChecklistManager.addTextChangedListener(mFragment);
      mChecklistManager.setCheckListChangedListener(mFragment);

      // Switches the views
      View newView = null;
      try {
         newView = mChecklistManager.convert(toggleChecklistView);
      } catch (ViewNotSupportedException e) {
         LogDelegate.e("Error switching checklist view", e);
      }

      // Switches the views
      if (newView != null) {
         mChecklistManager.replaceViews(toggleChecklistView, newView);
         toggleChecklistView = newView;
         animate(toggleChecklistView).alpha(1).scaleXBy(0).scaleX(1).scaleYBy(0).scaleY(1);
         noteTmp.setChecklist(!noteTmp.isChecklist());
      }
   }

   private void moveCheckedItemsToBottom() {
      if (noteTmp.isChecklist()) {
         mChecklistManager.moveCheckedToBottom();
      }
   }

   /**
    * Categorize note choosing from a list of previously created categories
    */
   private void categorizeNote() {

      String currentCategory = noteTmp.getCategory() != null ? String.valueOf(noteTmp.getCategory().getId()) : null;
      final List<Category> categories = Observable.from(DbHelper.getInstance().getCategories()).map(category -> {
         if (String.valueOf(category.getId()).equals(currentCategory)) {
            category.setCount(category.getCount() + 1);
         }
         return category;
      }).toList().toBlocking().single();

      final MaterialDialog dialog = new MaterialDialog.Builder(mainActivity)
              .title(R.string.categorize_as)
              .adapter(new CategoryRecyclerViewAdapter(mainActivity, categories), null)
              .backgroundColorAttr(R.attr.themeDialogBackgroundColor)
              .positiveText(R.string.add_category)
              .positiveColorAttr(R.attr.themeAccent)
              .negativeText(R.string.remove_category)
              .negativeColorAttr(R.attr.themeDialogNormalButtonColor)
              .onPositive((dialog1, which) -> {
                 Intent intent = new Intent(mainActivity, CategoryActivity.class);
                 intent.putExtra("noHome", true);
                 startActivityForResult(intent, CATEGORY);
              })
              .onNegative((dialog12, which) -> {
                 noteTmp.setCategory(null);
                 setTagMarkerColor(null);
              }).build();

      RecyclerViewItemClickSupport.addTo(dialog.getRecyclerView()).setOnItemClickListener((recyclerView, position, v) -> {
         noteTmp.setCategory(categories.get(position));
         setTagMarkerColor(categories.get(position));
         dialog.dismiss();
      });

      dialog.show();

   }

   private void showAttachmentsPopup() {
      LayoutInflater inflater = mainActivity.getLayoutInflater();
      final View layout = inflater.inflate(R.layout.attachment_dialog, null);

      attachmentDialog = new MaterialDialog.Builder(mainActivity)
              .autoDismiss(false)
              .customView(layout, false)
              .build();
      attachmentDialog.show();

      // Camera
      android.widget.TextView cameraSelection = layout.findViewById(R.id.camera);
      cameraSelection.setOnClickListener(new AttachmentOnClickListener());
      // Audio recording
      android.widget.TextView recordingSelection = layout.findViewById(R.id.recording);
      toggleAudioRecordingStop(recordingSelection);
      recordingSelection.setOnClickListener(new AttachmentOnClickListener());
      // Video recording
      android.widget.TextView videoSelection = layout.findViewById(R.id.video);
      videoSelection.setOnClickListener(new AttachmentOnClickListener());
      // Files
      android.widget.TextView filesSelection = layout.findViewById(R.id.files);
      filesSelection.setOnClickListener(new AttachmentOnClickListener());
      // Sketch
      android.widget.TextView sketchSelection = layout.findViewById(R.id.sketch);
      sketchSelection.setOnClickListener(new AttachmentOnClickListener());
      // Location
      android.widget.TextView locationSelection = layout.findViewById(R.id.location);
      locationSelection.setOnClickListener(new AttachmentOnClickListener());
      // Time
      android.widget.TextView timeStampSelection = layout.findViewById(R.id.timestamp);
      timeStampSelection.setOnClickListener(new AttachmentOnClickListener());
      // Desktop note with PushBullet
      android.widget.TextView pushbulletSelection = layout.findViewById(R.id.pushbullet);
      pushbulletSelection.setVisibility(View.GONE);
      pushbulletSelection.setOnClickListener(new AttachmentOnClickListener());
   }

   private void takePhoto() {
      // Checks for camera app available
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      if (!IntentChecker.isAvailable(mainActivity, intent, new String[]{PackageManager.FEATURE_CAMERA})) {
         mainActivity.showMessage(R.string.feature_not_available_on_this_device, ONStyle.ALERT);
         return;
      }
      // Checks for created file validity
      File f = StorageHelper.createNewAttachmentFile(mainActivity, MIME_TYPE_IMAGE_EXT);
      if (f == null) {
         mainActivity.showMessage(R.string.error, ONStyle.ALERT);
         return;
      }
      attachmentUri = FileProviderHelper.getFileProvider(f);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, attachmentUri);
      startActivityForResult(intent, TAKE_PHOTO);
   }

   private void takeVideo() {
      Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
      if (!IntentChecker.isAvailable(mainActivity, takeVideoIntent, new String[]{PackageManager.FEATURE_CAMERA})) {
         mainActivity.showMessage(R.string.feature_not_available_on_this_device, ONStyle.ALERT);
         return;
      }
      // File is stored in custom ON folder to speedup the attachment
      File f = StorageHelper.createNewAttachmentFile(mainActivity, MIME_TYPE_VIDEO_EXT);
      if (f == null) {
         mainActivity.showMessage(R.string.error, ONStyle.ALERT);
         return;
      }
      attachmentUri = FileProviderHelper.getFileProvider(f);
      takeVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, attachmentUri);
      String maxVideoSizeStr = "".equals(Prefs.getString("settings_max_video_size",
              "")) ? "0" : Prefs.getString("settings_max_video_size", "");
      long maxVideoSize = parseLong(maxVideoSizeStr) * 1024L * 1024L;
      takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, maxVideoSize);
      startActivityForResult(takeVideoIntent, TAKE_VIDEO);
   }

   private void takeSketch(Attachment attachment) {

      File f = StorageHelper.createNewAttachmentFile(mainActivity, MIME_TYPE_SKETCH_EXT);
      if (f == null) {
         mainActivity.showMessage(R.string.error, ONStyle.ALERT);
         return;
      }
      attachmentUri = Uri.fromFile(f);

      // Forces portrait orientation to this fragment only
      mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

      // Fragments replacing
      FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
      mainActivity.animateTransition(transaction, TRANSITION_HORIZONTAL);
      SketchFragment mSketchFragment = new SketchFragment();
      Bundle b = new Bundle();
      b.putParcelable(MediaStore.EXTRA_OUTPUT, attachmentUri);
      if (attachment != null) {
         b.putParcelable("base", attachment.getUri());
      }
      mSketchFragment.setArguments(b);
      transaction.replace(R.id.fragment_container, mSketchFragment, mainActivity.FRAGMENT_SKETCH_TAG)
              .addToBackStack(FRAGMENT_DETAIL_TAG).commit();
   }

   private void addTimestamp() {
      Editable editable = binding.fragmentDetailContent.detailContent.getText();
      int position = binding.fragmentDetailContent.detailContent.getSelectionStart();
      DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
      String dateStamp = dateFormat.format(new Date().getTime()) + " ";
      if (noteTmp.isChecklist()) {
         if (mChecklistManager.getFocusedItemView() != null) {
            editable = mChecklistManager.getFocusedItemView().getEditText().getEditableText();
            position = mChecklistManager.getFocusedItemView().getEditText().getSelectionStart();
         } else {
            ((CheckListView) toggleChecklistView)
                    .addItem(dateStamp, false, mChecklistManager.getCount());
         }
      }
      String leadSpace = position == 0 ? "" : " ";
      dateStamp = leadSpace + dateStamp;
      editable.insert(position, dateStamp);
      Selection.setSelection(editable, position + dateStamp.length());
   }

   @SuppressLint("NewApi")
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent intent) {
      // Fetch uri from activities, store into adapter and refresh adapter
      Attachment attachment;
      if (resultCode == Activity.RESULT_OK) {
         switch (requestCode) {
            case TAKE_PHOTO:
               attachment = new Attachment(attachmentUri, MIME_TYPE_IMAGE);
               addAttachment(attachment);
               mAttachmentAdapter.notifyDataSetChanged();
               mGridView.autoresize();
               break;
            case TAKE_VIDEO:
               attachment = new Attachment(attachmentUri, MIME_TYPE_VIDEO);
               addAttachment(attachment);
               mAttachmentAdapter.notifyDataSetChanged();
               mGridView.autoresize();
               break;
            case FILES:
               onActivityResultManageReceivedFiles(intent);
               break;
            case SET_PASSWORD:
               noteTmp.setPasswordChecked(true);
               lockUnlock();
               break;
            case SKETCH:
               attachment = new Attachment(attachmentUri, MIME_TYPE_SKETCH);
               addAttachment(attachment);
               mAttachmentAdapter.notifyDataSetChanged();
               mGridView.autoresize();
               break;
            case CATEGORY:
               mainActivity.showMessage(R.string.category_saved, ONStyle.CONFIRM);
               Category category = intent.getParcelableExtra("category");
               noteTmp.setCategory(category);
               setTagMarkerColor(category);
               break;
            case DETAIL:
               mainActivity.showMessage(R.string.note_updated, ONStyle.CONFIRM);
               break;
            default:
               LogDelegate.e("Wrong element choosen: " + requestCode);
         }
      }
   }

   private void onActivityResultManageReceivedFiles(Intent intent) {
      List<Uri> uris = new ArrayList<>();
      if (intent.getClipData() != null) {
         for (int i = 0; i < intent.getClipData().getItemCount(); i++) {
            uris.add(intent.getClipData().getItemAt(i).getUri());
         }
      } else {
         uris.add(intent.getData());
      }
      for (Uri uri : uris) {
         String name = FileHelper.getNameFromUri(mainActivity, uri);
         new AttachmentTask(this, uri, name, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
      }
   }

   private String removeTitleFrom(String htmlText) {
      String sanitizedtext = htmlText.replaceAll("(<br>)|<BR>", "\n").trim();

      if (sanitizedtext.startsWith(ConstantsBase.HTML_TEXT_BODY_CLASS)) {
         sanitizedtext = sanitizedtext.replaceFirst(ConstantsBase.HTML_TEXT_BODY_CLASS, "");
         if (sanitizedtext.endsWith(ConstantsBase.HTML_DIV_END_TAG)) {
            sanitizedtext = sanitizedtext.substring(0, sanitizedtext.length() - ConstantsBase.HTML_DIV_END_TAG.length());
         }
      }
      return sanitizedtext;
   }

   private void editNote() {
      View contentView = binding.detailRoot.findViewById(R.id.detail_content);
      View tileCard = binding.detailRoot.findViewById(R.id.detail_tile_card);

      binding.fragmentDetailContent.myweb.setVisibility(View.GONE);
      binding.fragmentDetailContent.detailContent.setText(removeTitleFrom(noteTmp.getHTMLContent()));
      contentView.setVisibility(View.VISIBLE);
      tileCard.setVisibility(View.VISIBLE);
      mainActivity.supportInvalidateOptionsMenu(); // need refresh action bar menu because some icons are irrelevant in edit mode
   }

   /**
    * Discards changes done to the note and eventually delete new attachments
    */
   private void discard() {
      new MaterialDialog.Builder(mainActivity)
              .content(R.string.undo_changes_note_confirmation)
              .positiveText(R.string.ok)
              .onPositive((dialog, which) -> {
                 if (!noteTmp.getAttachmentsList().equals(note.getAttachmentsList())) {
                    for (Attachment newAttachment : noteTmp.getAttachmentsList()) {
                       if (!note.getAttachmentsList().contains(newAttachment)) {
                          StorageHelper.delete(mainActivity, newAttachment.getUri().getPath());
                       }
                    }
                 }

                 goBack = true;

                 if (noteTmp.equals(noteOriginal)) {
                    goHome();
                 }

                 if (noteOriginal.get_id() != null) {
                    new SaveNoteTask(mFragment, false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, noteOriginal);
                    BaseActivity.notifyAppWidgets(mainActivity);
                 } else {
                    goHome();
                 }
              }).build().show();
   }

   @SuppressLint("NewApi")
   private void archiveNote(boolean archive) {
      // Simply go back if is a new note
      if (noteTmp.get_id() == null) {
         goHome();
         return;
      }

      noteTmp.setArchived(archive);
      goBack = true;
      exitMessage = archive ? getString(R.string.note_archived) : getString(R.string.note_unarchived);
      exitCroutonStyle = archive ? ONStyle.WARN : ONStyle.INFO;
      saveNote(this);
   }

   @SuppressLint("NewApi")
   private void trashNote(boolean trash) {
      // Simply go back if is a new note
      if (noteTmp.get_id() == null) {
         goHome();
         return;
      }

      noteTmp.setTrashed(trash);
      goBack = true;
      exitMessage = trash ? getString(R.string.note_trashed) : getString(R.string.note_untrashed);
      exitCroutonStyle = trash ? ONStyle.WARN : ONStyle.INFO;
      if (trash) {
         ShortcutHelper.removeShortcut(Knizka.getAppContext(), noteTmp);
         ReminderHelper.removeReminder(Knizka.getAppContext(), noteTmp);
      } else {
         ReminderHelper.addReminder(Knizka.getAppContext(), note);
      }
      saveNote(this);
   }

   private void deleteNote() {
      new MaterialDialog.Builder(mainActivity)
              .content(R.string.delete_note_confirmation)
              .positiveText(R.string.ok)
              .onPositive((dialog, which) -> {
                 mainActivity.deleteNote(noteTmp);
                 LogDelegate.d("Deleted note with ID '" + noteTmp.get_id() + "'");
                 mainActivity.showMessage(R.string.note_deleted, ONStyle.ALERT);
                 EventBus.getDefault().post(new NotesUpdatedEvent(Collections.singletonList(noteTmp))); // todo: @pk check if cannot be done better, send event that menu has to be refreshed
                 goHome();
              }).build().show();
   }

   public void saveAndExit(OnNoteSaved mOnNoteSaved) {
      if (isAdded()) {
         exitMessage = getString(R.string.note_updated);
         exitCroutonStyle = ONStyle.CONFIRM;
         goBack = true;
         saveNote(mOnNoteSaved);
      }
   }

   /**
    * Save new notes, modify them or archive
    */
   void saveNote(OnNoteSaved mOnNoteSaved) {

      // Changed fields
      noteTmp.setTitle(getNoteTitle());
      noteTmp.setContent(getNoteContent());
      noteTmp.setHTMLContent(getNoteContent());

      // Check if some text or attachments of any type have been inserted or is an empty note
      if (goBack && TextUtils.isEmpty(noteTmp.getTitle()) && TextUtils.isEmpty(noteTmp.getContent())
              && noteTmp.getAttachmentsList().isEmpty()) {
         LogDelegate.d("Empty note not saved");
         exitMessage = getString(R.string.empty_note_not_saved);
         exitCroutonStyle = ONStyle.INFO;
         goHome();
         return;
      }

      if (saveNotNeeded()) {
         exitMessage = "";
         if (goBack) {
            goHome();
         }
         return;
      }

      noteTmp.setAttachmentsListOld(note.getAttachmentsList());

      new SaveNoteTask(mOnNoteSaved, lastModificationUpdatedNeeded()).executeOnExecutor(AsyncTask
              .THREAD_POOL_EXECUTOR, noteTmp);
   }

   /**
    * Checks if nothing is changed to avoid committing if possible (check)
    */
   private boolean saveNotNeeded() {
      if (noteTmp.get_id() == null && Prefs.getBoolean(PREF_AUTO_LOCATION, false)) {
         note.setLatitude(noteTmp.getLatitude());
         note.setLongitude(noteTmp.getLongitude());
      }
      return !noteTmp.isChanged(note) || (noteTmp.isLocked() && !noteTmp.isPasswordChecked());
   }

   /**
    * Checks if only tag, archive or trash status have been changed and then force to not update last modification date*
    */
   private boolean lastModificationUpdatedNeeded() {
      note.setCategory(noteTmp.getCategory());
      note.setArchived(noteTmp.isArchived());
      note.setErased(noteTmp.isErased());
      note.setTrashed(noteTmp.isTrashed());
      note.setLocked(noteTmp.isLocked());
      return noteTmp.isChanged(note);
   }

   @Override
   public void onNoteSaved(Note noteSaved) {
      if (!activityPausing) {
         EventBus.getDefault().post(new NotesUpdatedEvent(Collections.singletonList(noteSaved)));
         deleteMergedNotes(mergedNotesIds);
         if (noteTmp.getAlarm() != null && !noteTmp.getAlarm().equals(note.getAlarm())) {
            ReminderHelper.showReminderMessage(String.valueOf(noteTmp.getAlarm()));
         }
      }
      note = new Note(noteSaved);
      if (goBack) {
         goHome();
      }
   }

   private void deleteMergedNotes(List<String> mergedNotesIds) {
      ArrayList<Note> notesToDelete = new ArrayList<>();
      if (mergedNotesIds != null) {
         for (String mergedNoteId : mergedNotesIds) {
            Note noteToDelete = new Note();
            noteToDelete.set_id(Long.valueOf(mergedNoteId));
            notesToDelete.add(noteToDelete);
         }
         new NoteProcessorDelete(notesToDelete).process();
      }
   }

   private String getNoteTitle() {
    if (!TextUtils.isEmpty(binding.detailTitle.getText())) {
         return binding.detailTitle.getText().toString();
      } else {
         return "";
      }
   }

   private String getNoteContent() {
      String contentText = "";
      if (!noteTmp.isChecklist()) {
         // Due to checklist library introduction the returned EditText class is no more a
         // com.neopixl.pixlui.components.edittext.EditText but a standard android.widget.EditText
         View contentView = binding.detailRoot.findViewById(R.id.detail_content);
         if (contentView instanceof EditText) {
            contentText = ((EditText) contentView).getText().toString();
         } else if (contentView instanceof android.widget.EditText) {
            contentText = ((android.widget.EditText) contentView).getText().toString();
         }
      } else {
         if (mChecklistManager != null) {
            mChecklistManager.keepChecked(true).showCheckMarks(true);
            contentText = mChecklistManager.getText();
         }
      }
      return contentText;
   }

   /**
    * Updates share intent
    */
   private void shareNote() {
      Note sharedNote = new Note(noteTmp);
      sharedNote.setTitle(getNoteTitle());
//    sharedNote.setContent(getNoteContent());
      sharedNote.setContent(PKStringUtils.stripHTML(noteTmp.getHTMLContent())); // todo: @pk: not saved note will be not properly shared, should be done as it is in getNoteContent()

      mainActivity.shareNote(sharedNote);
   }

   /**
    * Notes locking with security password to avoid viewing, editing or deleting from unauthorized
    */
   private void lockNote() {
      LogDelegate.d("Locking or unlocking note " + note.get_id());

      // If security password is not set yes will be set right now
      if (Prefs.getString(PREF_PASSWORD, null) == null) {
         Intent passwordIntent = new Intent(mainActivity, PasswordActivity.class);
         startActivityForResult(passwordIntent, SET_PASSWORD);
         return;
      }

      // If password has already been inserted will not be asked again
      if (noteTmp.isPasswordChecked() || Prefs.getBoolean("settings_password_access", false)) {
         lockUnlock();
         return;
      }

      // Password will be requested here
      PasswordHelper.requestPassword(mainActivity, passwordConfirmed -> {
         switch (passwordConfirmed) {
            case SUCCEED:
               lockUnlock();
               break;
            default:
               break;
         }
      });
   }

   private void lockUnlock() {
      // Empty password has been set
      if (Prefs.getString(PREF_PASSWORD, null) == null) {
         mainActivity.showMessage(R.string.password_not_set, ONStyle.WARN);
         return;
      }
      mainActivity.showMessage(R.string.save_note_to_lock_it, ONStyle.INFO);
      mainActivity.supportInvalidateOptionsMenu();
      noteTmp.setLocked(!noteTmp.isLocked());
      noteTmp.setPasswordChecked(true);
   }

   /**
    * Used to set actual reminder state when initializing a note to be edited
    */
   private String initReminder(Note note) {
      if (noteTmp.getAlarm() == null) {
         return "";
      }
      long reminder = parseLong(note.getAlarm());
      String rrule = note.getRecurrenceRule();
      if (!TextUtils.isEmpty(rrule)) {
         return RecurrenceHelper.getNoteRecurrentReminderText(reminder, rrule);
      } else {
         return RecurrenceHelper.getNoteReminderText(reminder);
      }
   }

   /**
    * Audio recordings playback
    */
   private void playback(View v, Uri uri) {
      // Some recording is playing right now
      if (mPlayer != null && mPlayer.isPlaying()) {
         if (isPlayingView != v) {
            // If the audio actually played is NOT the one from the click view the last one is played
            stopPlaying();
            isPlayingView = v;
            startPlaying(uri);
            replacePlayingAudioBitmap(v);
         } else {
            // Otherwise just stops playing
            stopPlaying();
         }
      } else {
         // If nothing is playing audio just plays
         isPlayingView = v;
         startPlaying(uri);
         replacePlayingAudioBitmap(v);
      }
   }

   private void replacePlayingAudioBitmap(View v) {
      Drawable d = ((ImageView) v.findViewById(R.id.gridview_item_picture)).getDrawable();
      if (BitmapDrawable.class.isAssignableFrom(d.getClass())) {
         recordingBitmap = ((BitmapDrawable) d).getBitmap();
      } else {
         recordingBitmap = ((BitmapDrawable) d.getCurrent()).getBitmap();
      }
      ((ImageView) v.findViewById(R.id.gridview_item_picture)).setImageBitmap(ThumbnailUtils
              .extractThumbnail(BitmapFactory.decodeResource(mainActivity.getResources(),
                      R.drawable.stop), THUMBNAIL_SIZE, THUMBNAIL_SIZE));
   }

   private void startPlaying(Uri uri) {
      if (mPlayer == null) {
         mPlayer = new MediaPlayer();
      }
      try {
         mPlayer.setDataSource(mainActivity, uri);
         mPlayer.prepare();
         mPlayer.start();
         mPlayer.setOnCompletionListener(mp -> {
            mPlayer = null;
            if (isPlayingView != null) {
               ((ImageView) isPlayingView.findViewById(R.id.gridview_item_picture)).setImageBitmap
                       (recordingBitmap);
               recordingBitmap = null;
               isPlayingView = null;
            }
         });
      } catch (IOException e) {
         LogDelegate.e("prepare() failed", e);
         mainActivity.showMessage(R.string.error, ONStyle.ALERT);
      }
   }

   private void stopPlaying() {
      if (mPlayer != null) {
         if (isPlayingView != null) {
            ((ImageView) isPlayingView.findViewById(R.id.gridview_item_picture)).setImageBitmap(recordingBitmap);
         }
         isPlayingView = null;
         recordingBitmap = null;
         mPlayer.release();
         mPlayer = null;
      }
   }

   private void startRecording(View v) {
      PermissionsHelper.requestPermission(getActivity(), Manifest.permission.RECORD_AUDIO,
              R.string.permission_audio_recording, binding.snackbarPlaceholder, () -> {

                 isRecording = true;
                 toggleAudioRecordingStop(v);

                 File f = StorageHelper.createNewAttachmentFile(mainActivity, MIME_TYPE_AUDIO_EXT);
                 if (f == null) {
                    mainActivity.showMessage(R.string.error, ONStyle.ALERT);
                    return;
                 }
                 if (mRecorder == null) {
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mRecorder.setAudioEncodingBitRate(96000);
                    mRecorder.setAudioSamplingRate(44100);
                 }
                 recordName = f.getAbsolutePath();
                 mRecorder.setOutputFile(recordName);

                 try {
                    audioRecordingTimeStart = Calendar.getInstance().getTimeInMillis();
                    mRecorder.prepare();
                    mRecorder.start();
                 } catch (IOException | IllegalStateException e) {
                    LogDelegate.e("prepare() failed", e);
                    mainActivity.showMessage(R.string.error, ONStyle.ALERT);
                 }
              });
   }

   private void toggleAudioRecordingStop(View v) {
      if (isRecording) {
         ((android.widget.TextView) v).setText(getString(R.string.stop));
         ((android.widget.TextView) v).setTextColor(Color.parseColor("#ff0000"));
      }
   }

   private void stopRecording() {
      isRecording = false;
      if (mRecorder != null) {
         mRecorder.stop();
         audioRecordingTime = Calendar.getInstance().getTimeInMillis() - audioRecordingTimeStart;
         mRecorder.release();
         mRecorder = null;
      }
   }

   private void fade(final View v, boolean fadeIn) {

      int anim = R.animator.fade_out_support;
      int visibilityTemp = View.GONE;

      if (fadeIn) {
         anim = R.animator.fade_in_support;
         visibilityTemp = View.VISIBLE;
      }

      final int visibility = visibilityTemp;

      // Checks if user has left the app
      if (mainActivity != null) {
         Animation mAnimation = AnimationUtils.loadAnimation(mainActivity, anim);
         mAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
               // Nothing to do
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
               // Nothing to do
            }

            @Override
            public void onAnimationEnd(Animation animation) {
               v.setVisibility(visibility);
            }
         });
         v.startAnimation(mAnimation);
      }
   }

   /**
    * Pin note as ongoing notifications
    */
   private void pinNote() {
      PendingIntent notifyIntent = IntentHelper.getNotePendingIntent(getContext(), SnoozeActivity.class, ACTION_PINNED, note);

      Spanned[] titleAndContent = TextHelper.parseTitleAndContent(getContext(), note);
      String pinnedTitle = titleAndContent[0].toString();
      String pinnedContent = titleAndContent[1].toString();

      NotificationsHelper notificationsHelper = new NotificationsHelper(getContext());
      notificationsHelper.createOngoingNotification(NotificationChannelNames.PINNED, R.drawable.ic_stat_notification,
              pinnedTitle, notifyIntent).setMessage(pinnedContent);

      List<Attachment> attachments = note.getAttachmentsList();
      if (!attachments.isEmpty() && !attachments.get(0).getMime_type().equals(MIME_TYPE_FILES)) {
         Bitmap notificationIcon = BitmapHelper.getBitmapFromAttachment(getContext(), note.getAttachmentsList().get(0), 128,
                 128);
         notificationsHelper.setLargeIcon(notificationIcon);
      }

      PendingIntent unpinIntent = IntentHelper.getNotePendingIntent(getContext(), SnoozeActivity.class, ACTION_DISMISS, note);
      notificationsHelper.getBuilder().addAction(R.drawable.ic_material_reminder_time_light, getString(R.string.remove_pinned), unpinIntent);

      notificationsHelper.show(note.get_id());
   }

   /**
    * Adding shortcut on Home screen
    */
   private void addShortcut() {
      ShortcutHelper.addShortcut(Knizka.getAppContext(), noteTmp);
      mainActivity.showMessage(R.string.shortcut_added, ONStyle.INFO);
   }

   private void showNextNote(int direction) {
      Note newNote;
      if (direction < 0) {
         newNote = DbHelper.getInstance().getNote(note.getHandleID() + 1);
      } else {
         newNote = DbHelper.getInstance().getNote(note.getHandleID() - 1);
      }

      FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
      mainActivity.animateTransition(transaction, TRANSITION_VERTICAL);
      DetailFragment mDetailFragment = new DetailFragment();
      Bundle b = new Bundle();
      b.putParcelable(INTENT_NOTE, newNote);
      mDetailFragment.setArguments(b);
      transaction.replace(R.id.fragment_container, mDetailFragment, FRAGMENT_DETAIL_TAG).addToBackStack(FRAGMENT_DETAIL_TAG).commit();
   }

   private void activateBrowsingTexts(int direction) {

      String actTitle = "";
      int notePosition = 0;
      ArrayList<String> notesIds = new ArrayList<>();
      int i = 0;
      if (mainActivity.getNotesList() != null)
      {
         for (Note note : mainActivity.getNotesList()) {
            notesIds.add("" + note.getHandleID().longValue());
            if (noteTmp.getHandleID().longValue() == note.getHandleID().longValue())
            {
               notePosition = i + direction;
            }
            i++;
         }
      } else {

         for (String s : notesIdsInListView) {
            notesIds.add(s);
            if (noteTmp.getHandleID().longValue() == Long.valueOf(s).longValue())
            {
               notePosition = i + direction;
            }
            i++;
         }
      }

      if (notesIds.isEmpty())
      {
         mainActivity.showMessage(R.string.jks_list_is_empty, ONStyle.ALERT);
         binding.contentWrapper.animate().x(0).y(0).setDuration(100).start();
      } else {
         Intent browseTextsFormatIntent = new Intent(getActivity(), BrowseTextsActivity.class);
         browseTextsFormatIntent.putExtra(INTENT_EXTRA_MAX_PAGES_IN_BROWSER, notesIds.size());
         browseTextsFormatIntent.putExtra(INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER, notesIds);
         browseTextsFormatIntent.putExtra(INTENT_EXTRA_LIST_VIEW_POSITION_OFFSET_FOR_VIEWPAGER, notePosition);

         actTitle = mainActivity.getNotesListCaption();
         if ((actTitle == null) || (actTitle.isEmpty()))
         {
            actTitle = titleForBrowsing;
         }

         browseTextsFormatIntent.putExtra(INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER, actTitle);

         startActivity(browseTextsFormatIntent);
         navigateUp();
      }
   }

   @SuppressLint("NewApi")
   @Override
   public boolean onTouch(View v, MotionEvent event) {
      int x = (int) event.getX();
      int y = (int) event.getY();

      switch (event.getAction()) {

         case MotionEvent.ACTION_DOWN:
            LogDelegate.v("MotionEvent.ACTION_DOWN");
            int w;

            Point displaySize = Display.getUsableSize(mainActivity);
            w = displaySize.x;

            if (x < SWIPE_MARGIN || x > w - SWIPE_MARGIN) {
               swiping = true;
               startSwipeX = x;
            }

            break;

         case MotionEvent.ACTION_UP:
            LogDelegate.v("MotionEvent.ACTION_UP");
            if (swiping) {
               swiping = false;
            }
            break;

         case MotionEvent.ACTION_MOVE:
            if (swiping) {
               LogDelegate.v("MotionEvent.ACTION_MOVE at position " + x + ", " + y);
               if (Math.abs(x - startSwipeX) > SWIPE_OFFSET) {
                  swiping = false;
                  //showNextNote(x - startSwipeX);  //in general works, but not good user experience
               //   activateBrowsingTexts(x - startSwipeX); // could activate texts browsing, but not used now
            /*  // original code, creates new note
            FragmentTransaction transaction = mainActivity.getSupportFragmentManager().beginTransaction();
            mainActivity.animateTransition(transaction, TRANSITION_VERTICAL);
            DetailFragment mDetailFragment = new DetailFragment();
            Bundle b = new Bundle();
            b.putParcelable(INTENT_NOTE, new Note());
            mDetailFragment.setArguments(b);
            transaction.replace(R.id.fragment_container, mDetailFragment, FRAGMENT_DETAIL_TAG).addToBackStack(
                FRAGMENT_DETAIL_TAG).commit();

             */
               }
            }
            break;

         default:
            LogDelegate.e("Wrong element choosen: " + event.getAction());
      }

      return true;
   }

   @Override
   public void onAttachingFileErrorOccurred(Attachment mAttachment) {
      mainActivity.showMessage(R.string.error_saving_attachments, ONStyle.ALERT);
      if (noteTmp.getAttachmentsList().contains(mAttachment)) {
         removeAttachment(mAttachment);
         mAttachmentAdapter.notifyDataSetChanged();
         mGridView.autoresize();
      }
   }

   private void addAttachment(Attachment attachment) {
      noteTmp.addAttachment(attachment);
   }

   private void removeAttachment(Attachment mAttachment) {
      noteTmp.removeAttachment(mAttachment);
   }

   private void removeAttachment(int position) {
      noteTmp.removeAttachment(noteTmp.getAttachmentsList().get(position));
   }

   private void removeAllAttachments() {
      noteTmp.setAttachmentsList(new ArrayList<>());
      mAttachmentAdapter = new AttachmentAdapter(mainActivity, new ArrayList<>(), mGridView);
      mGridView.invalidateViews();
      mGridView.setAdapter(mAttachmentAdapter);
   }

   @Override
   public void onAttachingFileFinished(Attachment mAttachment) {
      addAttachment(mAttachment);
      mAttachmentAdapter.notifyDataSetChanged();
      mGridView.autoresize();
   }

   @Override
   public void onReminderPicked(long reminder) {
      noteTmp.setAlarm(reminder);
      if (mFragment.isAdded()) {
         binding.fragmentDetailContent.reminderIcon.setImageDrawable(getAlarmIconForTheme(R.attr.ic_alarm_note_layout));
         binding.fragmentDetailContent.datetime.setText(RecurrenceHelper.getNoteReminderText(reminder));
      }
   }

   @Override
   public void onRecurrenceReminderPicked(String recurrenceRule) {
      noteTmp.setRecurrenceRule(recurrenceRule);
      if (!TextUtils.isEmpty(recurrenceRule)) {
         LogDelegate.d("Recurrent reminder set: " + recurrenceRule);
         binding.fragmentDetailContent.datetime.setText(RecurrenceHelper.getNoteRecurrentReminderText(Long.parseLong(noteTmp.getAlarm()), recurrenceRule));
      }
   }

   @Override
   public void onTextChanged(CharSequence s, int start, int before, int count) {
      scrollContent();
   }

   @Override
   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      // Nothing to do
   }

   @Override
   public void afterTextChanged(Editable s) {
      // Nothing to do
   }

   @Override
   public void onCheckListChanged() {
      scrollContent();
   }

   private void scrollContent() {
      if (noteTmp.isChecklist()) {
         if (mChecklistManager.getCount() > contentLineCounter) {
            binding.contentWrapper.scrollBy(0, 60);
         }
         contentLineCounter = mChecklistManager.getCount();
      } else {
         if (binding.fragmentDetailContent.detailContent.getLineCount() > contentLineCounter) {
            binding.contentWrapper.scrollBy(0, 60);
         }
         contentLineCounter = binding.fragmentDetailContent.detailContent.getLineCount();
      }
   }

   private void showAddNewTagsDialog(MaterialDialog dialogToDismiss) {
      MaterialDialog dialog = new MaterialDialog.Builder(mainActivity)
              .title(R.string.add_new_tag_caption)
              .input(getString(R.string.add_new_tag_hint), "",
                      new MaterialDialog.InputCallback() {
                         @Override
                         public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            if (dialogToDismiss != null) {
                               dialogToDismiss.dismiss();
                            }
                            addTags(input.toString());
                         }
                      })
              .build();
      dialog.show();
   }

   /**
    * Add previously created tags to content
    */
   private void addTags(String newTags) {
      contentCursorPosition = getCursorIndex();

      final List<Tag> tags = TagsHelper.getAllTags();

      TagsHelper.AddAdditionalTags(tags, newTags);
      TagsHelper.AddAdditionalTags(tags, noteTmp.getTagList()); // add "new" tags already assigned to note, but not saved in DB

      if (tags.isEmpty()) {
         //mainActivity.showMessage(R.string.no_tags_created, ONStyle.WARN);
         showAddNewTagsDialog(null);
         return;
      }

      final Note currentNote = new Note();
      currentNote.setTitle(getNoteTitle());
      currentNote.setContent(getNoteContent());
      currentNote.setTagList(noteTmp.getTagList());
      Integer[] preselectedTags = TagsHelper.getPreselectedTagsArray(currentNote, tags);

      // Dialog and events creation
      MaterialDialog dialog = new MaterialDialog.Builder(mainActivity)
              .title(R.string.select_tags)
              .positiveText(R.string.ok)
              .negativeText(R.string.add_new_tag)
              .negativeColorRes(R.color.colorPrimary)
              .items(TagsHelper.getTagsArray(tags))

              .autoDismiss(false)
              .itemsCallbackMultiChoice(preselectedTags, (dialog1, which, text) -> {
                 dialog1.dismiss();
                 tagNote(tags, which, currentNote);
                 return false;
              })
              .onNegative((dialog12, which) -> {
                 showAddNewTagsDialog(dialog12);
              })
              .build();
      dialog.show();
   }

   private void tagNote(List<Tag> tags, Integer[] selectedTags, Note note) {
      Pair<String, List<Tag>> taggingResult = TagsHelper.addTagToNote(tags, selectedTags, note);
      noteTmp.setTagList(taggingResult.first);
   }

   private int getCursorIndex() {
      if (!noteTmp.isChecklist()) {
         return binding.fragmentDetailContent.detailContent.getSelectionStart();
      } else {
         CheckListViewItem mCheckListViewItem = mChecklistManager.getFocusedItemView();
         if (mCheckListViewItem != null) {
            return mCheckListViewItem.getEditText().getSelectionStart();
         } else {
            return 0;
         }
      }
   }

   /**
    * Used to check currently opened note from activity to avoid openind multiple times the same one
    */
   public Note getCurrentNote() {
      return note;
   }

   private boolean isNoteLocationValid() {
      return noteTmp.getLatitude() != null
              && noteTmp.getLatitude() != 0
              && noteTmp.getLongitude() != null
              && noteTmp.getLongitude() != 0;
   }

   public void startGetContentAction() {
      Intent filesIntent;
      filesIntent = new Intent(Intent.ACTION_GET_CONTENT);
      filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
      filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
      filesIntent.setType("*/*");
      startActivityForResult(filesIntent, FILES);
   }

   private void askReadExternalStoragePermission() {
      PermissionsHelper.requestPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,
              R.string.permission_external_storage_detail_attachment,
              binding.snackbarPlaceholder, this::startGetContentAction);
   }

  public void onEventMainThread(PushbulletReplyEvent pushbulletReplyEvent) {
    String text =
        getNoteContent() + System.getProperty("line.separator") + pushbulletReplyEvent.getMessage();
    binding.fragmentDetailContent.detailContent.setText(text);
  }

   private static class OnGeoUtilResultListenerImpl implements OnGeoUtilResultListener {

      private final WeakReference<MainActivity> mainActivityWeakReference;
      private final WeakReference<DetailFragment> detailFragmentWeakReference;
      private final WeakReference<Note> noteTmpWeakReference;

      OnGeoUtilResultListenerImpl(MainActivity activity, DetailFragment mFragment, Note noteTmp) {
         mainActivityWeakReference = new WeakReference<>(activity);
         detailFragmentWeakReference = new WeakReference<>(mFragment);
         noteTmpWeakReference = new WeakReference<>(noteTmp);
      }

      @Override
      public void onAddressResolved(String address) {
         // Nothing to do
      }

      @Override
      public void onCoordinatesResolved(Location location, String address) {
         // Nothing to do
      }

      @Override
      public void onLocationUnavailable() {
         mainActivityWeakReference.get().showMessage(R.string.location_not_found, ONStyle.ALERT);
      }

      @Override
      public void onLocationRetrieved(Location location) {

         if (!checkWeakReferences()) {
            return;
         }

         if (location == null) {
            return;
         }
         if (!ConnectionManager.internetAvailable(mainActivityWeakReference.get())) {
            noteTmpWeakReference.get().setLatitude(location.getLatitude());
            noteTmpWeakReference.get().setLongitude(location.getLongitude());
            onAddressResolved("");
            return;
         }
         LayoutInflater inflater = mainActivityWeakReference.get().getLayoutInflater();
         View v = inflater.inflate(R.layout.dialog_location, null);
         final AutoCompleteTextView autoCompView = v.findViewById(R.id
                 .auto_complete_location);
         autoCompView.setHint(mainActivityWeakReference.get().getString(R.string.search_location));
         autoCompView.setAdapter(new PlacesAutoCompleteAdapter(mainActivityWeakReference.get(), R.layout
                 .simple_text_layout));
         final MaterialDialog dialog = new MaterialDialog.Builder(mainActivityWeakReference.get())
                 .customView(autoCompView, false)
                 .positiveText(R.string.use_current_location)
                 .onPositive((dialog1, which) -> {
                    if (TextUtils.isEmpty(autoCompView.getText().toString())) {
                       noteTmpWeakReference.get().setLatitude(location.getLatitude());
                       noteTmpWeakReference.get().setLongitude(location.getLongitude());
                       GeocodeHelper.getAddressFromCoordinates(location, detailFragmentWeakReference.get());
                    } else {
                       GeocodeHelper.getCoordinatesFromAddress(autoCompView.getText().toString(),
                               detailFragmentWeakReference.get());
                    }
                 })
                 .build();
         autoCompView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // Nothing to do
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.length() != 0) {
                  dialog.setActionButton(DialogAction.POSITIVE, mainActivityWeakReference.get().getString(R
                          .string.confirm));
               } else {
                  dialog.setActionButton(DialogAction.POSITIVE, mainActivityWeakReference.get().getString(R
                          .string
                          .use_current_location));
               }
            }

            @Override
            public void afterTextChanged(Editable s) {
               // Nothing to do
            }
         });
         dialog.show();
      }

      private boolean checkWeakReferences() {
         return mainActivityWeakReference.get() != null && !mainActivityWeakReference.get().isFinishing()
                 && detailFragmentWeakReference.get() != null && noteTmpWeakReference.get() != null;
      }
   }

   /**
    * Manages clicks on attachment dialog
    */
   @SuppressLint("InlinedApi")
   private class AttachmentOnClickListener implements OnClickListener {

      @Override
      public void onClick(View v) {

         switch (v.getId()) {
            // Photo from camera
            case R.id.camera:
               takePhoto();
               break;
            case R.id.recording:
               if (!isRecording) {
                  startRecording(v);
               } else {
                  stopRecording();
                  Attachment attachment = new Attachment(Uri.fromFile(new File(recordName)), MIME_TYPE_AUDIO);
                  attachment.setLength(audioRecordingTime);
                  addAttachment(attachment);
                  mAttachmentAdapter.notifyDataSetChanged();
                  mGridView.autoresize();
               }
               break;
            case R.id.video:
               takeVideo();
               break;
            case R.id.files:
               if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                       PackageManager.PERMISSION_GRANTED) {
                  startGetContentAction();
               } else {
                  askReadExternalStoragePermission();
               }
               break;
            case R.id.sketch:
               takeSketch(null);
               break;
            case R.id.location:
               displayLocationDialog();
               break;
            case R.id.timestamp:
               addTimestamp();
               break;
            case R.id.pushbullet:
               MessagingExtension.mirrorMessage(mainActivity, getString(R.string.app_name),
                       getString(R.string.pushbullet),
                       getNoteContent(), BitmapFactory.decodeResource(getResources(),
                               R.drawable.ic_stat_literal_icon),
                       null, 0);
               break;
            default:
               LogDelegate.e("Wrong element choosen: " + v.getId());
         }
         if (!isRecording) {
            attachmentDialog.dismiss();
         }
      }
   }

   private class ourBrowser extends WebViewClient {
      @SuppressWarnings("deprecation")
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
         view.loadUrl(url);
         return true;
      }

      @Override
      public void onPageFinished(WebView view, String url) {
         // view.loadUrl("javascript: void AndroidHook.showToast(document.getElementsByTagName('body')[0].innerHTML);");
         //  view.loadUrl("javascript: void AndroidHook.backgroundColorInfo(window.getComputedStyle(document.body).backgroundColor);");
      }


   }

   public class JSInterface {
      @JavascriptInterface
      public void showToast(final String html) {
         // just sample how to get HTML content to android code
         mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               Toast.makeText(mainActivity, "Reached JS: " + html, Toast.LENGTH_LONG).show();
            }
         });
      }

      @JavascriptInterface
      public void backgroundColorInfo(final String colorCode) {
         // get background color of HTML page
         int RGB = Color.LTGRAY;

         String cleanedColorString = colorCode.replaceAll("rgb", "");
         cleanedColorString = cleanedColorString.replace("(", "");
         cleanedColorString = cleanedColorString.replace(")", "");

         List<String> myList = new ArrayList<String>(Arrays.asList(cleanedColorString.split(",")));

         if (myList.size() == 3) {
            int r = Integer.parseInt(myList.get(0));
            int g = Integer.valueOf(myList.get(1).trim());
            int b = Integer.parseInt(myList.get(2).trim());
            RGB = android.graphics.Color.rgb(r, g, b);
         }

         final int RGBx = RGB; // we need final variable

         mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               binding.detailContentCard.setBackgroundColor(RGBx);
               // Toast.makeText(mainActivity, colorCode, Toast.LENGTH_LONG).show();
            }
         });
      }

   }

   final OnTouchListener touchListenerForContentWrapper = new OnTouchListener() {

      float dX;
      float dY;
      int lastAction;
      float onDownXPosition = 0;
      private boolean wasOnDownPerformed;
      private boolean wasMovingStarted;
      private int displayWidth;
      @Override
      public boolean onTouch(View v, MotionEvent event) {

         switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
               int x = (int) event.getX();

               Point displaySize = Display.getUsableSize(mainActivity);
               displayWidth = displaySize.x;

               if (((x < MIN_X_MOVING_OFFSET_FOR_TEXT_BROWSING  || x > displayWidth - MIN_X_MOVING_OFFSET_FOR_TEXT_BROWSING))
                      // || (v instanceof ScrollView)
               )
               {
                  dX = v.getX() - event.getRawX();
                  dY = v.getY() - event.getRawY();
                  onDownXPosition = event.getRawX();
                  lastAction = MotionEvent.ACTION_DOWN;
                  wasOnDownPerformed = true;
               }

               break;

            case MotionEvent.ACTION_MOVE:
               if (((Math.abs(event.getRawX() - onDownXPosition)> MIN_X_MOVING_OFFSET_FOR_TEXT_BROWSING) && (wasOnDownPerformed)) || (wasMovingStarted))
               {
                  wasMovingStarted = true;

                  // v.setY(event.getRawY() + dY);
                  v.setX(event.getRawX() + dX);

                  // fade out text if browsing will be activated
                  if ((Math.abs(event.getRawX() - onDownXPosition) > (displayWidth / ACTIVATE_TEXT_BROWSING_DISPLAY_RATIO) ))
                  {
                    v.setAlpha(0.4f);
                  } else {
                     v.setAlpha(1.0f);
                  }
               }

               lastAction = MotionEvent.ACTION_MOVE;
               break;

            case MotionEvent.ACTION_UP:

               if( (wasOnDownPerformed) && (wasMovingStarted))
               {
                  if ((Math.abs(event.getRawX() - onDownXPosition) > (displayWidth / ACTIVATE_TEXT_BROWSING_DISPLAY_RATIO))){
                     if (event.getRawX() - onDownXPosition > 0)
                     {
                        onSwipeRight();
                     } else {
                        onSwipeLeft();
                     }
                  } else {
                     // animate to the origin position, text browsing is not acitvated
                     v.animate().x(0).y(0).setDuration(100).start();
                  }
               }

               wasOnDownPerformed = false;
               wasMovingStarted = false;

               v.setAlpha(1.0f);

               break;

            default:
               return false;
         }

         return false;
      }
   };

}



