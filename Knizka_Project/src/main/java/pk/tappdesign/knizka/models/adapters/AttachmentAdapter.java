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

import static pk.tappdesign.knizka.utils.ConstantsBase.DATE_FORMAT_SORTABLE;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_AUDIO;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_FILES;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.helpers.date.DateHelper;
import pk.tappdesign.knizka.models.Attachment;
import pk.tappdesign.knizka.models.views.ExpandableHeightGridView;
import pk.tappdesign.knizka.models.views.SquareImageView;
import pk.tappdesign.knizka.utils.BitmapHelper;
import pk.tappdesign.knizka.utils.Constants;
import pk.tappdesign.knizka.utils.date.DateUtils;
import pk.tappdesign.knizka.utils.Fonts;

public class AttachmentAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<Attachment> attachmentsList;
    private LayoutInflater inflater;


    public AttachmentAdapter(Activity mActivity, List<Attachment> attachmentsList, ExpandableHeightGridView mGridView) {
        this.mActivity = mActivity;
		if (attachmentsList == null) {
			attachmentsList = Collections.emptyList();
		}
		this.attachmentsList = attachmentsList;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return attachmentsList.size();
    }


    public Attachment getItem(int position) {
        return attachmentsList.get(position);
    }


    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        LogDelegate.v("GridView called for position " + position);

        Attachment mAttachment = attachmentsList.get(position);

        AttachmentHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);

            // Overrides font sizes with the one selected from user
            Fonts.overrideTextSize(mActivity, mActivity.getSharedPreferences(Constants.PREFS_NAME,
                    Context.MODE_MULTI_PROCESS), convertView);

            holder = new AttachmentHolder();
            holder.image = (SquareImageView) convertView.findViewById(R.id.gridview_item_picture);
            holder.text = (TextView) convertView.findViewById(R.id.gridview_item_text);
            convertView.setTag(holder);
        } else {
            holder = (AttachmentHolder) convertView.getTag();
        }

        // Draw name in case the type is an audio recording
        if (mAttachment.getMime_type() != null && mAttachment.getMime_type().equals(MIME_TYPE_AUDIO)) {
            String text;

            if (mAttachment.getLength() > 0) {
                // Recording duration
                text = DateHelper.formatShortTime(mActivity, mAttachment.getLength());
            } else {
                // Recording date otherwise
                text = DateUtils.getLocalizedDateTime(mActivity, mAttachment
                        .getUri().getLastPathSegment().split("\\.")[0], DATE_FORMAT_SORTABLE);
            }

            if (text == null) {
                text = mActivity.getString(R.string.attachment);
            }
            holder.text.setText(text);
            holder.text.setVisibility(View.VISIBLE);
        } else {
            holder.text.setVisibility(View.GONE);
        }

        // Draw name in case the type is an audio recording (or file in the future)
        if (mAttachment.getMime_type() != null && mAttachment.getMime_type().equals(MIME_TYPE_FILES)) {
            holder.text.setText(mAttachment.getName());
            holder.text.setVisibility(View.VISIBLE);
        }

        // Starts the AsyncTask to draw bitmap into ImageView
        Uri thumbnailUri = BitmapHelper.getThumbnailUri(mActivity, mAttachment);
        Glide.with(mActivity.getApplicationContext())
                .load(thumbnailUri)
                .into(holder.image);

        return convertView;
    }


	public List<Attachment> getAttachmentsList() {
        return attachmentsList;
	}




    public class AttachmentHolder {

        TextView text;
        SquareImageView image;
    }


}
