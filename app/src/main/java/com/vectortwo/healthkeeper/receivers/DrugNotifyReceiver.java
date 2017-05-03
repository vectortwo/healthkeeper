package com.vectortwo.healthkeeper.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.vectortwo.healthkeeper.services.DrugNotifyService;

/**
 * Receive alarms scheduled by {@link DrugNotifyService} to start it.
 * This receiver simply forwards received Intent to {@link DrugNotifyService}
 * The purpose of this receiver is to ensure that the device won't go to sleep immediately
 * after onReceive() before starting {@link DrugNotifyService}
 * DO NOT send broadcast to it manually! Directly start {@link DrugNotifyService} to schedule
 * notifications for a drug.
 */
public class DrugNotifyReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(intent);
        i.setClass(context, DrugNotifyService.class);
        startWakefulService(context, i);
    }
}