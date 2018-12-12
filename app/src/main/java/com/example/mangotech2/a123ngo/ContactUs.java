package com.example.mangotech2.a123ngo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;

public class ContactUs extends AppCompatActivity {
    private String array_spinner[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        DocumentEnums.garbagecollect();

        array_spinner=new String[5];
        array_spinner[0]="App feedback";
        array_spinner[1]="Require any update";
        array_spinner[2]="Excellent";
        array_spinner[3]="option 4";
        array_spinner[4]="option 5";
        Spinner ss = (Spinner) findViewById(R.id.spinner01);
        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.spinner_layout, array_spinner);
        ss.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
