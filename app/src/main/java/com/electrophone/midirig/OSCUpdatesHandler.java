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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
