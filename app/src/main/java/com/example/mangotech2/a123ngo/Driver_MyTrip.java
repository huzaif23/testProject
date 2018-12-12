package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Process;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Model.TripModel;
import com.example.mangotech2.a123ngo.Parsing_JSON.DriverBookingHistory;
import com.example.mangotech2.a123ngo.Parsing_JSON.GetScheduledRidesByDriverId;
import com.example.mangotech2.a123ngo.Parsing_JSON.GetScheduledRidesByUserId;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignOutDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.UserBookingHistory;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.maps.model.Marker;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Driver_MyTrip extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static com.example.mangotech2.a123ngo.List_Adapter.CustomAdapter adapter,sadapter;
    String pickUpAddress,dropOffAddress;
    int FindBooking = 1, ConfirmBookingFromPassenger = 2, ConfirmBookingFromDriver = 3, CompleteBooking = 4, CancelBooking = 5, BookingLater = 6;

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    ListView lvtrips,List_Scheduled;
    private android.widget.LinearLayout tvnodrivertripfound,tvnoscheduledfound,scheduledhistory,triphistory;
    ArrayList<TripModel> dataModels,sdataModels;
    Button btntripshistory,btnscheduledhistory;
    SwipyRefreshLayout mSwipeRefreshLayout,mSwipeRefreshLayoutScheduled;
    public  static Driver_MyTrip contextDriverMyTrip;
    // TextView tvusertripdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_drawer_my_trips);
contextDriverMyTrip= Driver_MyTrip.this;
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
DriverHome.tripCounter=1;

        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        lvtrips=(ListView)findViewById(R.id.List_Trips);
        this.tvnodrivertripfound = (LinearLayout) findViewById(R.id.tvnodrivertripfound);
        this.tvnoscheduledfound = (LinearLayout) findViewById(R.id.tvnoscheduledfound);
        tvnoscheduledfound.setVisibility(View.GONE);
        tvnodrivertripfound.setVisibility(View.GONE);
        List_Scheduled = (ListView) findViewById(R.id.List_Scheduled);
        this.scheduledhistory = (LinearLayout) findViewById(R.id.scheduledhistory);
        scheduledhistory.setVisibility(View.GONE);
        this.triphistory = (LinearLayout) findViewById(R.id.triphistory);
        triphistory.setVisibility(View.GONE);
        //insertdataintolist();
        new DriverBookingHistory(DriverHome.contextdriverhome).execute(Signin.user_id,DriverHome.tripCounter+"");
        new GetScheduledRidesByDriverId(DriverHome.contextdriverhome).execute(Signin.user_id,"1",DriverHome.tripCounter+"");
        DriverHome.tripCounter++;

        try {
            mSwipeRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.driver_my_trip_swiperefresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 3000);
                    
                    new DriverBookingHistory(DriverHome.contextdriverhome).execute(Signin.user_id,DriverHome.tripCounter+"");
                    new GetScheduledRidesByDriverId(DriverHome.contextdriverhome).execute(Signin.user_id,"1",DriverHome.tripCounter+"");
                    DriverHome.tripCounter++;
                    //       Toast.makeText(MyTrip.this, "Swiped", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            showresponse(""+e.getMessage());
        }

        try {
            mSwipeRefreshLayoutScheduled = (SwipyRefreshLayout) findViewById(R.id.driverscheduled_my_trip_swiperefresh);
            mSwipeRefreshLayoutScheduled.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayoutScheduled.setRefreshing(false);
                        }
                    }, 3000);

                    new DriverBookingHistory(DriverHome.contextdriverhome).execute(Signin.user_id,DriverHome.tripCounter+"");
                    new GetScheduledRidesByDriverId(DriverHome.contextdriverhome).execute(Signin.user_id,"1",DriverHome.tripCounter+"");
                    DriverHome.tripCounter++;
                    //       Toast.makeText(MyTrip.this, "Swiped", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            showresponse(""+e.getMessage());
        }



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






        btnscheduledhistory.setBackgroundColor(Color.parseColor("#979595"));
        btntripshistory.setBackgroundColor(Color.parseColor("#747373"));
        triphistory.setVisibility(View.VISIBLE);
        scheduledhistory.setVisibility(View.GONE);



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
                DocumentEnums.garbagecollect();
                //  sdataModels.get(position).getdropoffaddress();

                DriverRoute.BookingDetailId = sdataModels.get(position).getBookingDetailId();
                //  TripConfirmed.driver_id = intent.getStringExtra("driver_id");
                //  TripConfirmed.vehicle_id = intent.getStringExtra("vehicle_id");
                DriverBooking.PassengerName = sdataModels.get(position).getfirst_name();
                DriverBooking.PassengerPhoneNo =sdataModels.get(position).getphone_num();
                //  TripConfirmed.vehicle_name = intent.getStringExtra("vehicle_name");
                // TripConfirmed.registration_number = intent.getStringExtra("registration_number");
                //  TripConfirmed.vehicle_color = intent.getStringExtra("vehicle_color");
                // TripConfirmed.vehicle_model = intent.getStringExtra("vehicle_model");


                DriverBooking.PickupLatitude =  sdataModels.get(position).getpickuplatitude();
                DriverBooking.PickupLongitude = sdataModels.get(position).getpickuplongitude();
                DriverRoute.dropOffLatitude = sdataModels.get(position).getdropofflatitude();
                DriverRoute.dropOffLongitude = sdataModels.get(position).getdropofflongitude();
                DriverRoute.booking_date=sdataModels.get(position).getdate();
                DriverRoute.booking_time=sdataModels.get(position).gettime();

                DriverRoute.fromtrips=true;
                Intent n=new Intent(Driver_MyTrip.this,DriverRoute.class);
                startActivity(n);

            }
        });
    }

    public void insertdataintolist() {
        if(DriverHome.booking_date==null){
            DriverHome.Intitializedrivermytrpobject();
        }
        if(!DriverHome.booking_date.isEmpty()) {

            try {
        dataModels= new ArrayList<TripModel>(5);
        for(int i=0;i<DriverHome.booking_date.size();i++) {

                pickUpAddress = getaddress(Double.parseDouble(DriverHome.pickup_latitude.get(i)), Double.parseDouble(DriverHome.pickup_longitude.get(i)), null);
                dropOffAddress = getaddress(Double.parseDouble(DriverHome.dropoff_latitude.get(i)), Double.parseDouble(DriverHome.dropoff_longitude.get(i)), null);
                if (DriverHome.booking_status_id.get(i).equals(String.valueOf(FindBooking)) || DriverHome.booking_status_id.get(i) == "1") {
                    DriverHome.booking_status.add("FindBooking");
                } else if (DriverHome.booking_status_id.get(i).equals(String.valueOf(ConfirmBookingFromPassenger))) {
                    DriverHome.booking_status.add("ConfirmBookingFromPassenger");
                } else if (DriverHome.booking_status_id.get(i).equals(String.valueOf(ConfirmBookingFromDriver))) {
                    DriverHome.booking_status.add("ConfirmBookingFromDriver");
                } else if (DriverHome.booking_status_id.get(i).equals(String.valueOf(CompleteBooking))) {
                    DriverHome.booking_status.add("CompleteBooking");
                } else if (DriverHome.booking_status_id.get(i).equals(String.valueOf(CancelBooking))) {
                    DriverHome.booking_status.add("CancelBooking");
                } else if (DriverHome.booking_status_id.get(i).equals(String.valueOf(BookingLater))) {
                    DriverHome.booking_status.add("BookingLater");
                }
                if (DriverHome.amount.get(i) == null || DriverHome.amount.get(i) == "null" || DriverHome.amount.get(i) == "") {
                    DriverHome.amount.set(i, "0");
                }
                dataModels.add(new TripModel(DriverHome.booking_status.get(i), DriverHome.booking_date.get(i), "Rs " + DriverHome.amount.get(i), pickUpAddress, dropOffAddress));
            }
            } catch (IOException e) {
                e.printStackTrace();
            }



        try{


            //   dataModels.add(new TripModel("Hello","123","43","23","12334"));
//            dataModels.add(new TripModel("Hellos","12343","432","223","1233434"));

            //    if(booking_date.size()>0) {
            adapter = new com.example.mangotech2.a123ngo.List_Adapter.CustomAdapter(dataModels, Driver_MyTrip.this);
            lvtrips.setAdapter(adapter);
        }catch (Exception e){
            String msg=e.getMessage();
            e.printStackTrace();

        }
            tvnodrivertripfound.setVisibility(View.GONE);

        }else{
            tvnodrivertripfound.setVisibility(View.VISIBLE);
        }


        if(!DriverHome.sbooking_date.isEmpty()) {
            sdataModels = new ArrayList<TripModel>(5);
            for (int i = 0; i < DriverHome.sbooking_date.size(); i++) {
                try {
                    pickUpAddress = getaddress(Double.parseDouble(DriverHome.spickup_latitude.get(i)), Double.parseDouble(DriverHome.spickup_longitude.get(i)), null);
                    dropOffAddress = getaddress(Double.parseDouble(DriverHome.sdropoff_latitude.get(i)), Double.parseDouble(DriverHome.sdropoff_longitude.get(i)), null);

                 //   sdataModels.add(new TripModel(DriverHome.sbooking_status_id.get(i), DriverHome.sbooking_date.get(i), "" + DriverHome.sbooking_time.get(i), pickUpAddress, dropOffAddress,DriverHome.sfirst_name.get(i),DriverHome.sphone_num.get(i)));
               //     sdataModels.add(new TripModel("Booked", DriverHome.sbooking_date.get(i), "" + DriverHome.sbooking_time.get(i), pickUpAddress, dropOffAddress,DriverHome.sfirst_name.get(i),DriverHome.sphone_num.get(i)));
                    sdataModels.add(new TripModel("Booked", DriverHome.sbooking_date.get(i), "" + DriverHome.sbooking_time.get(i), pickUpAddress, dropOffAddress,DriverHome.sfirst_name.get(i),DriverHome.sphone_num.get(i),DriverHome.sbooking_status_id.get(i),DriverHome.spickup_latitude.get(i),DriverHome.spickup_longitude.get(i),DriverHome.sdropoff_latitude.get(i),DriverHome.sdropoff_longitude.get(i),DriverHome.sbooking_time.get(i)));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                //   dataModels.add(new TripModel("Hello","123","43","23","12334"));
//            dataModels.add(new TripModel("Hellos","12343","432","223","1233434"));
                //    if(booking_date.size()>0) {
                sadapter = new com.example.mangotech2.a123ngo.List_Adapter.CustomAdapter(sdataModels, Driver_MyTrip.this);
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
        //    System.exit(0);
         //   Process.killProcess(Process.myPid());
         //   super.onDestroy();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DocumentEnums.garbagecollect();
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.driver_home, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
           super.onBackPressed();
            //     Intent n=new Intent(this,DriverHome.class);
            //    startActivity(n);
            //   finish();
        } else if (id == R.id.nav_MyTrips) {
         //   new DriverBookingHistory(DriverHome.contextdriverhome).execute(Signin.user_id);
        } else if (id == R.id.nav_MyProfile) {
            Intent n=new Intent(this,Settings.class);
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
        }else if (id == R.id.nav_Logout) {
            getApplication().stopService(Splash.serviceintent);
            SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("access_token");
            editor.remove("driver_id");
            editor.remove("vehicle_name");
            editor.remove("registration_number");
            editor.remove("driver_status");
            editor.remove("user_id");
            editor.commit();
            new SignOutDriver().execute(Signin.user_id,"1");
            Intent n=new Intent(DriverHome.contextdriverhome,SigniUpSignIn.class);
            startActivity(n);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
