package com.vectortwo.healthkeeper.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.services.PedometerService;

/**
 * Created by ilya on 28/03/2017.
 */
public class PedometerBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_pedometer), Context.MODE_PRIVATE);

            if (!sharedPrefs.getBoolean(context.getString(R.string.preference_pedometer_was_killed), true)) {
                Intent intentStartPedometer = new Intent(context, PedometerService.class);
                context.stopService(intentStartPedometer);
                context.startService(intentStartPedometer);
            }

            sharedPrefs.edit().clear().apply();
        }
    }
}