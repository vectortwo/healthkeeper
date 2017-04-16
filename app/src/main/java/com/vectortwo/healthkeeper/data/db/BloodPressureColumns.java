package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class BloodPressureColumns extends DBColumns {
    public BloodPressureColumns putSystolic(int systolic) {
        contentValues.put(DBContract.BloodPressure.SYSTOLIC, systolic);
        return this;
    }

    public BloodPressureColumns putDiastolic(int diastolic) {
        contentValues.put(DBContract.BloodPressure.DIASTOLIC, diastolic);
        return this;
    }

    public BloodPressureColumns putDate(String date) {
        contentValues.put(DBContract.BloodPressure.DATE, date);
        return this;
    }
}
