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

package pk.tappdesign.knizka.helpers;

import static it.feio.android.checklistview.interfaces.Constants.CHECKED_SYM;
import static it.feio.android.checklistview.interfaces.Constants.UNCHECKED_SYM;
import static pk.tappdesign.knizka.utils.ConstantsBase.HTML_DIV_END_TAG;
import static pk.tappdesign.knizka.utils.ConstantsBase.HTML_TEXT_SUB_TITLE_CLASS;
import static pk.tappdesign.knizka.utils.ConstantsBase.MERGED_NOTES_SEPARATOR;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_AUDIO;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_FILES;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_IMAGE;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_SKETCH;
import static pk.tappdesign.knizka.utils.ConstantsBase.MIME_TYPE_VIDEO;
import static pk.tappdesign.knizka.utils.ConstantsBase.PACKAGE_USER_ADDED;
import static pk.tappdesign.knizka.utils.PKStringUtils.removeBRTags;

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.R;
import pk.tappdesign.knizka.helpers.count.CountFactory;
import pk.tappdesign.knizka.models.Attachment;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.StatsSingleNote;
import pk.tappdesign.knizka.utils.ConstantsBase;
import pk.tappdesign.knizka.utils.StorageHelper;
import pk.tappdesign.knizka.utils.TagsHelper;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class NotesHelper {

  public static boolean haveSameId (Note note, Note currentNote) {
    return currentNote != null
        && currentNote.get_id() != null
        && currentNote.get_id().equals(note.get_id());

  }

  public static StringBuilder appendContent (Note note, StringBuilder content, boolean includeTitle) {
    if (content.length() > 0
        && (!StringUtils.isEmpty(note.getTitle()) || !StringUtils.isEmpty(note.getHTMLContent()))) {
      content.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"))
             .append(MERGED_NOTES_SEPARATOR).append(System.getProperty("line.separator"))
             .append(System.getProperty("line.separator"));
    }
    if (!StringUtils.isEmpty(note.getTitle())) {
      content.append(HTML_TEXT_SUB_TITLE_CLASS + note.getTitle() + HTML_DIV_END_TAG  );
    }
    if (!StringUtils.isEmpty(note.getTitle()) && !StringUtils.isEmpty(note.getHTMLContent())) {
      content.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
    }
    if (!StringUtils.isEmpty(note.getHTMLContent())) {
      content.append(removeBRTags(note.getHTMLContent()));
    }
    return content;
  }

  public static void addAttachments (boolean keepMergedNotes, Note note, ArrayList<Attachment> attachments) {
    if (keepMergedNotes) {
      for (Attachment attachment : note.getAttachmentsList()) {
        attachments.add(StorageHelper.createAttachmentFromUri(Knizka.getAppContext(), attachment.getUri
            ()));
      }
    } else {
      attachments.addAll(note.getAttachmentsList());
    }
  }

  public static Note mergeNotes (List<Note> notes, boolean keepMergedNotes) {
    boolean locked = false;
    ArrayList<Attachment> attachments = new ArrayList<>();
    String reminder = null;
    String reminderRecurrenceRule = null;
    Double latitude = null;
    Double longitude = null;

    Note mergedNote = new Note();
    mergedNote.setTitle(notes.get(0).getTitle() + Knizka.getAppContext().getString(R.string.note_merged_title_text));
    mergedNote.setArchived(notes.get(0).isArchived());
    mergedNote.setCategory(notes.get(0).getCategory());
    mergedNote.setPackageID(PACKAGE_USER_ADDED);
    StringBuilder content = new StringBuilder();
    // Just first note title must not be included into the content
    boolean includeTitle = false;

    for (Note note : notes) {
      appendContent(note, content, includeTitle);
      locked = locked || note.isLocked();
      String currentReminder = note.getAlarm();
      if (!StringUtils.isEmpty(currentReminder) && reminder == null) {
        reminder = currentReminder;
        reminderRecurrenceRule = note.getRecurrenceRule();
      }
      latitude = ObjectUtils.defaultIfNull(latitude, note.getLatitude());
      longitude = ObjectUtils.defaultIfNull(longitude, note.getLongitude());
      addAttachments(keepMergedNotes, note, attachments);
      includeTitle = true;
    }

    mergedNote.setContent(content.toString()); // todo: @pk: consider if we should use setContent() at all... (maybe setHTMLContent() is good enough)
    mergedNote.setHTMLContent(mergedNote.getContent());
    mergedNote.setLocked(locked);
    mergedNote.setAlarm(reminder);
    mergedNote.setRecurrenceRule(reminderRecurrenceRule);
    mergedNote.setLatitude(latitude);
    mergedNote.setLongitude(longitude);
    mergedNote.setAttachmentsList(attachments);
    mergedNote.setPrayerMerged(new Long(ConstantsBase.PRAYER_MERGED_YES).longValue());

    return mergedNote;
  }

  /**
   * Retrieves statistics data for a single note
   */
  public static StatsSingleNote getNoteInfos (Note note) {
    StatsSingleNote infos = new StatsSingleNote();

    int words;
    int chars;
    if (note.isChecklist()) {
      infos.setChecklistCompletedItemsNumber(StringUtils.countMatches(note.getContent(), CHECKED_SYM));
      infos.setChecklistItemsNumber(infos.getChecklistCompletedItemsNumber() +
          StringUtils.countMatches(note.getContent(), UNCHECKED_SYM));
    }
    infos.setTags(TagsHelper.retrieveTags(note).size());
    words = getWords(note);
    chars = getChars(note);
    infos.setWords(words);
    infos.setChars(chars);

    int attachmentsAll = 0;
    int images = 0;
    int videos = 0;
    int audioRecordings = 0;
    int sketches = 0;
    int files = 0;

    for (Attachment attachment : note.getAttachmentsList()) {
      if (MIME_TYPE_IMAGE.equals(attachment.getMime_type())) {
        images++;
      } else if (MIME_TYPE_VIDEO.equals(attachment.getMime_type())) {
        videos++;
      } else if (MIME_TYPE_AUDIO.equals(attachment.getMime_type())) {
        audioRecordings++;
      } else if (MIME_TYPE_SKETCH.equals(attachment.getMime_type())) {
        sketches++;
      } else if (MIME_TYPE_FILES.equals(attachment.getMime_type())) {
        files++;
      }
      attachmentsAll++;
    }
    infos.setAttachments(attachmentsAll);
    infos.setImages(images);
    infos.setVideos(videos);
    infos.setAudioRecordings(audioRecordings);
    infos.setSketches(sketches);
    infos.setFiles(files);

    if (note.getCategory() != null) {
      infos.setCategoryName(note.getCategory().getName());
    }

    return infos;
  }

  /**
   * Counts words in a note
   */
  public static int getWords (Note note) {
    return CountFactory.getWordCounter().countWords(note);
  }

  /**
   * Counts chars in a note
   */
  public static int getChars (Note note) {
    return CountFactory.getWordCounter().countChars(note);
  }

}
