package com.teamcanjica.settings.device;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MasterSeekBarDialogPreference extends
		DialogPreference implements SeekBar.OnSeekBarChangeListener, OnPreferenceChangeListener {
	// Layout widgets.
	private SeekBar seekBar = null;
	private TextView valueText = null;

	// Custom xml attributes.
	private int maximumValue = 0;
	private int minimumValue = 0;
	private int stepSize = 0;
	private String units = null;

	private int value = 0;

	private static final String TAG = "GalaxySAdvance_Settings_Seekbar";

	private static final String FILE_READAHEADKB = "/sys/block/mmcblk0/queue/read_ahead_kb";
	private static final String FILE_CPU_VOLTAGE = "/sys/kernel/liveopp/arm_step";

	/**
	 * The SeekBarDialogPreference constructor.
	 * @param context of this preference.
	 * @param attrs custom xml attributes.
	 */
	public MasterSeekBarDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.setOnPreferenceChangeListener(this);

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
			R.styleable.MasterSeekBarDialogPreference);

		maximumValue = typedArray.getInteger(
			R.styleable.MasterSeekBarDialogPreference_maximumValue, 0);
		minimumValue = typedArray.getInteger(
			R.styleable.MasterSeekBarDialogPreference_minimumValue, 0);
		stepSize = typedArray.getInteger(
			R.styleable.MasterSeekBarDialogPreference_stepSize, 1);
		units = typedArray.getString(
			R.styleable.MasterSeekBarDialogPreference_units);

		typedArray.recycle();
	}

	/**
	 * {@inheritDoc}
	 */
	protected View onCreateDialogView() {
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());

		View view = layoutInflater.inflate(
			R.layout.preference_seek_bar_dialog, null);

		seekBar = (SeekBar)view.findViewById(R.id.seekbar);
		valueText = (TextView)view.findViewById(R.id.valueText);

		// Get the persistent value and correct it for the minimum value.
		value = getPersistedInt(minimumValue) - minimumValue;

		// You're never know...
		if (value < 0) {
			value = 0;
		}

		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setKeyProgressIncrement(stepSize);
		seekBar.setMax(maximumValue - minimumValue);
		SharedPreferences prefs = getContext().getSharedPreferences(DeviceSettings.KEY_SEEKBARVAL, Context.MODE_PRIVATE);
		value = prefs.getInt("seekBarValue", 512);
		seekBar.setProgress(value);

		return view;
	}
	/**
	 * {@inheritDoc}
	 */
	public void onProgressChanged(SeekBar seek, int newValue,
			boolean fromTouch) {
		// Round the value to the closest integer value.
		if (stepSize >= 1) {
			value = Math.round(newValue/stepSize)*stepSize;
		}
		else {
			value = newValue;
		}

		// Set the valueText text.
		valueText.setText(String.valueOf(value + minimumValue) +
			(units == null ? "" : units));

		callChangeListener(value);
	}
	/**
	 * {@inheritDoc}
	 */
	public void onStartTrackingTouch(SeekBar seek) {
	}
	/**
	 * {@inheritDoc}
	 */
	public void onStopTrackingTouch(SeekBar seek) {
	}
	/**
	 * {@inheritDoc}
	 */
	public void onClick(DialogInterface dialog, int which) {
		// if the positive button is clicked, we persist the value.
		if (which == DialogInterface.BUTTON_POSITIVE) {
			SharedPreferences prefs = getContext().getSharedPreferences(DeviceSettings.KEY_SEEKBARVAL, Context.MODE_PRIVATE);
			prefs.edit().putInt("seekBarValue", seekBar.getProgress()).commit();
			if (shouldPersist()) {
				persistInt(value + minimumValue);
			}
		}

		super.onClick(dialog, which);
	}
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {

		String key = preference.getKey();
		Log.w(TAG, "key: " + key);

		if (key.equals(DeviceSettings.KEY_READAHEADKB)) {
			Utils.writeValue(FILE_READAHEADKB, String.valueOf((Integer) newValue + 128));
		} else if (key.equals(DeviceSettings.KEY_CPU_VOLTAGE)) {
			switch ((String) newValue) {
			case "-12.5":
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x17");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x19");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x1f");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x23");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2e");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x31");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3e");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3e");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3e");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3e");
				break;
			case "-25.0":
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x16");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x18");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x1e");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x22");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2d");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x30");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3d");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3d");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3d");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3d");
				break;
			case "-37.5":
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x15");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x17");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x1d");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x21");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2c");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x2f");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3c");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3c");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3c");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3c");
				break;
			case "-50.0":
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x14");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x16");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x1c");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x20");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2b");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x2e");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3b");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3b");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3b");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3b");
				break;
			default:
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x18");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x1a");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x20");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x24");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2f");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x32");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3f");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3f");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3f");
				Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3f");
				break;
			}
		}

		return true;
	}

	public static void restore(Context context) {

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		Utils.writeValue(FILE_READAHEADKB,
				String.valueOf(sharedPrefs.getString(DeviceSettings.KEY_READAHEADKB, "512")));

		switch (String.valueOf(sharedPrefs.getString(DeviceSettings.KEY_CPU_VOLTAGE, "0.0"))) {
		case "-12.5":
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x17");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x19");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x1f");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x23");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2e");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x31");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3e");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3e");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3e");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3e");
			break;
		case "-25.0":
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x16");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x18");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x1e");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x22");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2d");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x30");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3d");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3d");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3d");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3d");
			break;
		case "-37.5":
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x15");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x17");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x1d");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x21");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2c");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x2f");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3c");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3c");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3c");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3c");
			break;
		case "-50.0":
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x14");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x16");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x1c");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x20");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2b");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x2e");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3b");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3b");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3b");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3b");
			break;
		default:
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(0), "0x18");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(1), "0x1a");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(2), "0x20");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(3), "0x24");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(4), "0x2f");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(5), "0x32");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(6), "0x3f");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(7), "0x3f");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(8), "0x3f");
			Utils.writeValue(FILE_CPU_VOLTAGE + String.valueOf(9), "0x3f");
			break;
		}
	}
}
