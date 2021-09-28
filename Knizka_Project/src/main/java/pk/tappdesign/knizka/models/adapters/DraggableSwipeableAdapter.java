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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemState;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemState;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
//import com.h6ah4i.android.widget.advrecyclerview.utils.DrawableUtils;

import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.commons.utils.DrawableUtils;
import pk.tappdesign.knizka.commons.utils.ViewUtils;
import pk.tappdesign.knizka.models.data.ItemAbstractDataProvider;

public class DraggableSwipeableAdapter
        extends RecyclerView.Adapter<DraggableSwipeableAdapter.MyViewHolder>
        implements DraggableItemAdapter<DraggableSwipeableAdapter.MyViewHolder>,
        SwipeableItemAdapter<DraggableSwipeableAdapter.MyViewHolder> {
   private static final String TAG = "MyDSItemAdapter";

   // NOTE: Make accessible with short name
   private interface Swipeable extends SwipeableItemConstants {
   }

   private ItemAbstractDataProvider mProvider;
   private EventListener mEventListener;
   private View.OnClickListener mItemViewOnClickListener;
   private View.OnClickListener mSwipeableViewContainerOnClickListener;
   private View.OnClickListener mUnderSwipeableViewButtonOnClickListener;

   public interface EventListener {
      void onItemRemoved(int position);

      void onItemPinned(int position);

      void onItemViewClicked(View v, boolean pinned);

      void onUnderSwipeableViewButtonClicked(View v);
   }

   public static class MyViewHolder extends AbstractDraggableSwipeableItemViewHolder {
      public FrameLayout mContainer;
      public View mDragHandle;
      public TextView mTextView;
      public RelativeLayout mBehindViews;
      public Button mButton;

      public MyViewHolder(View v) {
         super(v);
         mContainer = v.findViewById(R.id.container);
         mDragHandle = v.findViewById(R.id.drag_handle);
         mTextView = v.findViewById(android.R.id.text1);
         mBehindViews = v.findViewById(R.id.behind_views);
         mButton = v.findViewById(android.R.id.button1);
      }

      @Override
      @NonNull
      public View getSwipeableContainerView() {
         return mContainer;
      }
   }

   public DraggableSwipeableAdapter(ItemAbstractDataProvider dataProvider) {
      mProvider = dataProvider;
      mItemViewOnClickListener = v -> onItemViewClick(v);
      mSwipeableViewContainerOnClickListener = v -> onSwipeableViewContainerClick(v);
      mUnderSwipeableViewButtonOnClickListener = v -> onUnderSwipeableViewButtonClick(v);

      // DraggableItemAdapter and SwipeableItemAdapter require stable ID, and also
      // have to implement the getItemId() method appropriately.
      setHasStableIds(true);
   }

   private void onItemViewClick(View v) {
      // nothing to do... we do not allow click on half opened area of item (there is a button)
      //if (mEventListener != null) {
      //   mEventListener.onItemViewClicked(v, true); // true --- pinned
      //}
   }

   private void onSwipeableViewContainerClick(View v) {
      if (mEventListener != null) {
         mEventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), false);  // false --- not pinned
      }
   }

   private void onUnderSwipeableViewButtonClick(View v) {
      if (mEventListener != null) {
         mEventListener.onUnderSwipeableViewButtonClicked(
                 RecyclerViewAdapterUtils.getParentViewHolderItemView(v));
      }
   }

   @Override
   public long getItemId(int position) {
      return mProvider.getItem(position).getId();
   }

   @Override
   public int getItemViewType(int position) {
      return mProvider.getItem(position).getViewType();
   }

   @NonNull
   @Override
   public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//      final View v = inflater.inflate((viewType == 0) ? R.layout.list_item_draggable : R.layout.list_item2_draggable, parent, false);
      final View v = inflater.inflate((viewType == 0) ? R.layout.list_item_draggable : R.layout.list_item_draggable, parent, false);

      return new MyViewHolder(v);
   }

   @Override
   public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      final ItemAbstractDataProvider.Data item = mProvider.getItem(position);

      // set listeners
      // (if the item is *pinned*, click event comes to the itemView)
      holder.itemView.setOnClickListener(mItemViewOnClickListener);
      // (if the item is *not pinned*, click event comes to the mContainer)
      holder.mContainer.setOnClickListener(mSwipeableViewContainerOnClickListener);

      holder.mButton.setOnClickListener(mUnderSwipeableViewButtonOnClickListener);

      // set text
      holder.mTextView.setText(item.getText());

      // set background resource (target view ID: container)
      final DraggableItemState dragState = holder.getDragState();
      final SwipeableItemState swipeState = holder.getSwipeState();

      if (dragState.isUpdated() || swipeState.isUpdated()) {
         int bgResId;

         if (dragState.isActive()) {
            bgResId = R.drawable.bg_item_dragging_active_state;

            // need to clear drawable state here to get correct appearance of the dragging item.
            DrawableUtils.clearState(holder.mContainer.getForeground());
         } else if (dragState.isDragging()) {
            //bgResId = R.drawable.bg_item_dragging_state;
            bgResId = R.drawable.bg_item_normal_state;  // do not change color of dragged item
         } else if (swipeState.isActive()) {
            //bgResId = R.drawable.bg_item_swiping_active_state;
            bgResId = R.drawable.bg_item_normal_state;  // do not change color of swipped item
         } else if (swipeState.isSwiping()) {
            //bgResId = R.drawable.bg_item_swiping_state;
            bgResId = R.drawable.bg_item_normal_state;
         } else {
            bgResId = R.drawable.bg_item_normal_state;
         }

         holder.mContainer.setBackgroundResource(bgResId);
      }

      // set swiping properties
      holder.setMaxLeftSwipeAmount(-0.5f);
      holder.setMaxRightSwipeAmount(0);
      holder.setSwipeItemHorizontalSlideAmount(item.isPinned() ? -0.5f : 0);


      //holder.setSwipeItemHorizontalSlideAmount(
        //      item.isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
   }

   @Override
   public int getItemCount() {
      return mProvider.getCount();
   }

   @Override
   public void onMoveItem(int fromPosition, int toPosition) {
    //  Log.d(TAG, "onMoveItem(fromPosition = " + fromPosition + ", toPosition = " + toPosition + ")");

      mProvider.moveItem(fromPosition, toPosition);
   }

   @Override
   public boolean onCheckCanStartDrag(@NonNull MyViewHolder holder, int position, int x, int y) {
      // x, y --- relative from the itemView's top-left
      final View containerView = holder.mContainer;
      final View dragHandleView = holder.mDragHandle;

      final int offsetX = containerView.getLeft() + (int) (containerView.getTranslationX() + 0.5f);
      final int offsetY = containerView.getTop() + (int) (containerView.getTranslationY() + 0.5f);

      return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
   }

   @Override
   public ItemDraggableRange onGetItemDraggableRange(@NonNull MyViewHolder holder, int position) {
      // no drag-sortable range specified
      return null;
   }

   @Override
   public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
      return true;
   }

   @Override
   public void onItemDragStarted(int position) {
      notifyDataSetChanged();
   }

   @Override
   public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
      notifyDataSetChanged();
   }

   @Override
   public int onGetSwipeReactionType(@NonNull MyViewHolder holder, int position, int x, int y) {
      if (onCheckCanStartDrag(holder, position, x, y)) {
         return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
      } else {
         return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
      }
   }

   @Override
   public void onSwipeItemStarted(@NonNull MyViewHolder holder, int position) {
      notifyDataSetChanged();
   }

   @Override
   public void onSetSwipeBackground(@NonNull MyViewHolder holder, int position, int type) {
      int bgRes = 0;
      switch (type) {
         case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
            bgRes = R.drawable.bg_swipe_item_neutral;
            holder.mBehindViews.setVisibility(View.GONE);
            break;
         case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
            bgRes = R.drawable.bg_swipe_item_left;
            holder.mBehindViews.setVisibility(View.VISIBLE);
            break;
         case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
            bgRes = R.drawable.bg_swipe_item_right;
            break;
      }

      holder.itemView.setBackgroundResource(bgRes);
   }

   @Override
   public SwipeResultAction onSwipeItem(@NonNull MyViewHolder holder, final int position, int result) {
    //  Log.d(TAG, "onSwipeItem(position = " + position + ", result = " + result + ")");

      switch (result) {
         // swipe right
         case Swipeable.RESULT_SWIPED_RIGHT:
            if (mProvider.getItem(position).isPinned()) {
               // pinned --- back to default position
               return new UnpinResultAction(this, position);
            } else {
               // not pinned --- remove
               //return new SwipeRightResultAction(this, position);
               return new UnpinResultAction(this, position);
            }
            // swipe left -- pin
         case Swipeable.RESULT_SWIPED_LEFT:
            return new SwipeLeftResultAction(this, position);
         // other --- do nothing
         case Swipeable.RESULT_CANCELED:
         default:
            if (position != RecyclerView.NO_POSITION) {
               return new UnpinResultAction(this, position);
            } else {
               return null;
            }
      }
   }

   public EventListener getEventListener() {
      return mEventListener;
   }

   public void setEventListener(EventListener eventListener) {
      mEventListener = eventListener;
   }

   private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
      private DraggableSwipeableAdapter mAdapter;
      private final int mPosition;
      private boolean mSetPinned;

      SwipeLeftResultAction(DraggableSwipeableAdapter adapter, int position) {
         mAdapter = adapter;
         mPosition = position;
      }

      @Override
      protected void onPerformAction() {
         super.onPerformAction();

         ItemAbstractDataProvider.Data item = mAdapter.mProvider.getItem(mPosition);

         if (!item.isPinned()) {
            item.setPinned(true);
            mAdapter.notifyItemChanged(mPosition);
            mSetPinned = true;
         }
      }

      @Override
      protected void onSlideAnimationEnd() {
         super.onSlideAnimationEnd();

         if (mSetPinned && mAdapter.mEventListener != null) {
            mAdapter.mEventListener.onItemPinned(mPosition);
         }
      }



      @Override
      protected void onCleanUp() {
         super.onCleanUp();
         // clear the references
         mAdapter = null;
      }
   }

   private static class SwipeRightResultAction extends SwipeResultActionRemoveItem {
      private DraggableSwipeableAdapter mAdapter;
      private final int mPosition;

      SwipeRightResultAction(DraggableSwipeableAdapter adapter, int position) {
         mAdapter = adapter;
         mPosition = position;
      }

      @Override
      protected void onPerformAction() {
         super.onPerformAction();

         mAdapter.mProvider.removeItem(mPosition);
         mAdapter.notifyItemRemoved(mPosition);
      }

      @Override
      protected void onSlideAnimationEnd() {
         super.onSlideAnimationEnd();

         if (mAdapter.mEventListener != null) {
            mAdapter.mEventListener.onItemRemoved(mPosition);
         }
      }

      @Override
      protected void onCleanUp() {
         super.onCleanUp();
         // clear the references
         mAdapter = null;
      }
   }

   private static class UnpinResultAction extends SwipeResultActionDefault {
      private DraggableSwipeableAdapter mAdapter;
      private final int mPosition;

      UnpinResultAction(DraggableSwipeableAdapter adapter, int position) {
         mAdapter = adapter;
         mPosition = position;
      }

      @Override
      protected void onPerformAction() {
         super.onPerformAction();

         ItemAbstractDataProvider.Data item = mAdapter.mProvider.getItem(mPosition);
         if (item.isPinned()) {
            item.setPinned(false);
            mAdapter.notifyItemChanged(mPosition);
         }
      }

      @Override
      protected void onCleanUp() {
         super.onCleanUp();
         // clear the references
         mAdapter = null;
      }
   }
}

