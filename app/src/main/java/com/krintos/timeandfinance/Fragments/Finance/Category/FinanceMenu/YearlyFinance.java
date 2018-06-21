package com.krintos.timeandfinance.Fragments.Finance.Category.FinanceMenu;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearlyFinance extends Fragment {
    private TextView incometotal, spenttotal, total;
    private ListView listview;
    private FinanceSQLiteHandler db;
    private long totalincome= (long) 0.0, totalspent= (long) 0.0, budget= (long) 0.0;


    public YearlyFinance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_yearly_finance, container, false);
        totalincome= (long) 0.0; totalspent= (long) 0.0; budget= (long) 0.0;
        incometotal = rootView.findViewById(R.id.incometotal);
        spenttotal = rootView.findViewById(R.id.spenttotal);
        total = rootView.findViewById(R.id.total);
        listview = rootView.findViewById(R.id.listviewforyears);
        db = new FinanceSQLiteHandler(getContext());


        setbudgets();

        return rootView;
    }
    @SuppressLint("SetTextI18n")
    private void setbudgets() {
        setincome(FinanceSQLiteHandler.Table_Name_income);
        setspent(FinanceSQLiteHandler.Table_Name_spent);
        budget = totalincome-totalspent;
        if (budget>0){
            total.setText("+"+budget+"p");
        }else if (budget<0){
            total.setText(budget+"p");
        }else {
            total.setText(""+budget+"p");
        }
    }
    @SuppressLint("SetTextI18n")
    private void setspent(String table) {
        Cursor income = db.getAlldatas(table);
        if (income.getCount()>0){
            while(income.moveToNext()){
                String amount = income.getString(4);
                amount = amount.replaceAll("[^\\.0123456789]", "");
                float f = Float.parseFloat(amount);
                totalspent = (long) (totalspent + f);
            }
        }
        spenttotal.setText("-"+totalspent+"p");
    }

    @SuppressLint("SetTextI18n")
    private void setincome(String table) {
        Cursor income = db.getAlldatas(table);
        if (income.getCount()>0){
            while(income.moveToNext()){
                String amount = income.getString(4);
                amount = amount.replaceAll("[^\\.0123456789]", "");
                float f = Float.parseFloat(amount);
                totalincome = (long) (totalincome + f);
            }
        }
        incometotal.setText("+"+totalincome+"p");
    }
}
