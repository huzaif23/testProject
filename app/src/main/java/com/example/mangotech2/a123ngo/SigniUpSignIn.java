package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SigniUpSignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signi_up_sign_in);

        SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
        String user_id = prefs.getString("user_id","no user_id");
        if(!user_id.equals("no user_id")) {
        finish();
       //     super.onDestroy();
        }
            Button btnsignup=(Button)findViewById(R.id.btnsignupfirst);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent n=new Intent(SigniUpSignIn.this,MainActivity.class);
                    startActivity(n);
                }catch (Exception e){

                }
            }
        });
        Button btnsignin=(Button)findViewById(R.id.btnsigninfirst);
        btnsignin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent n = new Intent(SigniUpSignIn.this, Signin.class);
                    startActivity(n);
                }catch (Exception e){

                }
            }
        });

    }

}
