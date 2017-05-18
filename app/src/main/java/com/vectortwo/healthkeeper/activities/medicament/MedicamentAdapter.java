package com.vectortwo.healthkeeper.activities.medicament;

/**
 * Created by skaper on 10.05.17.
 */
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.adapters.RecyclerCursorAdapter;
import com.vectortwo.healthkeeper.data.ScheduleDateViewer;
import com.vectortwo.healthkeeper.data.db.DBContract;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ilya on 05/05/2017.
 */
public class MedicamentAdapter extends RecyclerCursorAdapter<MedicamentAdapter.DrugHolder> {
    public Context contextThis;
    private List<MList> medicamentList;

    public MedicamentAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        contextThis =context;
    }

    public class DrugHolder extends RecyclerView.ViewHolder implements CardView.OnClickListener{
        public TextView nameEntry;
        private TextView name, description, dose, nextTime;
        private CardView mcard_card;
        //private Context context1;

        public DrugHolder(View view) {
            //super(itemView);
            super(view);
            name = (TextView) view.findViewById(R.id.mcard_name);
            description = (TextView) view.findViewById(R.id.mcard_description);
            dose = (TextView) view.findViewById(R.id.mcard_dose);
            nextTime = (TextView) view.findViewById(R.id.mcard_nexttime);
            mcard_card = (CardView) view.findViewById(R.id.mcard_card);
            mcard_card.setOnClickListener(this);
            //nameEntry = (TextView) itemView.findViewById(R.id.name_entry);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            long id = MedicamentAdapter.this.getItemId(pos);
            Log.w("DRUG ID", " "+id);
        }
    }


    @Override
    public void onBindViewHolder(final DrugHolder viewHolder, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(DBContract.Drug.TITLE));
        //String description = cursor.getString(cursor.getColumnIndex(DBContract.Drug.DESCRIPTION));
        String description = cursor.getString(cursor.getColumnIndex(DBContract.Drug.DESCRIPTION));

        int itemPosition = viewHolder.getAdapterPosition();
        long id = MedicamentAdapter.this.getItemId(itemPosition);
        ScheduleDateViewer sdv = new ScheduleDateViewer(contextThis, new ScheduleDateViewer.IResultHandler() {
            public void onResult(Calendar outCalendar){
                int dayOfWeek = outCalendar.get(Calendar.DAY_OF_WEEK);
                int hour = outCalendar.get(Calendar.HOUR);
                int minute = outCalendar.get(Calendar.MINUTE);
                int am_pmInt = outCalendar.get(Calendar.AM_PM);
                viewHolder.nextTime.setText(hour+":"+minute+" "+dayOfWeek + " " + am_pmInt);
            }
        });
        //sdv.execute((int)id);

        viewHolder.name.setText(name);
        viewHolder.description.setText(description);


    }

    @Override
    public DrugHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.m_cardview, parent, false);

        return new DrugHolder(itemView);

    }
}