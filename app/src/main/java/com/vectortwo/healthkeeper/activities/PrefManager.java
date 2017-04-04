package com.vectortwo.healthkeeper.activities;

/**
 * Created by skaper
 * Класс для свайпа приветсвия
 */
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String PEDOMETR_IS_ON = "PedometrIsOn";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){ return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setPedometrIsOn(boolean pedometr_is_on){
        editor.putBoolean(PEDOMETR_IS_ON, pedometr_is_on);
        editor.commit();
    }

    public  boolean isPedometrOn(){ return pref.getBoolean(PEDOMETR_IS_ON, true); }
}
