package com.vectortwo.healthkeeper.activities.walking;

/**
 * Created by skaper on 30.03.17.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vectortwo.healthkeeper.R;
import com.vectortwo.healthkeeper.widgets.FitDoughnut;

/**
 * Created by skaper on 30.03.17.
 */

public class Walking_main_fragment extends Fragment {
    public FitDoughnut doughnut;
    public Walking_main_fragment() {
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

        View view = inflater.inflate(R.layout.walking_main_tab, container, false);
        doughnut = (FitDoughnut) view.findViewById(R.id.walking_doughnut);

        float time = 45.f;
        doughnut.animateSetPercent(time);
        doughnut.animateSetPercent(time/60.f * 100.f);
        doughnut.setText("45Min");

        return view;//inflater.inflate(R.layout.walking_main_tab, container, false);

    }

}