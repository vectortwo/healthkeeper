package com.vectortwo.healthkeeper.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.MainActivity;

/**
 * Created by ilya on 10/04/2017.
 */
public class DrugNotifyNotification {

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private int notificationID;

    public DrugNotifyNotification(Context context, int notificationID) {
        this.notificationID = notificationID;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent contentIntent = new Intent(context, MainActivity.class);

        Uri defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("")
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultRingtone)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0));
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public void update() {
        notificationManager.notify(notificationID, builder.build());
    }
}
