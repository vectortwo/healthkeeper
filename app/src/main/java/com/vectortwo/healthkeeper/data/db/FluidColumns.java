package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link com.vectortwo.healthkeeper.data.db.DBContract.Fluid} table. Ensures type-safety.
 */
public class FluidColumns extends DBColumns {
    public FluidColumns putDrank(float drank) {
        contentValues.put(DBContract.Fluid.DRANK, drank);
        return this;
    }

    public FluidColumns putDate(String date) {
        contentValues.put(DBContract.Fluid.DATE, date);
        return this;
    }
}
