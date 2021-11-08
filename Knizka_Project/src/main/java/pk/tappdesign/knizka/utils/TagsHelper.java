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



import androidx.core.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.models.Tag;
import it.feio.android.pixlui.links.UrlCompleter;
import rx.Observable;


public class TagsHelper {


    public static List<Tag> getAllTags() {
        return DbHelper.getInstance().getTags();
    }

    private static boolean isAlreadyExistinginList(List<Tag> aTags, Tag tag) {
        boolean result = false;
        for (Tag word: aTags) {
            if (word.getText().equals(tag.getText()))
            {
                result = true;
            }
        }
        return result;
    }

    public static void AddAdditionalTags(List<Tag> aTags, String newTags)
    {
        if (newTags != null && !newTags.isEmpty())
        {
//            String[] words = newTags.replaceAll(",", " ").replaceAll("#", " ").replaceAll(" +", " "). trim().split(" ");
            String[] words = newTags.replaceAll("#", " ").replaceAll(" +", " "). trim().split(",");

            for (String word: words) {
                Tag tag = new Tag("#" + word.trim(), 1 );
                // add to list only if not existing in list yet. (todo: rewrite it to some more sophisticated comparator, not just hepler function "isAlreadyExistinginList()")
                if (isAlreadyExistinginList(aTags, tag) == false)
                {
                    aTags.add(tag);
                }
            }
        }
    }

    public static String createHashtagFromTags(String newTags) {
        String result = "";
        if (newTags != null && !newTags.isEmpty()) {
           // String[] words = newTags.replaceAll(",", " ").replaceAll("#", " ").replaceAll(" +", " ").trim().split(" ");
            String[] words = newTags.replaceAll("#", " ").replaceAll(" +", " ").trim().split(",");
            for (String word : words) {
                if (!result.isEmpty()) {
                    result =  result + ", #" + word;
                } else {
                    result = "#" + word;
                }
            }
        }
        return result;
    }

	public static HashMap<String, Integer> retrieveTags(Note note) {
		HashMap<String, Integer> tagsMap = new HashMap<>();
		if (note.getTagList() != null)
      {
          String[] words = (note.getTagList()).replaceAll("\n", " ").trim().split(",");
          for (String word: words) {
              String parsedHashtag = UrlCompleter.parseHashtag(word);
              if (StringUtils.isNotEmpty(parsedHashtag)) {
                  int count = tagsMap.get(parsedHashtag) == null ? 0 : tagsMap.get(parsedHashtag);
                  tagsMap.put(parsedHashtag, ++count);
              }
          }
      }

		return tagsMap;
	}


    public static Pair<String, List<Tag>> addTagToNote(List<Tag> tags, Integer[] selectedTags, Note note) {
        StringBuilder sbTags = new StringBuilder();
        List<Tag> tagsToRemove = new ArrayList<>();

        List<Integer> selectedTagsList = Arrays.asList(selectedTags);
        for (int i = 0; i < tags.size(); i++) {

            if (!selectedTagsList.contains(i)) {
                tagsToRemove.add(tags.get(i));  // we don't need this tags to remove, but from historical reasons....
            } else {
                if (selectedTagsList.contains(i)) {
                   if (sbTags.length() > 0) {
                        sbTags.append(", ");
                   }
                   sbTags.append(tags.get(i));
                }
            }
        }

        return Pair.create(sbTags.toString(), tagsToRemove);
    }


    private static boolean mapContainsTag(HashMap<String, Integer> tagsMap, Tag tag) {
        for (String tagsMapItem : tagsMap.keySet()) {
            if (tagsMapItem.equals(tag.getText())) {
                return true;
            }
        }
        return false;
    }


    public static Pair<String, String> removeTag(String noteTitle, String noteContent, List<Tag> tagsToRemove) {
        String title = noteTitle, content = noteContent;
        for (Tag tagToRemove : tagsToRemove) {
            if (StringUtils.isNotEmpty(title)) {
                title = Observable.from(title.replaceAll(ConstantsBase.TAG_SPECIAL_CHARS_TO_REMOVE, " ").split("\\s"))
                        .map(String::trim)
                        .filter(s -> !s.matches(tagToRemove.getText()))
                        .reduce((s, s2) -> s + " " + s2)
                        .toBlocking()
                        .singleOrDefault("");
            }
            if (StringUtils.isNotEmpty(content)) {
                content = Observable.from(content.replaceAll(ConstantsBase.TAG_SPECIAL_CHARS_TO_REMOVE, " ").split("\\s"))
                        .map(String::trim)
                        .filter(s -> !s.matches(tagToRemove.getText()))
                        .reduce((s, s2) -> s + " " + s2)
                        .toBlocking()
                        .singleOrDefault("");
            }

        }
        return new Pair<>(title, content);
    }


    public static String[] getTagsArray(List<Tag> tags) {
        String[] tagsArray = new String[tags.size()];
        for (int i = 0; i < tags.size(); i++) {
            tagsArray[i] = tags.get(i).getText().substring(1) + " (" + tags.get(i).getCount() + ")";
        }
        return tagsArray;
    }


    public static Integer[] getPreselectedTagsArray (Note note, List<Tag> tags) {
        List<Integer> t = new ArrayList<>();
        for (String noteTag : TagsHelper.retrieveTags(note).keySet()) {
            for (Tag tag : tags) {
                if (tag.getText().equals(noteTag)) {
                    t.add(tags.indexOf(tag));
                    break;
                }
            }
        }
        return t.toArray(new Integer[]{});
    }


    public static Integer[] getPreselectedTagsArray (List<Note> notes, List<Tag> tags) {
        HashSet<Integer> set = new HashSet<>();
        for (Note note : notes) {
            set.addAll(Arrays.asList(getPreselectedTagsArray(note, tags)));
        }
        return set.toArray(new Integer[]{});
    }

}
