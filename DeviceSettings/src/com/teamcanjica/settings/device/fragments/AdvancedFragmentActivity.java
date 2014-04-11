/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teamcanjica.settings.device.fragments;

import java.io.IOException;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;

import com.teamcanjica.settings.device.DeviceSettings;
import com.teamcanjica.settings.device.R;
import com.teamcanjica.settings.device.Utils;

public class AdvancedFragmentActivity extends PreferenceFragment {

	private static final String TAG = "GalaxySAdvance_Settings_Advanced";

	private static final String FILE_ACCELEROMETER_CALIB = "/sys/class/sensors/accelerometer_sensor/calibration";

	private static final String FILE_SPI_CRC = "/sys/module/mmc_core/parameters/use_spi_crc";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.advanced_preferences);

		getActivity().getActionBar().setTitle(getResources().getString(R.string.advanced_name));
		getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.sadvancesettings_icon));

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		String key = preference.getKey();

		Log.w(TAG, "key: " + key);

		if (key.equals(DeviceSettings.KEY_SWITCH_STORAGE)) {
			boolean b = ((CheckBoxPreference) preference).isChecked();
			String cmd = "SwapStorages.sh " + (b?"1":"0");

			try {
			    Process proc = Runtime.getRuntime().exec(new String[]{"su","-c",cmd});
			    proc.waitFor();
			} catch (IOException e) {
			    e.printStackTrace();
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		}

		if (key.equals(DeviceSettings.KEY_USE_SPI_CRC)) {
			Utils.writeValue(FILE_SPI_CRC, (((CheckBoxPreference) preference).
					isChecked() ? "0" : "1"));
		}

		if (key.compareTo(DeviceSettings.KEY_USE_ACCELEROMETER_CALIBRATION) == 0) {
			Utils.writeValue(FILE_ACCELEROMETER_CALIB, (((CheckBoxPreference) preference).
					isChecked() ? "1" : "0"));
		} else if (key.compareTo(DeviceSettings.KEY_CALIBRATE_ACCELEROMETER) == 0) {
			// when calibration data utilization is disabled and enabled back,
			// calibration is done at the same time by driver
			Utils.writeValue(FILE_ACCELEROMETER_CALIB, "0");
			Utils.writeValue(FILE_ACCELEROMETER_CALIB, "1");
			Utils.showDialog((Context) getActivity(),
					getString(R.string.accelerometer_dialog_head),
					getString(R.string.accelerometer_dialog_message));
		}

		return true;
	}

	public static void restore(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		int sstor = SystemProperties.getInt("persist.sys.vold.switchexternal", 0) ;
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putBoolean(DeviceSettings.KEY_SWITCH_STORAGE,sstor==1?true:false);
		editor.commit();

		Utils.writeValue(FILE_SPI_CRC, sharedPrefs.getBoolean(
				DeviceSettings.KEY_USE_SPI_CRC, false) ? "0" : "1");

		Utils.writeValue(FILE_ACCELEROMETER_CALIB, sharedPrefs.getBoolean(
				DeviceSettings.KEY_USE_ACCELEROMETER_CALIBRATION, true) ? "1" : "0");

	}

}
