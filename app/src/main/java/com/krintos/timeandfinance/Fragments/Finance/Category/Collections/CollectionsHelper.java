package com.krintos.timeandfinance.Fragments.Finance.Category.Collections;

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
 * Created by root on 6/25/18.
 */

public class CollectionsHelper extends ArrayAdapter<String> {
    private String [] aim, full, permonth, now;
    private Activity context;
    public CollectionsHelper(Activity context, ArrayList<String> aim,ArrayList<String> full,ArrayList<String> permonth,
                             ArrayList<String> now){
        super(context, R.layout.collection_helper, aim);
        this.context = context;
        this.aim = aim.toArray(new String[0]);
        this.full = full.toArray(new String[0]);
        this.permonth = permonth.toArray(new String[0]);
        this.now = now.toArray(new String[0]);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CollectionsHelper.ViewHolder viewHolder = null;
        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.collection_helper, null,true);
            viewHolder = new CollectionsHelper.ViewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder = (CollectionsHelper.ViewHolder) r.getTag();
        }

        viewHolder.taim.setText(aim[position]);
        viewHolder.tfull.setText(full[position]);
        viewHolder.tpermonth.setText(permonth[position]);
        viewHolder.tnow.setText(now[position]);

        return r;
    }
    class ViewHolder{
        TextView taim,tfull,tpermonth,tnow;
        ViewHolder(View v){
            taim = v.findViewById(R.id.aim);
            tfull = v.findViewById(R.id.full);
            tpermonth = v.findViewById(R.id.permonth);
            tnow = v.findViewById(R.id.now);
        }
    }
}
