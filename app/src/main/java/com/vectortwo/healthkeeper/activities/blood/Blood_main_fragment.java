package com.vectortwo.healthkeeper.activities.blood;

/**
 * Created by skaper on 30.03.17.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vectortwo.healthkeeper.R;

public class Blood_main_fragment extends Fragment {

    public Blood_main_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pressure_statistics_tab, container, false);
    }

}