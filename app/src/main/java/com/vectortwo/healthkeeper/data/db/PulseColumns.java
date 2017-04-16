package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class PulseColumns extends DBColumns {
    public PulseColumns putValue(int value) {
        contentValues.put(DBContract.Pulse.VALUE, value);
        return this;
    }

    public PulseColumns putDate(String date) {
        contentValues.put(DBContract.Pulse.DATE, date);
        return this;
    }
}
