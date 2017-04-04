package com.vectortwo.healthkeeper.activities.medicament;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vectortwo.healthkeeper.R;

import java.util.List;

/**
 * Created by skaper on 01.04.17.
 */
public class MListAdapter extends RecyclerView.Adapter<MListAdapter.MyViewHolder> {
    private Context mContext;
    private List<MList> medicamentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, dose, nextTime;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.mcard_name);
            description = (TextView) view.findViewById(R.id.mcard_description);
            dose = (TextView) view.findViewById(R.id.mcard_dose);
            nextTime = (TextView) view.findViewById(R.id.mcard_nexttime);
        }
    }

    public MListAdapter(Context mContext, List<MList> medicamentList){
        this.mContext = mContext;
        this.medicamentList = medicamentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.m_cardview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MList medlist = medicamentList.get(position);
        holder.name.setText(medlist.getmName());
        holder.nextTime.setText(medlist.getmNextTime());
        holder.dose.setText(medlist.getmDose());// + getResources().getString(R.string.medicament_dosage_type));
        holder.description.setText(medlist.getmDescription());


    }

    @Override
    public int getItemCount() {
        return medicamentList.size();
    }
}
