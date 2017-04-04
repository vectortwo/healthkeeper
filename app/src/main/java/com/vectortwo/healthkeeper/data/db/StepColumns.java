package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class StepColumns extends DBColumns {
    public void putCount(int count) {
        contentValues.put(DBContract.Steps.COUNT, count);
    }

    public void putDate(String date) {
        contentValues.put(DBContract.Steps.DATE, date);
    }

    public void putHour(int hour) {
        contentValues.put(DBContract.Steps.HOUR, hour);
    }
}