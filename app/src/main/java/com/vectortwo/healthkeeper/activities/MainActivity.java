package com.vectortwo.healthkeeper.activities;

import android.content.Context;
import android.content.Intent;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.vectortwo.healthkeeper.*;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;
import com.vectortwo.healthkeeper.services.DrugNotifyService;

public class MainActivity extends AppCompatActivity {

    Button submitBt;
    EditText textDrugId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getContentResolver().query(DBContract.Weight.CONTENT_URI, null, null, null, null);

        textDrugId = (EditText) findViewById(R.id.drug_id);
        submitBt = (Button) findViewById(R.id.submit);

        final Context context = this;

        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DrugNotifyService.class);
                i.putExtra(DrugNotifyService.KEY_DRUG_ID, getDrugId());
                i.setAction(DrugNotifyService.ACTION_RESCHEDULE);

                startService(i);
            }
        });
    }

    private int getDrugId() {
        return Integer.parseInt(textDrugId.getText().toString());
    }
}