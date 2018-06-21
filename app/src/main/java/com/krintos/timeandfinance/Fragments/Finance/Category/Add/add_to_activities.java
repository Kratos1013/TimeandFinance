package com.krintos.timeandfinance.Fragments.Finance.Category.Add;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class add_to_activities extends Fragment {


    public add_to_activities() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_to_activities, container, false);
        return rootView;
    }

}
