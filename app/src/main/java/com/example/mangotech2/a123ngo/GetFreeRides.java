package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingContinueForDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignOutDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.UserBookingHistory;

import java.util.ArrayList;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class GetFreeRides extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    public static  String promotion_code;
    TextView txtpromotion_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_get_free_rides);
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


        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtpromotion_code=(TextView)findViewById(R.id.txtpromotion_code);
        try{
            txtpromotion_code.setText(promotion_code);
        }catch(Exception e){

        }
    }



    @Override
    public void onBackPressed() {
        DocumentEnums.garbagecollect();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        DocumentEnums.garbagecollect();
        onBackPressed();
        return true;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent n=new Intent(this,HomeMain.class);
            startActivity(n);
            finish();

            } else if (id == R.id.nav_MyTrips) {
                HomeMain.initializemytripsobject();
                new UserBookingHistory(HomeMain.contexthomemain).execute(Signin.user_id);
                finish();

            } else if (id == R.id.nav_MyProfile) {
                Intent n=new Intent(this,Settings.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_GetFREERides) {
             /*   Intent n=new Intent(this,GetFreeRides.class);
                startActivity(n);
          */  } else if (id == R.id.nav_Settings) {
                Intent n=new Intent(this,MainSettings.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_Language) {
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
                finish();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }catch (Exception e){
            //showresponse(e.getMessage());
        }

        return true;

}
}