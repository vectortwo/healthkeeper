package com.vectortwo.healthkeeper.data.db;

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
     * @param date corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK"
     * @return this
     */
    public StepColumns putDate(String date) {
        contentValues.put(DBContract.Steps.DATE, date);
        return this;
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