package com.krintos.timeandfinance.Fragments.Finance.Category;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.Fragments.Finance.Category.Add.Custom_add_finance;
import com.krintos.timeandfinance.Fragments.Finance.Finance;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DaylyFinance extends Fragment {
    private Calendar mCurrentDate;
    private int day,month,mmonth,year,myear;
    private String  pickedDate,mday,categoryName,idofitem,table_name_income="categoriesforincome";
    private TextView tdate,tincome,tspent,total,nodata;
    private ListView mlistView;
    private float totalincome = (float) 0.00;
    private float totalspent = (float)  0.00;
    private float budget = (float) 0.00;
    public FinanceSQLiteHandler db;
    private ArrayList<String> category = new ArrayList<String>();
    private ArrayList<String> description = new ArrayList<String>();
    private ArrayList<String> amount = new ArrayList<String>();
    private ArrayList<String> table = new ArrayList<String>();
    private ArrayList<Integer> colors = new ArrayList<Integer>();
    private ArrayList<String> icons = new ArrayList<>();
    private LinearLayout budgetzone;
    private DailyHelper dailyHelper;
    private Drawable budget_color;
    private FloatingActionButton add;

    public DaylyFinance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dayly_finance, container, false);
        db = new FinanceSQLiteHandler(getActivity().getApplicationContext());
        tdate = (TextView) rootView.findViewById(R.id.date);
        tincome = (TextView) rootView.findViewById(R.id.incomemoney);
        tspent = (TextView) rootView.findViewById(R.id.spentmoney);
        total = (TextView) rootView.findViewById(R.id.total);
        mlistView = (ListView) rootView.findViewById(R.id.listviewforday);
        nodata = (TextView) rootView.findViewById(R.id.nothingfound);
        budgetzone = (LinearLayout) rootView.findViewById(R.id.budget_zone);
        add = (FloatingActionButton) rootView.findViewById(R.id.add);
        budget_color = budgetzone.getBackground();
        setdate();
        setincome(tdate.getText().toString());
        setspent(tdate.getText().toString());
        showall(tdate.getText().toString());
        settotal();
        tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick();
            }
        });
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = ((TextView) view.findViewById(R.id.description)).getText().toString();
                String amount = ((TextView) view.findViewById(R.id.amount)).getText().toString();
                String category = ((TextView) view.findViewById(R.id.category)).getText().toString();
                Bitmap icon = ((ImageView) view.findViewById(R.id.icons)).getDrawingCache();
                String table;
                String cat;
                Pattern check = Pattern.compile("-");
                Matcher m = check.matcher(amount);
                if (m.find()){
                    table = FinanceSQLiteHandler.Table_Name_spent;
                    cat = FinanceSQLiteHandler.TABLE_CATEGORIES_Spent;
                }else {
                    table = FinanceSQLiteHandler.Table_Name_income;
                    cat = FinanceSQLiteHandler.TABLE_CATEGORIES_Income;
                }

                changeitem(icon,description,amount,category, table,cat);
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
        Fragment fragment = new Custom_add_finance();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("custom_add_finance");
        ft.commit();
    }

    private void changeitem(Bitmap icon, final String description, final String amount, final String category, final String table, String cat) {
        final ArrayList<String> cats = new ArrayList<>();


        AlertDialog.Builder change_dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.finance_item_selected, null);
        change_dialog.setView(dialog);
        final LinearLayout ask =  dialog.findViewById(R.id.ask);
        final LinearLayout changelayout= dialog.findViewById(R.id.changelayout);
        ImageView delete =  dialog.findViewById(R.id.delete);
        ImageView change =  dialog.findViewById(R.id.change);
        Cursor categories = db.getCategoryNames(cat);

        while (categories.moveToNext()){
            String catNames = categories.getString(1);
            cats.add(catNames);
        }
        final ArrayAdapter<String > adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final AlertDialog alertDialog = change_dialog.create();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Cursor result = db.IsDataExist(description,pickedDate,amount,table);
            if (result.moveToNext()){
               String  id = result.getString(0);
                Integer delete = db.deletedata(table,id);
                if (delete>0){
                    alertDialog.dismiss();
                    showall(pickedDate);
                    setspent(pickedDate);
                    setincome(pickedDate);
                    settotal();

                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                }
                result.close();

            }
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) dialog.findViewById(R.id.description)).setText(description);
                ((EditText) dialog.findViewById(R.id.amount)).setText(amount);
                Cursor result = db.IsDataExist(description,pickedDate,amount,table);
                if (result.moveToNext()){
                   idofitem = result.getString(0);
                    result.close();

                }
                ask.setVisibility(View.GONE);
                changelayout.setVisibility(View.VISIBLE);
                final ImageButton pickaDate = dialog.findViewById(R.id.pickDate);
                final TextView showdate = dialog.findViewById(R.id.showdate);
                showdate.setText(pickedDate);
                Button save = dialog.findViewById(R.id.save);
                Button cancel = dialog.findViewById(R.id.cancel);
                Spinner spinner = dialog.findViewById(R.id.spinner);
                spinner.setAdapter(adapter);


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                categoryName = (String) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                                categoryName = cats.get(0);
                    }
                });
                pickaDate.setOnClickListener(new View.OnClickListener() {
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
                                    String pickedDate1 = mmday+"."+"0"+setmonth+"."+setyear;
                                    showdate.setText(pickedDate1);


                                }else {
                                    String pickedDate1 = mmday+"."+setmonth+"."+setyear;
                                    showdate.setText(pickedDate1);

                                }
                            }
                        }, year,month, day);
                        datePickerDialog.show();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();

                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String newdesc = ((EditText) dialog.findViewById(R.id.description)).getText().toString();
                        final String newdate = showdate.getText().toString().trim();
                        String amount = ((EditText) dialog.findViewById(R.id.amount)).getText().toString().trim();
                        amount = amount.replaceAll("[^\\.0123456789]", "");

                        if (newdesc.equals("") || newdate.equals("")|| amount.equals("")){
                            Toast.makeText(getActivity(), "Cant be Empty", Toast.LENGTH_SHORT).show();

                        }else {
                            String amountl = amount;
                            if (table.equals(FinanceSQLiteHandler.Table_Name_income)){

                                amountl = "+ "+amount;
                            }else if (table.equals(FinanceSQLiteHandler.Table_Name_spent)){
                                amountl = "- "+amount;
                            }
                            boolean update = db.update(table,newdesc,categoryName,newdate,amountl,idofitem);
                            if (update){
                                showall(pickedDate);
                                setincome(pickedDate);
                                setspent(pickedDate);
                                settotal();
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), ""+getString(R.string.updated), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), ""+getString(R.string.notupdated), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });



            }
        });
        alertDialog.show();

    }

    @SuppressLint("SetTextI18n")
    private void settotal() {
        budget = (float) 0.00;
        budget = totalincome - totalspent;
        GradientDrawable shapeDrawable = (GradientDrawable) budget_color;
        if (budget>0){
            total.setText("+"+budget+"p");
            total.setTextColor(getResources().getColor(R.color.income));
            shapeDrawable.setColor(ContextCompat.getColor(getContext(),R.color.budget_plus));
           // budgetzone.setBackgroundColor(getResources().getColor(R.color.budget_plus));
        }else if (budget<0){
            total.setText(""+budget+"p");
            total.setTextColor(getResources().getColor(R.color.spent));
            shapeDrawable.setColor(ContextCompat.getColor(getContext(),R.color.budget_minus));
           // budgetzone.setBackgroundColor(getResources().getColor(R.color.budget_minus));
        }else {
            total.setText(""+budget+"p");
            total.setTextColor(getResources().getColor(R.color.zero));
            shapeDrawable.setColor(ContextCompat.getColor(getContext(),R.color.listview_zone));

        }

    }

    private void showall(String date) {
        category.clear();
        description.clear();
        amount.clear();
        colors.clear();
        table.clear();
        Cursor resultincome = db.getAlldatas(FinanceSQLiteHandler.Table_Name_income);
        if (resultincome.getCount()==0){}
        while (resultincome.moveToNext()){
            String categoryname1 = resultincome.getString(1);
            String desc1 = resultincome.getString(2);
            String moment1 = resultincome.getString(3);
            String money1 = resultincome.getString(4);
            String icon1 = resultincome.getString(5);
            if (moment1.equals(date)) {
                category.add(categoryname1);
                description.add(desc1);
                amount.add(money1);
                table.add(FinanceSQLiteHandler.TABLE_CATEGORIES_Income);
                colors.add(getActivity().getApplicationContext().getResources().getColor(R.color.income));
                icons.add(icon1);

            }
        }
        Cursor result = db.getAlldatas(FinanceSQLiteHandler.Table_Name_spent);
        if (result.getCount()==0){}
        while (result.moveToNext()){
            String categoryname = result.getString(1);
            String desc = result.getString(2);
            String moment = result.getString(3);
            String money = result.getString(4);
            String icon = result.getString(5);
            if (moment.equals(date)) {
                category.add(categoryname);
                description.add(desc);
                amount.add(money);
                table.add(FinanceSQLiteHandler.TABLE_CATEGORIES_Spent);
                colors.add(getActivity().getApplicationContext().getResources().getColor(R.color.spent));
                icons.add(icon);
            }
        }
        if (category.isEmpty()){
            nodata.setVisibility(View.VISIBLE);
            nodata.setText(getString(R.string.nodata));
            mlistView.setVisibility(View.GONE);
        }else {
            nodata.setVisibility(View.GONE);
            mlistView.setVisibility(View.VISIBLE);
            dailyHelper = new DailyHelper(getActivity(), category, description, amount, table, colors,icons);
            dailyHelper.notifyDataSetChanged();
            mlistView.setAdapter(dailyHelper);
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
                    tdate.setText(pickedDate);


                }else {
                    pickedDate = mmday+"."+setmonth+"."+setyear;
                    tdate.setText(pickedDate);

                }

                setincome(pickedDate);
                setspent(pickedDate);
                showall(pickedDate);
                settotal();
            }
        }, year,month, day);
        datePickerDialog.show();
    }
    @SuppressLint("SetTextI18n")
    private void setspent(String date) {
        totalspent = (float) 0.00;
        tspent.setText("+"+totalspent+"p");
        tspent.setTextColor(getResources().getColor(R.color.spent));
        String table = FinanceSQLiteHandler.Table_Name_spent;
        Cursor result = db.getAlldatas(table);
        if (result.getCount()==0){
            totalspent = (float) 0.00;
            tspent.setText("-"+totalspent+"p");
            tspent.setTextColor(getResources().getColor(R.color.spent));
        }
        while (result.moveToNext()){
            String day = result.getString(3);
            if (day.equals(date)) {
                String amount = result.getString(4);
                amount = amount.replaceAll("[^\\.0123456789]", "");
                float f = Float.parseFloat(amount);
                totalspent = totalspent + f;
                tspent.setText("-" + totalspent + "р");
            }
        }
        tspent.setTextColor(getResources().getColor(R.color.spent));

    }
    @SuppressLint("SetTextI18n")
    private void setincome(String date) {
        totalincome = (float) 0.00;
        tincome.setText("+"+totalincome+"p");
        tincome.setTextColor(getResources().getColor(R.color.income));
        String table = FinanceSQLiteHandler.Table_Name_income;
        Cursor result = db.getAlldatas(table);
        if (result.getCount()==0){
            totalincome = (float) 0.00;
            tincome.setText("+"+totalincome+"p");
            tincome.setTextColor(getResources().getColor(R.color.income));
        }
        while (result.moveToNext()){
            String day = result.getString(3);
            if (day.equals(date)) {
                String amount = result.getString(4);
                amount = amount.replaceAll("[^\\.0123456789]", "");
                float f = Float.parseFloat(amount);
                totalincome = totalincome + f;
                tincome.setText("+" + totalincome + "р");
            }
        }
        tincome.setTextColor(getResources().getColor(R.color.income));

    }

    private void setdate() {
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        if (day<=9){
            mday = "0"+day;
        }else {
            mday=""+day;
        }
        month = mCurrentDate.get(Calendar.MONTH);
        mmonth=month+1;
        year = mCurrentDate.get(Calendar.YEAR);
        myear = year-2000;
        if (month<=9){
            pickedDate = mday+"."+"0"+mmonth+"."+myear;
        }else {
            pickedDate = mday+"."+mmonth+"."+myear;
        }
        tdate.setText(pickedDate);
    }

}
