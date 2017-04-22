package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.BloodPressure} table. Ensures type-safety.
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

    /**
     * A date when the blood pressure was measured
     * @param date corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK-Calendar.HOUR_OF_DAY-Calendar.MINUTE"
     * @return this
     */
    public BloodPressureColumns putDate(String date) {
        contentValues.put(DBContract.BloodPressure.DATE, date);
        return this;
    }
}
