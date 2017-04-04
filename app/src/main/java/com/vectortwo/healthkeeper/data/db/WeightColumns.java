package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class WeightColumns extends DBColumns {
    public void putValue(int value) {
        contentValues.put(DBContract.Weight.VALUE, value);
    }

    public void putDate(String date) {
        contentValues.put(DBContract.Weight.DATE, date);
    }
}
