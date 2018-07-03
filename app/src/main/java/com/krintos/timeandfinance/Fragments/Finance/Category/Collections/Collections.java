package com.krintos.timeandfinance.Fragments.Finance.Category.Collections;


import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.Fragments.Finance.Category.Add.add_to_collections;
import com.krintos.timeandfinance.Fragments.Finance.Category.PassivesMenu.PassivesMain;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Collections extends Fragment {
    private FloatingActionButton addnew;
    private ListView listview;
    private FinanceSQLiteHandler db;
    private String  pickedDate,mday,category;
    private Calendar calendar;
    private int day,month,mmonth,year,myear;
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
        setdate();
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
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ids = ((TextView) view.findViewById(R.id.tvids)).getText().toString().trim();
                String aim = ((TextView) view.findViewById(R.id.aim)).getText().toString().trim();
                String amount = ((TextView) view.findViewById(R.id.full)).getText().toString().trim();
                String amountpermonth = ((TextView) view.findViewById(R.id.permonth)).getText().toString().trim();
                String alreadycollected = ((TextView) view.findViewById(R.id.now)).getText().toString().trim();
                itemselected(ids,aim,amount,amountpermonth,alreadycollected);
            }
        });
        return rootView;
    }

    private void itemselected(final String ids, String aim, String amount, String amountpermonth, String alreadycollected) {
        AlertDialog.Builder change = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.fragment_add_to_collections,null);
        change.setView(dialog);
        Button delete = (Button) dialog.findViewById(R.id.cancel);
        delete.setText(getString(R.string.delete));
        Button save = (Button) dialog.findViewById(R.id.save);
        final EditText eaim = (EditText) dialog.findViewById(R.id.aim);
        final EditText eamount = (EditText) dialog.findViewById(R.id.collectionsfull);
        final EditText eamountpermonth = (EditText) dialog.findViewById(R.id.collectionspermonth);
        final EditText eamountnow = (EditText) dialog.findViewById(R.id.collectionsnow);
        eaim.setText(aim);
        eamount.setText(amount);
        eamountpermonth.setText(amountpermonth);
        eamountnow.setText(alreadycollected);
        final AlertDialog alertDialog = change.create();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer del = db.deletedata(FinanceSQLiteHandler.TABLE_NAME_COLLECTIONS,ids);
                if(del>0){
                    Toast.makeText(getActivity(), ""+getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    setlistview();
                    alertDialog.cancel();
                }else {
                    Toast.makeText(getActivity(), ""+getString(R.string.notdeleted), Toast.LENGTH_SHORT).show();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saim = eaim.getText().toString().trim();
                String sfull = eamount.getText().toString().trim();
                String sper = eamountpermonth.getText().toString().trim();
                String snow = eamountnow.getText().toString().trim();
                if (saim.equals("")||sfull.equals("")||sper.equals("")||snow.equals("")){
                    Toast.makeText(getActivity(), ""+getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                }else {
                    boolean update = db.updatecollections(ids,saim,sfull,sper,snow);
                    if (update){
                        Toast.makeText(getActivity(), ""+getString(R.string.updated), Toast.LENGTH_SHORT).show();
                        setlistview();
                        alertDialog.cancel();
                    }else {
                        Toast.makeText(getActivity(), ""+getString(R.string.notupdated), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        alertDialog.show();
    }

    private void setlistview() {
        ArrayList<String> aim = new ArrayList<>();
        ArrayList<String> full = new ArrayList<>();
        ArrayList<String> permonth = new ArrayList<>();
        ArrayList<String> now = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();

        Cursor collections = db.getAlldatas(FinanceSQLiteHandler.TABLE_NAME_COLLECTIONS);
        if (collections.getCount() > 0){
            while (collections.moveToNext()){
                listview.setVisibility(View.VISIBLE);
                String id = collections.getString(0);
                String gaim = collections.getString(1);
                String gfull = collections.getString(2);
                String gnow = collections.getString(3);
                String gpermonth = collections.getString(4);
                ids.add(id);
                aim.add(gaim);
                full.add(gfull);
                permonth.add(gpermonth);
                now.add(gnow);
            }
        }else {
            // TODO: 6/25/18 nothing found in collections
            listview.setVisibility(View.GONE);
        }
        CollectionsHelper collectionsHelper = new CollectionsHelper(getActivity(),aim,full,permonth,now,ids);
        collectionsHelper.notifyDataSetChanged();
        listview.setAdapter(collectionsHelper);
    }
    private String setdate() {

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day<=9){
            mday = "0"+day;
        }else {
            mday=""+day;
        }
        month = calendar.get(Calendar.MONTH);
        mmonth=month+1;
        year = calendar.get(Calendar.YEAR);
        myear = year-2000;
        if (month<=9){
            pickedDate = mday+"."+"0"+mmonth+"."+myear;
        }else {
            pickedDate = mday+"."+mmonth+"."+myear;
        }
        return pickedDate;


    }
}
