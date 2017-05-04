package com.vectortwo.healthkeeper.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.vectortwo.healthkeeper.R;

/**
 * Created by ilya on 27/04/2017.
 */
public class BackendPrefManager {
    private SharedPreferences globalPrefs;
    private SharedPreferences miscPrefs;
    private Context context;

    public BackendPrefManager(Context context) {
        this.context = context;
        globalPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_bglobal), Context.MODE_PRIVATE);
        miscPrefs = context.getSharedPreferences(context.getString(R.string.preference_file_bmisc), Context.MODE_PRIVATE);
    }

    public void setPostponeTime(int postponeTime) {
        String key = context.getString(R.string.preference_global_postpone_time);
        globalPrefs.
                edit().
                putInt(key, postponeTime).
                apply();
    }

    public int getPostponeTime() {
        return globalPrefs.getInt(context.getString(R.string.preference_global_postpone_time),
                Integer.parseInt(context.getString(R.integer.postpone_time)));
    }

    public void setPostponeMaxCount(int postponeMaxCount) {
        String key = context.getString(R.string.preference_global_postpone_max_count);
        globalPrefs.
                edit().
                putInt(key, postponeMaxCount).
                apply();
    }

    public int getPostponeMaxCount() {
        return globalPrefs.getInt(context.getString(R.string.preference_global_postpone_max_count),
                Integer.parseInt(context.getString(R.integer.postpone_max_count)));
    }

    public void setDrugExpiredDelta(int drugExpiredDelta) {
        String key = context.getString(R.string.preference_global_postpone_drug_expired_delta);
        globalPrefs.
                edit().
                putInt(key, drugExpiredDelta).
                apply();
    }

    public int getDrugExpiredDelta() {
        return globalPrefs.getInt(context.getString(R.string.preference_global_postpone_drug_expired_delta),
                Integer.parseInt(context.getString(R.integer.drug_expired_deltatime)));
    }

    public boolean getDrugArchiveStarted() {
        return miscPrefs.getBoolean(context.getString(R.string.preference_misc_drug_archive_started), false);
    }

    public boolean getBirthdayCheckStarted() {
        return miscPrefs.getBoolean(context.getString(R.string.preference_misc_birthday_check_started), false);
    }

    public void setBirthdayCheckStarted(boolean value) {
        String key = context.getString(R.string.preference_misc_birthday_check_started);
        miscPrefs.
                edit().
                putBoolean(key, value).
                apply();
    }

    public void setDrugArchiveStarted(boolean value) {
        String key = context.getString(R.string.preference_misc_drug_archive_started);
        miscPrefs.
                edit().
                putBoolean(key, value).
                apply();
    }
}
