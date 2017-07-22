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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.util.ArrayList;


public class OSCTransmitter extends AsyncTask<Object, Void, Boolean> implements LogConstant {

    private Context context;

    public OSCTransmitter(Context context) {
        this.context = context;

    }

    @Override
    protected Boolean doInBackground(Object[] inputParameters) {

        try {
            if (connected()) {
                MidiDingsOSCParams oscInputParams;
                String cmd;
                InetAddress ip;
                int port;
                ArrayList outputParams;

                oscInputParams = (MidiDingsOSCParams) inputParameters[0];

                ip = InetAddress.getByName(oscInputParams.getInetAddress());
                port = oscInputParams.getPort();

                cmd = oscInputParams.getCmd();
                outputParams = oscInputParams.getParams();

                OSCPortOut transmitter = new OSCPortOut(ip, port);
                OSCMessage msg = new OSCMessage(cmd, outputParams);
                Log.d(LOG_TAG, msg.getAddress().toString() + ", " + msg.getArguments().toString());

                transmitter.send(msg);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
            return false;
        }
    }

    private boolean connected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Log.d(LOG_TAG, "Not connected");
            return false;
        } else {
            return true;
        }
    }

}
