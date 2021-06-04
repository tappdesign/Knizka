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

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import pk.tappdesign.knizka.helpers.LogDelegate;
import pk.tappdesign.knizka.utils.Constants;


public class CopyDBFromAssets {


    public static synchronized void tryCopyFromAssets(Context context)
    {
        if (needCopyEmbeddedDB(context))
        {
            copyEmbeddedDB(context);
        }
        if (needCopyUserDB(context))
        {
            copyUserDB(context);
        }
    }

    public static synchronized boolean needCopyEmbeddedDB(Context context)
    {
        boolean retVal = false;

        if (existFileInAssets(context, DBConst.DB_ASSETS_FILE_ATTACHED)) {
            File f = new File(context.getApplicationInfo().dataDir + DBConst.DB_STORAGE_FILE_ATTACHED);

            if (getEmbeddedDBVersionFromPrefs(context) < DBConst.DB_VERSION_ATTACHED) {
                retVal = true;
            }

            if (!f.exists()) {
                retVal = true;
            }
        }
        return retVal;
    }

    public static synchronized boolean existFileInAssets(Context context, String fullFileName)
    {
        InputStream is = null;
        boolean retVal = false;

        try {
            is = context.getAssets().open(fullFileName);
            retVal = true;
        } catch (IOException e) {

        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e)
                {
                    LogDelegate.w("Cannot close file");
                }
            }
            is = null;
        }
        return retVal;
    }

    public static synchronized int getEmbeddedDBVersionFromPrefs(Context context)
    {
        return Prefs.getInt(Constants.PREF_DB_EMBEDDED_VERSION, 0);
    }

    public static synchronized void setEmbeddedDBVersion(Context context)
    {
        Prefs.edit().putInt(Constants.PREF_DB_EMBEDDED_VERSION, DBConst.DB_VERSION_ATTACHED).commit();
    }


    public static synchronized void copyEmbeddedDB(Context context)
    {
        try {
            copyDatabaseFromAssets(context, DBConst.DB_ASSETS_FILE_ATTACHED, context.getApplicationInfo().dataDir + DBConst.DB_STORAGE_FILE_ATTACHED);
            setEmbeddedDBVersion(context);
        } catch (SQLiteAssetException e)
        {
            LogDelegate.e("Error occurs by copying the embedded database");
            e.printStackTrace();
        }
    }

    public static void copyUserDB(Context context)
    {
        try {
            copyDatabaseFromAssets(context, DBConst.DB_ASSETS_FILE_USER_DATA, context.getApplicationInfo().dataDir + DBConst.DB_STORAGE_FILE_USER_DATA);
        } catch (SQLiteAssetException e)
        {
            LogDelegate.e("Error occurs by copying the user database");
            e.printStackTrace();
        }
    }



    public static synchronized boolean needCopyUserDB(Context context)
    {
        boolean retVal = false;

        if (existFileInAssets(context, DBConst.DB_ASSETS_FILE_USER_DATA)) {
            File f = new File(context.getApplicationInfo().dataDir + DBConst.DB_STORAGE_FILE_USER_DATA);

            if (!f.exists()) {
                retVal = true;
            }
        }

        return retVal;
    }

    public static synchronized InputStream getInputStream(Context context, String fullFileName)
    {
        InputStream retVal;
        try {
            retVal = context.getAssets().open(fullFileName);
        } catch (IOException e) {
            LogDelegate.w("File '" + fullFileName + "'not found.");
            retVal = null;
        }
        return retVal;
    }

    public static synchronized InputStream getZipInputStream(Context context, String fullFileName)
    {
        InputStream retVal;

        try {
            InputStream is = context.getAssets().open(fullFileName + ".zip");
            retVal = getFileFromZip(is);
        } catch (IOException e2) {
            LogDelegate.w("File '" + fullFileName + "'not found.");
            retVal = null;
        }

        return retVal;
    }

    public static synchronized void createDestinationDirIfNeeded(Context context)
    {
        File f = new File(context.getApplicationInfo().dataDir + DBConst.DB_STORAGE_PATH);

        if (!f.exists()) {
            f.mkdirs();
        }
    }

    private static synchronized void copyDatabaseFromAssets(Context context, String sourceInAssets, String destInternalStorage) throws SQLiteAssetException {
        LogDelegate.w("Copying database from assets");

        InputStream is = getInputStream(context, sourceInAssets);

        if (is == null)
        {
            is = getZipInputStream(context, sourceInAssets);
        }

        try {
            createDestinationDirIfNeeded(context);

            writeExtractedFileToDisk(is, new FileOutputStream(destInternalStorage));
            LogDelegate.w("Database '" + sourceInAssets + "' copy complete");

        } catch (IOException e) {
                SQLiteAssetException se = new SQLiteAssetException("Unable to write " + destInternalStorage + " to data directory");
                se.setStackTrace(e.getStackTrace());
                throw se;
         }
    }


    public static synchronized void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer))>0){
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }

    public static synchronized ZipInputStream getFileFromZip(InputStream zipFileStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipFileStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            LogDelegate.w("Extracting file: '" + ze.getName() + "'...");
            return zis;
        }
        return null;
    }


    /**
     * An exception that indicates there was an error with SQLite asset retrieval or parsing.
     */
    @SuppressWarnings("serial")
    public static class SQLiteAssetException extends SQLiteException {

        public SQLiteAssetException() {}

        public SQLiteAssetException(String error) {
            super(error);
        }
    }


}
