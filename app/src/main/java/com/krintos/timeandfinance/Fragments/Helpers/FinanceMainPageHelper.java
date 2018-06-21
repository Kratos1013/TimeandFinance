package com.krintos.timeandfinance.Fragments.Helpers;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krintos.timeandfinance.R;

import java.util.ArrayList;

/**
 * Created by root on 6/19/18.
 */

public class FinanceMainPageHelper extends ArrayAdapter<String> {
    private String[] categoryname;
    private Float[] amounts;
    private String type;
    private Activity context;

    public FinanceMainPageHelper(Activity context, ArrayList<String> categories, ArrayList<Float> amount,String type){
        super(context, R.layout.finance_main_page_helper, categories);
        this.context = context;
        this.categoryname = categories.toArray(new String[0]);
        this.amounts = amount.toArray(new Float[0]);
        this.type = type;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        FinanceMainPageHelper.ViewHolder viewHolder = null;
        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.finance_main_page_helper, null,true);
            viewHolder = new FinanceMainPageHelper.ViewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder = (FinanceMainPageHelper.ViewHolder) r.getTag();
        }
        if (type.equals("income")){

            viewHolder.cat.setText(categoryname[position]);
            viewHolder.cat.setTextColor(context.getResources().getColor(R.color.ts2));
            String floattostring = String.valueOf(amounts[position]);
            String price = "+"+floattostring+"p";
            viewHolder.amount.setText(price);
            viewHolder.amount.setTextColor(context.getResources().getColor(R.color.ts2));
        }else if (type.equals("spent")){
            viewHolder.cat.setTextColor(context.getResources().getColor(R.color.tsred));
            viewHolder.cat.setText(categoryname[position]);
            String floattostring = String.valueOf(amounts[position]);
            String price = "-"+floattostring+"p";
            viewHolder.amount.setText(price);
            viewHolder.amount.setTextColor(context.getResources().getColor(R.color.tsred));
        }
        return r;
    }
    class ViewHolder{
        TextView cat, amount;
        ViewHolder(View v){
            cat = (TextView) v.findViewById(R.id.categories);
            amount = (TextView) v.findViewById(R.id.prices);
        }
    }
}
