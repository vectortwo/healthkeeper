package com.vectortwo.healthkeeper.data.db;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.vectortwo.healthkeeper.services.DrugNotifyService;

/**
 * Created by ilya on 21/04/2017.
 */
// todo: remove on release
public final class TempDBCleaner {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    private Context cntx;

    private final String[] tables = new String[] {
            "pulse", "blood_sugar", "blood_pressure", "calorie",
            "well_being", "weight", "fluid", "sleep", "drug", "intake", "steps"};

    public TempDBCleaner(Context context) {
        this.cntx = context;
        dbOpenHelper = new DBOpenHelper(context);
    }

    public void dropAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db = dbOpenHelper.getWritableDatabase();

                Cursor c = cntx.getContentResolver().query(
                        DBContract.Drug.CONTENT_URI,
                        new String[] {DBContract.Drug._ID},
                        DBContract.Drug.NOTIFY_ME + "=1", null, null);

                while (c.moveToNext()) {
                    int colId = c.getColumnIndex(DBContract.Drug._ID);
                    int id = c.getInt(colId);
                    Intent i = new Intent(cntx, DrugNotifyService.class);
                    i.setAction(DrugNotifyService.ACTION_CANCEL);
                    i.putExtra(DrugNotifyService.KEY_DRUG_ID, id);
                    cntx.startService(i);
                }
                c.close();

                for (String table : tables) {
                    db.delete(table, null, null);
                }
                db.close();
            }
        }).start();
    }
}
