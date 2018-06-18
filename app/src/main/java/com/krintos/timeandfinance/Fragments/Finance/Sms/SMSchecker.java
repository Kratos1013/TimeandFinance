package com.krintos.timeandfinance.Fragments.Finance.Sms;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krintos.timeandfinance.Fragments.Helpers.SectionsPageAdapter;
import com.krintos.timeandfinance.R;
import com.krintos.timeandfinance.Services.FinanceService;

/**
 * A simple {@link Fragment} subclass.
 */
public class SMSchecker extends Fragment {

    public static final int MY_PERMISSIONS_REQUEST_READ_SMS = 99;
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 88;
    public static final int MY_PERMISSIONS_REQUEST_WINDOW = 77;

    private static final String TAG = "SMSchecker";
    private SectionsPageAdapter mSectionPageAdapter;


    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public SMSchecker() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_smschecker, container, false);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        checkthepermission();

        //load the listview

        return rootView;
    }
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new spent(), "Расходы");

        adapter.addFragment(new income(), "Доходы");
        viewPager.setAdapter(adapter);
    }

    private void checkthepermission() {
        // Here, thisActivity is the current activity
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED ) {
            //permission is not granted. ask permission
            requestPermissions(new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);

        }else {
            gotpermission();
        }
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
        }if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                    MY_PERMISSIONS_REQUEST_WINDOW);
        }
        else {
            gotpermission();
        }

    }


    public  void gotpermission() {
        mSectionPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        startservice();
    }

    private void startservice() {
        Intent service = new Intent(getContext(), FinanceService.class);
        getActivity().getApplicationContext().startService(service);
    }


    private void permissiondenied() {
        checkthepermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    gotpermission();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permissiondenied();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WINDOW:{
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}
