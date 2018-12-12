package com.example.mangotech2.a123ngo.Model;

/**
 * Created by Sameer on 12/8/2017.
 */

public class TripModel {
    String status;
    String date,time;
    String price;
    String pickupaddress;
    String dropoffaddress;
    String first_name;
    String phone_num,BookingDetailId,pickuplatitude,pickuplongitude,dropofflatitude,dropofflongitude;

    public TripModel(String status,String date,String price,String pickupaddress,String dropoffaddress){
        this.status=status;
        this.date=date;
        this.price=price;
        this.pickupaddress=pickupaddress;
        this.dropoffaddress=dropoffaddress;
    }
    public TripModel(String status,String date,String price,String pickupaddress,String dropoffaddress,String first_name,String phone_num){
        this.status=status;
        this.date=date;
        this.price=price;
        this.pickupaddress=pickupaddress;
        this.dropoffaddress=dropoffaddress;
        this.first_name=first_name;
        this.phone_num=phone_num;
    }
    public TripModel(String status,String date,String price,String pickupaddress,String dropoffaddress,String first_name,String phone_num,String BookingDetailId,String pickuplatitude,String pickuplongitude,String dropofflatitude,String dropofflongitude,String time){
        this.status=status;
        this.date=date;
        this.price=price;
        this.pickupaddress=pickupaddress;
        this.dropoffaddress=dropoffaddress;
        this.first_name=first_name;
        this.phone_num=phone_num;
        this.BookingDetailId=BookingDetailId;
        this.pickuplatitude=pickuplatitude;
        this.pickuplongitude=pickuplongitude;
        this.dropofflatitude=dropofflatitude;
        this.dropofflongitude=dropofflongitude;
        this.time=time;
    }
    public String getstatus() {
        return status;
    }
    public String getdate() {
        return date;
    }
    public String gettime() {
        return time;
    }
    public String getprice() {
        return price;
    }
    public String getpickupaddress() {
        return pickupaddress;
    }
    public String getdropoffaddress() {
        return dropoffaddress;
    }
    public String getfirst_name() {
        return first_name;
    }
    public String getphone_num() {
        return phone_num;
    }
    public String getBookingDetailId() {
        return BookingDetailId;
    }
    public String getpickuplatitude() {
        return pickuplatitude;
    }
    public String getpickuplongitude() {
        return pickuplongitude;
    }
    public String getdropofflatitude() {
        return dropofflatitude;
    }
    public String getdropofflongitude() {
        return dropofflongitude;
    }

}
