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

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import pk.tappdesign.knizka.models.WebViewTouchListener;

public class OnSwipeWebviewTouchListener implements View.OnTouchListener {
   private final GestureDetector gestureDetector;
   public OnSwipeWebviewTouchListener(Context ctx, WebViewTouchListener touchListener) {
      gestureDetector = new GestureDetector(ctx, new GestureListener(touchListener));
   }
   @Override
   public boolean onTouch(View v, MotionEvent event) {
      return gestureDetector.onTouchEvent(event);
   }
   private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
      private WebViewTouchListener touchListener;
      GestureListener(WebViewTouchListener touchListener) {
         super();
         this.touchListener = touchListener;
      }
      @Override
      public boolean onDown(MotionEvent e) {
         return false;  // THIS does the trick
      }

      @Override
      public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
         boolean result = false;
         try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
               // You can customize these settings, so 30 is an example
               if (Math.abs(diffX) > 30 && Math.abs(velocityX) > 30) {
                  if (diffX > 0) {
                     touchListener.onSwipeRight();
                  } else {
                     touchListener.onSwipeLeft();
                  }
                  result = true;
               }
            } else {
              // todo: vertical fling could be caught here
            }
         } catch (Exception exception) {
            exception.printStackTrace();
         }
         return result;
      }
   }
}