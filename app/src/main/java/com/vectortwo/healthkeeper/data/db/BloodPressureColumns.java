package com.vectortwo.healthkeeper.data.db;

import android.database.Cursor;
import com.vectortwo.healthkeeper.data.Utils;

import java.text.ParseException;
import java.util.Calendar;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.BloodPressure} table. Ensures type-safety.
 */
public class BloodPressureColumns extends DBColumns {

    public BloodPressureColumns putSystolic(int systolic) {
        contentValues.put(DBContract.BloodPressure.SYSTOLIC, systolic);
        return this;
    }

    public BloodPressureColumns putDiastolic(int diastolic) {
        contentValues.put(DBContract.BloodPressure.DIASTOLIC, diastolic);
        return this;
    }

    public static int getSystolic(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.BloodPressure.SYSTOLIC);
        return c.getInt(colId);
    }

    public static int getDiastolic(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.BloodPressure.DIASTOLIC);
        return c.getInt(colId);
    }

    public BloodPressureColumns putDate(Calendar cal) {
        String date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
                + "-" + cal.get(Calendar.DAY_OF_MONTH);
        date = Utils.fixMonth(date);
        date = Utils.addLeadZeros(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String hourStr = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);
        String minStr = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);

        date += " " + hourStr + ":" + minStr;

        contentValues.put(DBContract.BloodPressure.DATE, date);
        return this;
    }

    public static Calendar getDate(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.BloodPressure.DATE);
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
