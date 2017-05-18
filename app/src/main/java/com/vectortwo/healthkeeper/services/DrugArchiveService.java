package com.vectortwo.healthkeeper.services;

import android.app.IntentService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import java.util.Calendar;

import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;

/**
 * Checks and moves overdue drugs to archive. Runs every day after 12:00 AM on device wakeup.
 * Should be started only once within the app lifetime.
 */
public class DrugArchiveService extends IntentService {
    public DrugArchiveService() {
        super("CheckArchiveService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor drugCursor = getContentResolver().query(
                DBContract.Drug.CONTENT_URI,
                new String[]{DBContract.Drug._ID, DBContract.Drug.END_DATE},
                DBContract.Drug.ARCHIVED + "=0",
                null,
                null);

        Calendar currentDate = Calendar.getInstance();

        while (drugCursor.moveToNext()) {
            // Parse endDate
            Calendar endDate = DrugColumns.getEndDate(drugCursor);

            if (currentDate.compareTo(endDate) > 0) {
                int drugID = drugCursor.getInt(drugCursor.getColumnIndex(DBContract.Drug._ID));

                // Update db
                DrugColumns values = new DrugColumns();
                values.putArchived(1).putNotifyMe(0);

                getContentResolver().update(
                        DBContract.Drug.CONTENT_URI,
                        values.getContentValues(),
                        DBContract.Drug._ID + "=" + drugID,
                        null);

                // Cancel notifications
                Intent i = new Intent(this, DrugNotifyService.class);
                i.setAction(DrugNotifyService.ACTION_CANCEL);
                i.putExtra(DrugNotifyService.KEY_DRUG_ID, drugID);
                startService(i);
            }
        }
        drugCursor.close();

        // Schedule AlarmManager for the next check
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        currentDate.add(Calendar.DAY_OF_MONTH, 1);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);

        Intent alarmIntent = new Intent(this, DrugArchiveService.class);
        PendingIntent scheduleIntent = PendingIntent.getService(getApplicationContext(), 0, alarmIntent, 0);
        alarmManager.setExact(AlarmManager.RTC, currentDate.getTimeInMillis(), scheduleIntent);
    }
}