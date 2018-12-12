package com.example.mangotech2.a123ngo.Enum;

import android.content.Intent;

import com.example.mangotech2.a123ngo.SigniUpSignIn;
import com.example.mangotech2.a123ngo.Splash;

/**
 * Created by mangotech2 on 10/26/2017.
 */

public class DocumentEnums {
 public static final int btncnicfront=0,btncnicback = 1,btnvehiclepaper = 2,
         btnletterofconcert = 3,btnvehicletaxpaper = 4,btnvaliddrivingfront = 5,btnvaliddrivingback = 6,btndriverimg=7
 ,FindBooking=1,ConfirmBookingFromPassenger=2,ConfirmBookingFromDriver=3,CompleteBooking=4,CancelBooking=5,BookingLater=6,CancelBookingFromPassenger=7,CancelBookingFromDriver=8,UpdateDriverVehicleLocation=9,RideStart=10,DriverReached=13;
 public static String current_latitude,current_longitude;
    public static String IsDriverSubscribed="false";

 public  static  String Bike = "1", Auto = "2", Goplus = "3", Go_X = "4", Business="5",Taxi = "6", Bus = "7";
 public static String ApiUrl="http://162.241.191.184:8089";


 public static String interneterror="Internet Connection Problem";
 public static void garbagecollect(){

  System.gc();
 }
}
