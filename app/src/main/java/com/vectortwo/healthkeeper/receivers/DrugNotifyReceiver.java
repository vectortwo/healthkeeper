package com.vectortwo.healthkeeper.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.vectortwo.healthkeeper.services.DrugNotifyService;

/**
 * Receive alarms scheduled by {@link DrugNotifyService} to
 * start it.
 * DO NOT send broadcast to this receiver manually! Directly start {@link DrugNotifyService}
 */
public class DrugNotifyReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(intent);
        i.setClass(context, DrugNotifyService.class);
        startWakefulService(context, i);
    }
}