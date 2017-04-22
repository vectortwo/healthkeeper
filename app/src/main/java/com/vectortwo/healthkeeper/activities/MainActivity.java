package com.vectortwo.healthkeeper.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.vectortwo.healthkeeper.*;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;
import com.vectortwo.healthkeeper.services.DrugArchiveService;
import com.vectortwo.healthkeeper.services.DrugNotifyService;
import com.vectortwo.healthkeeper.services.PedometerService;

public class MainActivity extends AppCompatActivity {

    Button bt_schedule, bt_unschedule, bt_dropall;
    EditText text;

    SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        // Start pedometer
        Intent pedometerService = new Intent(this, PedometerService.class);
        stopService(pedometerService);
        startService(pedometerService);
        //

        // Activate drug archiver
        sharedPrefs = getPreferences(MODE_PRIVATE);
        if (sharedPrefs.getBoolean(getString(R.string.prefs_first_launch), true)) {
            sharedPrefs
                    .edit()
                    .putBoolean(getString(R.string.prefs_first_launch), false)
                    .apply();
            Intent intent = new Intent(this, DrugArchiveService.class);
            startService(intent);
        }

        // Init db on startup; remove on release
        DrugColumns values = new DrugColumns();
        values.putTitle("sdasd");
        getContentResolver().insert(DBContract.Drug.CONTENT_URI, values.getContentValues());
        //

        bt_schedule = (Button) findViewById(R.id.bt_schedule);
        bt_unschedule = (Button) findViewById(R.id.bt_unschedule);
        bt_dropall = (Button) findViewById(R.id.bt_dropall);
        text = (EditText) findViewById(R.id.inDrugID);

        bt_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drugID = Integer.parseInt(text.getText().toString());

                Intent intent = new Intent(context, DrugNotifyService.class);
                intent.setAction(DrugNotifyService.ACTION_SCHEDULE);
                intent.putExtra(DBContract.Intake.DRUG_ID, drugID);
                startService(intent);
            }
        });

        bt_unschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drugID = Integer.parseInt(text.getText().toString());

                Intent intent = new Intent(context, DrugNotifyService.class);
                intent.setAction(DrugNotifyService.ACTION_CANCEL);
                intent.putExtra(DBContract.Intake.DRUG_ID, drugID);
                startService(intent);
            }
        });
    }
}