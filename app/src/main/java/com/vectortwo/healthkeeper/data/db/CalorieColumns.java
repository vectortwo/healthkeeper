package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class CalorieColumns extends DBColumns {
    public CalorieColumns putGained(int gained) {
        contentValues.put(DBContract.Calorie.GAINED, gained);
        return this;
    }

    public CalorieColumns putLost(int lost) {
        contentValues.put(DBContract.Calorie.LOST, lost);
        return this;
    }

    public CalorieColumns putDate(int date) {
        contentValues.put(DBContract.Calorie.DATE, date);
        return this;
    }
}
