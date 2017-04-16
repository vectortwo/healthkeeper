package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class DrugColumns extends DBColumns {
    public DrugColumns putTitle(String title) {
        contentValues.put(DBContract.Drug.TITLE, title);
        return this;
    }

    public DrugColumns putDosage(float dosage) {
        contentValues.put(DBContract.Drug.DOSAGE, dosage);
        return this;
    }

    public DrugColumns putTimesADay(int timesADay) {
        contentValues.put(DBContract.Drug.TIMES_A_DAY, timesADay);
        return this;
    }

    public DrugColumns putStartDate(String date) {
        contentValues.put(DBContract.Drug.START_DATE, date);
        return this;
    }

    public DrugColumns putEndDate(String date) {
        contentValues.put(DBContract.Drug.END_DATE, date);
        return this;
    }

    public DrugColumns putDescription(String desc) {
        contentValues.put(DBContract.Drug.DESCRIPTION, desc);
        return this;
    }

    public DrugColumns putWarnings(String warnings) {
        contentValues.put(DBContract.Drug.WARNINGS, warnings);
        return this;
    }

    public DrugColumns putArchived(int archived) {
        contentValues.put(DBContract.Drug.ARCHIVED, archived);
        return this;
    }

    public DrugColumns putWeekdays(String weekdays) {
        contentValues.put(DBContract.Drug.WEEKDAYS, weekdays);
        return this;
    }

    public DrugColumns putNotifyMe(int notifyMe) {
        contentValues.put(DBContract.Drug.NOTIFY_ME, notifyMe);
        return this;
    }
}