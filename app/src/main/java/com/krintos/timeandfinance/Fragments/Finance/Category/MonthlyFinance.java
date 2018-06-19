package com.krintos.timeandfinance.Fragments.Finance.Category;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyFinance extends Fragment {
    private TextView incometotal, spenttotal, total,showyear;
    private ListView listview;
    private long totalincome= (long) 0.0, totalspent= (long) 0.0, budget= (long) 0.0;
    private float monthtotalincome = (float) 0.0, monthtotalspent = (float) 0.0, monthtotalbudget = (float) 0.0;
    private float[] monthincome = new float[12];
    private float[] monthspent = new float[12];
    private float[] monthtotal = new float[12];
    private ArrayList<String> months = new ArrayList<>();
    private ArrayList<Float> mincome = new ArrayList<>();
    private ArrayList<Float> mspent = new ArrayList<>();
    private ArrayList<Float> month = new ArrayList<>();
    private MonthHelper monthHelper;
    private FinanceSQLiteHandler db;
    private Calendar mCurrentDate;
    private int tday,tmonth,tmmonth,tyear;
    private String  pickedDate,tmday,categoryName,idofitem,table_name_income="categoriesforincome";

    public MonthlyFinance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_monthly_finance, container, false);
        months.clear();
        mincome.clear();
        mspent.clear();
        month.clear();
        totalincome= (long) 0.0; totalspent= (long) 0.0; budget= (long) 0.0;
        monthincome = new float[]{0,0,0,0,0,0,0,0,0,0,0,0};
        monthspent = new float[]{0,0,0,0,0,0,0,0,0,0,0,0};
        monthtotal = new float[]{0,0,0,0,0,0,0,0,0,0,0,0};
        /*for (int i = 0; i < 12;i++ ){
            monthtotal[i] = 0;
            monthspent[i] = 0;
            monthincome[i] = 0;
        }*/

        showyear = rootView.findViewById(R.id.showyear);
        incometotal = rootView.findViewById(R.id.incometotal);
        spenttotal = rootView.findViewById(R.id.spenttotal);
        total = rootView.findViewById(R.id.total);
        listview = rootView.findViewById(R.id.listviewformonths);
        db = new FinanceSQLiteHandler(getContext());
        setdate();
        setbudgets();
        showmonths();

        return rootView;
    }



    private void showmonths() {
        String table_income = FinanceSQLiteHandler.Table_Name_income;
        String table_spent = FinanceSQLiteHandler.Table_Name_spent;
        Cursor income = db.getAlldatas(table_income);
         if (income.getCount()==0){

        }else {
            while (income.moveToNext()){
                String date = income.getString(3);
                date = date.substring(4,Math.min(date.length(),5));
                int index = Integer.parseInt(date)-1;
                String amount = income.getString(4);
                amount = amount.replaceAll("[^\\.0123456789]", "");
                float f = Float.parseFloat(amount);
                float result = monthincome[index] +f;
                monthincome[index] = result;
            }
        }
        Cursor spent = db.getAlldatas(table_spent);
        if (spent.getCount()==0){

        }else {
            while (spent.moveToNext()){
                String date = spent.getString(3);
                date = date.substring(4, Math.min(date.length(),5));
                int index = Integer.parseInt(date)-1;
                String amount = spent.getString(4);
                amount = amount.replaceAll("[^\\.0123456789]", "");
                float f  = Float.parseFloat(amount);
                float result = monthspent[index]+f;
                monthspent[index] = result;
            }
        }
        Resources res = getResources();
        String[] mmonths = res.getStringArray(R.array.months);
        for (int i = 11; i>=0;i--){
            if (monthincome[i] != 0 || monthspent[i] != 0) {
                monthtotal[i] = monthincome[i] - monthspent[i];
                month.add(monthtotal[i]);
                mincome.add(monthincome[i]);
                mspent.add(monthspent[i]);
                months.add(mmonths[i]);
            }
        }

        monthHelper = new MonthHelper(getActivity(),mincome,mspent,month,months);
        monthHelper.notifyDataSetChanged();
        listview.setAdapter(monthHelper);


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
        int twodigityear = tyear-2000;
        String year = String.valueOf(twodigityear);
        Cursor income = db.getAlldatas(table);
        if (income.getCount()>0){
            Pattern p = Pattern.compile(year);

            while(income.moveToNext()){
                String date = income.getString(3);
                Matcher m = p.matcher(date);
                if (m.find()){
                    String amount = income.getString(4);
                    amount = amount.replaceAll("[^\\.0123456789]", "");
                    float f = Float.parseFloat(amount);
                    totalspent = (long) (totalspent + f);
                }

            }
        }
        spenttotal.setText("-"+totalspent+"p");
    }

    @SuppressLint("SetTextI18n")
    private void setincome(String table) {
        int twodigityear = tyear-2000;
        String year = String.valueOf(twodigityear);
        Cursor income = db.getAlldatas(table);
        if (income.getCount()>0){
            Pattern p = Pattern.compile(year);

            while(income.moveToNext()) {
                String date = income.getString(3);
                Matcher m = p.matcher(date);
                if (m.find()) {
                    String amount = income.getString(4);
                    amount = amount.replaceAll("[^\\.0123456789]", "");
                    float f = Float.parseFloat(amount);
                    totalincome = (long) (totalincome + f);
                }
            }
        }
        incometotal.setText("+"+totalincome+"p");
    }
    private void setdate() {
        mCurrentDate = Calendar.getInstance();
        tyear = mCurrentDate.get(Calendar.YEAR);
        showyear.setText(""+tyear);
    }

}
