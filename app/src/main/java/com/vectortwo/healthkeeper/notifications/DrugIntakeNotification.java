package com.vectortwo.healthkeeper.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.MainActivity;
import com.vectortwo.healthkeeper.data.BackendPrefManager;
import com.vectortwo.healthkeeper.services.DrugNotifyService;
import com.vectortwo.healthkeeper.services.IntakeStatsService;

/**
 * Created by ilya on 10/04/2017.
 */
public class DrugIntakeNotification {

    public static final String EXTRA_DRUG_NAME = "drug_name";
    public static final String EXTRA_DOSAGE = "drug_dosage";
    public static final String EXTRA_COLORID = "drug_color_id";
    public static final String EXTRA_IMAGEID = "drug_image_id";
    public static final String EXTRA_FORM = "drug_dosage_form";

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

        String drugName = intent.getStringExtra(EXTRA_DRUG_NAME);
        String form = intent.getStringExtra(EXTRA_FORM);
        float dosage = intent.getFloatExtra(EXTRA_DOSAGE, -1);
        int imageId = intent.getIntExtra(EXTRA_IMAGEID, -1);
        int colorId = intent.getIntExtra(EXTRA_COLORID, -1);

        String contentInfo = "Take " + dosage + " (" + form + ")";

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // todo: add navigation to contentintent's activity (back button support)
        Intent contentIntent = new Intent(context, MainActivity.class);

        Uri defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(drugName)
                .setContentText(contentInfo)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultRingtone)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0));

        Intent takeIntent = new Intent(context, IntakeStatsService.class);
        takeIntent.setAction(IntakeStatsService.ACTION_TAKE);
        takeIntent.putExtra(IntakeStatsService.EXTRA_KEY_DRUG_ID, drugID);

        builder.addAction(R.mipmap.ic_launcher, context.getString(R.string.intake_take),
                PendingIntent.getService(context, drugID, takeIntent, PendingIntent.FLAG_CANCEL_CURRENT));

        if (currentPostponeCount < maxPostponeCount) {
            int intakeId = intent.getIntExtra(DrugNotifyService.KEY_INTAKE_ID, -1);

            Intent postponeIntent = new Intent(context, DrugNotifyService.class);
            postponeIntent.setAction(DrugNotifyService.ACTION_POSTPONE);
            postponeIntent.putExtra(DrugNotifyService.KEY_POSTPONE_TIME, postponeTime)
                    .putExtra(DrugNotifyService.KEY_CURRENT_POSTPONE_COUNT, currentPostponeCount)
                    .putExtra(DrugNotifyService.KEY_DRUG_ID, drugID)
                    .putExtra(DrugNotifyService.KEY_INTAKE_ID, intakeId);

            builder.addAction(R.mipmap.ic_launcher, context.getString(R.string.intake_postpone),
                    PendingIntent.getService(context, drugID, postponeIntent, PendingIntent.FLAG_CANCEL_CURRENT));
        }

        Intent skipIntent = new Intent(context, IntakeStatsService.class);
        skipIntent.setAction(IntakeStatsService.ACTION_SKIP);
        skipIntent.putExtra(IntakeStatsService.EXTRA_KEY_DRUG_ID, drugID);

        builder.addAction(R.mipmap.ic_launcher, context.getString(R.string.intake_skip),
                PendingIntent.getService(context, drugID, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT));
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
