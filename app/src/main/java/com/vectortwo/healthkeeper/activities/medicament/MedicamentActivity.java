/**
 * Окно лекарств и статистки
 */

package com.vectortwo.healthkeeper.activities.medicament;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.medicament.search.MedicamentSearchSActivity;
import com.vectortwo.healthkeeper.data.db.DBContract;


import java.util.ArrayList;
import java.util.List;

public class MedicamentActivity extends  AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    private RecyclerView recyclerView;
    private MListAdapter adapter;
    private List<MList> albumList;
    private FloatingActionButton fab;
    private MedicamentAdapter medicamentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament);

        fab = (FloatingActionButton)findViewById(R.id.addDrug);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MedicamentActivity.this, MedicamentSearchSActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.medicament_recycler_view);

        //albumList = new ArrayList<>();
        //adapter = new MListAdapter(this, albumList);
        medicamentAdapter = new MedicamentAdapter(this, null);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(medicamentAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 ||dy<0 && fab.isShown()) {
                    fab.hide();
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                DBContract.Drug.CONTENT_URI,
                new String[] {DBContract.Drug._ID, DBContract.Drug.TITLE, DBContract.Drug.DESCRIPTION},
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        medicamentAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        medicamentAdapter.swapCursor(null);
    }
}