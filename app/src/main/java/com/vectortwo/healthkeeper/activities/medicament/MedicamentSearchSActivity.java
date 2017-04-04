package com.vectortwo.healthkeeper.activities.medicament;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.data.db.DBContentProvider;
import com.vectortwo.healthkeeper.data.db.DBOpenHelper;

public class MedicamentSearchSActivity extends AppCompatActivity {

    private DBContentProvider mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_search);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbar);
        toolbar.setTitle("some title");
        toolbar.inflateMenu(R.menu.main_menu);
        final View logoLayout = findViewById(R.id.logo_layout);



        SearchView search = (SearchView) findViewById(R.id.simpleSearchView);

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    logoLayout.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                }else{
                    logoLayout.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        Intent intent = getIntent();
        //Проверяем тип Intent
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //Берем строку запроса из экстры
            String query = intent.getStringExtra(SearchManager.QUERY);
            //Выполняем поиск
            //showResults(query);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
    /*
    private void showResults(String query) {
        //Ищем совпадения
        Cursor cursor = mDbHelper.fetchRecordsByQuery(query);
        startManagingCursor(cursor);
        String[] from = new String[] { RecordsDbHelper.KEY_DATA };
        int[] to = new int[] { R.id.text1 };

        SimpleCursorAdapter records = new SimpleCursorAdapter(this,
                R.layout.record, cursor, from, to);
        //Обновляем адаптер
        setListAdapter(records);
    }*/
}
