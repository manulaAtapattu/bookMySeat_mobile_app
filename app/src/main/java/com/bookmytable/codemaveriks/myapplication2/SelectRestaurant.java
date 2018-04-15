package com.bookmytable.codemaveriks.myapplication2;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SelectRestaurant extends AppCompatActivity {

    public String[] restaurantList;
    public String[] names;
    public String[] locations;
    private SharedPreferences mPrefs;
    Waiter waiter1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_restaurant);
        getRestaurants();

    }

    public void getRestaurants(){
        //get restaurantList for the waiter
        mPrefs = getSharedPreferences("pref1",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("waiter",null);
        waiter1=gson.fromJson(json,Waiter.class);
        restaurantList=  waiter1.restaurants;

        names=new String[ restaurantList.length];
        locations=new String[ restaurantList.length];

        //get information about the restaurants
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Restaurants");
        Log.d("tag28","HEREEEEEEEEEEEEEEEEEEEEE");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (int i=0;i<restaurantList.length;i++){
                        Log.d("tag25",dataSnapshot.child(restaurantList[i]).child("name").getValue().toString());
                        names[i]=dataSnapshot.child(restaurantList[i]).child("name").getValue().toString();
                        locations[i]=dataSnapshot.child(restaurantList[i]).child("location").getValue().toString();
                    }
                    Log.d("29","HEREEEEEEEEEEEEEEEEEEEEE");
                    Log.d("tag30",restaurantList[0]);
                    Log.d("tag31",restaurantList[1]);
                    showRestaurants();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        Log.d("tag33","HEREEEEEEEEEEEEEEEEEEEEE");

    }


    public void showRestaurants(){

        LinearLayout LinearLayoutView = (LinearLayout)findViewById(R.id.ll_example);
        Log.d("tag32",restaurantList[0]);
        for( int j = 0; j < restaurantList.length; j++ ){
            Log.d("tag31","HEREEEEEEEEEEEEEEEEEEEEEEE");
            TextView textView1 = new TextView(this);
            textView1.setTextSize(25);
            textView1.setBackgroundColor(0xff66ff66);
            textView1.setPadding(150, 20, 20, 20);// in pixels (left, top, right, bottom)
            textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView1.setText(restaurantList[j]+" : "+names[j]+" : "+locations[j]);
            Button myButton = new Button(this);
            myButton.setText("SELECT");
            final int finalJ = j;
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update waiter in mPrefs with current Restaurant
                    waiter1.currentRestaurant=restaurantList[finalJ];
                    SharedPreferences.Editor prefsEditor=mPrefs.edit();
                    Gson gson2 = new Gson();
                    String json2 = gson2.toJson(waiter1);
                    prefsEditor.putString("waiter", json2);
                    prefsEditor.commit();

                    //load next page
                    Intent intent = new Intent(getBaseContext(), SelectTable.class);
                    startActivity(intent);
                }
            });

//            textView1.append("\n");
            LinearLayoutView.addView(textView1);
            LinearLayoutView.addView(myButton);
        }
    }
}
