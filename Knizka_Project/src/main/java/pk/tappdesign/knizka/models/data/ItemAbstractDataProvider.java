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

public abstract class ItemAbstractDataProvider {

   public static abstract class Data {
      public abstract long getId();

      public abstract boolean isSectionHeader();

      public abstract int getViewType();

      public abstract String getText();

      public abstract void setPinned(boolean pinned);

      public abstract boolean isPinned();

      public abstract long getTextRefId();

      public abstract long getHandleIdRef();

      public abstract int getTextType();

      public abstract int getCategoryType();

   }

   public abstract int getCount();

   public abstract Data getItem(int index);

   public abstract void removeItem(int position);

   public abstract void moveItem(int fromPosition, int toPosition);

   public abstract void swapItem(int fromPosition, int toPosition);

   public abstract int undoLastRemoval();

   public abstract void clear();

   public abstract void addItem(PrayerSetItemDataProvider.ConcreteData item);

}

