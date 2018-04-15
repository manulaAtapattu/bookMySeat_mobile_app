package com.bookmytable.codemaveriks.myapplication2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class SelectTable extends AppCompatActivity {

    private Waiter waiter1;
    private String currentRestaurant;
    private String tempRoomTable;
    private String tempTableSize;
    private String[] roomTable;
    private String[] tableSize;
    private SharedPreferences mPrefs;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_table);
        getLayout();
    }
    public void getLayout(){

        mPrefs = getSharedPreferences("pref1",MODE_PRIVATE);
        Log.d("sharedPref",mPrefs.getAll().toString());
        Gson gson = new Gson();
        String json = mPrefs.getString("waiter",null);
        waiter1=gson.fromJson(json,Waiter.class);
        currentRestaurant=  waiter1.currentRestaurant;
        Log.d("currentRestaurant",currentRestaurant);

        //get layout of the restaurant
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Restaurants");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempRoomTable=dataSnapshot.child(currentRestaurant).child("roomTable").getValue().toString();
                tempTableSize=dataSnapshot.child(currentRestaurant).child("tableSize").getValue().toString();
                roomTable=tempRoomTable.split(",");
                tableSize=tempTableSize.split(",");
                showLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void showLayout(){

        LinearLayout LinearLayoutView = (LinearLayout)findViewById(R.id.lll_example);

        for( int j = 0; j < roomTable.length; j++ ){
            Log.d("tag33","HEREEEEEEEEEEEEEEEEEEEEEEE");
            TextView textView1 = new TextView(this);
            textView1.setTextSize(25);
            textView1.setBackgroundColor(0xff66ff66);
            textView1.setPadding(150, 20, 20, 20);// in pixels (left, top, right, bottom)
            textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView1.setText(roomTable[j]+" : "+tableSize[j]);
            Button myButton = new Button(this);
            myButton.setText("SELECT");
            final int finalJ = j;

            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update waiter in sharedPreferences with table number
                    waiter1.currentTable=roomTable[finalJ];
                    waiter1.currentSeatNumber=Integer.parseInt(tableSize[finalJ]);
                    SharedPreferences.Editor prefsEditor=mPrefs.edit();
                    Gson gson2 = new Gson();
                    String json2 = gson2.toJson(waiter1);
                    prefsEditor.putString("waiter", json2);
                    prefsEditor.commit();

                    Intent intent = new Intent(getBaseContext(), SelectOrder.class);
                    startActivity(intent);
                }
            });

            LinearLayoutView.addView(textView1);
            LinearLayoutView.addView(myButton);
        }
    }
}
