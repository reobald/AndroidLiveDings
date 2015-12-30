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

    private static final String DELIMITER = ".";
    private static final String FORMAT = "%03d" + DELIMITER;

    private TextView sceneNumber;
    private TextView sceneName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.patch_item_layout, container, false);
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
