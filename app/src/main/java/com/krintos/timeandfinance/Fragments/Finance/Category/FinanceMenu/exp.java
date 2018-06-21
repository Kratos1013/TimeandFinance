package com.krintos.timeandfinance.Fragments.Finance.Category.FinanceMenu;


import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class exp extends Fragment {
    private FinanceSQLiteHandler db;
    private String link,name;
    private ImageView image;
    public exp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exp, container, false);
        db = new FinanceSQLiteHandler(getActivity().getApplicationContext());
        image = (ImageView) rootView.findViewById(R.id.show);

        insertfirst();
        return rootView;
    }

    private void insertfirst() {
        String link = "food";
        boolean result = db.insertnew(link,"blablabla");
        if (result){
            showfromdb();
        }else {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void showfromdb() {
        Cursor result = db.getnewicon(FinanceSQLiteHandler.TABLE_NEW_ICON);
        while (result.moveToNext()){
           link  = result.getString(2);
            name = result.getString(1);

        }
        Toast.makeText(getActivity(), ""+ name, Toast.LENGTH_SHORT).show();

       int id = getResources().getIdentifier(link, "drawable",getActivity().getPackageName());
       image.setImageResource(id);
    }


}
