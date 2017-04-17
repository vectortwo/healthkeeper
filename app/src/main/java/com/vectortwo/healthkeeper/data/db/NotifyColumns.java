package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link com.vectortwo.healthkeeper.data.db.DBContract.Notify} table. Ensures type-safety.
 */
public class NotifyColumns extends DBColumns {
    public NotifyColumns putWeekday(int weekday) {
        contentValues.put(DBContract.Notify.WEEKDAY, weekday);
        return this;
    }

    public NotifyColumns putDrugID(int drugID) {
        contentValues.put(DBContract.Notify.DRUG_ID, drugID);
        return this;
    }

    public NotifyColumns putTime(String time) {
        contentValues.put(DBContract.Notify.TIME, time);
        return this;
    }
}
