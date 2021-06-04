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

import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_NOTE;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_COLORS_APP_DEFAULT;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_WIDGET_PREFIX;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.models.Attachment;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.utils.BitmapHelper;

import pk.tappdesign.knizka.utils.Navigation;
import pk.tappdesign.knizka.utils.TextHelper;



public class ListRemoteViewsFactory implements RemoteViewsFactory {

    private static final String SET_BACKGROUND_COLOR = "setBackgroundColor";
    private static boolean showThumbnails = true;
    private static boolean showTimestamps = true;
    private final int WIDTH = 80;
    private final int HEIGHT = 80;
    private Knizka app;
    private int appWidgetId;
    private List<Note> notes;
    private int navigation;


    public ListRemoteViewsFactory(Application app, Intent intent) {
        this.app = (Knizka) app;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    static void updateConfiguration (Context mContext, int mAppWidgetId, String sqlCondition,
                                           boolean thumbnails, boolean timestamps) {
        LogDelegate.d("Widget configuration updated");
        Prefs.edit().putString(PREF_WIDGET_PREFIX + mAppWidgetId, sqlCondition).apply();
        showThumbnails = thumbnails;
        showTimestamps = timestamps;
    }

    @Override
    public void onCreate() {
        LogDelegate.d("Created widget " + appWidgetId);
        String condition = Prefs.getString(
                        PREF_WIDGET_PREFIX
                                + String.valueOf(appWidgetId), "");
        notes = DbHelper.getInstance().getNotes(condition, "", "", true);
    }

    @Override
    public void onDataSetChanged() {
        LogDelegate.d("onDataSetChanged widget " + appWidgetId);
        navigation = Navigation.getNavigation();

        String condition = Prefs.getString(
                        PREF_WIDGET_PREFIX
                                + String.valueOf(appWidgetId), "");
        notes = DbHelper.getInstance().getNotes(condition, "", "", true);
    }

    @Override
    public void onDestroy() {
        Prefs.edit().remove(PREF_WIDGET_PREFIX + appWidgetId).apply();
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(app.getPackageName(), R.layout.note_layout_widget);

        Note note = notes.get(position);

        Spanned[] titleAndContent = TextHelper.parseTitleAndContent(app, note);

        row.setTextViewText(R.id.note_title, titleAndContent[0]);
        row.setTextViewText(R.id.note_content, titleAndContent[1]);

        color(note, row);

        if (!note.isLocked() && showThumbnails && !note.getAttachmentsList().isEmpty()) {
      			Attachment mAttachment = note.getAttachmentsList().get(0);

            	Bitmap bmp = BitmapHelper.getBitmapFromAttachment(app, mAttachment, WIDTH, HEIGHT);
      			row.setBitmap(R.id.attachmentThumbnail, "setImageBitmap", bmp);
            row.setInt(R.id.attachmentThumbnail, "setVisibility", View.VISIBLE);
        } else {
            row.setInt(R.id.attachmentThumbnail, "setVisibility", View.GONE);
        }
        if(showTimestamps) {
          row.setTextViewText(R.id.note_date, TextHelper.getDateText(app, note, navigation));
        } else {
          row.setTextViewText(R.id.note_date, "");
        }

        // Next, set a fill-intent, which will be used to fill in the pending intent template
        // that is set on the collection view in StackWidgetProvider.
        Bundle extras = new Bundle();
        extras.putParcelable(INTENT_NOTE, note);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        row.setOnClickFillInIntent(R.id.root, fillInIntent);

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void color(Note note, RemoteViews row) {

        String colorsPref = Prefs.getString("settings_colors_widget",
                        PREF_COLORS_APP_DEFAULT);

        // Checking preference
        if (!colorsPref.equals("disabled")) {

            // Resetting transparent color to the view
            row.setInt(R.id.tag_marker, SET_BACKGROUND_COLOR, Color.parseColor("#00000000"));

            // If tag is set the color will be applied on the appropriate target
            if (note.getCategory() != null && note.getCategory().getColor() != null) {
                if (colorsPref.equals("list")) {
                    row.setInt(R.id.card_layout, SET_BACKGROUND_COLOR, Integer.parseInt(note.getCategory().getColor()));
                } else {
                    row.setInt(R.id.tag_marker, SET_BACKGROUND_COLOR, Integer.parseInt(note.getCategory().getColor()));
                }
            } else {
                row.setInt(R.id.tag_marker, SET_BACKGROUND_COLOR, 0);
            }
        }
    }

}
