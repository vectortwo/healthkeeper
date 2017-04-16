package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class StepColumns extends DBColumns {
    public StepColumns putCount(int count) {
        contentValues.put(DBContract.Steps.COUNT, count);
        return this;
    }

    public StepColumns putDate(String date) {
        contentValues.put(DBContract.Steps.DATE, date);
        return this;
    }

    public StepColumns putHour(int hour) {
        contentValues.put(DBContract.Steps.HOUR, hour);
        return this;
    }
}