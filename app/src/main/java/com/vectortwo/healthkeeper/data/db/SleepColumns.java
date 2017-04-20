package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link com.vectortwo.healthkeeper.data.db.DBContract.Sleep} table. Ensures type-safety.
 */
public class SleepColumns extends DBColumns {

    public SleepColumns putSleepTime(String sleepTime) {
        contentValues.put(DBContract.Sleep.SLEEP_TIME, sleepTime);
        return this;
    }

    /**
     * When the user has started sleeping?
     * @param startDate corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK"
     * @return this
     */
    public SleepColumns putStartDate(String startDate) {
        contentValues.put(DBContract.Sleep.START_DATE, startDate);
        return this;
    }

    /**
     * When the user has ended sleeping?
     * @param endDate corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK"
     * @return this
     */
    public SleepColumns putEndDate(String endDate) {
        contentValues.put(DBContract.Sleep.END_DATE, endDate);
        return this;
    }
}