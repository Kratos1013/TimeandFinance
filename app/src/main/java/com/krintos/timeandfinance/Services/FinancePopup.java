package com.krintos.timeandfinance.Services;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FinancePopup extends Activity {
    public String phonenumber, message, table_name,category_name;
    private String who, skolko,sms_how,sms_amount,sms_time,categoryName;
    public FinanceSQLiteHandler db;
    private EditText transaction,date,amount,add_new_category;
    private Spinner spinner;
    private Button cancel, save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_popup);
        phonenumber = getIntent().getStringExtra("PHONE_NUMBER");
        message = getIntent().getStringExtra("MESSAGE_FROM_BANK");
        transaction = findViewById(R.id.transaction);
        date = findViewById(R.id.date);
        amount = findViewById(R.id.amount);
        spinner = findViewById(R.id.spinner);
        add_new_category = findViewById(R.id.add_new_category);
        cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.save);

        Pattern p = Pattern.compile("зачисление");
        Matcher m = p.matcher(message);
        Pattern pi = Pattern.compile("перевел");
        Matcher mi = pi.matcher(message);
        String remove = "\"";
        Pattern r = Pattern.compile(remove);


        if (mi.find()){
            String who1 = message.substring(message.lastIndexOf("Онлайн.")+7);
            who = who1.substring(0, who1.indexOf(" перевел")+1);
            String skolko1 = message.substring(message.lastIndexOf("Вам")+3);
            skolko = skolko1.substring(0,skolko1.indexOf("."));
            Matcher ri = r.matcher(who);
            if (ri.find()){
                who = who.replace("\"","");
            }
        }
        if (m.find()) {
            String day = message.substring(9, Math.min(message.length(), 17));
            String amount = message.substring(message.lastIndexOf("зачисление")+10,message.indexOf("р")+1);

            String amount1 = amount.substring(0,amount.indexOf("р")-1);
            sms_time = day;
            if (skolko != null){
                Pattern k = Pattern.compile(amount1);
                Matcher ki = k.matcher(skolko);
                if (ki.find()){
                    sms_how = who;
                    who = null;

                }else{
                    sms_how = "Неизвестно";
                }

            }else{
                sms_how = "Неизвестно";
            }
            table_name = FinanceSQLiteHandler.Table_Name_income;
            category_name = FinanceSQLiteHandler.TABLE_CATEGORIES_Income;
            sms_amount = "+"+amount;
        }
        Pattern so = Pattern.compile("ОТКАЗ");
        Matcher som = so.matcher(message);
        Pattern sp = Pattern.compile("покупка");
        Matcher sm = sp.matcher(message);
        Pattern su = Pattern.compile("USD");
        Matcher susd = su.matcher(message);
        Pattern ss = Pattern.compile("списание");
        Matcher ssp = ss.matcher(message);
        if (sm.find()) {
            if (som.find()){
                //Denied purchases also existed
            }else {
                if (susd.find()){

                }else {

                    {
                        String day = message.substring(9, Math.min(message.length(), 17));
                        String amount = "-"+message.substring(message.lastIndexOf("покупка")+7,message.indexOf("р")+1);
                        String place = message.substring(message.indexOf("р")+1,message.lastIndexOf(" Баланс"));
                        place = place.replace("\"", "");

                        sms_time = day;
                        sms_how = place;
                        sms_amount = amount;
                        category_name = FinanceSQLiteHandler.Table_Name_spent;


                    }
                }


            }
        }else if (ssp.find()) {
            Pattern b = Pattern.compile("Баланс:");
            Matcher bm = b.matcher(message);
            if (bm.find()) {
                String day = message.substring(9, Math.min(message.length(), 17));
                String amount = "-" + message.substring(message.lastIndexOf("списание") + 8, message.indexOf("р") + 1);
                String place = "списание";

                sms_time = day ;
                sms_how = place;
                sms_amount = amount;
                table_name = FinanceSQLiteHandler.Table_Name_spent;
                category_name = FinanceSQLiteHandler.TABLE_CATEGORIES_Spent;

            } else {
            }
        }
        List<String> array = new ArrayList<String>();

        db = new FinanceSQLiteHandler(FinancePopup.this);
        Cursor result = db.getCategoryNames(category_name);
        if (result.getCount() == 0) {
            spinner.setVisibility(View.GONE);
            add_new_category.setVisibility(View.VISIBLE);
        }
        while (result.moveToNext()) {
            String categorynames = result.getString(1);
            array.add(categorynames);
        }
        if (result.getCount() > 0) {
            array.add(getString(R.string.add_new_category));
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(FinancePopup.this, android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String empty = (String) parent.getItemAtPosition(position);
                if (empty.equals(getString(R.string.add_new_category))) {
                    spinner.setVisibility(View.GONE);
                    add_new_category.setVisibility(View.VISIBLE);
                } else {
                    categoryName = (String) parent.getItemAtPosition(position);
                    add_new_category.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        transaction.setText(sms_how);
        date.setText(sms_time);
        amount.setText(sms_amount);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Toast.makeText(FinancePopup.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trans_name = transaction.getText().toString().trim();
                String sdate = date.getText().toString().trim();
                String samount = amount.getText().toString().trim();
                if (trans_name.equals("") || sdate.equals("") || samount.equals("")) {
                    Toast.makeText(FinancePopup.this, "" + getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                } else {
                    if (add_new_category.getVisibility() == View.VISIBLE && spinner.getVisibility() == View.GONE) {
                        String categoryName1 = add_new_category.getText().toString();
                        String categoryName = categoryName1.replace("\"", "");
                        Cursor result = db.IsCategoryExist(categoryName, category_name);

                        if (!categoryName.isEmpty() && result.getCount() == 0) {
                            boolean isAdded = db.addcategoryname(categoryName, category_name);
                            if (isAdded) {
                                Boolean isInserted = db.inserttocategory(categoryName, trans_name, sdate, samount, table_name,"noicon");
                                if (isInserted) {
                                    Toast.makeText(FinancePopup.this, "Added", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    //what to do when its not added to the category
                                    Toast.makeText(FinancePopup.this, "COULDNT", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //category name is not added to category name table
                                Toast.makeText(FinancePopup.this, "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                            //everything is added without errors
                        } else if (categoryName.isEmpty()) {
                            //category field is empty
                            Toast.makeText(FinancePopup.this, "Can't be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            //category already exists
                            Toast.makeText(FinancePopup.this, "Sorry category already exists", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Boolean isInserted = db.inserttocategory(categoryName, trans_name, sdate, samount, table_name,"noicon");
                        if (isInserted) {
                            Toast.makeText(FinancePopup.this, "Added", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(FinancePopup.this, "COULDNT", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

}
