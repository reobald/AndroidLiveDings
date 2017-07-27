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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SceneInfoMap implements Parcelable {

    public static final Parcelable.Creator<SceneInfoMap> CREATOR
            = new Parcelable.Creator<SceneInfoMap>() {
        public SceneInfoMap createFromParcel(Parcel in) {
            return new SceneInfoMap(in);
        }

        public SceneInfoMap[] newArray(int size) {
            return new SceneInfoMap[size];
        }
    };
    HashMap<Integer, SceneInfo> map = null;

    public SceneInfoMap() {
        map = new HashMap<Integer, SceneInfo>();
    }

    public SceneInfoMap(Parcel in) {
        super();
        SceneInfo si = null;
        int size = in.readInt();
        HashMap<Integer, SceneInfo> tmpMap = new HashMap<Integer, SceneInfo>(size);
        for (int i = 0; i < size; i++) {
            si = new SceneInfo(in);
            tmpMap.put(si.getNumber(), si);
        }
        map = tmpMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Collection<SceneInfo> c = (Collection<SceneInfo>) values();
        dest.writeInt(c.size());
        for (SceneInfo si : c) {
            si.writeToParcel(dest, flags);
        }
    }

    public void put(SceneInfo si) {
        map.put(si.getNumber(), si);
    }

    public SceneInfo get(int i) {
        return map.get(i);
    }

    public boolean isEmpty() {
        return ((map == null) || (map.size() == 0));
    }

    public ArrayList<SceneInfo> values() {
        return new ArrayList<SceneInfo>(map.values());
    }

}
