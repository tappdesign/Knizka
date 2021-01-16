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

package pk.tappdesign.knizka.models.holders;


import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.neopixl.pixlui.components.textview.TextView;
import pk.tappdesign.knizka.databinding.NoteLayoutBinding;
import pk.tappdesign.knizka.databinding.NoteLayoutExpandedBinding;
import pk.tappdesign.knizka.models.views.SquareImageView;
import pk.tappdesign.knizka.R;

public class NoteViewHolder extends ViewHolder {

  public View root;
  public View cardLayout;
  public View categoryMarker;

  public TextView title;
  public TextView content;
  public TextView date;

  public ImageView archiveIcon;
  public ImageView locationIcon;
  public ImageView alarmIcon;
  public ImageView lockedIcon;
  @Nullable
  public ImageView attachmentIcon;
  @Nullable
  public SquareImageView attachmentThumbnail;

  public NoteViewHolder (View view, boolean expandedView) {
    super(view);

    if (expandedView) {
      NoteLayoutExpandedBinding binding = NoteLayoutExpandedBinding.bind(view);
      root = binding.root;
      cardLayout = binding.cardLayout;
      categoryMarker = binding.categoryMarker;
      title = binding.noteTitle;
      content = binding.noteContent;
      date = binding.noteDate;
      archiveIcon = binding.archivedIcon;
      locationIcon = binding.locationIcon;
      alarmIcon = binding.alarmIcon;
      lockedIcon = binding.lockedIcon;
      attachmentThumbnail = binding.attachmentThumbnail;
      lockedIcon = binding.lockedIcon;
      lockedIcon = binding.lockedIcon;
    } else {
      NoteLayoutBinding binding = NoteLayoutBinding.bind(view);
      root = binding.root;
      cardLayout = binding.cardLayout;
      categoryMarker = binding.categoryMarker;
      title = binding.noteTitle;
      content = binding.noteContent;
      date = binding.noteDate;
      archiveIcon = binding.archivedIcon;
      locationIcon = binding.locationIcon;
      alarmIcon = binding.alarmIcon;
      lockedIcon = binding.lockedIcon;
      attachmentIcon = binding.attachmentIcon;
      lockedIcon = binding.lockedIcon;
      lockedIcon = binding.lockedIcon;
    }

  }

}
