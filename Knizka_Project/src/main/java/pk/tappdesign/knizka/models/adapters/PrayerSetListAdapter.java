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

package pk.tappdesign.knizka.models.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;

import java.util.ArrayList;
import java.util.List;

import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.models.PrayerSetListItem;
import pk.tappdesign.knizka.models.holders.PrayerSetItemViewHolder;

public class PrayerSetListAdapter extends RecyclerView.Adapter<PrayerSetItemViewHolder> implements DraggableItemAdapter<PrayerSetItemViewHolder> {
   List<PrayerSetListItem> mItems;

   public PrayerSetListAdapter() {
      setHasStableIds(true); // this is required for D&D feature.

      mItems = new ArrayList<>();
      for (int i = 0; i < 20; i++) {
         mItems.add(new PrayerSetListItem(i, "Item " + i));
      }
   }
   @Override
   public long getItemId(int position) {
      return mItems.get(position).id; // need to return stable (= not change even after reordered) value
   }

   @NonNull
   @Override
   public PrayerSetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_minimal, parent, false);
      return new PrayerSetItemViewHolder(v);
   }

   @Override
   public void onBindViewHolder(@NonNull PrayerSetItemViewHolder holder, int position) {
      PrayerSetListItem item = mItems.get(position);
      holder.textView.setText(item.text);
   }

   @Override
   public int getItemCount() {
      return mItems.size();
   }

   @Override
   public void onMoveItem(int fromPosition, int toPosition) {
      PrayerSetListItem movedItem = mItems.remove(fromPosition);
      mItems.add(toPosition, movedItem);
   }

   @Override
   public boolean onCheckCanStartDrag(@NonNull PrayerSetItemViewHolder holder, int position, int x, int y) {
      return true;
   }

   @Override
   public ItemDraggableRange onGetItemDraggableRange(@NonNull PrayerSetItemViewHolder holder, int position) {
      return null;
   }

   @Override
   public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
      return true;
   }

   @Override
   public void onItemDragStarted(int position) {
   }

   @Override
   public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
   }

}

