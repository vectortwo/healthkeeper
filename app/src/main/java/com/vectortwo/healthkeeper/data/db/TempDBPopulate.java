package com.vectortwo.healthkeeper.data.db;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ilya on 30/04/2017.
 */
// todo: remove on release
public final class TempDBPopulate {
    private TempDBPopulate() {}

    private static final String[] DATE_WITH_HM = new String[] {
            "2016-11-30-23-59",
            "2016-11-31-1-1",
            "2017-0-9-7-1",
            "2017-3-10-5-0",
            "2017-5-3-0-0",
            "2017-5-3-0-1",
            "2017-5-15-15-40",
            "2018-4-1-10-14",
    };

    private static final int COUNT = 5;

    public static void fill(final Context cntx) {

        (new Thread() {
            @Override
            public void run() {
                // bloodpressure
                for (int i = 0; i < DATE_WITH_HM.length; i++) {
                    BloodPressureColumns bp = new BloodPressureColumns();
                    bp.putSystolic(i + 10).putDiastolic(i + 4).putDate(DATE_WITH_HM[i]);
                    cntx.getContentResolver().insert(DBContract.BloodPressure.CONTENT_URI, bp.getContentValues());
                }

                // user
                UserColumns user = new UserColumns();
                user.putAge(12).putBirthday("2017-5-10").putCity("Cityname").putCountry("countryname").putFirstName("firstname").putSecondName("secondname")
                        .putSex("male").putHeight(181).putWeight(85);

                cntx.getContentResolver().insert(DBContract.User.CONTENT_URI, user.getContentValues());

                StepColumns steps = new StepColumns();
                steps.putCount(93).putHour(14).putDate("2017-1-2");
                cntx.getContentResolver().insert(DBContract.Steps.CONTENT_URI, steps.getContentValues());

                steps = new StepColumns();
                steps.putWakingTime(12).putCount(211).putHour(3).putDate("2017-4-1");
                cntx.getContentResolver().insert(DBContract.Steps.CONTENT_URI, steps.getContentValues());

                steps = new StepColumns();
                steps.putWakingTime(30).putCount(12).putHour(4).putDate("2017-4-5");
                cntx.getContentResolver().insert(DBContract.Steps.CONTENT_URI, steps.getContentValues());

                steps = new StepColumns();
                steps.putWakingTime(1).putCount(100).putHour(6).putDate("2017-4-5");
                cntx.getContentResolver().insert(DBContract.Steps.CONTENT_URI, steps.getContentValues());

                steps = new StepColumns();
                steps.putCount(31).putHour(9).putDate("2017-4-5");
                cntx.getContentResolver().insert(DBContract.Steps.CONTENT_URI, steps.getContentValues());
            }
        }).start();
    }
}
