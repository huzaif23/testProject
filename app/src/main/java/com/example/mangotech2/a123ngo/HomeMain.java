package com.example.mangotech2.a123ngo;

import android.*;
import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Address;


import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Model.TripModel;
import com.example.mangotech2.a123ngo.Modules.DirectionFinderListener;
import com.example.mangotech2.a123ngo.Modules.Route;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingContinueForDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.GetDriverDetailsByUserId;
import com.example.mangotech2.a123ngo.Parsing_JSON.GetScheduledRidesByUserId;
import com.example.mangotech2.a123ngo.Parsing_JSON.GetVehicleLocationToPassengers;
import com.example.mangotech2.a123ngo.Parsing_JSON.PassengerBookingConfirm;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignOutDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateVehicleLocation;
import com.example.mangotech2.a123ngo.Parsing_JSON.UserBookingHistory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class HomeMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,GoogleMap.OnMarkerClickListener,OnMapReadyCallback,DirectionFinderListener,GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener {
    private GoogleMap mMap;
    /* private Button btnFindPath;
     private EditText etOrigin;
     private EditText etDestination;
     */
    private List<Marker> originMarkers = new ArrayList<>();
    public Marker pickupmarker;
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    com.example.mangotech2.a123ngo.GPSTracker gps;
    public static  String TxtPickupLoc;
    public static LatLng Pickuploc;
    public static ArrayList<String> VehicleId,VehicleTypeId,Latitude,Longitude;
   public TextView txtcurlocation,txthomeusername,txthomeemailaddress;
    double latitude,longitude;
    public static ArrayList<String> booking_status,booking_date,ride_start_time,ride_end_time,pickup_latitude,pickup_longitude,dropoff_latitude,dropoff_longitude,feedback,booking_status_id,amount,first_name,last_name,vehicle_name,registration_number;
    public static ArrayList<String> sbooking_time,sbooking_status, sbooking_date,sbooking_status_id,samount,spickup_latitude,spickup_longitude,sdropoff_latitude,sdropoff_longitude,sfirst_name,sphone_num;
    public  static HomeMain contexthomemain;
public  static int tripcounter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
DocumentEnums.garbagecollect();
        contexthomemain=HomeMain.this;
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setMaxWidth(60);
        imageView.setMaxHeight(40);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.final_baricon);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.LEFT
                | Gravity.CENTER_VERTICAL);
        layoutParams.leftMargin = 0;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        VehicleId=new ArrayList<String>(1);
        VehicleTypeId=new ArrayList<String>(1);
        Latitude=new ArrayList<String>(1);
        Longitude=new ArrayList<String>(1);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = new com.example.mangotech2.a123ngo.GPSTracker(HomeMain.this);

        // check if GPS enabled
        if(gps.canGetLocation()){
            gps.getLocation();

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{
            gps.showSettingsAlert();
        }
        try {
            txtcurlocation=(TextView)findViewById(R.id.txtcurrloc);
            //       txtcurlocation.setText("Latitude = "+latitude+" Longitude = "+longitude);
            Pickuploc=new LatLng(latitude, longitude);

            txtcurlocation.setText("Current Location");
            TxtPickupLoc=txtcurlocation.getText().toString();
            txtcurlocation.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                  /*  PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                    Intent intent;
                    try{
                        Context context = getApplicationContext();
//intent= builder.build();
                        //      startActivityForResult(intent,Place_Picker_Request);
                        startActivityForResult(builder.build(HomeMain.this), Place_Picker_Request);
                    }catch (Exception e){

                    }
                */
                    findPlace(view);
                }
            });
            View headerView = navigationView.getHeaderView(0);
            txthomeusername = (TextView) headerView.findViewById(R.id.txthomeusername);
            txthomeemailaddress = (TextView) headerView.findViewById(R.id.txthomeemailaddress);
            txthomeusername.setText(Signin.userName + " ");
            txthomeemailaddress.setText(Signin.emailaddress + " ");



        }catch (Exception e){

            String message=e.getMessage();
//showresponse(""+message);
        }
        DocumentEnums.garbagecollect();
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

    int Place_Picker_Request=1;
    public  static String pickupaddress;
    /*
      protected void onActivityResult(int requestCode,int resultCode,Intent data){
          if(requestCode==Place_Picker_Request){
              if(resultCode==RESULT_OK){
                  Place place =PlacePicker.getPlace(data,this);
                  String Address=String.format("place: %s",place.getAddress());
                  // etOrigin.setText(Address);
                  txtcurlocation.setText(place.getAddress());
                  pickupaddress=place.getAddress().toString();
                  Pickuploc=place.getLatLng();
                  Log.d(Address,Address);
                  mMap.clear();
                  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Pickuploc, 13));

                 mMap.addMarker(new MarkerOptions()
                          .title("Selected Location")
                          .position(Pickuploc));
              }
          }
      }
  */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DocumentEnums.garbagecollect();
        if (requestCode == Place_Picker_Request) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("Place", "Place: " + place.getName());
                String placenameadd= (String) place.getAddress();

                String places=   placenameadd.replaceAll((String) place.getName()," ");

                txtcurlocation.setText(place.getName()+""+places);
                TxtPickupLoc=txtcurlocation.getText().toString();
                pickupaddress=TxtPickupLoc;
                Pickuploc=place.getLatLng();

                if(pickupmarker!=null){
                    pickupmarker.remove();
                    //          originMarkers.remove(0);
                }
/*
                try {
                    getaddress(Pickuploc.latitude,Pickuploc.longitude,pickupmarker);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                pickupmarker=mMap.addMarker(new MarkerOptions()
                        .title(TxtPickupLoc).draggable(true).icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))
                        .position(Pickuploc));
           /*     originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title(TxtPickupLoc).draggable(true).icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))
                        .position(Pickuploc)));
             */
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Pickuploc, 13));


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Status", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    public void btnsearchclick(View v) {

        DocumentEnums.garbagecollect();
        findPlace(v);
    }
    public void findPlace(View view) {
        DocumentEnums.garbagecollect();
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("PK")
                    .build();

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, Place_Picker_Request);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    public void btnnextclick(View v) {
        DocumentEnums.garbagecollect();


        Intent n=new Intent(this,mapdirection.class);
        startActivity(n);
    }

    @Override
    public void onBackPressed() {
        DocumentEnums.garbagecollect();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            try {
                finish();

                System.exit(0);
                Process.killProcess(Process.myPid());
                super.onDestroy();

                trimCache(this);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    public static void trimCache(Context context) {
        DocumentEnums.garbagecollect();
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        DocumentEnums.garbagecollect();
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.home_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
   /*     if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    };
    LocationRequest mLocationRequest;
    @Override
    public void onConnected(Bundle bundle) {
        DocumentEnums.garbagecollect();
        try{
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(20000);
            mLocationRequest.setFastestInterval(20000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(20);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }catch (Exception e){
//showresponse(e.getMessage());
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DocumentEnums.garbagecollect();
        try{
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_Home) {
        /*    Intent n=new Intent(this,HomeMain.class);
            startActivity(n);
            finish();
        */
            } else if (id == R.id.nav_MyTrips) {
                initializemytripsobject();
          //      new UserBookingHistory(HomeMain.this).execute(Signin.user_id);
           //     new GetScheduledRidesByUserId(HomeMain.this).execute(Signin.user_id,"2");
gotomytrip();
            } else if (id == R.id.nav_MyProfile) {
                Intent n=new Intent(this,Settings.class);
                startActivity(n);
            } else if (id == R.id.nav_GetFREERides) {
                Intent n=new Intent(this,GetFreeRides.class);
                startActivity(n);
            } else if (id == R.id.nav_Settings) {
                Intent n=new Intent(this,MainSettings.class);
                startActivity(n);
            } else if (id == R.id.nav_Language) {
                Intent n=new Intent(this,Language.class);
                startActivity(n);
            } else if (id == R.id.nav_Contactus) {
                Intent n=new Intent(this,Contactusdialog.class);
                startActivity(n);
            } else if (id == R.id.nav_Help) {
                //      Intent n=new Intent(this,Settings.class);
                //    startActivity(n);
            } else if (id == R.id.nav_Rates) {
                Intent n=new Intent(this,Rates.class);
                startActivity(n);
            } else if (id == R.id.nav_DrivethroughUs) {

                /*if(!DriverDocuments.driver_id.isEmpty()&&!DriverDocuments.driver_id.equals("no driver_id")){
                    new BookingContinueForDriver(HomeMain.this).execute(DriverDocuments.driver_id);
                 //gotodriverhome();
                }else
                {
                    Intent n=new Intent(this,DriverDetails.class);
                    startActivity(n);
                }*/

                if(DriverDocuments.driver_id.isEmpty()||DriverDocuments.driver_id.equals("no driver_id")) {
                    new GetDriverDetailsByUserId(HomeMain.this).execute(Signin.user_id);

                }else
                {
                    new BookingContinueForDriver(HomeMain.this).execute(DriverDocuments.driver_id);

                 //   gotodriverdetails();

                }
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
                DriverDocuments. driver_id="no driver_id";
                Intent n=new Intent(HomeMain.this,SigniUpSignIn.class);
                startActivity(n);
                finish();
            }else if (id == R.id.nav_Logout) {

                SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("access_token");
                editor.commit();
                Intent n=new Intent(HomeMain.this,SigniUpSignIn.class);
                startActivity(n);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }catch (Exception e){
            //showresponse(e.getMessage());
        }
        return true;
    }

    public void checkdriverbooking() {
        new BookingContinueForDriver(HomeMain.this).execute(DriverDocuments.driver_id);
    }

    public  void gotodriverdetails() {
        Intent n=new Intent(this,DriverDetails.class);
        startActivity(n);
    }

    public static void initializemytripsobject() {
        DocumentEnums.garbagecollect();
        booking_date=new ArrayList<String>(1);
        ride_start_time=new ArrayList<String>(1);
        ride_end_time=new ArrayList<String>(1);
        pickup_latitude=new ArrayList<String>(1);
        pickup_longitude=new ArrayList<String>(1);
        dropoff_latitude=new ArrayList<String>(1);
        dropoff_longitude=new ArrayList<String>(1);
        feedback=new ArrayList<String>(1);
        booking_status_id=new ArrayList<String>(1);
        amount=new ArrayList<String>(1);
        first_name=new ArrayList<String>(1);
        last_name=new ArrayList<String>(1);
        vehicle_name=new ArrayList<String>(1);
        registration_number=new ArrayList<String>(1);
        booking_status=new ArrayList<String>(1);


       sbooking_status=new ArrayList<String>(1);
        sbooking_date=new ArrayList<String>(1);
        sbooking_status_id=new ArrayList<String>(1);
        samount=new ArrayList<String>(1);
        spickup_latitude=new ArrayList<String>(1);
        spickup_longitude=new ArrayList<String>(1);
        sdropoff_latitude=new ArrayList<String>(1);
        sdropoff_longitude=new ArrayList<String>(1);
        sphone_num=new ArrayList<String>(1);
        sfirst_name=new ArrayList<String>(1);
        sbooking_time=new ArrayList<String>(1);
    }

    public void gotodriverhome() {
        Intent n=new Intent(HomeMain.this,DriverHome.class);
        startActivity(n);
        finish();
    }

    public void gotodriverroute() {
        Intent n=new Intent(HomeMain.this,DriverRoute.class);
        startActivity(n);
        finish();
    }

    public void gotodrivermetering() {
        Intent n=new Intent(HomeMain.this,DriverMetering.class);
        startActivity(n);
        finish();
    }

    public void gotomytrip() {try{
        Intent n=new Intent(this,MyTrip.class);
        startActivity(n); }catch (Exception e){
        //showresponse(e.getMessage());
    }
    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {

    }



    GoogleApiClient mGoogleApiClient;

    protected synchronized void buildGoogleApiClient() {
        try{
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }catch (Exception e){
            //showresponse(e.getMessage());
        }
    }
    double lats=24.934668;
    double longs=67.105415;
    Bitmap smallMarker=null,smallMarkers;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        DocumentEnums.garbagecollect();
        try{
            mMap = googleMap;
            mMap = googleMap;

            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }

            mMap.setOnMarkerDragListener(this);
            mMap.setOnMarkerClickListener(this);

            BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.drawable.final_marker);
            Bitmap bs=bitmapdraws.getBitmap();
            smallMarkers = Bitmap.createScaledBitmap(bs, 100, 150, false);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                latitude=24.942165825910955;
                longitude=67.1144485473633;
                LatLng hcmus = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 13));

                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title("Karachi").draggable(true).icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))
                        .position(hcmus)));
                String Token=""+ FirebaseInstanceId.getInstance().getToken();
                new GetVehicleLocationToPassengers(HomeMain.this).execute(Token,latitude+"",longitude+"");
                return;
            }else {
                mMap.setMyLocationEnabled(true);

                LatLng hcmus = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 13));

                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .title("Current Location").draggable(true).icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))
                        .position(hcmus)));
                String Token=""+ FirebaseInstanceId.getInstance().getToken();
                new GetVehicleLocationToPassengers(HomeMain.this).execute(Token,latitude+"",longitude+"");

            }


            int height = 50;
            int width = 50;
            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.cartop);
            Bitmap b=bitmapdraw.getBitmap();
            smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


            LatLng    carloc = new LatLng(24.933705, 67.103462);
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).flat(true).rotation(140)
                    .position(carloc));
            carloc = new LatLng(24.936361, 67.103870);
     /*   mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.carplus)).flat(true)
                .position(carloc)).setDraggable(true);
*/

            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                    Log.i("centerLat", String.valueOf(cameraPosition.target.latitude));

                    Log.i("centerLong", String.valueOf(cameraPosition.target.longitude));
                }
            });











            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
/*
    Marker marker = mMap.addMarker(new MarkerOptions()
            .position(latLng)
            .draggable(true));

    originMarkers.add(marker);
*/
                }
            });
            googleMap.setOnMarkerDragListener((GoogleMap.OnMarkerDragListener) this);
     /*   mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
mMap.clear();
                // TODO Auto-generated method stub
                LatLng dragPosition = marker.getPosition();
                double dragLat = dragPosition.latitude;
                double dragLong = dragPosition.longitude;
              marker = mMap.addMarker(new MarkerOptions()
                        .position(dragPosition)
                        .draggable(true));

                originMarkers.add(marker);

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                mMap.clear();
                // TODO Auto-generated method stub
                LatLng dragPosition = marker.getPosition();
                double dragLat = dragPosition.latitude;
                double dragLong = dragPosition.longitude;
            marker = mMap.addMarker(new MarkerOptions()
                        .position(dragPosition)
                        .draggable(true));

                originMarkers.add(marker);

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mMap.clear();
                // TODO Auto-generated method stub
                LatLng dragPosition = marker.getPosition();
                double dragLat = dragPosition.latitude;
                double dragLong = dragPosition.longitude;
                marker = mMap.addMarker(new MarkerOptions()
                        .position(dragPosition)
                        .draggable(true));

                originMarkers.add(marker);

            }
        });

*/






        }catch (Exception e){
            //showresponse(e.getMessage());
        }
    }
    Marker marker;

    @Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        DocumentEnums.garbagecollect();
        try{
            // TODO Auto-generated method stub

            LatLng dragPosition = arg0.getPosition();
            double dragLat = dragPosition.latitude;
            double dragLong = dragPosition.longitude;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dragPosition, 13));
            Log.i("info", "on drag   end :" + dragLat + " dragLong :" + dragLong);
            Pickuploc=dragPosition;
            arg0.setTitle("Selected Location");
            //  Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG). show();
            try {
                getaddress(dragLat,dragLong,arg0);
                String Token=""+ FirebaseInstanceId.getInstance().getToken();
                new GetVehicleLocationToPassengers(HomeMain.this).execute(Token,dragLat+"",dragLong+"");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            //showresponse(e.getMessage());
        }
    }

    public void getvehicleonmap() {
        DocumentEnums.garbagecollect();
        try {
            mMap.clear();
            mMap.clear();
            if(pickupmarker!=null){
                pickupmarker.remove();
                //          originMarkers.remove(0);
            }
            pickupmarker=mMap.addMarker(new MarkerOptions()
                    .title("PickUp Location").draggable(true).icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))
                    .position(Pickuploc));
          /*  originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title("PickUp Location").draggable(true).icon(BitmapDescriptorFactory.fromBitmap(smallMarkers))
                    .position(Pickuploc)));
*/
            for (int i = 0; i < VehicleId.size(); i++) {

                int height = 50;
                int width = 50;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cartop);
                Bitmap b = bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                LatLng carloc = new LatLng(Double.valueOf(Latitude.get(i)), Double.valueOf(Longitude.get(i)));
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).flat(true).rotation(140)
                        .position(carloc));
            }
        }catch (Exception e){
            //showresponse(e.getMessage());
        }
    }
     public void getaddress(double dragLat,double dragLong,Marker arg) throws IOException {

        try {
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
            pickupaddress = address;
            arg.setTitle(address + "");
            arg.setTag(knownName);

            txtcurlocation.setText(address + " ");
            TxtPickupLoc = txtcurlocation.getText().toString();
        }catch (Exception e){
            String msg=e.getMessage();
//showresponse(e.getMessage());
        }
        DocumentEnums.garbagecollect();
    }
    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    Location mLastLocation;
    Marker mCurrLocationMarker;
    @Override
    public void onLocationChanged(Location location) {
        DocumentEnums.garbagecollect();
        try{
            Log.d("onLocationChanged", "entered");

            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }else{
                String Token=""+ FirebaseInstanceId.getInstance().getToken();
                new GetVehicleLocationToPassengers(HomeMain.this).execute(Token,location.getLatitude()+"",location.getLongitude()+"");
            }

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            DocumentEnums.current_latitude=Double.toString(latitude);
            DocumentEnums.current_longitude=Double.toString(longitude);

            //  Pickuploc.latitude=latitude;
            //  Pickuploc.longitude=longitude;
//Dropoffloc=new LatLng(latitude,longitude);
            Pickuploc=new LatLng(latitude,longitude);
            BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.drawable.final_marker);
            Bitmap bs=bitmapdraws.getBitmap();
            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 100, 150, false);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.draggable(true);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarkers));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            try {
                getaddress(latitude,longitude,mCurrLocationMarker);
                //  Toast.makeText(HomeMain.this,latitude+","+longitude, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            //      end_latitude=latitude;
//        end_longitude=longitude;

            //      Dropoffloc=new LatLng(latitude,longitude);
//directionpath();
            try {
                if(!DriverDocuments.vehicle_id.equals("no vehicle_id")&&DriverDocuments.vehicle_id!=""&&!DriverDocuments.vehicle_id.isEmpty())
                    new UpdateVehicleLocation(HomeMain.this).execute(Double.toString(latitude), Double.toString(longitude), DriverDocuments.vehicle_id);
            }catch (Exception e){

            }


            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                //    Log.d("onLocationChanged", "Removing Location Updates");
                //   showresponse("Removing Location Updates");
            }
        }catch (Exception e){
            //showresponse(e.getMessage());
        }
    }

    public void showresponse(String message) {
        DocumentEnums.garbagecollect();
        try{
            Toast.makeText(getApplicationContext(),
                    "" + message, Toast.LENGTH_LONG)
                    .show();
        }catch (Exception e){
//        showresponse(e.getMessage());
        }
    }

}
