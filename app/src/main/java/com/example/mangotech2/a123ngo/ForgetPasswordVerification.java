package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.ConfirmRegistrationSO;
import com.example.mangotech2.a123ngo.Parsing_JSON.ForgetPasswordConfirm;
import com.example.mangotech2.a123ngo.Validators.TextValidator;

public class ForgetPasswordVerification extends AppCompatActivity {

    TextView txtforgetpasswordvercode;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_verification);
        Button btnforgetpasswordverifycode = (Button) findViewById(R.id.btnforgetpasswordverifycode);
        txtforgetpasswordvercode=(TextView)findViewById(R.id.txtforgetpasswordvercode);

        btnforgetpasswordverifycode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if(!TextValidator.validateverificationcode(txtforgetpasswordvercode.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Verification Code length 4 characters" , Toast.LENGTH_LONG)
                            .show();
                }else{
                new ForgetPasswordConfirm(ForgetPasswordVerification.this).execute(txtforgetpasswordvercode.getText().toString(), Signin.user_id.toString());

                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    public void verficationcodeaccepted(){
        Intent n=new Intent(ForgetPasswordVerification.this,ForgetChangePassword.class);
        startActivity(n);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
    public void showresponse(String message){
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }

}
