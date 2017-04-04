/**
 * Окно лекарств и статистки
 */

package com.vectortwo.healthkeeper.activities.medicament;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.data.DrugInfo;


import java.util.ArrayList;
import java.util.List;

public class MedicamentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MListAdapter adapter;
    private List<MList> albumList;
    private FloatingActionButton fab;
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

        albumList = new ArrayList<>();
        adapter = new MListAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


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
        prepareAlbums();

    }

       /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        DrugInfo base = new DrugInfo();
        final DrugInfo.Warnings desc = base.new Warnings();
        desc.addHandler(new DrugInfo.EventHandler<String>() {
            @Override
            public void onPostExecute(String o) {
                MList a = new MList("adderall", o, "cp 1:25pm", "0.25");
                albumList.add(a);
                a = new MList("adderall", o, "cp 1:25pm", "0.25");
                albumList.add(a);
                a = new MList("adderall", o, "cp 1:25pm", "0.25");
                albumList.add(a);
                a = new MList("adderall", o, "cp 1:25pm", "0.25");
                albumList.add(a);
                a = new MList("adderall", o, "cp 1:25pm", "0.25");
                albumList.add(a);
                a = new MList("adderall", o, "cp 1:25pm", "0.25");
                albumList.add(a);

                adapter.notifyDataSetChanged();
            }
        });
        //setContentView(R.layout.activity_medicament);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        desc.execute("adderall");
        MList a = new MList("Adderral", "some some", "cp 1:25pm", "0.25");
        albumList.add(a);
        a = new MList("Adderral 2", "some some", "cp 1:25pm", "0.25");
        albumList.add(a);
        a = new MList("Adderral 3", "some some", "cp 1:25pm", "0.25");

        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }
}
/*
public class MedicamentActivity extends AppCompatActivity {
    LinearLayout mainLinearLayout;
    ScrollView mainView;
    Button btn_element;
    CoordinatorLayout mainCoordinator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainCoordinator = new CoordinatorLayout(this);
        mainView = new ScrollView(this);

        mainLinearLayout = new LinearLayout(this);
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        
        ViewGroup.LayoutParams mainLinearLayoutParams = new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainView.addView(mainLinearLayout, mainLinearLayoutParams);

        mainCoordinator.addView(mainView);
        setContentView(mainCoordinator, mainLinearLayoutParams);



        for (int i=0; i<10; i++){
            createElement();
        }

        btn_element = new Button(this);
        btn_element.setText("+");
        CoordinatorLayout coordinatorLayout = new CoordinatorLayout(this);
        FloatingActionButton fab = new FloatingActionButton(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MedicamentActivity.this, MedicamentSearchSActivity.class));
            }
        });
        fab.setForegroundGravity(Gravity.BOTTOM | Gravity.END); //TODO Fix fab button

        LinearLayout.LayoutParams btn_element_Params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_element_Params.gravity = Gravity.RIGHT;// & Gravity.TOP;
        //fab.setLayoutParams(btn_element_Params);
        //coordinatorLayout.addView(fab);

        mainLinearLayout.addView(coordinatorLayout);
        mainCoordinator.addView(fab);


        DrugInfo base = new DrugInfo();
        final DrugInfo.Warnings desc = base.new Warnings();
        desc.addHandler(new DrugInfo.EventHandler<String>() {
            @Override
            public void onPostExecute(String o) {
                Log.w("MED: ", o);
            }
        });
        //setContentView(R.layout.activity_medicament);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        desc.execute("adderall");



    }

    public void createAllElements(int quantity){

        for (int i=0; i<=quantity; i++){

        }


    }

    private void creatingAllDrugs(){
        // Read quantity

        //Generate
    }

    //Создание одного элемента элемента лекарства
    public boolean createElement(){
        LinearLayout singlLinelLayout = new LinearLayout(this);
        singlLinelLayout.setOrientation(LinearLayout.HORIZONTAL);
        singlLinelLayout.setBackgroundResource(R.drawable.layout_border);
        int elementHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()); //convert dp to pixel
        ViewGroup.LayoutParams singlLinelLayoutParams = new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, elementHeight);
        mainLinearLayout.addView(singlLinelLayout,singlLinelLayoutParams);

        //*Картинка препората
        int imgWidlth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()); //convert dp to pixel
        ViewGroup.LayoutParams imgParams = new ViewGroup.LayoutParams(imgWidlth, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView medicamentImg = new ImageView(this);
        medicamentImg.setImageResource(R.mipmap.drug); //TODO загрузка картинки из сети
        singlLinelLayout.addView(medicamentImg, imgParams);

        LinearLayout forTextLinLay = new LinearLayout(this);
        forTextLinLay.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams forTextLinLayParams = new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        singlLinelLayout.addView(forTextLinLay, forTextLinLayParams);

        //*Название препората
        TextView medicamentName = new TextView(this);
        medicamentName.setText(R.string.medicament_default_name);
        medicamentName.setTextSize(25);
        forTextLinLay.addView(medicamentName);
        //*Описание препората TODO: Добавить анимацию пролистывания текста при нажатии
        TextView medicamentSpecification = new TextView(this);
        medicamentSpecification.setText(R.string.medicament_specification);
        medicamentSpecification.setTextSize(15);
        forTextLinLay.addView(medicamentSpecification);
        medicamentSpecification.setMaxLines(1); //setSingleLine(true)
        medicamentSpecification.setEllipsize(TextUtils.TruncateAt.END);
        //*Layout для дозировки
        LinearLayout forTextLinLay1 = new LinearLayout(this);
        forTextLinLay1.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams forTextLinLayParams1 = new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //*Layout для Времени приёма
        LinearLayout forTextLinLay2 = new LinearLayout(this);
        forTextLinLay2.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams forTextLinLayParams2 = new
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        int textWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()); //convert dp to pixel
        ViewGroup.LayoutParams TextParams = new
                ViewGroup.LayoutParams(textWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textDosageName = new TextView(this);
        textDosageName.setText(R.string.medicament_dosage);
        TextView textDosage = new TextView(this);
        textDosage.setText("0 " + R.string.medicament_dosage_ru); //Дозировка в мг

        TextView textDataName = new TextView(this);
        textDataName.setText(R.string.medicament_data);
        TextView textData = new TextView(this);
        textData.setText("0 00"); //Время до следубщего приема.

        forTextLinLay1.addView(textDosageName, TextParams);
        forTextLinLay1.addView(textDataName);

        forTextLinLay2.addView(textDosage, TextParams);
        forTextLinLay2.addView(textData);

        forTextLinLay.addView(forTextLinLay1, forTextLinLayParams1);
        forTextLinLay.addView(forTextLinLay2, forTextLinLayParams2);

        return true;
    }

}
*/