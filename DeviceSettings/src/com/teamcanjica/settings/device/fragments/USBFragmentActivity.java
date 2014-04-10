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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.teamcanjica.settings.device.DeviceSettings;
import com.teamcanjica.settings.device.R;
import com.teamcanjica.settings.device.Utils;

public class USBFragmentActivity extends PreferenceFragment {

	private static final String TAG = "GalaxySAdvance_Settings_USB";
	private static final String FILE_VOTG = "/sys/kernel/abb-regu/VOTG";
	private static final String FILE_CHARGER_CONTROL = "/sys/kernel/abb-charger/charger_curr";
	private static final String FILE_EOC = "/sys/kernel/abb-chargalg/eoc_status";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.usb_preferences);

		PreferenceScreen prefSet = getPreferenceScreen();

		prefSet.findPreference(DeviceSettings.KEY_USB_OTG_POWER).setEnabled(
				isSupported(FILE_VOTG));
		prefSet.findPreference(DeviceSettings.KEY_USE_CHARGER_CONTROL).setEnabled(
				isSupported(FILE_CHARGER_CONTROL));

		CheckBoxPreference pref = (CheckBoxPreference) findPreference("use_charger_control");
		if (pref.isChecked()){
			prefSet.findPreference(DeviceSettings.KEY_CHARGER_CURRENCY).setEnabled(
					true);
		} else {
			prefSet.findPreference(DeviceSettings.KEY_CHARGER_CURRENCY).setEnabled(
					false);
		}

		getActivity().getActionBar().setTitle(getResources().getString(R.string.usb_name));
		getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.usb_icon));

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		// String boxValue;
		String key = preference.getKey();

		Log.w(TAG, "key: " + key);

		if (key.equals(DeviceSettings.KEY_USB_OTG_POWER)) {
			if (((CheckBoxPreference) preference).isChecked()) {
				Utils.writeValue(FILE_VOTG, "1");
			} else {
				Utils.writeValue(FILE_VOTG, "0");
			}
		}

		if (key.equals(DeviceSettings.KEY_USE_CHARGER_CONTROL)) {
			if (((CheckBoxPreference) preference).isChecked()) {
				Utils.writeValue(FILE_CHARGER_CONTROL, "on");
				getPreferenceScreen().findPreference(DeviceSettings.KEY_CHARGER_CURRENCY).setEnabled(
				true);
			} else {
				Utils.writeValue(FILE_CHARGER_CONTROL, "off");
				getPreferenceScreen().findPreference(DeviceSettings.KEY_CHARGER_CURRENCY).setEnabled(
				false);
			}
		}

		if (key.compareTo(DeviceSettings.KEY_EOC) == 0) {
			String eoc = null;
			BufferedReader buffread;
			try {
				buffread = new BufferedReader(new FileReader(FILE_EOC));
				eoc = buffread.readLine();
				buffread.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Utils.showDialog((Context) getActivity(),
					getString(R.string.eoc_title),
					(String) eoc);

		}

		return true;
	}

	public static boolean isSupported(String FILE) {
		return Utils.fileExists(FILE);
	}

	public static void restore(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String votgvalue = sharedPrefs.getBoolean(
				DeviceSettings.KEY_USB_OTG_POWER, false) ? "1" : "0";
		Utils.writeValue(FILE_VOTG, votgvalue);

		String ccvalue = sharedPrefs.getBoolean(
				DeviceSettings.KEY_USE_CHARGER_CONTROL, false) ? "on" : "off";
		Utils.writeValue(FILE_CHARGER_CONTROL, ccvalue);
	}
}
