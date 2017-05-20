package com.vectortwo.healthkeeper.data.db;

import android.database.Cursor;
import com.vectortwo.healthkeeper.data.Utils;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by ilya on 19/05/2017.
 */
public class IntakeEarlyColumns extends DBColumns {

    /**
     * Insert a time of the notification which will be taken/skipped
     *
     * @param cal MUST be the time of the scheduled notification which is taken/skipped early
     * @return
     */
    public IntakeEarlyColumns putDate(Calendar cal) {
        String date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
                + "-" + cal.get(Calendar.DAY_OF_MONTH);
        date = Utils.fixMonth(date);
        date = Utils.addLeadZeros(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String hourStr = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
        String minStr = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);

        date += " " + hourStr + ":" + minStr;
        contentValues.put(DBContract.IntakeEarly.DATE, date);
        return this;
    }

    public IntakeEarlyColumns putDrugId(int drugId) {
        contentValues.put(DBContract.IntakeEarly.DRUG_ID, drugId);
        return this;
    }

    public static int getDrugId(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.IntakeEarly.DRUG_ID);
        return c.getInt(colId);
    }

    public static Calendar getDate(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.IntakeEarly.DATE);
        String dateRaw = c.getString(colId);
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(Utils.sdf_yMdHM.parse(dateRaw));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
}