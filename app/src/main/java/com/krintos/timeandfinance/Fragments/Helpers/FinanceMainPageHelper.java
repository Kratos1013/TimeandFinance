package com.krintos.timeandfinance.Fragments.Helpers;

import android.app.Activity;
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
    private Activity context;

    public FinanceMainPageHelper(Activity context, ArrayList<String> categories, ArrayList<Float> amount){
        super(context, R.layout.finance_main_page_helper, categories);
        this.context = context;
        this.categoryname = categories.toArray(new String[0]);
        this.amounts = amount.toArray(new Float[0]);

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
        viewHolder.cat.setText(categoryname[position]);
        String floattostring = String.valueOf(amounts[position]);
        String price = "-"+floattostring+"p";
        viewHolder.amount.setText(price);
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
