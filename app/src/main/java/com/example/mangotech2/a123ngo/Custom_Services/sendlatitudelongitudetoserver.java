package com.example.mangotech2.a123ngo.Custom_Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.DriverDocuments;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.GetDistanceData;
import com.example.mangotech2.a123ngo.HomeMain;
import com.example.mangotech2.a123ngo.Modules.DirectionFinderListener;
import com.example.mangotech2.a123ngo.Modules.Route;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateDriverLocation;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateVehicleLocation;
import com.example.mangotech2.a123ngo.R;
import com.example.mangotech2.a123ngo.mapdirection;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class sendlatitudelongitudetoserver extends Service implements OnMapReadyCallback, DirectionFinderListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    public sendlatitudelongitudetoserver() {
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startwork();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // Log.i("EXIT", "ondestroy!");
     //   Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
      //  sendBroadcast(broadcastIntent);
      //  stoptimertask();
    }


    public void startwork() {
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
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
    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
       }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
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
        try{
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(25000);
            mLocationRequest.setFastestInterval(25000);
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

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        DocumentEnums.current_latitude=Double.toHexString(location.getLatitude());
        DocumentEnums.current_longitude=Double.toHexString(location.getLongitude());

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            if(!DriverDocuments.vehicle_id.equals("no vehicle_id")&&DriverDocuments.vehicle_id!=""&&!DriverDocuments.vehicle_id.isEmpty()&&DriverDocuments.vehicle_id!="0")
                new UpdateDriverLocation().execute(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()), DriverDocuments.vehicle_id);

        }catch (Exception e){

        }
        //stop location updates
        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    //        Log.d("onLocationChanged", "Removing Location Updates");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

}
