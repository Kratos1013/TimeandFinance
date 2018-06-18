package com.krintos.timeandfinance.Fragments.Settings;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;


import java.util.ArrayList;
import java.util.Arrays;
/**
 * Created by root on 5/26/18.
 */

public class CategoryHelper extends ArrayAdapter<String> {
    private String [] category, icons;

    private Activity context;
    public CategoryHelper(Activity context, ArrayList<String> categories,ArrayList<String >icons){
        super(context, R.layout.category_helper, categories);
        this.context = context;
        this.category = categories.toArray(new String [0]);
        this.icons = icons.toArray(new String [0]);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CategoryHelper.ViewHolder viewHolder = null;
        if(r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.category_helper, null,true);
            viewHolder = new CategoryHelper.ViewHolder(r);
            r.setTag(viewHolder);
        }else {
            viewHolder = (CategoryHelper.ViewHolder) r.getTag();
        }

        viewHolder.name.setText(category[position]);
        if (icons[position]!=null){
            int id = getContext().getResources().getIdentifier(icons[position],"drawable",getContext().getPackageName());
            viewHolder.iconics.setImageResource(id);
        }

        return r;
    }
    class ViewHolder{
        TextView name;
        ImageView iconics;
        ViewHolder(View v){
            iconics = (ImageView) v.findViewById(R.id.icons);
            name = (TextView) v.findViewById(R.id.names);

        }
    }
}