package com.example.mangotech2.a123ngo;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Modules.DirectionFinderListener;
import com.example.mangotech2.a123ngo.Modules.Route;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingCancelFromPassenger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class TripConfirmed extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {
public static LinearLayout buttonlist;

    Button btncanceltrip;
    public static String booking_date,booking_time,Message, BookingDetailId, BookingStatusId, driver_id,
            vehicle_id, first_name, phone_num, vehicle_name, registration_number,pickup_latitude,pickup_longitude,dropoff_latitude,dropoff_longitude,
            vehicle_color, vehicle_model, current_location_latitude, current_location_longitude, old_location_latitude, old_location_longitude;
    public static Queue<LatLng> vehiclerunninglatlng;
    LatLng vehicleloc;
    private android.widget.TextView textView11;
    private android.widget.TextView textView10;
    private android.widget.ImageView imageView2;
    private android.widget.TextView txtconfirmpickuplocation;
    private android.widget.TextView textView12;
    private android.widget.TextView txtconfirmdrivername;
    private android.widget.RatingBar ratingBar;
    private android.widget.TextView txtconfirmvehiclecolor;
    private android.widget.TextView txtconfirmvehiclename;
    private android.widget.TextView txtconfirmvehiclereg;
    private Button btncontactdriver;
    Marker drivermarker;
    public static TripConfirmed contexttripconfirmed;
    public android.widget.LinearLayout tvtripconfirmedridestatus;
    public static boolean fromtrips=false;
    public static String IsBookLater="";
    public static TextView txtrideinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_confirmed);
        this.txtrideinfo = (TextView) findViewById(R.id.txtrideinfo);
        DocumentEnums.garbagecollect();

        buttonlist=(LinearLayout)findViewById(R.id.buttonlist);
        this.tvtripconfirmedridestatus = (LinearLayout) findViewById(R.id.tvtripconfirmedridestatus);
        tvtripconfirmedridestatus.setVisibility(View.GONE);
        contexttripconfirmed = TripConfirmed.this;
        this.btncontactdriver = (Button) findViewById(R.id.btncontactdriver);
        this.txtconfirmvehiclereg = (TextView) findViewById(R.id.txtconfirmvehiclereg);
        this.txtconfirmvehiclename = (TextView) findViewById(R.id.txtconfirmvehiclename);
        this.txtconfirmvehiclecolor = (TextView) findViewById(R.id.txtconfirmvehiclecolor);
        this.ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        this.txtconfirmdrivername = (TextView) findViewById(R.id.txtconfirmdrivername);
        this.textView12 = (TextView) findViewById(R.id.textView12);
        this.txtconfirmpickuplocation = (TextView) findViewById(R.id.txtconfirmpickuplocation);
        this.imageView2 = (ImageView) findViewById(R.id.imageView2);
        this.textView10 = (TextView) findViewById(R.id.textView10);
        this.textView11 = (TextView) findViewById(R.id.textView11);
        old_location_latitude=current_location_latitude;
        old_location_longitude=current_location_longitude;
        vehiclerunninglatlng=new LinkedList();

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cartop);
        Bitmap b = bitmapdraw.getBitmap();
        //  Bitmap rotated=rotateBitmap(b,90);
        smallMarker = Bitmap.createScaledBitmap(b, 60, 60, false);
        //   DriverData();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        polyLineList=new ArrayList<>();
        polylineAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        polylineOptions = new PolylineOptions();
        blackPolylineOptions = new PolylineOptions();
        handler = new Handler();
        index = -1;
        next = 1;



        btncanceltrip = (Button) findViewById(R.id.btncanceltrip);
        btncanceltrip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DocumentEnums.garbagecollect();


                try {
// custom dialog
                    final Dialog dialog = new Dialog(TripConfirmed.this);
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
                                new BookingCancelFromPassenger(TripConfirmed.this).execute(BookingDetailId);
                                Intent n = new Intent(TripConfirmed.this, HomeMain.class);
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
        btncontactdriver = (Button) findViewById(R.id.btncontactdriver);
        btncontactdriver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone_num));
                    if (ActivityCompat.checkSelfPermission(TripConfirmed.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding

                        //  public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults);
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                } catch (Exception e) {

                }
            }
        });
        DriverData();
    }


    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);
        Canvas canvas = new Canvas(rotatedBitmap);
        canvas.drawBitmap(original, 5.0f, 0.0f, null);

        return rotatedBitmap;
    }


    public void DriverData() {
        DocumentEnums.garbagecollect();

        try {
            txtconfirmpickuplocation.setText(HomeMain.TxtPickupLoc);
            txtconfirmdrivername.setText(first_name);
            txtconfirmvehiclecolor.setText(vehicle_color);
            txtconfirmvehiclename.setText(vehicle_name);
            txtconfirmvehiclereg.setText(registration_number);
        } catch (Exception e) {
            //showresponse(e.getMessage());
        }
    }

    public void showresponse(String message) {
        DocumentEnums.garbagecollect();

        Toast.makeText(getApplicationContext(),
                "" + message, Toast.LENGTH_LONG)
                .show();
    }
    public void showshortresponse(String message) {
        DocumentEnums.garbagecollect();

        Toast.makeText(getApplicationContext(),
                "" + message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {

    }

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(7000);
            mLocationRequest.setFastestInterval(7000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } catch (Exception e) {

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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        //     currentdriverlocationtomap();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    GoogleMap mMap;
    double latitude, longitude;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        DocumentEnums.garbagecollect();

        try {
            mMap = googleMap;
            //    currentdriverlocationtomap();



            directionpath();
            vehicleloc = new LatLng(Double.parseDouble(current_location_latitude), Double.parseDouble(current_location_longitude));
         //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vehicleloc, 20));

       //     currentdriverlocationtomap();
        } catch (Exception e) {
            //showresponse(e.getMessage());
        }
    }

    public void currentdriverlocationtomap() {
        DocumentEnums.garbagecollect();

        if (drivermarker != null) {
            drivermarker.remove();
        }
        vehicleloc = new LatLng(Double.parseDouble(current_location_latitude), Double.parseDouble(current_location_longitude));
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cartop);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 40, 40, false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vehicleloc, 40));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(vehicleloc);
        markerOptions.draggable(true);
        markerOptions.title("Vehicle Location");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        drivermarker = mMap.addMarker(markerOptions);

        //  drivermarker=new MarkerOptions().title("Vehicle Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        //     mMap.addMarker(drivermarker.position(vehicleloc)).setDraggable(false);
    }

    public void btncanceltrip(View v) {

        DocumentEnums.garbagecollect();

        Intent n = new Intent(TripConfirmed.this, mapdirection.class);
        startActivity(n);
        finish();
    }

    public void directionpath() {

        DocumentEnums.garbagecollect();

        try {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(current_location_latitude),Double.valueOf(current_location_longitude)), 35));

            Object dataTransfer[] = new Object[2];
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            dataTransfer = new Object[3];
            String url = getDirectionsUrl(pickup_latitude,pickup_longitude);
            GetDirectionsData getDirectionsData = new GetDirectionsData();
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            dataTransfer[2] = new LatLng(Double.valueOf(pickup_latitude), Double.valueOf(pickup_longitude));
            getDirectionsData.execute(dataTransfer);
            mMap.clear();
            BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.cartop);
            Bitmap bs = bitmapdraws.getBitmap();
            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 50, 50, false);
            mMap.addMarker(new MarkerOptions()
                    .title("Vehicle Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarkers)).draggable(false)
                    .position(new LatLng(Double.valueOf(current_location_latitude),Double.valueOf(current_location_longitude))));

            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.final_marker);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 150, false);
            mMap.addMarker(new MarkerOptions()
                    .title("PickUp Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .position(new LatLng(Double.valueOf(pickup_latitude),Double.valueOf(pickup_longitude)))).setDraggable(false);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(Double.valueOf(current_location_latitude), Double.valueOf(current_location_longitude)));
            builder.include(new LatLng(Double.valueOf(pickup_latitude), Double.valueOf(pickup_longitude)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 25));

//            mMap.getMapAsync(TripConfirmed.this);
        } catch (Exception e) {
            Log.d(e.toString(), e.getMessage());
        }

    }

    public void directionpathtodropoff() {

        DocumentEnums.garbagecollect();

        try {

            mMap.clear();
            Object dataTransfer[] = new Object[2];
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            dataTransfer = new Object[3];
            String url = getDirectionsUrl(dropoff_latitude,dropoff_longitude);
            GetDirectionsData getDirectionsData = new GetDirectionsData();
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            dataTransfer[2] = new LatLng(Double.valueOf(dropoff_latitude), Double.valueOf(dropoff_longitude));
            getDirectionsData.execute(dataTransfer);
            BitmapDrawable bitmapdraws = (BitmapDrawable) getResources().getDrawable(R.drawable.cartop);
            Bitmap bs = bitmapdraws.getBitmap();
            Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 50, 50, false);
            mMap.addMarker(new MarkerOptions()
                    .title("Vehicle Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarkers)).draggable(false)
                    .position(new LatLng(Double.valueOf(current_location_latitude),Double.valueOf(current_location_longitude))));

            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.final_searchred);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 150, false);
            mMap.addMarker(new MarkerOptions()
                    .title("Dropoff Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .position(new LatLng(Double.valueOf(dropoff_latitude),Double.valueOf(dropoff_longitude)))).setDraggable(false);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(Double.valueOf(current_location_latitude), Double.valueOf(current_location_longitude)));
            builder.include(new LatLng(Double.valueOf(dropoff_latitude), Double.valueOf(dropoff_longitude)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 25));

        } catch (Exception e) {
            Log.d(e.toString(), e.getMessage());
        }

    }


    private String getDirectionsUrl(String dropofflat,String dropofflong) {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("&origin=" + current_location_latitude + "," + current_location_longitude);
        googleDirectionsUrl.append("&destination=" + dropofflat + "," + dropofflong);
        googleDirectionsUrl.append("&key=AIzaSyCApwhgQ13jXMGkKCM61BL3nT-UDe7lGPs");

        return googleDirectionsUrl.toString();
    }

    int PROXIMITY_RADIUS = 10000;

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        DocumentEnums.garbagecollect();

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=24.941682759056405,67.11447168141603");
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBj-cnmMUY21M0vnIKz0k3tD3bRdyZea-Y");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    final GetDirectionsDataforvehicle getDirectionsDataforvehicle = new GetDirectionsDataforvehicle();

    public void directionpathforvehicle() {
        DocumentEnums.garbagecollect();

        try {
            Object dataTransfer[] = new Object[2];
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            dataTransfer = new Object[3];
            String url = getDirectionsUrlforvehicle();
            //getDirectionsData = new GetDirectionsData();
            dataTransfer[0] = mMap;
            dataTransfer[1] = url;
            dataTransfer[2] = new LatLng(Double.valueOf(current_location_latitude), Double.valueOf(current_location_longitude));
            Object returnval = new GetDirectionsDataforvehicle().execute(dataTransfer);
        //    old_location_latitude=current_location_latitude;
         //   old_location_longitude=current_location_longitude;

         //   isAnimated();
            //  showshortresponse("directionpathforvehicle");


         /*       if(!thread.isAlive()){
                thread.start();
                showshortresponse("threadstarted");
            }
*/
        } catch (Exception e) {

        }
    }

    public void isAnimated() {
        DocumentEnums.garbagecollect();

        polylineAnimator.setRepeatCount(0);
       if(!polylineAnimator.isRunning()&&!valueAnimator.isRunning()){
           animatedcar();
       }else{
           polylineAnimator.end();
           polylineAnimator.cancel();
           mMap.stopAnimation();
       }
    }

    public String getDirectionsUrlforvehicle() {
        DocumentEnums.garbagecollect();

        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("&origin=" + old_location_latitude + "," + old_location_longitude);
        googleDirectionsUrl.append("&destination=" + current_location_latitude + "," + current_location_longitude);
        googleDirectionsUrl.append("&key=AIzaSyCApwhgQ13jXMGkKCM61BL3nT-UDe7lGPs");
        return googleDirectionsUrl.toString();
    }
    static LatLng vehicle;
    public Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
/*            while(true){
                try{
                    sleep(10);
                }catch (Exception e){

                }finally {
                    if(!vehiclerunninglatlng.isEmpty()){

                        vehicle=vehiclerunninglatlng.poll();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                showshortresponse("vehicleonroad "+vehicle.latitude+","+vehicle.longitude);
                            }
                        });
                        currentdriverlocationtomapfromlatlng(vehicle);

                    }else{
                        runOnUiThread(new Runnable() {
                            public void run() {
                                showshortresponse("no vehicle latlng on road ");
                            }
                        });
                    }
                }
            }
*/
        }
    });
    Bitmap smallMarker;
    static MarkerOptions markerOptions;
    public void currentdriverlocationtomapfromlatlng(final LatLng vehiclelocation) {
    /*    runOnUiThread(new Runnable() {
            public void run() {
                if (drivermarker != null) {
                    //    drivermarker.remove();
                    //       showshortresponse("drivermarker.remove()");
                }
            }
        });
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.cartop);
        Bitmap b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, 40, 40, false);
        markerOptions = new MarkerOptions();
        markerOptions.draggable(true);
        markerOptions.title("Vehicle Location");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //CODE
                float  v=valueAnimator.getAnimatedFraction();
                double  lng = v * vehiclelocation.longitude + (1 - v)* Double.valueOf(old_location_longitude);
                double   lat = v * vehiclelocation.latitude + (1 - v)* Double.valueOf(old_location_latitude);
                runOnUiThread(new Runnable() {
                    public void run() {
                        markerOptions.position(vehiclelocation);
                        markerOptions.anchor(0.5f, 0.5f);
                        markerOptions.rotation((float)(bearingBetweenLocations(new LatLng(Double.valueOf(old_location_latitude),Double.valueOf(old_location_longitude)), vehiclelocation)));
                        mMap.moveCamera(CameraUpdateFactory
                                .newCameraPosition
                                        (new CameraPosition.Builder().target(vehiclelocation)
                                                .zoom(15.5f)
                                                .build()));
                        //    drivermarker = mMap.addMarker(markerOptions);
                        showshortresponse("marker added");
                        showshortresponse("currentdriverlocationtomap marker added");

                    }
                });


                old_location_latitude=String.valueOf(vehiclelocation.latitude);
                old_location_longitude=String.valueOf(vehiclelocation.longitude);

            }
        });
        valueAnimator.start();
*/
        //   vehicleloc = new LatLng(Double.parseDouble(current_location_latitude), Double.parseDouble(current_location_longitude));

       /* runOnUiThread(new Runnable() {
            public void run() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(), 18));

                drivermarker = mMap.addMarker(markerOptions);
                showshortresponse("currentdriverlocationtomap marker added");

            }
        });
        */
        //  drivermarker=new MarkerOptions().title("Vehicle Location").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        //     mMap.addMarker(drivermarker.position(vehicleloc)).setDraggable(false);
    }

    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {
        DocumentEnums.garbagecollect();


        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }






























    private List<LatLng> polyLineList,polylinelistlatlng;
    private Marker movingmarker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition;
    private PolylineOptions polylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, greyPolyLine;
    private int index, next;
    ValueAnimator polylineAnimator;
    ValueAnimator valueAnimator;
    public void animatedcar(){
        DocumentEnums.garbagecollect();

        /*
        if(polylineAnimator.isStarted()){
            polylineAnimator.end();
            polylineAnimator.removeAllListeners();
            polyLineList=new ArrayList<>();
            polylinelistlatlng=new ArrayList<>();
            polylineOptions = new PolylineOptions();
            blackPolylineOptions = new PolylineOptions();
            handler=null;
            handler = new Handler();
            index = -1;
            next = 1;
            polylineAnimator.cancel();
            polylineAnimator=null;
            polylineAnimator = ValueAnimator.ofInt(0, 100);


        }if(valueAnimator.isStarted()){
            valueAnimator.end();
            valueAnimator.removeAllListeners();
            valueAnimator.cancel();
            valueAnimator=null;
            valueAnimator = ValueAnimator.ofFloat(0, 1);
        }
        */
        String requestUrl = null;
        try {
            requestUrl=getDirectionsUrlforvehicle();
            //  Log.d(TAG, requestUrl);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    requestUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //            Log.d(TAG, response + "");
                            try {

                                polylinelistlatlng=new ArrayList<>();
                                JSONArray jsonArray = response.getJSONArray("routes");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject route = jsonArray.getJSONObject(i);
                                    JSONObject poly = route.getJSONObject("overview_polyline");
                                    String polyline = poly.getString("points");
                                    polylinelistlatlng = decodePoly(polyline);
                                  //  polyLineList = decodePoly(polyline);
                                    //   Log.d(TAG, polyLineList + "");
                                }
                              //
                                if(!polylinelistlatlng.isEmpty()){
                                polyLineList.addAll(polylinelistlatlng);
                                }
                                polylineAnimator.setRepeatCount(0);
                                if(!polylineAnimator.isRunning()&&!valueAnimator.isRunning()){
                                    makecarrunning();
                                }else{
                                    polylineAnimator.end();
                                    polylineAnimator.cancel();
                                    mMap.stopAnimation();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //      TripConfirmed.old_location_latitude=TripConfirmed.current_location_latitude;
                            //        TripConfirmed.old_location_longitude=TripConfirmed.current_location_longitude;
                        }

                        public  void makecarrunning() {
                            //Adjusting bounds
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (LatLng latLng : polyLineList) {
                                builder.include(latLng);
                            }
                            LatLngBounds bounds = builder.build();
                            CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                            mMap.animateCamera(mCameraUpdate);

                            polylineOptions.color(Color.GRAY);
                            polylineOptions.width(5);
                            polylineOptions.startCap(new SquareCap());
                            polylineOptions.endCap(new SquareCap());
                            polylineOptions.jointType(ROUND);
                            polylineOptions.addAll(polyLineList);
                            greyPolyLine = mMap.addPolyline(polylineOptions);

                            blackPolylineOptions.width(5);
                            blackPolylineOptions.color(Color.BLACK);
                            blackPolylineOptions.startCap(new SquareCap());
                            blackPolylineOptions.endCap(new SquareCap());
                            blackPolylineOptions.jointType(ROUND);
                            blackPolyline = mMap.addPolyline(blackPolylineOptions);

                            //           mMap.addMarker(new MarkerOptions()
                            //                 .position(polyLineList.get(polyLineList.size() - 1)));


                            polylineAnimator.setDuration(2000);
                            polylineAnimator.setRepeatCount(0);
                            polylineAnimator.setInterpolator(new LinearInterpolator());
                            polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    List<LatLng> points = greyPolyLine.getPoints();
                                    int percentValue = (int) valueAnimator.getAnimatedValue();
                                    int size = points.size();
                                    int newPoints = (int) (size * (percentValue / 100.0f));
                                    List<LatLng> p = points.subList(0, newPoints);
                                    blackPolyline.setPoints(p);
                                }

                            });
                            polylineAnimator.start();

                            if(movingmarker==null) {
                                movingmarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(old_location_latitude), Double.valueOf(old_location_longitude)))
                                        .flat(true)
                                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                            }
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (index < polyLineList.size() - 1) {
                                        index++;
                                        next = index + 1;
                                    }
                                    if (index < polyLineList.size() - 1) {
                                        startPosition = polyLineList.get(index);
                                        endPosition = polyLineList.get(next);
                                    }
                                    valueAnimator.setDuration(3000);
                                    valueAnimator.setRepeatCount(0);
                                    valueAnimator.setInterpolator(new LinearInterpolator());
                                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                            v = valueAnimator.getAnimatedFraction();
                                            lng = v * endPosition.longitude + (1 - v)
                                                    * startPosition.longitude;
                                            lat = v * endPosition.latitude + (1 - v)
                                                    * startPosition.latitude;
                                            LatLng newPos = new LatLng(lat, lng);
                                            old_location_latitude=String.valueOf(lat);
                                            old_location_longitude=String.valueOf(lng);
                                            movingmarker.setPosition(newPos);
                                            movingmarker.setAnchor(0.5f, 0.5f);
                                            movingmarker.setRotation(getBearing(startPosition, newPos));
                                            mMap.moveCamera(CameraUpdateFactory
                                                    .newCameraPosition
                                                            (new CameraPosition.Builder()
                                                                    .target(newPos)
                                                                    .zoom(15.5f)
                                                                    .build()));
                                        }
                                    });
                                    try {
                                        valueAnimator.start();
                                        handler.postDelayed(this, 3000);
                                    }catch (Exception e){
                                    }
                                }
                            }, 3000);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //     Log.d(TAG, error + "");
                }
            });

            RequestQueue requestQueue =Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);

            //    old_location_latitude=String.valueOf(current_location_latitude);
            //     old_location_longitude=String.valueOf(current_location_latitude);
        }catch (Exception e){

        }

    }



    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }




    private float getBearing(LatLng begin, LatLng end) {
        DocumentEnums.garbagecollect();

        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }



    @Override
    public void onBackPressed() {
        DocumentEnums.garbagecollect();

        if(fromtrips){
            super.onBackPressed();
        }

    }

}