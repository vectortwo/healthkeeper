package com.vectortwo.healthkeeper.data.db;

import android.database.Cursor;
import com.vectortwo.healthkeeper.data.Utils;

import java.text.ParseException;
import java.util.Calendar;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.WellBeing} table. Ensures type-safety.
 */
public class WellBeingColumns extends DBColumns {

    public WellBeingColumns putValue(int value) {
        contentValues.put(DBContract.WellBeing.VALUE, value);
        return this;
    }

    public static int getValue(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.WellBeing.VALUE);
        return c.getInt(colId);
    }

    /**
     * A date when well being was estimated
     * @param cal Calendar with desired date
     * @return this
     */
    public WellBeingColumns putDate(Calendar cal) {
        String date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH)
                + "-" + cal.get(Calendar.DAY_OF_MONTH);
        date = Utils.fixMonth(date);
        date = Utils.addLeadZeros(date);

        contentValues.put(DBContract.WellBeing.DATE, date);
        return this;
    }

    public static Calendar getDate(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.WellBeing.DATE);
        String dateRaw = c.getString(colId);

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(Utils.sdf_yMd.parse(dateRaw));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
}
