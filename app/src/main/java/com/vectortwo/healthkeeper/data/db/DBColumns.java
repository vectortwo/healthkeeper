package com.vectortwo.healthkeeper.data.db;

import android.content.ContentValues;

/**
 * Created by ilya on 29/03/2017.
 */
public abstract class DBColumns {
    ContentValues contentValues;

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
