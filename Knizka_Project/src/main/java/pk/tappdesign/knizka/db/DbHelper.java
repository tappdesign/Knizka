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
package pk.tappdesign.knizka.db;

import static pk.tappdesign.knizka.db.DBConst.DB_VERSION_USER_DATA;
import static pk.tappdesign.knizka.utils.ConstantsBase.JKS_SORTING_TYPE_NAME;
import static pk.tappdesign.knizka.utils.ConstantsBase.JKS_SORTING_TYPE_NUMBER;
import static pk.tappdesign.knizka.utils.ConstantsBase.LINKED_NOTE_TYPE_RANDOM_CATEGORY;
import static pk.tappdesign.knizka.utils.ConstantsBase.PACKAGE_USER_ADDED;
import static pk.tappdesign.knizka.utils.ConstantsBase.PACKAGE_USER_INTENT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PRAYER_MERGED_LINKED_SET;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_FILTER_ARCHIVED_IN_CATEGORIES;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_FILTER_PAST_REMINDERS;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_JKS_SORTING_TYPE;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_JKS_SORTING_TYPE_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_NAVIGATION_JKS_CATEGORY_ID;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_PASSWORD;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_AUDIO;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_FILES;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_IMAGE;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_SKETCH;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_VIDEO;
import static pk.tappdesign.knizka.utils.PKStringUtils.stripHTML;
import static pk.tappdesign.knizka.utils.PKStringUtils.replaceLineBreak;
import static pk.tappdesign.knizka.utils.PKStringUtils.stripToMaxChars;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.pixplicity.easyprefs.library.Prefs;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.List;
import java.util.regex.Pattern;

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.async.upgrade.UpgradeProcessor;
import pk.tappdesign.knizka.exceptions.DatabaseException;
import pk.tappdesign.knizka.helpers.BackupHelper;
import pk.tappdesign.knizka.helpers.LanguageHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.models.Attachment;
import pk.tappdesign.knizka.models.Category;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.NoteLink;
import pk.tappdesign.knizka.models.Stats;
import pk.tappdesign.knizka.models.Tag;
import pk.tappdesign.knizka.utils.AssetUtils;
import pk.tappdesign.knizka.utils.ConstantsBase;
import pk.tappdesign.knizka.utils.Navigation;
import pk.tappdesign.knizka.utils.Security;
import pk.tappdesign.knizka.utils.StorageHelper;
import pk.tappdesign.knizka.utils.TagsHelper;


public class DbHelper extends SQLiteOpenHelper {

  // Sql query file directory
  private static final String SQL_DIR = "sql";

  // Tables definitions
  public static final String ATTACHED_DB = "USER_DB";
  public static final String MAIN_DB = "main";

  public static final String TBL_PRAYERS = "prayers";
  public static final String TBL_CATEGORIES = "categories";
  public static final String TBL_USER_FLAGS = "user_flags";
  public static final String TBL_LAST_SHOWN = "last_shown";
  public static final String TBL_CATEGORIES_FLAG = "category_flags";
  public static final String TBL_ATTACHMENTS = "attachments";
  public static final String TBL_PRAYER_LINKED_SET = "prayer_linked_set";

  public static final String ATTCH_PRAYERS = ATTACHED_DB + "." + TBL_PRAYERS;
  public static final String ATTCH_CATEGORIES = ATTACHED_DB + "." + TBL_CATEGORIES;
  public static final String ATTCH_ATTACHMENTS = ATTACHED_DB + "." + TBL_ATTACHMENTS;

  public static final String MAIN_PRAYERS = MAIN_DB + "." + TBL_PRAYERS;
  public static final String MAIN_CATEGORIES = MAIN_DB + "." + TBL_CATEGORIES;
  public static final String MAIN_ATTACHMENTS = MAIN_DB + "." + TBL_ATTACHMENTS;

  // columns for ATTACHMENTS_TABLE
  public static final String COL_ATTCH_ID = "attachment_id";
  public static final String COL_ATTCH_URI = "uri";
  public static final String COL_ATTCH_NAME = "name";
  public static final String COL_ATTCH_SIZE = "size";
  public static final String COL_ATTCH_LENGTH = "length";
  public static final String COL_ATTCH_MIME_TYPE = "mime_type";
  public static final String COL_ATTCH_PRAYER_HANDLE_ID_REF = "prayer_handle_id_ref";

  public static final String COL_HANDLE_ID = "handle_id";
  public static final String COL_LAST_MODIFICATION = "last_modification";
  public static final String COL_TITLE = "title";
  public static final String COL_TEXT_NUMBER = "text_number";
  public static final String COL_INSIGHT = "insight";
  public static final String COL_CONTENT = "content";
  public static final String COL_CATEGORY_ID_REF = "category_id_ref";
  public static final String COL_CATEGORY_ID = "category_id";
  public static final String COL_PACKAGE_ID = "package_id";
  public static final String COL_PRAYER_MERGED = "prayer_set_flag";
  public static final String COL_TAG_LIST = "tag_list";

  public static final String COL_IS_FAVORITE = "uf_is_favorite";
  public static final String COL_IS_TRASHED = "uf_is_trashed";
  public static final String COL_IS_ARCHIVED = "uf_is_archived";
  public static final String COL_IS_ERASED = "uf_is_erased";
  public static final String COL_IS_CATEGORY = "uf_category";

  public static final String COL_IS_UF_ALARM = "uf_alarm";
  public static final String COL_IS_UF_REMINDER_FIRED = "uf_reminder_fired";
  public static final String COL_IS_UF_RECURRENCE_RULE = "uf_recurrence_rule";

  public static final String COL_TAG_LIST_USER = "uf_tag_list";

  public static final String COL_CATEGORY_NAME = "cat_name";
  public static final String COL_CATEGORY_DESCRIPTION = "cat_description";
  public static final String COL_CATEGORY_COLOR = "cat_color";

  public static final String COL_CATEGORY_ID_ALIAS = "category_alias";
  public static final String COL_CATEGORY_ID_SEl = COL_CATEGORY_ID_REF + " AS " + COL_CATEGORY_ID_ALIAS;
  public static final String COL_ATT_CATEGORY = TBL_CATEGORIES + "." + COL_CATEGORY_ID;

  public static final String COL_HANDLE_ID_REF = "uf_handle_id_ref";

  public static final String COL_UF_HANDLE_ID_REF = TBL_USER_FLAGS + "."+ COL_HANDLE_ID_REF;
  public static final String COL_UF_IS_FAVORITE = TBL_USER_FLAGS + "."+ COL_IS_FAVORITE;
  public static final String COL_UF_IS_TRASHED = TBL_USER_FLAGS + "."+ COL_IS_TRASHED;
  public static final String COL_UF_IS_ARCHIVED = TBL_USER_FLAGS + "."+ COL_IS_ARCHIVED;
  public static final String COL_UF_IS_ERASED = TBL_USER_FLAGS + "."+ COL_IS_ERASED;

  public static final String COL_UF_IS_ALARM = TBL_USER_FLAGS + "."+ COL_IS_UF_ALARM;
  public static final String COL_UF_IS_REMINDER_FIRED = TBL_USER_FLAGS + "."+ COL_IS_UF_REMINDER_FIRED;
  public static final String COL_UF_IS_RECURRENCE_RULE = TBL_USER_FLAGS + "."+ COL_IS_UF_RECURRENCE_RULE;

  public static final String COL_UF_CATEGORY = TBL_USER_FLAGS + "."+ COL_IS_CATEGORY;
  public static final String COL_UF_TAG_LIST = TBL_USER_FLAGS + "."+ COL_TAG_LIST_USER;

  // cols for category_flags table
  public static final String DB_COL_CF_CATEGORY_ID_REF = "cf_category_id_ref";
  public static final String DB_COL_CF_NAME = "cf_name";
  public static final String DB_COL_CF_DESCRIPTION  = "cf_description";
  public static final String DB_COL_CF_COLOR  = "cf_color";
  public static final String DB_COL_CF_DELETED  = "cf_is_deleted";

  public static final String DB_COL_FULL_NAME_CF_ID = TBL_CATEGORIES_FLAG + "." + DB_COL_CF_CATEGORY_ID_REF;
  public static final String DB_COL_FULL_NAME_CF_NAME = TBL_CATEGORIES_FLAG + "." + DB_COL_CF_NAME;
  public static final String DB_COL_FULL_NAME_CF_DESCRIPTION  = TBL_CATEGORIES_FLAG + "." + DB_COL_CF_DESCRIPTION;
  public static final String DB_COL_FULL_NAME_CF_COLOR  = TBL_CATEGORIES_FLAG + "." + DB_COL_CF_COLOR;
  public static final String DB_COL_FULL_NAME_CF_DELETED  = TBL_CATEGORIES_FLAG + "." + DB_COL_CF_DELETED;

  public static final String COL_LS_HANDLE_ID_REF = "ls_handle_id_ref";
  public static final String COL_LS_LAST_SHOWN = "ls_last_shown_date";

  public static final String COL_LS_HANDLE_ID = TBL_LAST_SHOWN + "."+ COL_LS_HANDLE_ID_REF;
  public static final String COL_LS_LAST_SHOWN_DATE = TBL_LAST_SHOWN + "."+ COL_LS_LAST_SHOWN;

  // columns for prayer linked sets
  public static final String COL_LINKED_SET_ID = "linked_set_row_id";
  public static final String COL_LINKED_SET_HANDLE_ID_REF = "prayer_handle_id_ref";
  public static final String COL_LINKED_SET_TEXT_ID_REF = "prayer_text_id_ref";
  public static final String COL_LINKED_SET_TEXT_ORDER = "text_order";
  public static final String COL_LINKED_SET_TEXT_TYPE = "text_type";
  public static final String COL_LINKED_SET_TEXT_CATEGORY = "text_category";

  // temporary columns
  public static final String COL_MERGED_CATEGORY = "MergedCategory";

  public static final String WHERE_NOT_ARCHIVED_NOT_TRASHED = COL_UF_IS_ARCHIVED + " IS NOT 1 AND " + COL_UF_IS_TRASHED + " IS NOT 1 " ;
//  public static final String WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT = COL_UF_IS_ARCHIVED + " IS NOT 1 AND " + COL_UF_IS_TRASHED + " IS NOT 1 AND " + COL_PACKAGE_ID + " IS NOT " + PACKAGE_USER_INTENT + " ";
 // public static final String WHERE_NOT_ARCHIVED_NOT_TRASHED_IS_INTENT = COL_UF_IS_ARCHIVED + " IS NOT 1 AND " + COL_UF_IS_TRASHED + " IS NOT 1 AND " + COL_PACKAGE_ID + " IS " + PACKAGE_USER_INTENT + " ";
  public static final String WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT = WHERE_NOT_ARCHIVED_NOT_TRASHED + " AND " + COL_PACKAGE_ID + " IS NOT " + PACKAGE_USER_INTENT + " ";
  public static final String WHERE_NOT_ARCHIVED_NOT_TRASHED_IS_INTENT = WHERE_NOT_ARCHIVED_NOT_TRASHED + " AND " + COL_PACKAGE_ID + " IS " + PACKAGE_USER_INTENT + " ";

  // Notes table name
  public static final String TABLE_NOTES = "notes";
  // Notes table columns
  public static final String KEY_REMINDER = "alarm"; // used only in "getDateText() func, no DB related"
  public static final String KEY_LONGITUDE = "longitude";  // location for notes is not implemented yet
  public static final String KEY_CATEGORY = "category_id"; // probably not used
  public static final String KEY_LOCKED = "locked";  // locked notes, this is prepared for future releases, but not used yet

  // Attachments table name
  public static final String TABLE_ATTACHMENTS = "attachments";

  // Categories table name
  public static final String TABLE_CATEGORY = "categories";


  // Queries
  private static final String CREATE_QUERY = "create.sql";
  private static final String UPGRADE_QUERY_PREFIX = "upgrade-";
  private static final String UPGRADE_QUERY_SUFFIX = ".sql";

  private final Context mContext;

  private static DbHelper instance = null;
  private SQLiteDatabase db;

  public static synchronized DbHelper doGetInstance(Context context) {
    CopyDBFromAssets.tryCopyFromAssets(context);
    instance = new DbHelper(context);
    instance.getWritableDatabase().execSQL("ATTACH DATABASE '" + context.getApplicationInfo().dataDir + DBConst.DB_STORAGE_FILE_ATTACHED + "' AS "+ ATTACHED_DB);
    return instance;
  }

  public static synchronized DbHelper getInstance() {
    return getInstance(Knizka.getAppContext());
  }

  public static synchronized DbHelper getInstance(Context context) {
    if (instance == null) {
      instance = doGetInstance(context);
    }
    return instance;
  }

  public static synchronized DbHelper getInstance(boolean forcedNewInstance) {

    if (forcedNewInstance)
    {
      if (instance != null)
      {
       closeDB();
      }
    }

    if (instance == null || forcedNewInstance) {
      Context context;
      if (instance == null)
      {
        context = Knizka.getAppContext();
      } else {
        context = instance.mContext == null ? Knizka.getAppContext() : instance.mContext;
      }

      instance = getInstance(context);
    }
    return instance;
  }

  public static synchronized void closeDB() {
    if (instance != null) {
      instance.close();
      instance = null;
    }
  }

  public static synchronized void forceOpenDB() {
    getInstance(true);
  }

  private DbHelper(Context mContext) {
    super(mContext, mContext.getApplicationInfo().dataDir + DBConst.DB_STORAGE_FILE_USER_DATA, null, DB_VERSION_USER_DATA);
    this.mContext = mContext;
  }

  public String getDatabaseName() {
    return  DBConst.DB_ATTACHED;
  }

  public SQLiteDatabase getDatabase() {
    return getDatabase(false);
  }

  public SQLiteDatabase getDatabase(boolean forceWritable) {
    try {
      return forceWritable ? getWritableDatabase() : getReadableDatabase();
    } catch (IllegalStateException e) {
      return this.db;
    }
  }

  @Override
  public void onOpen(SQLiteDatabase db) {
    db.disableWriteAheadLogging();
    super.onOpen(db);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    try {
      LogDelegate.i("Database creation");
      execSqlFile(CREATE_QUERY, db);
      onUpgrade(db, 0, DB_VERSION_USER_DATA);
    } catch (IOException e) {
      throw new DatabaseException("Database creation failed: " + e.getMessage(), e);
    }
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    LogDelegate.w("Downgrading database version from " + newVersion + " to " + oldVersion + "!");
    db.setVersion(oldVersion);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    this.db = db;
    LogDelegate.i("Upgrading database version from " + oldVersion + " to " + newVersion);

    try {

      UpgradeProcessor.process(oldVersion, newVersion);

      for (String sqlFile : AssetUtils.list(SQL_DIR, mContext.getAssets())) {
        if (sqlFile.startsWith(UPGRADE_QUERY_PREFIX)) {
          int fileVersion = Integer.parseInt(sqlFile.substring(UPGRADE_QUERY_PREFIX.length(),
                  sqlFile.length() - UPGRADE_QUERY_SUFFIX.length()));
          if (fileVersion > oldVersion && fileVersion <= newVersion) {
            execSqlFile(sqlFile, db);
          }
        }
      }
      LogDelegate.i("Database upgrade successful");

    } catch (IOException |InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException("Database upgrade failed", e);
    }
  }

  public Note updateNote(Note note, boolean updateLastModification) {
    db = getDatabase(true);

    // To ensure note and attachments insertions are atomic and boost performances transaction are used
    db.beginTransaction();

    if ((note.getPackageID() == ConstantsBase.PACKAGE_USER_ADDED) || (note.getPackageID() == ConstantsBase.PACKAGE_USER_INTENT))
    {
      updateUserNote(db, note, updateLastModification);
    }

    updateNoteFlags(db, note);

    updateAttachments(db, note);

    LogDelegate.d("Updated note titled '" + note.getTitle() + "'");

    db.setTransactionSuccessful();
    db.endTransaction();

    return note;
  }

  private String addHTMLTagsIfNeeded(String content)
  {
    String sanitizedText = content.replaceAll("(\r\n|\n\r|\r|\n)", "<br>").trim();
    if (sanitizedText.startsWith(ConstantsBase.HTML_TEXT_BODY_CLASS) == false)
    {
      sanitizedText = ConstantsBase.HTML_TEXT_BODY_CLASS + sanitizedText+ ConstantsBase.HTML_DIV_END_TAG;
    }
    return sanitizedText;
  }

  private void updateUserNote(SQLiteDatabase db, Note note, boolean updateLastModification)
  {
//    String content = Boolean.TRUE.equals(note.isLocked())
//            ? Security.encrypt(note.getContent(), Prefs.getString(PREF_PASSWORD, ""))
//            : note.getContent();

    String content;
    if (note.getHTMLContent().isEmpty()) {
      content = Boolean.TRUE.equals(note.isLocked())
              ? Security.encrypt(note.getContent(), Prefs.getString(PREF_PASSWORD, ""))
              : note.getContent();
    } else {
      content = Boolean.TRUE.equals(note.isLocked())
              ? Security.encrypt(note.getHTMLContent(), Prefs.getString(PREF_PASSWORD, ""))
              : note.getHTMLContent();
    }

    if (note.getCreation() == null)
    {
      note.setCreation(Calendar.getInstance().getTimeInMillis());
      note.setHandleID(note.getCreation());
    }

    ContentValues values = new ContentValues();
    values.put(COL_TITLE, note.getTitle());
    values.put(COL_CONTENT, addHTMLTagsIfNeeded(content));
    values.put(COL_INSIGHT, replaceLineBreak( stripToMaxChars(stripHTML(content), 500)));
    values.put(COL_HANDLE_ID, note.getCreation());
    long lastModification = note.getLastModification() != null && !updateLastModification
            ? note.getLastModification()
            : Calendar.getInstance().getTimeInMillis();
    values.put(COL_LAST_MODIFICATION, lastModification);
    values.put(COL_PACKAGE_ID, note.getPackageID());
    values.put(COL_PRAYER_MERGED, note.getPrayerMerged());
    values.put(COL_TAG_LIST, note.getTagList());

    values.put(COL_CATEGORY_ID_REF, note.getCategory() != null ? note.getCategory().getId() : null);
    values.put(KEY_LOCKED, note.isLocked() != null && note.isLocked());

    db.insertWithOnConflict(MAIN_PRAYERS, COL_HANDLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);

    // Fill the note with correct data before returning it
    note.setLastModification(values.getAsLong(COL_LAST_MODIFICATION));
  }

  private void execSqlFile(String sqlFile, SQLiteDatabase db) throws SQLException, IOException {
    LogDelegate.i("  exec sql file: {}" + sqlFile);
    for (String sqlInstruction : SqlParser.parseSqlFile(SQL_DIR + "/" + sqlFile, mContext.getAssets())) {
      LogDelegate.v("    sql: {}" + sqlInstruction);
      try {
        db.execSQL(sqlInstruction);
      } catch (Exception e) {
        LogDelegate.e("Error executing command: " + sqlInstruction, e);
      }
    }
  }


  /**
   * Attachments update
   * */
  public Attachment updateAttachment(Attachment attachment) {
    return updateAttachment(-1, attachment, getDatabase(true));
  }

  public Attachment updateAttachment(long noteId, Attachment attachment, SQLiteDatabase db) {
    ContentValues valuesAttachments = new ContentValues();
    valuesAttachments.put(COL_ATTCH_ID, attachment.getId() != null ? attachment.getId() : Calendar.getInstance().getTimeInMillis());
    valuesAttachments.put(COL_ATTCH_PRAYER_HANDLE_ID_REF, noteId);
    valuesAttachments.put(COL_ATTCH_URI, attachment.getUri().toString());
    valuesAttachments.put(COL_ATTCH_MIME_TYPE, attachment.getMime_type());
    valuesAttachments.put(COL_ATTCH_NAME, attachment.getName());
    valuesAttachments.put(COL_ATTCH_SIZE, attachment.getSize());
    valuesAttachments.put(COL_ATTCH_LENGTH, attachment.getLength());
    db.insertWithOnConflict(MAIN_ATTACHMENTS, COL_ATTCH_ID, valuesAttachments, SQLiteDatabase.CONFLICT_REPLACE);
    return attachment;
  }

  /**
   * Getting single note
   */
  public Note getNote(long id) {
    List<Note> notes = getNotes(" WHERE " + COL_HANDLE_ID + " = " + id, "", "",true);
    return notes.isEmpty() ? null : notes.get(0);
  }

  /**
   * Getting note content for linked sets
   */
  public String getNoteContentForLinkedSet(long id) {
    String retVal = "";
    List<NoteLink> linkedNotes = getLinkedNotes(id, COL_LINKED_SET_TEXT_ORDER, "", true);
    for (NoteLink linkedNote : linkedNotes) {
      Note note = null;
      if (linkedNote.getTextType() == LINKED_NOTE_TYPE_RANDOM_CATEGORY) {
        note = getRandomFromCategory(linkedNote.getCategory());
      } else {
        note = getNote(linkedNote.getTextIdRef());
      }

      if (note != null) {
        retVal = retVal + "  <div class= \"PRStartPrayerDecoration\"></div> " + "<div class= \"PRTitleLinkedSet\">" + note.getTitle() + "</div>" + note.getHTMLContent();
      }
    }
    return retVal;
  }

  public String getNoteContentForShare(Note note) {
    String result = "";

    if (note.getPrayerMerged() == ConstantsBase.PRAYER_MERGED_LINKED_SET)
    {
      result = DbHelper.getInstance().getNoteContentForLinkedSet(note.getHandleID());
    } else {
      Note dbNote = getNote(note.getHandleID());
      if (dbNote != null)
      {
        result =  dbNote.getHTMLContent();
      }
    }
    return result;
  }

  /**
   * Getting All notes
   *
   * @param checkNavigation Tells if navigation status (notes, archived) must be kept in
   *                        consideration or if all notes have to be retrieved
   * @return Notes list
   */
  public List<Note> getAllNotes(Boolean checkNavigation) {
    String whereCondition = "";
    if (Boolean.TRUE.equals(checkNavigation)) {
      int navigation = Navigation.getNavigation();
      switch (navigation) {
        case Navigation.NOTES:
          return getNotesActivePrayers();
        case Navigation.ARCHIVE:
          return getNotesArchived();
        case Navigation.REMINDERS:
          return getNotesWithReminder(Prefs.getBoolean(PREF_FILTER_PAST_REMINDERS, false));
        case Navigation.TRASH:
          return getNotesTrashed();
        case Navigation.UNCATEGORIZED:
          return getNotesUncategorized();
        case Navigation.FAVORITES:
          return getFavorites();
        case Navigation.LAST_SHOWN:
          return getLastShown();
        case Navigation.RANDOM:
          return getRandom();
        case Navigation.PRAYER_MERGED:
          return getPrayerMerged();
        case Navigation.PRAYER_LINKED_SET:
          return getPrayerLinkedSet();
        case Navigation.JKS:
        case Navigation.JKS_NUMBER_SEARCH:
        case Navigation.JKS_CATEGORIES:
            return getNotesByCategory();
        case Navigation.INTENTIONS:
          return getIntentions();
        case Navigation.CATEGORY:
          return getNotesByCategory(Navigation.getCategory(), "");
        default:
          return getNotes(whereCondition, "", "",true);
      }
    } else {
      return getNotes(whereCondition, "", "", true);
    }
  }

  private String getJKSSortingType()
  {
    String result;

    String jksSortingType = Prefs.getString(PREF_JKS_SORTING_TYPE, PREF_JKS_SORTING_TYPE_DEFAULT);

    switch (jksSortingType)
    {
      default:
      case JKS_SORTING_TYPE_NUMBER:
        result =  COL_TEXT_NUMBER + " ASC, ";
        break;
      case JKS_SORTING_TYPE_NAME:
        result =  COL_TITLE + " COLLATE LOCALIZED ASC, ";
        break;
    }
    return result;
  }

  public List<Note> getNotesActive() {
    String whereCondition = " WHERE " + COL_UF_IS_ARCHIVED + " IS NOT 1 AND " + COL_UF_IS_TRASHED + " IS NOT 1 ";
    return getNotes(whereCondition, "", "",true);
  }

  public List<Note> getNotesActivePrayers() {
    String whereCondition = " WHERE " +  COL_TEXT_NUMBER + " IS NULL AND "  + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT + " AND " + COL_PRAYER_MERGED + " IS NOT 1 ";
    return getNotes(whereCondition, "", "",true);
  }

  public List<Note> getNotesArchived() {
    String whereCondition = " WHERE " + COL_UF_IS_ARCHIVED + " = 1 AND " + COL_UF_IS_TRASHED + " IS NOT 1 ";
    return getNotes(whereCondition, "", "",true);
  }

  public List<Note> getNotesTrashed() {
    String whereCondition = " WHERE " + COL_UF_IS_TRASHED + " = 1 ";
    return getNotes(whereCondition, "", "", true);
  }

  public List<Note> getNotesUncategorized() {
    String whereCondition = " WHERE "
            + "(" + COL_MERGED_CATEGORY  + " IS NULL OR " + COL_MERGED_CATEGORY  + " == 0) "
            + "AND " + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;
    return getNotes(whereCondition, "", "", true);
  }

  public List<Note> getLastShown() {
    String whereCondition = " WHERE " +  COL_LS_LAST_SHOWN_DATE +" IS NOT NULL ";
    String orderBy = " " + COL_LS_LAST_SHOWN_DATE + " DESC, ";
    return getNotes(whereCondition, orderBy, "", true);
  }

  public List<Note> getRandom() {
    String whereCondition = " WHERE " +  COL_TEXT_NUMBER + " IS NULL AND "  + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;
    return getNotes(whereCondition, " RANDOM(), ", " LIMIT 1 ", true);
  }

  public Note getRandomFromCategory(int categoryID) {
    String whereCondition = " WHERE " + COL_MERGED_CATEGORY + " = " + categoryID + " AND " + COL_TEXT_NUMBER + " IS NULL AND "  + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;
    List<Note> notes = getNotes(whereCondition, " RANDOM(), ", " LIMIT 1 ", true);
    return notes.isEmpty() ? null : notes.get(0);
  }

  public List<Note> getFavorites() {
    String whereCondition = " WHERE " +  COL_UF_IS_FAVORITE +" = 1 AND "+ WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;
    // String orderBy = " " + COL_LS_LAST_SHOWN_DATE + " DESC, ";
    return getNotes(whereCondition, "", "", true);
  }

  public List<Note> getPrayerMerged() {
    String whereCondition = " WHERE " + COL_PRAYER_MERGED + " = 1 AND "  + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;
    return getNotes(whereCondition, "", "", true);
  }

  public List<Note> getPrayerLinkedSet() {
    String whereCondition = " WHERE " + COL_PRAYER_MERGED + " = 2 AND "  + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;
    return getNotes(whereCondition, "", "", true);
  }

  public List<Note> getJKS() {
    String whereCondition = " WHERE "
            +  COL_HANDLE_ID +" > 12000000 AND " + COL_HANDLE_ID + " < 13000000 ";
    return getNotes(whereCondition,  getJKSSortingType(), "", true);
  }


  public List<Note> getJKSByCategories(String jksCategoryID) {
    String whereCondition = " WHERE "
            +  COL_HANDLE_ID +" > 12000000 AND " + COL_HANDLE_ID + " < 13000000 AND " + COL_MERGED_CATEGORY + " = " + jksCategoryID ;
    return getNotes(whereCondition,  getJKSSortingType(), "", true);
  }

  public List<Note> getIntentions() {
    String whereCondition = " WHERE " +  WHERE_NOT_ARCHIVED_NOT_TRASHED_IS_INTENT ;
    return getNotes(whereCondition, "", "", true);
  }

  public List<Note> getNotesWithLocation() {
    String whereCondition = " WHERE " + KEY_LONGITUDE + " IS NOT NULL "
            + "AND " + KEY_LONGITUDE + " != 0 ";
    return getNotes(whereCondition, "", "", true);
  }

  public List<Note> getNotes(String whereCondition, String orderBy, String limit, boolean order) {
    List<Note> noteList = new ArrayList<>();

    String sort_column, sort_order = "";

    // Generic query to be specialized with conditions passed as parameter

    if (whereCondition.isEmpty() )
    {
      whereCondition = " WHERE " + COL_IS_ERASED + " IS NOT 1 ";
    } else {
      whereCondition = whereCondition +  " AND " + COL_IS_ERASED + " IS NOT 1 ";
    }

    String query = "";

    query = " select " +  COL_UF_HANDLE_ID_REF + ", " + COL_HANDLE_ID + ", " + COL_TITLE + ", " + COL_INSIGHT +", "  + COL_CONTENT + ", " + COL_TAG_LIST + ", "    + COL_PACKAGE_ID + ", " + COL_PRAYER_MERGED + ", " + COL_CATEGORY_ID_SEl +
            ", "+ COL_CATEGORY_NAME + ", "+ COL_CATEGORY_DESCRIPTION + ", "+ COL_CATEGORY_COLOR + ", " + COL_UF_IS_TRASHED +  ", " + COL_IS_FAVORITE +  ", " +  COL_UF_IS_ARCHIVED +  ", " +  COL_UF_IS_ERASED +  ", " + COL_UF_CATEGORY +  ", " + COL_UF_TAG_LIST +  ", " +
            COL_UF_IS_ALARM + ", " +  COL_UF_IS_REMINDER_FIRED + ", " + COL_UF_IS_RECURRENCE_RULE + ", " +
            DB_COL_FULL_NAME_CF_ID +  ", " + DB_COL_FULL_NAME_CF_NAME +  ", " + DB_COL_FULL_NAME_CF_DESCRIPTION + ", " + DB_COL_FULL_NAME_CF_COLOR + ", " + DB_COL_FULL_NAME_CF_DELETED + ", " +
            COL_LS_LAST_SHOWN_DATE + ", " +

            " CASE " +

            " WHEN " + COL_UF_CATEGORY + " IS NULL THEN " + COL_CATEGORY_ID_REF +
            " WHEN " + COL_UF_CATEGORY + " = 0 THEN " + COL_CATEGORY_ID_REF +
            " WHEN " + COL_UF_CATEGORY + " = 99 THEN 0 " +

            " ELSE " + COL_UF_CATEGORY  +

            " END AS '" + COL_MERGED_CATEGORY + "' " +

            " FROM ( " +
            " select * from " + ATTCH_PRAYERS +
            " UNION " +
            " select * from " + MAIN_PRAYERS +
            " ) foo " +

            " LEFT JOIN " + TBL_USER_FLAGS +
            " ON (" + COL_UF_HANDLE_ID_REF + " = " + COL_HANDLE_ID + ") " +

            " LEFT JOIN " + ATTCH_CATEGORIES +
            " ON ( " + COL_MERGED_CATEGORY  + " = " + COL_ATT_CATEGORY + ") " +

            " LEFT JOIN " + TBL_CATEGORIES_FLAG +
            " ON (" + DB_COL_FULL_NAME_CF_ID + " = " + COL_MERGED_CATEGORY +" ) " +

            " LEFT JOIN " + TBL_LAST_SHOWN +
            " ON (" + COL_HANDLE_ID + " = " + COL_LS_HANDLE_ID + ") " +

            whereCondition +
            " order by " + orderBy + COL_TITLE + " COLLATE LOCALIZED ASC " + limit;

    LogDelegate.v("Query: " + query);

    try (Cursor cursor = getDatabase().rawQuery(query, null)) {


      // Looping through all rows and adding to list
      if (cursor.moveToFirst()) {
        do {
          Note note = new Note();
          note.setCreation(cursor.getLong(cursor.getColumnIndex(COL_HANDLE_ID)));
          note.setLastModification(new Long(0));  //we don't care about this value, not implemented yet
          note.setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
          note.setContent(cursor.getString(cursor.getColumnIndex(COL_INSIGHT)));
          note.setHTMLContent(cursor.getString(cursor.getColumnIndex(COL_CONTENT)));
          note.setHandleID(cursor.getLong(cursor.getColumnIndex(COL_HANDLE_ID)));
          note.setPackageID(cursor.getLong(cursor.getColumnIndex(COL_PACKAGE_ID)));
          note.setPrayerMerged(cursor.getLong(cursor.getColumnIndex(COL_PRAYER_MERGED)));
          note.setTagList(cursor.getString(cursor.getColumnIndex(COL_TAG_LIST)));
          if (cursor.getString(cursor.getColumnIndex(COL_TAG_LIST_USER)) != null)
          {
            note.setTagList( cursor.getString(cursor.getColumnIndex(COL_TAG_LIST_USER)));
          }
          note.setFavorite((int)cursor.getLong(cursor.getColumnIndex(COL_IS_FAVORITE)));
          note.setArchived((int)cursor.getLong(cursor.getColumnIndex(COL_IS_ARCHIVED)));
          note.setTrashed((int)cursor.getLong(cursor.getColumnIndex(COL_IS_TRASHED)));
          note.setErased((int)cursor.getLong(cursor.getColumnIndex(COL_IS_ERASED)));

          note.setAlarm(cursor.getString(cursor.getColumnIndex(COL_IS_UF_ALARM)));
          note.setReminderFired(cursor.getInt(cursor.getColumnIndex(COL_IS_UF_REMINDER_FIRED)));
          note.setRecurrenceRule(cursor.getString(cursor.getColumnIndex(COL_IS_UF_RECURRENCE_RULE)));

          note.setLatitude("");
          note.setLongitude("");
          note.setAddress("");
          note.setLocked(false);
          note.setChecklist(false);

              // Eventual decryption of content
              // could be imnplemented in future
              //      if (note.isLocked()) {
              //          note.setContent(Security.decrypt(note.getContent(), Prefs.getString(Constants.PREF_PASSWORD,
              //                  "")));
              //      }

          // Set category
          long categoryId = cursor.getLong(cursor.getColumnIndex(COL_MERGED_CATEGORY));

          if (cursor.getInt(cursor.getColumnIndex(DB_COL_CF_DELETED)) == 1)
          {
            categoryId = 0;
          }

          if (categoryId != 0)
          {
            String catName = cursor.getString(cursor.getColumnIndex(DB_COL_CF_NAME)) == null ? cursor.getString(cursor.getColumnIndex(COL_CATEGORY_NAME)) : cursor.getString(cursor.getColumnIndex(DB_COL_CF_NAME));
            String catDescription = cursor.getString(cursor.getColumnIndex(DB_COL_CF_DESCRIPTION)) == null ? cursor.getString(cursor.getColumnIndex(COL_CATEGORY_DESCRIPTION)) : cursor.getString(cursor.getColumnIndex(DB_COL_CF_DESCRIPTION));
            String catColor = cursor.getString(cursor.getColumnIndex(DB_COL_CF_COLOR)) == null ? cursor.getString(cursor.getColumnIndex(COL_CATEGORY_COLOR)) : cursor.getString(cursor.getColumnIndex(DB_COL_CF_COLOR));

            Category category = new Category(categoryId, catName, catDescription, catColor);
            note.setCategory(category);
          }

          // Add eventual attachments uri
          note.setAttachmentsList(getNoteAttachments(note));

          // Adding note to list
          noteList.add(note);

        } while (cursor.moveToNext());
      }
    }

    LogDelegate.v("Query: Retrieval finished!");
    return noteList;
  }

  public List<NoteLink> getLinkedNotes(long  parentNoteID, String orderBy, String limit, boolean order) {
    List<NoteLink> noteList = new ArrayList<>();

    String whereCondition = " WHERE " + COL_LINKED_SET_HANDLE_ID_REF + " = " + parentNoteID;
    String query = "";
    query = " SELECT " +  COL_LINKED_SET_ID + ", " + COL_LINKED_SET_HANDLE_ID_REF + ", " + COL_LINKED_SET_TEXT_ID_REF + ", " + COL_LINKED_SET_TEXT_ORDER +
            ", "+ COL_LINKED_SET_TEXT_TYPE + ", "+ COL_LINKED_SET_TEXT_CATEGORY +
            " FROM " +
            " " + TBL_PRAYER_LINKED_SET + " " +
            whereCondition +
            " ORDER BY " + orderBy;

    try (Cursor cursor = getDatabase().rawQuery(query, null)) {

      // Looping through all rows and adding to list
      if (cursor.moveToFirst()) {
        do {
          NoteLink noteLink = new NoteLink();

          noteLink.setTextIdRef(cursor.getLong(cursor.getColumnIndex(COL_LINKED_SET_TEXT_ID_REF)));
          noteLink.setTextOrder(cursor.getInt(cursor.getColumnIndex(COL_LINKED_SET_TEXT_ORDER)));
          noteLink.setTextType(cursor.getInt(cursor.getColumnIndex(COL_LINKED_SET_TEXT_TYPE)));
          noteLink.setCategory(cursor.getInt(cursor.getColumnIndex(COL_LINKED_SET_TEXT_CATEGORY)));

          noteList.add(noteLink);
        } while (cursor.moveToNext());
      }
    }
    return noteList;
  }

  private String GetPrayerSetNoteCaption(long textID, long categoryId)
  {
    String result = "";
    Note noteFromLinkedSet = getNote(textID);
    if (noteFromLinkedSet != null)
    {
      result = noteFromLinkedSet.getTitle();
    } else {
      Category category = getCategory((long)categoryId);
      if (category != null)
      {
        result = "[" + category.getName() + "]";
      }
    }

    return result;
  }

  public List<NoteLink> getLinkedNotesWithCaptions(long  parentNoteID, String orderBy, String limit, boolean order) {

    List<NoteLink> noteList = getLinkedNotes(parentNoteID, orderBy, limit, order);

    for (NoteLink linkedNote : noteList) {
      linkedNote.setTextRefCaption(GetPrayerSetNoteCaption(linkedNote.getTextIdRef(),(long)linkedNote.getCategory()));
    }

    return noteList;
  }

    /**
     * Duplicates single note
     */
  public Note duplicateNote(Note note) {
    Note newNote = new Note(note);

    newNote.setHandleID(null);
    newNote.setCreation("");
    newNote.setLastModification("");
    newNote.setTitle(note.getTitle() + Knizka.getAppContext().getString(R.string.note_duplicate_title_text));
    newNote.getAttachmentsList().clear();

    if (note.getPrayerMerged() == PRAYER_MERGED_LINKED_SET) {
      newNote.setPackageID(PACKAGE_USER_ADDED);
      newNote.setPrayerMerged(PRAYER_MERGED_LINKED_SET);
    } else {
      if (note.getPackageID() <= ConstantsBase.PACKAGE_SYSTEM) {
        newNote.setPackageID(ConstantsBase.PACKAGE_USER_ADDED);
      }
    }

    Note noteNewSaved = DbHelper.getInstance().updateNote(newNote, true);

    duplicateAttachments(note, newNote);

    copyLinkedNotesToPrayerSet(note, noteNewSaved);

    return noteNewSaved;
  }


  public void duplicateAttachments(Note sourceNote, Note duplicatedNote) {

    BackupHelper.duplicateAttachments(StorageHelper.getBaseStorageDir(mContext) , sourceNote, duplicatedNote);

  }


  /**
   * Archives/restore single note
   */
  public void archiveNote(Note note, boolean archive) {
    note.setArchived(archive);
    updateNote(note, false);
  }


  /**
   * Trashes/restore single note
   */
  public void trashNote(Note note, boolean trash) {
    note.setTrashed(trash);
    updateNote(note, false);
  }


  /**
   * Deleting single note
   */
  public boolean deleteNote(Note note) {
    return deleteNote(note, false);
  }


  /**
   * Deleting single note, eventually keeping attachments
   */
  public boolean deleteNote(Note note, boolean keepAttachments) {

    SQLiteDatabase db = getDatabase(true);

    if (note.getPackageID() != ConstantsBase.PACKAGE_SYSTEM)
    {
      db.delete(MAIN_PRAYERS, COL_HANDLE_ID + " = ?", new String[]{String.valueOf(note.getHandleID())});
      db.delete(TBL_USER_FLAGS, COL_HANDLE_ID_REF + " = ?", new String[]{String.valueOf(note.getHandleID())});
      db.delete(TBL_PRAYER_LINKED_SET, COL_LINKED_SET_HANDLE_ID_REF + " = ?", new String[]{String.valueOf(note.getHandleID())});
    } else {
      // mark prayer as deleted, if it is not a "system" - bundled prayer
      ContentValues values = new ContentValues();
      values.put(COL_HANDLE_ID_REF, note.getHandleID());
      values.put(COL_IS_ERASED, Integer.valueOf(1));
      db.insertWithOnConflict(TBL_USER_FLAGS, COL_HANDLE_ID_REF, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    if (!keepAttachments) {
      db.delete(MAIN_ATTACHMENTS, COL_ATTCH_PRAYER_HANDLE_ID_REF + " = ?", new String[]{String.valueOf(note.getHandleID())});
    }

    return true;
  }


  /**
   * Deleting single note by its ID
   */
  public boolean deleteNote(long noteId, boolean keepAttachments) {
    SQLiteDatabase db = getDatabase(true);
    db.delete(MAIN_PRAYERS, COL_HANDLE_ID + " = ?", new String[]{String.valueOf(noteId)});
    db.delete(TBL_USER_FLAGS, COL_HANDLE_ID_REF + " = ?", new String[]{String.valueOf(noteId)});

    if (!keepAttachments) {
      db.delete(MAIN_ATTACHMENTS, COL_ATTCH_PRAYER_HANDLE_ID_REF + " = ?", new String[]{String.valueOf(noteId)});
    }

    return true;
  }

  public boolean deleteNoteFromPrayerSets(long noteId)
  {
    SQLiteDatabase db = getDatabase(true);

    db.delete(TBL_PRAYER_LINKED_SET, COL_LINKED_SET_HANDLE_ID_REF + " = ?", new String[]{String.valueOf(noteId)});

    return true;
  }

  /**
   * Empties trash deleting all trashed notes
   */
  public void emptyTrash() {
    for (Note note : getNotesTrashed()) {
      deleteNote(note);
    }
  }


  /**
   * Gets notes matching pattern with title or content text
   *
   * @param pattern String to match with
   * @return Notes list
   */
  public List<Note> getNotesByPattern(String pattern) {
    String escapedPattern = escapeSql(pattern);
    int navigation = Navigation.getNavigation();
    String whereCondition = " WHERE "
            + COL_UF_IS_TRASHED + (navigation == Navigation.TRASH ? " IS 1" : " IS NOT 1")
            + (navigation == Navigation.ARCHIVE ? " AND " + COL_UF_IS_ARCHIVED + " IS 1" : "")
            + (navigation == Navigation.CATEGORY ? " AND " + COL_MERGED_CATEGORY + " = " + Navigation.getCategory() : "")
            + (navigation == Navigation.UNCATEGORIZED ? " AND (" + COL_MERGED_CATEGORY + " IS NULL OR " + COL_MERGED_CATEGORY
            + " == 0) " : "")
            + (Navigation.checkNavigation(Navigation.REMINDERS) ? " AND " + COL_UF_IS_ALARM + " IS NOT NULL" : "")
            + " AND ("
            + " ( " + KEY_LOCKED + " IS NOT 1 AND (" + COL_TITLE + " LIKE '%" + escapedPattern + "%' ESCAPE '\\' " + " OR " +
            COL_CONTENT + " LIKE '%" + escapedPattern + "%' ESCAPE '\\' ))"
            + " OR ( " + KEY_LOCKED + " = 1 AND " + COL_TITLE + " LIKE '%" + escapedPattern + "%' ESCAPE '\\' )"
            + ")";
    return getNotes(whereCondition, "", "", true);
  }


  /**
   * Gets notes matching pattern with JKS number
   *
   * @param pattern String to match with JKS number
   * @return Notes list
   */
  public List<Note> getNotesByJKSNumber(String pattern) {
    String escapedPattern = escapeSql(pattern);

    String whereCondition = " WHERE " +  COL_HANDLE_ID +" > 12000000 AND " + COL_HANDLE_ID + " < 13000000 AND   "  + COL_TEXT_NUMBER + " = " + escapedPattern + " ";

    return getNotes(whereCondition, "", "", true);
  }


  static String escapeSql(String pattern) {
    return StringUtils.replace(pattern, "'", "''")
            .replace("%", "\\%")
            .replace("_", "\\_");
  }


  /**
   * Search for notes with reminder
   *
   * @param filterPastReminders Excludes past reminders
   * @return Notes list
   */
  public List<Note> getNotesWithReminder(boolean filterPastReminders) {
    String whereCondition = " WHERE " + COL_IS_UF_ALARM
            + (filterPastReminders ? " >= " + Calendar.getInstance().getTimeInMillis() : " IS NOT NULL")
            + " AND " + WHERE_NOT_ARCHIVED_NOT_TRASHED;

    return getNotes(whereCondition, "", "", true);
  }


  /**
   * Returns all notes that have a reminder that has not been alredy fired
   *
   * @return Notes list
   */
  public List<Note> getNotesWithReminderNotFired () {
    String whereCondition = " WHERE " + COL_IS_UF_ALARM + " IS NOT NULL"
            + " AND " + COL_UF_IS_REMINDER_FIRED + " IS NOT 1"
            + " AND " + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT;
    return getNotes(whereCondition, "", "", true);
  }


  /**
   * Retrieves locked or unlocked notes
   */
  public List<Note> getNotesWithLock(boolean locked) {
    String whereCondition = " WHERE " + KEY_LOCKED + (locked ? " = 1 " : " IS NOT 1 ");
    return getNotes(whereCondition, "",  "", true);
  }


  /**
   * Search for notes with reminder expiring the current day
   *
   * @return Notes list
   */
  public List<Note> getTodayReminders() {
    String whereCondition = " WHERE DATE(" + COL_UF_IS_ALARM + "/1000, 'unixepoch') = DATE('now') AND " +
              COL_UF_IS_TRASHED  + " IS NOT 1";
    return getNotes(whereCondition, "",  "", false);
  }


  /**
   * Retrieves all attachments related to specific note
   */
  public ArrayList<Attachment> getNoteAttachments(Note note) {
    String whereCondition = " WHERE " + COL_ATTCH_PRAYER_HANDLE_ID_REF + " = " + note.getHandleID();
    return getAttachments(whereCondition);
  }

  public List<Note> getMasked() {
    String whereCondition = " WHERE " + KEY_LOCKED + " = 1";
    return getNotes(whereCondition, "",  "", false);
  }


  /**
   * Retrieves all notes related to Category it passed as parameter
   *
   * @param categoryId Category integer identifier
   * @return List of notes with requested category
   */
  public List<Note> getNotesByCategory(Long categoryId, String orderBy) {
    List<Note> notes;
    boolean filterArchived = Prefs.getBoolean(PREF_FILTER_ARCHIVED_IN_CATEGORIES + categoryId, false);
    try {
      String whereCondition = " WHERE "
              + COL_MERGED_CATEGORY + " = " + categoryId
              + " AND " + COL_UF_IS_TRASHED + " IS NOT 1"
              + (filterArchived ? " AND " + COL_UF_IS_ARCHIVED + " IS NOT 1" : "");
      notes = getNotes(whereCondition, orderBy,  "", true);
    } catch (NumberFormatException e) {
      notes = getAllNotes(true);
    }
    return notes;
  }

  public List<Note> getNotesByCategory() {

    int categoryID = Prefs.getInt(PREF_NAVIGATION_JKS_CATEGORY_ID, PREF_NAVIGATION_JKS_CATEGORY_ID_DEFAULT);
    if (categoryID > 0)
    {
      return getNotesByCategory(new Long(categoryID), getJKSSortingType());
    } else {
      return getJKS();
    }
  }

  /**
   * Retrieves all tags
   */
  public List<Tag> getTags() {
    return getTags(null);
  }


  /**
   * Retrieves all tags of a specified note
   */
  public List<Tag> getTags(Note note) {
    List<Tag> tags = new ArrayList<>();
    HashMap<String, Integer> tagsMap = new HashMap<>();

    String whereCondition = " WHERE "
            + (note != null ? COL_HANDLE_ID + " = " + note.getHandleID() + " AND " : "")
            + "(" + COL_TAG_LIST + " LIKE '%#%' OR " + COL_TITLE + " LIKE '%#%'  OR " + COL_UF_TAG_LIST + " LIKE '%#%' " + ")"
            + " AND " + COL_UF_IS_TRASHED + " IS " + (Navigation.checkNavigation(Navigation.TRASH) ? "" : " NOT ") + " 1";

    List<Note> notesRetrieved = getNotes(whereCondition, "",  "", true);

    for (Note noteRetrieved : notesRetrieved) {
      HashMap<String, Integer> tagsRetrieved = TagsHelper.retrieveTags(noteRetrieved);
      for (String s : tagsRetrieved.keySet()) {
        int count = tagsMap.get(s) == null ? 0 : tagsMap.get(s);
        tagsMap.put(s, ++count);
      }
    }

    for (Entry<String, Integer> entry : tagsMap.entrySet()) {
      Tag tag = new Tag(entry.getKey(), entry.getValue());
      tags.add(tag);
    }

    // localized sorting
    Collections.sort(tags, new Comparator<Tag>() {
    Collator collator = Collator.getInstance(new Locale(LanguageHelper.getCurrentLocaleAsString(Knizka.getAppContext())));
//      Collator collator = Collator.getInstance(LanguageHelper.getCurrentLanguageFromPrefs());

      @Override
      public int compare(Tag o1, Tag o2) {
        return collator.compare(o1.getText(), o2.getText());
      }
    });

    return tags;
  }


  /**
   * Retrieves all notes related to category it passed as parameter
   */
  public List<Note> getNotesByTag(String tag) {
    if (tag.contains(",")) {
      return getNotesByTag(tag.split(","));
    } else {
      return getNotesByTag(new String[]{tag});
    }
  }


  /**
   * Retrieves all notes with specified tags
   */
  public List<Note> getNotesByTag(String[] tags) {
    StringBuilder whereCondition = new StringBuilder();
    whereCondition.append(" WHERE ");
    for (int i = 0; i < tags.length; i++) {
      if (i != 0) {
        whereCondition.append(" AND ");
      }
      whereCondition.append("(" + COL_UF_TAG_LIST + " LIKE '%").append(tags[i].trim()).append("%' OR ").append(COL_TITLE)
              .append(" LIKE '%").append(tags[i].trim()).append("%')");
    }
    // Trashed notes must be included in search results only if search if performed from trash
    whereCondition.append(" AND " + COL_UF_IS_TRASHED + " IS ").append(Navigation.checkNavigation(Navigation.TRASH) ?
            "" : "" +
            " NOT ").append(" 1");

    return rx.Observable.from(getNotes(whereCondition.toString(), "",  "", true))
            .map(note -> {
              boolean matches = rx.Observable.from(tags)
                      .all(tag -> {
                        Pattern p = Pattern.compile(".*" + tag + ".*", Pattern.MULTILINE);
                     //   Pattern p = Pattern.compile(".*(\\s|^)" + tag + "(\\s|$).*", Pattern.MULTILINE);
//                        return p.matcher((note.getTitle() + " " + note.getContent())).find();
                        return p.matcher((note.getTitle() + " " + note.getTagList())).find();
                      }).toBlocking().single();
              return matches ? note : null;
            })
            .filter(note -> note != null) // todo: diference between ON
//                .filter(Objects::nonNull)    // requires API 24
            .toList().toBlocking().single();
  }



  /**
   * Retrieves all attachments
   */
  public ArrayList<Attachment> getAllAttachments() {
    return getAttachments("");
  }


  public ArrayList<Attachment> getAttachments(String whereCondition) {

    ArrayList<Attachment> attachmentsList = new ArrayList<>();

    String sql = " select " +  COL_ATTCH_ID + ", " + COL_ATTCH_URI + ", " + COL_ATTCH_NAME + ", " + COL_ATTCH_SIZE +", "  +
            COL_ATTCH_LENGTH + ", " + COL_ATTCH_MIME_TYPE + ", "    + COL_ATTCH_PRAYER_HANDLE_ID_REF + " " +
            " FROM ( " +
            " select * from " + ATTCH_ATTACHMENTS +
            " UNION " +
            " select * from " + MAIN_ATTACHMENTS +
            " ) foo " +
            whereCondition;

         //   SQLiteDatabase db;
    try (Cursor cursor = getDatabase().rawQuery(sql, null)) {

      // Looping through all rows and adding to list
      if (cursor.moveToFirst()) {
        Attachment mAttachment;
        do {
          mAttachment = new Attachment(cursor.getLong(cursor.getColumnIndex(COL_ATTCH_ID)),
                  Uri.parse(cursor.getString(cursor.getColumnIndex(COL_ATTCH_URI))), cursor.getString(cursor.getColumnIndex(COL_ATTCH_NAME)),
                  cursor.getInt(cursor.getColumnIndex(COL_ATTCH_SIZE)),
                  (long) cursor.getInt(cursor.getColumnIndex(COL_ATTCH_LENGTH)), cursor.getString(cursor.getColumnIndex(COL_ATTCH_MIME_TYPE)));
          attachmentsList.add(mAttachment);
        } while (cursor.moveToNext());
      }
    }
    return attachmentsList;
  }


  /**
   * Retrieves categories list from database
   *
   * @return List of categories
   */
  public ArrayList<Category> getCategories() {
    ArrayList<Category> categoriesList = new ArrayList<>();
    String sql = " select " + COL_CATEGORY_ID + ", " + COL_CATEGORY_NAME + ", " + COL_CATEGORY_DESCRIPTION + ", "  + COL_CATEGORY_COLOR  +  ", " +
                  DB_COL_FULL_NAME_CF_NAME + ", " + DB_COL_FULL_NAME_CF_DESCRIPTION + ", " + DB_COL_FULL_NAME_CF_COLOR + ", " + DB_COL_FULL_NAME_CF_DELETED +
            " FROM ( " +
            " select * from " + ATTCH_CATEGORIES +
            " UNION " +
            " select * from " + MAIN_CATEGORIES +
            " ) foo " +
            " LEFT JOIN " + TBL_CATEGORIES_FLAG +
            " ON (" + DB_COL_CF_CATEGORY_ID_REF + " = " + COL_CATEGORY_ID + ") " +
            " WHERE " + DB_COL_CF_DELETED + " IS NOT 1 "+
            " ORDER BY " + COL_CATEGORY_NAME + " ASC ";

    try (Cursor cursor = getDatabase().rawQuery(sql, null)) {
      // Looping through all rows and adding to list
      if (cursor.moveToFirst()) {
        do {
          long catID = cursor.getLong(cursor.getColumnIndex(COL_CATEGORY_ID));

          String catName = cursor.getString(cursor.getColumnIndex(DB_COL_CF_NAME)) == null ? cursor.getString(cursor.getColumnIndex(COL_CATEGORY_NAME)) : cursor.getString(cursor.getColumnIndex(DB_COL_CF_NAME));
          String catDescription = cursor.getString(cursor.getColumnIndex(DB_COL_CF_DESCRIPTION)) == null ? cursor.getString(cursor.getColumnIndex(COL_CATEGORY_DESCRIPTION)) : cursor.getString(cursor.getColumnIndex(DB_COL_CF_DESCRIPTION));
          String catColor = cursor.getString(cursor.getColumnIndex(DB_COL_CF_COLOR)) == null ? cursor.getString(cursor.getColumnIndex(COL_CATEGORY_COLOR)) : cursor.getString(cursor.getColumnIndex(DB_COL_CF_COLOR));

          int numOfCategorized = getCategorizedNotesCount(catID);

          categoriesList.add(new Category( catID, catName, catDescription, catColor, numOfCategorized));

        } while (cursor.moveToNext());
      }

    }
    return categoriesList;
  }

  private void updateUserCategory(Category category)
  {
    ContentValues values = new ContentValues();

    values.put(COL_CATEGORY_ID, category.getId());
    values.put(COL_CATEGORY_NAME, category.getName());
    values.put(COL_CATEGORY_DESCRIPTION, category.getDescription());
    values.put(COL_CATEGORY_COLOR, category.getColor());
    getDatabase(true).insertWithOnConflict(MAIN_CATEGORIES, COL_CATEGORY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);

  }

  /**
   * Updates or insert a new a category
   *
   * @param category Category to be updated or inserted
   * @return Rows affected or new inserted category ID
   */
  public Category updateCategory(Category category) {

    // update category ID if required. (if not exist)
    category.setId(category.getId() != null ? category.getId() : Calendar.getInstance().getTimeInMillis());

    // skip JKS categories, cannot be edited
    if ((category.getId() >= 1000) && (category.getId() <= 1100))
    {
      return category;
    }

    if (category.getId() > 100)
    {
      updateUserCategory(category);
    }

      ContentValues values = new ContentValues();
      values.put(DB_COL_CF_CATEGORY_ID_REF, category.getId());
      values.put(DB_COL_CF_NAME, category.getName());
      values.put(DB_COL_CF_DESCRIPTION, category.getDescription());
      values.put(DB_COL_CF_COLOR, category.getColor());
      getDatabase(true).insertWithOnConflict(TBL_CATEGORIES_FLAG, DB_COL_CF_CATEGORY_ID_REF, values, SQLiteDatabase.CONFLICT_REPLACE);

    return category;
  }


  private void clearCategoryFromUserPrayers(SQLiteDatabase db, Category category)
  {
    ContentValues values = new ContentValues();
    values.put(COL_CATEGORY_ID_REF, "0");

    // Updating row
    db.update(MAIN_PRAYERS, values, COL_CATEGORY_ID_REF + " = ?", new String[]{String.valueOf(category.getId())});
  }

  private void clearCategoryFromUserFlags(SQLiteDatabase db, Category category)
  {
    ContentValues values = new ContentValues();
    values.put(COL_IS_CATEGORY, "99");

    // Updating row
    db.update(TBL_USER_FLAGS, values, COL_IS_CATEGORY + " = ?", new String[]{String.valueOf(category.getId())});
  }

  public void updateCategoryDeleteFlag(SQLiteDatabase db, Category category) {

    ContentValues values = new ContentValues();
    values.put(DB_COL_CF_CATEGORY_ID_REF, category.getId());
    values.put(DB_COL_CF_DELETED, 1);

    getDatabase(true).insertWithOnConflict(TBL_CATEGORIES_FLAG, DB_COL_CF_CATEGORY_ID_REF, values, SQLiteDatabase.CONFLICT_REPLACE);

    return;
  }



  /**
   * Deletion of  a category
   *
   * @param category Category to be deleted
   * @return Number 1 if category's record has been deleted, 0 otherwise
   */
  public long deleteCategory(Category category) {
    long deleted;

    SQLiteDatabase db = getDatabase(true);

    // Un-categorize notes associated with this category
    clearCategoryFromUserPrayers(db, category);

    clearCategoryFromUserFlags(db, category);

    updateCategoryDeleteFlag(db, category);

    // Delete category
    deleted = db.delete(MAIN_CATEGORIES, COL_CATEGORY_ID + " = ?", new String[]{String.valueOf(category.getId())});
    return deleted;
  }


  /**
   * Get note Category
   */
  public Category getCategory(Long id) {
    Category category = null;

    String sql = " select " + COL_CATEGORY_ID + ", " + COL_CATEGORY_NAME + ", " + COL_CATEGORY_DESCRIPTION + ", "  + COL_CATEGORY_COLOR  +  ", " +

            DB_COL_FULL_NAME_CF_NAME + ", " + DB_COL_FULL_NAME_CF_DESCRIPTION + ", " + DB_COL_FULL_NAME_CF_COLOR + ", " + DB_COL_FULL_NAME_CF_DELETED +

            " FROM ( " +
            " select * from " + ATTCH_CATEGORIES +
            " UNION " +
            " select * from " + MAIN_CATEGORIES +
            " ) foo " +
            " LEFT JOIN " + TBL_CATEGORIES_FLAG +
            " ON (" + DB_COL_CF_CATEGORY_ID_REF + " = " + COL_CATEGORY_ID + ") " +
            " WHERE " + DB_COL_CF_DELETED + " IS NOT 1 AND " + COL_CATEGORY_ID +" = " + id;

    try (Cursor cursor = getDatabase().rawQuery(sql, null)) {

      // Looping through all rows and adding to list
      if (cursor.moveToFirst()) {
        category = new Category(cursor.getLong(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3));
      }
    }
    return category;
  }

  public int getCategorizedNotesCount(long catId) {
    int count = 0;
    String sql = "SELECT COUNT(*)"
            + " FROM (" +
            " SELECT * FROM " + ATTCH_PRAYERS +
            " UNION " +
            " SELECT * FROM " + MAIN_PRAYERS +
            " ) foo " +

            // add user flag table to skip trashed notes
            " LEFT JOIN " + TBL_USER_FLAGS +
            " ON (" + COL_UF_HANDLE_ID_REF + " = " + COL_HANDLE_ID + ") " +

            " WHERE " + COL_CATEGORY_ID_REF + " = " + catId +
            " AND " + COL_UF_IS_TRASHED + " IS NOT 1";           // exclude trashed notes

    try (Cursor cursor = getDatabase().rawQuery(sql, null)) {

      // Looping through all rows and adding to list
      if (cursor.moveToFirst()) {
        count = cursor.getInt(0);
      }
    }
    return count;
  }


  /**
   * Retrieves all linked sets as list from database
   *
   * @return List of categories
   */
  public ArrayList<Note> getLinkedSets() {
    ArrayList<Note> notesList = new ArrayList<>();

    String sql = " select " + COL_HANDLE_ID + ", " + COL_TITLE + ", " + COL_PRAYER_MERGED +  ", " + COL_PACKAGE_ID + ", "
             + COL_UF_IS_TRASHED +  ", " + COL_IS_FAVORITE +  ", " +  COL_UF_IS_ARCHIVED +  ", " +  COL_UF_IS_ERASED +
            " FROM ( " +
            " select * from " + ATTCH_PRAYERS +
            " UNION " +
            " select * from " + MAIN_PRAYERS +
            " ) foo " +
            " LEFT JOIN " + TBL_USER_FLAGS +
            " ON (" + COL_UF_HANDLE_ID_REF + " = " + COL_HANDLE_ID + ") " +
            " WHERE " + COL_PRAYER_MERGED + " = 2 AND " + WHERE_NOT_ARCHIVED_NOT_TRASHED_NOT_INTENT +
            " ORDER BY " + COL_TITLE + " ASC ";

    try (Cursor cursor = getDatabase().rawQuery(sql, null)) {
      // Looping through all rows and adding to list
      if (cursor.moveToFirst()) {
        do {

          Note note = new Note();
          note.setHandleID(cursor.getLong(cursor.getColumnIndex(COL_HANDLE_ID)));
          note.setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
          note.setPackageID(cursor.getLong(cursor.getColumnIndex(COL_PACKAGE_ID)));
          note.setPrayerMerged(cursor.getLong(cursor.getColumnIndex(COL_PRAYER_MERGED)));

          notesList.add(note);

        } while (cursor.moveToNext());
      }

    }
    return notesList;
  }

  /**
   * Retrieves statistics data based on app usage
   */
  public Stats getStats() {
    Stats mStats = new Stats();

    // Categories
    mStats.setCategories(getCategories().size());

    // Everything about notes and their text stats
    int notesActive = 0, notesArchived = 0, notesTrashed = 0, reminders = 0, remindersFuture = 0, checklists = 0,
            notesMasked = 0, tags = 0, locations = 0;
    int totalWords = 0, totalChars = 0, maxWords = 0, maxChars = 0, avgWords = 0, avgChars = 0;
    int words = 0, chars = 0;
    List<Note> notes = getAllNotes(false);
    for (Note note : notes) {
      if (note.isTrashed()) {
        notesTrashed++;
      } else if (note.isArchived()) {
        notesArchived++;
      } else {
        notesActive++;
      }
      if (note.getAlarm() != null && Long.parseLong(note.getAlarm()) > 0) {
        if (Long.parseLong(note.getAlarm()) > Calendar.getInstance().getTimeInMillis()) {
          remindersFuture++;
        } else {
          reminders++;
        }
      }
      if (note.isChecklist()) {
        checklists++;
      }
      if (note.isLocked()) {
        notesMasked++;
      }
      tags += TagsHelper.retrieveTags(note).size();
      if (note.getLongitude() != null && note.getLongitude() != 0) {
        locations++;
      }

      // do not count words, could be slow with big data
      //words = NotesHelper.getWords(note);
      //chars = NotesHelper.getChars(note);

      if (words > maxWords) {
        maxWords = words;
      }
      if (chars > maxChars) {
        maxChars = chars;
      }
      totalWords += words;
      totalChars += chars;
    }
    mStats.setNotesActive(notesActive);
    mStats.setNotesArchived(notesArchived);
    mStats.setNotesTrashed(notesTrashed);
    mStats.setReminders(reminders);
    mStats.setRemindersFutures(remindersFuture);
    mStats.setNotesChecklist(checklists);
    mStats.setNotesMasked(notesMasked);
    mStats.setTags(tags);
    mStats.setLocation(locations);
    avgWords = totalWords / (!notes.isEmpty() ? notes.size() : 1);
    avgChars = totalChars / (!notes.isEmpty() ? notes.size() : 1);

    mStats.setWords(totalWords);
    mStats.setWordsMax(maxWords);
    mStats.setWordsAvg(avgWords);
    mStats.setChars(totalChars);
    mStats.setCharsMax(maxChars);
    mStats.setCharsAvg(avgChars);

    // Everything about attachments
    int attachmentsAll = 0, images = 0, videos = 0, audioRecordings = 0, sketches = 0, files = 0;
    List<Attachment> attachments = getAllAttachments();
    for (Attachment attachment : attachments) {
      if (MIME_TYPE_IMAGE.equals(attachment.getMime_type())) {
        images++;
      } else if (MIME_TYPE_VIDEO.equals(attachment.getMime_type())) {
        videos++;
      } else if (MIME_TYPE_AUDIO.equals(attachment.getMime_type())) {
        audioRecordings++;
      } else if (MIME_TYPE_SKETCH.equals(attachment.getMime_type())) {
        sketches++;
      } else if (MIME_TYPE_FILES.equals(attachment.getMime_type())) {
        files++;
      }
    }
    mStats.setAttachments(attachmentsAll);
    mStats.setImages(images);
    mStats.setVideos(videos);
    mStats.setAudioRecordings(audioRecordings);
    mStats.setSketches(sketches);
    mStats.setFiles(files);

    return mStats;
  }

  public void setReminderFired(long noteId, boolean fired) {
    ContentValues values = new ContentValues();
    values.put(COL_IS_UF_REMINDER_FIRED, fired ? 1 : 0);
    getDatabase(true).update(TBL_USER_FLAGS, values, COL_HANDLE_ID_REF + " = ?", new String[]{String.valueOf(noteId)});
  }

  public void insertNoteToLastShown(Note note)
  {
    SQLiteDatabase db = getDatabase(true);

    ContentValues values = new ContentValues();
    values.put(COL_LS_HANDLE_ID_REF, note.getHandleID());
    values.put(COL_LS_LAST_SHOWN, Calendar.getInstance().getTimeInMillis());

    // Inserting row
    db.insertWithOnConflict(TBL_LAST_SHOWN, COL_LS_HANDLE_ID_REF, values, SQLiteDatabase.CONFLICT_REPLACE);
  }

  public void updateNoteFlags(SQLiteDatabase db, Note note)
  {
    ContentValues values = new ContentValues();
    values.put(COL_HANDLE_ID_REF, note.getHandleID());
    values.put(COL_IS_FAVORITE, note.isFavorite() ? 1 : 0);
    values.put(COL_IS_ARCHIVED, note.isArchived() ? 1 : 0);
    values.put(COL_IS_TRASHED, note.isTrashed() ? 1 : 0);
    values.put(COL_IS_CATEGORY, note.getCategory() != null ? note.getCategory().getId() : 0);
    values.put(COL_TAG_LIST_USER, note.getTagList());
    values.put(COL_IS_UF_ALARM, note.getAlarm());
    values.put(COL_IS_UF_REMINDER_FIRED, note.isReminderFired() ? 1 : 0);
    values.put(COL_IS_UF_RECURRENCE_RULE, note.getRecurrenceRule());

    db.insertWithOnConflict(TBL_USER_FLAGS, COL_HANDLE_ID_REF, values, SQLiteDatabase.CONFLICT_REPLACE);
  }
  
  public void updateAttachments(SQLiteDatabase db, Note note)
  {
    // Updating attachments
    List<Attachment> deletedAttachments = note.getAttachmentsListOld();
    for (Attachment attachment : note.getAttachmentsList()) {
      updateAttachment(note.getHandleID(), attachment, db);
      deletedAttachments.remove(attachment);
    }

    // Remove from database deleted attachments
    for (Attachment attachmentDeleted : deletedAttachments) {
      db.delete(MAIN_ATTACHMENTS, COL_ATTCH_ID + " = ?", new String[]{String.valueOf(attachmentDeleted.getId())});
    }
  }

  public void insertNoteToFavorites(Note note, Boolean value)
  {
    SQLiteDatabase db = getDatabase(true);
    updateNoteFlags(db, note);
  }

  public void insertNoteToLinkedSet(Long parentNoteID, NoteLink linkedNote)
  {
    SQLiteDatabase db = getDatabase(true);

    ContentValues values = new ContentValues();
    values.put(COL_LINKED_SET_HANDLE_ID_REF, parentNoteID);
    values.put(COL_LINKED_SET_TEXT_ID_REF, linkedNote.getTextIdRef());
    values.put(COL_LINKED_SET_TEXT_ORDER, linkedNote.getTextOrder());
    values.put(COL_LINKED_SET_TEXT_TYPE, linkedNote.getTextType());
    values.put(COL_LINKED_SET_TEXT_CATEGORY, linkedNote.getCategory());

    db.insertWithOnConflict(TBL_PRAYER_LINKED_SET, null, values, SQLiteDatabase.CONFLICT_REPLACE);

  }

  public void copyLinkedNotesToPrayerSet(Note sourceNote, Note newNote) {
    List<NoteLink> linkedNotes = getLinkedNotes(sourceNote.getHandleID(), COL_LINKED_SET_TEXT_ORDER, "", true);
    for (NoteLink linkedNote : linkedNotes) {
      insertNoteToLinkedSet(newNote.getHandleID(), linkedNote);
    }
  }

  public void insertLinkedNotesToPrayerSet(List<NoteLink> noteList, Long parentNoteID)
  {
    for (NoteLink linkedNote : noteList) {
      insertNoteToLinkedSet(parentNoteID, linkedNote);
    }
  }

  public void addNotesToPrayerSet(List<Note> noteList, Long parentNoteID) {

    for (Note note : noteList) {
      if (DbHelper.getInstance().isNoteParentLinkedSet(note.getHandleID())) {
        List<NoteLink> linkedNotes = getLinkedNotes(note.getHandleID(), COL_LINKED_SET_TEXT_ORDER, "", true);
        insertLinkedNotesToPrayerSet(linkedNotes, parentNoteID);
      } else {
        NoteLink linkedNote = new NoteLink();
        linkedNote.setTextIdRef(note.getHandleID());
        linkedNote.setTextOrder(9999);
        linkedNote.setCategory(0);
        linkedNote.setTextType(0);
        insertNoteToLinkedSet(parentNoteID, linkedNote);
      }
    }
  }

  public boolean isNoteParentLinkedSet(Long parentNoteID)
  {
    boolean result = false;

    String sql = " select " + COL_HANDLE_ID +  ", " + COL_PRAYER_MERGED +
            " FROM ( " +
            " select * from " + ATTCH_PRAYERS +
            " UNION " +
            " select * from " + MAIN_PRAYERS +
            " ) foo " +
            " WHERE " + COL_HANDLE_ID + " = " + parentNoteID;

    try (Cursor cursor = getDatabase().rawQuery(sql, null)) {
      // only one row is expected
      if (cursor.moveToFirst()) {
        long mergedType = cursor.getLong(cursor.getColumnIndex(COL_PRAYER_MERGED));
        if (mergedType == PRAYER_MERGED_LINKED_SET) {
          result = true;
        }
      }
    }

  return  result;
  }

  public Note updatePrayerSetNoteContent(Note note, Long parentNoteID) {

    Note result;

    List<NoteLink> linkedNotes = getLinkedNotes(parentNoteID, COL_LINKED_SET_TEXT_ORDER, "", true);

    String noteContent = "";
    for (NoteLink linkedNote : linkedNotes) {

      String title = GetPrayerSetNoteCaption(linkedNote.getTextIdRef(), linkedNote.getCategory());

      if (noteContent.isEmpty()) {
        noteContent = title;
      } else {
        noteContent = noteContent + ", " + title;
      }
    }

    Note parentNote;
    if (note == null)
    {
      parentNote = getNote(parentNoteID);
    } else {
      parentNote = note;
    }

    parentNote.setContent(noteContent);
    parentNote.setHTMLContent(noteContent);
    result = updateNote(parentNote, false);
    return result;
  }

}
