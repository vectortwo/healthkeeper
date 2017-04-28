package com.vectortwo.healthkeeper.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.MainActivity;
import com.vectortwo.healthkeeper.data.BackendPrefManager;
import com.vectortwo.healthkeeper.services.DrugNotifyService;

/**
 * Created by ilya on 10/04/2017.
 */
public class DrugIntakeNotification {

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    private int drugID;

    private int maxPostponeCount;
    private int postponeTime;

    public DrugIntakeNotification(Context context, Intent intent) {
        BackendPrefManager prefs = new BackendPrefManager(context);

        maxPostponeCount = prefs.getPostponeMaxCount();
        postponeTime = prefs.getPostponeTime();

        this.drugID = intent.getIntExtra(DrugNotifyService.KEY_DRUG_ID, -1);
        int currentPostponeCount = intent.getIntExtra(DrugNotifyService.KEY_CURRENT_POSTPONE_COUNT, 0);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent contentIntent = new Intent(context, MainActivity.class);

        Uri defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("drugid " + drugID)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultRingtone)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0));

        if (currentPostponeCount < maxPostponeCount) {
            Intent postponeIntent = new Intent(context, DrugNotifyService.class);
            postponeIntent.setAction(DrugNotifyService.ACTION_POSTPONE);
            postponeIntent.putExtra(DrugNotifyService.KEY_POSTPONE_TIME, postponeTime);
            postponeIntent.putExtra(DrugNotifyService.KEY_CURRENT_POSTPONE_COUNT, currentPostponeCount);
            postponeIntent.putExtra(DrugNotifyService.KEY_DRUG_ID, drugID);

            builder.addAction(R.mipmap.ic_launcher, context.getString(R.string.intake_postpone),
                    PendingIntent.getService(context, drugID, postponeIntent, PendingIntent.FLAG_CANCEL_CURRENT));
        }
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public void show() {
        notificationManager.notify(drugID, builder.build());
    }
}
