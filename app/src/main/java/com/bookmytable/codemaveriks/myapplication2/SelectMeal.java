package com.bookmytable.codemaveriks.myapplication2;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SelectMeal extends AppCompatActivity {

    private List<String> sample = new ArrayList<String>();
    private List<String> menu = new ArrayList<String>();
    private List<String> finalMealList = new ArrayList<String>();
    private String path;
    private LinearLayout LinearLayoutView;
    private Spinner spinner;
    private String Item;
    private boolean flag=false;

    protected void onCreate(Bundle savedInstanceState) {
        sample.add("Select Food Item");sample.add("Food");sample.add("Drinks");sample.add("Desert");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_meal);
        LinearLayoutView = (LinearLayout) findViewById(R.id.select_meal);
        spinner = new Spinner(this);
        showAddedMeals();
        Button addFoodItem = new Button(this);
        final Button finish = new Button((this));
        addFoodItem.setText("Add Food Item");
        finish.setText("Finish");
        addFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutView.removeView(spinner);
                addFoodItem();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutView.addView(addFoodItem);
        LinearLayoutView.addView(finish);


    }

    public void showAddedMeals() {

    }

    public void addFoodItem() {
        flag=false;
        path="Meals";
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sample));
        LinearLayoutView.addView(spinner);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!flag){flag=true;}else {flag=false;generateSpinner(spinner.getSelectedItem().toString());}

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
    }
     public void generateSpinner(String item){
        Item=item;
        showToast("Selected item : "+item);
         FirebaseDatabase database = FirebaseDatabase.getInstance();
         Log.d("pathInitial",path);
         DatabaseReference myRef = database.getReference(path);
         myRef.child(item).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("XXXX",dataSnapshot.getValue().toString());
                 Iterable<DataSnapshot> contactChildren = dataSnapshot.getChildren();
                 Log.d("pathResult", String.valueOf(dataSnapshot.getChildrenCount()));
                 if (dataSnapshot.getChildrenCount() != 0) {
                     menu.clear();
                     menu.add("Select " + Item);
                     int counter = 0;
//                 if(path.equals("Meals")){path+="/"+item;}
                     if (Item.contains(" ")) {
                         Log.d("tag45", "space");
                     }
                     path += "/" + Item;
                     for (DataSnapshot contact : contactChildren) {
                         counter++;
                         String c = contact.getKey().toString();
                         menu.add(c);
                         showToast(c);
                         if (counter == dataSnapshot.getChildrenCount()) {
                             LinearLayoutView.removeView(spinner);
                             spinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, menu));
                             LinearLayoutView.addView(spinner);
                             spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                 @Override
                                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                     if (!flag) {
                                         flag = true;
                                     } else {
                                         flag = false;
//                                     path+="/"+spinner.getSelectedItem().toString();
                                         Log.d("tag44", "HEREEEE");
                                         generateSpinner(spinner.getSelectedItem().toString());
                                     }

                                 }

                                 @Override
                                 public void onNothingSelected(AdapterView<?> parent) {

                                 }

                             });
                         }
//                    addFoodItem();
                     }

                 }else {
                     final TextView textView1 = new TextView(getBaseContext());
                     finalMealList.add(Item);
                     textView1.setTextSize(25);
                     textView1.setBackgroundColor(0xff66ff66);
                     textView1.setPadding(150, 20, 20, 20);// in pixels (left, top, right, bottom)
                     textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                     textView1.setText(Item);
                     final Button addFoodItem = new Button(getBaseContext());
                     addFoodItem.setText("CANCEL ITEM");
                     addFoodItem.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             finalMealList.remove(Item);
                             LinearLayoutView.removeView(textView1);
                             LinearLayoutView.removeView(addFoodItem);
                         }
                     });


                     LinearLayoutView.addView(textView1);
                     LinearLayoutView.addView(addFoodItem);


                 }}
                 @Override
                 public void onCancelled (DatabaseError databaseError){

                 }

         });
         }
    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    }

