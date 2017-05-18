package com.vectortwo.healthkeeper.activities.menu_settings;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.PrefManager;
import com.vectortwo.healthkeeper.activities.main.MainActivity;
import com.vectortwo.healthkeeper.utilities.utils;

import java.util.ArrayList;
import java.util.List;

public class ElementsSettingsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ElementsMListAdapter elementsMListAdapter;
    private List<ElementsList> elementsLists;
    private utils useUtils;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager = new PrefManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.elements_recycler_view);
        elementsLists = new ArrayList<>();
        elementsMListAdapter = new ElementsMListAdapter(this, elementsLists);
        useUtils = new utils(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, useUtils.dpToPx(3), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(elementsMListAdapter);


        ElementsList item = new ElementsList(
                getResources().getString(R.string.medicament),
                R.drawable.img_medicament,
                prefManager.getMedicametVisible());
        elementsLists.add(item);
        item = new ElementsList(
                getResources().getString(R.string.blood_sugar),
                R.drawable.img_blood,
                prefManager.getBloodSugarVisible());
        elementsLists.add(item);
        item = new ElementsList(
                getResources().getString(R.string.walking),
                R.drawable.img_walking,
                prefManager.getWalkingVisible());
        elementsLists.add(item);
        item = new ElementsList(
                getResources().getString(R.string.pressure),
                R.drawable.img_pressure,
                prefManager.getPressureVisible());
        elementsLists.add(item);
        item = new ElementsList(
                getResources().getString(R.string.weight),
                R.drawable.img_weight,
                prefManager.getWeightVisible());
        elementsLists.add(item);
        item = new ElementsList(
                getResources().getString(R.string.aqua),
                R.drawable.img_aqua,
                prefManager.getAquaVisible());
        elementsLists.add(item);
        item = new ElementsList(
                getResources().getString(R.string.sleep),
                R.drawable.img_sleep,
                prefManager.getSleepVisible());
        elementsLists.add(item);

        elementsMListAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            startActivity(new Intent(ElementsSettingsActivity.this, MainActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
