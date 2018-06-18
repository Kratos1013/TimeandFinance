package com.krintos.timeandfinance.Fragments.Finance.Sms;

import android.app.Activity;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;

/**
 * Created by root on 5/19/18.
 */

public class Sms_manager_Spent extends ArrayAdapter<String> {
    private String [] sms_time, sms_place, sms_amount;
    private Activity context;
    public FinanceSQLiteHandler db;

    public Sms_manager_Spent(Activity context, ArrayList<String> sms_time, ArrayList<String> sms_place, ArrayList<String> sms_amount){
        super(context, R.layout.sms_helper_spent, sms_time);
        this.context = context;
        this.sms_place = sms_place.toArray(new String[0]);
        this.sms_time = sms_time.toArray(new String[0]);
        this.sms_amount = sms_amount.toArray(new String[0]);
        this.db = new FinanceSQLiteHandler(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.sms_helper_spent, null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) r.getTag();

        }
        viewHolder.ttime.setText(sms_time[position]);
        viewHolder.tplace.setText(sms_place[position]);
        viewHolder.tamount.setText(sms_amount[position]);
        Cursor result = db.IsDataExist(sms_place[position],sms_time[position],sms_amount[position],FinanceSQLiteHandler.Table_Name_spent);

        if (result.getCount()==0){
                viewHolder.background.setBackgroundResource(R.color.sms_notadded);
        }else {
            viewHolder.background.setBackgroundResource(R.color.sms_added);
        }

        return r;
    }
    class ViewHolder{
        TextView ttime, tplace, tamount;
        LinearLayout background;
        ViewHolder(View v){
        ttime =  v.findViewById(R.id.time);
        tplace=  v.findViewById(R.id.where);
        tamount =  v.findViewById(R.id.amount);
        background =  v.findViewById(R.id.listview_background);
        }
    }
}
