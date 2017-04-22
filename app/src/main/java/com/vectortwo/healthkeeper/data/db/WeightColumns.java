package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.Weight} table. Ensures type-safety.
 */
public class WeightColumns extends DBColumns {

    public WeightColumns putValue(int value) {
        contentValues.put(DBContract.Weight.VALUE, value);
        return this;
    }

    /**
     * A date when the weight was measured
     * @param date corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK-Calendar.HOUR_OF_DAY-Calendar.MINUTE"
     * @return this
     */
    public WeightColumns putDate(String date) {
        contentValues.put(DBContract.Weight.DATE, date);
        return this;
    }
}
