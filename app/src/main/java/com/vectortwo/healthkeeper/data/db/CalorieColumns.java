package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.Calorie} table. Ensures type-safety.
 */
public class CalorieColumns extends DBColumns {

    public CalorieColumns putGained(int gained) {
        contentValues.put(DBContract.Calorie.GAINED, gained);
        return this;
    }

    public CalorieColumns putBurned(int burned) {
        contentValues.put(DBContract.Calorie.BURNED, burned);
        return this;
    }

    /**
     * Indicates a date when calories were recorded
     * @param date corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK-Calendar.HOUR_OF_DAY-Calendar.MINUTE"
     * @return this
     */
    public CalorieColumns putDate(int date) {
        contentValues.put(DBContract.Calorie.DATE, date);
        return this;
    }
}
