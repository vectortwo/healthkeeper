package com.vectortwo.healthkeeper.data.db;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by ilya on 07/03/2017.
 */
public class DBOpenHelper extends SQLiteAssetHelper {

    DBOpenHelper(Context context) {
        super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
    }
}