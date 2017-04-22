package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.WellBeing} table. Ensures type-safety.
 */
public class WellBeingColumns extends DBColumns {

    public WellBeingColumns putValue(int value) {
        contentValues.put(DBContract.WellBeing.VALUE, value);
        return this;
    }

    /**
     * A date when well being was estimated
     * @param date corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK"
     * @return this
     */
    public WellBeingColumns putDate(String date) {
        contentValues.put(DBContract.WellBeing.DATE, date);
        return this;
    }
}
