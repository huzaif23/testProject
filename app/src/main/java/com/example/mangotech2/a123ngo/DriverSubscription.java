package com.example.mangotech2.a123ngo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.SetDriverSubscriptionDetails;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Driver;
import java.util.UUID;

public class DriverSubscription extends AppCompatActivity {

    private android.widget.ImageView btnsubscriptionback;
    private android.widget.LinearLayout btnsubscriptionbankslip;
    private android.widget.EditText etsubscriptionamount;
    private android.widget.Button btnsubscriptionsubmit;
    public  static   Integer btnindex=0;
    static Bitmap[] imgdoc;
    Intent intent;
    public static  String[] uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_subscription);
        this.btnsubscriptionsubmit = (Button) findViewById(R.id.btnsubscriptionsubmit);
        this.etsubscriptionamount = (EditText) findViewById(R.id.etsubscriptionamount);
        this.btnsubscriptionbankslip = (LinearLayout) findViewById(R.id.btnsubscriptionbankslip);
        this.btnsubscriptionback = (ImageView) findViewById(R.id.btnsubscriptionback);
        btnsubscriptionback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverSubscription.super.onBackPressed();
            }
        });
        imgdoc=new Bitmap[1];
        btnsubscriptionbankslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnindex=0;
                showPictureDialog( DocumentEnums.btncnicfront);

            }
        });


        btnsubscriptionsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uid=new String[1];
                getallimages();
                for(int i=0;i<1;i++) {
                    uid[i]= UUID.randomUUID().toString();
                    ImageResizer(imgdoc[i]);
                }
                if(!etsubscriptionamount.getText().toString().isEmpty()){
                new SetDriverSubscriptionDetails(DriverSubscription.this).execute(uid[0].toString(),etsubscriptionamount.getText().toString(),Signin.user_id);
            }else{
                showresponse("Please Enter Amount");
            }
            }
        });
    }



    void getallimages(){
        DocumentEnums.garbagecollect();
       /* for(int i=0;i<imgdoc.length;i++){
                if(imgdoc[i]!=null){

                    Toast.makeText(getBaseContext(), "Cnic Front Image Uploaded", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getBaseContext(), "Please Insert Cnic Front Image ", Toast.LENGTH_SHORT)
                            .show();
                }
        }*/
    }
    public  static  int i;
    public static String fileloc="";
    private void ImageResizer(Bitmap bitmap) {
        //File root = android.os.Environment.getExternalStorageDirectory();
        //  File dir = new File(root.getAbsolutePath() + "/123NGo/Pic");
        File dir = new File(getApplicationContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), "123NGO");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        fileloc=dir.toString();
        String fname = uid[i]+".jpg";
        File file = new File (dir, fname);
        if (file.exists()){
            file.delete();
            SaveResized(file, bitmap);
        } else {
            SaveResized(file, bitmap);
        }
    }

    private void SaveResized(File file, Bitmap bitmap) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showPictureDialog( final int request){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery(request);
                                break;
                            case 1:
                                takePhotoFromCamera(request);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }


    public void choosePhotoFromGallery(int request){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, request);

    }
    private void takePhotoFromCamera(int request) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, request);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (null != data&& resultCode == RESULT_OK) {
                intent=data;
                imgdoc[requestCode] = (Bitmap) data.getExtras().get("data");
//Bitmap bmp=(Bitmap) data.getExtras().get("refimg");
                // savebitmap((Bitmap) data.getExtras().get("refimg"));
                if(DriverSubscription.btnindex==0){
                    btnsubscriptionbankslip.setBackground(getResources().getDrawable(R.drawable.btnshape));
                }
            }
        } catch (Exception ignored) {

        }
        try{
            if ( resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();

                intent=data;

                imgdoc[requestCode] = MediaStore.Images.Media.getBitmap(this.getContentResolver(), URI);
                if(DriverSubscription.btnindex==0){
                    btnsubscriptionbankslip.setBackground(getResources().getDrawable(R.drawable.btnshape));
                }

            }
        } catch (Exception e) {
            //  Toast.makeText(this, "Please try again", Toast.LENGTH_LONG).show();
        }
    }
    public void showresponse(String message){
        DocumentEnums.garbagecollect();
        Toast.makeText(getApplicationContext(),
                ""+message , Toast.LENGTH_LONG)
                .show();
    }

    public void gotohome(){
        try{
            Intent n=new Intent(this,DriverHome.class);
            startActivity(n);
            this.finish();
    }catch (Exception e){
    }
    }
}
