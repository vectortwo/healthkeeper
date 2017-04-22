package com.vectortwo.healthkeeper.services;

import android.app.*;
import android.content.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
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

    private long stepsToday;
    private long stepsLastHour;

    private long daily_offset_cached;
    private long hourly_offset_cached;

    private boolean freshInstall;

    private SensorManager sensorManager;

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    private SharedPreferences sharedPrefs;

    private PedometerNotification notification;

    private final IBinder binder = new PedometerBinder();

    private static final String ACTION_UPDATE = "com.vectortwo.healthkeeper.broadcast.PEDOMETER_UPDATE";

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String currentDate = PedometerDateFormat.getCurrentDate();

            StepColumns values = new StepColumns();
            values.putCount((int) stepsLastHour);
            values.putDate(PedometerDateFormat.getDateWithoutHour(currentDate));
            values.putHour(PedometerDateFormat.getOnReceivedHour(currentDate));
            getContentResolver().insert(DBContract.Steps.CONTENT_URI, values.getContentValues());

            // deal with hourly show
            stepsLastHour = 0;
            hourly_offset_cached = sensorData;
            sharedPrefs.edit().putLong(getString(R.string.preference_pedometer_hourly_offset), sensorData).apply();

            // deal with daily show
            String lastReceivedDate = sharedPrefs.getString(getString(R.string.preference_pedometer_onreceive_date), currentDate);
            if (!PedometerDateFormat.sameDay(currentDate, lastReceivedDate)) {
                stepsToday = 0;
                daily_offset_cached = sensorData;

                sharedPrefs.edit().putLong(getString(R.string.preference_pedometer_daily_offset), sensorData).apply();
                // show notification
                notification.getBuilder().setContentTitle(String.valueOf(0));
                notification.update();
            }
            sharedPrefs.edit().putString(getString(R.string.preference_pedometer_onreceive_date), currentDate).apply();

            setAlarmNextHour(alarmManager, alarmIntent);
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

        stepsLastHour = sensorData - hourly_offset_cached;
        stepsToday = sensorData - daily_offset_cached;

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

        sharedPrefs.edit()
                .putBoolean(getString(R.string.preference_pedometer_force_stopped), false)
                .putLong(getString(R.string.preference_pedometer_onkilled_sensordata), sensorData)
                .putString(getString(R.string.preference_pedometer_onkilled_date), PedometerDateFormat.getCurrentDate())
                .putBoolean(getString(R.string.preference_pedometer_was_killed), true)
                .apply();
    }

    @Override
    public void onCreate() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        sharedPrefs = getSharedPreferences(getString(R.string.preference_file_pedometer), Context.MODE_PRIVATE);

        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null) {
            notification = new PedometerNotification(this);

            alarmIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_UPDATE), 0);

            registerReceiver(receiver, new IntentFilter(ACTION_UPDATE));
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            stopSelf();
        }
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_STOP.equals(intent.getAction())) {
            stopSelf();
            return START_NOT_STICKY;
        }
        String currentDate = PedometerDateFormat.getCurrentDate();
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

            if (PedometerDateFormat.sameDay(onkilledDate, currentDate)) {
                daily_offset_cached = sharedPrefs.getLong(getString(R.string.preference_pedometer_daily_offset), 0);
                notificationValue = sensorData - daily_offset_cached;
            } else {
                daily_offset_cached = sensorData;
                sharedPrefs.edit().putLong(getString(R.string.preference_pedometer_daily_offset), sensorData).apply();
            }

            hourly_offset_cached = sharedPrefs.getLong(getString(R.string.preference_pedometer_hourly_offset), 0);
            if (!PedometerDateFormat.sameHour(onkilledDate, currentDate)) {
                StepColumns values = new StepColumns();
                values.putCount((int) (sensorData - hourly_offset_cached));
                values.putDate(PedometerDateFormat.getDateWithoutHour(onkilledDate));
                values.putHour(PedometerDateFormat.getOnKilledHour(onkilledDate));
                getContentResolver().insert(DBContract.Steps.CONTENT_URI, values.getContentValues());

                hourly_offset_cached = sensorData;
                sharedPrefs.edit().putLong(getString(R.string.preference_pedometer_hourly_offset), sensorData).apply();
            }
        }
        sharedPrefs.edit().putString(getString(R.string.preference_pedometer_onreceive_date), currentDate).apply();

        notification.getBuilder().setContentTitle(String.valueOf(notificationValue));
        startForeground(notification.getNotificationID(), notification.getBuilder().build());

        setAlarmNextHour(alarmManager, alarmIntent);

        sharedPrefs.edit().putBoolean(getString(R.string.preference_pedometer_force_stopped), true).apply();

        stepsToday = sensorData - daily_offset_cached;
        stepsLastHour = sensorData - hourly_offset_cached;

        return START_NOT_STICKY;
    }

    private void setAlarmNextHour(AlarmManager manager, PendingIntent intent) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.add(Calendar.HOUR_OF_DAY, 1);

        manager.cancel(intent);
        manager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), intent);
    }
}