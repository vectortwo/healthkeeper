package com.vectortwo.healthkeeper.data.db;

import android.content.ContentValues;

/**
 *  Base class for managing {@link ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for specific table. Ensures type-safety.
 */
public abstract class DBColumns {
    protected ContentValues contentValues;

    public DBColumns() {
        contentValues = new ContentValues();
    }

    public DBColumns(ContentValues from) {
        contentValues = new ContentValues(from);
    }

    public ContentValues getContentValues() {
        return contentValues;
    }
}
