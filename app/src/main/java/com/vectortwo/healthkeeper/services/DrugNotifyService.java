package com.vectortwo.healthkeeper.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import java.util.Calendar;
import android.provider.BaseColumns;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.notifications.DrugIntakeNotification;
import com.vectortwo.healthkeeper.receivers.DrugNotifyReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *  Drug notification scheduler service.
 *  Accepts an {@link Intent} with
 *  {@link #ACTION_SCHEDULE} to schedule a notification for a first time
 *  {@link #ACTION_RESCHEDULE} to reschedule it, call when the underlying data has been changed
 *  {@link #ACTION_CANCEL} to cancel it, call when the drug or notifications are deleted/disabled
 *  Operates on a drug with id found in {@link #KEY_DRUG_ID} extra data of the passed Intent.
 *  Won't schedule if the end date precedes the schedule date.
 */
public class DrugNotifyService extends IntentService {

    public static final String ACTION_SCHEDULE = "com.vectortwo.healthkeeper.intent.DRUG_NOTIFY_SCHEDULE";
    public static final String ACTION_RESCHEDULE = "com.vectortwo.healthkeeper.intent.DRUG_NOTIFY_RESCHEDULE";
    public static final String ACTION_CANCEL = "com.vectortwo.healthkeeper.intent.DRUG_NOTIFY_CANCEL";
    public static final String ACTION_POSTPONE = "com.vectortwo.healthkeeper.intent.DRUG_NOTIFY_POSTPONE";

    private static final String ACTION_NOTIFY = "com.vectortwo.healthkeeper.intent.DRUG_NOTIFY_NOTIFY";
    private static final String ACTION_SCHEDULE_NOTIFY = "com.vectortwo.healthkeeper.intent.DRUG_NOTIFY_SCHEDULE_NOTIFY";

    public static final String KEY_DRUG_ID = DBContract.Intake.DRUG_ID;
    public static final String KEY_POSTPONE_TIME = "postpone_time";
    public static final String KEY_CURRENT_POSTPONE_COUNT = "current_postpone_count";

    private AlarmManager alarmManager;

    public DrugNotifyService() {
        super("DrugNotifyService");
    }

    @Override
    public void onCreate() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int drugID = getDrugID(intent);

        switch (intent.getAction()) {
            case ACTION_CANCEL:
                cancelNotifications(drugID);
                break;

            case ACTION_POSTPONE:
                postponeNotification(intent, drugID);
                break;

            case ACTION_SCHEDULE:
                scheduleNotification(drugID);
                break;

            case ACTION_SCHEDULE_NOTIFY:
                scheduleNotification(drugID);
                showNotification(intent);
                DrugNotifyReceiver.completeWakefulIntent(intent);
                break;

            case ACTION_NOTIFY:
                showNotification(intent);
                DrugNotifyReceiver.completeWakefulIntent(intent);
                break;

            case ACTION_RESCHEDULE:
                cancelNotifications(drugID);
                scheduleNotification(drugID);
                break;

            default:
                final String msg = "Unrecognized intent action " + intent.getAction();
                throw new IllegalStateException(msg);
        }
    }

    private void postponeNotification(Intent intent, int drugID) {
        int postponeTime = intent.getIntExtra(KEY_POSTPONE_TIME, -1);
        if (postponeTime < 0) {
            throw new IllegalStateException("Postpone time is missing in intent " + intent.toString());
        }
        int currentPostponeCount = intent.getIntExtra(KEY_CURRENT_POSTPONE_COUNT, 0);

        Intent notifyIntent = new Intent(this, DrugNotifyReceiver.class);
        notifyIntent.setAction(ACTION_NOTIFY);
        notifyIntent.putExtra(DBContract.Intake.DRUG_ID, drugID);
        notifyIntent.putExtra(KEY_CURRENT_POSTPONE_COUNT, currentPostponeCount + 1);

        PendingIntent scheduleIntent = PendingIntent.getBroadcast(this, drugID, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar scheduleTime = Calendar.getInstance();
        scheduleTime.add(Calendar.MINUTE, postponeTime);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, scheduleTime.getTimeInMillis(), scheduleIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(drugID);
    }

    private void scheduleNotification(int drugID) {
        Cursor drugCursor = getContentResolver().query(
                DBContract.Drug.CONTENT_URI,
                new String[]{DBContract.Drug.START_DATE, DBContract.Drug.END_DATE},
                BaseColumns._ID + "=?",
                new String[]{Integer.toString(drugID)},
                null);
        Cursor notifyCursor = getContentResolver().query(
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

        Intent notifyIntent = new Intent(this, DrugNotifyReceiver.class);
        notifyIntent.setAction(ACTION_SCHEDULE_NOTIFY);
        notifyIntent.putExtra(DBContract.Intake.DRUG_ID, drugID);

        PendingIntent scheduleIntent = PendingIntent.getBroadcast(this, drugID, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);

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

        String currentTime = currentDate.get(Calendar.HOUR_OF_DAY) + "-" + currentDate.get(Calendar.MINUTE);

        String startDateStr = drugCursor.getString(drugCursor.getColumnIndex(DBContract.Drug.START_DATE));
        Calendar startDate = (Calendar) currentDate.clone();
        setDate(startDateStr, startDate);

        String endDateStr = drugCursor.getString(drugCursor.getColumnIndex(DBContract.Drug.END_DATE));
        Calendar endDate = (Calendar) currentDate.clone();
        setDate(endDateStr, endDate);

        Calendar scheduleDate = getScheduleDate(currentTime, currentDate, startDate, endDate, weekdays, times);

        if (scheduleDate != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, scheduleDate.getTimeInMillis(), scheduleIntent);
        }

        drugCursor.close();
        notifyCursor.close();
    }

    private void showNotification(Intent intent) {
        DrugIntakeNotification notification = new DrugIntakeNotification(this, intent);
        notification.show();
    }

    private static int getDrugID(Intent intent) {
        int drugID = intent.getIntExtra(KEY_DRUG_ID, -1);
        if (drugID < 0) {
            final String msg = "Invalid drugID in intent " + intent.toString();
            throw new IllegalArgumentException(msg);
        }
        return drugID;
    }

    private void cancelNotifications(int drugID) {
        // Cancel scheduled notifications
        Intent notifyIntent = new Intent(this, DrugNotifyReceiver.class);
        notifyIntent.setAction(ACTION_SCHEDULE_NOTIFY);
        PendingIntent cancelIntent = PendingIntent.getBroadcast(this, drugID, notifyIntent, 0);
        alarmManager.cancel(cancelIntent);

        // Cancel postponed notifications
        notifyIntent.setAction(ACTION_NOTIFY);
        cancelIntent = PendingIntent.getBroadcast(this, drugID, notifyIntent, 0);
        alarmManager.cancel(cancelIntent);
    }

    private static void setDate(String dateFormatted, Calendar out) {
        String[] dateComponents = dateFormatted.split("-");
        int year = Integer.parseInt(dateComponents[0]);
        int month = Integer.parseInt(dateComponents[1]);
        int day = Integer.parseInt(dateComponents[2]);
        out.set(year, month, day);
    }

    private static int compareTimes(String x, String y) {
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

    private static Calendar getScheduleDate(String currentTime, Calendar currentDate, Calendar startDate, Calendar endDate,
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