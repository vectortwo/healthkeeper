package com.vectortwo.healthkeeper.activities.medicament.edit;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;
import com.vectortwo.healthkeeper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by skaper on 05.04.17.
 * Setup
 * Adapter for setting the time of reception (alerts), dosage, type of dosage.
 */

public class MTimeReceptionAdapter {
    private ArrayList<Map<String, String>> data;
    private SimpleAdapter timesListAdapter;
    private Context Incontext;
    private int layout;
    public static final String EMPLY = "";
    
    public static final String KEY_TAKE   = "TAKE";
    public static final String KEY_TIME   = "TIME";
    public static final String KEY_DOSAGE = "DOSAGE";
    public static final String VALUE_NEWTIME = "New time";
    public String WORD_TAKE = "";


    public MTimeReceptionAdapter(Context context, int layout, int initialCapacity){
        this.Incontext = context;
        this.data= new ArrayList<Map<String, String>>(initialCapacity);
        this.layout = layout;
        WORD_TAKE = Incontext.getResources().getString(R.string.mtc_text_headline);

    }

    /**
     * New time for medicamet in SimpleAdapter
     * @param value - Dosage
     * @param dosageType - Dosage type
     * @param time - Dosing time
     * @return formed SimpleAdapter for ListView
     */
    public SimpleAdapter setAdapter(String value, String dosageType, String time){
        Map<String, String> m;
        String mystring = Incontext.getResources().getString(R.string.mtc_text_headline);
        value = mystring + " " + value;
        m = new HashMap<String, String>();
        m.put(KEY_TAKE, value);
        m.put(KEY_TIME, time);
        m.put(KEY_DOSAGE, dosageType);
        data.add(m);
        String[] from = { KEY_TAKE, KEY_DOSAGE, KEY_TIME};
        int[] to = { R.id.mtc_headline, R.id.mtc_type_dosage, R.id.mtc_time};
        timesListAdapter = new SimpleAdapter(Incontext, data, layout, from, to);

        return  timesListAdapter;
    }

    /**
     * Create button for New Time
     */
    public void newTimeButton(){
        Map<String, String> m;

        m = new HashMap<String, String>();
        m.put(KEY_TAKE, VALUE_NEWTIME);
        m.put(KEY_DOSAGE, EMPLY);
        m.put(KEY_TIME, EMPLY);
        data.add(m);
        String[] from = { KEY_TAKE, KEY_DOSAGE, KEY_TIME};
        int[] to = { R.id.mtc_headline, R.id.mtc_type_dosage, R.id.mtc_time};
        timesListAdapter = new SimpleAdapter(Incontext, data, layout, from, to);


    }

    /**
     * Set data in SimpleAdapter
     * @param value - Dosage
     * @param dosageType - Dosage type
     * @param time - Dosing time
     */
    public void addTime(String value, String dosageType, String time){
        Map<String, String> m;
        String mystring = Incontext.getResources().getString(R.string.mtc_text_headline);
        value = mystring + " " + value;
        m = new HashMap<String, String>();
        m.put(KEY_TAKE, value);
        m.put(KEY_TIME, time);
        m.put(KEY_DOSAGE, dosageType);
        data.add(m);
        String[] from = { KEY_TAKE, KEY_DOSAGE, KEY_TIME};
        int[] to = { R.id.mtc_headline, R.id.mtc_type_dosage, R.id.mtc_time};
        timesListAdapter = new SimpleAdapter(Incontext, data, layout, from, to);
    }
    /**
     * @return SimpleAdapter for ListView
     * if you not before call method newTimeButton, addTime or setAdapter @return null SimpleAdapter
     */
    public SimpleAdapter getAdapter(){

        return  timesListAdapter;
    }

    /**
     * Check adapter emply
     * @return true or false
     */
    public boolean checkAdapterEmply(){

        if (timesListAdapter==null){
            return true;
        }else {
            return false;
        }
    }

    /**
     * @param position - Position element in ListView
     * @param value - Dosage
     * @param dosageType - Dosage type
     * @param time - Dosing time
     */
    public void changeData(int position, String value, String dosageType, String time){
        String mystring = Incontext.getResources().getString(R.string.mtc_text_headline);
        value = mystring + " " + value;
        Map<String, String> m;
        m = new HashMap<String, String>();
        m.put(KEY_TAKE, value);
        m.put(KEY_DOSAGE, dosageType);
        m.put(KEY_TIME, time);
        data.set(position, m);
    }

    /**
     * @return Returns all text data from the adapter in ArrayList<Map<String, String>>
     */
    public ArrayList<Map<String, String>> getData(){
        return data;
    }

    /**
     * Clear all data in SimpleAdapter
     */
    public void clear(){
        data.clear();
        timesListAdapter.notifyDataSetChanged();
    }

    /**
     * Remove a specific item from the SimpleAdapter
     * @param position - Position element in ListView
     */
    public void removeTime(int position){
        data.remove(position);
        timesListAdapter.notifyDataSetChanged();
    }

    /**
     * Update data in SimpleAdapter
     */
    public void update(){
        timesListAdapter.notifyDataSetChanged();
    }
}