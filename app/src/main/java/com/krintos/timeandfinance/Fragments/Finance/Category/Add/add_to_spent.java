
package com.krintos.timeandfinance.Fragments.Finance.Category.Add;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class add_to_spent extends Fragment {
    public FinanceSQLiteHandler db;
    private EditText description,amount, new_category;
    private TextView date;
    private Spinner spinner;
    private Button cancel, add;
    private ImageButton pickdate;
    private Calendar calendar;
    private int day,month,mmonth,year,myear;
    private String  pickedDate,mday,category;
    private List<String> array = new ArrayList<String>();
    public add_to_spent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_to_spent, container, false);
        db = new FinanceSQLiteHandler(getActivity().getApplicationContext());
        ArrayAdapter<String > adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array);

        description = rootView.findViewById(R.id.description);
        pickdate = rootView.findViewById(R.id.pickDate);
        spinner =  rootView.findViewById(R.id.category);
        amount =  rootView.findViewById(R.id.amount);
        new_category =  rootView.findViewById(R.id.add_new_category);
        date =  rootView.findViewById(R.id.showdate);
        cancel =  rootView.findViewById(R.id.cancel);
        add =  rootView.findViewById(R.id.add);
        setdate();

        Cursor result = db.getCategoryNames(FinanceSQLiteHandler.TABLE_CATEGORIES_Spent);

        if (result.getCount() == 0){
            spinner.setVisibility(View.GONE);
            new_category.setVisibility(View.VISIBLE);
        }
        while (result.moveToNext()){
            String categorynames = result.getString(1);
            array.add(categorynames);
        }
        if (result.getCount()>0){
            array.add(getString(R.string.add_new_category));
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String empty = (String) parent.getItemAtPosition(position);
                if (empty.equals(getString(R.string.add_new_category))) {
                    spinner.setVisibility(View.GONE);
                    new_category.setVisibility(View.VISIBLE);

                }else {
                    category = (String ) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = array.get(0);

            }
        });
        pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnew();
            }
        });
        return rootView;
    }

    private void addnew() {
        String categoryname = new_category.getText().toString().trim();
        String descript = description.getText().toString();
        String price = amount.getText().toString().trim();
        String moment = date.getText().toString().trim();
        if ( descript.equals("") || price.equals("")) {
            Toast.makeText(getContext(), "Can't be empty", Toast.LENGTH_SHORT).show();

        } else {
            if (new_category.getVisibility() == View.VISIBLE && spinner.getVisibility() == View.GONE  ) {
                if (categoryname.equals("")){
                    Toast.makeText(getContext(), "Can't be empty", Toast.LENGTH_SHORT).show();
                }else {
                    price = "- " + price;
                    boolean insert = db.inserttocategory(categoryname, descript, moment, price, FinanceSQLiteHandler.Table_Name_spent, "noicon");
                    if (insert) {
                        boolean add = db.addcategorynamewithicon(categoryname, "noicon", FinanceSQLiteHandler.TABLE_CATEGORIES_Spent);
                        if (add) {
                            View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                    } else {
                        Toast.makeText(getContext(), "Not added", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                price = "- " + price;
                boolean insertto = db.inserttocategory(category, descript, moment, price, FinanceSQLiteHandler.Table_Name_spent, "noicon");
                if (insertto) {
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "Not Added", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }

    private void pick() {
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

    private void setdate() {
        calendar =  Calendar.getInstance();
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

}
