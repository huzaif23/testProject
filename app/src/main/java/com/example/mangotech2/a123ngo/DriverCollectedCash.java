package com.example.mangotech2.a123ngo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;

public class DriverCollectedCash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_collected_cash);
    }

    @Override
    public void onBackPressed() {

    }

}
