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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SceneItemFragment extends Fragment {

    private static final String DELIMITER = ".";
    private static final String FORMAT = "%03d" + DELIMITER;

    private TextView sceneNumber;
    private TextView sceneName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.current_scene_layout, container, false);
        sceneNumber = (TextView) view.findViewById(R.id.sceneNumber);
        sceneName = (TextView) view.findViewById(R.id.sceneName);
        return view;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        String nr = String.format(FORMAT, sceneInfo.getNumber());
        this.sceneNumber.setText(nr);
        this.sceneName.setText(sceneInfo.getSceneName());
    }

    public int getSceneNumber() {
        String t = sceneNumber.getText().toString();
        String delimiters = DELIMITER;
        String[] tokens = t.split(delimiters);
        if (tokens.length > 0) {
            return Integer.getInteger(tokens[0]);
        } else return 0;
    }
}
