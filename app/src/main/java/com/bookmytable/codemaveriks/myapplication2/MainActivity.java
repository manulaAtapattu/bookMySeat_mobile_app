package com.bookmytable.codemaveriks.myapplication2;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText index;
    private EditText password;
    private TextView Info;
    private Button Login;
    private int counter=5;
    public Waiter waiter1=new Waiter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        index = (EditText)findViewById(R.id.etIndex);
        password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvInfo);
        Login = (Button) findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            //            System.out.println("here");
            @Override
            public void onClick(View v) {
                    validate(index.getText().toString(),password.getText().toString());
            }
        });
    }

    private void validate (String UserIndex, final String UserPassword) {

        //code to connect to database, assign myRef to Reference 'message2'
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Waiters");

        //validate password
        myRef.child(UserIndex.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("tag9",dataSnapshot.child("firstName").getValue().toString());

                if(dataSnapshot.child("password").getValue().toString().equals(UserPassword)){
                    Log.d("tag10",dataSnapshot.child("firstName").getValue().toString());
                    //enter values into a waiter object for future use
                    waiter1.firstName= dataSnapshot.child("firstName").getValue().toString();
                    waiter1.lastName= dataSnapshot.child("lastName").getValue().toString();
                    waiter1.email= dataSnapshot.child("email").getValue().toString();
                    String restaurants= dataSnapshot.child("restaurants").getValue().toString();
                    String[] restaurantList= restaurants.split(",");
                    waiter1.restaurants=restaurantList;

                    SharedPreferences mPrefs = getSharedPreferences("pref1",MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor=mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(waiter1);
                    prefsEditor.putString("waiter", json);
                    prefsEditor.commit();
                    Log.d("tag15", String.valueOf(mPrefs.contains("waiter")));
                    Log.d("tag16", String.valueOf(mPrefs.getAll()));
                    nextPage();
                }else{
                    counter--;
                    Info.setText("No of attempts remaining"+String.valueOf(counter));
                    if (counter ==0){
                        Login.setEnabled(false);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }

        public void nextPage(){
            Intent intent = new Intent(getBaseContext(), HomePage.class);
            startActivity(intent);
        }
    }
