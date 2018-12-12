package com.example.mangotech2.a123ngo;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.DriverBookingConfirmed;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateVehicleLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

public class DriverBooking extends AppCompatActivity  implements LocationListener{
    LocationManager mLocationManager;
    TextView tvrem_sec;
    public static String BookingDate,BookingTime,booking_details_id,dropoff_longitude,dropoff_latitude,booking_time,booking_date,Message,IsBookLater, BookRide, BookingDetailId, VehicleId, PickupLatitude, PickupLongitude, DropoffLatitude, DropoffLongitude, EstimatedFares, DistanceInKm, PickUpLocationName, DropoffLocationName, SurgeFare,PassengerName,PassengerPhoneNo;
    private Button txtsurgefare;
    private Button txtestimatedfare;
    private TextView txtpickupaddress;
    private TextView textView30;
    private TextView textView31;
    private TextView txtdropoffaddress;
    private TextView textView28;
    private TextView txtpaymentmethod;
    private Button btnaccept;
    private Button btnreject;
    private TextView lblremsec,bookingdate,bookingtime;
    public static double vehicle_latitude;
    public static double vehicle_longitude;
    public static Double Latitude,Longitude;
    LinearLayout scheduledlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_booking);
        DocumentEnums.garbagecollect();

        scheduledlayout=(LinearLayout)findViewById(R.id.scheduledlayout);

        this.lblremsec = (TextView) findViewById(R.id.lblrem_sec);
        this.btnreject = (Button) findViewById(R.id.btnreject);
        this.btnaccept = (Button) findViewById(R.id.btnaccept);
        this.txtpaymentmethod = (TextView) findViewById(R.id.txtpaymentmethod);
        this.bookingdate = (TextView) findViewById(R.id.bookingdate);
        this.bookingtime = (TextView) findViewById(R.id.bookingtime);
        bookingdate.setText("Date: "+BookingDate);
        bookingtime.setText("Time: "+BookingTime);
        this.textView28 = (TextView) findViewById(R.id.textView28);
        this.txtdropoffaddress = (TextView) findViewById(R.id.txtdropoffaddress);
        this.textView31 = (TextView) findViewById(R.id.textView31);
        this.textView30 = (TextView) findViewById(R.id.textView30);
        this.textView28 = (TextView) findViewById(R.id.textView28);
        this.txtpickupaddress = (TextView) findViewById(R.id.txtpickupaddress);
        this.txtestimatedfare = (Button) findViewById(R.id.txtestimatedfare);
        this.txtsurgefare = (Button) findViewById(R.id.txtsurgefare);
        tvrem_sec = (TextView) findViewById(R.id.lblrem_sec);
        try {
            if (IsBookLater.equals("1")) {
                scheduledlayout.setVisibility(View.VISIBLE);
            } else {
                scheduledlayout.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }



        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
            vehicle_latitude = location.getLatitude();
            vehicle_longitude = location.getLongitude();
//              new UpdateVehicleLocation(DriverBooking.this).execute(Double.toString(vehicle_latitude), Double.toString(vehicle_longitude), DriverBooking.VehicleId, Signin.user_id, DriverBooking.BookingDetailId);


        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


        try {

            //  gps = new GPSTracker(DriverBooking.this);
            txtsurgefare.setText(SurgeFare);
            txtestimatedfare.setText(EstimatedFares);
            txtpickupaddress.setText(PickUpLocationName);
            txtdropoffaddress.setText(DropoffLocationName);
        } catch (Exception e) {
            //showresponse(e.getMessage());
        }
        new Thread() {
            public void run() {

                while (i > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                      //  showresponse(e.getMessage());
                    }
                    tvrem_sec.post(new Runnable() {
                        public void run() {
                            tvrem_sec.setText(i + " sec");
                            if (i == 0) {
                                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancelAll();
                                finish();
                            }
                            i--;
                        }
                    });
                }
            }
        }.start();

        final Button btnaccept = (Button) findViewById(R.id.btnaccept);
        btnaccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnaccept.setEnabled(false);
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        new DriverBookingConfirmed(DriverBooking.this).execute(BookingDetailId, Signin.user_id, VehicleId);
                    }

                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();


            //        updatelocationtoserver();
/*
                    // check if GPS enabled
                    if (gps.canGetLocation()) {
                        gps.getLocation();
                        vehicle_latitude = gps.getLatitude();
                        vehicle_longitude = gps.getLongitude();
                   //     new UpdateVehicleLocation(DriverBooking.this).execute(Double.toString(vehicle_latitude), Double.toString(vehicle_longitude), DriverBooking.VehicleId, Signin.user_id, DriverBooking.BookingDetailId).wait(5000);


                        while (!BookRide.equals(String.valueOf(DocumentEnums.CompleteBooking)) || !BookRide.equals(String.valueOf(DocumentEnums.CancelBookingFromPassenger)) || !BookRide.equals(String.valueOf(DocumentEnums.CancelBookingFromDriver))) {
                      */     /* Thread th = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        sleep(5000);
                                    } catch (Exception e) {

                                    } finally {

                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {

                                                vehicle_latitude = gps.getLatitude();
                                                vehicle_longitude = gps.getLongitude();

                                                new UpdateVehicleLocation(DriverBooking.this).execute(Double.toString(vehicle_latitude), Double.toString(vehicle_longitude), DriverBooking.VehicleId, Signin.user_id, DriverBooking.BookingDetailId);
                                            }
                                        });
                                    }
                                }
                            };
                            th.start();
*/
                      /*      gps.getLocation();
                            vehicle_latitude = gps.getLatitude();
                            vehicle_longitude = gps.getLongitude();
                              new UpdateVehicleLocation(DriverBooking.this).execute(Double.toString(vehicle_latitude), Double.toString(vehicle_longitude), DriverBooking.VehicleId, Signin.user_id, DriverBooking.BookingDetailId).wait(5000);

                            //mhandler.postDelayed(   new UpdateVehicleLocation(DriverBooking.this).execute(Double.toString(vehicle_latitude), Double.toString(vehicle_longitude), DriverBooking.VehicleId, Signin.user_id, DriverBooking.BookingDetailId), 1000*3);
                        }
                    }else{
                        gps.showSettingsAlert();
                    }

*/



                } catch (Exception e) {
                    //showresponse(e.getMessage());
                }
            }
        });
        Button btnreject = (Button) findViewById(R.id.btnreject);
        btnreject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                    finish();
                } catch (Exception e) {

                }
            }
        });
        onNewIntent(getIntent());
        FirebaseMessaging.getInstance().
                subscribeToTopic("ServiceNow");
    }

    public void bookinconfirmed() {
        Intent n = new Intent(DriverBooking.this, DriverRoute.class);
        startActivity(n);
        finish();
    }

    public void laterbookinconfirmed() {
        Intent n = new Intent(DriverBooking.this, Driver_MyTrip.class);
        startActivity(n);
        finish();
    }

    public void updatelocationtoserver() {
        // if(!DocumentEnums.current_latitude.equals(null)&&!DocumentEnums.current_longitude.equals(null)) {
        //      new UpdateVehicleLocation(DriverBooking.this).execute(DocumentEnums.current_latitude + "", DocumentEnums.current_longitude + "", DriverBooking.VehicleId, Signin.user_id, DriverBooking.BookingDetailId);
        //    }
                  /*  Thread th=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2000);
                }catch (Exception e){

                }finally {
                    if (gps.canGetLocation()) {
                        gps.getLocation();

                        vehicle_latitude = gps.getLatitude();
                        vehicle_longitude = gps.getLongitude();

                        new UpdateVehicleLocation(DriverBooking.this).execute(vehicle_latitude+"", vehicle_longitude+"", DriverBooking.VehicleId, Signin.user_id, DriverBooking.BookingDetailId);
                    }else{
                        gps.showSettingsAlert();
                    }  }
            }
        };
        th.start();
*/
    }

    int i = 10;
    final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            int rem_sec = bundle.getInt("rem_sec");
            tvrem_sec.setText("" + rem_sec);
        }
    };
    static String strSDesc = "ShortDesc";
    static String strIncidentNo = "IncidentNo";
    static String strDesc = "IncidentNo";

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            setContentView(R.layout.activity_driver_booking);
            String IncidentTextView;
            String SDescTextView;
            String DescTextView;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                strSDesc = extras.getString("ShortDesc",
                        "ShortDesc");

                strIncidentNo = extras.getString("IncidentNo",
                        "IncidentNo");
                strDesc = extras.getString("Description",
                        "IncidentNo");
            }
            IncidentTextView = strIncidentNo.toString();
            SDescTextView = strSDesc.toString();
            DescTextView = strDesc.toString();
        }
    }

    public void browser1(View view) {
        Intent browserIntent = new Intent
                (Intent.ACTION_VIEW, Uri.parse
                        ("https://somebrowser?uri=incident.do?sysparm_query=number="
                                + strIncidentNo));
        startActivity(browserIntent);
    }

    // GPSTracker gps;

    public void btnaccept(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            new DriverBookingConfirmed(DriverBooking.this).execute(BookingDetailId, Signin.user_id, VehicleId);

        }
    }


    public void showresponse(String message) {
        Toast.makeText(getApplicationContext(),
                "" + message, Toast.LENGTH_LONG)
                .show();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
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












    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            vehicle_latitude = location.getLatitude();
            vehicle_longitude = location.getLongitude();
            // new UpdateVehicleLocation(DriverBooking.this).execute(Double.toString(vehicle_latitude), Double.toString(vehicle_longitude), DriverBooking.VehicleId, Signin.user_id, DriverBooking.BookingDetailId);

            mLocationManager.removeUpdates(this);
        }
    }

    // Required functions
    public void onProviderDisabled(String arg0) {}
    public void onProviderEnabled(String arg0) {}
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}





}