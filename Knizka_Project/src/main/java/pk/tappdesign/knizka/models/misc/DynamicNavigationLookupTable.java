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

package pk.tappdesign.knizka.models.misc;


import java.util.List;

import de.greenrobot.event.EventBus;
import pk.tappdesign.knizka.async.bus.DynamicNavigationReadyEvent;
import pk.tappdesign.knizka.async.bus.NotesUpdatedEvent;
import pk.tappdesign.knizka.db.DbHelper;
import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.models.Note;
import pk.tappdesign.knizka.utils.ConstantsBase;


public class DynamicNavigationLookupTable {

	private static DynamicNavigationLookupTable instance;
	int archived;
	int trashed;
	int uncategorized;
	int reminders;
	int favorites;
	int prayerSets;
	int intentions;


	private DynamicNavigationLookupTable() {
		EventBus.getDefault().register(this);
		update();
	}


	public static DynamicNavigationLookupTable getInstance() {
		if (instance == null) {
			instance = new DynamicNavigationLookupTable();
		}
		return instance;
	}

/*
	public void update() {
		((Runnable) () -> {
			archived = trashed = uncategorized = reminders = favorites = prayerSets = intentions = 0;
			List<Note> notes = DbHelper.getInstance().getAllNotes(false);
			for (int i = 0; i < notes.size(); i++) {
				if (notes.get(i).isTrashed()) trashed++;
				else if (notes.get(i).isArchived()) archived++;
				else if (notes.get(i).getAlarm() != null) reminders++;
				else if (notes.get(i).isFavorite()) favorites++;
				else if (notes.get(i).getPackageID() == ConstantsBase.PACKAGE_USER_INTENT) intentions++;
				else if (notes.get(i).getPrayerSet() > 0) prayerSets++;

				if (notes.get(i).getCategory() == null || notes.get(i).getCategory().getId().equals(0L)) {
					uncategorized++;
				}
			}
			EventBus.getDefault().post(new DynamicNavigationReadyEvent());
			LogDelegate.d("Dynamic menu finished counting items");
		}).run();
	}
*/

	public void update() {
		((Runnable) () -> {
			archived = trashed = uncategorized = reminders = favorites = prayerSets = intentions = 0;
			List<Note> notes = DbHelper.getInstance().getAllNotes(false);
			for (int i = 0; i < notes.size(); i++) {
				if (notes.get(i).isTrashed()) trashed++;
				else if (notes.get(i).isArchived()) archived++;

				else {  // if not trashed and not archived
					if (notes.get(i).getAlarm() != null) reminders++;
					if (notes.get(i).isFavorite()) favorites++;
					if (notes.get(i).getPackageID() == ConstantsBase.PACKAGE_USER_INTENT) intentions++;
					if (notes.get(i).getPrayerSet() > 0) prayerSets++;
					if (notes.get(i).getCategory() == null || notes.get(i).getCategory().getId().equals(0L)) {
						uncategorized++;
					}
				}

			}
			EventBus.getDefault().post(new DynamicNavigationReadyEvent());
			LogDelegate.d("Dynamic menu finished counting items");
		}).run();
	}


	public void onEventAsync(NotesUpdatedEvent event) {
		update();
	}


	public int getArchived() {
		return archived;
	}


	public int getTrashed() {
		return trashed;
	}


	public int getReminders() {
		return reminders;
	}


	public int getUncategorized() {
		return uncategorized;
	}

	public int getFavorites() {
		return favorites;
	}

	public int getPrayerSets() {
		return prayerSets;
	}

	public int getIntentions() {
		return intentions;
	}
}
