package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.ForgetPassword;
import com.example.mangotech2.a123ngo.Validators.TextValidator;

/**
 * The type Forget pin.
 */
public class ForgetPin extends AppCompatActivity {
    /**
     * The Txtforgetmobile.
     */
    EditText txtforgetmobile;
    /**
     * The constant mobile.
     */
    public static String mobile;
    /**
     * The Spnforgetcountrycode.
     */
    Spinner spnforgetcountrycode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pin);
        txtforgetmobile=(EditText)findViewById(R.id.txtforgetmobile);
        spnforgetcountrycode=(Spinner)findViewById(R.id.spnforgetcountrycode);

    }

    /**
     * Btnforgetpassword.
     *
     * @param v the v
     */
    public void btnforgetpassword(View v){
        mobile=txtforgetmobile.getText().toString();

        if(!TextValidator.validatenumber(txtforgetmobile.getText().toString())){
            Toast.makeText(getApplicationContext(),
                    "Invalid Number" , Toast.LENGTH_LONG)
                    .show();
        }else{ new ForgetPassword(ForgetPin.this).execute(spnforgetcountrycode.getSelectedItem()+""+mobile);
    }
    }

    /**
     * Codesent.
     */
    public void  codesent(){
        Intent n=new Intent(ForgetPin.this,ForgetPasswordVerification.class);
        startActivity(n);
    }

    /**
     * Codenotsent.
     */
    public void  codenotsent(){
        Intent n=new Intent(ForgetPin.this,Signin.class);
        startActivity(n);
    }

    /**
     * Showresponse.
     *
     * @param message the message
     */
    public void showresponse(String message){
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }

}
