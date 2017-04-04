package com.vectortwo.healthkeeper.services;

import android.app.*;
import android.content.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.StepColumns;

import java.util.Calendar;

/**
 * Created by ilya on 24/03/2017.
 */
public class PedometerService extends Service implements SensorEventListener {

    long sensorData;
    long stepsToday;

    private long daily_offset_cached;
    private long hourly_offset_cached;

    private boolean freshInstall;

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    private SharedPreferences sharedPrefs;

    private PedometerNotification notification;

    private static final String ACTION_BROADCAST_ALARM = "com.vectortwo.healthkeeper.intent.action.PEDOMETER_BROADCAST";

    public static final String ACTION_STOP_PEDOMETER_SERVICE = "com.vectortwo.healthkeeper.intent.action.STOP_PEDOMETER_SERVICE";

    private String getDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return new StringBuilder().
                append(year).append('-').
                append(month).append('-').
                append(day).append('-').
                append(hour).toString();
    }

    private boolean sameDay(String dateA, String dateB) {
        String[] xs = dateA.split("-");
        String[] ys = dateB.split("-");
        return xs[0].equals(ys[0]) && xs[1].equals(ys[1]) && xs[2].equals(ys[2]);
    }

    private boolean sameHour(String dateA, String dateB) {
        String[] xs = dateA.split("-");
        String[] ys = dateB.split("-");
        return xs[0].equals(ys[0]) && xs[1].equals(ys[1]) && xs[2].equals(ys[2]) && xs[3].equals(ys[3]);
    }

    private int getHour(String date) {
       return Integer.parseInt(date.substring(date.lastIndexOf("-") + 1));
    }

    private String getDateWithoutHour(String dateWithHour) {
        return dateWithHour.substring(0, dateWithHour.lastIndexOf("-"));
    }

    private int getOnReceivedHour(String date) {
        int hour = getHour(date);
        return (hour == 0) ? 23 : hour - 1;
    }

    private int getOnKilledHour(String date) {
        int hour = getHour(date);
        return (hour == 0) ? 23 : hour;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String currentDate = getDate();

            StepColumns values = new StepColumns();
            values.putCount((int) (sensorData - hourly_offset_cached));
            values.putDate(getDateWithoutHour(currentDate));
            values.putHour(getOnReceivedHour(currentDate));
            getContentResolver().insert(DBContract.Steps.CONTENT_URI, values.getContentValues());

            // deal with hourly update
            hourly_offset_cached = sensorData;
            sharedPrefs.edit().putLong(getString(R.string.preference_pedometer_hourly_offset), sensorData).apply();

            // deal with daily update
            String lastReceivedDate = sharedPrefs.getString(getString(R.string.preference_pedometer_onreceive_date), currentDate);
            if (!sameDay(currentDate, lastReceivedDate)) {
                daily_offset_cached = sensorData;
                sharedPrefs.edit().putLong(getString(R.string.preference_pedometer_daily_offset), sensorData).apply();
                // update notification
                notification.getBuilder().setContentTitle(String.valueOf(0));
                notification.update();
            }
            sharedPrefs.edit().putString(getString(R.string.preference_pedometer_onreceive_date), currentDate).apply();

            setAlarmNextHour(alarmManager, alarmIntent);
       }
    };

    void setAlarmNextHour(AlarmManager manager, PendingIntent intent) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.add(Calendar.HOUR_OF_DAY, 1);

        manager.cancel(intent);
        manager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Main Thread
    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorData = (long) event.values[0];

        if (freshInstall) {
            daily_offset_cached = sensorData;
            hourly_offset_cached = sensorData;
            sharedPrefs.edit()
                    .putLong(getString(R.string.preference_pedometer_hourly_offset), sensorData)
                    .putLong(getString(R.string.preference_pedometer_daily_offset), sensorData)
                    .apply();
            freshInstall = false;
        }

        stepsToday = sensorData - daily_offset_cached;

        notification.getBuilder().setContentTitle(String.valueOf(stepsToday));
        notification.update();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        alarmManager.cancel(alarmIntent);
        unregisterReceiver(receiver);
        stopForeground(true);

        sharedPrefs.edit()
                .putBoolean(getString(R.string.preference_pedometer_force_stopped), false)
                .putLong(getString(R.string.preference_pedometer_onkilled_sensordata), sensorData)
                .putString(getString(R.string.preference_pedometer_onkilled_date), getDate())
                .putBoolean(getString(R.string.preference_pedometer_was_killed), true)
                .apply();
    }

    @Override
    public void onCreate() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sharedPrefs = getSharedPreferences(getString(R.string.preference_file_pedometer), Context.MODE_PRIVATE);
        notification = new PedometerNotification(this);

        alarmIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_BROADCAST_ALARM), 0);

        registerReceiver(receiver, new IntentFilter(ACTION_BROADCAST_ALARM));
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_STOP_PEDOMETER_SERVICE.equals(intent.getAction())) {
            stopSelf();
            return START_NOT_STICKY;
        }
        String currentDate = getDate();
        long notificationValue = 0;
        boolean forceStopped = sharedPrefs.getBoolean(getString(R.string.preference_pedometer_force_stopped), false);

        freshInstall = sharedPrefs.getBoolean(getString(R.string.preference_pedometer_fresh_install), true);

        sharedPrefs.edit().putBoolean(getString(R.string.preference_pedometer_was_killed), false).apply();

        if (freshInstall) {
            sharedPrefs.edit().putBoolean(getString(R.string.preference_pedometer_fresh_install), false).apply();
        } else if (forceStopped) {
            freshInstall = true;
        } else {
            // restore cache
            sensorData = sharedPrefs.getLong(getString(R.string.preference_pedometer_onkilled_sensordata), 0);
            String onkilledDate = sharedPrefs.getString(getString(R.string.preference_pedometer_onkilled_date), currentDate);

            if (sameDay(onkilledDate, currentDate)) {
                daily_offset_cached = sharedPrefs.getLong(getString(R.string.preference_pedometer_daily_offset), 0);
                notificationValue = sensorData - daily_offset_cached;
            } else {
                daily_offset_cached = sensorData;
                sharedPrefs.edit().putLong(getString(R.string.preference_pedometer_daily_offset), sensorData).apply();
            }

            hourly_offset_cached = sharedPrefs.getLong(getString(R.string.preference_pedometer_hourly_offset), 0);
            if (!sameHour(onkilledDate, currentDate)) {
                StepColumns values = new StepColumns();
                values.putCount((int) (sensorData - hourly_offset_cached));
                values.putDate(getDateWithoutHour(onkilledDate));
                values.putHour(getOnKilledHour(onkilledDate));
                getContentResolver().insert(DBContract.Steps.CONTENT_URI, values.getContentValues());

                hourly_offset_cached = sensorData;
                sharedPrefs.edit().putLong(getString(R.string.preference_pedometer_hourly_offset), sensorData).apply();
            }
        }
        sharedPrefs.edit().putString(getString(R.string.preference_pedometer_onreceive_date), currentDate).apply();

        notification.getBuilder().setContentTitle(String.valueOf(notificationValue));
        startForeground(PedometerNotification.PEDOMETER_NOTIFICATION_ID, notification.getBuilder().build());

        setAlarmNextHour(alarmManager, alarmIntent);

        sharedPrefs.edit().putBoolean(getString(R.string.preference_pedometer_force_stopped), true);

        return START_NOT_STICKY;
    }
}