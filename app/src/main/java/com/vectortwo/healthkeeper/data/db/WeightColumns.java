package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link com.vectortwo.healthkeeper.data.db.DBContract.Weight} table. Ensures type-safety.
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
