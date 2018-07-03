package com.krintos.timeandfinance.Fragments.Finance.Category.PassivesMenu;


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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.Fragments.Finance.Category.Add.add_to_passives;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassivesMain extends Fragment {
    private FloatingActionButton addnew;
    private ListView listView;
    private Calendar calendar;
    private String  pickedDate,mday,category;
    private FinanceSQLiteHandler db;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private ArrayList<String> pricespermonth = new ArrayList<>();
    private ArrayList<String> enddates = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private int day,month,mmonth,year,myear;

    public PassivesMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_passives_main, container, false);
        addnew = rootView.findViewById(R.id.addnew);
        listView = rootView.findViewById(R.id.listviewforpassive);
        db = new FinanceSQLiteHandler(getContext());
        setlistview();
        setdate();
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment PassiveAddNew = new add_to_passives();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, PassiveAddNew);
                ft.addToBackStack("PassivesAdd");
                ft.commit();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ID = ((TextView) view.findViewById(R.id.tvids)).getText().toString().trim();
                String name = ((TextView) view.findViewById(R.id.tvname)).getText().toString().trim();
                String desc = ((TextView) view.findViewById(R.id.tvdescription)).getText().toString().trim();
                String price = ((TextView) view.findViewById(R.id.tvprice)).getText().toString().trim();
                String pricepermonth = ((TextView) view.findViewById(R.id.tvpricepermonth)).getText().toString().trim();
                String date = ((TextView) view.findViewById(R.id.tvenddate)).getText().toString().trim();

                itemsselected(ID,name,desc,price,pricepermonth,date);
            }
        });
        return rootView;
    }

    private void itemsselected(final String id, String name, String desc, String price, String pricepermonth, String date) {
        AlertDialog.Builder change = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.fragment_add_to_passives,null);
        change.setView(dialog);
        Button delete = (Button) dialog.findViewById(R.id.cancel);
        Button save = (Button) dialog.findViewById(R.id.save);
        delete.setText(getString(R.string.delete));
        final EditText pname = dialog.findViewById(R.id.passivename);
        final EditText pdesc = dialog.findViewById(R.id.passivedescription);
        final EditText pamount = dialog.findViewById(R.id.price);
        final EditText pamountpermonth = dialog.findViewById(R.id.pricepermonth);
        final TextView pdate = dialog.findViewById(R.id.enddate);
        TextView pickdate = dialog.findViewById(R.id.pickDate);
        pname.setText(name);
        pdesc.setText(desc);
        pamount.setText(price);
        pamountpermonth.setText(pricepermonth);
        pdate.setText(date);
        final AlertDialog alertDialog =  change.create();
        pickdate.setOnClickListener(new View.OnClickListener() {
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
                            pdate.setText(pickedDate);
                        }else {
                            pickedDate = mmday+"."+setmonth+"."+setyear;
                            pdate.setText(pickedDate);
                        }
                    }
                }, year,month, day);
                datePickerDialog.show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer del = db.deletedata(FinanceSQLiteHandler.TABLE_NAME_PASSIVES,id);
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
                String sname = pname.getText().toString().trim();
                String sdesc = pdesc.getText().toString().trim();
                String sprice = pamount.getText().toString().trim();
                String spricepermonth = pamountpermonth.getText().toString().trim();
                String sdate = pdate.getText().toString().trim();
                if (sname.equals("")||sdesc.equals("")||sprice.equals("")||spricepermonth.equals("")||
                        sdate.equals("")){
                    Toast.makeText(getActivity(), ""+getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                }else {
                    boolean update = db.updatepassives(id,sname,sdesc,sprice,spricepermonth,sdate);
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
        ids.clear();
        names.clear();
        descriptions.clear();
        prices.clear();
        pricespermonth.clear();
        enddates.clear();
        String table = FinanceSQLiteHandler.TABLE_NAME_PASSIVES;
        Cursor result = db.getAlldatas(table);
        if (result.getCount() > 0 ){
            while (result.moveToNext()){
                listView.setVisibility(View.VISIBLE);
                String id = result.getString(0);
               String name = result.getString(1);
               String description = result.getString(2);
               String price = result.getString(3);
               String pricepermonth = result.getString(4);
               String enddate = result.getString(5);
                ids.add(id);
                names.add(name);
                descriptions.add(description);
                prices.add(price);
                pricespermonth.add(pricepermonth);
                enddates.add(enddate);
            }
            PassivesMainHelper passivesMainHelper =  new PassivesMainHelper(getActivity(),
                    names,descriptions,prices,pricespermonth,enddates,ids);
            passivesMainHelper.notifyDataSetChanged();
            listView.setAdapter(passivesMainHelper);
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
