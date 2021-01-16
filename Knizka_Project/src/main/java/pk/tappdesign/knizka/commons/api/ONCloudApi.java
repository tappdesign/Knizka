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

package pk.tappdesign.knizka.commons.api;

import pk.tappdesign.knizka.commons.listeners.DataRetrievedListener;
import pk.tappdesign.knizka.commons.models.BaseAttachment;
import pk.tappdesign.knizka.commons.models.BaseCategory;
import pk.tappdesign.knizka.commons.models.BaseNote;

public interface ONCloudApi {

    public static final String NOTES_FOLDER = "notes";
    public static final String CATEGORIES_FOLDER = "categories";
    public static final String ATTACHMENTS_FOLDER = "attachments";

    public void getNotes(DataRetrievedListener dataRetrievedListener);
    public BaseNote getNote(String noteId);
    public void putNote(BaseNote baseNote);
    public void delNote(BaseNote baseNote);

    public void getCategories(DataRetrievedListener dataRetrievedListener);
    public BaseCategory getCategory(String categoryId);
    public void putCategory(BaseCategory baseCategory);
    public void delCategory(BaseCategory baseCategory);

    public void getAttachments(DataRetrievedListener dataRetrievedListener);
    public BaseAttachment getAttachment(String attachmentId);
    public void putAttachment(BaseAttachment baseAttachment);
    public void delAttachment(BaseAttachment baseAttachment);

    public void purge();
}

