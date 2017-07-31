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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;


public class SceneListFragment extends Fragment {

    private static ListView sceneListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scene_list_fragment, container, false);
        sceneListView = (ListView) view.findViewById(R.id.sceneListView);
        sceneListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SceneInfo sceneInfo = (SceneInfo) parent.getItemAtPosition(position);
                        selectScene(sceneInfo);
                    }
                }
        );
        return view;

    }

    private void selectScene(SceneInfo sceneInfo) {

        try {

            MidiDingsOSCParams oscParams = new MidiDingsOSCParams(getActivity(), MidiDingsOSCParams.SWITCH_SCENE);
            oscParams.addParam(sceneInfo.getNumber());
            OSCTransmitter transmitter = new OSCTransmitter(getActivity());
            transmitter.execute(oscParams);


            ((MainActivity) getActivity()).updateCurrentScene(sceneInfo.getNumber());
        } catch (UnknownHostException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setSceneList(ArrayList<SceneInfo> sceneList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(sceneListView.getContext());
        boolean sortAlphabetically = prefs.getBoolean(SettingsActivity.KEY_PREF_ALPHABETICAL_SORT, false);
        if (sortAlphabetically) {
            Collections.sort(sceneList, SceneInfo.alphabeticalComparator);
        } else {
            Collections.sort(sceneList, SceneInfo.numericalComparator);
        }
        SceneListAdapter sceneListAdapter = new SceneListAdapter(getActivity(), sceneList);
        sceneListView.setAdapter(sceneListAdapter);
        sceneListAdapter.notifyDataSetChanged();
    }

    private boolean isDemoMode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((getActivity()));
        return prefs.getBoolean(SettingsActivity.KEY_PREF_DEMO_MODE, false);
    }
}