package com.electrophone.androidlivedings;

/**
 * Created by Patrik on 15-12-21.
 */
public class MidiDingsOSCParams {

    //MidiDingsOSCParams OSC commands

    //output
    public static final String QUERY            = "/mididings/query";
    public static final String SWITCH_SCENE     = "/mididings/switch_scene";
    public static final String SWITCH_SUBSCENE  = "/mididings/switch_subscene";
    public static final String PREV_SCENE       = "/mididings/prev_scene";
    public static final String NEXT_SCENE       = "/mididings/next_scene";
    public static final String PREV_SUBSCENE    = "/mididings/prev_subscene";
    public static final String NEXT_SUBSCENE    = "/mididings/next_subscene";
    public static final String PANIC            = "/mididings/panic";
    public static final String QUIT             = "/mididings/quit";

    //input
    public static final String DATA_OFFSET      = "/mididings/data_offset";
    public static final String BEGIN_SCENES     = "/mididings/begin_scenes";
    public static final String ADD_SCENE        = "/mididings/add_scene";
    public static final String END_SCENES       = "/mididings/end_scenes";
    public static final String CURRENT_SCENE    = "/mididings/current_scene";
}
