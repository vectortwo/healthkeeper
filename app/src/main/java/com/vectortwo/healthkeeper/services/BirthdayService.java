package com.vectortwo.healthkeeper.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.UserColumns;

import java.util.Calendar;

/**
 *  Increases the user's age on his birthday.
 *  Runs every year (inexact)
 */

// TODO: start after user info has been added to db
public class BirthdayService extends IntentService {

    private static final String ACTION_AGE_INC = "com.vectortwo.healthkeeper.intent.ACTION_AGE_INCREMENT";

    public BirthdayService() {
        super("BirthdayService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Cursor userCursor = getContentResolver().query(
                DBContract.User.CONTENT_URI,
                new String[] {DBContract.User._ID, DBContract.User.AGE, DBContract.User.BIRTHDAY},
                null,
                null,
                null);
        userCursor.moveToFirst();

        if (ACTION_AGE_INC.equals(intent.getAction())) {
            int _id = userCursor.getInt(userCursor.getColumnIndex(DBContract.User._ID));
            int age = userCursor.getInt(userCursor.getColumnIndex(DBContract.User.AGE));

            UserColumns values = new UserColumns();
            values.putAge(age + 1);

            getContentResolver().update(
                    DBContract.User.CONTENT_URI,
                    values.getContentValues(),
                    DBContract.User._ID + "=" + _id,
                    null);
        }

        String birthDayStr = userCursor.getString(userCursor.getColumnIndex(DBContract.User.BIRTHDAY));
        String[] birthdayComps = birthDayStr.split("-");
        int year = Integer.parseInt(birthdayComps[0]);
        int month = Integer.parseInt(birthdayComps[1]);
        int day = Integer.parseInt(birthdayComps[2]);

        Calendar currentTime = Calendar.getInstance();
        Calendar birthdayTime = (Calendar) currentTime.clone();
        birthdayTime.set(year, month, day);

        int currentYear = currentTime.get(Calendar.YEAR);
        birthdayTime.add(Calendar.YEAR, currentYear - year + 1);

        birthdayTime.set(Calendar.HOUR_OF_DAY, 0);
        birthdayTime.set(Calendar.MINUTE, 0);

        setAlarmOn(birthdayTime);

        userCursor.close();
    }

    private void setAlarmOn(Calendar cal) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, BirthdayService.class);
        i.setAction(BirthdayService.ACTION_AGE_INC);

        alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis(), PendingIntent.getService(this, 0, i, 0));
    }
}
