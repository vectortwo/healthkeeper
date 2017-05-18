package com.vectortwo.healthkeeper.data.pdf;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ilya on 05/05/2017.
 */

public class DateInterval {

    public static final int INTERVAL_NO_INTERVAL = 0;
    public static final int INTERVAL_3MONTHS = 1;
    public static final int INTERVAL_YEAR = 2;

    private Calendar minDate, maxDate;

    public DateInterval(int interval) {
        switch (interval) {
            case INTERVAL_NO_INTERVAL:
                this.minDate = getActualMinimumDate();
                this.maxDate = getActualMaximumDate();
                break;

            case INTERVAL_3MONTHS:
                this.minDate = getDate3MonthsBack();
                this.maxDate = getActualMaximumDate();
                break;

            case INTERVAL_YEAR:
                this.minDate = getDateYearBack();
                this.maxDate = getActualMaximumDate();
                break;

            default:
                final String msg = "Invalid interval parameter in the constructor";
                throw new IllegalArgumentException(msg);
        }
    }

    public DateInterval(@NonNull Calendar minDate, @NonNull  Calendar maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;

        minDate.add(Calendar.DAY_OF_MONTH, -1);
        maxDate.add(Calendar.DAY_OF_MONTH, 1);
    }

    boolean withinDates(Date date) {
        return date.after(minDate.getTime()) && date.before(maxDate.getTime());
    }

    private static Calendar getActualMinimumDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(Long.MIN_VALUE));
        return cal;
    }

    private static Calendar getActualMaximumDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 5);
        return cal;
    }

    private static Calendar getDate3MonthsBack() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal;
    }

    private static Calendar getDateYearBack() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal;
    }
}