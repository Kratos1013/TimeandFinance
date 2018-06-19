package com.krintos.timeandfinance.Fragments.Finance;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krintos.timeandfinance.Fragments.Finance.Category.Finance;
import com.krintos.timeandfinance.Fragments.Finance.Sms.SMSchecker;
import com.krintos.timeandfinance.Fragments.Finance.Statistics.Finance_Statistics_Main_Frame;
import com.krintos.timeandfinance.Fragments.Helpers.FinancePageAdapter;
import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Finance_Main_Page extends Fragment {
    private int[] icons = {
            R.drawable.category,
            R.drawable.statistics,
            R.drawable.ic_credit_card_black_24dp
    };
    private FinancePageAdapter mFinancePageAdaper;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public Finance_Main_Page() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finance__main__page, container, false);
        mViewPager =  rootView.findViewById(R.id.viewpager);
        tabLayout =  rootView.findViewById(R.id.tabs);
        mFinancePageAdaper = new FinancePageAdapter(getActivity().getSupportFragmentManager());
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
        return rootView;
    }
    private void setupViewPager(ViewPager viewPager){
        FinancePageAdapter adapter = new FinancePageAdapter(getChildFragmentManager());
        adapter.addFragment(new Finance(), "Finance");
        adapter.addFragment(new Finance_Statistics_Main_Frame(), "Statistics");
        adapter.addFragment(new SMSchecker(), "Card");
        viewPager.setAdapter(adapter);
    }
}
