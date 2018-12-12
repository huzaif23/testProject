package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateUserName;

public class ChangeUserName extends AppCompatActivity {
    Button btnchangeusername;
    EditText txtchangeusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);
        btnchangeusername=(Button)findViewById(R.id.btnchangeusername);
        txtchangeusername=(EditText)findViewById(R.id.txtchangeusername);

    }
    public void btnchangeusername(View v){
        new UpdateUserName(ChangeUserName.this).execute(txtchangeusername.getText().toString(),"no",Signin.user_id.toString());
    }
    public void showresponse(String message){
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }
    public void usernamechanged(){
        Intent n=new Intent(ChangeUserName.this,Settings.class);
        startActivity(n);
        finish();

    }
}
