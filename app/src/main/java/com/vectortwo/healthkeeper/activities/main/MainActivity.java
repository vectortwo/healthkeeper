/**
 * Главное окно
 */
package com.vectortwo.healthkeeper.activities.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import com.vectortwo.healthkeeper.R;
import android.content.Intent;
import android.view.View;
import com.vectortwo.healthkeeper.activities.PrefManager;
import com.vectortwo.healthkeeper.activities.aqua.AquaActivity;
import com.vectortwo.healthkeeper.activities.blood.BloodSugarActivity;
import com.vectortwo.healthkeeper.activities.medicament.MedicamentActivity;
import com.vectortwo.healthkeeper.activities.menu_settings.*;
import com.vectortwo.healthkeeper.activities.pressure.PressureActivity;
import com.vectortwo.healthkeeper.activities.sleep.SleepActivity;
import com.vectortwo.healthkeeper.activities.walking.WalkingActivity;
import com.vectortwo.healthkeeper.activities.weight.WeightActivity;
import com.vectortwo.healthkeeper.data.BackendPrefManager;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.WellBeingColumns;
import com.vectortwo.healthkeeper.services.DrugArchiveExpiredService;
import com.vectortwo.healthkeeper.services.DrugArchiveService;
import com.vectortwo.healthkeeper.services.PedometerService;
import com.vectortwo.healthkeeper.utilities.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private MainListAdapter mainListAdapter;
    private List<MainList> mainLists;
    private utils useUtils;
    private PrefManager prefManager;

    //Main elements buttons
    Button btn_medicament, btn_walking, btn_pressure, btn_weight, btn_sugar, btn_aqua, btn_sleep, btn_play_again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); //activity_main
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        mainLists = new ArrayList<>();
        mainListAdapter = new MainListAdapter(this, mainLists);
        useUtils = new utils(this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mainListAdapter);
        MainList item;


        item = new MainList(mainListAdapter.TYPE_NAME_FITNESS);
        mainLists.add(item);

        Cursor cursorLastDay = getContentResolver().query(DBContract.WellBeing.CONTENT_URI,
                new String[] { DBContract.WellBeing.DATE},
                null, null,
                "date(" + DBContract.WellBeing.DATE + ") desc limit 1");
        Calendar lastDate = null; // when wellbeing table is empty (no date exist) lastDate is null
        while (cursorLastDay.moveToNext()) {
            lastDate = WellBeingColumns.getDate(cursorLastDay);
        }
        cursorLastDay.close();
        Calendar currentCal = Calendar.getInstance();

        if(lastDate!=null) {
            if (lastDate.get(Calendar.YEAR) <= currentCal.get(Calendar.YEAR)
                    && lastDate.get(Calendar.DAY_OF_YEAR) < currentCal.get(Calendar.DAY_OF_YEAR)) {

                item = new MainList(mainListAdapter.TYPE_NAME_FEELING);
                mainLists.add(item);
            }
        }else{
            item = new MainList(mainListAdapter.TYPE_NAME_FEELING);
            mainLists.add(item);
        }




        int smallElementQuantity = 0;

        if(prefManager.getMedicametVisible()) {
            item = new MainList(
                    getResources().getString(R.string.medicament),
                    R.drawable.img_medicament,
                    prefManager.getMedicametVisible(), mainListAdapter.TYPE_NAME_MAIN);
            mainLists.add(item);
            smallElementQuantity+=1;
        }
        if(prefManager.getBloodSugarVisible()) {
            item = new MainList(
                    getResources().getString(R.string.blood_sugar),
                    R.drawable.img_blood,
                    prefManager.getBloodSugarVisible(), mainListAdapter.TYPE_NAME_MAIN);
            mainLists.add(item);
            smallElementQuantity+=1;
        }
        if(prefManager.getAquaVisible()) {
            item = new MainList(
                    getResources().getString(R.string.aqua),
                    R.drawable.img_aqua,
                    prefManager.getAquaVisible(), mainListAdapter.TYPE_NAME_MAIN);
            mainLists.add(item);
            smallElementQuantity+=1;
        }
        if(prefManager.getWalkingVisible()) {
            item = new MainList(
                    getResources().getString(R.string.walking),
                    R.drawable.img_walking,
                    prefManager.getWalkingVisible(), mainListAdapter.TYPE_NAME_MAIN);
            mainLists.add(item);
            smallElementQuantity+=1;
        }
        if(prefManager.getPressureVisible()) {
            item = new MainList(
                    getResources().getString(R.string.pressure),
                    R.drawable.img_pressure,
                    prefManager.getPressureVisible(), mainListAdapter.TYPE_NAME_MAIN);
            mainLists.add(item);
            smallElementQuantity+=1;
        }
        if(prefManager.getWeightVisible()) {
            item = new MainList(
                    getResources().getString(R.string.weight),
                    R.drawable.img_weight,
                    prefManager.getWeightVisible(), mainListAdapter.TYPE_NAME_MAIN);
            mainLists.add(item);
            smallElementQuantity+=1;
        }
        if(prefManager.getSleepVisible()) {
            item = new MainList(
                    getResources().getString(R.string.sleep),
                    R.drawable.img_sleep,
                    prefManager.getSleepVisible(), mainListAdapter.TYPE_NAME_MAIN);
            mainLists.add(item);
            smallElementQuantity+=1;
        }

        if(smallElementQuantity%3!=0){
            Log.w("elem", String.valueOf(smallElementQuantity%3));
            for(int i=0; i<3-(smallElementQuantity%3); i++){
                item = new MainList(mainListAdapter.TYPE_NAME_EMPLY);
                mainLists.add(item);
            }
        }
        mainListAdapter.notifyDataSetChanged();


        final Context contextThis = this;

        BackendPrefManager prefs = new BackendPrefManager(contextThis);
        if (!prefs.getDrugArchiveStarted()) {
            prefs.setDrugArchiveStarted(true);

            Intent drugArchiveService = new Intent(contextThis, DrugArchiveService.class);
            contextThis.startService(drugArchiveService);
        }
        boolean isPedometor = PedometerService.hasSensor(contextThis);
        startService(new Intent(contextThis, DrugArchiveExpiredService.class));
        if(isPedometor) {
            Intent intentPedometr = new Intent(this, PedometerService.class);


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
        }


        /*
        */
        //final Uri CONTACT_URI = Uri.parse("content://com.vectortwo.healthkeeper.HealthProvider");
        //Cursor cursor = getContentResolver().query(CONTACT_URI, null, null, null, null);
        //startManagingCursor(cursor);
        //ContentProvider();

        //ContentProvider contentProvider = new DBContentProvider()
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PedometerService.class);
        bindService(intent, mainListAdapter.mConnection, 0);
        //startService(intent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainListAdapter.isPedometerBound()) {
            unbindService(mainListAdapter.mConnection);
            mainListAdapter.setPedometerBound(false);
        }
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
                finish();
                break;
            case R.id.main_menu_notification:
                startActivity(new Intent(MainActivity.this, NotificationSettingsActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
