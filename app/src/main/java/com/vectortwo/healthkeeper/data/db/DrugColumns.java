package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class DrugColumns extends DBColumns {
    public void putTitle(String title) {
        contentValues.put(DBContract.Drug.TITLE, title);
    }

    public void putDosage(float dosage) {
        contentValues.put(DBContract.Drug.DOSAGE, dosage);
    }

    public void putTimesADay(int times_a_day) {
        contentValues.put(DBContract.Drug.TIMES_A_DAY, times_a_day);

    }

    public void putTimesAWeek(int times_a_week) {
        contentValues.put(DBContract.Drug.TIMES_A_WEEK, times_a_week);
    }

    public void putStartDate(String date) {
        contentValues.put(DBContract.Drug.START_DATE, date);
    }

    public void putEndDate(String date) {
        contentValues.put(DBContract.Drug.END_DATE, date);
    }

    public void putDescription(String desc) {
        contentValues.put(DBContract.Drug.DESCRIPTION, desc);
    }

    public void putWarnings(String warnings) {
        contentValues.put(DBContract.Drug.WARNINGS, warnings);
    }
}