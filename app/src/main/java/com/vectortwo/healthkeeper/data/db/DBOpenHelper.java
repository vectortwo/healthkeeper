package com.vectortwo.healthkeeper.data.db;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Initializes and maintains a database connection for a database found in /assets/databases
 */
public class DBOpenHelper extends SQLiteAssetHelper {

    DBOpenHelper(Context context) {
        super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
    }
}