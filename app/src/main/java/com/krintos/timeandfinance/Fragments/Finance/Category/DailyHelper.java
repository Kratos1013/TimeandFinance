package com.krintos.timeandfinance.Fragments.Finance.Category;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.util.ArrayList;

/**
 * Created by root on 5/30/18.
 */

public class DailyHelper extends ArrayAdapter<String>{
    private String []  description, amount, category,table, icons;
    private Integer[] color;
    private Activity context;
    private FinanceSQLiteHandler db;
    public DailyHelper(Activity context,ArrayList<String> gcategory,ArrayList<String> gdescription,ArrayList<String> gamount, ArrayList<String> gtable,ArrayList<Integer> colors,ArrayList<String> icons){
        super(context, R.layout.daylyfinancehelper, gdescription);
        this.context  = context;
        this.description = gdescription.toArray(new String[0]);
        this.category = gcategory.toArray(new String[0]);
        this.amount = gamount.toArray(new String[0]);
        this.table = gtable.toArray(new String[0]);
        this.color = colors.toArray(new Integer[0]);
        this.icons = icons.toArray(new String [0]);
        this.db = new FinanceSQLiteHandler(getContext().getApplicationContext());

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        DailyHelper.ViewHolder viewHolder = null;
        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.daylyfinancehelper, null,true);
            viewHolder = new DailyHelper.ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (DailyHelper.ViewHolder) r.getTag();
        }
        viewHolder.desc.setText(description[position]);
        viewHolder.amount.setText(amount[position]);
        viewHolder.cat.setText(category[position]);
        viewHolder.amount.setTextColor(color[position]);
        Cursor result = db.geticon(table[position],category[position]);
        while (result.moveToNext()){
            String cat = result.getString(1);
            if (cat.equals(category[position])){
                String iconics = result.getString(2);
                if (iconics != null){
                    int id = getContext().getResources().getIdentifier(iconics,"drawable",getContext().getPackageName());
                    viewHolder.icon.setImageResource(id);
                }
            }
        }

        return r;

    }
    class ViewHolder{
        TextView  desc,cat, amount;
        ImageView icon;
        ViewHolder(View v){
            desc = (TextView) v.findViewById(R.id.description);
            cat = (TextView) v.findViewById(R.id.category);
            amount = (TextView) v.findViewById(R.id.amount);
            icon = (ImageView) v.findViewById(R.id.icons);

        }
    }
}
