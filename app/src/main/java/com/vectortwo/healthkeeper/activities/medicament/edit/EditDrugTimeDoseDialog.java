package com.vectortwo.healthkeeper.activities.medicament.edit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.vectortwo.healthkeeper.R;

import java.util.Calendar;

/**
 * Created by skaper on 18.04.17.
 */
public class EditDrugTimeDoseDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    public Dialog d;
    private  ImageView plus, minus;
    private TimePicker timePicker;
    public TextView cancel, ok, delete;
    private Spinner dose_type;
    private double value = 1.0;
    private boolean is24HourFormat;
    private String status = "";
    private String dose_text = "";
    private EditText valueView;
    private MyDialogListener listener;
    private static final double MIN_VALUE = 0.25;
    public EditDrugTimeDoseDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }
    public static interface MyDialogListener
    {
        public void userCanceled();
        public void userClickOk(double value, int hour, int minutes, String dose_type, boolean is24HourFormat, String status);
        public void deleteItem();
        public void end();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.m_dialog_time_dose);

        Spinner spinner = (Spinner) findViewById(R.id.mdtd_dose_typelist);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.dosage_types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                dose_text = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cancel = (TextView) findViewById(R.id.mdtd_cancel);
        ok = (TextView) findViewById(R.id.mdtd_ok);
        delete = (TextView)findViewById(R.id.mdtd_delete);
        valueView = (EditText) findViewById(R.id.mdtd_value);
        dose_type = (Spinner)  findViewById(R.id.mdtd_dose_typelist);
        plus = (ImageView) findViewById(R.id.mdtd_plus);
        minus = (ImageView) findViewById(R.id.mdtd_minus);
        timePicker = (TimePicker)findViewById(R.id.mdtd_time);
        /** Check system time format (12 or 24 hour) */

        is24HourFormat = DateFormat.is24HourFormat(context);

        timePicker.setIs24HourView(is24HourFormat);
        timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        delete.setOnClickListener(this);
        valueView.setMaxEms(1000);
        valueView.setMinEms(0);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Limitation of the maximum dosage*/
                if (value<1000.0) {
                    value = Double.parseDouble(valueView.getText().toString()) + MIN_VALUE;
                    valueView.setText(String.valueOf(value));
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Limitation of the minimum dosage*/
                if(value>0) {
                    value = Double.parseDouble(valueView.getText().toString()) - MIN_VALUE;
                    valueView.setText(String.valueOf(value));
                }
            }
        });
    }

    /**
     * Listener for event in Time dialog
     * @param listener
     */
    public void setMyDialogListener(MyDialogListener listener){

        this.listener = listener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            listener.userCanceled();
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * Tracking button clicks
     * @param view - View
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mdtd_ok:
                value = Double.parseDouble(valueView.getText().toString());
                if(value<0){
                    value = 0.25;
                }else if(value>1000){
                    value = 1000;
                }
                int minute = timePicker.getCurrentMinute();
                int hour = timePicker.getCurrentHour();
                /**
                 * Check time format
                 * {@status} - PM or AM
                 * @see userClickOk(double value, int hour, int minutes, String dose_type, boolean is24HourFormat, String status);
                 */
                if(!is24HourFormat) {
                    status = "AM";
                    if (hour > 11) {
                        status = "PM";
                    }
                    if (hour > 11) {
                        hour = hour - 12;
                    }
                }else {
                    status="";
                }
                /**
                 * @return Data time,volume and type of dosage for create еlement view card
                 */
                listener.userClickOk(value, hour, minute, dose_text, is24HourFormat, status);
                break;
            case R.id.mdtd_cancel:
                listener.userCanceled();
                break;

            case R.id.mdtd_delete:
                listener.deleteItem();
                dismiss();
                break;
            default:
                break;
        }
        listener.end();
        dismiss();
    }
}