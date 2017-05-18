package com.vectortwo.healthkeeper.activities.menu_settings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.activities.PrefManager;
import com.vectortwo.healthkeeper.activities.medicament.MList;
import com.vectortwo.healthkeeper.activities.medicament.MListAdapter;

import java.util.List;

/**
 * Created by skaper on 24.04.17.
 */
public class ElementsMListAdapter extends RecyclerView.Adapter<ElementsMListAdapter.MyViewHolder>{
    private Context contextThis;
    private List<ElementsList> elementsList;
    private PrefManager prefManager;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private Switch aSwitch;
        public MyViewHolder(View view){
            super(view);
            prefManager = new PrefManager(contextThis);
            imageView = (ImageView) view.findViewById(R.id.elements_image);
            aSwitch = (Switch) view.findViewById(R.id.elements_switch);
            aSwitch.setOnCheckedChangeListener(new CheckedChangeListener(this));

        }
    }

    public ElementsMListAdapter(Context context, List<ElementsList> _elementsLists){
        this.contextThis = context;
        this.elementsList = _elementsLists;
    }

    private class CheckedChangeListener implements Switch.OnCheckedChangeListener{
        private MyViewHolder myViewHolder;
        private CheckedChangeListener(MyViewHolder _context){
            super();
            this.myViewHolder = _context;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //boolean status = buttonView.isChecked();

            if(buttonView.getText().equals(contextThis.getResources().getString(R.string.medicament))){
                //prefManager.setMedicametVisible(isChecked);
                buttonView.setChecked(true);

            }else if(buttonView.getText().equals(contextThis.getResources().getString(R.string.blood_sugar))){
                prefManager.setBloodSugarVisible(isChecked);
            }else if(buttonView.getText().equals(contextThis.getResources().getString(R.string.walking))){
                prefManager.setWalkingVisible(isChecked);
            }else if(buttonView.getText().equals(contextThis.getResources().getString(R.string.pressure))){
                prefManager.setPressureVisible(isChecked);
            }else if(buttonView.getText().equals(contextThis.getResources().getString(R.string.weight))){
                prefManager.setWeightVisible(isChecked);
            }else if(buttonView.getText().equals(contextThis.getResources().getString(R.string.aqua))){
                prefManager.setAquaVisible(isChecked);
            }else if(buttonView.getText().equals(contextThis.getResources().getString(R.string.sleep))){
                prefManager.setSleepVisible(isChecked);
            }

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_card, parent, false);


        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ElementsList elemList = elementsList.get(position);
        holder.aSwitch.setText(elemList.getName());
        holder.aSwitch.setChecked(elemList.getStatus());
        if(elemList.getName().equals(contextThis.getResources().getString(R.string.medicament))){
            holder.aSwitch.setAlpha(.5f);
        }
        holder.imageView.setImageResource(elemList.getImage());


    }

    @Override
    public int getItemCount() {
        return elementsList.size();
    }



}
