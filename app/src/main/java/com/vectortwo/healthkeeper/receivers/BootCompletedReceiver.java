package com.vectortwo.healthkeeper.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.data.BackendPrefManager;
import com.vectortwo.healthkeeper.services.DrugArchiveService;
import com.vectortwo.healthkeeper.services.PedometerService;
import com.vectortwo.healthkeeper.services.RestoreDrugNotifyService;

/**
 * A receiver triggered at {@link Intent.ACTION_BOOT_COMPLETED} to perform
 * any tasks that are required at device startup.
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        BackendPrefManager prefs = new BackendPrefManager(context);

        // Handle pedometer
        SharedPreferences pedometerPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_pedometer), Context.MODE_PRIVATE);
        if (!pedometerPrefs.getBoolean(context.getString(R.string.preference_pedometer_was_killed), true)) {
            pedometerPrefs.edit().clear().apply();

            Intent pedometerService = new Intent(context, PedometerService.class);
            context.stopService(pedometerService);
            context.startService(pedometerService);
        }

        // Initiate drug archive check every day
        if (!prefs.getDrugArchiveStarted()) {
            prefs.setDrugArchiveStarted(true);

            Intent drugArchiveService = new Intent(context, DrugArchiveService.class);
            context.startService(drugArchiveService);
        }

        // Restore drug notifications
        Intent restoreNotifications = new Intent(context, RestoreDrugNotifyService.class);
        context.startService(restoreNotifications);
    }
}