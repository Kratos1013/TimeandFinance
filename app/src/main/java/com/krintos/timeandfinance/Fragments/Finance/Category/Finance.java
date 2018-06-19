package com.krintos.timeandfinance.Fragments.Finance.Category;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.Fragments.Finance.Category.Finance_Category_main;
import com.krintos.timeandfinance.Fragments.Finance.Sms.SMSchecker;
import com.krintos.timeandfinance.Fragments.Finance.Statistics.Finance_Statistics_Main_Frame;
import com.krintos.timeandfinance.Fragments.Helpers.FinanceMainPageHelper;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Finance extends Fragment {
    private Calendar calendar;
    private ArrayList<String> categories =new ArrayList<>();
    private ArrayList<Float> amounts =new ArrayList<>();
    private ListView spentlistview;
    private LinearLayout incomeandspend,statistics,card;
    private TextView tvincome,tvspent,tvtotal, showdate;
    private int datafilter, day, month, year, pfilter;
    private String mday,pickedDate;
    private FinanceSQLiteHandler db;
    private float fincome,fspent, ftotal;
    private ImageButton left, right;
    public Finance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finance, container, false);
        incomeandspend = rootView.findViewById(R.id.incomeandspent);
        statistics = rootView.findViewById(R.id.statistics);
        card = rootView.findViewById(R.id.card);
        tvincome = rootView.findViewById(R.id.income);
        tvspent = rootView.findViewById(R.id.spent);
        tvtotal = rootView.findViewById(R.id.total);
        spentlistview = rootView.findViewById(R.id.spentlistview);
        db = new FinanceSQLiteHandler(getContext());
        calendar = Calendar.getInstance();
        setdatasforincomeandspent(1, getcalendar(1));
        setspentlistview(getcalendar(1));

        incomeandspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Fragment Category = new Finance_Category_main();
                        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, Category);
                        ft.addToBackStack("incomeandspentmenu");
                        ft.commit();
                    }
                }, 100);

            }
        });

        return rootView;
    }

    private void setspentlistview(String month) {
        categories.clear();
        String table = FinanceSQLiteHandler.TABLE_CATEGORIES_Spent;
        Cursor spentcursor = db.getCategoryNames(table);
        while (spentcursor.moveToNext()){
            String categoryname = spentcursor.getString(1);
            Cursor getspends = db.getAlldatasbydate(FinanceSQLiteHandler.Table_Name_spent,categoryname,month);
            if (getspends.getCount() >0){
                categories.add(categoryname);
                float gspent = (float) 0.0;
            while (getspends.moveToNext()){
                String amount = getspends.getString(4);
                amount = amount.replaceAll("[^\\.0123456789]", "");
                float f = Float.parseFloat(amount);
                gspent +=f;
            }
                amounts.add(gspent);
            }
        }
        FinanceMainPageHelper financeMainPageHelper = new FinanceMainPageHelper(getActivity(), categories,amounts);
        financeMainPageHelper.notifyDataSetChanged();
        spentlistview.setAdapter(financeMainPageHelper);

    }

    private void listviewfiller(String categoryname, String amount) {

    }


    @SuppressLint("SetTextI18n")
    private void setdatasforincomeandspent(int datafilter, String date) {
        fincome = (float) 0.0;
        fspent = (float) 0.0;
        ftotal = (float) 0.0;
        Cursor income = db.getAlldatas(FinanceSQLiteHandler.Table_Name_income);
        if (income.getCount() == 0){
        }else {
            while (income.moveToNext()){
                String time = income.getString(3);
                if (datafilter==0){
                    if (time.equals(date)){
                        String amount = income.getString(4);
                        amount = amount.replaceAll("[^\\.0123456789]", "");
                        float f = Float.parseFloat(amount);
                        fincome = fincome +f;
                    }
                }else if (datafilter ==1){
                    String mtime = income.getString(3);
                    mtime = mtime.substring(3, mtime.length());

                    if (mtime.equals(date)){
                        String amount = income.getString(4);
                        amount = amount.replaceAll("[^\\.0123456789]", "");
                        float f = Float.parseFloat(amount);
                        fincome = fincome +f;
                    }
                }else if (datafilter == 2){
                    String ytime = income.getString(3);
                    ytime = ytime.substring(ytime.length()-2,ytime.length());
                if (ytime.equals(date)){
                    String amount = income.getString(4);
                    amount = amount.replaceAll("[^\\.0123456789]", "");
                    float f = Float.parseFloat(amount);
                    fincome = fincome +f;
                }

                }else if (datafilter == 3){
                    String amount = income.getString(4);
                    amount = amount.replaceAll("[^\\.0123456789]", "");
                    float f = Float.parseFloat(amount);
                    fincome = fincome +f;
                }
            }
        }
        Cursor spent = db.getAlldatas(FinanceSQLiteHandler.Table_Name_spent);
        if (spent.getCount() == 0){
        }else {
            while (spent.moveToNext()){
                String time = spent.getString(3);
                if (datafilter==0){
                    if (time.equals(date)){
                        String amount = spent.getString(4);
                        amount = amount.replaceAll("[^\\.0123456789]", "");
                        float f = Float.parseFloat(amount);
                        fspent = fspent +f;
                    }
                }else if (datafilter ==1){
                    String mtime = spent.getString(3);
                    mtime = mtime.substring(3, mtime.length());
                    if (mtime.equals(date)){
                        String amount = spent.getString(4);
                        amount = amount.replaceAll("[^\\.0123456789]", "");
                        float f = Float.parseFloat(amount);
                        fspent = fspent +f;
                    }
                }else if (datafilter == 2){
                    String ytime = spent.getString(3);
                    ytime = ytime.substring(6, ytime.length());
                    if (ytime.equals(date)){
                        String amount = spent.getString(4);
                        amount = amount.replaceAll("[^\\.0123456789]", "");
                        float f = Float.parseFloat(amount);
                        fspent = fspent +f;
                    }

                }else if (datafilter == 3){
                    String amount = spent.getString(4);
                    amount = amount.replaceAll("[^\\.0123456789]", "");
                    float f = Float.parseFloat(amount);
                    fspent = fspent +f;
                }
            }
        }

        ftotal = fincome - fspent;
        tvincome.setText("Доходы : +"+fincome+"p");
        tvspent.setText("Расходы : -"+fspent+"p");
        if (ftotal>0){
            tvtotal.setText("Бюджет : +"+ftotal+"p");
        }else {
            tvtotal.setText("Бюджет : "+ftotal+"p");
        }
    }
    private String getcalendar(int position) {
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day <= 9) {
            mday = "0" + day;
        } else {
            mday = "" + day;
        }
        month = calendar.get(Calendar.MONTH);
        month = month + 1;
        year = calendar.get(Calendar.YEAR);
        year = year - 2000;

        if (position == 0) {
            if (month <= 9) {
                pickedDate = mday + "." + "0" + month + "." + year;
            } else {
                pickedDate = mday + "." + month + "." + year;
            }
        } else if (position == 1) {
            if (month <= 9) {
                pickedDate = "0" + month + "." + year;
            } else {
                pickedDate = month + "." + year;
            }
        } else if (position == 2) {
            year = year + 2000;
            pickedDate = "" + year;
        } else {
            pickedDate = getResources().getString(R.string.forallperiod);
        }
        return pickedDate;
    }

}
















































/* Fragment Category = new Finance_Category_main();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.finance_main_frame, Category);
        ft.commit();*/
/* private void rightclicked() {
        String gdate = showdate.getText().toString().trim();
        if (datafilter == 0){
            int d2 = calendar.get(Calendar.DAY_OF_MONTH);
            int d1 = Integer.parseInt(gdate.substring(0,2));
            String d3 = gdate.substring(2,gdate.length());
            d1 = d1+1;
            if (d1<=d2){
                if (d1<10){
                    pickedDate = "0"+d1+d3;
                    showdate.setText(pickedDate);
                }else {
                    pickedDate = d1+d3;
                    showdate.setText(pickedDate);
                }
                setdatasforincomeandspent(datafilter,pickedDate);
            }else {
                Toast.makeText(getContext(), "Change months", Toast.LENGTH_SHORT).show();
            }
        }else if (datafilter == 1){
            int m2 = calendar.get(Calendar.MONTH)+1;
            int m1 = Integer.parseInt(gdate.substring(0,2));
            String m3 = gdate.substring(2,gdate.length());
            m1 = m1+1;
            if (m1<=m2){
                if (m1<10){
                    pickedDate = "0"+m1+m3;
                    showdate.setText(pickedDate);
                }else {
                    pickedDate = m1+m3;
                    showdate.setText(pickedDate);
                }
                setdatasforincomeandspent(datafilter,pickedDate);
            }else {
                Toast.makeText(getContext(), "Change years", Toast.LENGTH_SHORT).show();
            }
        }else if (datafilter == 2) {
            int y1 = Integer.parseInt(gdate);
            int yl = calendar.get(Calendar.YEAR);
            if (y1 < yl) {
                y1 = y1 + 1;
                String y3 = String.valueOf(y1);
                showdate.setText(y3);
                String dat = y3.substring(2, y3.length());
                setdatasforincomeandspent(datafilter, dat);
            } else {
                Toast.makeText(getContext(), "Cant go far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void leftclicked() {
        String gdate = showdate.getText().toString().trim();
        if (datafilter == 0){
                int d1 = Integer.parseInt(gdate.substring(0,2));
                String d2 = gdate.substring(2, gdate.length());
                d1 = d1-1;
                if (d1>=1){
                if (d1>=10){
                    pickedDate = d1+d2;
                    showdate.setText(pickedDate);
                }else {
                    pickedDate = "0"+d1+d2;
                    showdate.setText(pickedDate);
                }
                setdatasforincomeandspent(datafilter,pickedDate);
                }else {
                    Toast.makeText(getContext(), "Change to months", Toast.LENGTH_SHORT).show();
                }
        }else if (datafilter == 1){
            int m1 = Integer.parseInt(gdate.substring(0,2));
            String m2 = gdate.substring(2,gdate.length());
            m1 = m1 - 1;
            if (m1>=1){
                if (m1>=10){
                    pickedDate = m1+m2;
                    showdate.setText(pickedDate);
                }else {
                    pickedDate = "0"+m1+m2;
                    showdate.setText(pickedDate);
                }
                setdatasforincomeandspent(datafilter,pickedDate);
            }else {
                Toast.makeText(getContext(), "Change to years", Toast.LENGTH_SHORT).show();
            }
        }else if (datafilter == 2){
            int y1 = Integer.parseInt(gdate);
            if (y1>=2001) {
                y1 = y1 - 1;
                String y3 = String.valueOf(y1);
                showdate.setText(y3);
                String dat = y3.substring(2, y3.length());
                setdatasforincomeandspent(datafilter, dat);
            }else {
                Toast.makeText(getContext(), "Cant go back", Toast.LENGTH_SHORT).show();
            }

        }
    }*/
/*
left.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        leftclicked();
        }
        });
        right.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        rightclicked();
        }
        });*/
/*private void setfilter() {
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.filter));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(adapter);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                datafilter = position;
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
                showdate.setVisibility(View.VISIBLE);
                if (position == 0) {
                    setdatasforincomeandspent(datafilter, getcalendar(position));
                    showdate.setText(getcalendar(0));
                }else if (position == 1){
                    setdatasforincomeandspent(datafilter, getcalendar(position));
                    showdate.setText(getcalendar(1));
                }else if (position ==2){
                    showdate.setText(getcalendar(2));
                    String dat = getcalendar(2);
                    dat = dat.substring(dat.length()-2,dat.length());
                    setdatasforincomeandspent(datafilter, dat);
                }else if (position == 3){
                    left.setVisibility(View.GONE);
                    right.setVisibility(View.GONE);
                    showdate.setVisibility(View.GONE);
                    showdate.setText(getcalendar(3));
                    setdatasforincomeandspent(datafilter, getcalendar(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                datafilter = 1;
                setdatasforincomeandspent(datafilter, getcalendar(1));
                showdate.setText(getcalendar(1));
            }
        });
    }*/
/*
    }*/