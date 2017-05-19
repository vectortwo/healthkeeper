package com.vectortwo.healthkeeper.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.IntakeStatsColumns;

import java.util.Calendar;

/**
 * Created by ilya on 19/05/2017.
 */
public class IntakeStatsService extends IntentService {
    public static final String ACTION_TAKE = "com.vectortwo.healthkeeper.intent.ACTION_TAKE";
    public static final String ACTION_SKIP = "com.vectortwo.healthkeeper.intent.ACTION_SKIP";

    public static final String EXTRA_KEY_DRUG_ID = "drug_id";

    public IntakeStatsService() {
        super("IntakeStatsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int drugId = getDrugId(intent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(drugId);

        IntakeStatsColumns values = new IntakeStatsColumns();
        values.putDrugId(drugId)
                .putDate(Calendar.getInstance());

        switch (intent.getAction()) {
            case ACTION_SKIP:
                values.putAction(IntakeStatsColumns.ACTION_SKIP);
                break;

            case ACTION_TAKE:
                values.putAction(IntakeStatsColumns.ACTION_TAKE);
                break;

            default:
                final String msg = "Invalid action in " + intent.toString();
                throw new IllegalStateException(msg);
        }

        getContentResolver().insert(DBContract.IntakeStats.CONTENT_URI, values.getContentValues());
    }

    private static int getDrugId(Intent intent) {
        int drugId = intent.getIntExtra(EXTRA_KEY_DRUG_ID, -1);
        if (drugId < 0) {
            final String msg = "Invalid drugId in " + intent.toString();
            throw new IllegalStateException(msg);
        }
        return drugId;
    }
}
