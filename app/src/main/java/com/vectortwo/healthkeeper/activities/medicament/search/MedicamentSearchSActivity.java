package com.vectortwo.healthkeeper.activities.medicament.search;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.data.db.DBContentProvider;
import com.vectortwo.healthkeeper.data.db.DBContract;
import com.vectortwo.healthkeeper.data.db.DBOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicamentSearchSActivity extends AppCompatActivity {

    private static final String ACTION_EDIT_DRUG = "com.vectortwo.healthkeeper.intent.ADDDRUG";
    private DBContentProvider mDbHelper;
    private SearchView search;
    private SimpleCursorAdapter simpleCursorAdapter;
    private ListView listView;
    private Context contextThis;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_search);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbar);
        listView = (ListView) findViewById(R.id.search_list_view);
        toolbar.setTitle("some title");
        toolbar.inflateMenu(R.menu.main_menu);
        final View logoLayout = findViewById(R.id.logo_layout);
        contextThis = this;

        search = (SearchView) findViewById(R.id.searchMedicamentView);
        search.setQueryHint("Find drugs");
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
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.w("Text enter: ", s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {

                if(search.getWidth()>0)
                {
                    if(s.length()>0) {
                        Uri uri = Uri.withAppendedPath(DBContract.KnownDrugs.CONTENT_URI, s);
                        final Cursor cursor = getContentResolver().query(uri,
                                null, null, null, null);
                        String[] from =new String[] {DBContract.KnownDrugs.TITLE};
                        int[] to = new int[]{R.id.msearchitem_text,};
                        simpleCursorAdapter = new SearchSimpleCursorAdapter(
                                contextThis, R.layout.m_search_item, modCursor(cursor), from, to, 0);

                        listView.setAdapter(simpleCursorAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String drugName = ((TextView)
                                        view.findViewById(R.id.msearchitem_text)).getText().toString();
                                Intent intent = new Intent(ACTION_EDIT_DRUG);
                                Log.w("ID", id+"");
                                intent.putExtra("drug_name", drugName);
                                intent.putExtra("drug_id", id);
                                startActivity(intent);
                            }
                        });

                    }else{
                        listView.setAdapter(null);
                    }
                }


                return false;
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(cursor!=null){
            cursor.close();
        }
    }

    public Cursor modCursor(Cursor c) {
        MatrixCursor extras = new MatrixCursor(new String[] { "_id", "title" });
        extras.addRow(new String[] { "-1", "Create Drug" });
        Cursor[] cursors = { extras, c };
        Cursor extendedCursor = new MergeCursor(cursors);
        return extendedCursor;
    }

    private class SearchSimpleCursorAdapter extends SimpleCursorAdapter{
        public SearchSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
        }

        @Override
        public ViewBinder getViewBinder() {

            return super.getViewBinder();
        }
    }


}
