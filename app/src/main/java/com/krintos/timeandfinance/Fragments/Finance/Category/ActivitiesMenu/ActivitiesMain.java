package com.krintos.timeandfinance.Fragments.Finance.Category.ActivitiesMenu;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krintos.timeandfinance.Fragments.Finance.Category.Add.add_to_activities;
import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesMain extends Fragment {
    private FloatingActionButton add;

    public ActivitiesMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activities_main, container, false);
        add = rootView.findViewById(R.id.addnew);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ActivityAddNew = new add_to_activities();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, ActivityAddNew);
                ft.addToBackStack("ActiviesAdd");
                ft.commit();
            }
        });
        return rootView;
    }

}
