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

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SceneItemFragment extends Fragment {

    private static final String DELIMITER = ".";
    private static final String FORMAT = "%03d" + DELIMITER;

    private TextView sceneNumber;
    private TextView sceneName;

    private int currentScene = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.current_scene_layout, container, false);
        sceneNumber = (TextView) view.findViewById(R.id.sceneNumber);
        sceneName = (TextView) view.findViewById(R.id.sceneName);

        sceneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LogConstant.LOG_TAG, "Sort alphabetically");
                sortAlphabetically(true);
            }
        });

        sceneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LogConstant.LOG_TAG, "Sort numerically");
                sortAlphabetically(false);
            }
        });

        return view;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        currentScene = sceneInfo.getNumber();
        String nr = String.format(FORMAT, sceneInfo.getNumber());
        this.sceneNumber.setText(nr);
        this.sceneName.setText(sceneInfo.getSceneName());
    }

    public int getSceneNumber() {
        return currentScene;
    }

    public void sortAlphabetically(boolean choice) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SettingsActivity.KEY_PREF_ALPHABETICAL_SORT, choice);
        editor.apply();
        ((MainActivity) getActivity()).updateScenelistFragment();
    }
}
