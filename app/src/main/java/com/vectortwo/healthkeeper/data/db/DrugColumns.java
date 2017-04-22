package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.Drug} table. Ensures type-safety.
 */
public class DrugColumns extends DBColumns {
    public DrugColumns putTitle(String title) {
        contentValues.put(DBContract.Drug.TITLE, title);
        return this;
    }

    public DrugColumns putTimesADay(int timesADay) {
        contentValues.put(DBContract.Drug.TIMES_A_DAY, timesADay);
        return this;
    }

    /**
     * When the user should start taking the drug (inclusive)?
     * @param date corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK"
     * @return this
     */
    public DrugColumns putStartDate(String date) {
        contentValues.put(DBContract.Drug.START_DATE, date);
        return this;
    }

    /**
     * When the user should stop taking the drug (inclusive)?
     * @param date corresponds to "Calendar.YEAR-Calendar.MONTH-Calendar.DAY_OF_WEEK"
     * @return this
     */
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

    /**
     * Whether or not current date reached end date.
     * @param archived 0 - false, 1 - true
     * @return this
     */
    public DrugColumns putArchived(int archived) {
        contentValues.put(DBContract.Drug.ARCHIVED, archived);
        return this;
    }

    /**
     * In what days this drug should be taken in?
     * @param weekdays string with no delimiters, where each character corresponds to Calendar.DAY_OF_WEEK
     *                 and represents a day the user should take the drug. For example, if a drug to be taken on mondays and fridays,
     *                 {@param weekdays} = "Calendar.MONDAY" + "Calendar.FRIDAY";
     * @return this
     */
    public DrugColumns putWeekdays(String weekdays) {
        contentValues.put(DBContract.Drug.WEEKDAYS, weekdays);
        return this;
    }

    /**
     * Should the user be sent notifications/reminders when it's time to take a drug?
     * @param notifyMe 1 - notify, 0 - don't.
     * @return this
     */
    public DrugColumns putNotifyMe(int notifyMe) {
        contentValues.put(DBContract.Drug.NOTIFY_ME, notifyMe);
        return this;
    }

    /**
     * Total amount of a drug the user intends to take.
     * @param totalAmount
     * @return this
     */
    public DrugColumns putTotalAmount(int totalAmount) {
        contentValues.put(DBContract.Drug.TOTAL_AMOUNT, totalAmount);
        return this;
    }
}