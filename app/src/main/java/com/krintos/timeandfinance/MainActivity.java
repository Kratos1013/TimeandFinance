package com.krintos.timeandfinance;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.Fragments.Finance.Finance_Main_Page;
import com.krintos.timeandfinance.Fragments.Settings.Settings;
import com.krintos.timeandfinance.Fragments.Timing;
import com.krintos.timeandfinance.Services.FinanceService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        startservice();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        FinanceSQLiteHandler db = new FinanceSQLiteHandler(getApplicationContext());
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String store = sharedPref.getString("store","");
        if (store.equals("")){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("store", "saved");
            editor.commit();
          //  db.addfinancecategoryimages();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Creating custom shared preferences
    }

    private void startservice() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED ) {
            //permission is granted
            Intent service = new Intent(this, FinanceService.class);
            startService(service);
        }
    }

    /*private void autoincreamentcategories() {
        String MY_PREFS_NAME = "category_names";
        SharedPreferences getPreferences  = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String category1 = getPreferences.getString(getString(R.string.category1), getString(R.string.category1));
        String category2 = getPreferences.getString(getString(R.string.category1), getString(R.string.category1));
        String category3 = getPreferences.getString(getString(R.string.category1), getString(R.string.category1));
        String category4 = getPreferences.getString(getString(R.string.category1), getString(R.string.category1));

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("name", "Elena");
        editor.putInt("idName", 12);
        editor.apply();
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            toolbar.setTitle(getResources().getString(R.string.settings));
            Fragment fragment = new Settings();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack("settings");
            ft.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timing) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbar.setTitle(getResources().getString(R.string.timing));

                    Fragment fragment1 = new  Timing();
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main, fragment1);
                    ft.addToBackStack("timing");

                    ft.commit();
                }
            }, 250);


        } else if (id == R.id.nav_finance) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbar.setTitle(getResources().getString(R.string.finance));
                    Fragment fragment = new Finance_Main_Page();
                        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                       ft.replace(R.id.content_main, fragment);
                       ft.addToBackStack("finance");
                       ft.commit();
                }
            }, 300);
        } else if (id == R.id.nav_learning) {

        } else if (id == R.id.nav_setting) {
            toolbar.setTitle(getResources().getString(R.string.settings));
            Fragment fragment = new Settings();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack("settings");
            ft.commit();
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
