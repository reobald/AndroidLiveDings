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

    private static TextView sceneNumber;
    private static TextView sceneName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patch_item_layout, container, false);
        sceneNumber = (TextView) view.findViewById(R.id.sceneNumber);
        sceneName = (TextView) view.findViewById(R.id.sceneName);
        return view;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        String nr = String.format("%03d.", sceneInfo.getNumber());
        this.sceneNumber.setText(nr);
        this.sceneName.setText(sceneInfo.getSceneName());
    }
}
