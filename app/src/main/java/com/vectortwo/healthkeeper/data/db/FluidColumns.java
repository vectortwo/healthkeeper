package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class FluidColumns extends DBColumns {
    public void putDrank(float drank) {
        contentValues.put(DBContract.Fluid.DRANK, drank);
    }

    public void putDate(String date) {
        contentValues.put(DBContract.Fluid.DATE, date);
    }
}
