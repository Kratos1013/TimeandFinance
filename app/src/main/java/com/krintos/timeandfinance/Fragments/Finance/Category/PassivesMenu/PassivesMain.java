package com.krintos.timeandfinance.Fragments.Finance.Category.PassivesMenu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassivesMain extends Fragment {


    public PassivesMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passives_main, container, false);
    }

}
