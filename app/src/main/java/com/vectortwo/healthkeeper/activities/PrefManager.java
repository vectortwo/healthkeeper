package com.vectortwo.healthkeeper.activities;
import android.content.Context;
import android.content.SharedPreferences;
import com.vectortwo.healthkeeper.R;

public class PrefManager {
    private SharedPreferences pref;
    private Context contextThis;

    public PrefManager(Context context) {
        this.contextThis = context;
        pref = contextThis.getSharedPreferences(contextThis.getString(R.string.prefs_file_global), Context.MODE_PRIVATE);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        pref.edit().putBoolean(contextThis.getString(R.string.prefs_first_launch), isFirstTime).apply();
    }

    public boolean isFirstTimeLaunch(){ return pref.getBoolean(
            contextThis.getString(R.string.prefs_first_launch), true);
    }

    public void setPedometrIsOn(boolean pedometr_is_on){
        pref.edit().putBoolean(
                contextThis.getString(R.string.preference_pedometer_is_on), pedometr_is_on).apply();
    }

    public  boolean isPedometrOn(){
        return pref.getBoolean(
            contextThis.getString(R.string.preference_pedometer_is_on), true);
    }

    public void setWalkingTimeTarget(int timeMinute){
        pref.edit().putInt(contextThis.getString(R.string.prefs_walking_target), timeMinute);
    }

    public int getWalkingTimeTarget(){
        return pref.getInt(contextThis.getString(R.string.prefs_walking_target),
                           contextThis.getResources().getInteger(R.integer.walked_time_target_default));
    }
    //Managing elements

    public void setMedicametVisible(boolean status){
        pref.edit().putBoolean(contextThis.getString(R.string.prefs_element_medicament_status), status).apply();
    }
    public void setBloodSugarVisible(boolean status){
        pref.edit().putBoolean(contextThis.getString(R.string.prefs_element_blood_sugar_status), status).apply();
    }
    public void setWalkingVisible(boolean status){
        pref.edit().putBoolean(contextThis.getString(R.string.prefs_element_walking_status), status).apply();
    }
    public void setPressureVisible(boolean status){
        pref.edit().putBoolean(contextThis.getString(R.string.prefs_element_pressure_status), status).apply();
    }
    public void setAquaVisible(boolean status){
        pref.edit().putBoolean(contextThis.getString(R.string.prefs_element_aqua_status), status).apply();
    }
    public void setWeightVisible(boolean status){
        pref.edit().putBoolean(contextThis.getString(R.string.prefs_element_weight_status), status).apply();
    }
    public void setSleepVisible(boolean status){
        pref.edit().putBoolean(contextThis.getString(R.string.prefs_element_sleep_status), status).apply();
    }


    public boolean getMedicametVisible(){
        return pref.getBoolean(contextThis.getString(R.string.prefs_element_medicament_status), true);
    }
    public boolean getBloodSugarVisible(){
        return pref.getBoolean(contextThis.getString(R.string.prefs_element_blood_sugar_status), true);
    }
    public boolean getWalkingVisible(){
        return pref.getBoolean(contextThis.getString(R.string.prefs_element_walking_status), true);
    }

    public boolean getPressureVisible(){
        return pref.getBoolean(contextThis.getString(R.string.prefs_element_pressure_status), true);
    }
    public boolean getAquaVisible(){
        return pref.getBoolean(contextThis.getString(R.string.prefs_element_aqua_status), true);
    }
    public boolean getWeightVisible(){
        return pref.getBoolean(contextThis.getString(R.string.prefs_element_weight_status), true);
    }
    public boolean getSleepVisible(){
        return pref.getBoolean(contextThis.getString(R.string.prefs_element_sleep_status), true);
    }




}
