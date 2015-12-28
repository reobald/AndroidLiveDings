package com.electrophone.androidlivedings;

import android.app.Activity;
import android.app.Notification;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] list = new String[]{"Rhodes", "Brass", "Lead", "Strings", "Organ", "Guitar"};
        ListAdapter patchListAdapter = new PatchListAdapter(this, list);
        ListView patchListView = (ListView) findViewById(R.id.patchListView);
        patchListView.setAdapter(patchListAdapter);
        patchListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String patch = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(MainActivity.this, patch, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}

