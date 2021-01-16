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

package pk.tappdesign.knizka.commons.models;

import com.google.gson.Gson;
import pk.tappdesign.knizka.commons.utils.EqualityChecker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BaseNote implements Serializable {

	private String title;
	private String content;
	private String htmlContent;
	private Long handleID;
	private Boolean favorite;
	private Long packageID;
	private Long prayerSet;
	private String tagList;
	private Long creation;
	private Long lastModification;
	private Boolean archived;
	private Boolean trashed;
	private Boolean erased;
	private String alarm;
	private Boolean reminderFired;
	private String recurrenceRule;
	private Double latitude;
	private Double longitude;
	private String address;
	private BaseCategory baseCategory;
	private Boolean locked;
	private Boolean checklist;
	private List<? extends BaseAttachment> attachmentsList = new ArrayList<>();
	private transient List<? extends BaseAttachment> attachmentsListOld = new ArrayList<>();

	public BaseNote() {
		super();
		this.title = "";
		this.content = "";
		this.htmlContent = "";
		this.tagList = "";
		this.packageID = new Long(-1);
		this.prayerSet = new Long(0);
		this.handleID = new Long(0);
		this.archived = false;
		this.trashed = false;
		this.erased = false;
		this.locked = false;
		this.checklist = false;
	}

	public BaseNote(Long creation, Long lastModification, String title, String content, String htmlContent, Boolean favorite, Long handleID, Long packageID, Long prayerSet, String tagList, Integer archived,
					Integer trashed, Integer erased, String alarm, Integer reminderFired, String recurrenceRule, String latitude, String longitude, BaseCategory

							baseCategory, Integer locked, Integer checklist) {
		super();
		this.title = title;
		this.content = content;
		this.htmlContent = htmlContent;
		this.packageID = packageID;
		this.prayerSet = prayerSet;
		this.tagList = tagList;
		this.favorite = favorite;
		this.handleID = handleID;
		this.creation = creation;
		this.lastModification = lastModification;
		this.archived = archived == 1;
		this.trashed = trashed == 1;
		this.erased = erased == 1;
		this.alarm = alarm;
		this.reminderFired = reminderFired == 1;
		this.recurrenceRule = recurrenceRule;
		setLatitude(latitude);
		setLongitude(longitude);
		this.baseCategory = baseCategory;
		this.locked = locked == 1;
		this.checklist = checklist == 1;
	}

	public BaseNote(BaseNote baseNote) {
		super();
		buildFromNote(baseNote);
	}

	private void buildFromNote(BaseNote baseNote) {
		setTitle(baseNote.getTitle());
		setContent(baseNote.getContent());
		setHTMLContent(baseNote.getHTMLContent());
		setFavorite(baseNote.isFavorite());
		setHandleID(baseNote.getHandleID());
		setPackageID(baseNote.getPackageID());
		setPrayerSet(baseNote.getPrayerSet());
		setTagList(baseNote.getTagList());
		setCreation(baseNote.getCreation());
		setLastModification(baseNote.getLastModification());
		setArchived(baseNote.isArchived());
		setTrashed(baseNote.isTrashed());
		setErased(baseNote.isErased());
		setAlarm(baseNote.getAlarm());
		setRecurrenceRule(baseNote.getRecurrenceRule());
		setReminderFired(baseNote.isReminderFired());
		setLatitude(baseNote.getLatitude());
		setLongitude(baseNote.getLongitude());
		setAddress(baseNote.getAddress());
		setCategory(baseNote.getCategory());
		setLocked(baseNote.isLocked());
		setChecklist(baseNote.isChecklist());
		ArrayList<BaseAttachment> list = new ArrayList<BaseAttachment>();
		for (BaseAttachment mBaseAttachment : baseNote.getAttachmentsList()) {
			list.add(mBaseAttachment);
		}
		setAttachmentsList(list);
	}


	public void buildFromJson(String jsonNote) {
		Gson gson = new Gson();
		BaseNote baseNoteFromJson = gson.fromJson(jsonNote, this.getClass());
		buildFromNote(baseNoteFromJson);
	}


	public void set_id(Long _id) {
		this.creation = _id;
	}


	public Long get_id() {
		return creation;
	}


	public String getTitle() {
		if (title == null) return "";
		return title;
	}


	public void setTitle(String title) {
		this.title = title == null ? "" : title;
	}


	public String getContent() {
		if (content == null) return "";
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? "" : content;
	}

	public String getHTMLContent() {
		if (htmlContent == null) return "";
		return htmlContent;
	}

	public void setHTMLContent(String htmlContent) {
		this.htmlContent = htmlContent == null ? "" : htmlContent;
	}

	public Long getPackageID() {
		return packageID;
	}

	public void setPackageID(Long packageID) {
		this.packageID = packageID;
	}

	public String getTagList() {
		return tagList;
	}

	public void setTagList(String tagList) {
		this.tagList = tagList;
	}

	public Long getPrayerSet() {
		return prayerSet;
	}

	public void setPrayerSet(Long prayerSet) {
		this.prayerSet = prayerSet;
	}

	public Long getHandleID() {
		return handleID;
	}


	public void setHandleID(Long handleID) {
		this.handleID = handleID;
	}


	public Long getCreation() {
		return creation;
	}


	public void setCreation(Long creation) {
		this.creation = creation;
	}


	public void setCreation(String creation) {
		Long creationLong;
		try {
			creationLong = Long.parseLong(creation);
		} catch (NumberFormatException e) {
			creationLong = null;
		}
		this.creation = creationLong;
	}


	public Long getLastModification() {
		return lastModification;
	}


	public void setLastModification(Long lastModification) {
		this.lastModification = lastModification;
	}


	public void setLastModification(String lastModification) {
		Long lastModificationLong;
		try {
			lastModificationLong = Long.parseLong(lastModification);
		} catch (NumberFormatException e) {
			lastModificationLong = null;
		}
		this.lastModification = lastModificationLong;
	}


	public Boolean isArchived() {
		return !(archived == null || !archived);
	}


	public void setArchived(Boolean archived) {
		this.archived = archived;
	}


	public void setArchived(int archived) {
		this.archived = archived == 1;
	}

	public Boolean isErased() {
		return !(erased == null || !erased);
	}

	public void setErased(Boolean erased) {
		this.erased = erased;
	}

	public void setErased(int erased) {
		this.erased = erased == 1;
	}

	public Boolean isTrashed() {
		return !(trashed == null || !trashed);
	}


	public void setTrashed(Boolean trashed) {
		this.trashed = trashed;
	}


	public void setTrashed(int trashed) {
		this.trashed = trashed == 1;
	}


	public String getAlarm() {
		return alarm;
	}


	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}


	public void setAlarm(long alarm) {
		this.alarm = String.valueOf(alarm);
	}

	public Boolean isFavorite() {
		return !(favorite == null || !favorite);
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite == 1;
	}

	public Boolean isReminderFired() {
		return !(reminderFired == null || !reminderFired);
	}


	public void setReminderFired(Boolean reminderFired) {
		this.reminderFired = reminderFired;
	}


	public void setReminderFired(int reminderFired) {
		this.reminderFired = reminderFired == 1;
	}


	public String getRecurrenceRule() {
		return recurrenceRule;
	}


	public void setRecurrenceRule(String recurrenceRule) {
		this.recurrenceRule = recurrenceRule;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	public void setLatitude(String latitude) {
		try {
			setLatitude(Double.parseDouble(latitude));
		} catch (NumberFormatException | NullPointerException e) {
			this.latitude = null;
		}
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public void setLongitude(String longitude) {
		try {
			setLongitude(Double.parseDouble(longitude));
		} catch (NumberFormatException e) {
			this.longitude = null;
		} catch (NullPointerException e) {
			this.longitude = null;
		}
	}


	public BaseCategory getCategory() {
		return baseCategory;
	}


	public void setCategory(BaseCategory baseCategory) {
		this.baseCategory = baseCategory;
	}


	public Boolean isLocked() {
		return !(locked == null || !locked);
	}


	public void setLocked(Boolean locked) {
		this.locked = locked;
	}


	public void setLocked(int locked) {
		this.locked = locked == 1;
	}


	public Boolean isChecklist() {
		return !(checklist == null || !checklist);
	}


	public void setChecklist(Boolean checklist) {
		this.checklist = checklist;
	}


	public void setChecklist(int checklist) {
		this.checklist = checklist == 1;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public List<? extends BaseAttachment> getAttachmentsList() {
		return attachmentsList;
	}


	public void setAttachmentsList(List<? extends BaseAttachment> attachmentsList) {
		this.attachmentsList = attachmentsList;
	}


	public void backupAttachmentsList() {
		List<BaseAttachment> attachmentsListOld = new ArrayList<BaseAttachment>();
		for (BaseAttachment mBaseAttachment : getAttachmentsList()) {
			attachmentsListOld.add(mBaseAttachment);
		}
		this.attachmentsListOld = attachmentsListOld;
	}


	public List<? extends BaseAttachment> getAttachmentsListOld() {
		return attachmentsListOld;
	}


	public void setAttachmentsListOld(List<? extends BaseAttachment> attachmentsListOld) {
		this.attachmentsListOld = attachmentsListOld;
	}

/*
	@Override
	public boolean equals(Object o) {
		boolean res = false;
		BaseNote baseNote;
		try {
			baseNote = (BaseNote) o;
		} catch (Exception e) {
			return res;
		}

		Object[] a = {getTitle(), getContent(), getHTMLContent(), getCreation(), getLastModification(), getPackageID(), getTagList(), isArchived(),
				isTrashed(), isFavorite(), getAlarm(), getRecurrenceRule(), getLatitude(), getLongitude(), getAddress(), isLocked(),
				getCategory(), isChecklist() };
		Object[] b = {baseNote.getTitle(), baseNote.getContent(), baseNote.getHTMLContent(), baseNote.getCreation(),
				baseNote.getLastModification(), baseNote.getPackageID(), baseNote.getTagList(), baseNote.isArchived(), baseNote.isTrashed(), baseNote.isFavorite(), baseNote.getAlarm(),
				baseNote
				.getRecurrenceRule(), baseNote.getLatitude(), baseNote.getLongitude(), baseNote.getAddress(), baseNote.isLocked(),
				baseNote.getCategory(), baseNote.isChecklist()};
		if (EqualityChecker.check(a, b)) {
			res = true;
		}

		return res;
	}
*/
	// comparator without "getContent()"
	@Override
	public boolean equals(Object o) {
		boolean res = false;
		BaseNote baseNote;
		try {
			baseNote = (BaseNote) o;
		} catch (Exception e) {
			return res;
		}

		Object[] a = {getTitle(), getHTMLContent(), getCreation(), getLastModification(), getPackageID(), getTagList(), isArchived(),
				isTrashed(), isFavorite(), getAlarm(), getRecurrenceRule(), getLatitude(), getLongitude(), getAddress(), isLocked(),
				getCategory(), isChecklist() };
		Object[] b = {baseNote.getTitle(), baseNote.getHTMLContent(), baseNote.getCreation(),
				baseNote.getLastModification(), baseNote.getPackageID(), baseNote.getTagList(), baseNote.isArchived(), baseNote.isTrashed(), baseNote.isFavorite(), baseNote.getAlarm(),
				baseNote
						.getRecurrenceRule(), baseNote.getLatitude(), baseNote.getLongitude(), baseNote.getAddress(), baseNote.isLocked(),
				baseNote.getCategory(), baseNote.isChecklist()};
		if (EqualityChecker.check(a, b)) {
			res = true;
		}

		return res;
	}


	public boolean isChanged(BaseNote baseNote) {
		return !equals(baseNote) || !getAttachmentsList().equals(baseNote.getAttachmentsList());
	}


	public boolean isEmpty() {
		BaseNote emptyBaseNote = new BaseNote();
		// Field to exclude for comparison
		emptyBaseNote.setCreation(getCreation());
		emptyBaseNote.setCategory(getCategory());
		// Check
		return !isChanged(emptyBaseNote);
	}


	public String toString() {
		return getTitle();
	}


	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
