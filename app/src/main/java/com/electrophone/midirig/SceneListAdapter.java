package com.electrophone.midirig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Patrik on 15-12-16.
 */
public class SceneListAdapter extends ArrayAdapter<SceneInfo> {
    SceneListAdapter(Context context, ArrayList<SceneInfo> resource) {
        super(context, R.layout.patch_item_layout, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SceneInfo sceneInfo = getItem(position);

        LayoutInflater l_inflater = LayoutInflater.from(getContext());
        View patchItemView = l_inflater.inflate(R.layout.patch_item_layout, parent, false);

        String patchNumber = String.format("%03d.", sceneInfo.getNumber());
        TextView patchNumberView = (TextView) patchItemView.findViewById(R.id.sceneNumber);
        patchNumberView.setText(patchNumber);

        TextView patchNameView = (TextView) patchItemView.findViewById(R.id.sceneName);
        patchNameView.setText(sceneInfo.getSceneName());

        return patchItemView;
    }
}
