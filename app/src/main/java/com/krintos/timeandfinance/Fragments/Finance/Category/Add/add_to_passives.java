package com.krintos.timeandfinance.Fragments.Finance.Category.Add;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class add_to_passives extends Fragment {
    private EditText name, description, price, pricepermonth;
    private TextView pickdate,enddate;
    private Button cancel, save;
    private FinanceSQLiteHandler db;
    public add_to_passives() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_to_passives, container, false);
        name = rootView.findViewById(R.id.passivename);
        description = rootView.findViewById(R.id.passivedescription);
        price = rootView.findViewById(R.id.price);
        pricepermonth = rootView.findViewById(R.id.pricepermonth);
        pickdate = rootView.findViewById(R.id.pickDate);
        enddate = rootView.findViewById(R.id.enddate);
        cancel = rootView.findViewById(R.id.cancel);
        save = rootView.findViewById(R.id.save);
        db = new FinanceSQLiteHandler(getContext());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String sname = name.getText().toString().trim();
                    String sdescription = description.getText().toString().trim();
                    String sprice= pricepermonth.getText().toString().trim();
                    String spricepermonth = pricepermonth.getText().toString().trim();
                    String senddate = enddate.getText().toString().trim();
                if (sname.equals("")||sdescription.equals("")||sprice.equals("")||spricepermonth.equals("")
                        ||senddate.equals("")){
                    Toast.makeText(getActivity(), ""+getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                }else {
                    addtopassives(sname,sdescription,sprice,spricepermonth,senddate);
                }
            }
        });
        return rootView;
    }

    private void addtopassives(String sname, String sdescription, String sprice, String spricepermonth, String senddate) {
        boolean insert = db.inserttopassive(sname,sdescription,sprice,spricepermonth,senddate);
        if (insert){
            Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }else {
            Toast.makeText(getActivity(), "Not Added", Toast.LENGTH_SHORT).show();
        }

    }

}
