package com.krintos.timeandfinance.Fragments.Finance.Category.ActivitiesMenu;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.Fragments.Finance.Category.Add.add_to_activities;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesMain extends Fragment {
    private FloatingActionButton add;
    private ListView listView;
    private FinanceSQLiteHandler db;
    private ArrayList<String> activityname = new ArrayList<>();
    private ArrayList<String> activitydescriptions = new ArrayList<>();
    private ArrayList<String> activitydate = new ArrayList<>();
    private ArrayList<String> activityinitialprice = new ArrayList<>();
    private ArrayList<String> activitypricetoday = new ArrayList<>();
    public ActivitiesMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activities_main, container, false);
        add = rootView.findViewById(R.id.addnew);
        listView = rootView.findViewById(R.id.listviewforactivity);
        db = new FinanceSQLiteHandler(getContext());
        setlistview();
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

    private void setlistview() {
        activityname.clear();
        activitydescriptions.clear();
        activityinitialprice.clear();
        activitypricetoday.clear();
        activitydate.clear();
        String table = FinanceSQLiteHandler.TABLE_NAME_ACTIVIES;
        Cursor result = db.getAlldatas(table);
        if (result.getCount() > 0){
            while (result.moveToNext()){
                String name = result.getString(1);
                String description = result.getString(2);
                String date = result.getString(3);
                String initialprice = result.getString(4);
                String pricetoday = result.getString(5);
                activityname.add(name);
                activitydescriptions.add(description);
                activitydate.add(date);
                activityinitialprice.add(initialprice);
                activitypricetoday.add(pricetoday);
            }
            ActivityMainHelper activityMainHelper = new ActivityMainHelper(getActivity(),activityname,activitydescriptions,
                    activitydate,activityinitialprice,activitypricetoday);
            activityMainHelper.notifyDataSetChanged();
            listView.setAdapter(activityMainHelper);
        }else {
            //nothing found
        }

    }

}
