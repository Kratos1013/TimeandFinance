package com.krintos.timeandfinance.Fragments.Finance.Category.PassivesMenu;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.Fragments.Finance.Category.Add.add_to_passives;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassivesMain extends Fragment {
    private FloatingActionButton addnew;
    private ListView listView;
    private FinanceSQLiteHandler db;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private ArrayList<String> pricespermonth = new ArrayList<>();
    private ArrayList<String> enddates = new ArrayList<>();
    public PassivesMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_passives_main, container, false);
        addnew = rootView.findViewById(R.id.addnew);
        listView = rootView.findViewById(R.id.listviewforpassive);
        db = new FinanceSQLiteHandler(getContext());
        setlistview();
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment PassiveAddNew = new add_to_passives();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, PassiveAddNew);
                ft.addToBackStack("PassivesAdd");
                ft.commit();
            }
        });
        return rootView;
    }

    private void setlistview() {
        names.clear();
        descriptions.clear();
        prices.clear();
        pricespermonth.clear();
        enddates.clear();
        String table = FinanceSQLiteHandler.TABLE_NAME_PASSIVES;
        Cursor result = db.getAlldatas(table);
        if (result.getCount() > 0 ){
            while (result.moveToNext()){
               String name = result.getString(1);
               String description = result.getString(2);
               String price = result.getString(3);
               String pricepermonth = result.getString(4);
               String enddate = result.getString(5);
                names.add(name);
                descriptions.add(description);
                prices.add(price);
                pricespermonth.add(pricepermonth);
                enddates.add(enddate);
            }
            PassivesMainHelper passivesMainHelper =  new PassivesMainHelper(getActivity(),
                    names,descriptions,prices,pricespermonth,enddates);
            passivesMainHelper.notifyDataSetChanged();
            listView.setAdapter(passivesMainHelper);
        }else {
            //nothing found
        }
    }

}
