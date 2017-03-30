package com.vectortwo.healthkeeper.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.vectortwo.healthkeeper.*;
import com.vectortwo.healthkeeper.services.PedometerService;

public class MainActivity extends AppCompatActivity {

    Button unregisterBt, registerBt;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, PedometerService.class);

        registerBt = (Button) findViewById(R.id.bt_register);
        unregisterBt = (Button) findViewById(R.id.bt_unregister);
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
                startService(intent);
            }
        });

        unregisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });
    }
}