package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class SleepColumns extends DBColumns {
    public void putSleepTime(String sleepTime) {
        contentValues.put(DBContract.Sleep.SLEEP_TIME, sleepTime);
    }

    public void putStartDate(String startDate) {
        contentValues.put(DBContract.Sleep.START_DATE, startDate);
    }

    public void putEndDate(String endDate) {
        contentValues.put(DBContract.Sleep.END_DATE, endDate);
    }
}