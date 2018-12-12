package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.AddUserRatingAndFeedback;

public class UsersFeedback extends AppCompatActivity {
public static  String fare;
    private android.widget.TextView tvRating;
    private android.widget.RatingBar ratingBar;
    private android.widget.TextView tvFeedback;
    private android.widget.EditText etFeedback;
    private android.widget.ScrollView ScrollView01;
    private android.widget.Button btnratingnow;
    private android.widget.Button btnratinglater;
    String is_driver;
    private TextView passengercollectedcashfare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_feedback);
        DocumentEnums.garbagecollect();
        this.passengercollectedcashfare = (TextView) findViewById(R.id.passengercollectedcashfare);
        this.btnratinglater = (Button) findViewById(R.id.btnratinglater);
        this.btnratingnow = (Button) findViewById(R.id.btnratingnow);
        this.ScrollView01 = (ScrollView) findViewById(R.id.ScrollView01);
        this.etFeedback = (EditText) findViewById(R.id.etFeedback);
        this.tvFeedback = (TextView) findViewById(R.id.tvFeedback);
        this.ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        this.tvRating = (TextView) findViewById(R.id.tvRating);
        try {
            passengercollectedcashfare.setText("" + fare);
        }catch(Exception e){
Toast.makeText(this,"An Error has occured",Toast.LENGTH_LONG).show();
        }
    btnratingnow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(String.valueOf(ratingBar.getRating())==null||String.valueOf(ratingBar.getRating())=="0"){
                showresponse("please Fill Rating bar");
            }else
            if(TripConfirmed.BookingDetailId!=null) {
                if (etFeedback.getText()==null||etFeedback.getText().toString()==""){
                    etFeedback.setText("No Feedback");
                }
             is_driver="2";
                new AddUserRatingAndFeedback().execute(is_driver,TripConfirmed.BookingDetailId, String.valueOf(ratingBar.getRating()),etFeedback.getText().toString());
            showresponse("Thanks For Your Feedback");
                gotohome();
            }else
            if(DriverBooking.BookingDetailId!=null) {
                if (etFeedback.getText()==null||etFeedback.getText().toString()==""){
                    etFeedback.setText("No Feedback");
                }
                is_driver="1";
                new AddUserRatingAndFeedback().execute(is_driver,DriverBooking.BookingDetailId, String.valueOf(ratingBar.getRating()),etFeedback.getText().toString());
                showresponse("Thanks For Your Feedback");
                gotodriverhome();
            }
        }
    });
        btnratinglater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TripConfirmed.BookingDetailId!=null) {
                    gotohome();
                }else
                if(DriverBooking.BookingDetailId!=null) {
                    gotodriverhome();
                }
                }
        });
    }

    public void gotohome() {
        DocumentEnums.garbagecollect();
        Intent n=new Intent(UsersFeedback.this,HomeMain.class);
        startActivity(n);
        finish();
    }

    public void gotodriverhome() {
        DocumentEnums.garbagecollect();
        Intent n=new Intent(UsersFeedback.this,DriverHome.class);
        startActivity(n);
        finish();
    }

    public void showresponse(String message) {
        DocumentEnums.garbagecollect();
        try{
            Toast.makeText(getApplicationContext(),
                    "" + message, Toast.LENGTH_LONG)
                    .show();
        }catch (Exception e){
//        showresponse(e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        DocumentEnums.garbagecollect();

    }

}
