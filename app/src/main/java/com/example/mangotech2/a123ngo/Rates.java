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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingContinueForDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.GetFareDetails;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignOutDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.UserBookingHistory;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Rates extends AppCompatActivity  implements AdapterView.OnItemSelectedListener,NavigationView.OnNavigationItemSelectedListener  {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    public String spnloc[];
    String[] city = { "Karachi", "Lahore", "Islamabad"};
    String[] spnride = { "BIKE", "AUTO", "GO", "GO+", "BUSINESS", "TAXI", "BUS"};
    ArrayAdapter aa;
    ArrayAdapter adapters;
    private Spinner spnlocation;
    private Spinner spnrides;
    public android.widget.TextView textView18;
    public android.widget.TextView textView17;
    public android.widget.TextView txtnowstartingprice;
    public android.widget.TextView txtlaterstartingprice;
    public android.widget.TextView txtnowminimumprice;
    public android.widget.TextView txtlaterminimumprice;
    public android.widget.TextView txtmovingperkmprice;
    public android.widget.TextView txtwaitingperhourprice;
    int selectedcity=1,selectedvehicle=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_rates);

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



        this.txtwaitingperhourprice = (TextView) findViewById(R.id.txtwaitingperhourprice);
        this.txtmovingperkmprice = (TextView) findViewById(R.id.txtmovingperkmprice);
        this.txtlaterminimumprice = (TextView) findViewById(R.id.txtlaterminimumprice);
        this.txtnowminimumprice = (TextView) findViewById(R.id.txtnowminimumprice);
        this.txtlaterstartingprice = (TextView) findViewById(R.id.txtlaterstartingprice);
        this.txtnowstartingprice = (TextView) findViewById(R.id.txtnowstartingprice);
        this.textView17 = (TextView) findViewById(R.id.textView17);
        this.textView18 = (TextView) findViewById(R.id.textView18);
        this.spnrides = (Spinner) findViewById(R.id.spnrides);
        this.spnlocation = (Spinner) findViewById(R.id.spnlocation);
        new GetFareDetails(Rates.this).execute(String.valueOf(selectedcity),String.valueOf(selectedvehicle));
        try {
            Spinner s = (Spinner) findViewById(R.id.spnlocation);
            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getApplicationContext(),city[i] , Toast.LENGTH_LONG).show();
                    selectedcity=i+1;
                    new GetFareDetails(Rates.this).execute(String.valueOf(selectedcity),String.valueOf(selectedvehicle));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,city);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(aa);


            Spinner spnrides = (Spinner) findViewById(R.id.spnrides);
            spnrides.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    DocumentEnums.garbagecollect();
                    Toast.makeText(getApplicationContext(),spnride[i] , Toast.LENGTH_LONG).show();
                    selectedvehicle=i+1;
                    new GetFareDetails(Rates.this).execute(String.valueOf(selectedcity),String.valueOf(selectedvehicle));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            adapters  = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spnride);
            adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnrides.setAdapter(adapters);
        }catch (Exception e){
            Log.d(e.toString(),e.getMessage().toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(),city[i] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void showresponse(String message){
        DocumentEnums.garbagecollect();
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
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
          super.onBackPressed();
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
             /*   Intent n=new Intent(this,Rates.class);
                startActivity(n);
          */  } else if (id == R.id.nav_DrivethroughUs) {
                SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());

                if(!DriverDocuments.driver_id.equals("no driver_id") &&!DriverDocuments.driver_status.equals("no driver_status")){
                    new BookingContinueForDriver(HomeMain.contexthomemain).execute(DriverDocuments.driver_id);

                    //gotodriverhome();
                }else
                {

                    Intent n=new Intent(this,DriverDetails.class);
                    startActivity(n);

                }
                finish();
                // finish();
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
