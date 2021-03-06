package com.vectortwo.healthkeeper.services;

import android.app.*;
import android.content.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.vectortwo.healthkeeper.PedometerDateFormat;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.StepColumns;
import com.vectortwo.healthkeeper.notifications.PedometerNotification;

import java.util.Calendar;

/**
 * Pedometer implementation as a foreground service.
 */
public class PedometerService extends Service implements SensorEventListener {

    public static final String ACTION_STOP = "com.vectortwo.healthkeeper.intent.PEDOMETER_STOP";

    private long sensorData;

    private long daily_offset_cached;
    private long hourly_offset_cached;

    private int stepsToday;
    private int stepsLastHour;

    private long walkingTimeToday;
    private long prevTimestamp;
    private static final long WALKING_DELTA = 1500000000;
    private static final double NANO_TO_MINUTE = 0.000000001 / 60;

    private boolean freshInstall;

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    private SharedPreferences pedometerPrefs;

    private PedometerNotification notification;

    private final IBinder binder = new PedometerBinder();

    private static final String ACTION_UPDATE = "com.vectortwo.healthkeeper.broadcast.PEDOMETER_UPDATE";

    private PowerManager.WakeLock wl;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final PendingResult result = goAsync();

            (new Thread() {
                @Override
                public void run() {
                    String currentDate = PedometerDateFormat.getCurrentDate();

                    StepColumns values = new StepColumns();
                    values.putCount(stepsLastHour);
                    values.putDate(PedometerDateFormat.getDateWithoutHour(currentDate));
                    values.putHour(PedometerDateFormat.getOnReceivedHour(currentDate));
                    Uri uri = getContentResolver().insert(DBContract.Steps.CONTENT_URI, values.getContentValues());

                    // deal with hourly update
                    stepsLastHour = 0;
                    hourly_offset_cached = sensorData;
                    pedometerPrefs.edit().putLong(getString(R.string.preference_pedometer_hourly_offset), sensorData).apply();

                    // deal with daily update
                    String lastReceivedDate = pedometerPrefs.getString(getString(R.string.preference_pedometer_onreceive_date), currentDate);
                    if (!PedometerDateFormat.sameDay(currentDate, lastReceivedDate)) {
                        StepColumns walkingTimeUpdate = new StepColumns();
                        walkingTimeUpdate.putWakingTime(getWalkingTimeToday());

                        int id = Integer.parseInt(uri.getLastPathSegment());
                        getContentResolver().update(uri, walkingTimeUpdate.getContentValues(), DBContract.Steps._ID + "=" + id, null);

                        walkingTimeToday = 0;
                        stepsToday = 0;
                        daily_offset_cached = sensorData;

                        pedometerPrefs.edit().putLong(getString(R.string.preference_pedometer_daily_offset), sensorData).apply();

                        // show notification
                        notification.getBuilder().setContentTitle(String.valueOf(0));
                        notification.update();
                    }
                    pedometerPrefs.edit().putString(getString(R.string.preference_pedometer_onreceive_date), currentDate).apply();

                    setAlarmNextHour(alarmManager, alarmIntent);

                    result.finish();
                }
            }).start();
        }
    };

    public class PedometerBinder extends Binder {
        public PedometerService getService() {
            return PedometerService.this;
        }
    }

    public long getStepsToday() {
        return stepsToday;
    }

    public long getStepsLastHour() {
        return stepsLastHour;
    }

    /**
     * How much time the user has been walking today
     * @return time in minutes
     */
    public int getWalkingTimeToday() {
        return (int) (walkingTimeToday * NANO_TO_MINUTE);
    }

    /**
     * Helper function to check for a presence of step sensor on a device
     *
     * @return true if present, false otherwise
     */
    public static boolean hasSensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        return (stepSensor != null);
    }

    // Main Thread
    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorData = (long) event.values[0];

        if (freshInstall || sensorData - daily_offset_cached < 0) {
            daily_offset_cached = sensorData;
            hourly_offset_cached = sensorData;
            pedometerPrefs.edit()
                    .putLong(getString(R.string.preference_pedometer_hourly_offset), sensorData)
                    .putLong(getString(R.string.preference_pedometer_daily_offset), sensorData)
                    .apply();
            freshInstall = false;
        }
        stepsLastHour = (int) (sensorData - hourly_offset_cached);
        stepsToday = (int) (sensorData - daily_offset_cached);

        int currentDelta = (int) (event.timestamp - prevTimestamp);
        prevTimestamp = event.timestamp;

        if (currentDelta < WALKING_DELTA) {
            walkingTimeToday += currentDelta;
        }

        notification.getBuilder().setContentTitle(String.valueOf(stepsToday));
        notification.update();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        alarmManager.cancel(alarmIntent);
        unregisterReceiver(receiver);
        stopForeground(true);

        pedometerPrefs.edit()
                .putBoolean(getString(R.string.preference_pedometer_force_stopped), false)
                .putLong(getString(R.string.preference_pedometer_onkilled_sensordata), sensorData)
                .putString(getString(R.string.preference_pedometer_onkilled_date), PedometerDateFormat.getCurrentDate())
                .putBoolean(getString(R.string.preference_pedometer_was_killed), true)
                .putLong(getString(R.string.preference_pedometer_onkilled_walkingtime), walkingTimeToday)
                .apply();

        wl.release();
    }

    @Override
    public void onCreate() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        pedometerPrefs = getSharedPreferences(getString(R.string.preference_file_pedometer), Context.MODE_PRIVATE);
        notification = new PedometerNotification(this);

        alarmIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_UPDATE), 0);

        registerReceiver(receiver, new IntentFilter(ACTION_UPDATE));
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (stepSensor == null) {
            stopSelf();
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_STOP.equals(intent.getAction())) {
            stopSelf();
            return START_NOT_STICKY;
        }
        String currentDate = PedometerDateFormat.getCurrentDate();
        long notificationValue = 0;
        boolean forceStopped = pedometerPrefs.getBoolean(getString(R.string.preference_pedometer_force_stopped), false);

        freshInstall = pedometerPrefs.getBoolean(getString(R.string.preference_pedometer_fresh_install), true);

        pedometerPrefs.edit().putBoolean(getString(R.string.preference_pedometer_was_killed), false).apply();

        if (freshInstall) {
            pedometerPrefs.edit().putBoolean(getString(R.string.preference_pedometer_fresh_install), false).apply();
        } else if (forceStopped) {
            freshInstall = true;
        } else {
            // restore cache
            sensorData = pedometerPrefs.getLong(getString(R.string.preference_pedometer_onkilled_sensordata), 0);
            String onkilledDate = pedometerPrefs.getString(getString(R.string.preference_pedometer_onkilled_date), currentDate);

            if (PedometerDateFormat.sameDay(onkilledDate, currentDate)) {
                walkingTimeToday = pedometerPrefs.getLong(getString(R.string.preference_pedometer_onkilled_walkingtime), 0);

                daily_offset_cached = pedometerPrefs.getLong(getString(R.string.preference_pedometer_daily_offset), 0);
                notificationValue = sensorData - daily_offset_cached;
            } else {
                daily_offset_cached = sensorData;
                pedometerPrefs.edit().putLong(getString(R.string.preference_pedometer_daily_offset), sensorData).apply();
            }

            hourly_offset_cached = pedometerPrefs.getLong(getString(R.string.preference_pedometer_hourly_offset), 0);
            if (!PedometerDateFormat.sameHour(onkilledDate, currentDate)) {
                StepColumns values = new StepColumns();
                values.putCount((int) (sensorData - hourly_offset_cached));
                values.putDate(PedometerDateFormat.getDateWithoutHour(onkilledDate));
                values.putHour(PedometerDateFormat.getOnKilledHour(onkilledDate));
                if (!PedometerDateFormat.sameDay(onkilledDate, currentDate)) {
                    long walkingTimeOnkilled = pedometerPrefs.getLong(getString(R.string.preference_pedometer_onkilled_walkingtime), 0);
                    values.putWakingTime((int) (walkingTimeOnkilled * NANO_TO_MINUTE));
                }
                getContentResolver().insert(DBContract.Steps.CONTENT_URI, values.getContentValues());

                hourly_offset_cached = sensorData;
                pedometerPrefs.edit().putLong(getString(R.string.preference_pedometer_hourly_offset), sensorData).apply();
            }
        }
        pedometerPrefs.edit().putString(getString(R.string.preference_pedometer_onreceive_date), currentDate).apply();

        notification.getBuilder().setContentTitle(String.valueOf(notificationValue));
        startForeground(notification.getNotificationID(), notification.getBuilder().build());

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "vectortwo");
        wl.acquire();

        setAlarmNextHour(alarmManager, alarmIntent);

        pedometerPrefs.edit().putBoolean(getString(R.string.preference_pedometer_force_stopped), true).apply();

        stepsToday = (int) (sensorData - daily_offset_cached);
        stepsLastHour = (int) (sensorData - hourly_offset_cached);

        return START_STICKY;
    }

    private void setAlarmNextHour(AlarmManager manager, PendingIntent intent) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.add(Calendar.HOUR_OF_DAY, 1);

        manager.cancel(intent);
        manager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), intent);
    }
}