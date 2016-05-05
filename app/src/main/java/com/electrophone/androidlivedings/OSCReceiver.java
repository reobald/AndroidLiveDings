package com.electrophone.androidlivedings;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OSCReceiver implements LogConstant {
    public static final String ACTION = "ACTION";
    public static final String CURRENT_SCENE = "CURRENT_SCENE";
    public static final String ALL_SCENES = "ALL_SCENES";
    public static final int UPDATE_SCENES = 0;
    public static final int UPDATE_CURRENT_SCENE = 1;


    public static final int DEFAULT_DATA_OFFSET = 1;

    boolean updating = false;
    private OSCPortIn oscPort;
    private int dataOffset = DEFAULT_DATA_OFFSET;
    private ArrayList<SceneInfo> updatedScenes = null;

    private MainActivity mainActivity;
    private OSCListener dataOffsetListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            dataOffset = (int) message.getArguments().get(0);
            mainActivity.setDataOffset(dataOffset);
            log("Incoming data_offset message", message);
        }
    };
    private OSCListener beginScenesListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            if (!updating) {
                updating = true;
                updatedScenes = new ArrayList<SceneInfo>();
                log("Incoming begin_scenes message", message);
            }
        }
    };
    private OSCListener addSceneListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            log("Incoming add_scene message", message);
            SceneInfo sceneInfo = createSceneInfoFromOscMessage(message);
            if (sceneInfo != null) {
                updatedScenes.add(sceneInfo);
            } else {
                log("Could not create SceneInfo from OSC message", message);
            }
        }
    };
    private OSCListener endScenesListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            log("Incoming end_scenes message", message);
            if (updating) {
                updating = false;
                sendUpdateScenesMessage();
            }
        }
    };
    private OSCListener currentSceneListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            log("incomingCurrentSceneMessage", message);
            int currentSceneNr = (int) message.getArguments().get(0);
            currentSceneNr -= dataOffset;
            sendUpdateCurrentSceneMessage(currentSceneNr);
        }
    };


    public OSCReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        startOSCserver();
    }

    public void startOSCserver() {
        try {
            int oscInPortNr = getOscInPortNr();
            oscPort = new OSCPortIn(oscInPortNr);

            log("Adding listeners to server");
            oscPort.addListener(MidiDingsOSCParams.DATA_OFFSET, dataOffsetListener);
            oscPort.addListener(MidiDingsOSCParams.BEGIN_SCENES, beginScenesListener);
            oscPort.addListener(MidiDingsOSCParams.ADD_SCENE, addSceneListener);
            oscPort.addListener(MidiDingsOSCParams.END_SCENES, endScenesListener);
            oscPort.addListener(MidiDingsOSCParams.CURRENT_SCENE, currentSceneListener);

            log("Starting OSC server");
            oscPort.startListening();
            log("OSC server started, listening on port number: " + Integer.toString(oscInPortNr));

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void restartOSCserver() {
        if (oscPort != null && oscPort.isListening()) {
            stop();
        }
        log("Retarting server");
        startOSCserver();
    }

    public void destroy() {
        log("Destroy: freeing up resources");
        if (oscPort != null) {
            if (oscPort.isListening()) {
                oscPort.stopListening();
            }
            oscPort.close();
            oscPort = null;
        }
    }

    public void stop() {
        if (oscPort != null) {
            log("Stopping running server");
            oscPort.stopListening();
            oscPort.close();
            oscPort = null;
        }

    }

    private int getOscInPortNr() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        int oscInPortNr = new Integer(prefs.getString(SettingsActivity.KEY_PREF_OSC_INPUT_PORT, ""));
        return oscInPortNr;
    }


    public SceneInfo createSceneInfoFromOscMessage(OSCMessage msg) {
        List oscArguments = msg.getArguments();
        int noOfArgs = oscArguments.size();

        boolean argsContainsScene = (noOfArgs >= 2);
        if (argsContainsScene) {

            int number;
            String name;
            ArrayList<SceneInfo> subscenes;

            number = (int) oscArguments.get(0);
            name = (String) oscArguments.get(1);
            subscenes = new ArrayList<>();

            for (int i = 2; i < noOfArgs; i++) {
                int subsceneNumber = i - 1;
                String subsceneName;
                subsceneName = (String) oscArguments.get(i);

                subscenes.add(new SceneInfo(subsceneNumber, subsceneName));
            }

            return new SceneInfo(number, name, subscenes);

        } else {
            return null;
        }
    }

    private void sendUpdateScenesMessage() {
        Bundle data = new Bundle();
        data.putInt(ACTION, UPDATE_SCENES);
        data.putParcelableArrayList(ALL_SCENES, updatedScenes);
        Message msg = new Message();
        msg.setData(data);
        Handler handler = mainActivity.getHandler();
        handler.sendMessage(msg);
    }

    private void sendUpdateCurrentSceneMessage(int currentScene) {
        Bundle data = new Bundle();
        data.putInt(ACTION, UPDATE_CURRENT_SCENE);
        data.putInt(CURRENT_SCENE, currentScene);
        Message msg = new Message();
        msg.setData(data);
        Handler handler = mainActivity.getHandler();
        handler.sendMessage(msg);
    }


    public void log(String s, OSCMessage msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(": ");
        sb.append(msg.getAddress());
        for (Object o : msg.getArguments()) {
            sb.append(" [");
            sb.append(o.toString());
            sb.append(']');
        }
        log(sb.toString());
    }

    private void log(String msg) {
        Log.d(LOG_TAG, msg);
    }

 /*   private ArrayList<SceneInfo> generateTestData() {
        String[] list = new String[]{
                "Rhodes", "Brass", "Lead", "Strings", "Organ", "Guitar",
                "Marimba", "Lead", "Bass", "Banjo", "Sitar", "Mandolin",
                "Pad", "Synth Bass", "Bells", "Chimes", "Vibraphone",
                "Wurlitzer", "Theremin"};

        String scene;
        int number;
        ArrayList<SceneInfo> subscenes;
        SceneInfo testDataItem;
        ArrayList<SceneInfo> testData = new ArrayList<>();


        for (int i = 0; i < list.length; i++) {
            scene = list[i];
            number = i;
            subscenes = new ArrayList<>();
            testDataItem = new SceneInfo(number, scene, subscenes);
            testData.add(testDataItem);
        }
        return testData;
    }
    */

}
