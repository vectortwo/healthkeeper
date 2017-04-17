package com.vectortwo.healthkeeper.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.services.CheckArchiveService;
import com.vectortwo.healthkeeper.services.PedometerService;
import com.vectortwo.healthkeeper.services.RestoreDrugNotifyService;

/**
 * Created by ilya on 11/04/2017.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_pedometer), Context.MODE_PRIVATE);

        // Handle pedometer
        if (!sharedPrefs.getBoolean(context.getString(R.string.preference_pedometer_was_killed), true)) {
            Intent intentStartPedometer = new Intent(context, PedometerService.class);
            context.stopService(intentStartPedometer);
            context.startService(intentStartPedometer);
        }
        sharedPrefs.edit().clear().apply();

        // Initiate drug archive check
        Intent checkArchiveService = new Intent(context, CheckArchiveService.class);
        context.startService(checkArchiveService);

        // Restore drug notifications
        Intent restoreNotifications = new Intent(context, RestoreDrugNotifyService.class);
        context.startService(restoreNotifications);
    }
}