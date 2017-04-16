package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class WeightColumns extends DBColumns {
    public WeightColumns putValue(int value) {
        contentValues.put(DBContract.Weight.VALUE, value);
        return this;
    }

    public WeightColumns putDate(String date) {
        contentValues.put(DBContract.Weight.DATE, date);
        return this;
    }
}
