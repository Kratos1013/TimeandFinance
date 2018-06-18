package com.krintos.timeandfinance.Fragments.Settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {
    LinearLayout cust_cat;

    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        cust_cat =  (LinearLayout) rootView.findViewById(R.id.customize_categories);
        cust_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CategoryCustomizer();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack("categorycustomizer");
                ft.commit();
            }
        });

        return rootView;
    }

}
