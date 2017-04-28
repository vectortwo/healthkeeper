package com.vectortwo.healthkeeper.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.vectortwo.healthkeeper.*;
import com.vectortwo.healthkeeper.data.pdf.PdfDoc;
import com.vectortwo.healthkeeper.services.PedometerService;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_EXTERNAL_STORAGE = 0;

    private Button bt_generate;

    // todo: sleep time approx based on accelerometer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        bt_generate = (Button) findViewById(R.id.bt_generate);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_EXTERNAL_STORAGE);

        bt_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    PdfDoc.generate(0);
                }
            }
        });

        Intent i = new Intent(this, PedometerService.class);
        stopService(i);
        startService(i);
    }
}