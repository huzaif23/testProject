package com.example.mangotech2.a123ngo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.ConfirmRegistrationSO;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignInJsonParsing;
import com.example.mangotech2.a123ngo.Validators.TextValidator;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.mangotech2.a123ngo.R.id.btnsubmit;

public class Verificationcode extends AppCompatActivity {
    TextView txtvercode;
    public static Context contextverificationcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificationcode);
        DocumentEnums.garbagecollect();
        Button btngo = (Button) findViewById(R.id.btngo);
        txtvercode=(TextView)findViewById(R.id.txtvercode);
        contextverificationcode=Verificationcode.this;
        btngo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if(!TextValidator.validateverificationcode(txtvercode.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Verification Code length 4 characters" , Toast.LENGTH_LONG)
                            .show();
                }else{
                    new ConfirmRegistrationSO(Verificationcode.this).execute(txtvercode.getText().toString(), MainActivity.userid.toString());

                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    public void verficationcodeaccepted(){
        Intent n=new Intent(Verificationcode.this,HomeMain.class);
        startActivity(n);
        finish();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void showresponse(String message){
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }

    public void signindatatopreferences() {
        SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("access_token", Signin.access_token);
        editor.putString("user_id",Signin.user_id);
        editor.putString("promotion_code",GetFreeRides.promotion_code);
        editor.putString("userName", Signin.userName);
        editor.putString("emailaddress",Signin.emailaddress);
        editor.putString("phnumber",Signin.phonenumber);
        editor.commit();

    }
}
