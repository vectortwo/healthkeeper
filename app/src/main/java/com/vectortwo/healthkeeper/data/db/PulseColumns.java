package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.Pulse} table. Ensures type-safety.
 */
public class PulseColumns extends DBColumns {
    public PulseColumns putValue(int value) {
        contentValues.put(DBContract.Pulse.VALUE, value);
        return this;
    }

    /**
     * A date when the pulse was measured
     * @param date corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK-Calendar.HOUR_OF_DAY-Calendar.MINUTE"
     * @return this
     */
    public PulseColumns putDate(String date) {
        contentValues.put(DBContract.Pulse.DATE, date);
        return this;
    }
}
