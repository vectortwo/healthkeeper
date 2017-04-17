package com.vectortwo.healthkeeper.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Receive alarms scheduled by {@link com.vectortwo.healthkeeper.services.DrugNotifyService} to
 * start it.
 * DO NOT send broadcast to this receiver manually! Directly start {@link com.vectortwo.healthkeeper.services.DrugNotifyService}
 */
public class DrugNotifyReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        startWakefulService(context, intent);
    }
}