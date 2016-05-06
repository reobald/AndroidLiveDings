package com.electrophone.midirig;

import android.util.Log;

import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Patrik on 15-12-21.
 */
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
    public static final int DEFAULT_IN_PORT = 56418;
    public static final int DEFAULT_OUT_PORT = 56419;
    public static final String DEFAULT_INET_ADDR = "192.168.42.1";
    private static final String LOG_TAG = "MidiDingsOSCParams";
    private String inetAddress;
    private int port;
    private String cmd;
    private ArrayList params;

    public MidiDingsOSCParams(String cmd) throws UnknownHostException {
        setInetAddress(DEFAULT_INET_ADDR);
        this.port = DEFAULT_OUT_PORT;
        this.cmd = cmd;
        this.params = new ArrayList();
        Log.d(LOG_TAG, this.toString());
    }

    public MidiDingsOSCParams(String inetAddress, int port, String cmd) throws UnknownHostException {
        setInetAddress(inetAddress);
        this.port = port;
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
}
