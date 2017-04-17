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
 * A receiver triggered at {@link android.content.Intent.ACTION_BOOT_COMPLETED} to perform
 * any tasks that are required at device startup.
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