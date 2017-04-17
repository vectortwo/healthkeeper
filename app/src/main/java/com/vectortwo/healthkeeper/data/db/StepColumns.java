package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link com.vectortwo.healthkeeper.data.db.DBContract.Steps} table. Ensures type-safety.
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