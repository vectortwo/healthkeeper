package com.vectortwo.healthkeeper.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import java.util.Calendar;

import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.receivers.DrugNotifyReceiver;

/**
 * Restores drug notifications that were discarded on system shutdown.
 */
public class RestoreDrugNotifyService extends IntentService {
    public RestoreDrugNotifyService() {
        super("RestoreDrugNotifyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor drugCursor = getContentResolver().query(
                DBContract.Drug.CONTENT_URI,
                new String[] {DBContract.Drug._ID, DBContract.Drug.END_DATE},
                DBContract.Drug.NOTIFY_ME + "=1",
                null,
                null);

        Calendar currentDate = Calendar.getInstance();
        Calendar endDate = (Calendar) currentDate.clone();

        while (drugCursor.moveToNext()) {
            int drugID = drugCursor.getInt(drugCursor.getColumnIndex(DBContract.Drug._ID));

            String endDateStr = drugCursor.getString(drugCursor.getColumnIndex(DBContract.Drug.END_DATE));
            String[] endDateComps = endDateStr.split("-");
            int year = Integer.parseInt(endDateComps[0]);
            int month = Integer.parseInt(endDateComps[1]);
            int day = Integer.parseInt(endDateComps[2]);
            endDate.set(year, month, day);

            if (currentDate.compareTo(endDate) <= 0) {
                Intent i = new Intent(this, DrugNotifyService.class);
                i.setAction(DrugNotifyService.ACTION_SCHEDULE);
                i.putExtra(DrugNotifyService.KEY_DRUG_ID, drugID);
                startService(i);
            }
        }
        drugCursor.close();
    }
}
