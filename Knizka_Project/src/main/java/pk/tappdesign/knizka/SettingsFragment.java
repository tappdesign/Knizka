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
import static android.media.RingtoneManager.EXTRA_RINGTONE_EXISTING_URI;
import static android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;
import static pk.tappdesign.knizka.db.DBConst.DB_ATTACHED;
import static pk.tappdesign.knizka.db.DBConst.DB_USER_DATA;
import static pk.tappdesign.knizka.helpers.PermissionsHelper.requestPermission;
import static pk.tappdesign.knizka.utils.ConstantsBase.DATE_FORMAT_EXPORT;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_EXTRA_MAX_PAGES_IN_BROWSER;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_AUTO_LOCATION;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_COLORS_APP_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_ENABLE_FILE_LOGGING;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_PASSWORD;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_SHOW_UNCATEGORIZED;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_SNOOZE_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_TOUR_COMPLETE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.reverse;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import it.feio.android.analitica.AnalyticsHelper;
import pk.tappdesign.knizka.async.DataBackupIntentService;
import pk.tappdesign.knizka.helpers.AppVersionHelper;
import pk.tappdesign.knizka.helpers.BackupHelper;
import pk.tappdesign.knizka.helpers.LanguageHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.helpers.PermissionsHelper;
import pk.tappdesign.knizka.helpers.SpringImportHelper;
import pk.tappdesign.knizka.helpers.notifications.NotificationsHelper;
import pk.tappdesign.knizka.models.ONStyle;
import pk.tappdesign.knizka.models.PasswordValidator;
import pk.tappdesign.knizka.utils.FileHelper;
import pk.tappdesign.knizka.utils.IntentChecker;
import pk.tappdesign.knizka.utils.PasswordHelper;
import pk.tappdesign.knizka.utils.ResourcesUtils;
import pk.tappdesign.knizka.utils.StorageHelper;
import pk.tappdesign.knizka.utils.SystemHelper;
import pk.tappdesign.knizka.db.DbHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;


public class SettingsFragment extends PreferenceFragmentCompat {

  private static final int SPRINGPAD_IMPORT = 0;
  private static final int RINGTONE_REQUEST_CODE = 100;
  private static final int CHOOSE_DIRECTORY_REQUEST_CODE = 200;
  private static final int CHOOSE_RAW_DIRECTORY_REQUEST_CODE = 300;
  private static final int CHOOSE_DIRECTORY_FOR_IMPORT_REQUEST_CODE = 400;
  private static final int CHOOSE_DIRECTORY_FOR_RAW_IMPORT_REQUEST_CODE = 500;
  public static final String XML_NAME = "xmlName";
  public static final String INTENT_KEY_BACKUP_NAME = "keyBackupName";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int xmlId = R.xml.settings;
    if (getArguments() != null && getArguments().containsKey(XML_NAME)) {
      xmlId = ResourcesUtils.getXmlId(Knizka.getAppContext(), ResourcesUtils.ResourceIdentifiers.XML, String
          .valueOf(getArguments().get(XML_NAME)));
    }
    addPreferencesFromResource(xmlId);
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    //setTitle();
  }

  private void setTitle() {
    String title = getString(R.string.settings);
    if (getArguments() != null && getArguments().containsKey(XML_NAME)) {
      String xmlName = getArguments().getString(XML_NAME);
      if (!TextUtils.isEmpty(xmlName)) {
        int stringResourceId = getActivity().getResources()
            .getIdentifier(xmlName.replace("settings_",
                "settings_screen_"), "string", getActivity().getPackageName());
        title = stringResourceId != 0 ? getString(stringResourceId) : title;
      }
    }
    Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
    if (toolbar != null) {
      toolbar.setTitle(title);
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      getActivity().onBackPressed();
    } else {
      LogDelegate.e("Wrong element choosen: " + item.getItemId());
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onResume() {
    super.onResume();

    setTitle();

    // Export notes
    Preference export = findPreference("settings_export_data");
    if (export != null) {
      export.setOnPreferenceClickListener(arg0 -> {

        // Inflate layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_backup_layout, null);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
          // Finds actually saved backups names
          requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, R
                  .string.permission_external_storage, getActivity().findViewById(R.id.crouton_handle), () -> exportBeforeAndroid9
                  (v));
        } else {
          exportAfterAndroid9();
        }

        return false;
      });
    }

    // Export whole database file
    Preference exportRAWDB = findPreference("settings_export_data_raw_db");
    if (exportRAWDB != null) {
      exportRAWDB.setOnPreferenceClickListener(arg0 -> {

        // Inflate layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_backup_layout, null);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
          // Finds actually saved backups names
          requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, R
                  .string.permission_external_storage, getActivity().findViewById(R.id.crouton_handle), () -> exportRAWDB
                  (v));
        } else {
          exportRAWAfterAndroid9();
        }

        return false;
      });
    }

    // Import notes
    Preference importData = findPreference("settings_import_data");
    if (importData != null) {
      importData.setOnPreferenceClickListener(arg0 -> {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
          requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, R
                          .string.permission_external_storage,
                  getActivity().findViewById(R.id.crouton_handle), this::importNotes);
          return false;
        } else {
          importAfterAndroid9();
          return false;
        }
      });

    }

    // Import legacy notes
    Preference importLegacyData = findPreference("settings_import_data_legacy");
    if (importLegacyData != null) {
      importLegacyData.setOnPreferenceClickListener(arg0 -> {

        // Finds actually saved backups names
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
          PermissionsHelper
                  .requestPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE, R
                                  .string.permission_external_storage,
                          getActivity().findViewById(R.id.crouton_handle), () -> new
                                  FolderChooserDialog.Builder(getActivity())
                                  .chooseButton(R.string.md_choose_label)
                                  .show(getActivity()));
          return false;
        } else {
          importRAWDBAfterAndroid9();
          return false;
        }
      });
    }

//		// Autobackup feature integrity check
//		Preference backupIntegrityCheck = findPreference("settings_backup_integrity_check");
//		if (backupIntegrityCheck != null) {
//			backupIntegrityCheck.setOnPreferenceClickListener(arg0 -> {
//				List<LinkedList<DiffMatchPatch.Diff>> errors = BackupHelper.integrityCheck(StorageHelper
//						.getBackupDir(ConstantsBase.AUTO_BACKUP_DIR));
//				if (errors.isEmpty()) {
//					new MaterialDialog.Builder(activity)
//							.content("Everything is ok")
//							.positiveText(R.string.ok)
//							.build().show();
//				} else {
//					DiffMatchPatch diffMatchPatch = new DiffMatchPatch();
//					String content = Observable.from(errors).map(diffs -> diffMatchPatch.diffPrettyHtml(diffs) +
//							"<br/>").toList().toBlocking().first().toString();
//					View v = getActivity().getLayoutInflater().inflate(R.layout.webview, null);
//					((WebView) v.findViewById(R.ID.webview)).loadData(content, "text/html", null);
//					new MaterialDialog.Builder(activity)
//							.customView(v, true)
//							.positiveText(R.string.ok)
//							.negativeText("Copy to clipboard")
//							.onNegative((dialog, which) -> {
//								SystemHelper.copyToClipboard(activity, content);
//								Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show();
//							})
//							.build().show();
//				}
//				return false;
//			});
//		}
//
//		// Autobackup
//		final SwitchPreference enableAutobackup = (SwitchPreference) findPreference("settings_enable_autobackup");
//		if (enableAutobackup != null) {
//			enableAutobackup.setOnPreferenceChangeListener((preference, newValue) -> {
//				if ((Boolean) newValue) {
//					new MaterialDialog.Builder(activity)
//							.content(R.string.settings_enable_automatic_backup_dialog)
//							.positiveText(R.string.confirm)
//							.negativeText(R.string.cancel)
//							.onPositive((dialog, which) -> {
//								PermissionsHelper.requestPermission(getActivity(), Manifest.permission
//										.WRITE_EXTERNAL_STORAGE, R
//										.string.permission_external_storage, activity.findViewById(R.ID
//										.crouton_handle), () -> {
//									BackupHelper.startBackupService(AUTO_BACKUP_DIR);
//									enableAutobackup.setChecked(true);
//								});
//							})
//							.build().show();
//				} else {
//					enableAutobackup.setChecked(false);
//				}
//				return false;
//			});
//		}

    // @pk: springpad will be not supported anymore
//    Preference importFromSpringpad = findPreference("settings_import_from_springpad");
//    if (importFromSpringpad != null) {
//      importFromSpringpad.setOnPreferenceClickListener(arg0 -> {
//        Intent intent;
//        intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/zip");
//        if (!IntentChecker.isAvailable(getActivity(), intent, null)) {
//          Toast.makeText(getActivity(), R.string.feature_not_available_on_this_device,
//              Toast.LENGTH_SHORT).show();
//          return false;
//        }
//        startActivityForResult(intent, SPRINGPAD_IMPORT);
//        return false;
//      });
//    }

//		Preference syncWithDrive = findPreference("settings_backup_drive");
//		importFromSpringpad.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference arg0) {
//				Intent intent;
//				intent = new Intent(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
//				intent.setType("application/zip");
//				if (!IntentChecker.isAvailable(getActivity(), intent, null)) {
//					Crouton.makeText(getActivity(), R.string.feature_not_available_on_this_device,
// ONStyle.ALERT).show();
//					return false;
//				}
//				startActivityForResult(intent, SPRINGPAD_IMPORT);
//				return false;
//			}
//		});

    // Swiping action
    final SwitchPreference swipeToTrash = findPreference("settings_swipe_to_trash");
    if (swipeToTrash != null) {
      if (Prefs.getBoolean("settings_swipe_to_trash", false)) {
        swipeToTrash.setChecked(true);
        swipeToTrash
            .setSummary(getResources().getString(R.string.settings_swipe_to_trash_summary_2));
      } else {
        swipeToTrash.setChecked(false);
        swipeToTrash
            .setSummary(getResources().getString(R.string.settings_swipe_to_trash_summary_1));
      }
      swipeToTrash.setOnPreferenceChangeListener((preference, newValue) -> {
        if ((Boolean) newValue) {
          swipeToTrash
              .setSummary(getResources().getString(R.string.settings_swipe_to_trash_summary_2));
        } else {
          swipeToTrash
              .setSummary(getResources().getString(R.string.settings_swipe_to_trash_summary_1));
        }
        return true;
      });
    }

    // Show uncategorized notes in menu
    final SwitchPreference showUncategorized = findPreference(PREF_SHOW_UNCATEGORIZED);
    if (showUncategorized != null) {
      showUncategorized.setOnPreferenceChangeListener((preference, newValue) -> true);
    }

    // Show Automatically adds location to new notes
    final SwitchPreference autoLocation = findPreference(PREF_AUTO_LOCATION);
    if (autoLocation != null) {
      autoLocation.setOnPreferenceChangeListener((preference, newValue) -> true);
    }

    // Maximum video attachment size
    final EditTextPreference maxVideoSize = findPreference("settings_max_video_size");
    if (maxVideoSize != null) {
      maxVideoSize.setSummary(getString(R.string.settings_max_video_size_summary) + ": "
          + Prefs.getString("settings_max_video_size", getString(R.string.not_set)));
      maxVideoSize.setOnPreferenceChangeListener((preference, newValue) -> {
        maxVideoSize
            .setSummary(getString(R.string.settings_max_video_size_summary) + ": " + newValue);
        Prefs.edit().putString("settings_max_video_size", newValue.toString()).apply();
        return false;
      });
    }

    // Set notes' protection password
    Preference password = findPreference("settings_password");
    if (password != null) {
      password.setOnPreferenceClickListener(preference -> {
        Intent passwordIntent = new Intent(getActivity(), PasswordActivity.class);
        startActivity(passwordIntent);
        return false;
      });
    }

    // Use password to grant application access
    final SwitchPreference passwordAccess = findPreference("settings_password_access");
    if (passwordAccess != null) {
      if (Prefs.getString(PREF_PASSWORD, null) == null) {
        passwordAccess.setEnabled(false);
        passwordAccess.setChecked(false);
      } else {
        passwordAccess.setEnabled(true);
      }
      passwordAccess.setOnPreferenceChangeListener((preference, newValue) -> {
        PasswordHelper.requestPassword(getActivity(), passwordConfirmed -> {
          if (passwordConfirmed.equals(PasswordValidator.Result.SUCCEED)) {
            passwordAccess.setChecked((Boolean) newValue);
          }
        });
        return true;
      });
    }

    // Languages
    ListPreference lang = findPreference("settings_language");
    if (lang != null) {
      String languageName = getResources().getConfiguration().locale.getDisplayName();
      lang.setSummary(languageName.substring(0, 1).toUpperCase(getResources().getConfiguration().locale)
          + languageName.substring(1));
      lang.setOnPreferenceChangeListener((preference, value) -> {
        LanguageHelper.updateLanguage(getActivity(), value.toString());
        SystemHelper.restartApp(getActivity().getApplicationContext(), MainActivity.class);
        return false;
      });
    }

    // Text size
    final ListPreference textSize = findPreference("settings_text_size");
    if (textSize != null) {
      int textSizeIndex = textSize.findIndexOfValue(Prefs.getString("settings_text_size", "default"));
      String textSizeString = getResources().getStringArray(R.array.text_size)[textSizeIndex];
      textSize.setSummary(textSizeString);
      textSize.setOnPreferenceChangeListener((preference, newValue) -> {
        int textSizeIndex1 = textSize.findIndexOfValue(newValue.toString());
        String checklistString = getResources().getStringArray(R.array.text_size)[textSizeIndex1];
        textSize.setSummary(checklistString);
        Prefs.edit().putString("settings_text_size", newValue.toString()).apply();
        textSize.setValueIndex(textSizeIndex1);
        return false;
      });
    }


    // Set format of JKS
    Preference jksFormat = findPreference("settings_jks_format");
    if (jksFormat != null) {
      jksFormat.setOnPreferenceClickListener(preference -> {
        Intent jksFormatIntent = new Intent(getActivity(), JKSFormatActivity.class);
        startActivity(jksFormatIntent);
        return false;
      });
    }

    // Application's colors
    final ListPreference colorsApp = findPreference("settings_colors_app");
    if (colorsApp != null) {
      int colorsAppIndex = colorsApp.findIndexOfValue(Prefs.getString("settings_colors_app",
          PREF_COLORS_APP_DEFAULT));
      String colorsAppString = getResources().getStringArray(R.array.colors_app)[colorsAppIndex];
      colorsApp.setSummary(colorsAppString);
      colorsApp.setOnPreferenceChangeListener((preference, newValue) -> {
        int colorsAppIndex1 = colorsApp.findIndexOfValue(newValue.toString());
        String colorsAppString1 = getResources()
            .getStringArray(R.array.colors_app)[colorsAppIndex1];
        colorsApp.setSummary(colorsAppString1);
        Prefs.edit().putString("settings_colors_app", newValue.toString()).apply();
        colorsApp.setValueIndex(colorsAppIndex1);
        return false;
      });
    }

    // Application's theme
    final ListPreference themeApp = findPreference("settings_theme_app");
    if (themeApp != null) {
      int colorsAppIndex = themeApp.findIndexOfValue(Prefs.getString(PREF_HTML_COLOR_SCHEME, PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT));
      String colorsAppString = getResources().getStringArray(R.array.html_color_schemes)[colorsAppIndex];
      themeApp.setSummary(colorsAppString);
      themeApp.setOnPreferenceChangeListener((preference, newValue) -> {
        int colorsAppIndex1 = themeApp.findIndexOfValue(newValue.toString());
        String colorsAppString1 = getResources().getStringArray(R.array.html_color_schemes)[colorsAppIndex1];
        themeApp.setSummary(colorsAppString1);
        Prefs.edit().putString(PREF_HTML_COLOR_SCHEME, newValue.toString()).apply();
        themeApp.setValueIndex(colorsAppIndex1);
        //getActivity().recreate(); small bug, after recreating activity we need tap two times on back arrow in action bar
        return false;
      });
    }

    // Checklists
    final ListPreference checklist = findPreference("settings_checked_items_behavior");
    if (checklist != null) {
      int checklistIndex = checklist
          .findIndexOfValue(Prefs.getString("settings_checked_items_behavior", "0"));
      String checklistString = getResources()
          .getStringArray(R.array.checked_items_behavior)[checklistIndex];
      checklist.setSummary(checklistString);
      checklist.setOnPreferenceChangeListener((preference, newValue) -> {
        int checklistIndex1 = checklist.findIndexOfValue(newValue.toString());
        String checklistString1 = getResources().getStringArray(R.array.checked_items_behavior)
            [checklistIndex1];
        checklist.setSummary(checklistString1);
        Prefs.edit().putString("settings_checked_items_behavior", newValue.toString()).apply();
        checklist.setValueIndex(checklistIndex1);
        return false;
      });
    }

    // Widget's colors
    final ListPreference colorsWidget = findPreference("settings_colors_widget");
    if (colorsWidget != null) {
      int colorsWidgetIndex = colorsWidget
          .findIndexOfValue(Prefs.getString("settings_colors_widget",
              PREF_COLORS_APP_DEFAULT));
      String colorsWidgetString = getResources()
          .getStringArray(R.array.colors_widget)[colorsWidgetIndex];
      colorsWidget.setSummary(colorsWidgetString);
      colorsWidget.setOnPreferenceChangeListener((preference, newValue) -> {
        int colorsWidgetIndex1 = colorsWidget.findIndexOfValue(newValue.toString());
        String colorsWidgetString1 = getResources()
            .getStringArray(R.array.colors_widget)[colorsWidgetIndex1];
        colorsWidget.setSummary(colorsWidgetString1);
        Prefs.edit().putString("settings_colors_widget", newValue.toString()).apply();
        colorsWidget.setValueIndex(colorsWidgetIndex1);
        return false;
      });
    }

    // Ringtone selection
    final Preference ringtone = findPreference("settings_notification_ringtone");
    if (ringtone != null) {
      ringtone.setOnPreferenceClickListener(arg0 -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          new NotificationsHelper(getContext()).updateNotificationChannelsSound();
        } else {
          Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
          intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
          intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
          intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
          intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, DEFAULT_NOTIFICATION_URI);

          String existingValue = Prefs.getString("settings_notification_ringtone", null);
          if (existingValue != null) {
            if (existingValue.length() == 0) {
              // Select "Silent"
              intent.putExtra(EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
            } else {
              intent.putExtra(EXTRA_RINGTONE_EXISTING_URI, Uri.parse(existingValue));
            }
          } else {
            // No ringtone has been selected, set to the default
            intent.putExtra(EXTRA_RINGTONE_EXISTING_URI, DEFAULT_NOTIFICATION_URI);
          }

          startActivityForResult(intent, RINGTONE_REQUEST_CODE);
        }

        return false;
      });
    }

    // Notification snooze delay
    final EditTextPreference snoozeDelay = findPreference("settings_notification_snooze_delay");
    if (snoozeDelay != null) {
      String snooze = Prefs.getString("settings_notification_snooze_delay", PREF_SNOOZE_DEFAULT);
      snooze = TextUtils.isEmpty(snooze) ? PREF_SNOOZE_DEFAULT : snooze;
      snoozeDelay.setSummary(snooze + " " + getString(R.string.minutes));
      snoozeDelay.setOnPreferenceChangeListener((preference, newValue) -> {
        String snoozeUpdated = TextUtils.isEmpty(String.valueOf(newValue)) ? PREF_SNOOZE_DEFAULT
            : String.valueOf(newValue);
        snoozeDelay.setSummary(snoozeUpdated + " " + getString(R.string.minutes));
        Prefs.edit().putString("settings_notification_snooze_delay", snoozeUpdated).apply();
        return false;
      });
    }

    // NotificationServiceListener shortcut
    final Preference norificationServiceListenerPreference = findPreference(
        "settings_notification_service_listener");
    if (norificationServiceListenerPreference != null) {
      getPreferenceScreen().removePreference(norificationServiceListenerPreference);
    }

    // Changelog
    Preference changelog = findPreference("settings_changelog");
    if (changelog != null) {
      changelog.setOnPreferenceClickListener(arg0 -> {

        ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackEvent(AnalyticsHelper.CATEGORIES.SETTING,
            "settings_changelog");

        new MaterialDialog.Builder(getContext())
            .customView(R.layout.activity_changelog, false)
            .positiveText(R.string.ok)
            .build().show();
        return false;
      });
      try {
        changelog.setSummary(AppVersionHelper.getCurrentAppVersionName(getActivity()));
      } catch (NameNotFoundException e) {
        LogDelegate.e("Error retrieving version", e);
      }
    }

    // Settings reset
    Preference resetData = findPreference("reset_all_data");
    if (resetData != null) {
      resetData.setOnPreferenceClickListener(arg0 -> {

        new MaterialDialog.Builder(getContext())
            .content(R.string.reset_all_data_confirmation)
            .positiveText(R.string.confirm)
            .onPositive((dialog, which) -> {
              Prefs.edit().clear().commit();
              DbHelper.closeDB();
              File db_user = getActivity().getDatabasePath(DB_USER_DATA);
              StorageHelper.delete(getActivity(), db_user.getAbsolutePath());
              File db_embedded = getActivity().getDatabasePath(DB_ATTACHED);
              StorageHelper.delete(getActivity(), db_embedded.getAbsolutePath());
              File attachmentsDir = StorageHelper.getAttachmentDir();
              StorageHelper.delete(getActivity(), attachmentsDir.getAbsolutePath());
              File cacheDir = StorageHelper.getCacheDir(getActivity());
              StorageHelper.delete(getActivity(), cacheDir.getAbsolutePath());
              SystemHelper.restartApp(getActivity().getApplicationContext(), MainActivity.class);
            }).build().show();

        return false;
      });
    }

    // Logs on files activation
    final SwitchPreference enableFileLogging = findPreference(PREF_ENABLE_FILE_LOGGING);
    if (enableFileLogging != null) {
      enableFileLogging.setOnPreferenceChangeListener((preference, newValue) -> {
        if ((Boolean) newValue) {
          requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, R
                      .string.permission_external_storage,
                  getActivity().findViewById(R.id.crouton_handle),
                  () -> enableFileLogging.setChecked(true));
        } else {
          enableFileLogging.setChecked(false);
        }
        return false;
      });
    }

    // Instructions
    Preference instructions = findPreference("settings_tour_show_again");
    if (instructions != null) {
      instructions.setOnPreferenceClickListener(arg0 -> {
        new MaterialDialog.Builder(getActivity())
            .content(getString(R.string.settings_tour_show_again_summary) + "?")
            .positiveText(R.string.confirm)
            .onPositive((dialog, which) -> {
              ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackEvent(
                  AnalyticsHelper.CATEGORIES.SETTING, "settings_tour_show_again");
              Prefs.edit().putBoolean(PREF_TOUR_COMPLETE, false).commit();
              SystemHelper.restartApp(getActivity().getApplicationContext(), MainActivity.class);
            }).build().show();
        return false;
      });
    }
  }


  private void importNotes() {
    String[] backupsArray = StorageHelper.getOrCreateExternalStoragePublicDir().list();

    if (ArrayUtils.isEmpty(backupsArray)) {
      ((SettingsActivity) getActivity()).showMessage(R.string.no_backups_available, ONStyle.WARN);
    } else {
      final List<String> backups = asList(backupsArray);
      reverse(backups);

      MaterialAlertDialogBuilder importDialog = new MaterialAlertDialogBuilder(getActivity())
          .setTitle(R.string.settings_import)
          .setSingleChoiceItems(backupsArray, -1, (dialog, position) -> {
          })
          .setPositiveButton(R.string.data_import_message, (dialog, which) -> {
            int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            File backupDir = StorageHelper.getOrCreateBackupDir(backups.get(position));
            long size = StorageHelper.getSize(backupDir) / 1024;
            String sizeString = size > 1024 ? size / 1024 + "Mb" : size + "Kb";

            // Check preference presence
            String prefName = StorageHelper.getSharedPreferencesFile(getActivity()).getName();
            boolean hasPreferences = (new File(backupDir, prefName)).exists();

            String message = String.format("%s (%s %s)", backups.get(position), sizeString,
                hasPreferences ? getString(R.string.settings_included) : "");

            new MaterialAlertDialogBuilder(getActivity())
                .setTitle(R.string.confirm_restoring_backup)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, (dialog1, which1) -> {
                  ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackEvent(
                      AnalyticsHelper.CATEGORIES.SETTING,
                      "settings_import_data");

                  // An IntentService will be launched to accomplish the import task
                  Intent service = new Intent(getActivity(),
                      DataBackupIntentService.class);
                  service.setAction(DataBackupIntentService.ACTION_DATA_IMPORT);
                  service.putExtra(DataBackupIntentService.INTENT_BACKUP_NAME,
                      backups.get(position));
                  getActivity().startService(service);
                }).show();
          })
          .setNegativeButton(R.string.delete, (dialog, which) -> {
            int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            File backupDir = StorageHelper.getOrCreateBackupDir(backups.get(position));
            long size = StorageHelper.getSize(backupDir) / 1024;
            String sizeString = size > 1024 ? size / 1024 + "Mb" : size + "Kb";

            new MaterialDialog.Builder(getActivity())
                .title(R.string.confirm_removing_backup)
                .content(backups.get(position) + "" + " (" + sizeString + ")")
                .positiveText(R.string.confirm)
                .onPositive((dialog12, which1) -> {
                  Intent service = new Intent(getActivity(),
                      DataBackupIntentService.class);
                  service.setAction(DataBackupIntentService.ACTION_DATA_DELETE);
                  service.putExtra(DataBackupIntentService.INTENT_BACKUP_NAME,
                      backups.get(position));
                  getActivity().startService(service);
                }).build().show();
          });

      importDialog.show();
    }
  }


  private void importAfterAndroid9()
  {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
    startActivityForResult(intent, CHOOSE_DIRECTORY_FOR_IMPORT_REQUEST_CODE);
  }


  private void importRAWDBAfterAndroid9()
  {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
    startActivityForResult(intent, CHOOSE_DIRECTORY_FOR_RAW_IMPORT_REQUEST_CODE);
  }

  private void exportAfterAndroid9()
  {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
    startActivityForResult(intent, CHOOSE_DIRECTORY_REQUEST_CODE);
  }

  private void exportRAWAfterAndroid9()
  {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
    startActivityForResult(intent, CHOOSE_RAW_DIRECTORY_REQUEST_CODE);
  }


  private void exportBeforeAndroid9(View v) {
    String[] backupsArray = StorageHelper.getOrCreateExternalStoragePublicDir().list();
    final List<String> backups = ArrayUtils.isEmpty(backupsArray) ? emptyList() : asList(backupsArray);

    // Sets default export file name
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_EXPORT);
    String fileName = sdf.format(Calendar.getInstance().getTime());
    final EditText fileNameEditText = v.findViewById(R.id.export_file_name);
    final TextView backupExistingTextView = v.findViewById(R.id.backup_existing);
    fileNameEditText.setHint(fileName);
    fileNameEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // Nothing to do
      }


      @Override
      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // Nothing to do
      }


      @Override
      public void afterTextChanged(Editable arg0) {

        if (backups.contains(arg0.toString())) {
          backupExistingTextView.setText(R.string.backup_existing);
        } else {
          backupExistingTextView.setText("");
        }
      }
    });

    new MaterialDialog.Builder(getActivity())
        .title(R.string.data_export_message)
        .customView(v, false)
        .positiveText(R.string.confirm)
        .onPositive((dialog, which) -> {
          ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackEvent(
              AnalyticsHelper.CATEGORIES.SETTING, "settings_export_data");
          String backupName = TextUtils.isEmpty(fileNameEditText.getText().toString()) ?
              fileNameEditText.getHint().toString() : fileNameEditText.getText().toString();
          BackupHelper.startBackupService(backupName);
        }).build().show();

  }


  private void exportRAWDB(View v) {
    final List<String> backups = asList(StorageHelper.getOrCreateExternalStoragePublicDir().list());

    // Sets default export file name
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_EXPORT);
    String fileName = sdf.format(Calendar.getInstance().getTime());
    final EditText fileNameEditText = v.findViewById(R.id.export_file_name);
    final CheckBox cbxSettings = v.findViewById(R.id.backup_include_settings);
    cbxSettings.setVisibility(View.GONE);
    final TextView backupExistingTextView = v.findViewById(R.id.backup_existing);
    fileNameEditText.setHint(fileName);
    fileNameEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // Nothing to do
      }

      @Override
      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // Nothing to do
      }

      @Override
      public void afterTextChanged(Editable arg0) {

        if (backups.contains(arg0.toString())) {
          backupExistingTextView.setText(R.string.backup_existing);
        } else {
          backupExistingTextView.setText("");
        }
      }
    });

    new MaterialDialog.Builder(getActivity())
            .title(R.string.data_export_message)
            .customView(v, false)
            .positiveText(R.string.confirm)
            .onPositive((dialog, which) -> {
              ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackEvent(
                      AnalyticsHelper.CATEGORIES.SETTING, "settings_export_data");
              String backupName = TextUtils.isEmpty(fileNameEditText.getText().toString()) ?
                      fileNameEditText.getHint().toString() : fileNameEditText.getText().toString();
              BackupHelper.startRAWBackupService(backupName);
            }).build().show();
  }


  @Override
  public void onStart () {
    ((Knizka) getActivity().getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());
    super.onStart();
  }

  @Override
  public void onActivityResult (int requestCode, int resultCode, Intent intent) {
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case SPRINGPAD_IMPORT:
          Uri filesUri = intent.getData();
          String path = FileHelper.getPath(getActivity(), filesUri);
          // An IntentService will be launched to accomplish the import task
          Intent service = new Intent(getActivity(), DataBackupIntentService.class);
          service.setAction(SpringImportHelper.ACTION_DATA_IMPORT_SPRINGPAD);
          service.putExtra(SpringImportHelper.EXTRA_SPRINGPAD_BACKUP, path);
          getActivity().startService(service);
          break;

        case RINGTONE_REQUEST_CODE:
          Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
          String notificationSound = uri == null ? null : uri.toString();
          Prefs.edit().putString("settings_notification_ringtone", notificationSound).apply();
          break;

        case CHOOSE_DIRECTORY_REQUEST_CODE:
          Uri treePath = intent.getData();
          if (treePath != null)
          {
            BackupHelper.startBackupServiceScopedStorage(treePath);
          } else {
            LogDelegate.e("No Uri data provided");
          }
          break;

        case CHOOSE_RAW_DIRECTORY_REQUEST_CODE:
          Uri treePathRaw = intent.getData();
          if (treePathRaw != null)
          {
            BackupHelper.startRAWBackupServiceScopedStorage(treePathRaw);
          } else {
            LogDelegate.e("No Uri data provided");
          }
          break;

        case CHOOSE_DIRECTORY_FOR_IMPORT_REQUEST_CODE:
          Uri treePathImport = intent.getData();
          if (treePathImport != null)
          {
            BackupHelper.startImportServiceScopedStorage(treePathImport);
          } else {
            LogDelegate.e("No Uri data provided");
          }
          break;

        case CHOOSE_DIRECTORY_FOR_RAW_IMPORT_REQUEST_CODE:
          Uri treePathRAWImport = intent.getData();
          if (treePathRAWImport != null)
          {
            BackupHelper.startRAWImportServiceScopedStorage(treePathRAWImport);
          } else {
            LogDelegate.e("No Uri data provided");
          }
          break;


        default:
          LogDelegate.e("Wrong element choosen: " + requestCode);
      }
    }
  }
}
