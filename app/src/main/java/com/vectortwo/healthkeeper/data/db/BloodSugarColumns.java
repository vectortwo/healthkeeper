package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class BloodSugarColumns extends DBColumns {
    public void putValue(int value) {
        contentValues.put(DBContract.BloodSugar.VALUE, value);
    }

    public void putAfterFood(int bool) {
        contentValues.put(DBContract.BloodSugar.AFTER_FOOD, bool);
    }

    public void putDate(String date) {
        contentValues.put(DBContract.BloodSugar.DATE, date);
    }
}
