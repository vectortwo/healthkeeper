package com.vectortwo.healthkeeper;

import com.vectortwo.healthkeeper.data.Utils;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Helper class for manipulating Pedometer-formatted dates.
 */
public final class PedometerDateFormat {

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        return year + "-" + month + "-" + day + "-" + hour;
    }

    public static boolean sameDay(String dateA, String dateB) {
        String[] xs = dateA.split("-");
        String[] ys = dateB.split("-");
        return xs[0].equals(ys[0]) && xs[1].equals(ys[1]) && xs[2].equals(ys[2]);
    }

    public static boolean sameHour(String dateA, String dateB) {
        String[] xs = dateA.split("-");
        String[] ys = dateB.split("-");
        return xs[0].equals(ys[0]) && xs[1].equals(ys[1]) && xs[2].equals(ys[2]) && xs[3].equals(ys[3]);
    }

    public static int getHour(String date) {
        return Integer.parseInt(date.substring(date.lastIndexOf("-") + 1));
    }

    /**
     * Use only when writing result to db!
     */
    public static Calendar getDateWithoutHour(String dateWithHour) {
        dateWithHour = dateWithHour.substring(0, dateWithHour.lastIndexOf("-"));
        dateWithHour = Utils.fixMonth(dateWithHour);
        dateWithHour = Utils.addLeadZeros(dateWithHour);

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(Utils.sdf_yMd.parse(dateWithHour));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal;
    }

    public static int getOnReceivedHour(String date) {
        int hour = getHour(date);
        return (hour == 0) ? 23 : hour - 1;
    }

    public static int getOnKilledHour(String date) {
        int hour = getHour(date);
        return (hour == 0) ? 23 : hour;
    }
}
