package com.example.mangotech2.a123ngo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Model.TripModel;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingContinueForDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.GetScheduledRidesByUserId;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignOutDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.UserBookingHistory;
import com.google.android.gms.maps.model.Marker;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MyTrip extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener {
    private static com.example.mangotech2.a123ngo.List_Adapter.CustomAdapter adapter,sadapter;
    String pickUpAddress,dropOffAddress;
    int FindBooking = 1, ConfirmBookingFromPassenger = 2, ConfirmBookingFromDriver = 3, CompleteBooking = 4, CancelBooking = 5, BookingLater = 6;

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    ListView lvtrips;
    ArrayList<TripModel> dataModels,sdataModels;
    private android.widget.LinearLayout tvnotripfound,tvnoscheduledfound,scheduledhistory,triphistory;
    private ListView ListTrip,List_Scheduled;
    SwipyRefreshLayout mSwipeRefreshLayout;
    public  static MyTrip contextMyTrip;
    Button btntripshistory,btnscheduledhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_my_trips);
        contextMyTrip=MyTrip.this;
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

        HomeMain.tripcounter=1;
        new UserBookingHistory(HomeMain.contexthomemain).execute(Signin.user_id,HomeMain.tripcounter+"");
        new GetScheduledRidesByUserId(HomeMain.contexthomemain).execute(Signin.user_id,"2",HomeMain.tripcounter+"");
        HomeMain.tripcounter++;

        try {
            this.btntripshistory = (Button) findViewById(R.id.btntripshistory);
            btntripshistory.setBackgroundColor(Color.parseColor("#747373"));
            btntripshistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnscheduledhistory.setBackgroundColor(Color.parseColor("#979595"));
                    btntripshistory.setBackgroundColor(Color.parseColor("#747373"));
                    triphistory.setVisibility(View.VISIBLE);
                    scheduledhistory.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){

        }
        try {
            this.btnscheduledhistory = (Button) findViewById(R.id.btnscheduledhistory);
            btnscheduledhistory.setBackgroundColor(Color.parseColor("#979595"));
            btnscheduledhistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    triphistory.setVisibility(View.GONE);
                    scheduledhistory.setVisibility(View.VISIBLE);
                    btntripshistory.setBackgroundColor(Color.parseColor("#979595"));
                    btnscheduledhistory.setBackgroundColor(Color.parseColor("#747373"));

                }
            });
        }catch (Exception e){

        }
        this.ListTrip = (ListView) findViewById(R.id.List_Trip);
        this.tvnotripfound = (LinearLayout) findViewById(R.id.tvnotripfound);
        tvnotripfound.setVisibility(View.GONE);
        this.tvnoscheduledfound = (LinearLayout) findViewById(R.id.tvnoscheduledfound);
        tvnoscheduledfound.setVisibility(View.GONE);
        this.triphistory = (LinearLayout) findViewById(R.id.triphistory);
        triphistory.setVisibility(View.VISIBLE);
        this.scheduledhistory = (LinearLayout) findViewById(R.id.scheduledhistory);
        scheduledhistory.setVisibility(View.GONE);
        List_Scheduled = (ListView) findViewById(R.id.List_Scheduled);
/*
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        TripFragment tripFragment=new TripFragment();
        fragmentTransaction.add(R.id.Trip_Container,tripFragment);
        fragmentTransaction.commit();
*/
//tvusertripdate=(TextView)findViewById(R.id.tvusertripdate);
        //    lvtrips=(ListView) findViewById(R.id.List_Trip);
        //    lvtrips.setAdapter(new CustomAdapter(MyTrip.this, "sameer",2));

        //  tvusertripdate.setText(booking_date.get(0));
        //insertdatatolist();
        try {
            mSwipeRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.my_trip_swiperefresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 3000);
                    new UserBookingHistory(HomeMain.contexthomemain).execute(Signin.user_id, HomeMain.tripcounter + "");
                    new GetScheduledRidesByUserId(HomeMain.contexthomemain).execute(Signin.user_id, "2", HomeMain.tripcounter + "");
                    HomeMain.tripcounter++;
             //       Toast.makeText(MyTrip.this, "Swiped", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            showresponse(""+e.getMessage());
        }
       /* mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
*/
        List_Scheduled.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        List_Scheduled.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long i)
            {
                //  sdataModels.get(position).getdropoffaddress();

                DocumentEnums.garbagecollect();
                TripConfirmed.BookingDetailId = sdataModels.get(position).getBookingDetailId();
                //  TripConfirmed.driver_id = intent.getStringExtra("driver_id");
                //  TripConfirmed.vehicle_id = intent.getStringExtra("vehicle_id");
                TripConfirmed.first_name = sdataModels.get(position).getfirst_name();
                TripConfirmed.phone_num =sdataModels.get(position).getphone_num();
                //  TripConfirmed.vehicle_name = intent.getStringExtra("vehicle_name");
                // TripConfirmed.registration_number = intent.getStringExtra("registration_number");
                //  TripConfirmed.vehicle_color = intent.getStringExtra("vehicle_color");
                // TripConfirmed.vehicle_model = intent.getStringExtra("vehicle_model");


                TripConfirmed.current_location_latitude =  sdataModels.get(position).getpickuplatitude();
                TripConfirmed.current_location_longitude = sdataModels.get(position).getpickuplongitude();
                TripConfirmed.pickup_latitude = sdataModels.get(position).getdropofflatitude();
                TripConfirmed.pickup_longitude = sdataModels.get(position).getdropofflongitude();
                TripConfirmed.fromtrips=true;

                Intent n=new Intent(MyTrip.this,TripConfirmed.class);
                startActivity(n);

            }
        });

    }

    public void insertdatatolist() {
        if(HomeMain.booking_date==null){
            DriverHome.Intitializedrivermytrpobject();
        }
        if(!HomeMain.booking_date.isEmpty()) {
            dataModels = new ArrayList<TripModel>(5);
            lvtrips = (ListView) findViewById(R.id.List_Trip);
            for (int i = 0; i < HomeMain.booking_date.size(); i++) {
                try {
                    pickUpAddress = getaddress(Double.parseDouble(HomeMain.pickup_latitude.get(i)), Double.parseDouble(HomeMain.pickup_longitude.get(i)), null);
                    dropOffAddress = getaddress(Double.parseDouble(HomeMain.dropoff_latitude.get(i)), Double.parseDouble(HomeMain.dropoff_longitude.get(i)), null);
                    if (HomeMain.booking_status_id.get(i).equals(String.valueOf(FindBooking)) || HomeMain.booking_status_id.get(i) == "1") {
                        HomeMain.booking_status.add("FindBooking");
                    } else if (HomeMain.booking_status_id.get(i).equals(String.valueOf(ConfirmBookingFromPassenger))) {
                        HomeMain.booking_status.add("ConfirmBookingFromPassenger");
                    } else if (HomeMain.booking_status_id.get(i).equals(String.valueOf(ConfirmBookingFromDriver))) {
                        HomeMain.booking_status.add("ConfirmBookingFromDriver");
                    } else if (HomeMain.booking_status_id.get(i).equals(String.valueOf(CompleteBooking))) {
                        HomeMain.booking_status.add("CompleteBooking");
                    } else if (HomeMain.booking_status_id.get(i).equals(String.valueOf(CancelBooking))) {
                        HomeMain.booking_status.add("CancelBooking");
                    } else if (HomeMain.booking_status_id.get(i).equals(String.valueOf(BookingLater))) {
                        HomeMain.booking_status.add("BookingLater");
                    }
                    if (HomeMain.amount.get(i) == null || HomeMain.amount.get(i) == "null" || HomeMain.amount.get(i) == "") {
                        HomeMain.amount.set(i, "0");
                    }
                    dataModels.add(new TripModel(HomeMain.booking_status.get(i), HomeMain.booking_date.get(i), "Rs " + HomeMain.amount.get(i), pickUpAddress, dropOffAddress));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {


                //   dataModels.add(new TripModel("Hello","123","43","23","12334"));
                //            dataModels.add(new TripModel("Hellos","12343","432","223","1233434"));

                //    if(booking_date.size()>0) {
                adapter = new com.example.mangotech2.a123ngo.List_Adapter.CustomAdapter(dataModels, MyTrip.this);
                lvtrips.setAdapter(adapter);
            } catch (Exception e) {
                String msg = e.getMessage();
                e.printStackTrace();

            }

            tvnotripfound.setVisibility(View.GONE);
        }else{

            tvnotripfound.setVisibility(View.VISIBLE);
        }


        if(!HomeMain.sbooking_date.isEmpty()) {
            sdataModels = new ArrayList<TripModel>(5);
            for (int i = 0; i < HomeMain.sbooking_date.size(); i++) {
                try {
                    pickUpAddress = getaddress(Double.parseDouble(HomeMain.spickup_latitude.get(i)), Double.parseDouble(HomeMain.spickup_longitude.get(i)), null);
                    dropOffAddress = getaddress(Double.parseDouble(HomeMain.sdropoff_latitude.get(i)), Double.parseDouble(HomeMain.sdropoff_longitude.get(i)), null);
                    //sdataModels.add(new TripModel("Booked", HomeMain.sbooking_date.get(i), "" + HomeMain.sbooking_time.get(i), pickUpAddress, dropOffAddress,HomeMain.sfirst_name.get(i),HomeMain.sphone_num.get(i)));

                    sdataModels.add(new TripModel("Booked", HomeMain.sbooking_date.get(i), "" + HomeMain.sbooking_time.get(i), pickUpAddress, dropOffAddress,HomeMain.sfirst_name.get(i),HomeMain.sphone_num.get(i),HomeMain.sbooking_status_id.get(i),HomeMain.spickup_latitude.get(i),HomeMain.spickup_longitude.get(i),HomeMain.sdropoff_latitude.get(i),HomeMain.sdropoff_longitude.get(i),HomeMain.sbooking_time.get(i)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {


                //   dataModels.add(new TripModel("Hello","123","43","23","12334"));
//            dataModels.add(new TripModel("Hellos","12343","432","223","1233434"));

                //    if(booking_date.size()>0) {
                sadapter = new com.example.mangotech2.a123ngo.List_Adapter.CustomAdapter(sdataModels, MyTrip.this);
                List_Scheduled.setAdapter(sadapter);
            } catch (Exception e) {
                String msg = e.getMessage();
                e.printStackTrace();

            }
            tvnoscheduledfound.setVisibility(View.GONE);
        }else{

            tvnoscheduledfound.setVisibility(View.VISIBLE);
        }
    }

    public String getaddress(double dragLat,double dragLong,Marker arg) throws IOException {

        DocumentEnums.garbagecollect();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(dragLat, dragLong, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        //pickupaddress=address;
        //  arg.setTitle(address+"");
        //  arg.setTag(knownName);
        //      txtcurlocation.setText(address+"");
//        TxtPickupLoc=txtcurlocation.getText().toString();

        return address;
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
                Intent n=new Intent(this,HomeMain.class);
                startActivity(n);
                finish();

            } else if (id == R.id.nav_MyTrips) {
             /*   HomeMain.initializemytripsobject();
                new UserBookingHistory(HomeMain.contexthomemain).execute(Signin.user_id);
*/
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

                finish();  }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }catch (Exception e){
            //showresponse(e.getMessage());
        }

        return true;

    }

    @Override
    public void onRefresh() {
        /*
        new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }, 5000);
        new UserBookingHistory(HomeMain.contexthomemain).execute(Signin.user_id);
        new GetScheduledRidesByUserId(HomeMain.contexthomemain).execute(Signin.user_id,"2");
        Toast.makeText(this,"Swiped",Toast.LENGTH_LONG).show();
 */   }
}