/* Copyright (C) 2015-2017 Patrik Jonasson - All Rights Reserved
*
*
* This file is part of MidiRig.
*
* MidiRig is free software: you can redistribute it and/or modify it 
* under the terms of the GNU General Public License as published by 
* the Free Software Foundation, either version 3 of the License, 
* or (at your option) any later version.
*
* MidiRig is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with MidiRig.  
* If not, see <http://www.gnu.org licenses/>.
*/

package com.electrophone.midirig;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SettingsActivity extends Activity {
    public static final String KEY_PREF_OSC_OUTPUT_PORT = "pref_osc_output_port";
    public static final String KEY_PREF_OSC_INPUT_PORT = "pref_osc_input_port";
    public static final String KEY_PREF_OSC_REMOTE_HOST = "pref_osc_remote_host";
    public static final String KEY_PREF_ALPHABETICAL_SORT = "pref_alphabetical_sort";
    public static final String KEY_PREF_DEMO_MODE = "pref_demo_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
