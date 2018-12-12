package com.example.mangotech2.a123ngo;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Modules.DirectionFinderListener;
import com.example.mangotech2.a123ngo.Modules.Route;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingCancelFromPassenger;
import com.example.mangotech2.a123ngo.Parsing_JSON.CompleteRidePostRequest;
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

public class DriverMetering extends AppCompatActivity  implements OnMapReadyCallback, DirectionFinderListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener
        ,GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener{

    private GoogleMap mMap;
    LatLng PassengerDropOffLoc;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    public static double latitude, longitude;
    LocationRequest mLocationRequest;
    public static double totaldistance;
    public static String totalcollectedfare,BaseFare,TotalDistance,TotalTimeInMinutes,Promotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_metering);
        DocumentEnums.garbagecollect();


        // onNewIntent(getIntent());
     //   FirebaseMessaging.getInstance().
      //          subscribeToTopic("ServiceNow");



        try{
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            totaldistance=0;
        PassengerDropOffLoc=new LatLng(Double.valueOf(DriverRoute.dropOffLatitude),Double.valueOf(DriverRoute.dropOffLongitude));
    }catch (Exception e){

        }
    }

    final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            int rem_sec=bundle.getInt("rem_sec");

        }
    };
    static   String strSDesc = "ShortDesc";
    static String strIncidentNo = "IncidentNo";
    static String strDesc="IncidentNo";
    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {

            setContentView(R.layout.activity_driver_metering);

            DocumentEnums.garbagecollect();

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
        }
    }

    public void browser1(View view){
        Intent browserIntent =new Intent(Intent.ACTION_VIEW, Uri.parse("https://somebrowser?uri=incident.do?sysparm_query=number=" +strIncidentNo));
        startActivity(browserIntent);

    }
    String BookingDetailId="5",BookingStatusId="5",RideTotalTime="3600",RideStartTime="15:30",RideEndTime="17:30",TotalDistanceInKM="5.3",DropOffLatitude="28.987983",DropOffLongitude="28.987983";
    public  void btndriverendride(View v){

        DocumentEnums.garbagecollect();

        try {
// custom dialog
            final Dialog dialog = new Dialog(DriverMetering.this);
            dialog.setContentView(R.layout.completeride_confirmation);
            dialog.setTitle("Confirmation");

            // set the custom dialog components - text, image and button

            Button btncompleteconfirmationyes = (Button) dialog.findViewById(R.id.btncompleteconfirmationyes);
            Button btncompleteconfirmationno = (Button) dialog.findViewById(R.id.btncompleteconfirmationno);
            // if button is clicked, close the custom dialog
            btncompleteconfirmationyes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
                        Date currentLocalTime = cal.getTime();
                        currentLocalTime.setHours(currentLocalTime.getHours());
                        DateFormat date = new SimpleDateFormat("HH:mm");
// you can get seconds by adding  "...:ss" to it
                        date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
                        String endtime = date.format(currentLocalTime);
                        new CompleteRidePostRequest(DriverMetering.this).execute(DriverBooking.BookingDetailId, endtime, String.format ("%.2f", totaldistance).toString(), Double.valueOf(mLastLocation.getLatitude()).toString(),Double.valueOf(mLastLocation.getLongitude()).toString(),"1","1");

                        dialog.dismiss();
                    }catch (Exception e){

                    }
                }
            });
            btncompleteconfirmationno.setOnClickListener(new View.OnClickListener() {
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

    public void ridecompletedbydriver() {
        DocumentEnums.garbagecollect();

        Intent n = new Intent(DriverMetering.this, DriverCollectCash.class);
        startActivity(n);
        finish();
    }

    public void showresponse(String message) {
        DocumentEnums.garbagecollect();

        Toast.makeText(getApplicationContext(),
                "" + message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onBackPressed() {

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
            mLocationRequest.setSmallestDisplacement(25);
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
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
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        DocumentEnums.garbagecollect();

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    @Override
    public void onLocationChanged(Location location) {
        DocumentEnums.garbagecollect();

        try {
            Log.d("onLocationChanged", "entered");
            mMap.clear();
            if(mLastLocation!=null){
                double distance=distance(mLastLocation.getLatitude(),mLastLocation.getLongitude(),location.getLatitude(),location.getLongitude());
            totaldistance=totaldistance+distance;
            }
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
//Dropoffloc=new LatLng(latitude,longitude);
            BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.cartop);
            Bitmap bs = bitmapdraws.getBitmap();
            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 100, 150, false);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.draggable(false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarkers));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.final_searchred);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 75, 100, false);
            mMap.addMarker(new MarkerOptions()
                    .title("Drop Off Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .position(PassengerDropOffLoc));

        /*
       try {
            getaddress(latitude,longitude,mCurrLocationMarker);
        } catch (IOException e) {
            e.printStackTrace();
        }
      */
            directionpath();

            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitude + "," + longitude + "&destinations=" + DriverRoute.dropOffLatitude + "," + DriverRoute.dropOffLongitude + "&mode=driving&language=en-EN&sensor=false");

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            directionpath();

            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitude + "," + longitude + "&destinations=" + DriverRoute.dropOffLatitude + "," + DriverRoute.dropOffLongitude + "&mode=driving&language=en-EN&sensor=false");

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
        }catch (Exception e){

        }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
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
        }catch (Exception e){

        }
    }

    protected synchronized void buildGoogleApiClient() {
        try {
            DocumentEnums.garbagecollect();


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        }catch (Exception e){

        }
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
            dataTransfer[2] = new LatLng(Double.valueOf(DriverRoute.dropOffLatitude), Double.valueOf(DriverRoute.dropOffLongitude));
            Object returnval= new GetDirectionsData().execute(dataTransfer);
            //tvdistance.setText(""+getDirectionsData.distance);

            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+latitude+","+longitude+"&destinations="+DriverRoute.dropOffLatitude+","+DriverRoute.dropOffLatitude+"&mode=driving&language=en-EN&sensor=false");
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
                    .title("Drop Off Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .position(PassengerDropOffLoc)).setDraggable(true);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(latitude, longitude));
            builder.include(new LatLng(PassengerDropOffLoc.latitude, PassengerDropOffLoc.longitude));
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
    private String getDirectionsUrl()
    {
        DocumentEnums.garbagecollect();

        try{
            StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
            googleDirectionsUrl.append("&origin="+latitude+","+longitude);
            googleDirectionsUrl.append("&destination="+DriverRoute.dropOffLatitude+","+DriverRoute.dropOffLongitude);
            googleDirectionsUrl.append("&key=AIzaSyCApwhgQ13jXMGkKCM61BL3nT-UDe7lGPs");
            return googleDirectionsUrl.toString();
        }
        catch (Exception e){
            Log.d(e.toString(),e.getMessage());
            return null;
        }
    }
}
