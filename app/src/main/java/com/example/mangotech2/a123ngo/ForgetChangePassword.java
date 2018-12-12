package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.ChangePasswordPostRequest;
import com.example.mangotech2.a123ngo.Parsing_JSON.ForgetChangePasswordPostRequest;

public class ForgetChangePassword extends AppCompatActivity {
public static  String oldpassword;
    Button btnchangepassword;
    EditText txtnewpassword,txtconfirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_change_password);
        btnchangepassword=(Button)findViewById(R.id.btnchangepassword);
        txtnewpassword=(EditText)findViewById(R.id.txtnewpassword);
        txtconfirmpassword=(EditText)findViewById(R.id.txtrepassword);

    }
    public void btnchangepassword(View v){
        new ForgetChangePasswordPostRequest(ForgetChangePassword.this).execute(txtnewpassword.getText().toString(),txtconfirmpassword.getText().toString());
    }
    public void showresponse(String message){
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }
    public void passwordchanged(){
        Intent n=new Intent(ForgetChangePassword.this,Signin.class);
        startActivity(n);
    }
}
