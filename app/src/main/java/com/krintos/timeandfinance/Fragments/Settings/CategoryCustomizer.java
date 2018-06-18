package com.krintos.timeandfinance.Fragments.Settings;


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
public class CategoryCustomizer extends Fragment {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private SectionsPageAdapter mSectionPageAdapter;

    public CategoryCustomizer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_category_customizer, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        mSectionPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        return rootView;
    }
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new TimeCategory(), getString(R.string.timing));
        adapter.addFragment(new FinanceCategory(), getString(R.string.finance));
        viewPager.setAdapter(adapter);
    }

}
