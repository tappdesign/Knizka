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

package pk.tappdesign.knizka.models.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

public class PrayerSetItemDataProvider extends ItemAbstractDataProvider implements Parcelable {
   private List<ConcreteData> mData;
   private ConcreteData mLastRemovedData;
   private int mLastRemovedPosition = -1;

   public PrayerSetItemDataProvider() {
      final String atoz = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

      mData = new LinkedList<>();

      for (int i = 0; i < 2; i++) {
         for (int j = 0; j < atoz.length(); j++) {
            final long id = mData.size();
            final int viewType = 0;
            final String text = Character.toString(atoz.charAt(j));
            final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN;
            mData.add(new ConcreteData(id, viewType, text, swipeReaction));
         }
      }
   }

   protected PrayerSetItemDataProvider(Parcel in) {
      mData = in.createTypedArrayList(ConcreteData.CREATOR);
      mLastRemovedData = in.readParcelable(ConcreteData.class.getClassLoader());
      mLastRemovedPosition = in.readInt();
   }

   public static final Creator<PrayerSetItemDataProvider> CREATOR = new Creator<PrayerSetItemDataProvider>() {
      @Override
      public PrayerSetItemDataProvider createFromParcel(Parcel in) {
         return new PrayerSetItemDataProvider(in);
      }

      @Override
      public PrayerSetItemDataProvider[] newArray(int size) {
         return new PrayerSetItemDataProvider[size];
      }
   };

   @Override
   public int getCount() {
      return mData.size();
   }

   @Override
   public Data getItem(int index) {
      if (index < 0 || index >= getCount()) {
         throw new IndexOutOfBoundsException("index = " + index);
      }

      return mData.get(index);
   }

   @Override
   public int undoLastRemoval() {
      if (mLastRemovedData != null) {
         int insertedPosition;
         if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
            insertedPosition = mLastRemovedPosition;
         } else {
            insertedPosition = mData.size();
         }

         mData.add(insertedPosition, mLastRemovedData);

         mLastRemovedData = null;
         mLastRemovedPosition = -1;

         return insertedPosition;
      } else {
         return -1;
      }
   }

   @Override
   public void moveItem(int fromPosition, int toPosition) {
      if (fromPosition == toPosition) {
         return;
      }

      final ConcreteData item = mData.remove(fromPosition);

      mData.add(toPosition, item);
      mLastRemovedPosition = -1;
   }

   @Override
   public void swapItem(int fromPosition, int toPosition) {
      if (fromPosition == toPosition) {
         return;
      }

      Collections.swap(mData, toPosition, fromPosition);
      mLastRemovedPosition = -1;
   }

   @Override
   public void removeItem(int position) {
      //noinspection UnnecessaryLocalVariable
      final ConcreteData removedItem = mData.remove(position);

      mLastRemovedData = removedItem;
      mLastRemovedPosition = position;
   }

   public static final class ConcreteData extends Data implements Parcelable {

      private final long mId;
      @NonNull
      private final String mText;
      private final int mViewType;
      private boolean mPinned;

      ConcreteData(long id, int viewType, @NonNull String text, int swipeReaction) {
         mId = id;
         mViewType = viewType;
         mText = makeText(id, text, swipeReaction);
      }

      public static final Creator<ConcreteData> CREATOR = new Creator<ConcreteData>() {
         @Override
         public ConcreteData createFromParcel(Parcel in) {
            return new ConcreteData(in);
         }

         @Override
         public ConcreteData[] newArray(int size) {
            return new ConcreteData[size];
         }
      };

      private static String makeText(long id, String text, int swipeReaction) {
         return String.valueOf(id) + " - " + text;
      }

      @Override
      public boolean isSectionHeader() {
         return false;
      }

      @Override
      public int getViewType() {
         return mViewType;
      }

      @Override
      public long getId() {
         return mId;
      }

      @NonNull
      @Override
      public String toString() {
         return mText;
      }

      @Override
      public String getText() {
         return mText;
      }

      @Override
      public boolean isPinned() {
         return mPinned;
      }

      @Override
      public void setPinned(boolean pinned) {
         mPinned = pinned;
      }

      @Override
      public int describeContents() {
         return 0;
      }

      @Override
      public void writeToParcel(Parcel parcel, int flags) {
         parcel.writeLong(mId);
         parcel.writeString(mText);
         parcel.writeInt(mViewType);
         parcel.writeInt(mPinned ? 1 : 0);
      }

      private ConcreteData(Parcel in) {
         mId = in.readLong();
         mText = in.readString();
         mViewType = in.readInt();
         mPinned = in.readInt() == 1;
      }
   }

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel parcel, int flags) {
      parcel.writeList(mData);
      parcel.writeParcelable(mLastRemovedData, 0);
      parcel.writeInt(mLastRemovedPosition);
   }
}
