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


public interface Constants extends ConstantsBase {

	String TAG = TAG_BASE + " Alpha";
	String EXTERNAL_STORAGE_FOLDER = EXTERNAL_STORAGE_FOLDER_BASE + " Alpha";
	String PACKAGE = PACKAGE_BASE + ".alpha";
	String PREFS_NAME = PACKAGE + "_preferences";

	String CHANNEL_BACKUPS_ID = PACKAGE + ".backups";
	String CHANNEL_REMINDERS_ID = PACKAGE + ".reminders";
	String CHANNEL_PINNED_ID = PACKAGE + ".pinned";
}
