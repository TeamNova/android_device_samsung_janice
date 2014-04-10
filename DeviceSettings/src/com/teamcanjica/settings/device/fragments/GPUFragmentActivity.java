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

import com.teamcanjica.settings.device.DeviceSettings;
import com.teamcanjica.settings.device.R;
import com.teamcanjica.settings.device.Utils;

public class GPUFragmentActivity extends PreferenceFragment {

	private static final String TAG = "GalaxySAdvance_Settings_GPU";
	
	public static final String FILE_AUTOBOOST = "/sys/kernel/mali/mali_auto_boost";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.gpu_preferences);
		// Will add a GPU icon soon 
		getActivity().getActionBar().setTitle(getResources().getString(R.string.gpu_name));
		getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.screen_icon));
	}
	
	public static boolean isSupported(String FILE) {
		return Utils.fileExists(FILE);
	}
	
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		// String boxValue;
		String key = preference.getKey();

		Log.w(TAG, "key: " + key);

		if (key.equals(DeviceSettings.KEY_DISABLE_AUTOBOOST)) {
			if (((CheckBoxPreference) preference).isChecked()) {
				Utils.writeValue(FILE_AUTOBOOST, "0");
			} else {
				Utils.writeValue(FILE_AUTOBOOST, "1");
			}
		}

		return true;
	}
	
	public static void restore(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		String autoboostvalue = sharedPrefs.getBoolean(
				DeviceSettings.KEY_DISABLE_AUTOBOOST, false) ? "0" : "1";
		Utils.writeValue(FILE_AUTOBOOST, autoboostvalue);
	}
}
