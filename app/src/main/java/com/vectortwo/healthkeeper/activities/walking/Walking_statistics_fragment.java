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

/**
 * Created by skaper on 30.03.17.
 */

public class Walking_statistics_fragment extends Fragment {

    public Walking_statistics_fragment() {
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
        return inflater.inflate(R.layout.walking_statistics_tab, container, false);
    }

}
