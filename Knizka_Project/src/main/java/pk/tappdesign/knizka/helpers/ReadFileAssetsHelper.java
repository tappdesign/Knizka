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

import pk.tappdesign.knizka.Knizka;
import pk.tappdesign.knizka.utils.AssetUtils;

public class ReadFileAssetsHelper {
   private static ReadFileAssetsHelper instance = null;

   private static String tdJSUtils;

   public static synchronized ReadFileAssetsHelper doGetInstance()
   {
      tdJSUtils = "<script>" + AssetUtils.readFileFromAssetsAsString(Knizka.getAppContext(), "js/tdUtils.js") + "</script>";
      return new ReadFileAssetsHelper();
   }


   public static synchronized ReadFileAssetsHelper getInstance()
   {
      if (instance == null)
      {
         instance =  doGetInstance();
      }

      return instance;
   }


   public static String getTDJSUtils()
   {
      return tdJSUtils;
   }
}
