package com.vectortwo.healthkeeper.data.db;

import android.database.Cursor;

import java.util.Calendar;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.Intake} table. Ensures type-safety.
 */
public class IntakeColumns extends DBColumns {

    /**
     * A day of week this drug should be taken in
     * @param weekday corresponds to Calendar.DAY_OF_WEEK
     * @return this
     */
    public IntakeColumns putWeekday(int weekday) {
        contentValues.put(DBContract.Intake.WEEKDAY, weekday);
        return this;
    }

    /**
     * Time of day this drug should be taken in
     * @param cal Calendar with desired time
     * @return this
     */
    public IntakeColumns putTime(Calendar cal) {
        String time = cal.get(Calendar.HOUR_OF_DAY) + "-" + cal.get(Calendar.MINUTE);
        contentValues.put(DBContract.Intake.TIME, time);
        return this;
    }

    /**
     * What form should this drug be taken in? (e.g. pills, spray)
     * @param form
     * @return this
     */
    public IntakeColumns putForm(String form) {
        contentValues.put(DBContract.Intake.FORM, form);
        return this;
    }

    public IntakeColumns putDosage(float dosage) {
        contentValues.put(DBContract.Intake.DOSAGE, dosage);
        return this;
    }

    public IntakeColumns putDrugID(int drugID) {
        contentValues.put(DBContract.Intake.DRUG_ID, drugID);
        return this;
    }

    public static float getDosage(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.Intake.DOSAGE);
        return c.getFloat(colId);
    }

    public static String getForm(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.Intake.FORM);
        return c.getString(colId);
    }
}
