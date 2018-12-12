package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateUserEmail;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateUserName;

public class ChangeEmailAddress extends AppCompatActivity {
    Button btnchangeemail;
    EditText txtchangeemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email_address);
        btnchangeemail=(Button)findViewById(R.id.btnchangeemail);
        txtchangeemail=(EditText)findViewById(R.id.txtchangeemail);

    }
    public void btnchangeemail(View v){
        new UpdateUserEmail(ChangeEmailAddress.this).execute(txtchangeemail.getText().toString(),Signin.user_id.toString());
    }
    public void showresponse(String message){
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }
    public void emailchanged(){
        Intent n=new Intent(ChangeEmailAddress.this,Settings.class);
        startActivity(n);
        finish();

    }
}
