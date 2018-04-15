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

import com.google.gson.Gson;

public class SelectOrder extends AppCompatActivity {

    private Waiter waiter1;
    private SharedPreferences mPrefs;
    private int seatNumber;
    private Button confirm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_order );
        confirm = (Button) findViewById(R.id.btnConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SelectTable.class);
                startActivity(intent);
            }
        });
        getSeatNumber();
    }
    public void getSeatNumber(){
        //get seat number for the waiter
        mPrefs = getSharedPreferences("pref1",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("waiter",null);
        waiter1=gson.fromJson(json,Waiter.class);
        seatNumber=  waiter1.currentSeatNumber;
        showSeats();
    }

    public void showSeats(){
        LinearLayout LinearLayoutView = (LinearLayout)findViewById(R.id.select_order);

        for( int j = 0; j < seatNumber; j++ ){
            Log.d("tag36","HEREEEEEEEEEEEEEEEEEEEEEEE");
            TextView textView1 = new TextView(this);
            textView1.setTextSize(25);
            textView1.setBackgroundColor(0xff66ff66);
            textView1.setPadding(150, 20, 20, 20);// in pixels (left, top, right, bottom)
            textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView1.setText("Seat Number "+(j+1)+" : ");
            Button myButton = new Button(this);
            myButton.setText("Select Meal");
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //load next page
                    Intent intent = new Intent(getBaseContext(), SelectMeal.class);
                    startActivity(intent);
                }
            });

//            textView1.append("\n");
            LinearLayoutView.addView(textView1);
            LinearLayoutView.addView(myButton);
        }

    }
    public void confirmTableOrder(){

    }
}
