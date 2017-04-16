package com.vectortwo.healthkeeper.data.db;

import android.app.SearchManager;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ilya on 09/03/2017.
 */
public class DBContentProvider extends ContentProvider {

    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int TABLE_USER                 = 0;
    private static final int ITEM_USER                  = 1;
    private static final int TABLE_PULSE                = 2;
    private static final int ITEM_PULSE                 = 3;
    private static final int TABLE_BLOOD_PRESSURE       = 4;
    private static final int ITEM_BLOOD_PRESSURE        = 5;
    private static final int TABLE_BLOOD_SUGAR          = 6;
    private static final int ITEM_BLOOD_SUGAR           = 7;
    private static final int TABLE_WEIGHT               = 8;
    private static final int ITEM_WEIGHT                = 9;
    private static final int TABLE_FLUID                = 10;
    private static final int ITEM_FLUID                 = 11;
    private static final int TABLE_SLEEP                = 12;
    private static final int ITEM_SLEEP                 = 13;
    private static final int TABLE_CALORIE              = 14;
    private static final int ITEM_CALORIE               = 15;
    private static final int TABLE_STEPS                = 16;
    private static final int ITEM_STEPS                 = 17;
    private static final int TABLE_DRUG                 = 18;
    private static final int ITEM_DRUG                  = 19;
    private static final int TABLE_NOTIFY               = 20;
    private static final int ITEM_NOTIFY                = 21;

    private static final int DRUG_SUGGESTIONS           = 22;

    static {
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.User.TABLE_NAME, TABLE_USER);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.User.TABLE_NAME + "/#", ITEM_USER);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Pulse.TABLE_NAME, TABLE_PULSE);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Pulse.TABLE_NAME + "/#", ITEM_PULSE);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.BloodPressure.TABLE_NAME, TABLE_BLOOD_PRESSURE);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.BloodPressure.TABLE_NAME + "/#", ITEM_BLOOD_PRESSURE);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.BloodSugar.TABLE_NAME, TABLE_BLOOD_SUGAR);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.BloodSugar.TABLE_NAME + "/#", ITEM_BLOOD_SUGAR);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Weight.TABLE_NAME, TABLE_WEIGHT);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Weight.TABLE_NAME + "/#", ITEM_WEIGHT);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Fluid.TABLE_NAME, TABLE_FLUID);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Fluid.TABLE_NAME + "/#", ITEM_FLUID);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Sleep.TABLE_NAME, TABLE_SLEEP);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Sleep.TABLE_NAME + "/#", ITEM_SLEEP);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Calorie.TABLE_NAME, TABLE_CALORIE);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Calorie.TABLE_NAME + "/#", ITEM_CALORIE);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Steps.TABLE_NAME, TABLE_STEPS);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Steps.TABLE_NAME + "/#", ITEM_STEPS);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Drug.TABLE_NAME, TABLE_DRUG);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Drug.TABLE_NAME + "/#", ITEM_DRUG);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Notify.TABLE_NAME, TABLE_NOTIFY);
        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.Notify.TABLE_NAME + "/#", ITEM_NOTIFY);

        URI_MATCHER.addURI(DBContract.AUTHORITY, DBContract.KnownDrugs.TABLE_NAME + "/*", DRUG_SUGGESTIONS);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new DBOpenHelper(getContext());
        return true;
    }

    private Cursor getSuggestions(String from) {
        String query = "%" + from + "%";

        db = dbOpenHelper.getReadableDatabase();
        return db.query(
                DBContract.KnownDrugs.TABLE_NAME,
                new String[] {DBContract.KnownDrugs._ID, DBContract.KnownDrugs.TITLE},
                DBContract.KnownDrugs.TITLE + " like ?",
                new String[]{query},
                null,
                null,
                "length(" + DBContract.KnownDrugs.TITLE + ") limit 10");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case DRUG_SUGGESTIONS:
                String query = uri.getLastPathSegment();
                return getSuggestions(query);
            case TABLE_USER:
                queryBuilder.setTables(DBContract.User.TABLE_NAME);
                break;
            case ITEM_USER:
                queryBuilder.setTables(DBContract.User.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_PULSE:
                queryBuilder.setTables(DBContract.Pulse.TABLE_NAME);
                break;
            case ITEM_PULSE:
                queryBuilder.setTables(DBContract.Pulse.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_BLOOD_PRESSURE:
                queryBuilder.setTables(DBContract.BloodPressure.TABLE_NAME);
                break;
            case ITEM_BLOOD_PRESSURE:
                queryBuilder.setTables(DBContract.BloodPressure.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_BLOOD_SUGAR:
                queryBuilder.setTables(DBContract.BloodSugar.TABLE_NAME);
                break;
            case ITEM_BLOOD_SUGAR:
                queryBuilder.setTables(DBContract.BloodSugar.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_WEIGHT:
                queryBuilder.setTables(DBContract.Weight.TABLE_NAME);
                break;
            case ITEM_WEIGHT:
                queryBuilder.setTables(DBContract.Weight.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_FLUID:
                queryBuilder.setTables(DBContract.Fluid.TABLE_NAME);
                break;
            case ITEM_FLUID:
                queryBuilder.setTables(DBContract.Fluid.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_SLEEP:
                queryBuilder.setTables(DBContract.Sleep.TABLE_NAME);
                break;
            case ITEM_SLEEP:
                queryBuilder.setTables(DBContract.Sleep.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_CALORIE:
                queryBuilder.setTables(DBContract.Calorie.TABLE_NAME);
                break;
            case ITEM_CALORIE:
                queryBuilder.setTables(DBContract.Calorie.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_STEPS:
                queryBuilder.setTables(DBContract.Steps.TABLE_NAME);
                break;
            case ITEM_STEPS:
                queryBuilder.setTables(DBContract.Steps.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_DRUG:
                queryBuilder.setTables(DBContract.Drug.TABLE_NAME);
                break;
            case ITEM_DRUG:
                queryBuilder.setTables(DBContract.Drug.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
                break;
            case TABLE_NOTIFY:
                queryBuilder.setTables(DBContract.Notify.TABLE_NAME);
                break;
            case ITEM_NOTIFY:
                queryBuilder.setTables(DBContract.Notify.TABLE_NAME);
                queryBuilder.appendWhere("_ID=" + uri.getLastPathSegment());
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
            case TABLE_USER:
                mime = DBContract.User.MIME_DIR_TYPE;
                break;
            case ITEM_USER:
                mime = DBContract.User.MIME_ITEM_TYPE;
                break;
            case TABLE_PULSE:
                mime = DBContract.Pulse.MIME_DIR_TYPE;
                break;
            case ITEM_PULSE:
                mime = DBContract.Pulse.MIME_ITEM_TYPE;
                break;
            case TABLE_BLOOD_PRESSURE:
                mime = DBContract.BloodPressure.MIME_DIR_TYPE;
                break;
            case ITEM_BLOOD_PRESSURE:
                mime = DBContract.BloodPressure.MIME_ITEM_TYPE;
                break;
            case TABLE_BLOOD_SUGAR:
                mime = DBContract.BloodSugar.MIME_DIR_TYPE;
                break;
            case ITEM_BLOOD_SUGAR:
                mime = DBContract.BloodSugar.MIME_ITEM_TYPE;
                break;
            case TABLE_WEIGHT:
                mime = DBContract.Weight.MIME_DIR_TYPE;
                break;
            case ITEM_WEIGHT:
                mime = DBContract.Weight.MIME_ITEM_TYPE;
                break;
            case TABLE_FLUID:
                mime = DBContract.Fluid.MIME_DIR_TYPE;
                break;
            case ITEM_FLUID:
                mime = DBContract.Fluid.MIME_ITEM_TYPE;
                break;
            case TABLE_SLEEP:
                mime = DBContract.Sleep.MIME_DIR_TYPE;
                break;
            case ITEM_SLEEP:
                mime = DBContract.Sleep.MIME_ITEM_TYPE;
                break;
            case TABLE_CALORIE:
                mime = DBContract.Calorie.MIME_DIR_TYPE;
                break;
            case ITEM_CALORIE:
                mime = DBContract.Calorie.MIME_ITEM_TYPE;
                break;
            case TABLE_STEPS:
                mime = DBContract.Steps.MIME_DIR_TYPE;
                break;
            case ITEM_STEPS:
                mime = DBContract.Steps.MIME_ITEM_TYPE;
                break;
            case TABLE_DRUG:
                mime = DBContract.Drug.MIME_DIR_TYPE;
                break;
            case ITEM_DRUG:
                mime = DBContract.Drug.MIME_ITEM_TYPE;
                break;
            case TABLE_NOTIFY:
                mime = DBContract.Notify.MIME_DIR_TYPE;
                break;
            case ITEM_NOTIFY:
                mime = DBContract.Notify.MIME_ITEM_TYPE;
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
            case TABLE_USER: case ITEM_USER:
                table = DBContract.User.TABLE_NAME;
                break;
            case TABLE_PULSE: case ITEM_PULSE:
                table = DBContract.Pulse.TABLE_NAME;
                break;
            case TABLE_BLOOD_PRESSURE: case ITEM_BLOOD_PRESSURE:
                table = DBContract.BloodPressure.TABLE_NAME;
                break;
            case TABLE_BLOOD_SUGAR: case ITEM_BLOOD_SUGAR:
                table = DBContract.BloodSugar.TABLE_NAME;
                break;
            case TABLE_WEIGHT: case ITEM_WEIGHT:
                table = DBContract.Weight.TABLE_NAME;
                break;
            case TABLE_FLUID: case ITEM_FLUID:
                table = DBContract.Fluid.TABLE_NAME;
                break;
            case TABLE_SLEEP: case ITEM_SLEEP:
                table = DBContract.Sleep.TABLE_NAME;
                break;
            case TABLE_CALORIE: case ITEM_CALORIE:
                table = DBContract.Calorie.TABLE_NAME;
                break;
            case TABLE_STEPS: case ITEM_STEPS:
                table = DBContract.Steps.TABLE_NAME;
                break;
            case TABLE_DRUG: case ITEM_DRUG:
                table = DBContract.Drug.TABLE_NAME;
                break;
            case TABLE_NOTIFY: case ITEM_NOTIFY:
                table = DBContract.Notify.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri.toString());
        }
        return table;
    }
}