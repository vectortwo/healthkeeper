package com.vectortwo.healthkeeper.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ilya on 08/05/2017.
 */
public final class Utils {

    public static final SimpleDateFormat sdf_yMd = new SimpleDateFormat("y-M-d");
    public static final SimpleDateFormat sdf_yMdHM = new SimpleDateFormat("y-M-d HH:mm");

    /**
     * Insert x between xs and concat the whole thing.
     * @param x
     * @param xs
     * @return String in a form "xs[0] + x + xs[1] + x + ... + xs[n]"
     */
    public static String intersperse(final String x, final String[] xs) {
        String str = "";
        for (int i = 0; i < xs.length - 1; i++) {
            str += xs[i] + x;
        }
        str += xs[xs.length - 1];
        return str;
    }

    public static String intersperse(final String x, final ArrayList<String> xs) {
        String str = "";
        for (int i = 0; i < xs.size() - 1; i++) {
            str += xs.get(i) + x;
        }
        str += xs.get(xs.size() - 1);
        return str;
    }

    public static ArrayList<String> lines(final String xs) {
        return new ArrayList<>(Arrays.asList(xs.split("\n")));
    }

    public static String addLeadZeros(String date) {
        String[] compsStr = date.split("-");
        Integer[] comps = new Integer[] {
                Integer.parseInt(compsStr[1]),     // month
                Integer.parseInt(compsStr[2])};    // day_of_month
        if (comps[0] < 10) {
            compsStr[1] = "0" + compsStr[1];
        }
        if (comps[1] < 10) {
            compsStr[2] = "0" + compsStr[2];
        }

        return intersperse("-", compsStr);
    }


    /**
     * Increases month by 1 for accurate SimpleDateFormat parsing
     * @param str date corresponding to "Calendar.YEAR-Calendar.MONTH-..."
     * @return String parsed date with increased month
     */
    public static String fixMonth(String str) {
        String res;
        String[] comps = str.split("-");
        int fixedMonth = Integer.parseInt(comps[1]) + 1;
        res = comps[0] + "-" + String.valueOf(fixedMonth) + "-";
        for (int i = 2; i < comps.length-1; i++) {
            res += comps[i] + "-";
        }
        res += comps[comps.length-1];

        return res;
    }
}
