package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class BloodSugarColumns extends DBColumns {
    public BloodSugarColumns putValue(int value) {
        contentValues.put(DBContract.BloodSugar.VALUE, value);
        return this;
    }

    public BloodSugarColumns putAfterFood(int bool) {
        contentValues.put(DBContract.BloodSugar.AFTER_FOOD, bool);
        return this;
    }

    public BloodSugarColumns putDate(String date) {
        contentValues.put(DBContract.BloodSugar.DATE, date);
        return this;
    }
}
