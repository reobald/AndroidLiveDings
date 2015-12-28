package com.electrophone.androidlivedings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * Created by Patrik on 15-12-19.
 */
public class SceneListFragment extends Fragment {

    private static ListView patchListView;
    PatchItemListener patchitemListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            patchitemListener = (PatchItemListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patch_list_fragment, container, false);

        String[] list = new String[]{"Rhodes", "Brass", "Lead", "Strings", "Organ", "Guitar", "Marimba", "Lead", "Bass", "Banjo", "Sitar", "Mandolin", "Pad", "Synth Bass", "Bells", "Chimes", "Vibraphone", "Wurlitzer", "Theremin"};
        ListAdapter patchListAdapter = new PatchListAdapter(getActivity(), list);

        patchListView = (ListView) view.findViewById(R.id.patchListView);
        patchListView.setAdapter(patchListAdapter);
        patchListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int patchNumber = position;
                        String patchName = parent.getItemAtPosition(position).toString();
                        patchitemListener.updatePatchItem(patchNumber, patchName);
                    }
                }
        );
        return view;
    }


    public interface PatchItemListener {
        public void updatePatchItem(int patchNumber, String patchName);
    }
}