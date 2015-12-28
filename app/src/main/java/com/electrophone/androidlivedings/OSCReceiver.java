package com.electrophone.androidlivedings;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OSCReceiver extends Service{
    private String LOG_TAG = "com.electrophone.androidlivedings.OSCReceiver";

    private static final String CURRENT_SCENE = "/mididings/current_scene";
    private static final String BEGIN_SCENES = "/mididings/begin_scenes";
    private static final String END_SCENES =  "/mididings/end_scenes";
    private static final String ADD_SCENE =  "/mididings/add_scene";


    private ArrayList<SceneInfo> scenes = null;
    private ArrayList<SceneInfo> updatedScenes = null;

    private boolean sceneListUpdated = false;

    private int currentSceneNr;
    private int latestSceneNr;



    private String oscInetAddr = "192.168.42.0.1";
    private int oscInPortNr = 5000;

    private OSCPortIn oscReceiver;

    private OSCListener currentSceneListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            incomingCurrentSceneMessage(message);
        }
    };
    private OSCListener beginScenesListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            incomingBeginScenesMessage(message);
        }
    };
    private OSCListener endScenesListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            incomingEndScenesMessage(message);
        }
    };
    private OSCListener addSceneListener = new OSCListener() {
        @Override
        public void acceptMessage(Date time, OSCMessage message) {
            incomingAddSceneMessage(message);
        }
    };


    public OSCReceiver() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        oscInetAddr = (String)extras.get("oscInetAddr");
        oscInPortNr = (int) extras.get("oscInPort");

        startOSCserver();
        return super.onStartCommand(intent, flags, startId);

    }

    private void startOSCserver() {
        try {
            oscReceiver = new OSCPortIn(oscInPortNr);
            Log.d(LOG_TAG, "Adding listeners to server");
            oscReceiver.addListener(CURRENT_SCENE, currentSceneListener);
            oscReceiver.addListener(BEGIN_SCENES, beginScenesListener);
            oscReceiver.addListener(ADD_SCENE, addSceneListener);
            oscReceiver.addListener(END_SCENES, endScenesListener);
            Log.d(LOG_TAG, "Starting OSC server");
            oscReceiver.startListening();
            Log.d(LOG_TAG,"OSC server started");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void incomingBeginScenesMessage(OSCMessage message) {
        updatedScenes = new ArrayList<SceneInfo>();
    }
    private void incomingAddSceneMessage(OSCMessage message) {
        //latestScene
    }
    private void incomingEndScenesMessage(OSCMessage message) {
        scenes = updatedScenes;
        updatedScenes = null;
        sceneListUpdated = true;
        //perform broadcast
    }
    private void incomingCurrentSceneMessage(OSCMessage message) {
        currentSceneNr =  (int)message.getArguments().get(0);
    }

    @Override
    public void onDestroy() {
        stopThreads();
        super.onDestroy();
    }

    private void stopThreads(){
        //stop all Threads
        if(oscReceiver !=null) {
            oscReceiver.stopListening();
            oscReceiver = null;
        }
    }

    public void stopService(){
        stopThreads();
        stopSelf();
    }

}
