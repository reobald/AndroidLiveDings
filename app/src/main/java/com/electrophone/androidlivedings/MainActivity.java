package com.electrophone.androidlivedings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends Activity implements LogConstant {

    public static final String SAVED_SCENES = "SAVED_SCENES";
    public static final String SAVED_CURRENT_SCENE = "SAVED_CURRENT_SCENE";
    public static final String SAVED_DATA_OFFSET = "SAVED_DATA_OFFSET";
    OSCUpdatesHandler handler;
    private ArrayList<SceneInfo> scenes = null;
    private int dataOffset;
    private OSCReceiver oscReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPreferences();

        handler = new OSCUpdatesHandler(this);

        oscReceiver = new OSCReceiver(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(com.electrophone.androidlivedings.MainActivity.this, com.electrophone.androidlivedings.SettingsActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
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
            String oscRemoteHost = getOscRemoteHostFromPreferences();
            int oscOutPortNr = getOscOutPortNumberFromPreferences();
            MidiDingsOSCParams params = new MidiDingsOSCParams(oscRemoteHost, oscOutPortNr, MidiDingsOSCParams.QUERY);
            log("OSCparams: " + params.toString());
            transmitter.execute(params);
        } catch (UnknownHostException e) {
            log(e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public int getCurrentScene() {
        log("Get current scene");
        SceneItemFragment sceneItemFragment = (SceneItemFragment) getFragmentManager().findFragmentById(R.id.sceneItemFragment);
        int sceneNumber = sceneItemFragment.getSceneNumber() - dataOffset;
        return (sceneNumber < 0 ? 0 : sceneNumber);
    }

    public void updateCurrentScene(int sceneNumber) {
        log("Update current scene");
        SceneInfo sceneInfo = scenes.get(sceneNumber);
        SceneItemFragment sceneItemFragment = (SceneItemFragment) getFragmentManager().findFragmentById(R.id.sceneItemFragment);
        sceneItemFragment.setSceneInfo(sceneInfo);
    }

    public void updateSceneList(ArrayList<SceneInfo> sceneInfoList) {
        log("Update current scene list");
        scenes = sceneInfoList;
        SceneListFragment sceneListFragment = (SceneListFragment) getFragmentManager().findFragmentById(R.id.sceneListFragment);
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

    private void initPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    private String getOscRemoteHostFromPreferences() {
        return getPrefValueByKey(SettingsActivity.KEY_PREF_OSC_REMOTE_HOST, "");
    }

    private int getOscOutPortNumberFromPreferences() {
        String s = getPrefValueByKey(SettingsActivity.KEY_PREF_OSC_OUTPUT_PORT, "");
        return new Integer(s);
    }

    private String getPrefValueByKey(String key, String defaultValue) {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(key, defaultValue);
    }

}

