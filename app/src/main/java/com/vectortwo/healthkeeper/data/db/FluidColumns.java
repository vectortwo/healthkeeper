package com.vectortwo.healthkeeper.data.db;

import android.database.Cursor;
import com.vectortwo.healthkeeper.data.Utils;

import java.text.ParseException;
import java.util.Calendar;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.Fluid} table. Ensures type-safety.
 */
public class FluidColumns extends DBColumns {

    public FluidColumns putDrank(float drank) {
        contentValues.put(DBContract.Fluid.DRANK, drank);
        return this;
    }

    public static float getDrank(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.Fluid.DRANK);
        return c.getFloat(colId);
    }

    /**
     * A date when the fluid intake was measured
     * @param cal Calendar with desired date
     * @return this
     */
    public FluidColumns putDate(Calendar cal) {
        String date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
                + "-" + cal.get(Calendar.DAY_OF_MONTH);
        date = Utils.fixMonth(date);
        date = Utils.addLeadZeros(date);

        contentValues.put(DBContract.Fluid.DATE, date);
        return this;
    }

    public static Calendar getDate(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.Fluid.DATE);
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
     * Time when water has been drank
     * @param time corresponds to "Calendar.HOUR_OF_DAY-Calendar.MINUTES"
     * @return this
     */
    public FluidColumns putTime(String time) {
        contentValues.put(DBContract.Fluid.TIME, time);
        return this;
    }
}
