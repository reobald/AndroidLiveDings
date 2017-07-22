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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class MidiDingsOSCParams {

    //output
    public static final String QUERY = "/mididings/query";
    //MidiDingsOSCParams OSC commands
    public static final String SWITCH_SCENE = "/mididings/switch_scene";
    public static final String SWITCH_SUBSCENE = "/mididings/switch_subscene";
    public static final String PREV_SCENE = "/mididings/prev_scene";
    public static final String NEXT_SCENE = "/mididings/next_scene";
    public static final String PREV_SUBSCENE = "/mididings/prev_subscene";
    public static final String NEXT_SUBSCENE = "/mididings/next_subscene";
    public static final String PANIC = "/mididings/panic";
    public static final String QUIT = "/mididings/quit";
    //input
    public static final String DATA_OFFSET = "/mididings/data_offset";
    public static final String BEGIN_SCENES = "/mididings/begin_scenes";
    public static final String ADD_SCENE = "/mididings/add_scene";
    public static final String END_SCENES = "/mididings/end_scenes";
    public static final String CURRENT_SCENE = "/mididings/current_scene";
    private static final String LOG_TAG = "MidiDingsOSCParams";
    private String inetAddress;
    private int port;
    private String cmd;
    private ArrayList params;

    public MidiDingsOSCParams(Context context, String cmd) throws UnknownHostException {
        setInetAddress(getOscRemoteHostFromPreferences(context));
        this.port = getOscOutPortNumberFromPreferences(context);
        this.cmd = cmd;
        this.params = new ArrayList();
        Log.d(LOG_TAG, this.toString());
    }

    public void addParam(Object o) {
        params.add(o);
    }

    public String getInetAddress() {
        return inetAddress;
    }

    private void setInetAddress(String inetAddress) throws UnknownHostException {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public String getCmd() {
        return cmd;
    }

    public ArrayList getParams() {
        if (params.size() == 0) {
            return null;
        } else {
            return params;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("inetAddress: ");
        sb.append(this.inetAddress);
        sb.append(", port: ");
        sb.append(this.port);
        sb.append(", cmd:");
        sb.append(this.cmd);
        sb.append(", params:");
        sb.append(this.params.toString());
        return sb.toString();
    }

    private String getOscRemoteHostFromPreferences(Context context) {
        return getPrefValueByKey(context, SettingsActivity.KEY_PREF_OSC_REMOTE_HOST, "");
    }

    private int getOscOutPortNumberFromPreferences(Context context) {
        String s = getPrefValueByKey(context, SettingsActivity.KEY_PREF_OSC_OUTPUT_PORT, "");
        return new Integer(s);
    }

    private String getPrefValueByKey(Context context, String key, String defaultValue) {
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, defaultValue);
    }
}
