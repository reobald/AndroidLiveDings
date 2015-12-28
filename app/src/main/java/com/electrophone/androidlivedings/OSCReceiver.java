package com.electrophone.androidlivedings;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OSCReceiver extends Service {
    public static final String BROADCAST = "com.electrophone.androidlivedings.OSCReceiver.BROADCAST";
    public static final String UPDATE_CURRENT_SCENE = "UPDATE_CURRENT_SCENE";
    public static final String UPDATE_SCENES = "UPDATE_SCENES";
    public static final String PORT_NR = "oscInPortNumber";
    public static final int DEFAULT_DATA_OFFSET = 1;
    private final ServiceBinder myServiceBinder = new ServiceBinder();
    boolean updating = false;
    private String LOG_TAG = "com.electrophone.androidlivedings.OSCReceiver";
    private LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
    private OSCPortIn oscReceiver;
    private int oscInPortNr;
    private int dataOffset = DEFAULT_DATA_OFFSET;
    private ArrayList<SceneInfo> scenes = null;
    private ArrayList<SceneInfo> updatedScenes = null;
    private OSCListener dataOffsetListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            dataOffset = (int) message.getArguments().get(0);
            Log.d(LOG_TAG, logInfo("Incoming begin_scenes message", message));
        }
    };
    private OSCListener beginScenesListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            if (!updating) {
                updating = true;
                updatedScenes = new ArrayList<SceneInfo>();
                Log.d(LOG_TAG, logInfo("Incoming begin_scenes message", message));
            }
        }
    };
    private OSCListener addSceneListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            Log.d(LOG_TAG, logInfo("Incoming add_scene message", message));
            SceneInfo sceneInfo = createSceneInfoFromOscMessage(message);
            if (sceneInfo != null) {
                updatedScenes.add(sceneInfo);
            } else {
                Log.d(LOG_TAG, logInfo("Could not create SceneInfo from OSC message", message));
            }
        }
    };
    private OSCListener endScenesListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            if (updating) {
                scenes = updatedScenes;
                updating = false;
                broadcastUpdatedScenes(scenes);
                Log.d(LOG_TAG, logInfo("Incoming end_scenes message", message));
            }
        }
    };
    private OSCListener currentSceneListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            int currentSceneNr = (int) message.getArguments().get(0);
            currentSceneNr -= dataOffset;
            broadcastCurrentScene(scenes.get(currentSceneNr));
            Log.d(LOG_TAG, logInfo("incomingCurrentSceneMessage", message));
        }
    };

    public OSCReceiver() {
    }

    private void broadcastCurrentScene(SceneInfo sceneInfo) {
        Intent intent = new Intent(BROADCAST);
        intent.putExtra(UPDATE_CURRENT_SCENE, sceneInfo);
        broadcaster.sendBroadcast(intent);
    }

    private void broadcastUpdatedScenes(ArrayList<SceneInfo> scenes) {
        Intent intent = new Intent(BROADCAST);
        //intent.putParcelableArrayListExtra(UPDATE_SCENES, scenes);
        intent.putParcelableArrayListExtra(UPDATE_SCENES, scenes);
        broadcaster.sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Bundle extras = intent.getExtras();
        oscInPortNr = (int) extras.get(PORT_NR);

        startOSCserver();
        if (scenes == null) {
            try {
                MidiDingsOSCParams oscParams = new MidiDingsOSCParams("192.168.42.1", 56418, MidiDingsOSCParams.QUERY);
                OSCTransmitter transmitter = new OSCTransmitter(getBaseContext());
                transmitter.execute(oscParams);
            } catch (UnknownHostException e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT);
            }
        } else {
            broadcastUpdatedScenes(scenes);
        }

        return myServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopThreads();
        return super.onUnbind(intent);
    }

    private void startOSCserver() {
        try {

            oscReceiver = new OSCPortIn(oscInPortNr);

            Log.d(LOG_TAG, "Adding listeners to server");
            oscReceiver.addListener(MidiDingsOSCParams.DATA_OFFSET, dataOffsetListener);
            oscReceiver.addListener(MidiDingsOSCParams.BEGIN_SCENES, beginScenesListener);
            oscReceiver.addListener(MidiDingsOSCParams.ADD_SCENE, addSceneListener);
            oscReceiver.addListener(MidiDingsOSCParams.END_SCENES, endScenesListener);
            oscReceiver.addListener(MidiDingsOSCParams.CURRENT_SCENE, currentSceneListener);

            Log.d(LOG_TAG, "Starting OSC server");
            oscReceiver.startListening();
            Log.d(LOG_TAG, "OSC server started");

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        stopThreads();
        super.onDestroy();
    }

    public void stopThreads() {
        //stop all Threads
        if (oscReceiver != null) {
            oscReceiver.stopListening();
            oscReceiver.close();
            oscReceiver = null;
        }
    }

    public void stopService() {
        stopThreads();
        stopSelf();
    }

    public String logInfo(String s, OSCMessage msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(": ");
        sb.append(msg.getAddress());
        for (Object o : msg.getArguments()) {
            sb.append(" [");
            sb.append(o.toString());
            sb.append(']');
        }
        return sb.toString();
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

    private ArrayList<SceneInfo> generateTestData() {
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

    public ArrayList<SceneInfo> getSceneList() {
        return scenes;
    }

    public class ServiceBinder extends Binder {
        OSCReceiver getService() {
            return OSCReceiver.this;
        }
    }
}
