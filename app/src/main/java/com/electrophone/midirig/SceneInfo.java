package com.electrophone.midirig;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Patrik on 15-12-20.
 */
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
