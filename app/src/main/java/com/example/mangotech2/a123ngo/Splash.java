package com.example.mangotech2.a123ngo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Custom_Services.sendlatitudelongitudetoserver;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingContinueForDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingContinueForPassenger;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateDriverLocation;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateFCMToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Splash extends AppCompatActivity {
    public  static Context context;
    public  static  Intent serviceintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);


      /*  onNewIntent(getIntent());
        FirebaseMessaging.getInstance().
                subscribeToTopic("ServiceNow");
        */
        setContentView(R.layout.activity_splash);
        context=getApplicationContext();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
        String access_tokens = prefs.getString("access_token","no token");
        String user_id = prefs.getString("user_id","no user_id");
        String userName = prefs.getString("userName","no userName");
        String emailaddress = prefs.getString("emailaddress","no emailaddress");
        String phonenumber = prefs.getString("phnumber","no phonenumber");
        String lang = prefs.getString("lang","no lang");
String token=prefs.getString("FCM_Token","no FCM_Token");
        String driver_id = prefs.getString("driver_id","no driver_id");
        String driver_status = prefs.getString("driver_status","no driver_status");
        String registration_number = prefs.getString("registration_number","no registration_number");
        String vehicle_name = prefs.getString("vehicle_name","no vehicle_name");
        String vehicle_id = prefs.getString("vehicle_id","no vehicle_id");
        String vehicle_type_id=prefs.getString("vehicle_type_id","no vehicle_type_id");
        String promotion_code=prefs.getString("promotion_code","no promotion_code");
        DriverDocuments.driver_id=driver_id;
        DriverDocuments.driver_status=driver_status;
        DriverDocuments.registration_number=registration_number;
        DriverDocuments.vehicle_name=vehicle_name;
        DriverDocuments.vehicle_id=vehicle_id;
        DriverDocuments.vehicle_type_id=vehicle_type_id;

        Signin.user_id=user_id;
        Signin.access_token=access_tokens;
        Signin.userName=userName;
        Signin.emailaddress=emailaddress;
        Signin.phonenumber=phonenumber;
        GetFreeRides.promotion_code=promotion_code;

        serviceintent=new Intent(this, sendlatitudelongitudetoserver.class);
        getApplication().startService(serviceintent);
if(DriverDocuments.vehicle_id!="no vehicle_id"&&DriverDocuments.vehicle_id!="0") {
    new UpdateDriverLocation().execute(DocumentEnums.current_latitude, DocumentEnums.current_latitude, DriverDocuments.vehicle_id);
}
        if(!access_tokens.equals("no token") &&!user_id.equals("no user_id")){
            if(token.equals("no FCM_Token")) {
                String Token = "" + FirebaseInstanceId.getInstance().getToken();
                new UpdateFCMToken().execute(Token);
            }else{
                new UpdateFCMToken().execute(token);
            }
       if(lang.equals("no lang"))
       {
           new BookingContinueForPassenger(Splash.this).execute(Signin.user_id);
         //  gotohome();
       }else{
           setLocale(lang);
           new BookingContinueForPassenger(Splash.this).execute(Signin.user_id);
       }

        }else
        {

            gotosigninactivity();
        }
        // Toast.makeText(getApplicationContext(), ""+access_tokens , Toast.LENGTH_LONG).show();


    }

    public void gotohome() {
        Intent n=new Intent(Splash.this,HomeMain.class);
        startActivity(n);
        finish();
    }

    public void gototripconfirmed() {
        Intent n=new Intent(Splash.this,TripConfirmed.class);
        startActivity(n);
        finish();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    //    gotohome();
    }
    private void gotosigninactivity() {
        Thread th=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1000);
                }catch (Exception e){

                }finally {
                    Intent n=new Intent(Splash.this,SigniUpSignIn.class);
                    startActivity(n);
                    finish();
                }
            }
        };
        th.start();
    }

    static   String strSDesc = "ShortDesc";
    static String strIncidentNo = "IncidentNo";
    static String strDesc="IncidentNo";
    @Override
    public void onNewIntent(Intent intent) {
        try{   Bundle extras = intent.getExtras();
            strSDesc = extras.getString("ShortDesc",
                    "ShortDesc");
            if (strSDesc != "ShortDesc") {
                setContentView(R.layout.activity_driver_booking);
                String IncidentTextView ;
                String SDescTextView;
                String DescTextView ;
                strSDesc = extras.getString("ShortDesc",
                        "ShortDesc");
                strIncidentNo = extras.getString("IncidentNo",
                        "IncidentNo");
                strDesc=extras.getString("Description",
                        "IncidentNo");

                IncidentTextView=strIncidentNo.toString();
                SDescTextView=strSDesc.toString();
                DescTextView=strDesc.toString();
                Intent n=new Intent(Splash.this,DriverBooking.class);
                n.putExtra("strIncidentNo",strIncidentNo);
                n.putExtra("strSDesc",strSDesc);
                n.putExtra("strDesc",strDesc);
                startActivity(n);

            }else{
                gotosigninactivity();
            }
        }catch (Exception e){

            gotosigninactivity();
        }
    }

    public void browser1(View view){
        Intent browserIntent =new Intent
                (Intent.ACTION_VIEW, Uri.parse
                        ("https://somebrowser?uri=incident.do?sysparm_query=number="
                                +strIncidentNo));
        startActivity(browserIntent);
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }
    public void showresponse(String message) {
        try{
            Toast.makeText(getApplicationContext(),
                    "" + message, Toast.LENGTH_LONG)
                    .show();
        }catch (Exception e){
//        showresponse(e.getMessage());
        }
    }
}
