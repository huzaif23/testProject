package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;

public class DriverCollectCash extends AppCompatActivity {

    private android.widget.TextView drivercollectedcashfare,txtdrivercollectedcashbasefare,txtdrivercollectedcashdistance,txtdrivercollectedcashtime,txtdrivercollectedcashsubtotal,txtdrivercollectedcashpromotion;
    private android.widget.TextView txtcustomername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_collect_cash);
        txtcustomername = (TextView) findViewById(R.id.txtcustomername);
        txtdrivercollectedcashbasefare = (TextView) findViewById(R.id.txtdrivercollectedcashbasefare);
        txtdrivercollectedcashdistance = (TextView) findViewById(R.id.txtdrivercollectedcashdistance);
        txtdrivercollectedcashtime = (TextView) findViewById(R.id.txtdrivercollectedcashtime);
        txtdrivercollectedcashsubtotal = (TextView) findViewById(R.id.txtdrivercollectedcashsubtotal);
        txtdrivercollectedcashpromotion = (TextView) findViewById(R.id.txtdrivercollectedcashpromotion);
     try {
         txtcustomername.setText("Customer: "+DriverBooking.PassengerName);
         txtdrivercollectedcashbasefare.setText("Rs: "+DriverMetering.BaseFare);
         txtdrivercollectedcashdistance.setText(""+DriverMetering.TotalDistance+" Km");
         txtdrivercollectedcashtime.setText(""+DriverMetering.TotalTimeInMinutes+" Min");
         Integer subtotal=Integer.parseInt(DriverMetering.totalcollectedfare)-Integer.parseInt(DriverMetering.Promotion);
         txtdrivercollectedcashsubtotal.setText("Rs: "+String.valueOf(subtotal));
         txtdrivercollectedcashpromotion.setText(""+DriverMetering.Promotion+"");
     }catch (Exception e){

     }
     this.drivercollectedcashfare = (TextView) findViewById(R.id.drivercollectedcashfare);
        this.drivercollectedcashfare.setText(DriverMetering.totalcollectedfare);
    }
    public void btncollectcash(View v){
Intent n=new Intent(DriverCollectCash.this,UsersFeedback.class);
        startActivity(n);
        finish();
    }

    @Override
    public void onBackPressed() {

    }

}
