package com.krintos.timeandfinance.Fragments.Finance.Category;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.krintos.timeandfinance.Fragments.Finance.Sms.income;
import com.krintos.timeandfinance.Fragments.Finance.Sms.spent;
import com.krintos.timeandfinance.Fragments.Helpers.SectionsPageAdapter;
import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Finance_Category_main extends Fragment {

    private SectionsPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public Finance_Category_main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_finance__category_main, container, false);
        mViewPager =  rootView.findViewById(R.id.viewpager);
        tabLayout =  rootView.findViewById(R.id.tabs);
        mSectionPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        return rootView;
    }
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new DaylyFinance(), "День");
        adapter.addFragment(new MonthlyFinance(), "Месяц");
        adapter.addFragment(new YearlyFinance(), "Год");
        viewPager.setAdapter(adapter);
    }

}
