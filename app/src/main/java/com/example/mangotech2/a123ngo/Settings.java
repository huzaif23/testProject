package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingContinueForDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignOutDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.UserBookingHistory;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Settings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    LinearLayout btnchangepassword,btnprofilechangeusername,btnprofilechangeemail;
    private android.widget.ImageView imgperson;
    private android.widget.ImageView imageButton2;
    private android.widget.TextView txtprofileusername;
    private android.widget.TextView txtprofileusermobile;
    private android.widget.TextView txtprofileemailaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
      try {
          System.gc();
          Runtime.getRuntime().gc();
      }catch (Exception e){

      }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_settings);

        DocumentEnums.garbagecollect();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView txthomeusername,txthomeemailaddress;
        txthomeusername = (TextView) headerView.findViewById(R.id.txthomeusername);
        txthomeemailaddress = (TextView) headerView.findViewById(R.id.txthomeemailaddress);
        txthomeusername.setText(Signin.userName + " ");
        txthomeemailaddress.setText(Signin.emailaddress + " ");


        this.txtprofileemailaddress = (TextView) findViewById(R.id.txtprofileemailaddress);
        this.txtprofileusermobile = (TextView) findViewById(R.id.txtprofileusermobile);
        this.txtprofileusername = (TextView) findViewById(R.id.txtprofileusername);
        try{
            txtprofileusername.setText("Name\n"+Signin.userName);
            txtprofileemailaddress.setText("Mobile\n"+Signin.phonenumber);
            txtprofileusermobile.setText("Email\n"+Signin.emailaddress);
        }catch (Exception e){

        }

        this.imageButton2 = (ImageView) findViewById(R.id.imageButton2);
        this.imgperson = (ImageView) findViewById(R.id.imgperson);
        btnchangepassword=(LinearLayout)findViewById(R.id.btnchangepasswordfromsetting);
        btnchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentEnums.garbagecollect();
                Intent n=new Intent(Settings.this,ChangePassword.class);
                startActivity(n);
            }
        });
        btnprofilechangeusername=(LinearLayout)findViewById(R.id.btnprofilechangeusername);
        btnprofilechangeusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentEnums.garbagecollect();
                Intent n=new Intent(Settings.this,ChangeUserName.class);
                startActivity(n);
            }
        });
        btnprofilechangeemail=(LinearLayout)findViewById(R.id.btnprofilechangeemail);
        btnprofilechangeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentEnums.garbagecollect();
                Intent n=new Intent(Settings.this,ChangeEmailAddress.class);
                startActivity(n);
            }
        });
      }
    @Override
    public boolean onSupportNavigateUp() {
        DocumentEnums.garbagecollect();
        onBackPressed();
        return true;
    }



    @Override
    public void onBackPressed() {
        DocumentEnums.garbagecollect();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent n=new Intent(Settings.this,HomeMain.class);
            startActivity(n);
            finish();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DocumentEnums.garbagecollect();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DocumentEnums.garbagecollect();
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DocumentEnums.garbagecollect();

        try{
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_Home) {
                onBackPressed();
            } else if (id == R.id.nav_MyTrips) {
                HomeMain.initializemytripsobject();
                new UserBookingHistory(HomeMain.contexthomemain).execute(Signin.user_id);
                finish();

            } else if (id == R.id.nav_MyProfile) {
                Intent n=new Intent(this,Settings.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_GetFREERides) {
                Intent n=new Intent(this,GetFreeRides.class);
                startActivity(n);
                finish();
           } else if (id == R.id.nav_Settings) {
              /*  Intent n=new Intent(this,MainSettings.class);
                startActivity(n);
          */  } else if (id == R.id.nav_Language) {
                Intent n=new Intent(this,Language.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_Contactus) {
                Intent n=new Intent(this,Contactusdialog.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_Help) {
                //      Intent n=new Intent(this,Settings.class);
                //    startActivity(n);
            } else if (id == R.id.nav_Rates) {
                Intent n=new Intent(this,Rates.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_DrivethroughUs) {
                SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());

                if(!DriverDocuments.driver_id.equals("no driver_id") &&!DriverDocuments.driver_status.equals("no driver_status")){
                    new BookingContinueForDriver(HomeMain.contexthomemain).execute(DriverDocuments.driver_id);

                    //gotodriverhome();
                }else
                {

                    Intent n=new Intent(this,DriverDetails.class);
                    startActivity(n);

                }
                // finish();
                finish();
            }else if (id == R.id.nav_Logout) {

                SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("access_token");
                editor.remove("driver_id");
                editor.remove("vehicle_name");
                editor.remove("registration_number");
                editor.remove("driver_status");
                editor.remove("user_id");
                editor.commit();
                if(DriverDocuments.driver_id!="no driver_id"&&DriverDocuments.driver_id!=null&&DriverDocuments.driver_id!=""){
                    new SignOutDriver().execute(Signin.user_id,"1");
                }else{
                    new SignOutDriver().execute(Signin.user_id,"2");
                }
                DriverDocuments.driver_id=null;
                Intent n=new Intent(this,SigniUpSignIn.class);
                startActivity(n);
                finish();
            }else if (id == R.id.nav_Logout) {

                SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("access_token");
                editor.commit();
                Intent n=new Intent(this,SigniUpSignIn.class);
                startActivity(n);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            finish();
        }catch (Exception e){
            //showresponse(e.getMessage());
        }

        return true;

    }

}
