package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.LaterPassengerBookingConfirm;
import com.example.mangotech2.a123ngo.Parsing_JSON.PassengerBookingConfirm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.mangotech2.a123ngo.mapdirection.Dropoffaddress;
import static com.example.mangotech2.a123ngo.mapdirection.Dropoffloc;
import static com.example.mangotech2.a123ngo.mapdirection.distanceridelater;
import static com.example.mangotech2.a123ngo.mapdirection.farelater;
import static com.example.mangotech2.a123ngo.mapdirection.isvehicleselected;
import static com.example.mangotech2.a123ngo.mapdirection.promo;
import static com.example.mangotech2.a123ngo.mapdirection.vehicle_type_id;

public class RideLater extends AppCompatActivity {
    DatePicker dpDate;
    TimePicker tpTime;
    public static String times,date;
    public ImageView btnback;
    private android.widget.Button btnsavetrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_later);
        DocumentEnums.garbagecollect();
        btnback=(ImageView)findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.btnsavetrip = (Button) findViewById(R.id.btnsavetrip);
        dpDate = (DatePicker)findViewById(R.id.dpDate);
        tpTime = (TimePicker)findViewById(R.id.tpTime);
        // set the time picker mode to 24 hour view
        tpTime.setIs24HourView(false);

        // set a time changed listener to time picker
        tpTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                String time = "Current time: " + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
             //   Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();
                times=timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dpDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    date=(datePicker.getDayOfMonth()+"-")+((datePicker.getMonth() + 1)+"-")+(datePicker.getYear());
                 //   Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
                }
            });
        }
        btnsavetrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentEnums.garbagecollect();
                date=(dpDate.getYear()+"-")+((dpDate.getMonth() + 1)+"-")+(dpDate.getDayOfMonth());

              //  Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
                String currDate = "" + mdformat.format(calendar.getTime());
                //     new LaterPassengerBookingConfirm(RideLater.this).execute(Signin.user_id,"6",currDate,Signin.user_id,date,times,vehicle_type_id,"","","","",String.valueOf(HomeMain.Pickuploc.latitude),String.valueOf(HomeMain.Pickuploc.longitude),String.valueOf(mapdirection.Dropoffloc.latitude),String.valueOf(Dropoffloc.longitude),"1");

                try {
                    String pickuplat = String.valueOf(HomeMain.Pickuploc.latitude);
                    String pickuplong = String.valueOf(HomeMain.Pickuploc.longitude);
                    String dropoofflat = String.valueOf(Dropoffloc.latitude);
                    String dropofflong = String.valueOf(Dropoffloc.longitude);

                    if(Dropoffaddress!=null&&!Dropoffaddress.isEmpty()&&Dropoffaddress!="") {
                        if (isvehicleselected&&mapdirection.fareval[Integer.parseInt(mapdirection.vehicle_type_id)]!=null) {

                            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:00"));
                            Date currentLocalTime = cal.getTime();
                            currentLocalTime.setHours(currentLocalTime.getHours());
                            DateFormat dates = new SimpleDateFormat("HH:mm");
// you can get seconds by adding  "...:ss" to it
                            dates.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
                            String currtime = dates.format(currentLocalTime);

                            dates = new SimpleDateFormat("yyyy-MM-dd");
// you can get seconds by adding  "...:ss" to it
                            dates.setTimeZone(TimeZone.getTimeZone("GMT+5:00"));
                            String currdate = dates.format(currentLocalTime);

                            String dateStart = currDate +" "+currtime;
                            String dateStop = date+" "+times;

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date d1 = null;
                            Date d2 = null;

                            d1 = format.parse(dateStart);
                            d2 = format.parse(dateStop);

                            //in milliseconds
                            long diff = d2.getTime() - d1.getTime();
                            long diffSeconds = diff / 1000 % 60;
                            long diffMinutes = diff / (60 * 1000) % 60;
                            long diffHours = diff / (60 * 60 * 1000) % 24;
                            long diffDays = diff / (24 * 60 * 60 * 1000);

                            long totalminutes=(diffHours*60)+(diffMinutes)+(diffSeconds/60);

                            if(diffDays>=0&&totalminutes>=0) {
                                new LaterPassengerBookingConfirm(RideLater.this).execute(Signin.user_id, "6", date, Signin.user_id, date, times, vehicle_type_id, "", "", "", "", pickuplat, pickuplong, dropoofflat, dropofflong, farelater, distanceridelater, HomeMain.pickupaddress, Dropoffaddress, "1", "1", "1", "1",promo,"1");
                            }else{

                                showresponse("Please Select Valid date and time");
                            }
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
        });
    }
    public void getDate(View v) {
        DocumentEnums.garbagecollect();
        StringBuilder builder=new StringBuilder();
        builder.append("Current Date: ");
        builder.append((dpDate.getMonth() + 1)+"/");//month is 0 based
        builder.append(dpDate.getDayOfMonth()+"/");
        builder.append(dpDate.getYear());
      //  Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
    }
    public void showresponse(String message){
        DocumentEnums.garbagecollect();
       Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }
    public void bookingconfirmed(){
        DocumentEnums.garbagecollect();
        Toast.makeText(this, "Booking Confirmed", Toast.LENGTH_LONG);
        Intent n=new Intent(RideLater.this,HomeMain.class);
        startActivity(n);
    }
}
