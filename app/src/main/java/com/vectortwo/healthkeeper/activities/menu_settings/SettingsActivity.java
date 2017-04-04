package com.vectortwo.healthkeeper.activities.menu_settings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.PrefManager;

public class SettingsActivity extends AppCompatActivity {
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init the swipe back
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_sleep)
                .setSwipeBackView(R.layout.swipeback_default);


    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
    }

}
