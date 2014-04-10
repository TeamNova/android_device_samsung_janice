package com.teamcanjica.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

public class ReadaheadkB extends CustomSeekBarDialogPreference implements OnPreferenceChangeListener {

    public ReadaheadkB(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnPreferenceChangeListener(this);
	}

	private static final String FILE = "/sys/block/mmcblk0/queue/read_ahead_kb";

	public static boolean isSupported() {
		return Utils.fileExists(FILE);
	}

	/**
	 * Restore AnaGain3 Control settings from SharedPreferences.
	 * 
	 * @param context
	 *            The context to read the SharedPreferences from
	 */
	public static void restore(Context context) {
		if (!isSupported()) {
			return;
		}

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Utils.writeValue(FILE,
				String.valueOf(sharedPrefs.getString(DeviceSettings.KEY_READAHEADKB, "512")));
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		Utils.writeValue(FILE, String.valueOf((Integer) newValue + 128));
		return true;
	}

}
