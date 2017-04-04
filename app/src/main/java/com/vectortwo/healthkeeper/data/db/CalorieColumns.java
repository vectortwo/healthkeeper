package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class CalorieColumns extends DBColumns {
    public void putGained(int gained) {
        contentValues.put(DBContract.Calorie.GAINED, gained);
    }

    public void putLost(int lost) {
        contentValues.put(DBContract.Calorie.LOST, lost);
    }

    public void putDate(int date) {
        contentValues.put(DBContract.Calorie.DATE, date);
    }
}
