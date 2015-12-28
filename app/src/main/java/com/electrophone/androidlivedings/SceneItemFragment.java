package com.electrophone.androidlivedings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Patrik on 15-12-19.
 */
public class SceneItemFragment extends Fragment {

    private static TextView patchNumber;
    private static TextView patchName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patch_item_layout, container, false);
        patchNumber = (TextView) view.findViewById(R.id.patchNumber);
        patchName = (TextView) view.findViewById(R.id.patchName);
        return view;
    }

    public void setPatchInfo(int patchNumber, String patchName) {
        String nr = String.format("%03d.", patchNumber);
        this.patchNumber.setText(nr);
        this.patchName.setText(patchName);
    }
}
