package com.vectortwo.healthkeeper.data.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ilya on 07/03/2017.
 */
public class DBContract {
    private DBContract() {}

    public static final String AUTHORITY = "com.vectortwo.healthkeeper";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    static final int DB_VERSION = 1;
    static final String DB_NAME = "health_keeper.db";

    static final String SQL_CREATE_TABLES =
            User.SQL_CREATE_TABLE +
                    Pulse.SQL_CREATE_TABLE +
                    BloodPressure.SQL_CREATE_TABLE +
                    BloodSugar.SQL_CREATE_TABLE +
                    Weight.SQL_CREATE_TABLE +
                    Fluid.SQL_CREATE_TABLE +
                    Sleep.SQL_CREATE_TABLE +
                    Calorie.SQL_CREATE_TABLE +
                    Steps.SQL_CREATE_TABLE +
                    Drug.SQL_CREATE_TABLE;

    static final String SQL_DROP_TABLES =
            User.SQL_DROP_TABLE +
                    Pulse.SQL_DROP_TABLE +
                    BloodPressure.SQL_DROP_TABLE +
                    BloodSugar.SQL_DROP_TABLE +
                    Weight.SQL_DROP_TABLE +
                    Fluid.SQL_DROP_TABLE +
                    Sleep.SQL_DROP_TABLE +
                    Calorie.SQL_DROP_TABLE +
                    Steps.SQL_DROP_TABLE +
                    Drug.SQL_DROP_TABLE;

    public static final class Pulse implements BaseColumns {
        static final String TABLE_NAME = "pulse";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String VALUE = "value";
        public static final String DATE = "date";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                VALUE + " INTEGER NOT NULL," +
                DATE + " TEXT);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static final class BloodSugar implements BaseColumns {
        static final String TABLE_NAME = "blood_sugar";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String VALUE = "value";
        public static final String DATE = "date";
        public static final String AFTER_FOOD = "after_food";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                VALUE + " INTEGER NOT NULL," +
                AFTER_FOOD + " INTEGER NOT NULL," +
                DATE + " TEXT);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static final class BloodPressure implements BaseColumns {
        static final String TABLE_NAME = "blood_pressure";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String SYSTOLIC = "systolic";
        public static final String DIASTOLIC = "diastolic";
        public static final String DATE = "date";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                SYSTOLIC + " INTEGER NOT NULL," +
                DIASTOLIC + " INTEGER NOT NULL," +
                DATE + " TEXT);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static final class Weight implements BaseColumns {
        static final String TABLE_NAME = "weight";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String VALUE = "value";
        public static final String DATE = "date";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                VALUE + " INTEGER NOT NULL," +
                DATE + " TEXT);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static final class Fluid implements BaseColumns {
        static final String TABLE_NAME = "fluid";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String DRANK = "drank";
        public static final String DATE = "date";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                DRANK + " REAL NOT NULL," +
                DATE + " TEXT);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static final class Sleep implements BaseColumns {
        static final String TABLE_NAME = "sleep";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String SLEEP_TIME = "sleep_time";

        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                SLEEP_TIME + " TEXT," +
                END_DATE + " TEXT," +
                START_DATE + " TEXT);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static final class Calorie implements BaseColumns {
        static final String TABLE_NAME = "calorie";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String GAINED = "gained";
        public static final String LOST = "lost";
        public static final String DATE = "date";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                GAINED + " INTEGER," +
                DATE + " TEXT," +
                LOST + " INTEGER);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static final class Drug implements BaseColumns {
        static final String TABLE_NAME = "drug";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String TITLE = "title";
        public static final String DOSAGE = "dosage";

        public static final String TIMES_A_DAY = "times_a_day";
        public static final String TIMES_A_WEEK = "times_a_week";

        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                TITLE + " TEXT NOT NULL," +
                DOSAGE + " REAL," +
                TIMES_A_DAY + " INTEGER," +
                TIMES_A_WEEK + " INTEGER," +
                START_DATE + " TEXT," +
                END_DATE + " TEXT);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static final class Steps implements BaseColumns {
        static final String TABLE_NAME = "steps";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String COUNT = "count";
        public static final String DATE = "date";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COUNT + " INTEGER NOT NULL," +
                DATE + " TEXT);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static final class User implements BaseColumns {
        static final String TABLE_NAME = "user";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String FIRSTNAME = "firstname";
        public static final String SECONDNAME = "secondname";

        public static final String SEX = "sex";
        public static final String AGE = "age";
        public static final String CITY = "city";

        public static final String HEIGHT = "height";
        public static final String WEIGHT = "weight";
        public static final String BIRTHDAY = "birthday";

        static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                FIRSTNAME + " TEXT NOT NULL," +
                SECONDNAME + " TEXT," +
                SEX + " TEXT," +
                AGE + " INTEGER," +
                HEIGHT + " INTEGER," +
                WEIGHT + " INTEGER," +
                CITY + " TEXT," +
                BIRTHDAY + " TEXT);";

        static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
}