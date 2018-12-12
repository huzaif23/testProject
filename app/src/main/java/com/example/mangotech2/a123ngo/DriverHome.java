package com.example.mangotech2.a123ngo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Modules.DirectionFinderListener;
import com.example.mangotech2.a123ngo.Modules.Route;
import com.example.mangotech2.a123ngo.Parsing_JSON.ChangeDriverStatus;
import com.example.mangotech2.a123ngo.Parsing_JSON.DriverBookingHistory;
import com.example.mangotech2.a123ngo.Parsing_JSON.GetScheduledRidesByDriverId;
import com.example.mangotech2.a123ngo.Parsing_JSON.IsDriverSubscribed;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignOutDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateDriverLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class DriverHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,OnMapReadyCallback, DirectionFinderListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
public static double vehicle_latitude;
    public static double vehicle_longitude;
    com.example.mangotech2.a123ngo.GPSTracker gps;
    TextView txtvehicleregno,txtvehiclename,txtdriveremailaddress,txtdriverusername,txtdrivervehicletypeid,txtdriverrating;
    private NavigationView navview;
    private DrawerLayout drawerlayout;
public static int tripCounter=1;
    public static ArrayList<String> booking_status,booking_date,ride_start_time,ride_end_time,pickup_latitude,pickup_longitude,dropoff_latitude,dropoff_longitude,feedback,booking_status_id,amount,first_name,last_name,vehicle_name,registration_number;
    public static ArrayList<String> sbooking_time,sbooking_status, sbooking_date,sbooking_status_id,samount,spickup_latitude,spickup_longitude,sdropoff_latitude,sdropoff_longitude,sfirst_name,sphone_num;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;
    public static DriverHome contextdriverhome;
   public static LinearLayout tvsubscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        DocumentEnums.garbagecollect();
        Intitializedrivermytrpobject();
        contextdriverhome=DriverHome.this;
        this.drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navview = (NavigationView) findViewById(R.id.nav_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            View headerView = navigationView.getHeaderView(0);
            txtdriverusername = (TextView) headerView.findViewById(R.id.txtdriverusername);
            txtdriveremailaddress = (TextView) headerView.findViewById(R.id.txtdriveremailaddress);
            txtdriverusername.setText(Signin.userName + " ");
            txtdriveremailaddress.setText(Signin.emailaddress + " ");
        }catch (Exception e){

            String message=e.getMessage();

        }

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        gps = new GPSTracker(DriverHome.this);
        try {
            getApplication().startService(Splash.serviceintent);
        }catch (Exception e){

        }
try {
    txtdrivervehicletypeid= (TextView) findViewById(R.id.txtdrivervehicletypeid);
    if(DriverDocuments.vehicle_type_id.equals(DocumentEnums.Bike)){
        txtdrivervehicletypeid.setText("BIKE");
    }else  if(DriverDocuments.vehicle_type_id.equals(DocumentEnums.Auto)){
        txtdrivervehicletypeid.setText("AUTO");
    }else  if(DriverDocuments.vehicle_type_id.equals(DocumentEnums.Goplus)){
        txtdrivervehicletypeid.setText("GO");
    }else  if(DriverDocuments.vehicle_type_id.equals(DocumentEnums.Go_X)){
        txtdrivervehicletypeid.setText("GO+");
    }else  if(DriverDocuments.vehicle_type_id.equals(DocumentEnums.Business)){
        txtdrivervehicletypeid.setText("BUSINESS");
    }else  if(DriverDocuments.vehicle_type_id.equals(DocumentEnums.Taxi)){
        txtdrivervehicletypeid.setText("TAXI");
    }else  if(DriverDocuments.vehicle_type_id.equals(DocumentEnums.Bus)){
        txtdrivervehicletypeid.setText("BUS");
    }
    txtvehicleregno = (TextView) findViewById(R.id.txtvehicleregno);
    txtvehicleregno.setText(DriverDocuments.registration_number.toString());
    txtvehiclename = (TextView) findViewById(R.id.txtvehiclename);
    txtvehiclename.setText(DriverDocuments.vehicle_name.toString());
    new ChangeDriverStatus(DriverHome.this).execute(DriverDocuments.driver_id,"1");
}catch(Exception e){

    new ChangeDriverStatus(DriverHome.this).execute(DriverDocuments.driver_id,"1");
}// check if GPS enabled
        if(gps.canGetLocation()){
            gps.getLocation();

            vehicle_latitude = gps.getLatitude();
            vehicle_longitude = gps.getLongitude();

      //      new UpdateDriverLocation(DriverHome.this).execute(Double.toString(vehicle_latitude),Double.toString(vehicle_longitude),"6","1");

        }
        tvsubscription=(LinearLayout)findViewById(R.id.tvsubscription);

        new IsDriverSubscribed(DriverHome.this).execute(Signin.user_id);


    }

    public static void Intitializedrivermytrpobject() {
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

    public static  int isbusy=0;
    public void btnmakebusy(View v){
        DocumentEnums.garbagecollect();

        if(isbusy==1){
    isbusy=0;
    new ChangeDriverStatus(DriverHome.this).execute(DriverDocuments.driver_id,"1");
    getSupportFragmentManager()
            .findFragmentById(R.id.map)
            .getView().setVisibility(View.VISIBLE);
    v.setBackground(getResources().getDrawable(R.drawable.final_btnnext));
    getApplication().startService(Splash.serviceintent);
}else
{
    new ChangeDriverStatus(DriverHome.this).execute(DriverDocuments.driver_id,"2");
    isbusy=1;
    getSupportFragmentManager()
            .findFragmentById(R.id.map)
            .getView().setVisibility(View.INVISIBLE);
    v.setBackgroundColor(Color.RED);
    getApplication().stopService(Splash.serviceintent);
}
       // Intent n=new Intent(DriverHome.this,DriverRoute.class);
        //startActivity(n);
    }
    @Override
    public void onBackPressed() {
        DocumentEnums.garbagecollect();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            try {
                finish();

                Process.killProcess(Process.myPid());
                System.exit(0);
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
      //  getMenuInflater().inflate(R.menu.driver_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
       //     Intent n=new Intent(this,DriverHome.class);
        //    startActivity(n);
         //   finish();
        } else if (id == R.id.nav_MyTrips) {
            Intitializedrivermytrpobject();
            Intent n=new Intent(this,Driver_MyTrip.class);
            startActivity(n);

        } else if (id == R.id.nav_MyProfile) {
            Intent n=new Intent(this,Settings.class);
            startActivity(n);
        } else if (id == R.id.nav_Settings) {
            Intent n=new Intent(this,MainSettings.class);
            startActivity(n);
        } /*else if (id == R.id.nav_Report) {
            Intent n=new Intent(this,DriverReport.class);
            startActivity(n);
        } */else if (id == R.id.nav_Language) {
            Intent n=new Intent(this,Language.class);
            startActivity(n);
        } else if (id == R.id.nav_Contactus) {
            Intent n=new Intent(this,Contactusdialog.class);
            startActivity(n);
        } else if (id == R.id.nav_Subscription) {
            new IsDriverSubscribed(DriverHome.this).execute(Signin.user_id);
            if(DocumentEnums.IsDriverSubscribed.equals("true")){
                tvsubscription.setVisibility(View.GONE);
                showresponse("Already Subscribed");
            }else{
                tvsubscription.setVisibility(View.VISIBLE);
                Intent n=new Intent(this,DriverSubscription.class);
                startActivity(n);
            }

        } else if (id == R.id.nav_Help) {
            //      Intent n=new Intent(this,Settings.class);
            //    startActivity(n);
        } else if (id == R.id.nav_Rates) {
            Intent n=new Intent(this,Rates.class);
            startActivity(n);
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
            DriverDocuments.driver_id=null;
            DriverDocuments. driver_id="no driver_id";
            Intent n=new Intent(DriverHome.this,SigniUpSignIn.class);
            startActivity(n);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    public void gotomytrip() {try{
        DocumentEnums.garbagecollect();

        Intent n=new Intent(this,Driver_MyTrip.class);
        startActivity(n); }catch (Exception e){
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


    private GoogleMap mMap;

    double latitude,longitude;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DocumentEnums.garbagecollect();


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
            }
        } else {
            buildGoogleApiClient();
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {

    }

    @Override
    public void onMarkerDrag(Marker arg0) {

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {

    }

    @Override
    public void onMarkerDragStart(Marker arg0) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        DocumentEnums.garbagecollect();

        try{
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(10000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(15);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        DocumentEnums.garbagecollect();

        mMap.clear();
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        DocumentEnums.current_latitude=Double.toHexString(location.getLatitude());
        DocumentEnums.current_longitude=Double.toHexString(location.getLongitude());

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        int height = 75;
        int width = 75;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.newcar);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap   smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).flat(true)
                .position(latLng));

        new UpdateDriverLocation().execute(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()), DriverDocuments.vehicle_id);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        //stop location updates
        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            //        Log.d("onLocationChanged", "Removing Location Updates");
        }
    }
}
