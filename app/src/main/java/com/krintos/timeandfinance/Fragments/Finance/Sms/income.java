package com.krintos.timeandfinance.Fragments.Finance.Sms;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class income extends Fragment {
    private static final String TAG = "Income";
    public ArrayList<String> sms_time = new ArrayList<String>();
    public ArrayList<String> sms_how = new ArrayList<String>();
    public ArrayList<String> sms_amount = new ArrayList<String>();
    public ListView sms;
    public String who, skolko;
    public TextView tdate;
    public Calendar mCurrentDate;
    public int day,month,mmonth,year,myear;
    public LinearLayout nodata;
    public String  pickedDate,mday,categoryName,table_name_income="categoriesforincome";
    public ImageView pickDate, all;
    public FinanceSQLiteHandler db;

    public Sms_manager_income sms_manager;

    public income() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_income, container, false);
        sms_amount.clear();
        sms_how.clear();
        sms_time.clear();
        sms = (ListView) rootView.findViewById(R.id.listviewforsms);
        db = new FinanceSQLiteHandler(getActivity().getApplicationContext());

        pickDate = (ImageView) rootView.findViewById(R.id.pickDate);
        nodata = (LinearLayout) rootView.findViewById(R.id.emptylistview);
        all = (ImageView) rootView.findViewById(R.id.showAll);
        tdate = (TextView) rootView.findViewById(R.id.showdate);
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
        showsms(pickedDate);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
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
                        sms_how.clear();
                        sms_time.clear();
                        sms_amount.clear();
                        showsms(pickedDate);
                    }
                }, year,month, day);
                datePickerDialog.show();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sms_how.clear();
                sms_time.clear();
                sms_amount.clear();
                tdate.setText(getText(R.string.forallperiod));
                pickedDate = "showAll";
                showsms(pickedDate);
            }
        });
        sms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> array = new ArrayList<String>();
                final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.listview_background);
                ColorDrawable color = (ColorDrawable) linearLayout.getBackground();
                int colorID = color.getColor();
                if(colorID == getResources().getColor(R.color.sms_added)){

                    Toast.makeText(getActivity(), "You have already added this ", Toast.LENGTH_SHORT).show();
                }else {
                    Cursor result = db.getCategoryNames(FinanceSQLiteHandler.TABLE_CATEGORIES_Income);
                    String date = ((TextView) view.findViewById(R.id.time)).getText().toString();
                    String where = ((TextView) view.findViewById(R.id.how)).getText().toString();
                    String price = ((TextView) view.findViewById(R.id.amount)).getText().toString();
                    final Dialog add_dialog = new Dialog(getActivity());
                    add_dialog.setContentView(R.layout.sms_add_category);
                    EditText name = (EditText) add_dialog.findViewById(R.id.name);
                    EditText showtime = (EditText) add_dialog.findViewById(R.id.time);
                    EditText amount = (EditText) add_dialog.findViewById(R.id.amount);
                    final Spinner spinner = (Spinner) add_dialog.findViewById(R.id.choose_category);
                    final EditText add_new = (EditText) add_dialog.findViewById(R.id.add_new_category);
                    if (result.getCount() == 0) {
                        spinner.setVisibility(View.GONE);
                        add_new.setVisibility(View.VISIBLE);
                    }
                    while (result.moveToNext()) {
                        String categorynames = result.getString(1);
                        array.add(categorynames);
                    }
                    if (result.getCount() > 0) {
                        array.add(getString(R.string.add_new_category));
                    }

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, array);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String empty = (String) parent.getItemAtPosition(position);
                            if (empty.equals(getString(R.string.add_new_category))) {
                                spinner.setVisibility(View.GONE);
                                add_new.setVisibility(View.VISIBLE);
                            } else {
                                categoryName = (String) parent.getItemAtPosition(position);
                                add_new.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    Button cancel = (Button) add_dialog.findViewById(R.id.cancel);
                    Button add = (Button) add_dialog.findViewById(R.id.add);
                    name.setText(where);
                    showtime.setText(date);
                    amount.setText(price);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            add_dialog.cancel();
                        }
                    });
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            EditText name = (EditText) add_dialog.findViewById(R.id.name);
                            EditText showtime = (EditText) add_dialog.findViewById(R.id.time);
                            EditText priceamount = (EditText) add_dialog.findViewById(R.id.amount);
                            String trans_name = name.getText().toString();
                            String date = showtime.getText().toString();
                            String amount = priceamount.getText().toString();
                            if (trans_name.equals("") || date.equals("") || amount.equals("")) {
                                Toast.makeText(getActivity(), "" + getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                            } else {
                                if (add_new.getVisibility() == View.VISIBLE && spinner.getVisibility() == View.GONE) {
                                    String categoryName1 = add_new.getText().toString();
                                    String categoryName = categoryName1.replace("\"", "");
                                    Cursor result = db.IsCategoryExist(categoryName, FinanceSQLiteHandler.TABLE_CATEGORIES_Income);

                                    if (!categoryName.isEmpty() && result.getCount() == 0) {
                                        boolean isAdded = db.addcategoryname(categoryName, FinanceSQLiteHandler.TABLE_CATEGORIES_Income);
                                        if (isAdded) {
                                            Boolean isInserted = db.inserttocategory(categoryName, trans_name, date, amount, table_name_income,"noicon");
                                            if (isInserted) {
                                                linearLayout.setBackgroundResource(R.color.sms_added);
                                                add_dialog.cancel();
                                            } else {
                                                //what to do when its not added to the category
                                                Toast.makeText(getActivity(), "COULDNT", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            //category name is not added to category name table
                                            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                        //everything is added without errors
                                    } else if (categoryName.isEmpty()) {
                                        //category field is empty
                                        Toast.makeText(getActivity(), "Can't be empty", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //category already exists
                                        Toast.makeText(getActivity(), "Sorry category already exists", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                    Boolean isInserted = db.inserttocategory(categoryName, trans_name, date, amount, table_name_income,"noicon");
                                    if (isInserted) {
                                        linearLayout.setBackgroundResource(R.color.sms_added);
                                        add_dialog.cancel();
                                    } else {
                                        Toast.makeText(getActivity(), "COULDNT", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }


                        }
                    });

                    add_dialog.setCanceledOnTouchOutside(false);
                    add_dialog.show();
                }
            }
        });
        return rootView;
    }
    private void showsms(String pickedDate) {
        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor cur = getActivity().getContentResolver().query(uri, projection, "address='900'", null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);

                   /* smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(longDate + ", ");
                    smsBuilder.append(int_Type);
                    smsBuilder.append(" ]\n\n");*/
                    Pattern p = Pattern.compile("зачисление");
                    Matcher m = p.matcher(strbody);
                    Pattern pi = Pattern.compile("перевел");
                    Matcher mi = pi.matcher(strbody);
                    String remove = "\"";
                    Pattern r = Pattern.compile(remove);

                    if (mi.find()){
                        String who1 = strbody.substring(strbody.lastIndexOf("Онлайн.")+7);
                        who = who1.substring(0, who1.indexOf(" перевел")+1);
                        String skolko1 = strbody.substring(strbody.lastIndexOf("Вам")+3);
                        skolko = skolko1.substring(0,skolko1.indexOf("."));
                        Matcher ri = r.matcher(who);
                        if (ri.find()){
                            who = who.replace("\"","");
                        }
                    }
                    if (m.find()) {
                        String day = strbody.substring(9, Math.min(strbody.length(), 17));
                        String amount = strbody.substring(strbody.lastIndexOf("зачисление")+10,strbody.indexOf("р")+1);

                        String amount1 = amount.substring(0,amount.indexOf("р")-1);


                        if (pickedDate.equals("showAll")){
                            sms_time.add(day);
                                if (skolko != null){
                                    Pattern k = Pattern.compile(amount1);
                                    Matcher ki = k.matcher(skolko);
                                    if (ki.find()){
                                        sms_how.add(who);
                                        who = null;

                                    }else{
                                        sms_how.add("Неизвестно");
                                    }

                            }else{
                                 sms_how.add("Неизвестно");
                            }
                            sms_amount.add("+"+amount);
                        }else if (day.equals(pickedDate)) {
                            sms_time.add(day);
                            if (skolko != null){
                                Pattern k = Pattern.compile(amount1);
                                Matcher ki = k.matcher(skolko);
                                if (ki.find()) {
                                    sms_how.add(who);
                                    who = null;
                                }else{
                                    sms_how.add("Неизвестно");
                                }

                            }else{
                                    sms_how.add("Неизвестно");
                            }
                            sms_amount.add("+"+amount);

                        }


                    }
                    
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
        sms_manager = new Sms_manager_income(getActivity(), sms_time, sms_how, sms_amount);
        sms_manager.notifyDataSetChanged();
        sms.setAdapter(sms_manager);
        if (sms_how.isEmpty()||sms_time.isEmpty()||sms_amount.isEmpty()){
            if (sms.getVisibility() == View.VISIBLE){
                sms.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                //don't forget to add no result alert
            }
        }else {
            if (sms.getVisibility() == View.GONE) {
                sms.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
            }
        }
    }
}
