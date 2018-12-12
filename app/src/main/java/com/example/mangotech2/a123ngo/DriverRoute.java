package com.example.mangotech2.a123ngo;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Modules.DirectionFinderListener;
import com.example.mangotech2.a123ngo.Modules.Route;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingCancelFromDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.GoForPickup;
import com.example.mangotech2.a123ngo.Parsing_JSON.NotifyUserDriverReached;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateRideStatus;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class DriverRoute extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener
        ,GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener
{
    private GoogleMap mMap;
    LatLng PassengerPickupLoc;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    Button btnCancelBookinfFromDriver;
    public static double latitude, longitude;
    private android.widget.TextView textView21;
    private android.widget.TextView textView22;
    private android.widget.TextView txtpassengername;
    private android.widget.Button txtpassengercontact;
    private android.widget.TextView textView25;
    private android.widget.TextView textView27;
    private android.widget.ImageView imageView6;
    private Button btnRideStart,btnGoforpickup;
   public static String dropOffLatitude="0",dropOffLongitude="0",BookingDetailId,booking_time,booking_date;
    public static LinearLayout layoutgoforpickup,layoutridestart;
    public static boolean fromtrips=false;
    private Button btnreached;
    private LinearLayout layoutreached;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_route);
        this.layoutreached = (LinearLayout) findViewById(R.id.layoutreached);
        this.btnreached = (Button) findViewById(R.id.btnreached);
        DocumentEnums.garbagecollect();

        this.btnRideStart = (Button) findViewById(R.id.btnRideStart);
        this.btnGoforpickup = (Button) findViewById(R.id.btnGoforpickup);
        btnGoforpickup.setEnabled(false);

        btnGoforpickup.setBackgroundColor(Color.parseColor("#d7d4d4"));

     //   this.imageView6 = (ImageView) findViewById(R.id.imageView6);
     //   this.textView27 = (TextView) findViewById(R.id.textView27);
        this.textView25 = (TextView) findViewById(R.id.textView25);
        this.txtpassengercontact = (Button) findViewById(R.id.txtpassengercontact);
        this.txtpassengername = (TextView) findViewById(R.id.txtpassengername);
        this.textView22 = (TextView) findViewById(R.id.textView22);
        this.textView21 = (TextView) findViewById(R.id.textView21);
        this.layoutgoforpickup = (LinearLayout) findViewById(R.id.layoutgoforpickup);
        layoutgoforpickup.setVisibility(View.GONE);
        this.layoutridestart = (LinearLayout) findViewById(R.id.layoutridestart);
layoutgoforpickup.setVisibility(View.GONE);

        layoutreached.setVisibility(View.VISIBLE);
        btnreached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutreached.setVisibility(View.GONE);
                layoutridestart.setVisibility(View.VISIBLE);
                new NotifyUserDriverReached(DriverRoute.this).execute(DriverBooking.BookingDetailId);
            }
        });

        if(fromtrips){
            layoutgoforpickup.setVisibility(View.VISIBLE);
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
            Date currentLocalTime = cal.getTime();
            currentLocalTime.setHours(currentLocalTime.getHours());
            DateFormat date = new SimpleDateFormat("HH:mm");
// you can get seconds by adding  "...:ss" to it
            date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
            String time = date.format(currentLocalTime);

             date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
// you can get seconds by adding  "...:ss" to it
            date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
            String datetime = date.format(currentLocalTime);

/*
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date date1 = null;
            try {

                date1 = format.parse(time);
            Date date2 = format.parse(booking_time);
            long difference = date2.getTime() - date1.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

  */
            String dateStart = datetime;
            String dateStop = booking_date+" "+booking_time;

            //HH converts hour in 24 hours format (0-23), day calculation
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Date d1 = null;
            Date d2 = null;

            try {
                d1 = format.parse(dateStart);
                d2 = format.parse(dateStop);

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);

                long totalminutes=(diffHours*60)+(diffMinutes)+(diffSeconds/60);
if(diffDays==0&&totalminutes<=30&&totalminutes>=0){
    btnGoforpickup.setEnabled(true);
    btnGoforpickup.setBackground(getResources().getDrawable(R.drawable.btn_now));
}else if(totalminutes<0){
    showresponse("You have missed your ride!");
}else{
    showresponse("Go For PickUp will be enabled before 30 minutes");
}

            } catch (Exception e) {
                e.printStackTrace();
            }


layoutgoforpickup.setVisibility(View.VISIBLE);
      //      layoutridestart.setVisibility(View.GONE);
        }
        btnGoforpickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GoForPickup(DriverRoute.this).execute(DriverRoute.BookingDetailId);
                layoutgoforpickup.setVisibility(View.GONE);
                layoutreached.setVisibility(View.VISIBLE);
            }
        });
        try {
            PassengerPickupLoc = new LatLng(Double.valueOf(DriverBooking.PickupLatitude), Double.valueOf(DriverBooking.PickupLongitude));
        }catch (Exception e) {

        }// Obtain the SupportMapFragment and get notified when the map is ready to be used.

      try {
          latitude=0.0;
          longitude=0.0;
          latitude = Double.valueOf(DocumentEnums.current_latitude);
          longitude = Double.valueOf(DocumentEnums.current_longitude);
          directionpath();
      }catch (Exception e){
          //showresponse(latitude+","+longitude);
      }
      SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        try{
            txtpassengername.setText(DriverBooking.PassengerName);
            txtpassengercontact.setText("Call");
            txtpassengercontact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+DriverBooking.PassengerPhoneNo));
                        if (ActivityCompat.checkSelfPermission(DriverRoute.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding

                            //  public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults);
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(callIntent);
                    }catch (Exception e){

                    }
                }
            });
        }catch (Exception e){

        }
        btnCancelBookinfFromDriver=(Button)findViewById(R.id.btnCancelBookinfFromDriver);
        btnCancelBookinfFromDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
// custom dialog
                    final Dialog dialog = new Dialog(DriverRoute.this);
                    dialog.setContentView(R.layout.cancel_confirmation_dialog);
                    dialog.setTitle("Confirmation");

                    // set the custom dialog components - text, image and button

                    Button btncancelconfirmationyes = (Button) dialog.findViewById(R.id.btncancelconfirmationyes);
                    Button btncancelconfirmationno = (Button) dialog.findViewById(R.id.btncancelconfirmationno);
                    // if button is clicked, close the custom dialog
                    btncancelconfirmationyes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                new BookingCancelFromDriver(DriverRoute.this).execute(DriverBooking.BookingDetailId);
                                Intent n=new Intent(DriverRoute.this,DriverHome.class);
                                startActivity(n);
                                finish();

                            }catch (Exception e){
                                //showresponse(e.getMessage().toString());
                            }
                        }
                    });
                    btncancelconfirmationno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }catch (Exception e){
                    String msg=e.getMessage();
                }



            }
        });
    }
    public void btnRideStart(View v){
        DocumentEnums.garbagecollect();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
        Date currentLocalTime = cal.getTime();
        currentLocalTime.setHours(currentLocalTime.getHours());
        DateFormat date = new SimpleDateFormat("HH:mm");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
        String currtime = date.format(currentLocalTime);
        new UpdateRideStatus(DriverRoute.this).execute(Signin.user_id,DriverBooking.BookingDetailId,"1",currtime,"0","","");
/*
 new Thread() {
            public void run() {

                try {
                    Thread.sleep(5000);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                   //        new UpdateDriverLocation(DriverRoute.this).execute(Signin.user_id,DriverBooking.BookingDetailId,"2","15.30","16.20","","");
                    }
                    //     Toast.makeText(getApplicationContext(),"UPDated Ride",Toast.LENGTH_SHORT);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
*/
   //     ridestarted();
    }

    public void ridestarted() {
        DocumentEnums.garbagecollect();

        loaded();
        Intent n=new Intent(DriverRoute.this,DriverMetering.class);
        startActivity(n);
        finish();
    }

    public void showresponse(String message){
        Toast.makeText(getApplicationContext(),
                " "+message , Toast.LENGTH_LONG)
                .show();
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
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(20);
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

        Log.d("onLocationChanged", "entered");
        mMap.clear();
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
//Dropoffloc=new LatLng(latitude,longitude);
        BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.drawable.cartop);
        Bitmap bs=bitmapdraws.getBitmap();
        Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 100, 150, false);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarkers));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.final_marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 100, false);
        mMap.addMarker(new MarkerOptions()
                .title("Passenger Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .position(PassengerPickupLoc));

        /*
       try {
            getaddress(latitude,longitude,mCurrLocationMarker);
        } catch (IOException e) {
            e.printStackTrace();
        }
      */  directionpath();

        new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+latitude+","+longitude+"&destinations="+DriverBooking.PickupLatitude+","+DriverBooking.PickupLongitude+"&mode=driving&language=en-EN&sensor=false");

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        //      end_latitude=latitude;
//        end_longitude=longitude;

        //      Dropoffloc=new LatLng(latitude,longitude);
//directionpath();
        //   Toast.makeText(mapdirection.this,"Your Current Location", Toast.LENGTH_LONG).show();


        //stop location updates
        if (mGoogleApiClient != null) {
            //    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            //      Log.d("onLocationChanged", "Removing Location Updates");
        }


    }

    @Override
    public void onBackPressed() {
if(fromtrips){
    super.onBackPressed();
}
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        DocumentEnums.garbagecollect();

        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        DocumentEnums.garbagecollect();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }



    final GetDirectionsData getDirectionsData= new GetDirectionsData();

    public static String strdistance,strtime;
    public static double distance,dis,tim;
    static int count=0;
    public void directionpath(){

        DocumentEnums.garbagecollect();

        try{
            Object dataTransfer[] = new Object[2];
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            dataTransfer = new Object[3];
            String   url = getDirectionsUrl();
            //getDirectionsData = new GetDirectionsData();
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            dataTransfer[2] = new LatLng(Double.valueOf(DriverBooking.PickupLatitude), Double.valueOf(DriverBooking.PickupLongitude));
            Object returnval= new GetDirectionsData().execute(dataTransfer);
            //tvdistance.setText(""+getDirectionsData.distance);

            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+latitude+","+longitude+"&destinations="+DriverBooking.PickupLatitude+","+DriverBooking.PickupLongitude+"&mode=driving&language=en-EN&sensor=false");
            mMap.clear();

            BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.drawable.cartop);
            Bitmap bs=bitmapdraws.getBitmap();
            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 50, 50, false);
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarkers)).draggable(false)
                    .position(new LatLng(latitude,longitude)));

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.final_searchred);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 100, false);
            mMap.addMarker(new MarkerOptions()
                    .title("Passenger Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .position(PassengerPickupLoc)).setDraggable(false);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(latitude, longitude));
            builder.include(new LatLng(PassengerPickupLoc.latitude, PassengerPickupLoc.longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 250));



        /*          final Handler handler = new Handler() {

                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();

           //                 tvdistance.setText(String.valueOf(bundle.getString("distance")));
                        }
                    };
*/
/*
                    Thread th=new Thread(){
                        @Override
                        public void run() {
                            try{
                                while(getDirectionsData.distance==null)
                                {
                                    sleep(500);
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString("distance", getDirectionsData.distance);
                                Message message = new Message();
                                message.setData(bundle);

                                handler.sendMessage(message);
                            }catch (Exception e){

                            }finally {

                                Bundle bundle = new Bundle();
                                bundle.putString("distance", getDirectionsData.distance);
                                Message message = new Message();
                                message.setData(bundle);

                                handler.sendMessage(message);

                                //if(i%25 == 0) {

                            }
                        }
                    };
                    th.start();
*/
        }catch (Exception e){
            Log.d(e.toString(),e.getMessage());
        }

    }


    public ProgressDialog dialog;
    public void loading(){
        DocumentEnums.garbagecollect();

        try{

            dialog = new ProgressDialog(DriverRoute.this);
            dialog.setMessage("Ride Starting");
            dialog.show();
        }catch (Exception e){
        }
    }

    public void loaded(){
        try{

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }catch (Exception e){

        }
    }

    private String getDirectionsUrl()
    {
        DocumentEnums.garbagecollect();

        try{
            StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
            googleDirectionsUrl.append("&origin="+latitude+","+longitude);
            googleDirectionsUrl.append("&destination="+DriverBooking.PickupLatitude+","+DriverBooking.PickupLongitude);
            googleDirectionsUrl.append("&key=AIzaSyCApwhgQ13jXMGkKCM61BL3nT-UDe7lGPs");
            return googleDirectionsUrl.toString();
        }
        catch (Exception e){
            Log.d(e.toString(),e.getMessage());
            return null;
        }
    }
}