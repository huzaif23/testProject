package com.example.mangotech2.a123ngo;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Modules.DirectionFinderListener;
import com.example.mangotech2.a123ngo.Modules.Route;
import com.example.mangotech2.a123ngo.Parsing_JSON.EstimatedFares;
import com.example.mangotech2.a123ngo.Parsing_JSON.PassengerBookingConfirm;
import com.example.mangotech2.a123ngo.Parsing_JSON.ValidatePromoCode;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class mapdirection extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener
        ,GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener
{

public static String promo;
    public static List<Integer> farevalue;
    public static LatLng Dropoffloc;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    public static String vehicle_type_id="1";
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    private TextView etOrigin;
    int Place_Picker_Request=1;
    double end_latitude=24.88251662570783, end_longitude=67.03042298555374;
    public static String Dropoffaddress;
    public static Button RideNow,RideLater;
    /*
        protected void onActivityResult(int requestCode,int resultCode,Intent data){
            if(requestCode==Place_Picker_Request){
                if(resultCode==RESULT_OK){
                    Place place = PlacePicker.getPlace(data,this);
                    String Address=String.format("place: %s",place.getAddress());
                    Dropoffaddress=place.getAddress().toString();
                    // etOrigin.setText(Address);
                    etOrigin.setText(place.getAddress());
                    Dropoffloc=place.getLatLng();
                    Log.d(Address,Address);
                    directionpath();

                }
            }
        }
      */public static   TextView tvdistance,tvduraton,etPickup,tvfareestimate;
    LinearLayout panelfare,showfare,btnbike,btnauto,btngoplus,btngox,btnbusiness,btntaxi,btnbus,btnpromo;
    LinearLayout.LayoutParams lp;


    public static  String[] fareval;
    public static  String promoStatus="";
    public ImageView btnback;
    public  static  boolean isvehicleselected=false;
    public  static String distanceridelater,farelater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapdirection);
        DocumentEnums.garbagecollect();
        promo="0";
btnback=(ImageView)findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fareval=new String[10];
        etOrigin = (TextView) findViewById(R.id.etOrigin);
        tvfareestimate = (TextView) findViewById(R.id.tvfareestimate);
        tvfareestimate.setText("Please Wait");
        panelfare = (LinearLayout) findViewById(R.id.panelfare);
        RideNow = (Button) findViewById(R.id.btnRideNow);
      RideLater = (Button) findViewById(R.id.btnRideLater);
        RideLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               distanceridelater= strdistance;
                farelater=fareval[Integer.parseInt(vehicle_type_id)];
                Intent n=new Intent(mapdirection.this,RideLater.class);
                startActivity(n);
            }
        });
        RideNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {

                    if(promo==""||promo.isEmpty()){
                        promo="0";
                    }

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                    String currDate = "" + mdformat.format(calendar.getTime());


                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
                    Date currentLocalTime = cal.getTime();
                    currentLocalTime.setHours(currentLocalTime.getHours());
                    DateFormat date = new SimpleDateFormat("HH:mm");
// you can get seconds by adding  "...:ss" to it
                    date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
                    String currtime = date.format(currentLocalTime);
                    try {
                        String pickuplat = String.valueOf(HomeMain.Pickuploc.latitude);
                        String pickuplong = String.valueOf(HomeMain.Pickuploc.longitude);
                        String dropoofflat = String.valueOf(Dropoffloc.latitude);
                        String dropofflong = String.valueOf(Dropoffloc.longitude);

                        if(Dropoffaddress!=null&&!Dropoffaddress.isEmpty()&&Dropoffaddress!="") {
                            if (isvehicleselected&&mapdirection.fareval[Integer.parseInt(mapdirection.vehicle_type_id)]!=null) {

                                new PassengerBookingConfirm(mapdirection.this).execute(Signin.user_id, "1", currDate, Signin.user_id, currDate, currtime, vehicle_type_id, "", "", "", "", pickuplat, pickuplong, dropoofflat, dropofflong, fareval[Integer.parseInt(vehicle_type_id)], strdistance, HomeMain.pickupaddress, Dropoffaddress, "1", "1", "1", "1",promo,"0");
                            } else {
                                showresponse("Please Select Vehicle");
                            }
                        } else{
                            showresponse("Please Select Drop Off Location");
                        }

                    }catch(Exception e){
                        //   showresponse("Please select Dropoff location");
                    }
                }
            }
        });
        showfare = (LinearLayout) findViewById(R.id.showfare);
        btnbike = (LinearLayout) findViewById(R.id.btnbike);
        btnauto = (LinearLayout) findViewById(R.id.btnauto);
        btngoplus = (LinearLayout) findViewById(R.id.btngoplus);
        btngox = (LinearLayout) findViewById(R.id.btngox);
        btnbusiness = (LinearLayout) findViewById(R.id.btnbusiness);
        btntaxi = (LinearLayout) findViewById(R.id.btntaxi);
        btnbus = (LinearLayout) findViewById(R.id.btnbus);
        btnpromo = (LinearLayout) findViewById(R.id.btnpromo);
        vehicle_type_id="0";

        btnbike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(etOrigin.getText()==null ||etOrigin.getText()==""||etOrigin.getText().toString().isEmpty()) {
                    showresponse("Please Select Drop Off Location");
                }else{

                    try {
                        vehicle_type_id="0";


                        if(fareval[0]==null){
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                            String currDate = "" + mdformat.format(calendar.getTime());


                            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude+"&destinations="+Dropoffloc.latitude+","+Dropoffloc.longitude+"&mode=driving&language=en-EN&sensor=false");

                            if(Dropoffloc.latitude!=0 &&Dropoffloc.longitude!=0)
                            {  if(strtime=="0") {
                                getdistancetime();
                            }
                                new EstimatedFares(mapdirection.this).execute(Signin.user_id,strtime,strdistance,"","","","",""+HomeMain.Pickuploc.latitude,""+HomeMain.Pickuploc.longitude,""+Dropoffloc.latitude,""+Dropoffloc.longitude,Signin.user_id,currDate,"1","1","1","1");
                                isvehicleselected=true;
                            }   else{
                            }

                        }else{
                        }

                        btnbike.setBackground(getResources().getDrawable(R.drawable.bikeclicked));
                        btnauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                        btngoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                        btngox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                        btnbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                        btntaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                        btnbus.setBackground(getResources().getDrawable(R.drawable.final_bus));

                        isvehicleselected=true;
                        showfare.setVisibility(View.VISIBLE);
                        lp.setMargins(0, ((int) getResources().getDimension(R.dimen.showfaremargin)),0,0);
                        tvfareestimate.setText(fareval[0]);

                    }catch (Exception e){

                    }}

            }
        });
        btnauto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(etOrigin.getText()==null ||etOrigin.getText()==""||etOrigin.getText().toString().isEmpty()) {
                    showresponse("Please Select Drop Off Location");
                }else{
                    try {
                        vehicle_type_id="1";

                        if(fareval[1]==null){
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                            String currDate = "" + mdformat.format(calendar.getTime());
                            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude+"&destinations="+Dropoffloc.latitude+","+Dropoffloc.longitude+"&mode=driving&language=en-EN&sensor=false");
                            if(Dropoffloc.latitude!=0 &&Dropoffloc.longitude!=0)
                            {  if(strtime=="0") {
                                getdistancetime();
                            }
                                new EstimatedFares(mapdirection.this).execute(Signin.user_id,strtime,strdistance,"","","","",""+HomeMain.Pickuploc.latitude,""+HomeMain.Pickuploc.longitude,""+Dropoffloc.latitude,""+Dropoffloc.longitude,Signin.user_id,currDate,"1","1","1","1");
                            }

                        }   else{

                        }

                        btnbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                        btnauto.setBackground(getResources().getDrawable(R.drawable.autoclicked));
                        btngoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                        btngox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                        btnbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                        btntaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                        btnbus.setBackground(getResources().getDrawable(R.drawable.final_bus));
                        showfare.setVisibility(View.VISIBLE);
                        lp.setMargins(0, ((int) getResources().getDimension(R.dimen.showfaremargin)),0,0);
                        tvfareestimate.setText(fareval[1]);
                        isvehicleselected=true;

                    }catch (Exception e){

                    }
                }
            }
        });
        btngoplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(etOrigin.getText()==null ||etOrigin.getText()==""||etOrigin.getText().toString().isEmpty()) {
                    showresponse("Please Select Drop Off Location");
                }else {
                    try {

                        vehicle_type_id = "2";
                        if (fareval[2] == null) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                            String currDate = "" + mdformat.format(calendar.getTime());

                            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + HomeMain.Pickuploc.latitude + "," + HomeMain.Pickuploc.longitude + "&destinations=" + Dropoffloc.latitude + "," + Dropoffloc.longitude + "&mode=driving&language=en-EN&sensor=false");
                            if (Dropoffloc.latitude != 0 && Dropoffloc.longitude != 0) {
                                if(strtime=="0") {
                                    getdistancetime();
                                }
                                new EstimatedFares(mapdirection.this).execute(Signin.user_id, strtime, strdistance, "", "", "", "", "" + HomeMain.Pickuploc.latitude, "" + HomeMain.Pickuploc.longitude, "" + Dropoffloc.latitude, "" + Dropoffloc.longitude, Signin.user_id, currDate, "1", "1", "1", "1");
                            }

                        } else {
                        }

                        btnbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                        btnauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                        btngoplus.setBackground(getResources().getDrawable(R.drawable.gocarclicked));
                        btngox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                        btnbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                        btntaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                        btnbus.setBackground(getResources().getDrawable(R.drawable.final_bus));
                        isvehicleselected = true;
                        showfare.setVisibility(View.VISIBLE);
                        lp.setMargins(0, ((int) getResources().getDimension(R.dimen.showfaremargin)), 0, 0);
                        tvfareestimate.setText(fareval[2]);


                    } catch (Exception e) {

                    }
                }
            }
        });
        btngox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(etOrigin.getText()==null ||etOrigin.getText()==""||etOrigin.getText().toString().isEmpty()) {
                    showresponse("Please Select Drop Off Location");
                }else{
                    try {

                        vehicle_type_id="3";

                        if(fareval[0]==null){
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                            String currDate = "" + mdformat.format(calendar.getTime());

                            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude+"&destinations="+Dropoffloc.latitude+","+Dropoffloc.longitude+"&mode=driving&language=en-EN&sensor=false");

                            if(Dropoffloc.latitude!=0 &&Dropoffloc.longitude!=0)
                            {
                                if(strtime=="0") {
                                    getdistancetime();
                                }
                                new EstimatedFares(mapdirection.this).execute(Signin.user_id,strtime,strdistance,"","","","",""+HomeMain.Pickuploc.latitude,""+HomeMain.Pickuploc.longitude,""+Dropoffloc.latitude,""+Dropoffloc.longitude,Signin.user_id,currDate,"1","1","1","1");                   }

                        }   else{
                        }
                        btnbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                        btnauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                        btngoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                        btngox.setBackground(getResources().getDrawable(R.drawable.goplusclicked));
                        btnbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                        btntaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                        btnbus.setBackground(getResources().getDrawable(R.drawable.final_bus));
                        isvehicleselected=true;
                        showfare.setVisibility(View.VISIBLE);
                        lp.setMargins(0, ((int) getResources().getDimension(R.dimen.showfaremargin)),0,0);
                        tvfareestimate.setText(fareval[3]);


                    }catch (Exception e){

                    }
                }
            }
        });
        btnbusiness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(etOrigin.getText()==null ||etOrigin.getText()==""||etOrigin.getText().toString().isEmpty()) {
                    showresponse("Please Select Drop Off Location");
                }else{
                    try {

                        vehicle_type_id="4";
                        if(fareval[4]==null){
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                            String currDate = "" + mdformat.format(calendar.getTime());



                            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude+"&destinations="+Dropoffloc.latitude+","+Dropoffloc.longitude+"&mode=driving&language=en-EN&sensor=false");

                            if(Dropoffloc.latitude!=0 &&Dropoffloc.longitude!=0)
                            {
                                if(strtime=="0") {
                                    getdistancetime();
                                }
                                new EstimatedFares(mapdirection.this).execute(Signin.user_id,strtime,strdistance,"","","","",""+HomeMain.Pickuploc.latitude,""+HomeMain.Pickuploc.longitude,""+Dropoffloc.latitude,""+Dropoffloc.longitude,Signin.user_id,currDate,"1","1","1","1");                   }

                        }   else{

                        }
                        btnbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                        btnauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                        btngoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                        btngox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                        btnbusiness.setBackground(getResources().getDrawable(R.drawable.businessclicked));
                        btntaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                        btnbus.setBackground(getResources().getDrawable(R.drawable.final_bus));
                        isvehicleselected=true;
                        showfare.setVisibility(View.VISIBLE);
                        lp.setMargins(0, ((int) getResources().getDimension(R.dimen.showfaremargin)),0,0);
                        tvfareestimate.setText(fareval[4]);

                    }catch (Exception e){

                    }
                }
            }
        });
        btntaxi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(etOrigin.getText()==null ||etOrigin.getText()==""||etOrigin.getText().toString().isEmpty()) {
                    showresponse("Please Select Drop Off Location");
                }else{
                    try {

                        vehicle_type_id="5";
                        if(fareval[5]==null){
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                            String currDate = "" + mdformat.format(calendar.getTime());


                            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude+"&destinations="+Dropoffloc.latitude+","+Dropoffloc.longitude+"&mode=driving&language=en-EN&sensor=false");

                            if(Dropoffloc.latitude!=0 &&Dropoffloc.longitude!=0)
                            {
                                if(strtime=="0") {
                                    getdistancetime();
                                }
                                new EstimatedFares(mapdirection.this).execute(Signin.user_id,strtime,strdistance,"","","","",""+HomeMain.Pickuploc.latitude,""+HomeMain.Pickuploc.longitude,""+Dropoffloc.latitude,""+Dropoffloc.longitude,Signin.user_id,currDate,"1","1","1","1");                   }

                        }   else{

                        }
                        btnbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                        btnauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                        btngoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                        btngox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                        btnbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                        btntaxi.setBackground(getResources().getDrawable(R.drawable.taxiclicked));
                        btnbus.setBackground(getResources().getDrawable(R.drawable.final_bus));
                        isvehicleselected=true;
                        showfare.setVisibility(View.VISIBLE);
                        lp.setMargins(0, ((int) getResources().getDimension(R.dimen.showfaremargin)),0,0);
                        tvfareestimate.setText(fareval[5]);

                    }catch (Exception e){

                    }
                }
            }
        });
        btnbus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(etOrigin.getText()==null ||etOrigin.getText()==""||etOrigin.getText().toString().isEmpty()) {
                    showresponse("Please Select Drop Off Location");
                }else{
                    try {

                        vehicle_type_id="6";
                        if(fareval[6]==null){
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                            String currDate = "" + mdformat.format(calendar.getTime());



                            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude+"&destinations="+Dropoffloc.latitude+","+Dropoffloc.longitude+"&mode=driving&language=en-EN&sensor=false");

                            if(Dropoffloc.latitude!=0 &&Dropoffloc.longitude!=0)
                            {
                                if(strtime=="0") {
                                    getdistancetime();
                                }
                                new EstimatedFares(mapdirection.this).execute(Signin.user_id,strtime,strdistance,"","","","",""+HomeMain.Pickuploc.latitude,""+HomeMain.Pickuploc.longitude,""+Dropoffloc.latitude,""+Dropoffloc.longitude,Signin.user_id,currDate,"1","1","1","1");                   }

                        }   else{
                        }
                        btnbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                        btnauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                        btngoplus.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                        btngox.setBackground(getResources().getDrawable(R.drawable.final_gox));
                        btnbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                        btntaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                        btnbus.setBackground(getResources().getDrawable(R.drawable.busclicked));
                        isvehicleselected=true;
                        showfare.setVisibility(View.VISIBLE);
                        lp.setMargins(0, ((int) getResources().getDimension(R.dimen.showfaremargin)),0,0);
                        tvfareestimate.setText(fareval[6]);
                    }catch (Exception e){

                    }
                }
            }
        });
        btnpromo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
// custom dialog
                    final Dialog dialog = new Dialog(mapdirection.this);
                    dialog.setContentView(R.layout.promo_dialog);
                    dialog.setTitle("Promo");

                    // set the custom dialog components - text, image and button
                    final EditText textpromo = (EditText) dialog.findViewById(R.id.txtpromocode);

                    Button btnokpromo = (Button) dialog.findViewById(R.id.btnokpromo);
                    Button btncancelpromo = (Button) dialog.findViewById(R.id.btncancelpromo);
                    // if button is clicked, close the custom dialog
                    btnokpromo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                if(textpromo.getText().toString()!=null&&textpromo.getText().toString()!=""&&!textpromo.getText().toString().isEmpty()){
                                    new ValidatePromoCode(mapdirection.this).execute(textpromo.getText().toString(),Signin.user_id);

                                }else{
                                    showresponse("Promo Code Required");
                                }
                            }catch (Exception e){
                                //showresponse(e.getMessage().toString());
                            }
                        }
                    });
                    btncancelpromo.setOnClickListener(new View.OnClickListener() {
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
        int margintoppanelfare=-121;
        lp=(LinearLayout.LayoutParams)panelfare.getLayoutParams();
        showfare.setVisibility(View.GONE);
        lp.setMargins(0, ((int) getResources().getDimension(R.dimen.margintop)),0,0);

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        tvdistance =(TextView) findViewById(R.id.tvDistance);
        tvduraton =(TextView) findViewById(R.id.tvDuration);



        etOrigin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
              /*  PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                Intent intent;
                try{
                    Context context = getApplicationContext();
//intent= builder.build();
                    //      startActivityForResult(intent,Place_Picker_Request);
                    startActivityForResult(builder.build(mapdirection.this), Place_Picker_Request);
                }catch (Exception e){

                }*/
                findPlace(view);
            }
        });
        etPickup=(TextView)findViewById(R.id.etPickup);
        etPickup.setText(HomeMain.TxtPickupLoc);
        etPickup.setText(HomeMain.pickupaddress);
    }

    public void  getdistancetime(){
        DocumentEnums.garbagecollect();

        Location startPoint=new Location(HomeMain.pickupaddress);
        startPoint.setLatitude(HomeMain.Pickuploc.latitude);
        startPoint.setLongitude(HomeMain.Pickuploc.longitude);

        Location endPoint=new Location(Dropoffaddress);
        endPoint.setLatitude(Dropoffloc.latitude);
        endPoint.setLongitude(Dropoffloc.longitude);

        distance=startPoint.distanceTo(endPoint);
        tvduraton.setText(String.valueOf(Math.round((distance*1.12345)/10)/100.d)+"min");
        tvdistance.setText(String.valueOf(Math.round(distance/10)/100.0d)+"Km");
        strdistance=String.valueOf(Math.round(distance/10)/100.0d).toString();
        tim=Math.round((distance*1.12345)/10)/100.d;
        dis=Math.round(distance/10)/100.0d;
        strtime=String.valueOf(Math.round((distance*1.12345)/10)/100.d);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DocumentEnums.garbagecollect();

        if (requestCode == Place_Picker_Request) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("Place", "Place: " + place.getName());
                String placenameadd= (String) place.getAddress();

                String places=   placenameadd.replaceAll((String) place.getName()," ");

                etOrigin.setText(place.getName()+""+places);

                Dropoffaddress=place.getName()+""+places;

                Dropoffloc=place.getLatLng();

                end_latitude=place.getLatLng().latitude;
                end_longitude=place.getLatLng().longitude;


                directionpath();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                String currDate = "" + mdformat.format(calendar.getTime());


                new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude+"&destinations="+Dropoffloc.latitude+","+Dropoffloc.longitude+"&mode=driving&language=en-EN&sensor=false");
           if(strtime=="0") {
               getdistancetime();
           }
           new EstimatedFares(mapdirection.this).execute(Signin.user_id,strtime,strdistance,"","","","",""+HomeMain.Pickuploc.latitude,""+HomeMain.Pickuploc.longitude,""+Dropoffloc.latitude,""+Dropoffloc.longitude,Signin.user_id,currDate,"1","1","1","1");
                tvfareestimate.setText(fareval[Integer.parseInt(vehicle_type_id)]);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Status", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    private boolean CheckGooglePlayServices() {
        DocumentEnums.garbagecollect();

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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        DocumentEnums.garbagecollect();

        try {
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
        }catch (Exception e){

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
            dataTransfer[2] = new LatLng(end_latitude, end_longitude);
            Object returnval= new GetDirectionsData().execute(dataTransfer);
            tvdistance.setText(""+getDirectionsData.distance);

            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude+"&destinations="+Dropoffloc.latitude+","+Dropoffloc.longitude+"&mode=driving&language=en-EN&sensor=false");
            mMap.clear();

            BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.drawable.final_searchgreen);
            Bitmap bs=bitmapdraws.getBitmap();
            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 100, 150, false);
            mMap.addMarker(new MarkerOptions()
                    .title("PickUp Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarkers)).draggable(false)
                    .position(HomeMain.Pickuploc));

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.final_searchred);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 150, false);
            mMap.addMarker(new MarkerOptions()
                    .title("DropOff Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .position(Dropoffloc)).setDraggable(true);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(HomeMain.Pickuploc.latitude, HomeMain.Pickuploc.longitude));
            builder.include(new LatLng(mapdirection.Dropoffloc.latitude, Dropoffloc.longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 250));


            Location startPoint=new Location(HomeMain.pickupaddress);
            startPoint.setLatitude(HomeMain.Pickuploc.latitude);
            startPoint.setLongitude(HomeMain.Pickuploc.longitude);

            Location endPoint=new Location(Dropoffaddress);
            endPoint.setLatitude(Dropoffloc.latitude);
            endPoint.setLongitude(Dropoffloc.longitude);

            distance=startPoint.distanceTo(endPoint);
            tvduraton.setText(String.valueOf(Math.round((distance*1.12345)/10)/100.d)+"min");
            tvdistance.setText(String.valueOf(Math.round(distance/10)/100.0d)+"Km");
            strdistance=String.valueOf(Math.round(distance/10)/100.0d).toString();
            tim=Math.round((distance*1.12345)/10)/100.d;
            dis=Math.round(distance/10)/100.0d;
            strtime=String.valueOf(Math.round((distance*1.12345)/10)/100.d);



            final Handler handler = new Handler() {

                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();

                    tvdistance.setText(String.valueOf(bundle.getString("distance")));
                }
            };


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

        }catch (Exception e){
            Log.d(e.toString(),e.getMessage());
        }

    }
    public static  String total_distance;
    public void onClick(View v)
    {
        DocumentEnums.garbagecollect();

        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();


        switch(v.getId()) {
          /*  case R.id.B_search: {
                EditText tf_location = (EditText) findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();
                List<Address> addressList = null;
                MarkerOptions markerOptions = new MarkerOptions();
                Log.d("location = ", location);

                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList != null) {
                        for (int i = 0; i < addressList.size(); i++) {
                            Address myAddress = addressList.get(i);
                            LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                            markerOptions.position(latLng);
                            mMap.addMarker(markerOptions);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                    }

                }
            }
            break;
            case R.id.B_hospital:
                mMap.clear();
                String hospital = "hospital";
                String url = getUrl(latitude, longitude, hospital);

                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(mapdirection.this, "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
                break;

            case R.id.B_restaurant:
                mMap.clear();
                dataTransfer = new Object[2];
                String restaurant = "restaurant";
                url = getUrl(24.9417008, 67.1144485, restaurant);
                getNearbyPlacesData = new GetNearbyPlacesData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(mapdirection.this, "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
                break;
            case R.id.B_school:
                mMap.clear();
                String school = "school";
                dataTransfer = new Object[2];
                url = getUrl(latitude, longitude, school);
                getNearbyPlacesData = new GetNearbyPlacesData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;

                getNearbyPlacesData.execute(dataTransfer);
                Toast.makeText(mapdirection.this, "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
                break;

        */    case R.id.btnRideLater:
                try{

                    dataTransfer = new Object[3];
                    String   url = getDirectionsUrl();
                    final GetDirectionsData getDirectionsData = new GetDirectionsData();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(Dropoffloc.latitude, Dropoffloc.longitude);
                    getDirectionsData.execute(dataTransfer);
                    /*   Thread th=new Thread(){
                        @Override
                        public void run() {
                            try{
                                if(getDirectionsData.distance!=null){
                            //        tvdistance.setText(getDirectionsData.distance);
                                }

                            }catch (Exception e){

                            }finally {

                            }
                        }
                    };
                    th.start();*/
                }catch (Exception e){
                    Log.d(e.toString(),e.getMessage());
                }
                break;
        }
    }

    private String getDirectionsUrl()
    {
        try{
            StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
            googleDirectionsUrl.append("&origin="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude);
            googleDirectionsUrl.append("&destination="+Dropoffloc.latitude+","+Dropoffloc.longitude);
            googleDirectionsUrl.append("&key=AIzaSyCApwhgQ13jXMGkKCM61BL3nT-UDe7lGPs");
            return googleDirectionsUrl.toString();
        }
        catch (Exception e){
            Log.d(e.toString(),e.getMessage());
            return null;
        }


    }

    private String getUrl(double latitude, double longitude, String nearbyPlace)
    {
        DocumentEnums.garbagecollect();

        try {

            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=24.941682759056405,67.11447168141603");
            googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
            googlePlacesUrl.append("&type=" + nearbyPlace);
            googlePlacesUrl.append("&sensor=true");
            googlePlacesUrl.append("&key=" + "AIzaSyBj-cnmMUY21M0vnIKz0k3tD3bRdyZea-Y");
            Log.d("getUrl", googlePlacesUrl.toString());

            return (googlePlacesUrl.toString());
        }catch (Exception e){

            Log.d(e.toString(),e.getMessage());
            return null;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        DocumentEnums.garbagecollect();

        try{
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
    public void onLocationChanged(Location location) {
        DocumentEnums.garbagecollect();

        try {
            Log.d("onLocationChanged", "entered");

            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            end_latitude = latitude;
            end_longitude = longitude;

            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.final_searchgreen);
            Bitmap b=bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 150, false);
            mMap.addMarker(new MarkerOptions()
                    .title("PickUp Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).draggable(false)
                    .position(HomeMain.Pickuploc));

//Dropoffloc=new LatLng(latitude,longitude);
            BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.final_searchred);
            Bitmap bs = bitmapdraws.getBitmap();
            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 100, 150, false);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Dropoffloc = latLng;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.draggable(true);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarkers));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
         /*   try {
                getaddress(latitude, longitude, mCurrLocationMarker);
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
//            directionpath();

            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + HomeMain.Pickuploc.latitude + "," + HomeMain.Pickuploc.longitude + "&destinations=" + Dropoffloc.latitude + "," + Dropoffloc.longitude + "&mode=driving&language=en-EN&sensor=false");

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            //      end_latitude=latitude;
//        end_longitude=longitude;

            //      Dropoffloc=new LatLng(latitude,longitude);
//directionpath();
            //   Toast.makeText(mapdirection.this, "Your Current Location", Toast.LENGTH_LONG).show();


            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                Log.d("onLocationChanged", "Removing Location Updates");
            }
        }catch (Exception e){

        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){

        DocumentEnums.garbagecollect();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        DocumentEnums.garbagecollect();

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        DocumentEnums.garbagecollect();

        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        DocumentEnums.garbagecollect();

        try{
            LatLng dragPosition = arg0.getPosition();
            double dragLat = dragPosition.latitude;
            double dragLong = dragPosition.longitude;
            Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
            Dropoffloc=dragPosition;
            arg0.setTitle("Selected Location");
            //  Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();
            try {
                getaddress(dragLat,dragLong,arg0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            end_latitude=dragLat;
            end_longitude=dragLong;

            Dropoffloc=new LatLng(end_latitude,end_longitude);


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
            String currDate = "" + mdformat.format(calendar.getTime());

            directionpath();

            new GetDistanceData().execute("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+HomeMain.Pickuploc.latitude+","+HomeMain.Pickuploc.longitude+"&destinations="+Dropoffloc.latitude+","+Dropoffloc.longitude+"&mode=driving&language=en-EN&sensor=false");
            if(strtime=="0") {
                getdistancetime();
            }
            new EstimatedFares(mapdirection.this).execute(Signin.user_id,strtime,strdistance,"","","","",""+HomeMain.Pickuploc.latitude,""+HomeMain.Pickuploc.longitude,""+Dropoffloc.latitude,""+Dropoffloc.longitude,Signin.user_id,currDate,"1","1","1","1");
            tvfareestimate.setText(fareval[Integer.parseInt(vehicle_type_id)]);

        }catch (Exception e){

        }
    }
    public void getaddress(double dragLat,double dragLong,Marker arg) throws IOException {

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
        Dropoffaddress=address;
        arg.setTitle(address+"");
        arg.setTag(knownName);
        etOrigin.setText(address+"");

    }
    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }


    public ProgressDialog dialog;
    public void loading(){
        try{

            DocumentEnums.garbagecollect();

            dialog = new ProgressDialog(mapdirection.this);
            dialog.setMessage("Finding Driver");
            dialog.show();
        }catch (Exception e){
        }
    }

    public void loaded(){
        try{
            DocumentEnums.garbagecollect();


            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }catch (Exception e){

        }
    }

    public void bookingconfirmed(){
        DocumentEnums.garbagecollect();


        Thread th=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(30000);
                }catch (Exception e){

                }finally {
                    loaded();
                    try{
                        if(TripConfirmed.driver_id.isEmpty()){
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    showresponse("Sorry All Driver are busy");
                                }
                            });
                        }
                    }catch (Exception e){
                        runOnUiThread(new Runnable() {
                            public void run() {

                                showresponse("Sorry All Driver are busy");
                            }
                        });
                    }

                }
            }
        };
        th.start();
        // Intent n=new Intent(mapdirection.this,TripConfirmed.class);
        // startActivity(n);
    }

    public void showresponse(String message){
        DocumentEnums.garbagecollect();
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }
}
