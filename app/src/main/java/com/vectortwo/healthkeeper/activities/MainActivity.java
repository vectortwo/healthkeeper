package com.vectortwo.healthkeeper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.vectortwo.healthkeeper.*;
import com.vectortwo.healthkeeper.data.TaskHandler;
import com.vectortwo.healthkeeper.data.DrugInfo;
import com.vectortwo.healthkeeper.data.LocationInfo;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;
import com.vectortwo.healthkeeper.receivers.DrugNotifyReceiver;
import com.vectortwo.healthkeeper.services.CheckArchiveService;
import com.vectortwo.healthkeeper.services.DrugNotifyService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button bt_schedule, bt_unschedule;
    EditText text;

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = getPreferences(MODE_PRIVATE);
        if (sharedPrefs.getBoolean(getString(R.string.prefs_first_launch), true)) {
            sharedPrefs
                    .edit()
                    .putBoolean(getString(R.string.prefs_first_launch), false)
                    .apply();
            Intent intent = new Intent(this, CheckArchiveService.class);
            startService(intent);
        }

        // to init db on startup; remove on release
        DrugColumns values = new DrugColumns();
        values.putTitle("DSA");
        getContentResolver().insert(DBContract.Drug.CONTENT_URI, values.getContentValues());
        //

        bt_schedule = (Button) findViewById(R.id.bt_schedule);
        bt_unschedule = (Button) findViewById(R.id.bt_unschedule);
        text = (EditText) findViewById(R.id.inDrugID);

        bt_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drugID = Integer.parseInt(text.getText().toString());

                Intent intent = new Intent(DrugNotifyService.ACTION_SCHEDULE);
                intent.putExtra(DBContract.Notify.DRUG_ID, drugID);

                sendBroadcast(intent);
            }
        });

        bt_unschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drugID = Integer.parseInt(text.getText().toString());

                Intent intent = new Intent(DrugNotifyService.ACTION_SCHEDULE);
                intent.putExtra(DBContract.Notify.DRUG_ID, drugID);

                sendBroadcast(intent);
            }
        });

    }
}