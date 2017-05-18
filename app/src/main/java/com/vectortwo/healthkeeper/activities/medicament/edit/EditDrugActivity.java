package com.vectortwo.healthkeeper.activities.medicament.edit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.KeyListener;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.medicament.MedicamentActivity;
import com.vectortwo.healthkeeper.animations.CreateAnimation;
import com.vectortwo.healthkeeper.data.BaseInfo;
import com.vectortwo.healthkeeper.data.DrugInfo;
import com.vectortwo.healthkeeper.data.TaskHandler;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DrugColumns;
import com.vectortwo.healthkeeper.data.db.IntakeColumns;
import com.vectortwo.healthkeeper.services.DrugNotifyService;
import com.vectortwo.healthkeeper.utilities.KeyboardUtil;

import javax.xml.datatype.Duration;
import java.lang.reflect.Field;
import java.net.URI;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EditDrugActivity extends AppCompatActivity {
    //TODO Растгивается
    //TODO Появление клавы по клику
    //TODO сделать подсказки
    //TODO Поправить клаву при нажатии на тектс
    //TODO В warning не работает выделение слов после редактирования
    //TODO диалог выхода
    private CreateAnimation createAnimation;
    private Toolbar toolbar;
    private TextView startData, endData;
    private LinearLayout startDateLayout, endDateLayout,
            descriptionBTNlayout, warningsBTNlayout;
    private Switch notificationSwitch;
    private boolean notificationSwitchStatus = false;
    private EditText description, warnings;
    private ListView timesList;
    private ListView.OnItemClickListener listenerListView;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private boolean isClickdescription, isLongClkdescription, isFirstClickdescription;
    private boolean isClickwarnings, isLongClkwarnings, isFirstClickwarnings;
    private String drugName = "";
    private long drugID = -1;
    private Button editDrug_ok,
            description_fabOK, description_fabNO,
            warnings_fabOK, warnings_fabNO;
    private String warningsText, descriptionText;
    private float dosageValue;
    private boolean[] dayClicked;
    private ScrollView scrollView;
    private MTimeReceptionAdapter mTimeReceptionAdapter;
    private Context contextThis;
    private KeyboardUtil keyboardUtil;
    private static final long ANIM_TIME = 500;//500
    private static final int ANIM_DAY_BUTTON = 200;
    private static final int DP_TEXTVIEW_HEIGHT = 60;

    private Calendar calendarEndDate, calendarStartDate;
    private boolean timeListItemCklicke = false;
    private Button btn_sun, btn_mon, btn_tue, btn_wed, btn_thu, btn_fri, btn_sat;

    private SelectionImage selectionImage;


    private int countTimesInDay = 0;
    private int countTimesInDayMax = 10;

    //Data to DB
    private int toDBendDay, toDBendMonth, toDBendYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_drug); //activity_edit_drug_test
        Intent intent = getIntent();
        String action = intent.getAction();
        contextThis = this;
        createAnimation = new CreateAnimation(contextThis);

        //Sturting Settings
        /**Resize window when soft keyboard up */
        keyboardUtil = new KeyboardUtil(this, findViewById(android.R.id.content));
        keyboardUtil.enable();
        /** The soft keyboard is not called until it is needed */



        mTimeReceptionAdapter = new MTimeReceptionAdapter(
                contextThis, R.layout.m_time_card,1);

        toolbar = (Toolbar) findViewById(R.id.editDrug_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_clear);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO do something
                onBackPressed();
            }
        });
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.editDrug_CollapsingToolBar);
        timesList =          (ListView)findViewById(R.id.editDrug_timeList);
        description_fabOK =  (Button) findViewById(R.id.editDrug_description_ok);
        description_fabNO =  (Button) findViewById(R.id.editDrug_description_no);
        warnings_fabOK =     (Button) findViewById(R.id.editDrug_warnings_ok);
        warnings_fabNO =     (Button) findViewById(R.id.editDrug_warnings_no);
        editDrug_ok =        (Button) findViewById(R.id.editDrug_ok);
        description =        (EditText)findViewById(R.id.editDrug_description);
        warnings =           (EditText)findViewById(R.id.editDrug_warnings);
        startData =          (TextView)findViewById(R.id.editDrug_startdate);
        startDateLayout =    (LinearLayout)findViewById(R.id.editDrug_startdate_layout);
        endDateLayout =      (LinearLayout)findViewById(R.id.editDrug_enddate_layout);
        endData =            (TextView)findViewById(R.id.editDrug_enddate);
        descriptionBTNlayout=(LinearLayout)findViewById(R.id.editDrug_descriptionBTN_layoyt);
        warningsBTNlayout=   (LinearLayout)findViewById(R.id.editDrug_warningsBTN_layoyt);
        notificationSwitch=  (Switch)findViewById(R.id.editDrug_switch1);





        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notificationSwitchStatus = isChecked;
            }
        });

        dayOfWeekButtons(contextThis);//Create buttons
        keyboardUtil.hideSoftKeyboard(EditDrugActivity.this, description);
        //Start data
        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartData();
                //TODO сделать пересчет
            }
        });
        Calendar mcurrentTime = Calendar.getInstance();
        int toDBstartYear = mcurrentTime.get(Calendar.YEAR);
        int toDBstartMonth = mcurrentTime.get(Calendar.MONTH);
        int toDBstartDay= mcurrentTime.get(Calendar.DAY_OF_MONTH);

        calendarEndDate = (Calendar) mcurrentTime.clone();
        calendarEndDate.add(Calendar.DAY_OF_MONTH, 1);
        calendarStartDate = (Calendar) mcurrentTime.clone();


        int toDBendYear = calendarEndDate.get(Calendar.YEAR);
        int toDBendMonth = calendarEndDate.get(Calendar.MONTH);
        int toDBendDay = calendarEndDate.get(Calendar.DAY_OF_MONTH);

        startData.setText(toDBstartMonth + "." +toDBstartDay + "." + toDBstartYear);
        endData.setText(toDBendMonth + "." +toDBendDay + "." + toDBendYear);

        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndData();
            }
        });

        dayClicked = new boolean[7];
        Arrays.fill(dayClicked, false);

        //DESCRIPTIONS:
        isClickdescription = false; //false if addTextChangedListener not used
        isLongClkdescription = false; //false
        isFirstClickdescription = true;


        final KeyListener KeyLDescription = description.getKeyListener();
        description.setKeyListener(null);
        //description.setMovementMethod(new ScrollingMovementMethod());

        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(description.getLineCount()>3) {
                    createAnimation.expand_Anim(description);
                }
                //if(!isFirstClickdescription) {expand_Anim(description);}

            }
        });

        View.OnLongClickListener longClickListenerDescription = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (description.getKeyListener() != KeyLDescription){//isLongClkdescription) {
                    if(!isFirstClickdescription) {
                        createAnimation.expand_Anim(description);
                    }
                    isFirstClickdescription =false;
                    createAnimation.expand_Anim(descriptionBTNlayout);
                    createAnimation.showAnim(descriptionBTNlayout, ANIM_TIME);
                    final String textTemp = description.getText().toString();
                    description.setKeyListener(KeyLDescription); keyboardUtil.showSoftKeyboard(contextThis);
                    description.setTextIsSelectable(true);
                    isLongClkdescription = true;
                    description_fabNO.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            description.setText(textTemp);
                            description.setKeyListener(null); keyboardUtil.hideSoftKeyboard(contextThis, description);
                            createAnimation.hideAnim(descriptionBTNlayout, ANIM_TIME);
                            createAnimation.callapse_Anim(descriptionBTNlayout, 0);
                            int lineCount = description.getLineCount();
                            if (lineCount >= 4) {
                                createAnimation.callapse_Anim(description, DP_TEXTVIEW_HEIGHT);
                                isClickdescription = true;
                            }else {
                                isClickdescription = false;
                            }
                            isLongClkdescription = false;
                        }
                    });
                    description_fabOK.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            description.setKeyListener(null); keyboardUtil.hideSoftKeyboard(contextThis, description);
                            String text = description
                                    .getText()
                                    .toString()
                                    .replaceAll(System.getProperty("line.separator"), " ");
                            description.setText(text);
                            createAnimation.hideAnim(descriptionBTNlayout, ANIM_TIME);
                            createAnimation.callapse_Anim(descriptionBTNlayout, 0);
                            int lineCount = description.getLineCount();
                            if (lineCount >= 4) {
                                isClickdescription = true;
                                createAnimation.callapse_Anim(description, DP_TEXTVIEW_HEIGHT);
                            }else {
                                isClickdescription = false;
                            }
                            isLongClkdescription = false;
                        }
                    });

                }else {
                    description.setKeyListener(null); keyboardUtil.hideSoftKeyboard(contextThis, description);
                    createAnimation.hideAnim(descriptionBTNlayout, ANIM_TIME);
                    createAnimation.callapse_Anim(descriptionBTNlayout, 0);
                    int lineCount = description.getLineCount();
                    if (lineCount >= 4) {
                        isClickdescription = true;
                        createAnimation.callapse_Anim(description, DP_TEXTVIEW_HEIGHT);
                    }else {
                        isClickdescription = false;
                    }
                    isLongClkdescription = false;
                }
                return true;
            }
        };
        description.setOnLongClickListener(longClickListenerDescription);
        descriptionBTNlayout.setLongClickable(true);
        descriptionBTNlayout.setOnLongClickListener(longClickListenerDescription);


        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLongClkdescription) {
                    if (isClickdescription) {
                        createAnimation.expand_Anim(description);
                        isClickdescription = false;
                    } else {
                        int lineCount = description.getLineCount();
                        if (lineCount < 4) {
                            createAnimation.expand_Anim(description);
                            isClickdescription = true;

                        } else {
                            createAnimation.callapse_Anim(description, DP_TEXTVIEW_HEIGHT);
                            //callapse_Anim(descriptionBTNlayout, 0);
                            isClickdescription = true;
                        }
                    }
                }else {
                    //TODO FIX Show keybord if key back press
                    description.setKeyListener(KeyLDescription);
                    keyboardUtil.hideSoftKeyboard(contextThis, description);
                    keyboardUtil.showSoftKeyboard(contextThis);
                }
            }
        });
        //WARNINGS:
        isClickwarnings = false; //false if addTextChangedListener not used
        isLongClkwarnings = false; //false
        isFirstClickwarnings = true;

        final KeyListener KeyLwarnings = warnings.getKeyListener();
        warnings.setKeyListener(null);
        keyboardUtil.hideSoftKeyboard(contextThis, warnings);
        //warnings.setMovementMethod(new ScrollingMovementMethod());

        warnings.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(!isFirstClickwarnings) {
                    createAnimation.expand_Anim(warnings);
                }
            }
        });

        View.OnLongClickListener longClickListenerwarnings = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (warnings.getKeyListener() != KeyLwarnings){
                    if(!isFirstClickwarnings) {
                        createAnimation.expand_Anim(warnings);
                    }
                    warnings.setMovementMethod(new ScrollingMovementMethod());

                    int i = warnings.getSelectionStart();
                    warnings.setSelection(i);
                    isFirstClickwarnings = false;
                    createAnimation.expand_Anim(warningsBTNlayout);
                    createAnimation.showAnim(warningsBTNlayout, ANIM_TIME);
                    final String textTemp = warnings.getText().toString();
                    warnings.setKeyListener(KeyLwarnings); keyboardUtil.showSoftKeyboard(contextThis);
                    //Activity activity = EditDrugActivity.this;
                    //activity.getWindow().setSoftInputMode(
                    //        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    warnings.setTextIsSelectable(true);
                    isLongClkwarnings = true;
                    warnings_fabNO.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            warnings.setText(textTemp);
                            warnings.setKeyListener(null); keyboardUtil.hideSoftKeyboard(contextThis, warnings);
                            createAnimation.hideAnim(warningsBTNlayout, ANIM_TIME);
                            createAnimation.callapse_Anim(warningsBTNlayout, 0);
                            int lineCount = warnings.getLineCount();
                            if (lineCount >= 4) {
                                createAnimation.callapse_Anim(warnings, DP_TEXTVIEW_HEIGHT);
                                isClickwarnings = true;
                            }else {
                                isClickwarnings = false;
                            }
                            isLongClkwarnings = false;
                        }
                    });
                    warnings_fabOK.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            warnings.setKeyListener(null); keyboardUtil.hideSoftKeyboard(contextThis, warnings);
                            String text = warnings
                                    .getText()
                                    .toString()
                                    .replaceAll(System.getProperty("line.separator"), " ");
                            warnings.setText(text);
                            createAnimation.hideAnim(warningsBTNlayout, ANIM_TIME);
                            createAnimation.callapse_Anim(warningsBTNlayout, 0);
                            int lineCount = warnings.getLineCount();
                            if (lineCount >= 4) {
                                isClickwarnings = true;
                                createAnimation.callapse_Anim(warnings, DP_TEXTVIEW_HEIGHT);
                            }else {
                                isClickwarnings = false;
                            }
                            isLongClkwarnings = false;
                        }
                    });


                }else {
                    warnings.setKeyListener(null); keyboardUtil.hideSoftKeyboard(contextThis, warnings);
                    createAnimation.hideAnim(warningsBTNlayout, ANIM_TIME);
                    createAnimation.callapse_Anim(warningsBTNlayout, 0);
                    int lineCount = warnings.getLineCount();
                    if (lineCount >= 4) {
                        isClickwarnings = true;
                        createAnimation.callapse_Anim(warnings, DP_TEXTVIEW_HEIGHT);
                    }else {
                        isClickwarnings = false;
                    }
                    isLongClkwarnings = false;
                }
                return true;
            }
        };
        warnings.setOnLongClickListener(longClickListenerwarnings);
        warningsBTNlayout.setLongClickable(true);
        warningsBTNlayout.setOnLongClickListener(longClickListenerwarnings);


        warnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLongClkwarnings) {
                    isFirstClickwarnings =false;
                    if (isClickwarnings) {
                        createAnimation.expand_Anim(warnings);
                        isClickwarnings = false;
                    } else {
                        int lineCount = warnings.getLineCount();
                        if (lineCount < 4) {
                            createAnimation.expand_Anim(warnings);
                            isClickwarnings = true;

                        } else {
                            createAnimation.callapse_Anim(warnings, DP_TEXTVIEW_HEIGHT);
                            //callapse_Anim(warningsBTNlayout, 0);
                            isClickwarnings = true;
                        }
                    }
                }else {
                    //TODO FIX Show keybord if key back press
                    description.setKeyListener(KeyLwarnings);
                    keyboardUtil.hideSoftKeyboard(contextThis, warnings);
                    keyboardUtil.showSoftKeyboard(contextThis);
                }
            }
        });
        //END WARNINGS.

        selectionImage = new SelectionImage();

        if (action.equals(getString(R.string.action_add_drug))) {
            boolean checkConnection = isOnline();
            drugName = intent.getStringExtra("drug_name");
            drugID = intent.getLongExtra("drug_id", -1);
            collapsingToolbarLayout.setTitle(drugName);

            updateDrugData();
            Log.w("action: ", "vectortwo.healthkeeper.action.AddDrug");
            editDrug_ok.setText(R.string.create);
            editDrug_ok.setOnClickListener(new createMedicament());



        } else if (action.equals("vectortwo.healthkeeper.action.EditDrug")) {
            //TODO load from DB
            editDrug_ok.setText(R.string.save);

        }

        listenerListView = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
                TextView timeView = (TextView) convertView.findViewById(R.id.mtc_headline);
                String timeText = timeView.getText().toString();
                timesList.setOnItemClickListener(null);
                if(timeText.equals(mTimeReceptionAdapter.VALUE_NEWTIME)){
                    timeListItemCklicke = true;
                    if(countTimesInDay<countTimesInDayMax) {
                        setNewTime(position);
                    }
                }else {
                    setTime(position);
                }
                setListViewHeightBasedOnChildren(timesList);//Method for Setting the Height of the ListView dynamically.

            }
        };
        timesList.setOnItemClickListener(listenerListView);



        mTimeReceptionAdapter.newTimeButton();
        timesList.setAdapter(mTimeReceptionAdapter.getAdapter());

        setListViewHeightBasedOnChildren(timesList);
    }
    private class SelectionImage implements ImageView.OnClickListener{
        Context context;
        private static final long DURATION_TIME = 250;
        private int[] pillsId = {
                R.drawable.pill0,
                R.drawable.pill1,
                R.drawable.pill2,
                R.drawable.pill3,
                R.drawable.pill4,
                R.drawable.pill5,
                R.drawable.pill6,
                R.drawable.pill7,
                R.drawable.pill8};
        private FrameLayout editDrug_pill_layout;
        private ImageView editDrug_pill0, editDrug_pill1, editDrug_pill2, editDrug_pill3,
                editDrug_pill4, editDrug_pill5, editDrug_pill6, editDrug_pill7, editDrug_pill8;
        private ImageView[] pillsView  = {editDrug_pill0, editDrug_pill1, editDrug_pill2, editDrug_pill3,
                editDrug_pill4, editDrug_pill5, editDrug_pill6, editDrug_pill7, editDrug_pill8};
        private int[] colors = {R.style.pills_color_red, R.style.pills_color_pink,
                R.style.pills_color_purple, R.style.pills_color_indigo, R.style.pills_color_teal,
                R.style.pills_color_green, R.style.pills_color_orange, R.style.pills_color_brown,
                R.style.pills_color_grey};

        private int currentColor = R.style.pills_color_default;
        private int currentImage = 0;

        private int clickLevel = 0;

        public SelectionImage() {
            editDrug_pill0=      (ImageView)findViewById(R.id.editDrug_pill0);
            editDrug_pill1=      (ImageView)findViewById(R.id.editDrug_pill1);
            editDrug_pill2=      (ImageView)findViewById(R.id.editDrug_pill2);
            editDrug_pill3=      (ImageView)findViewById(R.id.editDrug_pill3);
            editDrug_pill4=      (ImageView)findViewById(R.id.editDrug_pill4);
            editDrug_pill5=      (ImageView)findViewById(R.id.editDrug_pill5);
            editDrug_pill6=      (ImageView)findViewById(R.id.editDrug_pill6);
            editDrug_pill7=      (ImageView)findViewById(R.id.editDrug_pill7);
            editDrug_pill8=      (ImageView)findViewById(R.id.editDrug_pill8);
            editDrug_pill_layout=(FrameLayout)findViewById(R.id.editDrug_pill_layout);

            pillsView[0] = editDrug_pill0;
            pillsView[1] = editDrug_pill1;
            pillsView[2] = editDrug_pill2;
            pillsView[3] = editDrug_pill3;
            pillsView[4] = editDrug_pill4;
            pillsView[5] = editDrug_pill5;
            pillsView[6] = editDrug_pill6;
            pillsView[7] = editDrug_pill7;
            pillsView[8] = editDrug_pill8;


            int i = 0;
            for(ImageView view:pillsView){
                view.setOnClickListener(this);
                final Resources.Theme theme = getResources().newTheme();
                theme.applyStyle(colors[i], false);
                final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pill_circle, theme);
                view.setImageDrawable(drawable);
                view.setTag(i); //set colorID
                i++;
            }


        }
        @Override
        public void onClick(final View v) {
            final int tagID =  (Integer)v.getTag();
            if(clickLevel==0){ //Color selection
                currentColor = colors[tagID];

                editDrug_pill_layout.animate()//Hide animation layout
                        .setDuration(DURATION_TIME)
                        .alpha(0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                int i = 0;
                                for(ImageView view:pillsView){//Set in background pills images
                                    view.setOnClickListener(SelectionImage.this);
                                    final Resources.Theme theme = getResources().newTheme();
                                    theme.applyStyle(colors[tagID], false);
                                    final Drawable drawable = ResourcesCompat.getDrawable(getResources(), pillsId[i], theme);
                                    view.setImageDrawable(drawable);
                                    i++;
                                }
                                editDrug_pill_layout.animate()//Show animation layout
                                        .setDuration(DURATION_TIME)
                                        .alpha(1f)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                clickLevel = 1;
                                            }
                                        });
                            }
                        });

            }else if(clickLevel == 1){ //Image selection
                editDrug_pill_layout.animate()
                        .setDuration(DURATION_TIME)
                        .alpha(0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                for(ImageView view:pillsView){
                                    view.setVisibility(View.GONE);
                                }
                                clickLevel = -1;
                                v.setAlpha(0f);
                                v.setVisibility(View.VISIBLE);
                                v.animate()
                                        .setDuration(DURATION_TIME)
                                        .alpha(1f)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                clickLevel = 2;
                                            }
                                        });
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(editDrug_pill_layout.getLayoutParams());
                                params.gravity = Gravity.CENTER;
                                editDrug_pill_layout.setLayoutParams(params);

                                currentImage = tagID;
                                editDrug_pill_layout.setAlpha(1f);

                            }
                        });
            }else if(clickLevel == 2){//Return to color selection
                editDrug_pill_layout.animate()
                        .setDuration(DURATION_TIME)
                        .alpha(0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                int i = 0;
                                for(ImageView view:pillsView){
                                    view.setVisibility(View.VISIBLE);
                                    final Resources.Theme theme = getResources().newTheme();
                                    theme.applyStyle(colors[i], false);
                                    final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pill_circle, theme);
                                    view.setImageDrawable(drawable);
                                    view.setTag(i); //set colorID

                                    i++;
                                }
                                editDrug_pill_layout.animate()
                                        .setDuration(DURATION_TIME)
                                        .alpha(1f)
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);

                                                clickLevel = 0;
                                            }
                                        });
                            }
                        });

                currentColor = R.style.pills_color_default;
                currentImage = 0;
                clickLevel = 0;
            }
        }

        /**
         *
         * @return Color selected by the user
         */
        public int getCurrentImage(){
            return currentImage;
        }
        /**
         *
         * @return Image selected by the user
         */
        public int getCurrentColor(){
            return currentColor;
        }

        /**
         *
         * @param id - Parameter from DB ColorID
         * @return id style resource
         */
        public int getColorStyleResurse_fromID(int id){
            return colors[id];
        }

        /**
         *
         * @param id -  Parameter from DB ImageID
         * @return id resource drawable
         */
        public int getImageDrawableResurse_fromID(int id){
            return pillsId[id];
        }
    }

    private class createMedicament implements Button.OnClickListener{

        @Override
        public void onClick(View v) {
            String weekdays = convertToWeekdays(dayClicked);
            ArrayList<Map<String, String>> data = mTimeReceptionAdapter.getData();

            DrugColumns value = new DrugColumns();
            value.putTitle(drugName);
            descriptionText = description.getText().toString();
            value.putDescription(descriptionText);
            Log.w("description", descriptionText);
            warningsText = warnings.getText().toString();
            value.putWarnings(warningsText);
            Log.w("warnings", warningsText);
            value.putWeekdays(weekdays);
            value.putEndDate(calendarEndDate);
            value.putStartDate(calendarStartDate);
            value.putNotifyMe((notificationSwitchStatus) ? 1 : 0); // 0 / 1

            selectionImage.getCurrentColor();
            selectionImage.getCurrentImage();
            //...
            value.putArchived(0);//Todo конечная дата только в будующем

            int newID = -1;
            Uri uri = getContentResolver().insert(
                    DBContract.Drug.CONTENT_URI,
                    value.getContentValues());
            newID = Integer.parseInt(uri.getLastPathSegment());
            Log.w("NEW ID", newID+"");
                /*
                if (drugID == -1) {
                    Uri uri = getContentResolver().insert(
                            DBContract.Drug.CONTENT_URI,
                            value.getContentValues());
                    newID = Integer.parseInt(uri.getLastPathSegment());
                }else {
                    getContentResolver().update(
                            DBContract.Drug.CONTENT_URI,
                            value.getContentValues(),
                            DBContract.Drug._ID + "=" + drugID,
                            null);
                }*/

            for(char day:weekdays.toCharArray()){
                for (Map<String, String> map : data) {
                    String arm = data.toString();

                    String dosageType = map.get(mTimeReceptionAdapter.KEY_DOSAGE);
                    String take = map.get(mTimeReceptionAdapter.KEY_TAKE);
                    String timeDosage = map.get(mTimeReceptionAdapter.KEY_TIME);

                    if(!take.equals(mTimeReceptionAdapter.VALUE_NEWTIME)) {
                        float takeFloat = Float.parseFloat(take.replace(mTimeReceptionAdapter.WORD_TAKE+" ", ""));

                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat formatDate;
                        if(DateFormat.is24HourFormat(contextThis)){
                            formatDate = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                        }else {
                            formatDate = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                        }
                        try {
                            cal.setTime(formatDate.parse(timeDosage));
                            String time = cal.get(Calendar.HOUR) + "-" +
                                    cal.get(Calendar.MINUTE);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        IntakeColumns intake = new IntakeColumns();
                        intake.putDosage(takeFloat)
                                .putDrugID(newID)
                                .putWeekday(Integer.parseInt(String.valueOf(day)))
                                .putForm(dosageType)
                                .putTime(cal);

                        //intake.putForm(); //dosage type
                        //intake.putTime();

                        //if(newID >= 0) {
                        //    getContentResolver().delete(
                        //            DBContract.Intake.CONTENT_URI,
                        //            DBContract.Intake.DRUG_ID + "=" + newID,
                        //            null);
                        //}
                        getContentResolver().insert(
                                DBContract.Intake.CONTENT_URI,
                                intake.getContentValues());



                    }
                }
            }
            if(notificationSwitchStatus) {
                Log.w("notificationStatus", "ON");
                Log.w("newID", Long.toString(newID));
                Intent i = new Intent(contextThis, DrugNotifyService.class);
                i.setAction(DrugNotifyService.ACTION_RESCHEDULE);
                i.putExtra(DrugNotifyService.KEY_DRUG_ID, newID);
                startService(i);
            }
            startActivity(new Intent(EditDrugActivity.this, MedicamentActivity.class));
            finish();

        }

    }

    private void saveNewMedicamentToDB(){
        String weekdays = convertToWeekdays(dayClicked);
        Log.w("Week day - ", weekdays);
        String end = calendarEndDate.get(Calendar.YEAR) + "-" +
                calendarEndDate.get(Calendar.MONTH) + "-" +
                calendarEndDate.get(Calendar.DAY_OF_MONTH);
        String star = calendarStartDate.get(Calendar.YEAR) + "-" +
                calendarStartDate.get(Calendar.MONTH) + "-" +
                calendarStartDate.get(Calendar.DAY_OF_MONTH);

        Log.w("startDate", star);
        Log.w("endDate", end);
        ArrayList<Map<String, String>> data = mTimeReceptionAdapter.getData();

        DrugColumns value = new DrugColumns();
        value.putTitle(drugName);
        value.putDescription(descriptionText);
        value.putWarnings(warningsText);
        value.putWeekdays(weekdays);
        value.putEndDate(calendarEndDate);
        value.putStartDate(calendarStartDate);
        int notificationStatus = (notificationSwitchStatus) ? 1 : 0;
        value.putNotifyMe(notificationStatus);

        //...
        value.putArchived(0);//Todo конечная дата только в будующем


        if (drugID == -1) {
            getContentResolver().insert(
                    DBContract.Drug.CONTENT_URI,
                    value.getContentValues());
        }else {
            getContentResolver().update(
                    DBContract.Drug.CONTENT_URI,
                    value.getContentValues(),
                    DBContract.Drug._ID + "=" + drugID,
                    null);
        }

        for(char day:weekdays.toCharArray()){
            for (Map<String, String> map : data) {
                String arm = data.toString();
                for (String current : map.keySet()) {
                    //String a =;
                    if (current.equals(mTimeReceptionAdapter.KEY_TAKE)) {


                    } else if (current.equals(mTimeReceptionAdapter.KEY_TIME)) {
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                        try {
                            cal.setTime(sdf.parse(map.get(current)));
                            String time = cal.get(Calendar.HOUR) + "-" +
                                    cal.get(Calendar.MINUTE);

                            IntakeColumns intake = new IntakeColumns();
                            //intake.putDosage();
                            intake.putDrugID((int)drugID);
                            //intake.putForm(); //dosage type
                            //intake.putTime();
                            intake.putWeekday(Integer.parseInt(String.valueOf(day)));
                            if(drugID >= 0) {
                                getContentResolver().delete(
                                        DBContract.Intake.CONTENT_URI,
                                        DBContract.Intake.DRUG_ID + "=" + drugID,
                                        null);
                            }
                            getContentResolver().insert(
                                    DBContract.Intake.CONTENT_URI,
                                    intake.getContentValues());

                            Log.w("TEKE TIME TRANSFORM", time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.w(current, map.get(current));
                }
            }
        }
                /*


                Cursor c = getContentResolver.query(
                 DBContract.Drug.CONTENT_URI,
                 new String [] {DBContract.Drug._ID, DBContract.Drug.TITLE},
                 null, null, null);
                ArrayList<String> arr = new ArrayList<>();
                while (c.moveToFirst) {
                arr.add(c.getString(c.getColumnIndex(DBContract.Drug.TITLE));
                }
                 */
        //eсли оповещения включены
        if(notificationSwitchStatus) {
            Intent i = new Intent(contextThis, DrugNotifyService.class);
            i.setAction(DrugNotifyService.ACTION_RESCHEDULE);
            i.putExtra(DrugNotifyService.KEY_DRUG_ID, drugID);
            startService(i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_drug_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.edit_drug_action_update:
                updateDrugData();
                break;
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            //TODO Диалог выхода;
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    private void updateDrugData(){
        boolean checkConnection = isOnline();

        if (checkConnection) {
            DrugInfo drugInfo = new DrugInfo(drugName);
            DrugInfo.Details details = drugInfo.new Details();
            details.addHandler(new TaskHandler<DrugInfo.Details>(){
                @Override
                public void onPostExecute(DrugInfo.Details r) {
                    String desc = r.getDescription();
                    String warn = r.getWarnings();
                    if(desc.equals(BaseInfo.NO_INFO)) {
                        descriptionText = getResources().getString(R.string.mtc_no_description);
                        description.setText(descriptionText);
                    }else {
                        description.setText(desc);
                        descriptionText = desc;
                    }
                    if(warn.equals(BaseInfo.NO_INFO)){
                        warningsText = getResources().getString(R.string.mtc_no_warnings);
                        warnings.setText(warningsText);
                    }else {
                        warnings.setText(warn);
                        warningsText = warn;
                    }

                }
            });
            details.execute();

        }else{
            description.setText(R.string.edit_drug_no_internet_description);
            warnings.setText(R.string.edit_drug_no_internet_warnings);
            Toast toast = Toast.makeText(contextThis, getResources().getString(R.string.edit_drug_nointernet), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }

    private void setTime(final int position){
        EditDrugTimeDoseDialog editDrugTimeDoseDialog = new EditDrugTimeDoseDialog(contextThis);
        editDrugTimeDoseDialog.setMyDialogListener( new EditDrugTimeDoseDialog.MyDialogListener()
        {
            public void userCanceled(){
                timesList.setOnItemClickListener(listenerListView);
            }

            @Override
            public void userClickOk(double value, int hour, int minutes,
                                    String dose_type, boolean is24HourFormat, String status) {

                    if(!is24HourFormat) {
                        String strMinutes= Integer.toString(minutes);
                        if(minutes<10){strMinutes = "0" + minutes;}
                        if(value>1000){value=1000;}
                        mTimeReceptionAdapter.changeData(position, String.valueOf(value), dose_type,
                                Integer.toString(hour) + ":" + strMinutes + " " +status);
                        mTimeReceptionAdapter.update();
                    }else {
                        String strMinutes= Integer.toString(minutes);
                        if(minutes<10){strMinutes = "0" + minutes;}
                        String strHour = Integer.toString(hour);
                        if(hour<10){strHour = "0"+hour;}
                        if(value>1000){value=1000;}
                        mTimeReceptionAdapter.changeData(position, String.valueOf(value), dose_type,
                                strHour + ":" + strMinutes + " " +status); //Integer.toString(number),
                        mTimeReceptionAdapter.update();
                    }

                    setListViewHeightBasedOnChildren(timesList);//Method for Setting the Height of the ListView dynamically.

            }

            @Override
            public void deleteItem() {
                mTimeReceptionAdapter.removeTime(position);
                if(countTimesInDay==countTimesInDayMax){
                    mTimeReceptionAdapter.newTimeButton();
                    timesList.setAdapter(mTimeReceptionAdapter.getAdapter());
                }
                if(countTimesInDay>0){
                    countTimesInDay-=1;
                }
                setListViewHeightBasedOnChildren(timesList);//Method for Setting the Height of the ListView dynamically.
            }

            @Override
            public void end() {
                timesList.setOnItemClickListener(listenerListView);
            }

        });
        editDrugTimeDoseDialog.show();
    }

    private void setNewTime(final int position){
        EditDrugTimeDoseDialog editDrugTimeDoseDialog = new EditDrugTimeDoseDialog(contextThis);
        editDrugTimeDoseDialog.setMyDialogListener( new EditDrugTimeDoseDialog.MyDialogListener()
        {
            public void userCanceled(){
                timesList.setOnItemClickListener(listenerListView);
            }

            @Override
            public void userClickOk(double value, int hour, int minutes,
                                    String dose_type, boolean is24HourFormat, String status) {
                if(timeListItemCklicke) {
                    mTimeReceptionAdapter.removeTime(position);
                    if(!is24HourFormat) {
                        String strMinutes= Integer.toString(minutes);
                        if(minutes<10){strMinutes = "0" + minutes;}
                        if(value>1000){value=1000;}
                        mTimeReceptionAdapter.addTime(String.valueOf(value), dose_type,
                                Integer.toString(hour) + ":" + strMinutes + " " +status);
                    }else {
                        String strMinutes= Integer.toString(minutes);
                        if(minutes<10){strMinutes = "0" + minutes;}
                        String strHour = Integer.toString(hour);
                        if(hour<10){strHour = "0"+hour;}
                        if(value>1000){value=1000;}
                        mTimeReceptionAdapter.addTime(String.valueOf(value), dose_type,
                                strHour + ":" + strMinutes + " " +status); //Integer.toString(number),
                    }
                    timesList.setAdapter(mTimeReceptionAdapter.getAdapter());
                    timeListItemCklicke = false;
                    countTimesInDay+=1;
                    if(countTimesInDay<countTimesInDayMax) {
                        mTimeReceptionAdapter.newTimeButton();
                        timesList.setAdapter(mTimeReceptionAdapter.getAdapter());
                    }


                    setListViewHeightBasedOnChildren(timesList);//Method for Setting the Height of the ListView dynamically.
                }
            }

            @Override
            public void deleteItem() {
            }

            @Override
            public void end() {
                timesList.setOnItemClickListener(listenerListView);
            }

        });
        editDrugTimeDoseDialog.show();
    }

    private void setStartData(){
        Calendar mcurrentTime = Calendar.getInstance();
        int year = mcurrentTime.get(Calendar.YEAR);
        int month = mcurrentTime.get(Calendar.MONTH);
        final int day_of_month = mcurrentTime.get(Calendar.DAY_OF_MONTH);

        TimePickerDialog mTimePicker;

        DatePickerDialog mDataDialog;
        mDataDialog = new DatePickerDialog(contextThis, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startData.setText(month + "." +dayOfMonth + "." + year);
                        calendarStartDate.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                        calendarStartDate.set(Calendar.MONTH, view.getMonth());
                        calendarStartDate.set(Calendar.YEAR, view.getYear());

                        if(calendarEndDate.before(calendarStartDate)){
                            calendarEndDate.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                            calendarEndDate.set(Calendar.MONTH, view.getMonth());
                            calendarEndDate.set(Calendar.YEAR, view.getYear());
                            calendarEndDate.add(Calendar.DAY_OF_MONTH, 1);
                            endData.setText(calendarEndDate.get(Calendar.MONTH) + "." +
                                    calendarEndDate.get(Calendar.DAY_OF_MONTH) + "." +
                                    calendarEndDate.get(Calendar.YEAR));
                        }
                    }
                }, year, month, day_of_month) ;

        //mDataDialog.getDatePicker()
        //        .setMinDate(System.currentTimeMillis() - 1000);
        Calendar calendarMaximum = (Calendar) calendarStartDate.clone();
        calendarMaximum.add(Calendar.YEAR, 1);
        mDataDialog.getDatePicker()
                .setMaxDate(calendarMaximum.getTimeInMillis());
        mDataDialog.show();
    }
    private void setEndData(){
        Calendar mcurrentTime = Calendar.getInstance();
        int year = mcurrentTime.get(Calendar.YEAR);
        int month = mcurrentTime.get(Calendar.MONTH);
        int day_of_month = mcurrentTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDataDialog;

        mDataDialog = new DatePickerDialog(contextThis, R.style.DialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarEndDate.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                        calendarEndDate.set(Calendar.MONTH, view.getMonth());
                        calendarEndDate.set(Calendar.YEAR, view.getYear());
                        if(calendarEndDate.equals(calendarStartDate)){
                            calendarEndDate.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        endData.setText(month + "." +calendarEndDate.get(Calendar.DAY_OF_MONTH) + "." + year);


                    }
                }, year, month, day_of_month) ;

        Calendar calendarTemp = (Calendar) calendarStartDate.clone();
        calendarTemp.add(Calendar.DAY_OF_MONTH, 1);
        Calendar calendarMaximum = (Calendar) calendarStartDate.clone();
        calendarMaximum.add(Calendar.YEAR, 1);
        mDataDialog.getDatePicker().setMinDate(calendarTemp.getTimeInMillis()-1000);
        mDataDialog.getDatePicker().setMaxDate(calendarMaximum.getTimeInMillis());
        mDataDialog.show();

    }

    private void dayOfWeekButtons(final Context contextThis){
        btn_sun = (Button)findViewById(R.id.ed_sun);
        btn_mon = (Button)findViewById(R.id.ed_mon);
        btn_tue = (Button)findViewById(R.id.ed_tue);
        btn_wed = (Button)findViewById(R.id.ed_wed);
        btn_thu = (Button)findViewById(R.id.ed_thu);
        btn_fri = (Button)findViewById(R.id.ed_fri);
        btn_sat = (Button)findViewById(R.id.ed_sat);

        final TransitionDrawable btn_sun_tran = (TransitionDrawable) btn_sun.getBackground();
        final TransitionDrawable btn_mon_tran = (TransitionDrawable) btn_mon.getBackground();
        final TransitionDrawable btn_tue_tran = (TransitionDrawable) btn_tue.getBackground();
        final TransitionDrawable btn_wed_tran = (TransitionDrawable) btn_wed.getBackground();
        final TransitionDrawable btn_thu_tran = (TransitionDrawable) btn_thu.getBackground();
        final TransitionDrawable btn_fri_tran = (TransitionDrawable) btn_fri.getBackground();
        final TransitionDrawable btn_sat_tran = (TransitionDrawable) btn_sat.getBackground();

        View.OnClickListener dayBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ed_sun:
                        dayOfWeekButtonsChange(0, btn_sun_tran);
                        break;
                    case R.id.ed_mon:
                        dayOfWeekButtonsChange(1, btn_mon_tran);
                        break;
                    case R.id.ed_tue:
                        dayOfWeekButtonsChange(2, btn_tue_tran);
                        break;
                    case R.id.ed_wed: {
                        dayOfWeekButtonsChange(3, btn_wed_tran);
                        break;}
                    case R.id.ed_thu:
                        dayOfWeekButtonsChange(4, btn_thu_tran);
                        break;
                    case R.id.ed_fri:
                        dayOfWeekButtonsChange(5, btn_fri_tran);
                        break;
                    case R.id.ed_sat:
                        dayOfWeekButtonsChange(6, btn_sat_tran);
                        break;
                }
            }
        };

        btn_sun.setOnClickListener(dayBtnListener);
        btn_mon.setOnClickListener(dayBtnListener);
        btn_tue.setOnClickListener(dayBtnListener);
        btn_wed.setOnClickListener(dayBtnListener);
        btn_thu.setOnClickListener(dayBtnListener);
        btn_fri.setOnClickListener(dayBtnListener);
        btn_sat.setOnClickListener(dayBtnListener);
    }
    private void dayOfWeekButtonsChange(int numButton, TransitionDrawable transitionDrawable){
        if(!dayClicked[numButton]){
            transitionDrawable.startTransition(ANIM_DAY_BUTTON);
            dayClicked[numButton] = true;
        }else {
            transitionDrawable.reverseTransition(ANIM_DAY_BUTTON);
            dayClicked[numButton] = false;
        }
    }

    private String convertToWeekdays(boolean[] _dayClicked){
        String weekdays = "";
        for (int i=0; i< _dayClicked.length; i++ ){
            if(_dayClicked[i]){
                Log.w("Week day - ", Integer.toString(i+1));
                weekdays+=Integer.toString(i+1);
            }
        }
        return weekdays;
    }



    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private boolean isOnline(){
        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            return false;
        }else{
            return true;
        }
    }
}