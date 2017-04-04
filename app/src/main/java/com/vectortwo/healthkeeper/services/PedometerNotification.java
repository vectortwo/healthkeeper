package com.vectortwo.healthkeeper.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.main.MainActivity;

/**
 * Created by ilya on 30/03/2017.
 */
public class PedometerNotification {

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private Context context;

    public static final int PEDOMETER_NOTIFICATION_ID = 2017;

    public PedometerNotification(Context context) {
        this.context = context; // TODO: check for memory leaks

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent stopServiceIntent = new Intent(context, PedometerService.class);
        Intent contentIntent = new Intent(context, MainActivity.class);

        stopServiceIntent.setAction(PedometerService.ACTION_STOP_PEDOMETER_SERVICE);

        builder = new NotificationCompat.Builder(this.context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(String.valueOf(0))
                .setPriority(Notification.PRIORITY_MIN)
                .addAction(R.mipmap.ic_launcher, context.getString(R.string.notification_turn_off),
                        PendingIntent.getService(context, 0, stopServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0));
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public void update() {
        notificationManager.notify(PEDOMETER_NOTIFICATION_ID, builder.build());
    }
}
