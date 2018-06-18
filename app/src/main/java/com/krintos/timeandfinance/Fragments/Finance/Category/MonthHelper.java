package com.krintos.timeandfinance.Fragments.Finance.Category;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.krintos.timeandfinance.R;

import java.util.ArrayList;

/**
 * Created by root on 6/10/18.
 */

public class MonthHelper extends ArrayAdapter<Float>{
    private Float [] income, spent, total;
    private String [] month;
    private Activity context;
    public MonthHelper(Activity context, ArrayList<Float> mincome, ArrayList<Float> mspent, ArrayList<Float> mtotal,ArrayList<String> month){
        super(context, R.layout.month_helper,mtotal );
        this.context = context;
        this.income = mincome.toArray(new Float[0]);
        this.spent = mspent.toArray(new Float[0]);
        this.total = mtotal.toArray(new Float[0]);
        this.month = month.toArray(new String[0]);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View r = convertView;
    MonthHelper.ViewHolder viewHolder = null;
    if (r==null){
        LayoutInflater layoutInflater = context.getLayoutInflater();
        r = layoutInflater.inflate(R.layout.month_helper, null,true);
        viewHolder = new MonthHelper.ViewHolder(r);
        r.setTag(viewHolder);
    }else{
        viewHolder = (MonthHelper.ViewHolder) r.getTag();
    }

        viewHolder.tincome.setText("+" + income[position] + "p");
        viewHolder.tspent.setText("-" + spent[position] + "p");
        viewHolder.months.setText("" + month[position]);
        if (total[position] > 0) {
            viewHolder.ttotal.setText("+" + total[position] + "p");
        } else {
            viewHolder.ttotal.setText("" + total[position] + "p");
        }
    return r;
    }

    class ViewHolder{
        TextView tincome,tspent,ttotal, months;
        ViewHolder(View v){
           tincome = v.findViewById(R.id.incometotal);
           tspent = v.findViewById(R.id.spenttotal);
           ttotal = v.findViewById(R.id.total);
           months = v.findViewById(R.id.month);
        }
    }
}
