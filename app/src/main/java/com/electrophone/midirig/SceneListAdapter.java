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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class SceneListAdapter extends ArrayAdapter<SceneInfo> {
    SceneListAdapter(Context context, ArrayList<SceneInfo> resource) {
        super(context, R.layout.scene_item_layout, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SceneInfo sceneInfo = getItem(position);

        LayoutInflater l_inflater = LayoutInflater.from(getContext());
        View patchItemView = l_inflater.inflate(R.layout.scene_item_layout, parent, false);

        String patchNumber = String.format("%03d.", sceneInfo.getNumber());
        TextView patchNumberView = (TextView) patchItemView.findViewById(R.id.sceneNumber);
        patchNumberView.setText(patchNumber);

        TextView patchNameView = (TextView) patchItemView.findViewById(R.id.sceneName);
        patchNameView.setText(sceneInfo.getSceneName());

        return patchItemView;
    }
}
