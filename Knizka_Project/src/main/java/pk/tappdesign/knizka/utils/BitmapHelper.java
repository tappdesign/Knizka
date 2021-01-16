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


import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_AUDIO;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_CONTACT_EXT;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_FILES;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_IMAGE;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_SKETCH;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_VIDEO;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.helpers.AttachmentsHelper;
import pk.tappdesign.knizka.models.Attachment;
import it.feio.android.simplegallery.util.BitmapUtils;
import java.util.concurrent.ExecutionException;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;

@UtilityClass
public class BitmapHelper {

	private static final String ANDROID_RESOURCE = "android.resource://";

  /**
   * Retrieves a the bitmap relative to attachment based on mime type
   */
  public static Bitmap getBitmapFromAttachment(Context mContext, Attachment mAttachment, int width,
      int height) {
    Bitmap bmp = null;

    if (AttachmentsHelper.typeOf(mAttachment, MIME_TYPE_VIDEO, MIME_TYPE_IMAGE, MIME_TYPE_SKETCH)) {
      bmp = getImageBitmap(mContext, mAttachment, width, height);

    } else if (MIME_TYPE_AUDIO.equals(mAttachment.getMime_type())) {
      bmp = ThumbnailUtils.extractThumbnail(
          BitmapUtils
              .decodeSampledBitmapFromResourceMemOpt(mContext.getResources().openRawResource(R
                  .raw.play), width, height), width, height);

    } else if (MIME_TYPE_FILES.equals(mAttachment.getMime_type())) {
      if (MIME_TYPE_CONTACT_EXT.equals(FilenameUtils.getExtension(mAttachment.getName()))) {
        bmp = ThumbnailUtils.extractThumbnail(
            BitmapUtils
                .decodeSampledBitmapFromResourceMemOpt(mContext.getResources().openRawResource(R
                    .raw.vcard), width, height), width, height);
      } else {
        bmp = ThumbnailUtils.extractThumbnail(
            BitmapUtils
                .decodeSampledBitmapFromResourceMemOpt(mContext.getResources().openRawResource(R
                    .raw.files), width, height), width, height);
      }
    }

    return bmp;
  }

  private static Bitmap getImageBitmap(Context mContext, Attachment mAttachment, int width,
      int height) {
    try {
      if (Looper.getMainLooper() == Looper.myLooper()) {
        return BitmapUtils.getThumbnail(mContext, mAttachment.getUri(), width, height);
      } else {
        return Glide.with(Knizka.getAppContext()).asBitmap()
            .apply(new RequestOptions()
                .centerCrop()
                .error(R.drawable.attachment_broken))
            .load(mAttachment.getUri())
            .submit(width, height).get();
      }
    } catch (NullPointerException | ExecutionException e) {
      return null;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    }
  }

  public static Uri getThumbnailUri(Context mContext, Attachment mAttachment) {
    Uri uri = mAttachment.getUri();
    String mimeType = StorageHelper.getMimeType(uri.toString());
    if (!TextUtils.isEmpty(mimeType)) {
      String type = mimeType.split("/")[0];
      String subtype = mimeType.split("/")[1];
      switch (type) {
        case "image":
        case "video":
          // Nothing to do, bitmap will be retrieved from this
          break;
        case "audio":
          uri = Uri.parse(ANDROID_RESOURCE + mContext.getPackageName() + "/" + R.raw.play);
          break;
        default:
          int drawable = "x-vcard".equals(subtype) ? R.raw.vcard : R.raw.files;
          uri = Uri.parse(ANDROID_RESOURCE + mContext.getPackageName() + "/" + drawable);
          break;
      }
    } else {
      uri = Uri.parse(ANDROID_RESOURCE + mContext.getPackageName() + "/" + R.raw.files);
    }
    return uri;
  }
}
