package com.vectortwo.healthkeeper.activities.main;

import android.animation.*;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.SensorEvent;
import android.os.IBinder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.PrefManager;
import com.vectortwo.healthkeeper.activities.aqua.AquaActivity;
import com.vectortwo.healthkeeper.activities.blood.BloodSugarActivity;
import com.vectortwo.healthkeeper.activities.medicament.MedicamentActivity;
import com.vectortwo.healthkeeper.activities.menu_settings.ElementsList;
import com.vectortwo.healthkeeper.activities.pressure.PressureActivity;
import com.vectortwo.healthkeeper.activities.sleep.SleepActivity;
import com.vectortwo.healthkeeper.activities.walking.WalkingActivity;
import com.vectortwo.healthkeeper.activities.weight.WeightActivity;
import com.vectortwo.healthkeeper.animations.CreateAnimation;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.WellBeingColumns;
import com.vectortwo.healthkeeper.services.PedometerService;
import com.vectortwo.healthkeeper.widgets.FitDoughnut;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by skaper on 24.04.17.
 */

public class MainListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context contextThis;
    private List<MainList> mainList;
    private PrefManager prefManager;
    private CardView cardView;
    private PedometerService pedometerService;
    private boolean pedometerBound = false;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private FitDoughnut doughnut;
    private TextView fitness_time, fitness_calories, fitness_walked;
    private GridLayout fitness_walked_layout;
    private ImageView feeling_smile_1, feeling_smile_2, feeling_smile_3, feeling_smile_4, feeling_smile_5;
    private LinearLayout feeling_smile_layout;
    private CardView feeling_card, main_emply_card;
    private CreateAnimation animation;

    public static final String TYPE_NAME_MAIN = "MAIN_CARD";
    public static final String TYPE_NAME_FITNESS = "FITNESS_CARD";
    public static final String TYPE_NAME_FEELING = "FEELING_CARD";
    public static final String TYPE_NAME_EMPLY = "EMPLY_CARD";

    public static final int TYPE_VALUE_MAIN = 0;
    public static final int TYPE_VALUE_FITNESS = 1;
    public static final int TYPE_VALUE_FEELING = 2;
    public static final int TYPE_VALUE_EMPLY = 3;

    private static final long FEELING_CARD_ANIM_START_DELAY = 1000;


    public ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PedometerService.PedometerBinder binder = (PedometerService.PedometerBinder) service;
            pedometerService = binder.getService();
            pedometerBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            pedometerBound = false;
        }
    };


    public boolean isPedometerBound() {
        return pedometerBound;
    }

    public void setPedometerBound(boolean pedometerBound) {
        this.pedometerBound = pedometerBound;
    }

    Animation zoomin, zoomout;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;
        public MyViewHolder(View view){
            super(view);
            prefManager = new PrefManager(contextThis);
            imageView = (ImageView) view.findViewById(R.id.main_card_image);
            textView = (TextView) view.findViewById(R.id.main_card_text);
            cardView = (CardView) view.findViewById(R.id.main_card);
            cardView.setClickable(true);
            cardView.setOnClickListener(new ClickListener(this));
            //aSwitch.setOnCheckedChangeListener(new MainListAdapter.CheckedChangeListener(this));
            zoomin = AnimationUtils.loadAnimation(contextThis, R.anim.zoom_in);
            zoomout = AnimationUtils.loadAnimation(contextThis, R.anim.zoom_out);

        }
    }

    public class FitnessViewHolder extends RecyclerView.ViewHolder{

        public FitnessViewHolder(View itemView) {
            super(itemView);

            fitness_time = (TextView) itemView.findViewById(R.id.fitness_time);
            fitness_calories = (TextView) itemView.findViewById(R.id.fitness_calories);
            fitness_walked = (TextView) itemView.findViewById(R.id.fitness_walked);
            fitness_walked_layout = (GridLayout) itemView.findViewById(R.id.fitness_walked_layout);

        }
    }

    public class EmplyViewHolder extends RecyclerView.ViewHolder{

        public EmplyViewHolder(View itemView) {
            super(itemView);

            main_emply_card = (CardView) itemView.findViewById(R.id.main_emply_cardview);

        }
    }

    public class FeelingViewHolder extends RecyclerView.ViewHolder{

        public FeelingViewHolder(View itemView) {
            super(itemView);

            feeling_smile_1 = (ImageView) itemView.findViewById(R.id.feeling_smile_1);
            feeling_smile_1.setOnClickListener(new smileClick(contextThis));
            feeling_smile_2 = (ImageView) itemView.findViewById(R.id.feeling_smile_2);
            feeling_smile_2.setOnClickListener(new smileClick(contextThis));
            feeling_smile_3 = (ImageView) itemView.findViewById(R.id.feeling_smile_3);
            feeling_smile_3.setOnClickListener(new smileClick(contextThis));
            feeling_smile_4 = (ImageView) itemView.findViewById(R.id.feeling_smile_4);
            feeling_smile_4.setOnClickListener(new smileClick(contextThis));
            feeling_smile_5 = (ImageView) itemView.findViewById(R.id.feeling_smile_5);
            feeling_smile_5.setOnClickListener(new smileClick(contextThis));
            feeling_smile_layout = (LinearLayout) itemView.findViewById(R.id.feeling_smile_layout);
            feeling_card = (CardView) itemView.findViewById(R.id.feeling_card);
            feeling_card.setOnClickListener(new CardView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //toggleProductDescriptionHeight();
                }
            });


        }
    }
    private class smileClick implements ImageView.OnClickListener{
        Context context;
        int rating = 3;
        private static final long ANIMATION_TIME = 200;
        public smileClick(Context _context){
            super();
            this.context = _context;


        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.feeling_smile_1:
                    //feeling_smile_1.setAlpha(1f);
                    animation.showAnimSmile(feeling_smile_1,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_2,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_3,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_4,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_5,  ANIMATION_TIME);
                    rating = 1;
                    break;

                case R.id.feeling_smile_2:
                    animation.hideAnimSmile(feeling_smile_1,  ANIMATION_TIME);
                    animation.showAnimSmile(feeling_smile_2,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_3,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_4,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_5,  ANIMATION_TIME);
                    rating = 2;
                    break;

                case R.id.feeling_smile_3:
                    animation.hideAnimSmile(feeling_smile_1,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_2,  ANIMATION_TIME);
                    animation.showAnimSmile(feeling_smile_3,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_4,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_5,  ANIMATION_TIME);
                    rating = 3;
                    break;

                case R.id.feeling_smile_4:
                    animation.hideAnimSmile(feeling_smile_1,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_2,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_3,  ANIMATION_TIME);
                    animation.showAnimSmile(feeling_smile_4,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_5,  ANIMATION_TIME);
                    rating = 4;
                    break;

                case R.id.feeling_smile_5:
                    animation.hideAnimSmile(feeling_smile_1,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_2,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_3,  ANIMATION_TIME);
                    animation.hideAnimSmile(feeling_smile_4,  ANIMATION_TIME);
                    animation.showAnimSmile(feeling_smile_5,  ANIMATION_TIME);
                    rating = 5;
                    break;

                default:
                    break;

            }
            /**
             * Put WellBeing in DB
             */
            WellBeingColumns values = new WellBeingColumns();
            Calendar mcurrentTime = Calendar.getInstance();
            values.putDate(mcurrentTime).putValue(rating);
            context.getContentResolver().insert(DBContract.WellBeing.CONTENT_URI, values.getContentValues());

            animation.hideShowCard(feeling_card, R.dimen.big_card_height, FEELING_CARD_ANIM_START_DELAY);

        }
    }


    public MainListAdapter(Context context, List<MainList> _mainList){
        this.contextThis = context;
        this.mainList = _mainList;
        this.animation = new CreateAnimation(context);
        prefManager = new PrefManager(contextThis);
    }
    private class ClickListener implements CardView.OnClickListener{
        private MainListAdapter.MyViewHolder myViewHolder;
        private ClickListener(MainListAdapter.MyViewHolder myViewHolder){
            super();
            this.myViewHolder = myViewHolder;
        }
        boolean pressed = false;

        @Override
        public void onClick(final View view) {
            String name = (String) myViewHolder.textView.getText();

            if(name.equals(contextThis.getResources().getString(R.string.medicament))){
                openActivity(view, new MedicamentActivity());
            }
            else if(name.equals(contextThis.getResources().getString(R.string.blood_sugar))){
                openActivity(view, new BloodSugarActivity());
            }
            else if(name.equals(contextThis.getResources().getString(R.string.walking))){
                openActivity(view, new WalkingActivity());
            }
            else if(name.equals(contextThis.getResources().getString(R.string.pressure))){
                openActivity(view, new PressureActivity());
            }
            else if(name.equals(contextThis.getResources().getString(R.string.weight))){
                openActivity(view, new WeightActivity());
            }
            else if(name.equals(contextThis.getResources().getString(R.string.aqua))){
                openActivity(view, new AquaActivity());
            }
            else if(name.equals(contextThis.getResources().getString(R.string.sleep))){
                openActivity(view, new SleepActivity());
                //contextThis.startActivity(new Intent(view.getContext(), SleepActivity.class));
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        int result = -1;
        if(mainList.get(position).getType().equals(TYPE_NAME_MAIN)){
            result = TYPE_VALUE_MAIN;
        }else if(mainList.get(position).getType().equals(TYPE_NAME_FITNESS)){
            result = TYPE_VALUE_FITNESS;
        }else if(mainList.get(position).getType().equals(TYPE_NAME_FEELING)){
            result = TYPE_VALUE_FEELING;
        }else if(mainList.get(position).getType().equals(TYPE_NAME_EMPLY)){
            result = TYPE_VALUE_EMPLY;
        }
        return result;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType){
            case TYPE_VALUE_MAIN: {
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_element_card, parent, false);

                return new MainListAdapter.MyViewHolder(itemView);

            }
            case TYPE_VALUE_FITNESS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fitness_element_card, parent, false);

                final int targetTime = prefManager.getWalkingTimeTarget();
                int time = 0;
                doughnut = (FitDoughnut) itemView.findViewById(R.id.fitness_doughnut);
                doughnut.animateSetTime(time);
                Thread t = new Thread() {

                    @Override
                    public void run() {
                        try {
                            while (!isInterrupted()) {
                                Thread.sleep(250);
                                ((MainActivity)contextThis).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(pedometerBound) {
                                            int time = pedometerService.getWalkingTimeToday();
                                            //int time = (int)pedometerService.getStepsLastHour();
                                            long stepToday = pedometerService.getStepsToday();
                                            fitness_time.setText(String.valueOf(time));
                                            fitness_walked.setText(String.valueOf(stepToday));
                                            if (time<=targetTime) {
                                                //doughnut.animateSetTime(time);
                                                doughnut.animateSetPercent(time, targetTime);
                                            }
                                            fitness_walked_layout.invalidate();
                                        }

                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                        }
                    }
                };

                t.start();

                return new MainListAdapter.FitnessViewHolder(itemView);

            case TYPE_VALUE_FEELING:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.feeling_element_card, parent, false);
                return new MainListAdapter.FeelingViewHolder(itemView);

            case TYPE_VALUE_EMPLY:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_emply_card, parent, false);

                return new MainListAdapter.EmplyViewHolder(itemView);

        }
        return null;

    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainList item;
        ViewGroup.LayoutParams lp;
        switch (holder.getItemViewType()) {
            case TYPE_VALUE_MAIN:
                MyViewHolder myViewHolder = (MyViewHolder)holder;
                MainList mainL = mainList.get(position);
                myViewHolder.textView.setText(mainL.getName());
                myViewHolder.imageView.setImageResource(mainL.getImage());
                break;

            case TYPE_VALUE_FITNESS:
                lp = holder.itemView.getLayoutParams();
                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
                    sglp.setFullSpan(true);
                    holder.itemView.setLayoutParams(sglp);
                }
                break;

            case TYPE_VALUE_FEELING:
                lp = holder.itemView.getLayoutParams();
                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
                    sglp.setFullSpan(true);
                    holder.itemView.setLayoutParams(sglp);
                }
                feeling_card.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver
                        .OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        feeling_card.getViewTreeObserver().removeOnPreDrawListener(this);
                        ViewGroup.LayoutParams layoutParams = feeling_card.getLayoutParams();
                        layoutParams.height = (int) contextThis.getResources().getDimension(R.dimen
                                .big_card_height);
                        feeling_card.setLayoutParams(layoutParams);

                        return true;
                    }
                });
                break;
            case TYPE_VALUE_EMPLY:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mainList.size();
    }

    private void openActivity(final View view, final Context context){
        view.setAnimation(zoomin);//myViewHolder.imageView
        view.startAnimation(zoomin);
        view.setAnimation(zoomout);
        view.startAnimation(zoomout);
        zoomout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contextThis.startActivity(new Intent(view.getContext(), context.getClass()));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}