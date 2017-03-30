package com.vectortwo.healthkeeper.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.vectortwo.healthkeeper.R;

/**
 * Created by ilya on 28/03/2017.
 */
public class PedometerBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_pedometer), context.MODE_PRIVATE);
            sharedPrefs.edit().clear().apply();
        }
    }
}