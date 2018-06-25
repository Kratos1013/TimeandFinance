package com.krintos.timeandfinance.Fragments.Finance.Category.Add;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class add_to_collections extends Fragment {
    private EditText aim, full, permonth, now;
    private Button cancel, save;
    private FinanceSQLiteHandler db;
    public add_to_collections() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_to_collections, container, false);
        aim = rootView.findViewById(R.id.aim);
        full = rootView.findViewById(R.id.collectionsfull);
        permonth = rootView.findViewById(R.id.collectionspermonth);
        now = rootView.findViewById(R.id.collectionsnow);
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
                String gaim = aim.getText().toString().trim();
                String gfull = full.getText().toString().trim();
                String gpermonth = permonth.getText().toString().trim();
                String gnow = now.getText().toString().trim();
                if (gaim.equals("") || gfull.equals("") || gpermonth.equals("") || gnow.equals("")){
                    Toast.makeText(getActivity(), ""+getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                }else {
                    addtocollections(gaim,gfull,gpermonth,gnow);
                }
            }
        });
        return rootView;
    }

    private void addtocollections(String gaim, String gfull, String gpermonth, String gnow) {
        boolean inserttocollections = db.inserttocollections(gaim,gfull,gnow,gpermonth);
        if (inserttocollections){
            Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }else {
            Toast.makeText(getContext(), "Not Added", Toast.LENGTH_SHORT).show();
        }
    }

}
