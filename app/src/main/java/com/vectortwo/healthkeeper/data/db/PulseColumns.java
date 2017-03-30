package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class PulseColumns extends DBColumns {
    public void putValue(int value) {
        contentValues.put(DBContract.Pulse.VALUE, value);
    }

    public void putDate(String date) {
        contentValues.put(DBContract.Pulse.DATE, date);
    }
}
