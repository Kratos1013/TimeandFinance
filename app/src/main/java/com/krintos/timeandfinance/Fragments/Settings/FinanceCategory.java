package com.krintos.timeandfinance.Fragments.Settings;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.krintos.timeandfinance.Database.FinanceSQLiteHandler;
import com.krintos.timeandfinance.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinanceCategory extends Fragment {
    public ListView listView;
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> icons = new ArrayList<>();
    public FinanceSQLiteHandler db;
    public FloatingActionButton add;
    List<CarouselPicker.PickerItem> images = new ArrayList<>();
    List<String> pickedimages = new ArrayList<>();
    public int picked=0, table =0;
    public Spinner spinner;
    public List<String> array = new ArrayList<>();

    public FinanceCategory() {
        // Required empty public constructor

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finance_category, container, false);
        listView =  rootView.findViewById(R.id.listviewforcategory);
        add =  rootView.findViewById(R.id.add);
        spinner =  rootView.findViewById(R.id.tablename);
        array.add("расходы");
        array.add("доходы");
        ArrayAdapter<String> tablenames = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,array);
        tablenames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(tablenames);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                table = position;
                showcategories(table);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                table = 0;
                showcategories(table);

            }
        });
        addicons();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewcategory();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pickedname = ((TextView)view.findViewById(R.id.names)).getText().toString();
                categorychangedialog(pickedname);
            }
        });


        return rootView;
    }

    private void categorychangedialog(final String pickedname) {
        picked = 0;
        AlertDialog.Builder change_dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.change_category, null);
        change_dialog.setView(dialog);
        //get id
        final LinearLayout ask =  dialog.findViewById(R.id.ask);
        final LinearLayout showchange =  dialog.findViewById(R.id.showchange);
        final ImageView remove =  dialog.findViewById(R.id.delete);
        ImageView change =  dialog.findViewById(R.id.change);
        final Button cancel =  dialog.findViewById(R.id.cancel);
        final Button save =  dialog.findViewById(R.id.save);
        CarouselPicker iconpicker =  dialog.findViewById(R.id.iconpicker);
        CarouselPicker.CarouselViewAdapter iconadapter = new CarouselPicker.CarouselViewAdapter(getActivity(), images,0);
        iconpicker.setAdapter(iconadapter);
        final EditText name =  dialog.findViewById(R.id.category_name);
        name.setText(pickedname);
        final AlertDialog alertDialog = change_dialog.create();

        iconpicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                picked = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissiontoremove(pickedname);
                alertDialog.dismiss();

            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask.setVisibility(View.GONE);
                showchange.setVisibility(View.VISIBLE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newname1 = name.getText().toString();
                        String  newname = newname1.replace("\"", "");
                        if (newname.equals("")){
                            Toast.makeText(getActivity(), ""+getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                        }else{
                        String pickedtable;
                        String pickedtablename;
                        if (table==0){
                            pickedtable = FinanceSQLiteHandler.Table_Name_spent;
                            pickedtablename = FinanceSQLiteHandler.TABLE_CATEGORIES_Spent;
                        }else {
                            pickedtable = FinanceSQLiteHandler.Table_Name_income;
                            pickedtablename = FinanceSQLiteHandler.TABLE_CATEGORIES_Income;
                        }


                        //Drawable d = pickedimages.get(picked);
                            String di = pickedimages.get(picked);
                        /*Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();*/

                       // Cursor result = db.IsCategoryExist(newname, pickedtablename);
                       // if (result.getCount() ==0){
                          boolean update =  db.updatecategoryname(pickedname,newname,di,pickedtablename);
                          if (update){
                              Cursor result = db.IsCategoryExist(pickedname,pickedtable);
                              if (result.getCount() == 0){
                                  alertDialog.dismiss();
                                  showcategories(table);
                                  Toast.makeText(getActivity(), ""+getString(R.string.updated), Toast.LENGTH_SHORT).show();

                              }
                              if (result.getCount()>0){
                                  boolean updateall = db.updatedatas(pickedname,newname,pickedtable);
                                  if (updateall) {
                                      alertDialog.dismiss();
                                      showcategories(table);
                                      Toast.makeText(getActivity(), "" + getString(R.string.updated), Toast.LENGTH_SHORT).show();
                                  }else {
                                      Toast.makeText(getActivity(), ""+getString(R.string.noupdateall), Toast.LENGTH_SHORT).show();
                                  }
                              }

                                    }else {
                              Toast.makeText(getActivity(), ""+getString(R.string.notupdated), Toast.LENGTH_SHORT).show();
                          }
                       /* }else {
                            Toast.makeText(getActivity(), ""+getString(R.string.cat_axist), Toast.LENGTH_SHORT).show();
                        }*/

                        }

                    }
                });
            }
        });
        alertDialog.show();


    }

    private void permissiontoremove(final String pickedname) {
        final String pickedtablename;
        final String pickedtablename2;
        if (table==0){
            pickedtablename = FinanceSQLiteHandler.TABLE_CATEGORIES_Spent;
            pickedtablename2 = FinanceSQLiteHandler.Table_Name_spent;
        }else {
            pickedtablename = FinanceSQLiteHandler.TABLE_CATEGORIES_Income;
            pickedtablename2 = FinanceSQLiteHandler.Table_Name_income;
        }
        final AlertDialog permission = new AlertDialog.Builder(getActivity()).create();
        permission.setTitle(""+getString(R.string.remove_permission));
        permission.setMessage(""+getString(R.string.cautiontoremove));
        permission.setButton(AlertDialog.BUTTON_NEGATIVE, "" + getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permission.dismiss();
            }
        });
        permission.setButton(AlertDialog.BUTTON_POSITIVE, "" + getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer deleteRaws = db.deletecategory(pickedtablename,pickedtablename2,pickedname);
                if (deleteRaws >0){
                    showcategories(table);
                    Toast.makeText(getActivity(), ""+getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), ""+getString(R.string.notdeleted), Toast.LENGTH_SHORT).show();
                }
            }
        });
       permission.show();
    }

    private void addnewcategory() {
        picked = 0;

        AlertDialog.Builder add_dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.add_new_category, null);
        add_dialog.setView(dialog);
        CarouselPicker iconpicker = (CarouselPicker) dialog.findViewById(R.id.iconpicker);
        CarouselPicker.CarouselViewAdapter iconadapter = new CarouselPicker.CarouselViewAdapter(getActivity(), images,0);
        iconpicker.setAdapter(iconadapter);

        final EditText name = (EditText) dialog.findViewById(R.id.category_name);
        Button add = (Button) dialog.findViewById(R.id.add);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        final AlertDialog alertDialog = add_dialog.create();

        iconpicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                picked = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            alertDialog.dismiss();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pickedtablename;
                if (table==0){
                    pickedtablename = FinanceSQLiteHandler.TABLE_CATEGORIES_Spent;
                }else {
                    pickedtablename = FinanceSQLiteHandler.TABLE_CATEGORIES_Income;
                }
                String cat_name = name.getText().toString().trim();
                /*Drawable d = pickedimages.get(picked);
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();*/
                String di = pickedimages.get(picked);
                String  categoryName = cat_name.replace("\"", "");

                if(!categoryName.equals("")) {
                    Cursor result = db.IsCategoryExist(categoryName, pickedtablename);
                    if (result.getCount() == 0) {
                        boolean isAdded = db.addcategorynamewithicon(categoryName, di,pickedtablename);
                        if (isAdded) {
                            alertDialog.dismiss();
                            categories.clear();
                            showcategories(table);
                            Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Something wrong", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "Category name already exist", Toast.LENGTH_SHORT).show();
                    }
                    } else {
                        Toast.makeText(getActivity(), "Category name cant be empty", Toast.LENGTH_SHORT).show();
                    }



            }
        });
        alertDialog.show();
    }

    private void showcategories(int tablenumber) {
        categories.clear();
        icons.clear();
        String pickedtablename;
        if (tablenumber==0){
            pickedtablename = FinanceSQLiteHandler.TABLE_CATEGORIES_Spent;
        }else {
            pickedtablename = FinanceSQLiteHandler.TABLE_CATEGORIES_Income;
        }
        db = new FinanceSQLiteHandler(getContext());
        Cursor result = db.getCategoryNames(pickedtablename);
        if (result.getCount()==0){
            //no category found
        }
        while (result.moveToNext()){
            String categoryname = result.getString(1);
            String icon = result.getString(2);
            categories.add(categoryname);
            icons.add(icon);

        }
        CategoryHelper categoryHelper = new CategoryHelper(getActivity(),categories,icons);
        categoryHelper.notifyDataSetChanged();
        listView.setAdapter(categoryHelper);

    }
    private void addicons() {
        images.add(new CarouselPicker.DrawableItem(R.drawable.food));
        pickedimages.add("food");
        images.add(new CarouselPicker.DrawableItem(R.drawable.transport));
        pickedimages.add("transport");
        images.add(new CarouselPicker.DrawableItem(R.drawable.electronics));
        pickedimages.add("electronics");



    }
}
