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

package pk.tappdesign.knizka.db;

public interface DBConst {

    String DB_STORAGE_PATH = "/databases/";
    String DB_ASSETS_DIR = "db/";

    String DB_ATTACHED = "knizka-attached.db3";
    String DB_STORAGE_FILE_ATTACHED = DB_STORAGE_PATH + DB_ATTACHED;
    String DB_ASSETS_FILE_ATTACHED = DB_ASSETS_DIR + DB_ATTACHED;

    String DB_USER_DATA = "knizka-user-data.db3";
    String DB_STORAGE_FILE_USER_DATA = DB_STORAGE_PATH + DB_USER_DATA;
    String DB_ASSETS_FILE_USER_DATA = DB_ASSETS_DIR + DB_USER_DATA;

    int DB_VERSION_ATTACHED = 21;
    int DB_VERSION_USER_DATA = 7;

}
