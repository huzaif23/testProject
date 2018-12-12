package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.ChangePasswordPostRequest;

public class ChangePassword extends AppCompatActivity {
Button btnchangepassword;
    EditText txtcurpassword,txtnewpassword,txtconfirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        DocumentEnums.garbagecollect();
        btnchangepassword=(Button)findViewById(R.id.btnchangepassword);
        txtcurpassword=(EditText)findViewById(R.id.txtcurrpassword);
        txtnewpassword=(EditText)findViewById(R.id.txtnewpassword);
        txtconfirmpassword=(EditText)findViewById(R.id.txtrepassword);

    }
    public void btnchangepassword(View v){
        new ChangePasswordPostRequest(ChangePassword.this).execute(txtcurpassword.getText().toString(), txtnewpassword.getText().toString(),txtconfirmpassword.getText().toString());
    }
    public void showresponse(String message){
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }
    public void passwordchanged(){
        Intent n=new Intent(ChangePassword.this,Settings.class);
        startActivity(n);
        finish();

    }
}
