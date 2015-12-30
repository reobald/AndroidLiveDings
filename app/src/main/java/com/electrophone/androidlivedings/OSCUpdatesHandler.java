package com.electrophone.androidlivedings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Patrik on 15-12-30.
 */
public class OSCUpdatesHandler extends Handler {
    private WeakReference<MainActivity> activity;

    public OSCUpdatesHandler(MainActivity activity) {
        super();
        this.activity = new WeakReference<MainActivity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle data = msg.getData();
        int action = data.getInt(OSCReceiver.ACTION);
        switch (action) {
            case OSCReceiver.UPDATE_CURRENT_SCENE:
                int currentScene = data.getInt(OSCReceiver.CURRENT_SCENE);
                activity.get().updateCurrentScene(currentScene);
                break;
            case OSCReceiver.UPDATE_SCENES:
                ArrayList<SceneInfo> scenes = data.getParcelableArrayList(OSCReceiver.ALL_SCENES);
                activity.get().updateSceneList(scenes);
                break;
        }
        super.handleMessage(msg);
    }

}
