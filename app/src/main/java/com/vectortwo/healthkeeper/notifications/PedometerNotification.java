package com.vectortwo.healthkeeper.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.MainActivity;
import com.vectortwo.healthkeeper.services.PedometerService;

/**
 * Created by ilya on 30/03/2017.
 */
public class PedometerNotification {

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private static final int PEDOMETER_NOTIFICATION_ID = 2017;

    public PedometerNotification(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent stopServiceIntent = new Intent(context, PedometerService.class);
        stopServiceIntent.setAction(PedometerService.ACTION_STOP);

        // todo: add navigation to contentintent's activity (back button support)
        Intent contentIntent = new Intent(context, MainActivity.class);

        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(String.valueOf(0))
                .setPriority(Notification.PRIORITY_MIN)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .addAction(R.mipmap.ic_launcher, context.getString(R.string.notification_turn_off),
                        PendingIntent.getService(context, 0, stopServiceIntent, 0))
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

    public int getNotificationID() {
        return PEDOMETER_NOTIFICATION_ID;
    }
}
