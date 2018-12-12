package com.example.mangotech2.a123ngo;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.*;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Validators.*;
import com.example.mangotech2.a123ngo.Parsing_JSON.*;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignInJsonParsing;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity  {
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    TextView txtname,txtpassword,txtemail,txtnumber;
    private TelephonyManager mTelephonyManager;
    String Imei;
    public  static String reg_code="",userid;
    Spinner spnregcountrycode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spnregcountrycode=(Spinner)findViewById(R.id.spnregcountrycode);


        TextView tvname=(TextView)findViewById(R.id.txtname);
        StringBuilder sb = new StringBuilder();
        String device_unique_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        txtname=(TextView)findViewById(R.id.txtname);
        txtpassword=(TextView)findViewById(R.id.txtpassword);
        txtemail=(TextView)findViewById(R.id.txtemail);
        txtnumber=(TextView)findViewById(R.id.txtnumber);

      /*  TelephonyManager telMan = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        sb.append("IMEI: " + telMan.getDeviceId() + "\n");

        sb.append("Android ID: " +  Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID) + "\n");
 */
        // tvname.setText(" "+device_unique_id);
        Imei=device_unique_id.toString();

        Button btnsubmit = (Button) findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //  setContentView(R.layout.verificationcode);
               if(txtname.getText().toString()==""||txtname.getText().toString().isEmpty()){
                   Toast.makeText(getApplicationContext(),
                           "Invalid Name" , Toast.LENGTH_LONG)
                           .show();
               }else if(!TextValidator.validatenumber(txtnumber.getText().toString())){
                   Toast.makeText(getApplicationContext(),
                           "Invalid Number" , Toast.LENGTH_LONG)
                           .show();
               }
                else if(!TextValidator.validateemail(txtemail.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Invalid Email" , Toast.LENGTH_LONG)
                            .show();
                }else if(!TextValidator.validatepassword(txtpassword.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Password length atleast 6 characters" , Toast.LENGTH_LONG)
                            .show();
                }else {
                   String Token=""+ FirebaseInstanceId.getInstance().getToken();
                    new PostRequest(MainActivity.this).execute(txtemail.getText().toString(), txtpassword.getText().toString(),spnregcountrycode.getSelectedItem().toString()+""+ txtnumber.getText().toString(), txtname.getText().toString(), Imei.toString(),Token);
                }      //   gotoverificationcode();

                //        startActivity(n);
            }
        });

    }

    public void gotoverificationcode() {
        Intent n=new Intent(MainActivity.this,Verificationcode.class);
        startActivity(n);
        finish();
    }

    public ProgressDialog dialog;
    public void loading(){
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Please wait.");
        dialog.show();
    }
    public void loaded(){
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showresponse(String message){
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }
}




