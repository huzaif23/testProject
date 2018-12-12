package com.example.mangotech2.a123ngo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;

public class VehicleDetail extends AppCompatActivity {
    EditText etvehiclename,etregistrationnumber,etvehiclemodel,etvehiclecolor;
    public static String vehiclename,registrationnumber,vehiclemodel,vehiclecolor;
    LinearLayout btndriverbike,btndriverauto,btndrivergoplus,btndrivergox,btndriverbusiness,btndrivertaxi,btndriverbus;
    public static int vehicle_type_id;
    Button btnvehiclenext;
    public ImageView btnvehicledetailtback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);
        DocumentEnums.garbagecollect();
        btnvehicledetailtback=(ImageView)findViewById(R.id.btnvehicledetailtback);
        btnvehicledetailtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        etvehiclename=(EditText)findViewById(R.id.etvehiclename);
        etregistrationnumber=(EditText)findViewById(R.id.etregistrationnumber);
        etvehiclemodel=(EditText)findViewById(R.id.etvehiclemodel);
        etvehiclecolor=(EditText)findViewById(R.id.etvehiclecolor);
        btnvehiclenext=(Button) findViewById(R.id.btnvehiclenext);
        btndriverbike=(LinearLayout)findViewById(R.id.btndriverbike);
        btndriverbike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicle_type_id=1;
                btndriverbike.setBackground(getResources().getDrawable(R.drawable.bikeclicked));
                btndriverauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                btndrivergoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                btndrivergox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                btndriverbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                btndrivertaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                btndriverbus.setBackground(getResources().getDrawable(R.drawable.final_bus));

            }
        });
        btndriverauto=(LinearLayout)findViewById(R.id.btndriverauto);
        btndriverauto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicle_type_id=2;
                btndriverbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                btndriverauto.setBackground(getResources().getDrawable(R.drawable.autoclicked));
                btndrivergoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                btndrivergox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                btndriverbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                btndrivertaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                btndriverbus.setBackground(getResources().getDrawable(R.drawable.final_bus));

            }
        });
        btndrivergoplus=(LinearLayout)findViewById(R.id.btndrivergoplus);
        btndrivergoplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicle_type_id=3;
                btndriverbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                btndriverauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                btndrivergoplus.setBackground(getResources().getDrawable(R.drawable.gocarclicked));
                btndrivergox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                btndriverbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                btndrivertaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                btndriverbus.setBackground(getResources().getDrawable(R.drawable.final_bus));

            }
        });
        btndrivergox=(LinearLayout)findViewById(R.id.btndrivergox);
        btndrivergox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicle_type_id=4;
                btndriverbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                btndriverauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                btndrivergoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                btndrivergox.setBackground(getResources().getDrawable(R.drawable.goplusclicked));
                btndriverbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                btndrivertaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                btndriverbus.setBackground(getResources().getDrawable(R.drawable.final_bus));

            }
        });
        btndriverbusiness=(LinearLayout)findViewById(R.id.btndriverbusiness);
        btndriverbusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicle_type_id=5;

                btndriverbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                btndriverauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                btndrivergoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                btndrivergox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                btndriverbusiness.setBackground(getResources().getDrawable(R.drawable.businessclicked));
                btndrivertaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                btndriverbus.setBackground(getResources().getDrawable(R.drawable.final_bus));

            }
        });
        btndrivertaxi=(LinearLayout)findViewById(R.id.btndrivertaxi);
        btndrivertaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicle_type_id=6;

                btndriverbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                btndriverauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                btndrivergoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                btndrivergox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                btndriverbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                btndrivertaxi.setBackground(getResources().getDrawable(R.drawable.taxiclicked));
                btndriverbus.setBackground(getResources().getDrawable(R.drawable.final_bus));

            }
        });
        btndriverbus=(LinearLayout)findViewById(R.id.btndriverbus);
        btndriverbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicle_type_id=7;

                btndriverbike.setBackground(getResources().getDrawable(R.drawable.final_bike));
                btndriverauto.setBackground(getResources().getDrawable(R.drawable.final_auto));
                btndrivergoplus.setBackground(getResources().getDrawable(R.drawable.gocar));
                btndrivergox.setBackground(getResources().getDrawable(R.drawable.fianl_go));
                btndriverbusiness.setBackground(getResources().getDrawable(R.drawable.final_business));
                btndrivertaxi.setBackground(getResources().getDrawable(R.drawable.final_taxi));
                btndriverbus.setBackground(getResources().getDrawable(R.drawable.busclicked));

            }
        });
        btnvehiclenext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehiclename=etvehiclename.getText().toString();
                registrationnumber=etregistrationnumber.getText().toString();
                vehiclemodel=etvehiclemodel.getText().toString();
                vehiclecolor=etvehiclecolor.getText().toString();
                if(!vehiclename.isEmpty()&&vehiclename!=null&&vehiclename!=""&&!registrationnumber.isEmpty()&&registrationnumber!=null&&registrationnumber!=""&&!vehiclemodel.isEmpty()&&vehiclemodel!=null&&vehiclemodel!=""&&!vehiclecolor.isEmpty()&&vehiclecolor!=null&&vehiclecolor!=""){
                    Intent n=new Intent(VehicleDetail.this,DriverDocuments.class);
                startActivity(n);
                finish();
            }else{
                    Toast.makeText(VehicleDetail.this,"Please Provide all details",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
