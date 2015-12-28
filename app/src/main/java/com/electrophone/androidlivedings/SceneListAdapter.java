package com.electrophone.androidlivedings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Patrik on 15-12-16.
 */
public class SceneListAdapter extends ArrayAdapter<String> {
    SceneListAdapter(Context context, String[] resource) {
        super(context, R.layout.patch_item_layout, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater l_inflater = LayoutInflater.from(getContext());
        View patchItemView = l_inflater.inflate(R.layout.patch_item_layout, parent, false);

        String patchNumber = String.format("%03d.", position);
        TextView patchNumberView = (TextView) patchItemView.findViewById(R.id.patchNumber);
        patchNumberView.setText(patchNumber);

        String patchName = getItem(position);
        TextView patchNameView = (TextView) patchItemView.findViewById(R.id.patchName);
        patchNameView.setText(patchName);

        return patchItemView;
    }
}
