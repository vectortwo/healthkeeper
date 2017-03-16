package com.vectortwo.healthkeeper.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by ilya on 09/03/2017.
 */
public class DBContentProvider extends ContentProvider {

    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int USER                   = 0;
    private static final int USER_ROW               = 1;
    private static final int PULSE                  = 2;
    private static final int PULSE_ROW              = 3;
    private static final int BLOOD_PRESSURE         = 4;
    private static final int BLOOD_PRESSURE_ROW     = 5;
    private static final int BLOOD_SUGAR            = 6;
    private static final int BLOOD_SUGAR_ROW        = 7;
    private static final int WEIGHT                 = 8;
    private static final int WEIGHT_ROW             = 9;
    private static final int FLUID                  = 10;
    private static final int FLUID_ROW              = 11;
    private static final int SLEEP                  = 12;
    private static final int SLEEP_ROW              = 13;
    private static final int CALORIE                = 14;
    private static final int CALORIE_ROW            = 15;
    private static final int STEPS                  = 16;
    private static final int STEPS_ROW              = 17;
    private static final int DRUG                   = 18;
    private static final int DRUG_ROW               = 19;


    static {
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.User.TABLE_NAME, USER);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.User.TABLE_NAME + "/#", USER_ROW);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Pulse.TABLE_NAME, PULSE);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Pulse.TABLE_NAME + "/#", PULSE_ROW);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.BloodPressure.TABLE_NAME, BLOOD_PRESSURE);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.BloodPressure.TABLE_NAME + "/#", BLOOD_PRESSURE_ROW);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.BloodSugar.TABLE_NAME, BLOOD_SUGAR);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.BloodSugar.TABLE_NAME + "/#", BLOOD_SUGAR_ROW);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Weight.TABLE_NAME, WEIGHT);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Weight.TABLE_NAME + "/#", WEIGHT_ROW);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Fluid.TABLE_NAME, FLUID);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Fluid.TABLE_NAME + "/#", FLUID_ROW);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Sleep.TABLE_NAME, SLEEP);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Sleep.TABLE_NAME + "/#", SLEEP_ROW);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Calorie.TABLE_NAME, CALORIE);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Calorie.TABLE_NAME + "/#", CALORIE_ROW);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Steps.TABLE_NAME, STEPS);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Steps.TABLE_NAME + "/#", STEPS_ROW);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Drug.TABLE_NAME, DRUG);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Drug.TABLE_NAME + "/#", DRUG_ROW);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new DBOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case USER_ROW: case PULSE_ROW: case BLOOD_PRESSURE_ROW: case BLOOD_SUGAR_ROW: case WEIGHT_ROW:
            case FLUID_ROW: case SLEEP_ROW: case CALORIE_ROW: case STEPS_ROW: case DRUG_ROW:
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                queryBuilder.setTables(getTableName(uri));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri.toString());
        }
        db = dbOpenHelper.getReadableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        String mime;
        switch (URI_MATCHER.match(uri)) {
            case USER:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.User.TABLE_NAME;
                break;
            case USER_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.User.TABLE_NAME;
                break;
            case PULSE:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.Pulse.TABLE_NAME;
                break;
            case PULSE_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.Pulse.TABLE_NAME;
                break;
            case BLOOD_PRESSURE:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.BloodPressure.TABLE_NAME;
                break;
            case BLOOD_PRESSURE_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.BloodPressure.TABLE_NAME;
                break;
            case BLOOD_SUGAR:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.BloodSugar.TABLE_NAME;
                break;
            case BLOOD_SUGAR_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.BloodSugar.TABLE_NAME;
                break;
            case WEIGHT:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.Weight.TABLE_NAME;
                break;
            case WEIGHT_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.Weight.TABLE_NAME;
                break;
            case FLUID:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.Fluid.TABLE_NAME;
                break;
            case FLUID_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.Fluid.TABLE_NAME;
                break;
            case SLEEP:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.Sleep.TABLE_NAME;
                break;
            case SLEEP_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.Sleep.TABLE_NAME;
                break;
            case CALORIE:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.Calorie.TABLE_NAME;
                break;
            case CALORIE_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.Calorie.TABLE_NAME;
                break;
            case STEPS:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.Steps.TABLE_NAME;
                break;
            case STEPS_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.Steps.TABLE_NAME;
                break;
            case DRUG:
                mime = "vnd.android.cursor.dir/vnd.com.vectortwo.provider." + DBContract.Drug.TABLE_NAME;
                break;
            case DRUG_ROW:
                mime = "vnd.android.cursor.item/vnd.com.vectortwo.provider." + DBContract.Drug.TABLE_NAME;
                break;
           default:
                throw new IllegalArgumentException("Unknown URI: " + uri.toString());
        }
        return mime;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dbOpenHelper.getWritableDatabase();
        long id = db.insert(getTableName(uri), null, values);

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbOpenHelper.getWritableDatabase();
        int rowsDeleted = db.delete(getTableName(uri), selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = dbOpenHelper.getWritableDatabase();
        int rowsUpdated = db.update(getTableName(uri), values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    private String getTableName(Uri uri) {
        String table;
        switch (URI_MATCHER.match(uri)) {
            case USER:
                table = DBContract.User.TABLE_NAME;
                break;
            case PULSE:
                table = DBContract.Pulse.TABLE_NAME;
                break;
            case BLOOD_PRESSURE:
                table = DBContract.BloodPressure.TABLE_NAME;
                break;
            case BLOOD_SUGAR:
                table = DBContract.BloodSugar.TABLE_NAME;
                break;
            case WEIGHT:
                table = DBContract.Weight.TABLE_NAME;
                break;
            case FLUID:
                table = DBContract.Fluid.TABLE_NAME;
                break;
            case SLEEP:
                table = DBContract.Sleep.TABLE_NAME;
                break;
            case CALORIE:
                table = DBContract.Calorie.TABLE_NAME;
                break;
            case STEPS:
                table = DBContract.Steps.TABLE_NAME;
                break;
            case DRUG:
                table = DBContract.Drug.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri.toString());
        }
        return table;
    }
}