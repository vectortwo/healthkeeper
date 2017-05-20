package com.vectortwo.healthkeeper.data.db;

import android.database.Cursor;
import com.vectortwo.healthkeeper.data.Utils;

import java.text.ParseException;
import java.util.Calendar;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.Weight} table. Ensures type-safety.
 */
public class WeightColumns extends DBColumns {

    public WeightColumns putValue(int value) {
        contentValues.put(DBContract.Weight.VALUE, value);
        return this;
    }

    public static int getValue(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.Weight.VALUE);
        return c.getInt(colId);
    }

    public WeightColumns putDate(Calendar cal) {
        String date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
                + "-" + cal.get(Calendar.DAY_OF_MONTH);
        date = Utils.fixMonth(date);
        date = Utils.addLeadZeros(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String hourStr = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
        String minStr = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);

        date += " " + hourStr + ":" + minStr;

        contentValues.put(DBContract.Weight.DATE, date);
        return this;
    }

    public static Calendar getDate(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.Weight.DATE);
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
