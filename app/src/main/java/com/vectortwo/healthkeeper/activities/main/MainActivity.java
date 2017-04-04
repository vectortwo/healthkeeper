/**
 * Главное окно
 */
package com.vectortwo.healthkeeper.activities.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import com.vectortwo.healthkeeper.R;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.vectortwo.healthkeeper.activities.PrefManager;
import com.vectortwo.healthkeeper.activities.aqua.AquaActivity;
import com.vectortwo.healthkeeper.activities.blood.BloodSugarActivity;
import com.vectortwo.healthkeeper.activities.medicament.MedicamentActivity;
import com.vectortwo.healthkeeper.activities.menu_settings.AboutActivity;
import com.vectortwo.healthkeeper.activities.menu_settings.ElementsSettingsActivity;
import com.vectortwo.healthkeeper.activities.menu_settings.NotificationSettingsActivity;
import com.vectortwo.healthkeeper.activities.menu_settings.SettingsActivity;
import com.vectortwo.healthkeeper.activities.pressure.PressureActivity;
import com.vectortwo.healthkeeper.activities.sleep.SleepActivity;
import com.vectortwo.healthkeeper.activities.walking.WalkingActivity;
import com.vectortwo.healthkeeper.activities.weight.WeightActivity;
import com.vectortwo.healthkeeper.services.PedometerService;

public class MainActivity extends AppCompatActivity{
    private PrefManager prefManager;
    public Intent intentPedometr;
    //Main elements buttons
    Button btn_medicament, btn_walking, btn_pressure, btn_weight, btn_sugar, btn_aqua, btn_sleep, btn_play_again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intentPedometr = new Intent(this, PedometerService.class);

        //ContentProvider db = new DBContentProvider();

        btn_medicament = (Button) findViewById(R.id.btn_medicament);
        btn_walking = (Button) findViewById(R.id.btn_walking);
        btn_pressure = (Button) findViewById(R.id.btn_pressure);
        btn_weight = (Button) findViewById(R.id.btn_weight);
        btn_sugar = (Button) findViewById(R.id.btn_sugar);
        btn_aqua = (Button) findViewById(R.id.btn_aqua);
        btn_sleep = (Button) findViewById(R.id.btn_sleep);
        btn_play_again = (Button) findViewById(R.id.btn_play_again);
        Button btn7 = (Button)findViewById(R.id.btn_7);

        View.OnClickListener menuButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_medicament:
                        startActivity(new Intent(MainActivity.this, MedicamentActivity.class));
                        break;
                    case R.id.btn_walking:
                        startActivity(new Intent(MainActivity.this, WalkingActivity.class));
                        break;
                    case R.id.btn_pressure:
                        startActivity(new Intent(MainActivity.this, PressureActivity.class));
                        break;
                    case R.id.btn_weight:
                        startActivity(new Intent(MainActivity.this, WeightActivity.class));
                        break;
                    case R.id.btn_sugar:
                        startActivity(new Intent(MainActivity.this, BloodSugarActivity.class));
                        break;
                    case R.id.btn_aqua:
                        startActivity(new Intent(MainActivity.this, AquaActivity.class));
                        break;
                    case R.id.btn_sleep:
                        startActivity(new Intent(MainActivity.this, SleepActivity.class));
                        break;
                    case R.id.btn_play_again: { //Play INTRO again
                        PrefManager prefManager = new PrefManager(getApplicationContext());
                        // make first time launch TRUE
                        prefManager.setFirstTimeLaunch(true);
                        startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                        finish();
                        break;
                    }
                    case R.id.btn_7:
                        stopService(intentPedometr);
                        startService(intentPedometr);
                        Log.w("Pedomete", "ON");
                        break;
                }
            }
        };

        //Start pedometr
        prefManager = new PrefManager(this);

        if(prefManager.isPedometrOn()){

            stopService(intentPedometr);
            startService(intentPedometr);
            Log.w("Pedomete", "ON");
        }else {
            Log.w("Pedomete", "OFF");
            stopService(intentPedometr);
        }

        btn_medicament.setOnClickListener(menuButtonClickListener);
        btn_walking.setOnClickListener(menuButtonClickListener);
        btn_pressure.setOnClickListener(menuButtonClickListener);
        btn_weight.setOnClickListener(menuButtonClickListener);
        btn_sugar.setOnClickListener(menuButtonClickListener);
        btn_aqua.setOnClickListener(menuButtonClickListener);
        btn_sleep.setOnClickListener(menuButtonClickListener);
        btn_play_again.setOnClickListener(menuButtonClickListener);
        btn7.setOnClickListener(menuButtonClickListener);
        //final Uri CONTACT_URI = Uri.parse("content://com.vectortwo.healthkeeper.HealthProvider");
        //Cursor cursor = getContentResolver().query(CONTACT_URI, null, null, null, null);
        //startManagingCursor(cursor);
        //ContentProvider();

        //ContentProvider contentProvider = new DBContentProvider()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.main_menu_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.main_menu_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.main_menu_elements:
                startActivity(new Intent(MainActivity.this, ElementsSettingsActivity.class));
                break;
            case R.id.main_menu_notification:
                startActivity(new Intent(MainActivity.this, NotificationSettingsActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
