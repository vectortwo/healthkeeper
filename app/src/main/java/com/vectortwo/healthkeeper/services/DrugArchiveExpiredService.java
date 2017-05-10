package com.vectortwo.healthkeeper.services;

import java.util.Calendar;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import com.vectortwo.healthkeeper.data.BackendPrefManager;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;

/**
 * Checks whether the end date of any drug is {@link #delta} overdue.
 * Default overdue time is provided in preference_bdefaults.xml
 * If overdue, deletes all info about such drug from the database.
 */

// todo: check for presence in MainActivity
public class DrugArchiveExpiredService extends IntentService {
    private int delta;

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

        BackendPrefManager prefs = new BackendPrefManager(this);
        delta = prefs.getDrugExpiredDelta();

        Calendar currentTime = Calendar.getInstance();

        while (drugCursor.moveToNext()) {
            int drugID = drugCursor.getInt(drugCursor.getColumnIndex(DBContract.Drug._ID));
            Calendar endDate = DrugColumns.getEndDate(drugCursor);

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
