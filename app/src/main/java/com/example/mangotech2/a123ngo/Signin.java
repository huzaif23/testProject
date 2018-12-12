package com.example.mangotech2.a123ngo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.*;
import com.example.mangotech2.a123ngo.Validators.TextValidator;
import com.google.firebase.iid.FirebaseInstanceId;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Signin extends AppCompatActivity {
    Spinner spncountrycode;
    public TextView txtView,txtforgotpassword;
    public EditText etphone,etpassword;
    public static  String access_token,user_id,token_type,userName,expires_in,issued,expires,emailaddress,phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        DocumentEnums.garbagecollect();
        this.spncountrycode = (Spinner) findViewById(R.id.spncountrycode);
        try{
            txtView = (TextView)findViewById(R.id.editText5);
            txtforgotpassword= (TextView)findViewById(R.id.txtforgotpassword);
            txtforgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent n=new Intent(Signin.this,ForgetPin.class);
                    startActivity(n);
                }
            });
            etphone=(EditText)findViewById(R.id.etphone);
            etpassword=(EditText)findViewById(R.id.etpassword);
        }catch (Exception e){

        }
    }
    public void btnSignIn(View v){
        DocumentEnums.garbagecollect();
        // Intent n=new Intent(Signin.this,HomeMain.class);
        // startActivity(n);
        //   new PostRequest(Signin.this).execute();
        //  loginaccepted();

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        if(connected){

            try{
                if(!TextValidator.validatenumber(etphone.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Invalid Number" , Toast.LENGTH_LONG)
                            .show();
                }else if(!TextValidator.validatepassword(etpassword.getText().toString())){
                    Toast.makeText(getApplicationContext(),
                            "Password length atleast 6 characters" , Toast.LENGTH_LONG)
                            .show();
                }else {
  //                  String Token=""+ FirebaseInstanceId.getInstance().getToken();
                    new SignInJsonParsing(Signin.this).execute(spncountrycode.getSelectedItem().toString()+""+etphone.getText().toString(), etpassword.getText().toString());

//                   new UpdateFCMToken().execute(Token);

                    // loginaccepted();
                }
            }catch (Exception e){

            }
        }else{
            Toast.makeText(getApplicationContext(),
                    "Internet Connection Problem" , Toast.LENGTH_LONG)
                    .show();
        }
    }
    public void loginaccepted(){
        DocumentEnums.garbagecollect();
      /*  SharedPreferences prefs = getApplicationContext().getSharedPreferences("com.example.mangotech2.a123ngo", MODE_PRIVATE);

    //    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("com.example.mangotech2.a123ngo", MODE_PRIVATE).edit();
        //SharedPreferences prefs = this.getSharedPreferences("com.example.mangotech2.a123ngo", Context.MODE_PRIVATE);
      prefs.edit().putString("access_token", access_token);
    //    prefs.edit().putString(user_id, user_id);
        prefs.edit().commit();
*/
        signindatatopreferences();
        SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
        String token=prefs.getString("FCM_Token","no FCM_Token");
        if(token.equals("no FCM_Token")) {
            String Token = "" + FirebaseInstanceId.getInstance().getToken();
            new UpdateFCMToken().execute(Token);
        }else{
            new UpdateFCMToken().execute(token);
        }
        Intent n=new Intent(Signin.this,HomeMain.class);
        n.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// This flag ensures all activities on top of the CloseAllViewsDemo are cleared.
        startActivity(n);
        finish();

    }

    public void signindatatopreferences() {

        DocumentEnums.garbagecollect();

        SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
            editor.putString("access_token", access_token);

        editor.putString("user_id",user_id);
        editor.putString("promotion_code",GetFreeRides.promotion_code);
        editor.putString("userName", userName);
        editor.putString("emailaddress",emailaddress);
        editor.putString("phnumber",phonenumber);
        editor.commit();

        Signin.access_token=access_token;
        Signin.user_id=user_id;
        Signin.userName=userName;
        Signin.emailaddress=emailaddress;
        Signin.phonenumber=phonenumber;
        GetFreeRides.promotion_code=GetFreeRides.promotion_code;

    }

    public ProgressDialog dialog;
    public void loading(){
        DocumentEnums.garbagecollect();
        dialog = new ProgressDialog(Signin.this);
        dialog.setMessage("Please wait.");
        dialog.show();
    }
    public void loaded(){
        try{
        DocumentEnums.garbagecollect();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }}catch (Exception e){

        }

    }


    public void showresponse(String message){
        DocumentEnums.garbagecollect();
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }


    public void verifyuraccount(){
        DocumentEnums.garbagecollect();
        Intent n=new Intent(Signin.this,Verificationcode.class);
        startActivity(n);
    }
}