package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link com.vectortwo.healthkeeper.data.db.DBContract.BloodSugar} table. Ensures type-safety.
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
