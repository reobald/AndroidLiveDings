package com.electrophone.androidlivedings;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LogConstant {

    public static final String SAVED_SCENES = "SAVED_SCENES";
    public static final String SAVED_CURRENT_SCENE = "SAVED_CURRENT_SCENE";
    public static final String SAVED_DATA_OFFSET = "SAVED_DATA_OFFSET";
    OSCUpdatesHandler handler;
    private ArrayList<SceneInfo> scenes = null;
    private int oscInPortNumber = 56419;
    private int oscOutPortNumber = 56418;
    private String oscRemoteHost = "192.168.42.1";
    private int dataOffset;
    private OSCReceiver oscReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new OSCUpdatesHandler(this);

        oscReceiver = new OSCReceiver(this, oscInPortNumber);
        oscReceiver.startOSCserver();
        try {
//            wait for server to become active
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (savedInstanceState != null) {

            scenes = savedInstanceState.getParcelableArrayList(SAVED_SCENES);

            if (scenes != null) {
                log("Recreating saved instance state");
                dataOffset = savedInstanceState.getInt(SAVED_DATA_OFFSET);
                setDataOffset(dataOffset);

                int currentScene = savedInstanceState.getInt(SAVED_CURRENT_SCENE);
                updateCurrentScene(currentScene);
                updateSceneList(scenes);
            }

        } else {
            log("transmit query");
            transmitQuery();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        log("Saving instancs state");
        outState.putParcelableArrayList(SAVED_SCENES, scenes);
        outState.putInt(SAVED_CURRENT_SCENE, getCurrentScene());
        outState.putInt(SAVED_DATA_OFFSET, dataOffset);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        log("Restarting");
        super.onRestart();
        oscReceiver.restartOSCserver();
    }

    @Override
    protected void onStop() {
        log("Stopping");
        super.onStop();
        oscReceiver.stop();
    }

    @Override
    protected void onDestroy() {
        log("destroying");
        super.onDestroy();
        oscReceiver.destroy();
    }

    private void transmitQuery() {
        OSCTransmitter transmitter = new OSCTransmitter(this);
        try {
            MidiDingsOSCParams params = new MidiDingsOSCParams(oscRemoteHost, oscOutPortNumber, MidiDingsOSCParams.QUERY);
            log("OSCparams: " + params.toString());
            transmitter.execute(params);
        } catch (UnknownHostException e) {
            log(e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public int getCurrentScene() {
        log("Get current scene");
        SceneItemFragment sceneItemFragment = (SceneItemFragment) getSupportFragmentManager().findFragmentById(R.id.sceneItemFragment);
        int sceneNumber = sceneItemFragment.getSceneNumber() - dataOffset;
        return (sceneNumber < 0 ? 0 : sceneNumber);
    }

    public void updateCurrentScene(int sceneNumber) {
        log("Update current scene");
        SceneInfo sceneInfo = scenes.get(sceneNumber);
        SceneItemFragment sceneItemFragment = (SceneItemFragment) getSupportFragmentManager().findFragmentById(R.id.sceneItemFragment);
        sceneItemFragment.setSceneInfo(sceneInfo);
    }

    public void updateSceneList(ArrayList<SceneInfo> sceneInfoList) {
        log("Update current scene list");
        scenes = sceneInfoList;
        SceneListFragment sceneListFragment = (SceneListFragment) getSupportFragmentManager().findFragmentById(R.id.sceneListFragment);
        sceneListFragment.setSceneList(sceneInfoList);
    }

    public Handler getHandler() {
        return handler;
    }

    public void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }

    private void log(String msg) {
        Log.d(LOG_TAG, msg);
    }

}

