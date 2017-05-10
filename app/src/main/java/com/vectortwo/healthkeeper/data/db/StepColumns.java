package com.vectortwo.healthkeeper.data.db;

import android.database.Cursor;
import com.vectortwo.healthkeeper.data.Utils;

import java.text.ParseException;
import java.util.Calendar;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.Steps} table. Ensures type-safety.
 */
public class StepColumns extends DBColumns {

    public StepColumns putCount(int count) {
        contentValues.put(DBContract.Steps.COUNT, count);
        return this;
    }

    /**
     * Indicates a date when the steps were measured
     * @param cal Calendar with desired date
     * @return this
     */
    public StepColumns putDate(Calendar cal) {
        String date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
                + "-" + cal.get(Calendar.DAY_OF_MONTH);
        date = Utils.fixMonth(date);
        date = Utils.addLeadZeros(date);

        contentValues.put(DBContract.Steps.DATE, date);
        return this;
    }

    public static Calendar getDate(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.Steps.DATE);
        String dateRaw = c.getString(colId);

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(Utils.sdf_yMd.parse(dateRaw));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * Indicates how many steps the user has taken during the last hour
     * @param hour corresponds to Calendar.HOUR_OF_DAY
     * @return this
     */
    public StepColumns putHour(int hour) {
        contentValues.put(DBContract.Steps.HOUR, hour);
        return this;
    }

    /**
     * How much time the user has been walking
     * @param minutes time in minutes
     * @return this
     */
    public StepColumns putWakingTime(int minutes) {
        contentValues.put(DBContract.Steps.WALKING_TIME, minutes);
        return this;
    }
}