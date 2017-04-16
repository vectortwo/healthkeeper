package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
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
