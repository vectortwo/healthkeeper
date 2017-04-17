package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link com.vectortwo.healthkeeper.data.db.DBContract.Calorie} table. Ensures type-safety.
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
