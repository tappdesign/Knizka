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

package pk.tappdesign.knizka;

import static pk.tappdesign.knizka.utils.Constants.PACKAGE;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_LANG;
import static pk.tappdesign.knizka.utils.ConstantsBase.PREF_SEND_ANALYTICS;
import static pk.tappdesign.knizka.utils.ConstantsBase.PROPERTIES_PARAMS_SEPARATOR;

import android.content.Context;
import android.content.res.Configuration;
import android.os.StrictMode;
import androidx.multidex.MultiDexApplication;
import com.pixplicity.easyprefs.library.Prefs;
import it.feio.android.analitica.AnalyticsHelper;
import it.feio.android.analitica.AnalyticsHelperFactory;
import it.feio.android.analitica.MockAnalyticsHelper;
import it.feio.android.analitica.exceptions.AnalyticsInstantiationException;
import it.feio.android.analitica.exceptions.InvalidIdentifierException;
import pk.tappdesign.knizka.helpers.LanguageHelper;
import pk.tappdesign.knizka.helpers.notifications.NotificationsHelper;
import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraHttpSender;
import org.acra.annotation.AcraToast;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;

import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

//
//@AcraCore(buildConfigClass = BuildConfig.class)
//@AcraHttpSender(uri = BuildConfig.CRASH_REPORTING_URL,
//		httpMethod = HttpSender.Method.POST)
//@AcraToast(resText = R.string.crash_toast)
public class Knizka extends MultiDexApplication {

	private static Context mContext;
	private AnalyticsHelper analyticsHelper;

	public static boolean isDebugBuild() {
		return BuildConfig.BUILD_TYPE.equals("debug");
	}

	public static Context getAppContext() {
		return Knizka.mContext;
  }

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);

		if (!Knizka.isDebugBuild())
		{
				CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);
				builder.setBuildConfigClass(BuildConfig.class).setReportFormat(StringFormat.JSON);

				builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class).setMailTo("tappdesignstudio@gmail.com").setEnabled(true);
				ACRA.init(this, builder);
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();

		mContext = getApplicationContext();
    	initSharedPreferences();
		enableStrictMode();

		new NotificationsHelper(this).initNotificationChannels();
	}

  private void initSharedPreferences() {
    new Prefs.Builder()
        .setContext(this)
        .setMode(MODE_PRIVATE)
        .setPrefsName(PACKAGE)
        .setUseDefaultSharedPreference(true)
        .build();
  }
	private void enableStrictMode() {
		if (isDebugBuild()) {
			StrictMode.enableDefaults();
		}
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
    String language = Prefs.getString(PREF_LANG, "");
		LanguageHelper.updateLanguage(this, language);
	}

	public AnalyticsHelper getAnalyticsHelper() {
		if (analyticsHelper == null) {
      boolean enableAnalytics = Prefs.getBoolean(PREF_SEND_ANALYTICS, true);
			try {
				String[] analyticsParams = BuildConfig.ANALYTICS_PARAMS.split(PROPERTIES_PARAMS_SEPARATOR);
				analyticsHelper = new AnalyticsHelperFactory().getAnalyticsHelper(this, enableAnalytics,
						analyticsParams);
			} catch (AnalyticsInstantiationException | InvalidIdentifierException e) {
				analyticsHelper = new MockAnalyticsHelper();
			}
		}
		return analyticsHelper;
	}
}
