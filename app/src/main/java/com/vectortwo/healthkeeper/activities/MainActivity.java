package com.vectortwo.healthkeeper.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.vectortwo.healthkeeper.*;
import com.vectortwo.healthkeeper.data.TaskHandler;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;
import com.vectortwo.healthkeeper.data.db.TempDBPopulate;
import com.vectortwo.healthkeeper.data.pdf.PdfDoc;
import com.vectortwo.healthkeeper.services.PedometerService;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_EXTERNAL_STORAGE = 0;

    private Button bt_generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        bt_generate = (Button) findViewById(R.id.bt_generate);


        bt_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    generatePDF();
                } else if (permission == PackageManager.PERMISSION_DENIED) {
                    requestPermission();
                }
            }
        });

        /*
        Intent i = new Intent(this, PedometerService.class);
        stopService(i);
        startService(i);
        */

        TempDBPopulate.fill(context);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_EXTERNAL_STORAGE &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            generatePDF();
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Error! Need a permission to access storage", Toast.LENGTH_LONG).show();
        }
    }

    void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_EXTERNAL_STORAGE);
    }

    void generatePDF() {
        int pages = PdfDoc.PAGE_STEPS | PdfDoc.PAGE_BLOODPRESSURE;

        final Context cntx = this;

        final Calendar minDate = Calendar.getInstance();
        minDate.set(2017, 3, 10);
        final Calendar maxDate = (Calendar) minDate.clone();
        maxDate.set(2017, 5, 10);

        PdfDoc doc = new PdfDoc(this, pages, minDate, maxDate);
        doc.addHandler(new TaskHandler<Void>() {
            @Override
            public void onPostExecute(Void r) {
                Toast.makeText(cntx, "PDF generated", Toast.LENGTH_LONG).show();
            }
        });
        doc.execute();
    }
}