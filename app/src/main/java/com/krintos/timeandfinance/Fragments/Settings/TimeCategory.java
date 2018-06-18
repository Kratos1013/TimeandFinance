package com.krintos.timeandfinance.Fragments.Settings;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeCategory extends Fragment {
    public ListView listView;
    public ArrayList<String> categories = new ArrayList<String >();
    public FinanceSQLiteHandler db;

    public TimeCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_time_category, container, false);
        listView = (ListView) rootView.findViewById(R.id.listviewforcategory);
        showcategories();
        return rootView;
    }
    private void showcategories() {
        db = new FinanceSQLiteHandler(getContext());
        Cursor result = db.getCategoryNames(FinanceSQLiteHandler.TABLE_CATEGORIES_Spent);
        if (result.getCount()==0){
            //no category found
        }
        while (result.moveToNext()){
            String categoryname = result.getString(1);
            categories.add(categoryname);
        }
        /*CategoryHelper categoryHelper = new CategoryHelper(getActivity(),categories,FinanceSQLiteHandler.TABLE_CATEGORIES_Spent);
        categoryHelper.notifyDataSetChanged();
        listView.setAdapter(categoryHelper);*/
    }

}
