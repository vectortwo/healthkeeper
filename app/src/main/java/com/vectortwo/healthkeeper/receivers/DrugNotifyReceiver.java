package com.vectortwo.healthkeeper.receivers;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.notifications.DrugNotifyNotification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by ilya on 06/04/2017.
 */
public class DrugNotifyReceiver extends WakefulBroadcastReceiver {

    public static final String ACTION_SCHEDULE           = "com.vectortwo.healthkeeper.broadcast.DRUG_NOTIFY_SCHEDULE";
    public static final String ACTION_CANCEL             = "com.vectortwo.healthkeeper.broadcast.DRUG_NOTIFY_CANCEL";
    public static final String ACTION_SCHEDULE_NOTIFY   = "com.vectortwo.healthkeeper.broadcast.DRUG_NOTIFY_SCHEDULE_NOTIFY";

    @Override
    public void onReceive(Context context, Intent intent) {
        int drugID = getDrugID(intent);

        Intent drugNotifyService = new Intent(context, DrugNotifyService.class);
        drugNotifyService.putExtra(DBContract.Notify.DRUG_ID, drugID);
        drugNotifyService.setAction(intent.getAction());

        startWakefulService(context, drugNotifyService);
    }

    public static Intent intentForBroadcast(String action, int drugID) {
        Intent intent = new Intent(action);
        intent.putExtra(DBContract.Notify.DRUG_ID, drugID);
        return intent;
    }

    private static int getDrugID(Intent intent) {
        int drugID = intent.getIntExtra(DBContract.Notify.DRUG_ID, -1);
        if (drugID < 0) {
            final String msg = "Invalid drugID";
            throw new IllegalArgumentException(msg);
        }
        return drugID;
    }

    public static class DrugNotifyService extends IntentService {
        public DrugNotifyService() {
            super("DrugNotifyService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            int drugID = getDrugID(intent);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            // Canceling scheduled notification
            if (DrugNotifyReceiver.ACTION_CANCEL.equals(intent.getAction())) {
                cancelNotifications(this, alarmManager, drugID);
                return;
            }

            Intent notifyIntent = new Intent(DrugNotifyReceiver.ACTION_SCHEDULE_NOTIFY);
            notifyIntent.putExtra(DBContract.Notify.DRUG_ID, drugID);

            PendingIntent scheduleIntent = PendingIntent.getBroadcast(this, drugID, notifyIntent, 0);

            Cursor drugCursor = getContentResolver().query(
                    DBContract.Drug.CONTENT_URI,
                    new String[]{DBContract.Drug.TITLE, DBContract.Drug.START_DATE, DBContract.Drug.END_DATE},
                    BaseColumns._ID + "=?",
                    new String[]{Integer.toString(drugID)},
                    null);
            Cursor notifyCursor = getContentResolver().query(
                    DBContract.Notify.CONTENT_URI,
                    new String[]{DBContract.Notify._ID, DBContract.Notify.TIME, DBContract.Notify.WEEKDAY},
                    DBContract.Notify.DRUG_ID + "=?",
                    new String[]{Integer.toString(drugID)},
                    "substr('0' || " + DBContract.Notify.TIME + ", -5, 5)");

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
                int columnTime = notifyCursor.getColumnIndex(DBContract.Notify.TIME);
                int columnWeekday = notifyCursor.getColumnIndex(DBContract.Notify.WEEKDAY);
                String time = notifyCursor.getString(columnTime);
                int weekday = notifyCursor.getInt(columnWeekday);
                if (!times.contains(time)) {
                    times.add(notifyCursor.getString(columnTime));
                }
                if (!weekdays.contains(weekday)) {
                    weekdays.add(notifyCursor.getInt(columnWeekday));
                }
            }
            Calendar cal = Calendar.getInstance();

            String currentTime = cal.get(Calendar.HOUR_OF_DAY) + "-" + cal.get(Calendar.MINUTE);

            Calendar currentDate = (Calendar) cal.clone();

            String startDateStr = drugCursor.getString(drugCursor.getColumnIndex(DBContract.Drug.START_DATE));
            Calendar startDate = (Calendar) currentDate.clone();
            setDate(startDateStr, startDate);

            String endDateStr = drugCursor.getString(drugCursor.getColumnIndex(DBContract.Drug.END_DATE));
            Calendar endDate = (Calendar) currentDate.clone();
            setDate(endDateStr, endDate);

            Calendar scheduleDate = getScheduleDate(currentTime, currentDate, startDate, endDate, weekdays, times);
            if (scheduleDate == null) {
                getContentResolver().delete(
                        DBContract.Notify.CONTENT_URI,
                        DBContract.Notify.DRUG_ID + "=" + drugID,
                        null);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, scheduleDate.getTimeInMillis(), scheduleIntent);
            }

            if (DrugNotifyReceiver.ACTION_SCHEDULE_NOTIFY.equals(intent.getAction())) {
                DrugNotifyNotification notification = new DrugNotifyNotification(this, drugID);
                String drugTitle = drugCursor.getString(drugCursor.getColumnIndex(DBContract.Drug.TITLE));

                notification.getBuilder().setContentTitle(drugTitle);
                notification.update();
            }

            drugCursor.close();
            notifyCursor.close();

            DrugNotifyReceiver.completeWakefulIntent(intent);
        }

        private void cancelNotifications(Context context, AlarmManager alarmManager, int drugID) {
            PendingIntent cancelIntent = PendingIntent.getBroadcast(context, drugID, new Intent(DrugNotifyReceiver.ACTION_SCHEDULE_NOTIFY), 0);
            alarmManager.cancel(cancelIntent);

            cancelIntent = PendingIntent.getBroadcast(context, drugID, new Intent(DrugNotifyReceiver.ACTION_SCHEDULE), 0);
            alarmManager.cancel(cancelIntent);
        }

        private void setDate(String dateFormatted, Calendar out) {
            String[] dateComponents = dateFormatted.split("-");
            int year = Integer.parseInt(dateComponents[0]);
            int month = Integer.parseInt(dateComponents[1]);
            int day = Integer.parseInt(dateComponents[2]);
            out.set(year, month, day);
        }

        private int compareTimes(String x, String y) {
            String[] xsStr = x.split("-");
            String[] ysStr = y.split("-");
            int[] xs = new int[]{Integer.parseInt(xsStr[0]), Integer.parseInt(xsStr[1])};
            int[] ys = new int[]{Integer.parseInt(ysStr[0]), Integer.parseInt(ysStr[1])};
            if (Arrays.equals(xs, ys)) {
                return 0;
            }
            if (xs[0] > ys[0] || (xs[0] == ys[0] && xs[1] > ys[1])) {
                return 1;
            } else {
                return -1;
            }
        }

        private Calendar getScheduleDate(String currentTime, Calendar currentDate, Calendar startDate, Calendar endDate,
                                                ArrayList<Integer> weekdays, /*sorted*/ ArrayList<String> times) {
            // startDate is in the past

            if (currentDate.compareTo(startDate) > 0)
                return getScheduleDate(currentTime, currentDate, currentDate, endDate, weekdays, times);

            Calendar res = (Calendar) startDate.clone();

            Collections.sort(weekdays);

            String desiredTime = times.get(0);
            int currentWeekday = currentDate.get(Calendar.DAY_OF_WEEK);
            int desiredWeekday = -1;

            if (currentDate.compareTo(startDate) < 0) {
                currentWeekday = startDate.get(Calendar.DAY_OF_WEEK);
            }
            // Find closest weekday
            for (int weekday : weekdays) {
                if (currentWeekday <= weekday) {
                    desiredWeekday = weekday;
                    break;
                }
            }

            // Check for available times and correct desiredWeekday where there aren't any
            if (desiredWeekday == currentWeekday && currentDate.compareTo(startDate) == 0) {
                desiredTime = null;
                for (String time : times) {
                    if (compareTimes(time, currentTime) > 0) {
                        desiredTime = time;
                        break;
                    }
                }
                // No time available, reschedule for next day
                if (desiredTime == null) {
                    desiredWeekday = -1;
                    for (int weekday : weekdays) {
                        if (currentWeekday < weekday) {
                            desiredWeekday = weekday;
                            desiredTime = times.get(0);
                            break;
                        }
                    }
                }
            }

            // No available terms this week, schedule for next week
            if (desiredWeekday == -1) {
                desiredWeekday = weekdays.get(0);
                desiredTime = times.get(0);

                // set Calendar for next week
                res.add(Calendar.WEEK_OF_MONTH, 1);
            }
            res.add(Calendar.DAY_OF_WEEK, desiredWeekday - startDate.get(Calendar.DAY_OF_WEEK));

            String[] desiredTimeComps = desiredTime.split("-");
            res.set(Calendar.HOUR_OF_DAY, Integer.parseInt(desiredTimeComps[0]));
            res.set(Calendar.MINUTE, Integer.parseInt(desiredTimeComps[1]));

            return (res.compareTo(endDate) > 0) ? null : res;
        }
    }
}