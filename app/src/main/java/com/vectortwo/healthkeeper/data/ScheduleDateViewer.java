package com.vectortwo.healthkeeper.data;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;
import com.vectortwo.healthkeeper.services.DrugNotifyService;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ilya on 15/05/2017.
 */
public class ScheduleDateViewer extends AsyncTask<Integer, Void, Calendar> {

    private Context mContext;
    private IResultHandler mHandler;

    public ScheduleDateViewer(Context context, IResultHandler handler) {
        mContext = context;
        mHandler = handler;
    }

    public interface IResultHandler {
        void onResult(@Nullable Calendar scheduleDate);
    }

    @Override
    protected void onPostExecute(Calendar calendar) {
        mHandler.onResult(calendar);
    }

    @Override
    @Nullable
    protected Calendar doInBackground(@NonNull Integer... ints) {
        int drugID = ints[0];
        if (drugID < 0) {
            final String msg = "Invalid drugId " + drugID;
            throw new IllegalArgumentException(msg);
        }

        Cursor drugCursor = mContext.getContentResolver().query(
                DBContract.Drug.CONTENT_URI,
                new String[]{DBContract.Drug.START_DATE, DBContract.Drug.END_DATE},
                BaseColumns._ID + "=?",
                new String[]{Integer.toString(drugID)},
                null);
        Cursor notifyCursor = mContext.getContentResolver().query(
                DBContract.Intake.CONTENT_URI,
                new String[]{DBContract.Intake._ID, DBContract.Intake.TIME, DBContract.Intake.WEEKDAY},
                DBContract.Intake.DRUG_ID + "=?",
                new String[]{Integer.toString(drugID)},
                "substr('0' || " + DBContract.Intake.TIME + ", -5, 5)");

        drugCursor.moveToFirst();

        if (drugCursor.getCount() == 0) {
            final String msg = "Drug " + drugID + " does not exist in the database!";
            throw new IllegalStateException(msg);
        }
        if (notifyCursor.getCount() == 0) {
            final String msg = "Notification info for drug " + drugID + " does not exist in the database!";
            throw new IllegalStateException(msg);
        }

        ArrayList<Integer> weekdays = new ArrayList<>();
        ArrayList<String> times = new ArrayList<>();

        while (notifyCursor.moveToNext()) {
            int columnTime = notifyCursor.getColumnIndex(DBContract.Intake.TIME);
            int columnWeekday = notifyCursor.getColumnIndex(DBContract.Intake.WEEKDAY);
            String time = notifyCursor.getString(columnTime);
            int weekday = notifyCursor.getInt(columnWeekday);
            if (!times.contains(time)) {
                times.add(notifyCursor.getString(columnTime));
            }
            if (!weekdays.contains(weekday)) {
                weekdays.add(notifyCursor.getInt(columnWeekday));
            }
        }
        Calendar currentDate = Calendar.getInstance();

        Calendar startDate = DrugColumns.getStartDate(drugCursor);
        Calendar endDate = DrugColumns.getEndDate(drugCursor);

        Calendar scheduleDate = DrugNotifyService.getScheduleDate(currentDate, startDate, endDate, weekdays, times);

        drugCursor.close();
        notifyCursor.close();

        return scheduleDate;
    }
}
