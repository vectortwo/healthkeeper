package com.vectortwo.healthkeeper.services;

import java.util.Calendar;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import com.vectortwo.healthkeeper.data.db.DBContract;

/**
 * Checks whether the end date of any drug is {@link #DELTA_TIME} overdue.
 * Overdue time may be provided in Intent.
 * If {@link #DELTA_TIME} extra is not present, one year will be set by default.
 * If overdue, deletes all info about such drug from the database.
 */
public class DrugArchiveExpiredService extends IntentService {
    public static final long YEAR = 365*24*60*60;
    public static final long HALF_YEAR = 183*24*60*60;
    public static final long MONTH = 28*24*60*60;

    public static final String DELTA_TIME = "delta_time";

    public DrugArchiveExpiredService() {
        super("DrugArchiveExpiredService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor drugCursor = getContentResolver().query(
                DBContract.Drug.CONTENT_URI,
                new String[] {DBContract.Drug._ID, DBContract.Drug.END_DATE},
                null,
                null,
                null);

        // TODO: connect default overdue value with preference setting.
        long delta = intent.getLongExtra(DELTA_TIME, YEAR);

        Calendar currentTime = Calendar.getInstance();
        Calendar endDate = (Calendar) currentTime.clone();

        while (drugCursor.moveToNext()) {
            int drugID = drugCursor.getInt(drugCursor.getColumnIndex(DBContract.Drug._ID));

            String endDateStr = drugCursor.getString(drugCursor.getColumnIndex(DBContract.Drug.END_DATE));
            String[] endDateComps = endDateStr.split("-");
            int year = Integer.parseInt(endDateComps[0]);
            int month = Integer.parseInt(endDateComps[1]);
            int day = Integer.parseInt(endDateComps[2]);
            endDate.set(year, month, day);

            long deltaCurrent = (currentTime.getTimeInMillis() - endDate.getTimeInMillis())/1000;
            if (deltaCurrent >= delta) {
                getContentResolver().delete(
                        DBContract.Drug.CONTENT_URI,
                        DBContract.Drug._ID + "=" + drugID,
                        null);

                getContentResolver().delete(
                        DBContract.Intake.CONTENT_URI,
                        DBContract.Intake.DRUG_ID + "=" + drugID,
                        null);
            }
        }
        drugCursor.close();
    }
}
