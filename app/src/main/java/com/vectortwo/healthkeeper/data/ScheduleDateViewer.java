package com.vectortwo.healthkeeper.data;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;
import com.vectortwo.healthkeeper.data.db.IntakeEarlyColumns;
import com.vectortwo.healthkeeper.services.DrugNotifyService;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ilya on 15/05/2017.
 */
public class ScheduleDateViewer extends AsyncTask<Integer, Void, ScheduleDateViewer.ScheduleInfo> {

    private Context mContext;
    private IResultHandler mHandler;

    public class ScheduleInfo {
        Calendar scheduleDate = null;
        String form;
        float dosage = -1;

        public ScheduleInfo(@Nullable Calendar scheduleDate, float dosage, String form) {
            this.scheduleDate = scheduleDate;
            this.dosage = dosage;
            this.form = form;
        }

        public @Nullable Calendar getScheduleDate() {
            return scheduleDate;
        }

        public float getDosage() {
            return dosage;
        }

        public String getForm() {
            return form;
        }
    }

    public ScheduleDateViewer(Context context, IResultHandler handler) {
        mContext = context;
        mHandler = handler;
    }

    public interface IResultHandler {
        void onResult(@Nullable ScheduleInfo result);
    }

    @Override
    protected void onPostExecute(ScheduleInfo result) {
        mHandler.onResult(result);
    }

    @Nullable
    @Override
    protected ScheduleInfo doInBackground(@NonNull Integer... ints) {
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
                new String[]{DBContract.Intake._ID, DBContract.Intake.TIME,
                        DBContract.Intake.WEEKDAY, DBContract.Intake.DOSAGE, DBContract.Intake.FORM},
                DBContract.Intake.DRUG_ID + "=?",
                new String[]{Integer.toString(drugID)},
                "substr('0' || " + DBContract.Intake.TIME + ", -5, 5)");

        drugCursor.moveToFirst();

        if (drugCursor.getCount() == 0) {
            final String msg = "Drug " + drugID + " does not exist in the database!";
            throw new IllegalStateException(msg);
        }

        if (notifyCursor.getCount() == 0) {
            return null;
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

        ArrayList<Calendar> alreadyTook = new ArrayList<>();
        Cursor tookCursor = mContext.getContentResolver().query(
                DBContract.IntakeEarly.CONTENT_URI, null,
                DBContract.IntakeEarly.DATE + ">= date('now')",
                null, null);

        if (tookCursor.getCount() > 0) {
            while (tookCursor.moveToNext()) {
                Calendar tookDate = IntakeEarlyColumns.getDate(tookCursor);
                alreadyTook.add(tookDate);
            }
        }
        tookCursor.close();

        Calendar scheduleDate = DrugNotifyService.getScheduleDate(currentDate, startDate, endDate, weekdays, times, alreadyTook);

        float scheduleDosage = -1;
        String form = "";
        if (scheduleDate != null) {
            notifyCursor.moveToPosition(-1);
            while (notifyCursor.moveToNext()) {
                int columnTime = notifyCursor.getColumnIndex(DBContract.Intake.TIME);
                int columnWeekday = notifyCursor.getColumnIndex(DBContract.Intake.WEEKDAY);
                String time = notifyCursor.getString(columnTime);
                int weekday = notifyCursor.getInt(columnWeekday);

                String scheduleTime = scheduleDate.get(Calendar.HOUR_OF_DAY) +
                        "-" + scheduleDate.get(Calendar.MINUTE);
                if (weekday == scheduleDate.get(Calendar.DAY_OF_WEEK) && time.equals(scheduleTime)) {
                    int colDosage = notifyCursor.getColumnIndex(DBContract.Intake.DOSAGE);
                    scheduleDosage = notifyCursor.getFloat(colDosage);

                    int colForm = notifyCursor.getColumnIndex(DBContract.Intake.FORM);
                    form = notifyCursor.getString(colForm);
                    break;
                }
            }
        }

        drugCursor.close();
        notifyCursor.close();

        return new ScheduleInfo(scheduleDate, scheduleDosage, form);
    }
}