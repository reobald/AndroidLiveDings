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
import java.util.Comparator;


public class SceneInfo implements Parcelable {

    public static final Parcelable.Creator<SceneInfo> CREATOR
            = new Parcelable.Creator<SceneInfo>() {
        public SceneInfo createFromParcel(Parcel in) {
            return new SceneInfo(in);
        }

        public SceneInfo[] newArray(int size) {
            return new SceneInfo[size];
        }
    };
    public static Comparator<SceneInfo> alphabeticalComparator = new Comparator<SceneInfo>() {
        @Override
        public int compare(SceneInfo lhs, SceneInfo rhs) {
            String a = lhs.getSceneName();
            String b = rhs.getSceneName();
            return a.compareToIgnoreCase(b);
        }
    };
    public static Comparator<SceneInfo> numericalComparator = new Comparator<SceneInfo>() {
        @Override
        public int compare(SceneInfo lhs, SceneInfo rhs) {
            Integer a = lhs.getNumber();
            Integer b = rhs.getNumber();
            return a.compareTo(b);
        }
    };
    private int number;
    private String sceneName;
    private ArrayList<SceneInfo> subscenes;


    public SceneInfo(Parcel in) {
        number = in.readInt();
        sceneName = in.readString();
        int numberOfSubScenes = in.readInt();

        subscenes = new ArrayList<>(numberOfSubScenes);
        for (int i = 0; i < numberOfSubScenes; i++) {
            int subsceneNumber = in.readInt();
            String subSceneName = in.readString();
            subscenes.add(new SceneInfo(subsceneNumber, subSceneName));
        }

    }

    public SceneInfo(int number, String scene) {
        this(number, scene, new ArrayList<SceneInfo>());
    }

    public SceneInfo(int number, String scene, ArrayList<SceneInfo> subscenes) {
        this.number = number;
        this.sceneName = scene;
        this.subscenes = subscenes;
    }

    public ArrayList<SceneInfo> getSubscenes() {
        return subscenes;
    }

    public int getNumber() {
        return number;
    }

    public String getSceneName() {
        return sceneName;
    }

    public SceneInfo getSubscene(int nr) {
        return subscenes.get(nr);
    }

    public void addSubscene(SceneInfo subscene) {
        subscenes.add(subscene);
    }

    @Override
    public boolean equals(Object o) {
        try {
            SceneInfo input = (SceneInfo) o;
            ArrayList<SceneInfo> inputSubscenes = getSubscenes();
            if (inputSubscenes.size() != subscenes.size()) {
                return false;
            }
            for (int i = 0; i < subscenes.size(); i++) {
                SceneInfo a = subscenes.get(i);
                SceneInfo b = inputSubscenes.get(i);
                if (!a.equals(b)) {
                    return false;
                }
            }
            return super.equals(o);
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        int lastRound = getSubscenes().size();
        int counter = 0;

        sb.append(" {");
        for (SceneInfo subscene : getSubscenes()) {
            sb.append(subscene.toString());
            if (counter != lastRound) {
                sb.append(", ");
            }
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(sceneName);
        dest.writeInt(subscenes.size());
        for (SceneInfo subscene : subscenes) {
            dest.writeInt(subscene.getNumber());
            dest.writeString(subscene.getSceneName());
        }
    }

}
