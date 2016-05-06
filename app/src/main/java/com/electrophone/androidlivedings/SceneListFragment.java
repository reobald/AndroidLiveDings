package com.electrophone.androidlivedings;

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


/**
 * Created by Patrik on 15-12-19.
 */
public class SceneListFragment extends Fragment {

    private static ListView sceneListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patch_list_fragment, container, false);
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
        //patchitemListener.updateCurrentScene(sceneInfo);

        try {

            MidiDingsOSCParams oscParams = new MidiDingsOSCParams("192.168.42.1", 56418, MidiDingsOSCParams.SWITCH_SCENE);
            oscParams.addParam(sceneInfo.getNumber());
            OSCTransmitter transmitter = new OSCTransmitter(getContext());
            transmitter.execute(oscParams);
        } catch (UnknownHostException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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