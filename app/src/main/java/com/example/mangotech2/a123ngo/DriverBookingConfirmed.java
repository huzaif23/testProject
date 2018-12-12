package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.UpdateRideStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DriverBookingConfirmed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_booking_confirmed);
    }
    public  void btnonmyway(View view){

/*
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
        Date currentLocalTime = cal.getTime();
        currentLocalTime.setHours(currentLocalTime.getHours()+8);
        DateFormat date = new SimpleDateFormat("HH:mm");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));


        String currtime = date.format(currentLocalTime);
      //  new UpdateRideStatus(DriverBookingConfirmed.this).execute(Signin.user_id,DriverBooking.BookingDetailId,"2",currtime,"0","","");
*/
        Intent n=new Intent(DriverBookingConfirmed.this,DriverRoute.class);
        startActivity(n);
        /*new Thread() {
            public void run() {

                try {
                    Thread.sleep(5000);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                      //     new UpdateRideStatus(DriverBookingConfirmed.this).execute(Signin.user_id,DriverBooking.BookingDetailId,"2","15.30","16.20","","");
                    }
                    //     Toast.makeText(getApplicationContext(),"UPDated Ride",Toast.LENGTH_SHORT);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        */
    }
}
