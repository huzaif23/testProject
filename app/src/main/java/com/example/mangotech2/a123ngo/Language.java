package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;

import java.util.Locale;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Language extends AppCompatActivity {

    android.widget.RadioButton cburdu;
     android.widget.RadioButton cbenglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_language);
        this.cbenglish = (RadioButton) findViewById(R.id.cb_english);
        this.cburdu = (RadioButton) findViewById(R.id.cb_urdu);
        cbenglish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setLocale("en");
                SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("lang","en");
                editor.commit();
            }
        });
        cburdu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setLocale("ur");
                    SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("lang","ur");
                    editor.commit();

            }
        });
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, HomeMain.class);
        startActivity(refresh);
        finish();
    }
}
