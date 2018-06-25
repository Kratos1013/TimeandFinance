package com.krintos.timeandfinance.Fragments.Finance.Category.Collections;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.Fragments.Finance.Category.Add.add_to_collections;
import com.krintos.timeandfinance.Fragments.Finance.Category.PassivesMenu.PassivesMain;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Collections extends Fragment {
    private FloatingActionButton addnew;
    private ListView listview;
    private FinanceSQLiteHandler db;
    public Collections() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_collections, container, false);
        db = new FinanceSQLiteHandler(getContext());
        listview = rootView.findViewById(R.id.collectionslistview);
        setlistview();
        addnew = rootView.findViewById(R.id.addnewtocollections);
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment collect = new add_to_collections();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, collect);
                ft.addToBackStack("Collectionadd");
                ft.commit();
            }
        });
        return rootView;
    }

    private void setlistview() {
        ArrayList<String> aim = new ArrayList<>();
        ArrayList<String> full = new ArrayList<>();
        ArrayList<String> permonth = new ArrayList<>();
        ArrayList<String> now = new ArrayList<>();

        Cursor collections = db.getAlldatas(FinanceSQLiteHandler.TABLE_NAME_COLLECTIONS);
        if (collections.getCount() > 0){
            while (collections.moveToNext()){
                String gaim = collections.getString(1);
                String gfull = collections.getString(2);
                String gnow = collections.getString(3);
                String gpermonth = collections.getString(4);
                aim.add(gaim);
                full.add(gfull);
                permonth.add(gpermonth);
                now.add(gnow);
            }
        }else {
            // TODO: 6/25/18 nothing found in collections
        }
        CollectionsHelper collectionsHelper = new CollectionsHelper(getActivity(),aim,full,permonth,now);
        collectionsHelper.notifyDataSetChanged();
        listview.setAdapter(collectionsHelper);
    }

}
