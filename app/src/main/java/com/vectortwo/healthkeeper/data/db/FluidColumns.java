package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.Fluid} table. Ensures type-safety.
 */
public class FluidColumns extends DBColumns {

    public FluidColumns putDrank(float drank) {
        contentValues.put(DBContract.Fluid.DRANK, drank);
        return this;
    }

    /**
     * A date when the fluid intake was measured
     * @param date corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK-Calendar.HOUR_OF_DAY-Calendar.MINUTES"
     * @return this
     */
    public FluidColumns putDate(String date) {
        contentValues.put(DBContract.Fluid.DATE, date);
        return this;
    }
}
