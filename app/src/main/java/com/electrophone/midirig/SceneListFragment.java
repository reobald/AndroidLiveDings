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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;


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
        } catch (UnknownHostException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void setSceneList(ArrayList<SceneInfo> sceneList) {
        SceneListAdapter sceneListAdapter = new SceneListAdapter(getActivity(), sceneList);
        sceneListView.setAdapter(sceneListAdapter);
        sceneListAdapter.notifyDataSetChanged();
    }
}