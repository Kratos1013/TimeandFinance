package com.krintos.timeandfinance.Fragments.Finance.Category.PassivesMenu;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.krintos.timeandfinance.R;

import java.util.ArrayList;

/**
 * Created by root on 6/21/18.
 */

public class PassivesMainHelper extends ArrayAdapter<String> {
    private String[] name, description, price, pricepermonth,enddate;
    private Activity context;
    public PassivesMainHelper(Activity context, ArrayList<String> names, ArrayList<String> descriptions, ArrayList <String>prices,
                              ArrayList <String> pricespermonth, ArrayList<String> enddates){
        super(context, R.layout.passive_main_helper,names);
        this.context = context;
        this.name = names.toArray(new String[0]);
        this.description = descriptions.toArray(new String[0]);
        this.price = prices.toArray(new String[0]);
        this.pricepermonth = pricespermonth.toArray(new String[0]);
        this.enddate = enddates.toArray(new String[0]);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        PassivesMainHelper.ViewHolder viewHolder = null;
        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.passive_main_helper, null,true);
            viewHolder = new PassivesMainHelper.ViewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder = (PassivesMainHelper.ViewHolder) r.getTag();
        }
            viewHolder.tname.setText(name[position]);
            viewHolder.tdesc.setText(description[position]);
            viewHolder.tprice.setText(price[position]);
            viewHolder.tpricepermonth.setText(pricepermonth[position]);
            viewHolder.tenddate.setText(enddate[position]);
            viewHolder.tname.setSelected(true);
            viewHolder.tdesc.setSelected(true);
        return r;
    }
    class ViewHolder{
        TextView tname,tdesc,tprice,tpricepermonth,tenddate;
        ViewHolder(View v){
            tname = v.findViewById(R.id.tvname);
            tdesc = v.findViewById(R.id.tvdescription);
            tprice = v.findViewById(R.id.tvprice);
            tpricepermonth = v.findViewById(R.id.tvpricepermonth);
            tenddate = v.findViewById(R.id.tvenddate);
        }
    }
}
