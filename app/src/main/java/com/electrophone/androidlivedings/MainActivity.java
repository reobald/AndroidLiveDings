package com.electrophone.androidlivedings;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = "MainActivity";
    OSCReceiver oscReceiver;
    boolean isBound = false;
    private BroadcastReceiver receiver;
    private ServiceConnection myOscServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            OSCReceiver.ServiceBinder binder = (OSCReceiver.ServiceBinder) service;
            oscReceiver = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SceneInfo currentScene = intent.getParcelableExtra(OSCReceiver.UPDATE_CURRENT_SCENE);
                if (currentScene != null) {
                    updateCurrentScene(currentScene);
                    return;
                }

                ArrayList<SceneInfo> sceneInfoList = intent.getParcelableArrayListExtra(OSCReceiver.UPDATE_SCENES);
                if (sceneInfoList != null) {
                    updateSceneList(sceneInfoList);
                    return;
                }
            }
        };
        this.bindToService();
    }

    public void bindToService() {
        Intent serviceIntent = new Intent(getBaseContext(), OSCReceiver.class);
        serviceIntent.putExtra(OSCReceiver.PORT_NR, 56419);
        Log.d(LOG_TAG, "Calling bindToService(Intent)");
        bindService(serviceIntent, myOscServiceConnection, BIND_AUTO_CREATE);
    }

    public void updateCurrentScene(SceneInfo sceneInfo) {
        SceneItemFragment sceneItemFragment = (SceneItemFragment) getSupportFragmentManager().findFragmentById(R.id.sceneItemFragment);
        sceneItemFragment.setSceneInfo(sceneInfo);
    }

    public void updateSceneList(ArrayList<SceneInfo> sceneInfoList) {
        SceneListFragment sceneListFragment = (SceneListFragment) getSupportFragmentManager().findFragmentById(R.id.sceneListFragment);
        sceneListFragment.setSceneList(sceneInfoList);
    }

    @Override
    protected void onStart() {

        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(OSCReceiver.BROADCAST));
        super.onStart();
    }


    @Override
    protected void onStop() {
        unbindService(myOscServiceConnection);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        oscReceiver.stopThreads();
        super.onDestroy();
    }


}

