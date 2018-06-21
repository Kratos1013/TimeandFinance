package com.krintos.timeandfinance.Fragments.Finance.Category.Add;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class add_to_activities extends Fragment {
    private EditText name, description, initialprice,pricetodaty;
    private TextView date;
    private ImageButton pickDate;
    private Button cancel,save;
    private String  pickedDate,mday,category;
    private Calendar calendar;
    private int day,month,mmonth,year,myear;
    private  FinanceSQLiteHandler db;


    public add_to_activities() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_to_activities, container, false);
        db = new FinanceSQLiteHandler(getActivity());
        name = rootView.findViewById(R.id.activityname);
        description = rootView.findViewById(R.id.activitydescription);
        initialprice = rootView.findViewById(R.id.initialprice);
        pricetodaty = rootView.findViewById(R.id.pricetoday);
        date = rootView.findViewById(R.id.showdate);
        pickDate = rootView.findViewById(R.id.pickDate);
        cancel = rootView.findViewById(R.id.cancel);
        save = rootView.findViewById(R.id.save);
        setdate();
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityname = name.getText().toString().trim();
                String activitydescription = description.getText().toString().trim();
                String activitydate = date.getText().toString().trim();
                String activitypriceinitial = initialprice.getText().toString().trim();
                String activitypricetoday = pricetodaty.getText().toString().trim();
                if (activityname.equals("")||activitydescription.equals("")||activitydate.equals("")||
                        activitypriceinitial.equals("")||activitypricetoday.equals("")){
                    Toast.makeText(getActivity(), ""+getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                }else {
                    addactivities(activityname,activitydescription,activitydate,activitypriceinitial,activitypricetoday);
                }
            }
        });
        return rootView;
    }

    private void addactivities(String activityname, String activitydescription, String activitydate, String activitypriceinitial, String activitypricetoday) {
        boolean addtoactivity =  db.inserttoactivity(activityname,activitydescription,activitydate,activitypriceinitial,activitypricetoday);
        if (addtoactivity){
            Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();

        }else {
            Toast.makeText(getActivity(), "Not Added", Toast.LENGTH_SHORT).show();
        }
    }

    private void setdate() {

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
            date.setText(pickedDate);


    }

    private void datepicker() {
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
                    date.setText(pickedDate);


                }else {
                    pickedDate = mmday+"."+setmonth+"."+setyear;
                    date.setText(pickedDate);

                }
            }
        }, year,month, day);
        datePickerDialog.show();
    }

}
