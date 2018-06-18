package com.krintos.timeandfinance.Fragments.Finance.Category.Add;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krintos.timeandfinance.Fragments.Helpers.SectionsPageAdapter;
import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Custom_add_finance extends Fragment {
    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public Custom_add_finance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_custom_add_finance, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        sectionsPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new add_to_spent(),"Расходы");
        adapter.addFragment(new add_to_income(),"Доходы");
        viewPager.setAdapter(adapter);
    }
}
