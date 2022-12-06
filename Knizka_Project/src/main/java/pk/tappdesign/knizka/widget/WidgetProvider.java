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


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_WIDGET;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_WIDGET_SHOW_LIST;
import static pk.tappdesign.knizka.utils.ConstantsBase.ACTION_WIDGET_TAKE_PHOTO;
import static pk.tappdesign.knizka.utils.ConstantsBase.INTENT_WIDGET;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.RemoteViews;

import pk.tappdesign.knizka.MainActivity;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.helpers.notifications.NotificationsHelper;


public abstract class WidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_WORD = "pk.tappdesign.knizka.widget.WORD";
    public static final String TOAST_ACTION = "pk.tappdesign.knizka.widget.NOTE";
    public static final String EXTRA_ITEM = "pk.tappdesign.knizka.widget.EXTRA_FIELD";


  @Override
  public void onUpdate (Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // Get all ids
    ComponentName thisWidget = new ComponentName(context, getClass());
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    for (int appWidgetId : allWidgetIds) {
      LogDelegate.d("WidgetProvider onUpdate() widget " + appWidgetId);
      // Get the layout for and attach an on-click listener to views
      setLayout(context, appWidgetManager, appWidgetId);
    }
    super.onUpdate(context, appWidgetManager, appWidgetIds);
  }


  @Override
  public void onAppWidgetOptionsChanged (Context context, AppWidgetManager appWidgetManager, int appWidgetId,
      Bundle newOptions) {
    LogDelegate.d("Widget size changed");
    setLayout(context, appWidgetManager, appWidgetId);
  }


  private void setLayout (Context context, AppWidgetManager appWidgetManager, int widgetId) {

    // Create an Intent to launch DetailActivity
    Intent intentDetail = new Intent(context, MainActivity.class);
    intentDetail.setAction(ACTION_WIDGET);
    intentDetail.putExtra(INTENT_WIDGET, widgetId);
    PendingIntent pendingIntentDetail = NotificationsHelper.createPendingIntent(context, widgetId, intentDetail, FLAG_ACTIVITY_NEW_TASK);


    // Create an Intent to launch ListActivity
    Intent intentList = new Intent(context, MainActivity.class);
    intentList.setAction(ACTION_WIDGET_SHOW_LIST);
    intentList.putExtra(INTENT_WIDGET, widgetId);
    PendingIntent pendingIntentList = NotificationsHelper.createPendingIntent(context, widgetId, intentList, FLAG_ACTIVITY_NEW_TASK);

    // Create an Intent to launch DetailActivity to take a photo
    Intent intentDetailPhoto = new Intent(context, MainActivity.class);
    intentDetailPhoto.setAction(ACTION_WIDGET_TAKE_PHOTO);
    intentDetailPhoto.putExtra(INTENT_WIDGET, widgetId);
    PendingIntent pendingIntentDetailPhoto = NotificationsHelper.createPendingIntent(context, widgetId, intentDetailPhoto, FLAG_ACTIVITY_NEW_TASK);

    // Check various dimensions aspect of widget to choose between layouts
    boolean isSmall = false;
    boolean isSingleLine = true;
    Bundle options = appWidgetManager.getAppWidgetOptions(widgetId);
    // Width check
    isSmall = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) < 110;
    // Height check
    isSingleLine = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) < 110;

    // Creation of a map to associate PendingIntent(s) to views
    SparseArray<PendingIntent> map = new SparseArray<>();
    map.put(R.id.list, pendingIntentList);
    map.put(R.id.add, pendingIntentDetail);
    map.put(R.id.camera, pendingIntentDetailPhoto);

    RemoteViews views = getRemoteViews(context, widgetId, isSmall, isSingleLine, map);

    // Tell the AppWidgetManager to perform an update on the current app widget
    appWidgetManager.updateAppWidget(widgetId, views);
  }


  abstract protected RemoteViews getRemoteViews (Context context, int widgetId, boolean isSmall, boolean isSingleLine,
      SparseArray<PendingIntent> pendingIntentsMap);

}
