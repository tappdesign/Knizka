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
package pk.tappdesign.knizka.utils;

import pk.tappdesign.knizka.R;

public interface ConstantsBase {

	String TAG_BASE = "Knizka";
	String EXTERNAL_STORAGE_FOLDER_BASE = "Knizka";
	String PACKAGE_BASE = "pk.tappdesign.knizka";

	String APP_STORAGE_DIRECTORY_SB_SYNC = "db_sync";

	String FACEBOOK_COMMUNITY = "https://www.facebook.com/Knizka/";
	String HOME_WEB_PAGE = "https://sites.google.com/view/tappdesignstudio/home";

	long PACKAGE_UNDEFINED = -1;
	long PACKAGE_SYSTEM = 0;
	long PACKAGE_USER_ADDED = 1;
	long PACKAGE_USER_INTENT = 2;

	long PRAYER_MERGED_NO = 0;
	long PRAYER_MERGED_YES = 1;
	long PRAYER_MERGED_LINKED_SET = 2;

	int LINKED_NOTE_TYPE_REFERENCED_NOTE = 0;
	int LINKED_NOTE_TYPE_RANDOM_CATEGORY = 1;



	String INTENT_EXTRA_MAX_PAGES_IN_BROWSER = "maxPagesInViewPager";
	String INTENT_EXTRA_NOTE_IDS_FOR_VIEWPAGER = "NoteIDsForViewPager";
	String INTENT_EXTRA_CATEGORY_TITLE_FOR_BROWSER = "CategoryTitleForViewPager";
	String INTENT_EXTRA_LIST_VIEW_POSITION_OFFSET_FOR_VIEWPAGER = "ListViewPositionOffsetForViewPager";

	String MUSIC_LIBRARY_OSMD = "osmd";
	String MUSIC_LIBRARY_ABCJS = "abcjs";

	String JKS_SORTING_TYPE_NUMBER = "by_number";
	String JKS_SORTING_TYPE_NAME = "by_name";

	int MIN_X_MOVING_OFFSET_FOR_TEXT_BROWSING = 30;
	int ACTIVATE_TEXT_BROWSING_DISPLAY_RATIO = 3;  //   1/4

	String HTML_TEXT_BODY_CLASS = "<div class=\"TextBody\">";
	String HTML_DIV_END_TAG = "</div>";
	String HTML_TEXT_TITLE_CLASS = "<div class=\"TextTitle\">";
	String HTML_TEXT_SUB_TITLE_CLASS = "<div class=\"TextSubTitle\">";
	String HTML_DIV_MUSIC_SCORE_CONTAINER = "<div id=\"MusicScoreContainer\"></div>";

	// Used for updates retrieval
	long UPDATE_MIN_FREQUENCY = 24L * 60L * 60L * 1000L; // 1 day
	String DRIVE_FOLDER_LAST_BUILD = "https://goo.gl/gB55RE";

	// Notes swipe
	int SWIPE_MARGIN = 30;
	int SWIPE_OFFSET = 100;

	// Floating action button
	int FAB_ANIMATION_TIME = 250;

	// Notes content masking
	String MASK_CHAR = "*";

	int THUMBNAIL_SIZE = 300;

	String DATE_FORMAT_SORTABLE = "yyyyMMdd_HHmmss_SSS";
	String DATE_FORMAT_SORTABLE_OLD = "yyyyMMddHHmmss";
	String DATE_FORMAT_EXPORT = "yyyy.MM.dd-HH.mm";

	String INTENT_KEY = "note_id";
	String INTENT_NOTE = "note";
	String GALLERY_TITLE = "gallery_title";
	String GALLERY_CLICKED_IMAGE = "gallery_clicked_image";
	String GALLERY_IMAGES = "gallery_images";
	String INTENT_CATEGORY = "category";
	String INTENT_GOOGLE_NOW = "com.google.android.gm.action.AUTO_SEND";
	String INTENT_WIDGET = "widget_id";
	String INTENT_UPDATE_DASHCLOCK = "update_dashclock";

	// Custom intent actions
	String ACTION_START_APP = "action_start_app";
	String ACTION_RESTART_APP = "action_restart_app";
	String ACTION_PINNED = "action_pinned";
	String ACTION_DISMISS = "action_dismiss";
	String ACTION_SNOOZE = "action_snooze";
	String ACTION_POSTPONE = "action_postpone";
	String ACTION_SHORTCUT = "action_shortcut";
	String ACTION_WIDGET = "action_widget";
	String ACTION_WIDGET_TAKE_PHOTO = "action_widget_take_photo";
	String ACTION_WIDGET_SHOW_LIST = "action_widget_show_list";
	String ACTION_SHORTCUT_WIDGET = "action_shortcut_widget";
	String ACTION_NOTIFICATION_CLICK = "action_notification_click";
	String ACTION_MERGE = "action_merge";
	String ACTION_FAB_TAKE_PHOTO = "action_fab_take_photo";
	String ACTION_PICKED_FROM_BROWSE_TEXTS = "action_picked_from_browse_texts";
	/**
	 * Used to quickly add a note, save, and perform backPress (eg. Tasker+Pushbullet) *
	 */
	String ACTION_SEND_AND_EXIT = "action_send_and_exit";
	String ACTION_SEARCH_UNCOMPLETE_CHECKLISTS = "action_search_uncomplete_checklists";

	String LAYOUT_JKS_PREFIX = "layout_jks_";

	String PREF_LANG = "settings_language";
	String PREF_LAST_UPDATE_CHECK = "last_update_check";
	String PREF_NAVIGATION = "navigation";
	String PREF_SORTING_COLUMN = "sorting_column";
	String PREF_PASSWORD = "password";
	String PREF_PASSWORD_QUESTION = "password_question";
	String PREF_PASSWORD_ANSWER = "password_answer";
	String PREF_KEEP_CHECKED = "keep_checked";
	String PREF_KEEP_CHECKMARKS = "show_checkmarks";
	String PREF_EXPANDED_VIEW = "expanded_view";
	String PREF_COLORS_APP_DEFAULT = "strip";
	String PREF_WIDGET_PREFIX = "widget_";
	String PREF_SHOW_UNCATEGORIZED = "settings_show_uncategorized";
	String PREF_AUTO_LOCATION = "settings_auto_location";
	String PREF_FILTER_PAST_REMINDERS = "settings_filter_past_reminders";
	String PREF_FILTER_ARCHIVED_IN_CATEGORIES = "settings_filter_archived_in_categories";
	String PREF_DYNAMIC_MENU = "settings_dynamic_menu";
	String PREF_NAVIGATION_SHOW_JKS_CATEGORIES = "settings_navdrawer_show_jks_categories";
	boolean PREF_NAVIGATION_SHOW_JKS_CATEGORIES_DEFAULT = false;
	String PREF_NAVIGATION_JKS_CATEGORY_ID = "jks_category_id";
	int PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT = 0;
	String PREF_JKS_SORTING_TYPE = "jks_sorting_type";
	String PREF_JKS_SORTING_TYPE_DEFAULT = JKS_SORTING_TYPE_NUMBER;
	String PREF_CURRENT_APP_VERSION = "settings_current_app_version";
	String PREF_FAB_EXPANSION_BEHAVIOR = "settings_fab_expansion_behavior";
	String PREF_ATTACHMENTS_ON_BOTTOM = "settings_attachments_on_bottom";
	String PREF_JKS_SHOW_MUSIC_SCORE = "settings_jks_show_music_score";
	boolean PREF_JKS_SHOW_MUSIC_SCORE_DEFAULT = true;
	String PREF_BOLD_FONT_IN_TEXTS = "settings_bold_font_in_texts";
	boolean PREF_BOLD_FONT_IN_TEXTS_DEFAULT = false;
	String PREF_KEEP_SCREEN_ON = "settings_keep_screen_on";
	boolean PREF_KEEP_SCREEN_ON_DEFAULT = false;
	String PREF_SHOW_FULLSCREEN = "settings_show_full_screen";
	boolean PREF_SHOW_FULLSCREEN_DEFAULT = false;
	String PREF_SNOOZE_DEFAULT = "10";
	String PREF_TOUR_COMPLETE = "pref_tour_complete";
	String PREF_ENABLE_SWIPE = "settings_enable_swipe";
	boolean PREF_ENABLE_SWIPE_DEFAULT = false;
	String PREF_SEND_ANALYTICS = "settings_send_analytics";
	String PREF_PRETTIFIED_DATES = "settings_prettified_dates";
	String PREF_ENABLE_AUTOBACKUP = "settings_enable_autobackup";
	String PREF_ENABLE_FILE_LOGGING = "settings_enable_file_logging";
	String PREF_DB_EMBEDDED_VERSION = "db_embedded_version";
	String PREF_WEBVIEW_ZOOM = "webview_zoom";
	int PREF_WEBVIEW_ZOOM_DEFAULT = 100;
	String PREF_HTML_COLOR_SCHEME = "html_color_scheme";
	String PREF_HTML_COLOR_SCHEME_DEFAULT = "";

	String PREF_HTML_COLOR_SCHEME_VALUE_BRIGHT = "bright";
	String PREF_HTML_COLOR_SCHEME_VALUE_DARK = "dark";
	String PREF_LAYOUT_JKS_CSS = "layout_jks_css";
	String PREF_LAYOUT_JKS_CSS_DEFAULT = LAYOUT_JKS_PREFIX + "01.css";

	String PREF_MUSIC_SCORE_LIBRARY = "music_score_library";
	String PREF_MUSIC_SCORE_LIBRARY_DEFAULT = MUSIC_LIBRARY_ABCJS;


	String MIME_TYPE_IMAGE = "image/jpeg";
	String MIME_TYPE_AUDIO = "audio/amr";
	String MIME_TYPE_VIDEO = "video/mp4";
	String MIME_TYPE_SKETCH = "image/png";
	String MIME_TYPE_FILES = "file/*";

	String MIME_TYPE_IMAGE_EXT = ".jpeg";
	String MIME_TYPE_AUDIO_EXT = ".amr";
	String MIME_TYPE_VIDEO_EXT = ".mp4";
	String MIME_TYPE_SKETCH_EXT = ".png";
	String MIME_TYPE_CONTACT_EXT = ".vcf";

	String TIMESTAMP_UNIX_EPOCH = "0";
	String TIMESTAMP_UNIX_EPOCH_FAR = "18464193800000";

	String TAG_SPECIAL_CHARS_TO_REMOVE = "[<>\\[\\],-\\.\\(\\)\\[\\]\\{\\}\\!\\?]";

	int MENU_SORT_GROUP_ID = 11998811;

	String MERGED_NOTES_SEPARATOR = "<HR>";
	String PROPERTIES_PARAMS_SEPARATOR = ",";

	String AUTO_BACKUP_DIR = "_autobackup";

}
