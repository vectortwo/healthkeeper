package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 06/04/2017.
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
