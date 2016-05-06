package com.electrophone.midirig;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Patrik on 16-05-05.
 */
public class SettingsActivity extends PreferenceActivity {
    public static final String KEY_PREF_OSC_OUTPUT_PORT = "pref_osc_output_port";
    public static final String KEY_PREF_OSC_INPUT_PORT = "pref_osc_input_port";
    public static final String KEY_PREF_OSC_REMOTE_HOST = "pref_osc_remote_host";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
