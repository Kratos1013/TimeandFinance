package com.krintos.timeandfinance.Fragments.Finance.Category.ActivitiesMenu;

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
 * Created by root on 6/21/18.
 */

public class ActivityMainHelper extends ArrayAdapter<String> {
    private String[] name, description, date, initialprice, pricetoday;
    private Activity context;
    private String today ;
    public ActivityMainHelper(Activity context, ArrayList<String> names,ArrayList<String> descriptions,
                              ArrayList<String> dates,ArrayList<String> initialprices,ArrayList<String> pricestoday){
        super(context, R.layout.activity_main_helper,names);
        this.context = context;
        this.name = names.toArray(new String[0]);
        this.description = descriptions.toArray(new String[0]);
        this.date = dates.toArray(new String[0]);
        this.initialprice = initialprices.toArray(new String[0]);
        this.pricetoday = pricestoday.toArray(new String[0]);
        this.today = context.getResources().getString(R.string.today);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ActivityMainHelper.ViewHolder viewHolder = null;
        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.activity_main_helper, null,true);
            viewHolder = new ActivityMainHelper.ViewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder = (ActivityMainHelper.ViewHolder) r.getTag();
        }
        viewHolder.tname.setText(name[position]);
        viewHolder.desc.setText(description[position]);
        viewHolder.inprice.setText(initialprice[position]);
        viewHolder.date.setText(date[position]);
        viewHolder.toprice.setText(pricetoday[position]);
        viewHolder.todate.setText(today);
        viewHolder.tname.setSelected(true);
        viewHolder.desc.setSelected(true);


        return r;
    }
    class ViewHolder{
        TextView tname, desc, date,todate, inprice, toprice;
        ViewHolder(View v){
            tname = v.findViewById(R.id.name);
            desc = v.findViewById(R.id.description);
            date = v.findViewById(R.id.date);
            todate = v.findViewById(R.id.today);
            inprice = v.findViewById(R.id.initialprice);
            toprice = v.findViewById(R.id.pricetoday);
        }
    }

}
