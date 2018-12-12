package com.example.mangotech2.a123ngo.NotificationClass;

/**
 * Created by Sameer on 11/24/2017.
 */


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.example.mangotech2.a123ngo.DriverBooking;
import com.example.mangotech2.a123ngo.DriverHome;
import com.example.mangotech2.a123ngo.DriverMetering;
import com.example.mangotech2.a123ngo.Driver_MyTrip;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Home;
import com.example.mangotech2.a123ngo.HomeMain;
import com.example.mangotech2.a123ngo.MainActivity;
import com.example.mangotech2.a123ngo.R;
import com.example.mangotech2.a123ngo.Settings;
import com.example.mangotech2.a123ngo.SigniUpSignIn;
import com.example.mangotech2.a123ngo.Splash;
import com.example.mangotech2.a123ngo.TripConfirmed;
import com.example.mangotech2.a123ngo.UsersFeedback;
import com.example.mangotech2.a123ngo.mapdirection;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;

import static com.google.android.gms.internal.zzagz.runOnUiThread;

public class FirebaseMessagingService
        extends  com.google.firebase.messaging.FirebaseMessagingService  {
    private static final String TAG="FirebaseMessagingServic";
    Ringtone r;
    public FirebaseMessagingService() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
           r= RingtoneManager.getRingtone(getApplicationContext(), notification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String strTitle=remoteMessage.getNotification().getTitle();
        String message=remoteMessage.getNotification().getBody();
      /*  Intent i = new Intent();
        i.setClass(this, DriverBooking.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       getBaseContext().startActivity(i);
       */
        // sendNotification(strTitle,message);
    }
    @Override
    public void handleIntent(Intent intent) {
DocumentEnums.garbagecollect();
        HomeMain.initializemytripsobject();
        DriverHome.Intitializedrivermytrpobject();
     //   super.handleIntent(intent);
        // String Token=""+ FirebaseInstanceId.getInstance().getToken();
        try {
            String BookRide = intent.getStringExtra("BookRide");
            if (Integer.parseInt(BookRide) == DocumentEnums.ConfirmBookingFromPassenger) {

               try {
                   r.play();
               }catch (Exception e){

               }
                   DriverBooking.BookRide = BookRide;
                String BookingDetailId = intent.getStringExtra("BookingDetailId");
                String VehicleId = intent.getStringExtra("VehicleId");
                DriverBooking.PickupLatitude = intent.getStringExtra("PickupLatitude");
                DriverBooking.PickupLongitude = intent.getStringExtra("PickupLongitude");
                DriverBooking.DropoffLatitude = intent.getStringExtra("DropoffLatitude");
                DriverBooking.DropoffLongitude = intent.getStringExtra("DropoffLongitude");
                DriverBooking.EstimatedFares = intent.getStringExtra("EstimatedFares");
                DriverBooking.DistanceInKm = intent.getStringExtra("DistanceInKm");
                DriverBooking.PickUpLocationName = intent.getStringExtra("PickUpLocationName");
                DriverBooking.DropoffLocationName = intent.getStringExtra("DropoffLocationName");
                DriverBooking.SurgeFare = intent.getStringExtra("SurgeFare");
                DriverBooking.BookRide = BookRide;
                DriverBooking.IsBookLater = intent.getStringExtra("IsBookLater");
                DriverBooking.BookingDate = intent.getStringExtra("BookingDate");
                DriverBooking.BookingTime = intent.getStringExtra("BookingTime");
                DriverBooking.BookingDetailId = BookingDetailId;
                DriverBooking.VehicleId = VehicleId;
                if (!BookingDetailId.isEmpty()) {
                    Intent i = new Intent();
                    i.setClass(this, DriverBooking.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                }
            } else if (Integer.parseInt(BookRide) == DocumentEnums.ConfirmBookingFromDriver) {

                try {
                    r.play();
                }catch (Exception e){

                }
                //   DriverBooking.BookRide=BookRide;
                TripConfirmed.BookingDetailId = intent.getStringExtra("BookingDetailId");
                TripConfirmed.driver_id = intent.getStringExtra("driver_id");
                TripConfirmed.vehicle_id = intent.getStringExtra("vehicle_id");
                TripConfirmed.first_name = intent.getStringExtra("first_name");
                TripConfirmed.phone_num = intent.getStringExtra("phone_num");
                TripConfirmed.vehicle_name = intent.getStringExtra("vehicle_name");
                TripConfirmed.registration_number = intent.getStringExtra("registration_number");
                TripConfirmed.vehicle_color = intent.getStringExtra("vehicle_color");
                TripConfirmed.vehicle_model = intent.getStringExtra("vehicle_model");
                TripConfirmed.current_location_latitude = intent.getStringExtra("current_location_latitude");
                TripConfirmed.current_location_longitude = intent.getStringExtra("current_location_longitude");
                TripConfirmed.pickup_latitude = intent.getStringExtra("pickup_latitude");
                TripConfirmed.pickup_longitude = intent.getStringExtra("pickup_longitude");
                TripConfirmed.IsBookLater = intent.getStringExtra("IsBookLater");
                if(TripConfirmed.IsBookLater.equals("1")){
                    TripConfirmed.dropoff_latitude = intent.getStringExtra("dropoff_latitude");
                    TripConfirmed.dropoff_longitude = intent.getStringExtra("dropoff_longitude");
                    TripConfirmed.booking_date = intent.getStringExtra("booking_date");
                    TripConfirmed.booking_time = intent.getStringExtra("booking_time");

                    HomeMain.initializemytripsobject();

                    HomeMain.contexthomemain.sbooking_date.add(TripConfirmed.booking_date);
                    HomeMain.contexthomemain.sbooking_time.add(TripConfirmed.booking_time);
                    HomeMain.contexthomemain.sbooking_status_id.add(TripConfirmed.BookingDetailId);
                    HomeMain.contexthomemain.spickup_latitude.add(TripConfirmed.pickup_latitude);
                    HomeMain.contexthomemain.spickup_longitude.add(TripConfirmed.pickup_longitude);
                    HomeMain.contexthomemain.sdropoff_latitude.add( TripConfirmed.dropoff_latitude);
                    HomeMain.contexthomemain.sdropoff_longitude.add(TripConfirmed.dropoff_longitude);
                    HomeMain.contexthomemain.sfirst_name.add(TripConfirmed.first_name);
                    HomeMain.contexthomemain.sphone_num.add(TripConfirmed.phone_num);
                    TripConfirmed.contexttripconfirmed.showresponse("Your Ride has been scheduled succesfully");

HomeMain.contexthomemain.gotomytrip();
                }else{



                if (!TripConfirmed.BookingDetailId.isEmpty()) {
                    Intent i = new Intent();
                    i.setClass(this, TripConfirmed.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);

                }
                }
            } else if (Integer.parseInt(BookRide) == DocumentEnums.UpdateDriverVehicleLocation) {
                //  DriverBooking.BookRide=BookRide;
        //        TripConfirmed.BookingDetailId = intent.getStringExtra("BookingDetailId");
                TripConfirmed.current_location_latitude = intent.getStringExtra("Latitude");
                TripConfirmed.current_location_longitude = intent.getStringExtra("Longitude");
                TripConfirmed.pickup_latitude=intent.getStringExtra("pickup_latitude");
                TripConfirmed.pickup_longitude=intent.getStringExtra("pickup_longitude");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(TripConfirmed.contexttripconfirmed!=null){
                               TripConfirmed.contexttripconfirmed.animatedcar();
                      }
                    }
                });

            } else if (Integer.parseInt(BookRide) == DocumentEnums.CancelBookingFromPassenger) {

                try {
                    r.play();
                }catch (Exception e){

                }
                DriverBooking.BookRide = BookRide;
                DriverBooking.Message = intent.getStringExtra("Message");

                Intent i = new Intent();
                i.setClass(this, DriverHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(i);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(HomeMain.contexthomemain!=null){
                            HomeMain.contexthomemain.showresponse("Booking Cancelled From Passenger");
                        }
                    }
                });

            } else if (Integer.parseInt(BookRide) == DocumentEnums.CancelBookingFromDriver) {

                try {
                    r.play();
                }catch (Exception e){

                }
                // DriverBooking.BookRide=BookRide;
                TripConfirmed.Message = intent.getStringExtra("Message");
       //         HomeMain.contexthomemain.showresponse("Booking Cancelled From Driver");
                Intent i = new Intent();
                i.setClass(this, HomeMain.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(i);

            }else if (Integer.parseInt(BookRide) == DocumentEnums.CompleteBooking) {

                try {

                    r.play();
                }catch (Exception e){

                }
                // DriverBooking.BookRide=BookRide;
                TripConfirmed.Message = intent.getStringExtra("Message");
                UsersFeedback.fare=intent.getStringExtra("TotalPrice");
             //   TripConfirmed
           //     HomeMain.contexthomemain.showresponse(TripConfirmed.Message+" Total Price="+intent.getStringExtra("TotalPrice"));
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(TripConfirmed.contexttripconfirmed!=null){
                            TripConfirmed.contexttripconfirmed.showresponse("Trip Completed");
                        }
                    }
                });
                if(TripConfirmed.contexttripconfirmed!=null){
                    TripConfirmed.contexttripconfirmed.finish();
                }

                Intent i = new Intent();
                i.setClass(this, UsersFeedback.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(i);

            }else if (Integer.parseInt(BookRide) == DocumentEnums.RideStart) {

                try {
                    r.play();
                }catch (Exception e){

                }
                // DriverBooking.BookRide=BookRide;
                TripConfirmed.Message = intent.getStringExtra("Message");
                TripConfirmed.dropoff_latitude = intent.getStringExtra("DropOffLatitude");
                TripConfirmed.dropoff_longitude = intent.getStringExtra("DropOffLongitude");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(TripConfirmed.contexttripconfirmed!=null) {
                            TripConfirmed.txtrideinfo.setText("Ride has been started");
                            TripConfirmed.contexttripconfirmed.showresponse("" + TripConfirmed.Message);
                            TripConfirmed.contexttripconfirmed.directionpathtodropoff();
                            TripConfirmed.contexttripconfirmed.tvtripconfirmedridestatus.setVisibility(View.VISIBLE);
                            TripConfirmed.contexttripconfirmed.buttonlist.setVisibility(View.GONE);

                            //   TripConfirmed.contexttripconfirmed.showshortresponse("directionpathtodropoff function call");
                        }
                    }
                });
        //        HomeMain.contexthomemain.showresponse(TripConfirmed.Message+"");
            }else if (Integer.parseInt(BookRide) == DocumentEnums.BookingLater) {
                Intent i = new Intent();
                i.setClass(this, Driver_MyTrip.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(i);
            }
            else if (Integer.parseInt(BookRide) == DocumentEnums.DriverReached) {

                String Message = intent.getStringExtra("Message");
                sendNotification("123NGO",Message);
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(TripConfirmed.contexttripconfirmed!=null) {
                            TripConfirmed.txtrideinfo.setText("Driver Reached");
                TripConfirmed.contexttripconfirmed.tvtripconfirmedridestatus.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            else{
                Intent i = new Intent();
                i.setClass(this, Settings.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(i);
            }
        }catch (Exception e){
            String  err=e.getMessage();
        }

    }
    @Override
    public void onDeletedMessages() {
        try {
            r.stop();
        }catch (Exception e){

        }
    }


    private  void sendNotification(String title,String messageBody) {

        Uri defaultSoundUri= RingtoneManager.getDefaultUri
                (RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationbuilder=
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_logo)
                        .setContentTitle("123NGO")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                         .setLargeIcon(BitmapFactory.decodeResource
                                (getResources(), R.drawable.app_logo));

        NotificationManager notificationManager=(NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationbuilder.build());

    }
}