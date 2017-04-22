package com.vectortwo.healthkeeper.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ilya on 21/04/2017.
 */
public final class TempDBCleaner {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    private final String[] tables = new String[] {
            "pulse", "known_drugs", "blood_sugar", "blood_pressure", "calorie",
            "well_being", "weight", "fluid", "sleep", "drug", "intake", "steps", "user"};

    public TempDBCleaner(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public void dropAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db = dbOpenHelper.getWritableDatabase();
                for (String table : tables) {
                    db.delete(table, null, null);
                }
                db.close();
            }
        }).start();
    }
}
