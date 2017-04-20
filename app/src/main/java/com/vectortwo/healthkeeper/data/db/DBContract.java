package com.vectortwo.healthkeeper.data.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ilya on 07/03/2017.
 */
public final class DBContract {
    private DBContract() {}

    public static final String AUTHORITY = "com.vectortwo.healthkeeper.HealthProvider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    static final String MIME_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.vectortwo.HealthProvider.";
    static final String MIME_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.vectortwo.HealthProvider.";

    static final int DB_VERSION = 1;
    static final String DB_NAME = "health_keeper.db";

    public static final class Pulse implements BaseColumns {
        static final String TABLE_NAME = "pulse";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String VALUE = "value";
        public static final String DATE = "date";
    }

    public static final class KnownDrugs implements BaseColumns {
        static final String TABLE_NAME = "known_drugs";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String TITLE = "title";
    }

    public static final class BloodSugar implements BaseColumns {
        static final String TABLE_NAME = "blood_sugar";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String VALUE = "value";
        public static final String DATE = "date";
        public static final String AFTER_FOOD = "after_food";
    }

    public static final class WellBeing implements BaseColumns {
        static final String TABLE_NAME = "well_being";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String VALUE = "value";
        public static final String DATE = "date";
    }

    public static final class BloodPressure implements BaseColumns {
        static final String TABLE_NAME = "blood_pressure";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String SYSTOLIC = "systolic";
        public static final String DIASTOLIC = "diastolic";
        public static final String DATE = "date";
    }

    public static final class Weight implements BaseColumns {
        static final String TABLE_NAME = "weight";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String VALUE = "value";
        public static final String DATE = "date";
    }

    public static final class Fluid implements BaseColumns {
        static final String TABLE_NAME = "fluid";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String DRANK = "drank";
        public static final String DATE = "date";
    }

    public static final class Sleep implements BaseColumns {
        static final String TABLE_NAME = "sleep";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String SLEEP_TIME = "sleep_time";

        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
    }

    public static final class Calorie implements BaseColumns {
        static final String TABLE_NAME = "calorie";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String GAINED = "gained";
        public static final String LOST = "lost";
        public static final String DATE = "date";
    }

    public static final class Drug implements BaseColumns {
        static final String TABLE_NAME = "drug";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String TITLE = "title";

        public static final String DESCRIPTION = "description";
        public static final String WARNINGS = "warnings";

        public static final String TIMES_A_DAY = "times_a_day";
        public static final String ARCHIVED = "archived";

        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";

        public static final String WEEKDAYS = "weekdays";
        public static final String NOTIFY_ME = "notify_me";

        public static final String TOTAL_AMOUNT = "total_amount";
    }

    public static final class Intake implements BaseColumns {
        static final String TABLE_NAME = "intake";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String DRUG_ID = "drug_id";
        public static final String WEEKDAY = "weekday";
        public static final String TIME = "time";
        public static final String FORM = "form";
        public static final String DOSAGE = "dosage";
    }

    public static final class Steps implements BaseColumns {
        static final String TABLE_NAME = "steps";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String COUNT = "count";
        public static final String HOUR = "hour";
        public static final String DATE = "date";
    }

    public static final class User implements BaseColumns {
        static final String TABLE_NAME = "user";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public static final String MIME_DIR_TYPE = DBContract.MIME_DIR_TYPE + TABLE_NAME;
        public static final String MIME_ITEM_TYPE = DBContract.MIME_ITEM_TYPE + TABLE_NAME;

        public static final String FIRSTNAME = "firstname";
        public static final String SECONDNAME = "secondname";

        public static final String SEX = "sex";
        public static final String AGE = "age";
        public static final String CITY = "city";

        public static final String HEIGHT = "height";
        public static final String WEIGHT = "weight";
        public static final String BIRTHDAY = "birthday";
    }
}