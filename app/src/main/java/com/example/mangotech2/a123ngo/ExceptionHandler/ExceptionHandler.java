package com.example.mangotech2.a123ngo.ExceptionHandler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.R;
import com.example.mangotech2.a123ngo.SigniUpSignIn;
import com.example.mangotech2.a123ngo.Splash;
import com.google.firebase.crash.FirebaseCrash;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by mangotech2 on 10/26/2017.
 */
public class ExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final String LINE_SEPARATOR = "\n";
    Thread.UncaughtExceptionHandler defaultUEH;

    public ExceptionHandler(Context con) {
        myContext = con;
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @SuppressWarnings("deprecation")
    public void uncaughtException(Thread thread, Throwable exception) {

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        final StringBuilder errorReport = new StringBuilder();
        errorReport.append(LINE_SEPARATOR);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());
        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("BOARD: ");
        errorReport.append(Build.BOARD);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("BOOTLOADER: ");
        errorReport.append(Build.BOOTLOADER);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("DISPLAY: ");
        errorReport.append(Build.DISPLAY);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("HARDWARE: ");
        errorReport.append(Build.HARDWARE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("HOST: ");
        errorReport.append(Build.HOST);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("ID: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("MANUFACTURER: ");
        errorReport.append(Build.MANUFACTURER);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("TAGS: ");
        errorReport.append(Build.TAGS);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("TIME: ");
        errorReport.append(Build.TIME);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("TYPE: ");
        errorReport.append(Build.TYPE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("USER: ");
        errorReport.append(Build.USER);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Android Version Code: ");
        errorReport.append(android.os.Build.VERSION.SDK_INT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Android Version Name: ");
        errorReport.append(Build.VERSION.CODENAME);
        try {
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("VM Property: ");
            errorReport.append(System.getProperty("java.vm.version"));
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("All properties: ");
            errorReport.append(System.getProperties());
            errorReport.append(LINE_SEPARATOR);
        }catch (Exception e){

        }
        FirebaseCrash.log(errorReport+"");
        FirebaseCrash.report(new Exception(errorReport.toString()));
        File root = android.os.Environment.getExternalStorageDirectory();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(
                new Date());

        File dir = new File(root.getAbsolutePath() + "/123NGo/Log");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "log.txt");

        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
            buf.append(currentDateTimeString + ":" + errorReport.toString());
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    final String fileurl=dir+"/log.txt";
        // Setting OK Button
/*
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("application/image");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"sameer71095@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"123NGo Unhandled Exception");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "123NGO Logs");
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileurl));
                myContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
*/



/*       Thread th=new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1000);
                }catch (Exception e){

                }finally {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("application/image");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{myContext.getString(R.string.BugMailId)});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"123NGo Unhandled Exception");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "123NGO Logs \n"+errorReport.toString());
                  //  emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileurl));
                    myContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
            }
        };
        th.start();

*/
              //  Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();


        defaultUEH.uncaughtException(thread, exception);
        System.exit(0);
    }

}