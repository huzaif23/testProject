package com.example.mangotech2.a123ngo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DriverReport extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private static final String TIME_PATTERN = "HH:mm";
    private android.widget.Spinner spnreport;
    private Button btnstartingdate;
    private Button btnendingdate;
    private Button btngeneratereport;
    private android.widget.TextView tvamount;
    private android.widget.TextView tvtrips;
    private android.widget.TextView tvacceptance;
    private android.widget.TextView tvrating;
    private android.widget.TextView tvdrivercancels;
    private static int selecteddate;

    private int selectedreport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_report);

        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        this.tvdrivercancels = (TextView) findViewById(R.id.tvdrivercancels);
        this.tvrating = (TextView) findViewById(R.id.tvrating);
        this.tvacceptance = (TextView) findViewById(R.id.tvacceptance);
        this.tvtrips = (TextView) findViewById(R.id.tvtrips);
        this.tvamount = (TextView) findViewById(R.id.tvamount);
        this.btngeneratereport = (Button) findViewById(R.id.btngeneratereport);
        this.btnendingdate = (Button) findViewById(R.id.btnendingdate);
        this.btnstartingdate = (Button) findViewById(R.id.btnstartingdate);
        this.spnreport = (Spinner) findViewById(R.id.spnreport);

        btnstartingdate.setVisibility(View.GONE);
        btnendingdate.setVisibility(View.GONE);
        btnstartingdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecteddate=0;
                DatePickerDialog.newInstance(DriverReport.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");

            }
        });
        btnendingdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecteddate=1;
                DatePickerDialog.newInstance(DriverReport.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
            }
        });
        spnreport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 selectedreport=i;

                if(selectedreport==0){
                    btnendingdate.setVisibility(View.GONE);
                    btnstartingdate.setVisibility(View.VISIBLE);
                    btnstartingdate.setHint("Date");
                } else if(selectedreport==1){
                    btnendingdate.setVisibility(View.GONE);
                    btnstartingdate.setVisibility(View.VISIBLE);
                    btnstartingdate.setHint("Date");
                } else if(selectedreport==2){
                    btnendingdate.setVisibility(View.VISIBLE);
                    btnstartingdate.setVisibility(View.VISIBLE);
                    btnstartingdate.setHint("From");
                    btnendingdate.setHint("To");
                }
             }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);



        if(selecteddate==0){
            btnstartingdate.setText(dateFormat.format(calendar.getTime()));
        }else  if(selecteddate==1){
            btnendingdate.setText(dateFormat.format(calendar.getTime()));
        }
      //  btnleavingdate.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
       // btnleavingtime.setText(timeFormat.format(calendar.getTime()));
    }
}
