package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class BloodPressureColumns extends DBColumns {
    public void putSystolic(int systolic) {
        contentValues.put(DBContract.BloodPressure.SYSTOLIC, systolic);
    }

    public void putDiastolic(int diastolic) {
        contentValues.put(DBContract.BloodPressure.DIASTOLIC, diastolic);
    }

    public void putDate(String date) {
        contentValues.put(DBContract.BloodPressure.DATE, date);
    }
}
