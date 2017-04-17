package com.vectortwo.healthkeeper.services;

import android.app.IntentService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import java.util.Calendar;

import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;

public class CheckArchiveService extends IntentService {
    public CheckArchiveService() {
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

        Calendar cal = Calendar.getInstance();
        Calendar currentDate = (Calendar) cal.clone();
        Calendar endDate = (Calendar) cal.clone();

        while (drugCursor.moveToNext()) {
            // Parse endDate
            String endDateStr = drugCursor.getString(drugCursor.getColumnIndex(DBContract.Drug.END_DATE));
            String[] endDateComps = endDateStr.split("-");
            int year = Integer.parseInt(endDateComps[0]);
            int month = Integer.parseInt(endDateComps[1]);
            int day = Integer.parseInt(endDateComps[2]);
            endDate.set(year, month, day);

            if (currentDate.compareTo(endDate) > 0) {
                int drugID = drugCursor.getInt(drugCursor.getColumnIndex(DBContract.Drug._ID));

                DrugColumns values = new DrugColumns();
                values.putArchived(1).putNotifyMe(0);

                getContentResolver().update(
                        DBContract.Drug.CONTENT_URI,
                        values.getContentValues(),
                        DBContract.Drug._ID + "=" + drugID,
                        null);
            }
        }
        drugCursor.close();

        // Schedule AlarmManager for the next check
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        currentDate.add(Calendar.DAY_OF_MONTH, 1);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);

        Intent alarmIntent = new Intent(this, CheckArchiveService.class);
        PendingIntent scheduleIntent = PendingIntent.getService(getApplicationContext(), 0, alarmIntent, 0);
        alarmManager.setExact(AlarmManager.RTC, currentDate.getTimeInMillis(), scheduleIntent);
    }
}