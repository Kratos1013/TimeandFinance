package com.krintos.timeandfinance.Fragments.Finance.Category.ActivitiesMenu;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.Fragments.Finance.Category.Add.add_to_activities;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesMain extends Fragment {
    private FloatingActionButton add;
    private ListView listView;
    private String  pickedDate,mday,category;
    private Calendar calendar;
    private int day,month,mmonth,year,myear;
    private FinanceSQLiteHandler db;
    private ArrayList<String> activityname = new ArrayList<>();
    private ArrayList<String> activitydescriptions = new ArrayList<>();
    private ArrayList<String> activitydate = new ArrayList<>();
    private ArrayList<String> activityinitialprice = new ArrayList<>();
    private ArrayList<String> activitypricetoday = new ArrayList<>();
    private ArrayList<String> activityId = new ArrayList<>();
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
        setdate();
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ID = ((TextView) view.findViewById(R.id.layoutid)).getText().toString();
                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                String description = ((TextView) view.findViewById(R.id.description)).getText().toString();
                String initialprice = ((TextView) view.findViewById(R.id.initialprice)).getText().toString();
                String pricetoday = ((TextView) view.findViewById(R.id.pricetoday)).getText().toString();
                String date = ((TextView) view.findViewById(R.id.date)).getText().toString();
                itemselected(ID,name,description,initialprice,pricetoday,date);


            }
        });
        return rootView;
    }

    private void itemselected(final String id, String name, String description, String initialprice, String pricetoday, String date) {
        final AlertDialog.Builder change = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.fragment_add_to_activities,null);
        change.setView(dialog);
        Button delete = (Button) dialog.findViewById(R.id.cancel);
        Button save = (Button) dialog.findViewById(R.id.save);
        final EditText etname = (EditText) dialog.findViewById(R.id.activityname);
        final EditText etdescript = (EditText) dialog.findViewById(R.id.activitydescription);
        final TextView showdate = (TextView) dialog.findViewById(R.id.showdate);
        ImageButton picker = (ImageButton) dialog.findViewById(R.id.pickDate);
        final EditText etinitialprice = (EditText) dialog.findViewById(R.id.initialprice);
        final EditText etpricetoday = (EditText) dialog.findViewById(R.id.pricetoday);
        etname.setText(name);
        etdescript.setText(description);
        showdate.setText(date);
        etinitialprice.setText(initialprice);
        etpricetoday.setText(pricetoday);
        delete.setText(getString(R.string.delete));
        final AlertDialog alertDialog =  change.create();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer del = db.deletedata(FinanceSQLiteHandler.TABLE_NAME_ACTIVIES,id);
                if(del>0){
                    Toast.makeText(getActivity(), ""+getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    setlistview();
                    alertDialog.cancel();
                }else {
                    Toast.makeText(getActivity(), ""+getString(R.string.notdeleted), Toast.LENGTH_SHORT).show();
                }
            }
        });
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int setyear, int setmonth, int dayOfMonth) {
                        setmonth=setmonth+1;
                        setyear = setyear-2000;
                        String mmday;
                        if (dayOfMonth<=9){
                            mmday = "0"+dayOfMonth;
                        }else {
                            mmday = ""+dayOfMonth;
                        }

                        if (setmonth<=9){
                            pickedDate = mmday+"."+"0"+setmonth+"."+setyear;
                            showdate.setText(pickedDate);


                        }else {
                            pickedDate = mmday+"."+setmonth+"."+setyear;
                            showdate.setText(pickedDate);

                        }
                    }
                }, year,month, day);
                datePickerDialog.show();
            }
        });
        save.setText(getString(R.string.save));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sname = etname.getText().toString().trim();
                String sdesc = etdescript.getText().toString().trim();
                String sdate = showdate.getText().toString().trim();
                String sinit = etinitialprice.getText().toString().trim();
                String stodayprice = etpricetoday.getText().toString().trim();
                if (sname.equals("")||sdesc.equals("")||sdate.equals("")||sinit.equals("")||
                        stodayprice.equals("")){
                    Toast.makeText(getActivity(), ""+getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                }else {
                    boolean update = db.updateactivities(id,sname,sdesc,sdate,sinit,stodayprice);
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
        activityId.clear();
        activityname.clear();
        activitydescriptions.clear();
        activityinitialprice.clear();
        activitypricetoday.clear();
        activitydate.clear();
        String table = FinanceSQLiteHandler.TABLE_NAME_ACTIVIES;
        Cursor result = db.getAlldatas(table);
        if (result.getCount() > 0){
            while (result.moveToNext()){
                listView.setVisibility(View.VISIBLE);

                String Id = result.getString(0);
                String name = result.getString(1);
                String description = result.getString(2);
                String date = result.getString(3);
                String initialprice = result.getString(4);
                String pricetoday = result.getString(5);
                activityId.add(Id);
                activityname.add(name);
                activitydescriptions.add(description);
                activitydate.add(date);
                activityinitialprice.add(initialprice);
                activitypricetoday.add(pricetoday);
            }
            ActivityMainHelper activityMainHelper = new ActivityMainHelper(getActivity(),activityname,activitydescriptions,
                    activitydate,activityinitialprice,activitypricetoday,activityId);
            activityMainHelper.notifyDataSetChanged();
            listView.setAdapter(activityMainHelper);
        }else {
            //nothing found
            listView.setVisibility(View.GONE);
        }

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
