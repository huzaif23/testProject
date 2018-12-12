package com.example.mangotech2.a123ngo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Interfaces.RetrofitInterface;
import com.example.mangotech2.a123ngo.Model.DriverDetailsSO;
import com.example.mangotech2.a123ngo.Model.VehicleSO;
import com.example.mangotech2.a123ngo.Parsing_JSON.AddVehicle;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignInJsonParsing;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import org.apache.commons.net.ftp.FTP;
//import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.util.UUID.randomUUID;


public class DriverDocuments extends AppCompatActivity {

    LinearLayout btncnicfront,btncnicback,btnvehiclepaper,btnletterofconcert,btnvehicletaxpaper,btnvaliddrivingfront,btnvaliddrivingback;
    // Bitmap imgcnicfront,imgcnicback,imgvehiclepaper,imgletterofconcert,imgvehicletaxpaper,imgvaliddrivingfront,imgvaliddrivingback;
    Button btnsubmitdoc;
    static Bitmap[] imgdoc;
    Intent intent;
    public  static   Integer btnindex=0;
    public static String driver_id;
    public static String vehicle_id,vehicle_type_id,vehicle_owner_id,vehicle_name,registration_number,vehicle_model,vehicle_color,driver_status,nic_number,
            user_id,is_active,created_by,Driving_lisence_back,driving_lisence_front,letterof_concert_from_owner,license,
            nic_back,nic_front,vehicle_tax_paper;


    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "162.241.191.184";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "docftp";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="vKsb13&4\\n";
    public void uploadFile(File fileName){

        FTPClient client = new FTPClient();

        try {

            client.connect(FTP_HOST,21);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
            // client.changeDirectory("/upload/");
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/123NGo/Pic");
            File filename=new File(dir+"/resized.im");
            //    client.upload(fileName, new MyTransferListener());
            client.upload(filename);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    public static  String[] uid;
    public  static  int i;
    public  static  String Driver_Id;
    public ImageView btndriverdocumentback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_documents);
        DocumentEnums.garbagecollect();
        btndriverdocumentback=(ImageView)findViewById(R.id.btndriverdocumentback);
        btndriverdocumentback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgdoc=new Bitmap[7];
        btnsubmitdoc=(Button) findViewById(R.id.btnsubmitdoc);
        btnsubmitdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentEnums.garbagecollect();
                uid=new String[7];
                getallimages();
                for(i=0;i<7;i++) {
                    uid[i]=UUID.randomUUID().toString();
                    ImageResizer(imgdoc[i]);
                }
                //gotodriverhome();

//Testing comment
                String latitude="1",longitude="1";
                GPSTracker  gps = new GPSTracker(DriverDocuments.this);
                if(gps.canGetLocation()) {
                    gps.getLocation();

                    latitude = String.valueOf(gps.getLatitude());
                    longitude = String.valueOf(gps.getLongitude());
                }

                new AddVehicle(DriverDocuments.this).execute(latitude,longitude,"1","1");


                SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Driver_Id", Driver_Id);
                editor.commit();
                /*
                DriverDetailsSO driverDetailsSO=new DriverDetailsSO();
                VehicleSO vehicleSO=new VehicleSO();
                vehicleSO.vehicle_type_id=2;
                vehicleSO.vehicle_owner_id=3;
                vehicleSO.vehicle_name="Corolla";
                vehicleSO.registration_number="AFR2018";
                vehicleSO.vehicle_model="2018";
                vehicleSO.vehicle_color="black";
                vehicleSO.vehicle_tax_paper="Vehicle Tax Paper";
driverDetailsSO.setVehicleSO(vehicleSO);
                driverDetailsSO.driver_status=1;
                driverDetailsSO.nic_number="4212899856638";
                driverDetailsSO.user_id=3;
                driverDetailsSO.is_active=1;
                driverDetailsSO.created_by=3;
                driverDetailsSO.driving_lisence_back="driver license";
                driverDetailsSO.driving_lisence_front="drivinglicensefront";
                driverDetailsSO.letterof_concert_from_owner="letter";
                driverDetailsSO.nic_back="nic_back";
                driverDetailsSO.nic_front="nic_front";
                driverDetailsSO.license_number="88283828382832";
  */        //      sendNetworkRequest(driverDetailsSO);
//uploadFile(new File("str"));

            /*    Thread th=new Thread(){
                    @Override
                    public void run() {
              */        /*  try {
                            FTPClient ftpClient = new FTPClient();
                            ftpClient.connect(InetAddress.getByName("192.185.10.184/services.123ngo.com/Images"));
                            ftpClient.login("123ngoimage", "G5dt2_2p");
                            //   ftpClient.changeWorkingDirectory(serverRoad);
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            imgdoc[0].compress(Bitmap.CompressFormat.PNG, 0 , bos);
                            byte[] bitmapdata = bos.toByteArray();
                            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);


                            File root = android.os.Environment.getExternalStorageDirectory();
                            File dir = new File(root.getAbsolutePath() + "/123NGo/Pic");
                            //  String testName =System.currentTimeMillis()+file.getName();

                            // Upload file to the ftp server
                            //result = client.storeFile(testName, fis);
                            BufferedInputStream buffIn = null;
                            buffIn = new BufferedInputStream(new FileInputStream(dir + "/resized.im"));
                            ftpClient.enterLocalPassiveMode();
                            UUID rndnum = UUID.randomUUID();
                            ftpClient.storeFile(rndnum + ".jpg", bs);
                            buffIn.close();
                            ftpClient.logout();
                            ftpClient.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                //    }
                //    };
                //th.start();

                // Intent n=new Intent(DriverDocuments.this,DriverHome.class);
                //   startActivity(n);
            }
        });
        btncnicfront=(LinearLayout)findViewById(R.id.btncnicfront);
        btncnicfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnindex=0;
                showPictureDialog( DocumentEnums.btncnicfront);

            }
        });
        btncnicback=(LinearLayout)findViewById(R.id.btncnicback);
        btncnicback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnindex=1;
                showPictureDialog(DocumentEnums.btncnicback);

            }
        });
        btnvehiclepaper=(LinearLayout)findViewById(R.id.btnvehiclepaper);
        btnvehiclepaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnindex=2;
                showPictureDialog( DocumentEnums.btnvehiclepaper);
            }
        });
        btnletterofconcert=(LinearLayout)findViewById(R.id.btnletterofconcert);
        btnletterofconcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnindex=3;
                showPictureDialog(DocumentEnums.btnletterofconcert);
            }
        });
        btnvehicletaxpaper=(LinearLayout)findViewById(R.id.btnvehicletaxpaper);
        btnvehicletaxpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnindex=4;
                showPictureDialog(DocumentEnums.btnvehicletaxpaper);
            }
        });
        btnvaliddrivingfront=(LinearLayout)findViewById(R.id.btnvaliddrivingfront);
        btnvaliddrivingfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnindex=5;
                showPictureDialog(DocumentEnums.btnvaliddrivingfront);
            }
        });
        btnvaliddrivingback=(LinearLayout)findViewById(R.id.btnvaliddrivingback);
        btnvaliddrivingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnindex=6;
                showPictureDialog( DocumentEnums.btnvaliddrivingback);
            }
        });
    }

    public  void gotodriverhome() {
        Intent n=new Intent(DriverDocuments.this,DriverHome.class);
        startActivity(n);
        finish();
    }


    private void sendNetworkRequest(DriverDetailsSO driverDetailsSO){
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("http://123ngo.somee.com/api/Vehicle/RegisterVehicle")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Call<DriverDetailsSO> call= retrofitInterface.createAccount(driverDetailsSO);
        call.enqueue(new Callback<DriverDetailsSO>() {
            @Override
            public void onResponse(Call<DriverDetailsSO> call, Response<DriverDetailsSO> response) {
                Toast.makeText(DriverDocuments.this,"Response"+response.message().toString(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<DriverDetailsSO> call, Throwable t) {
                Toast.makeText(DriverDocuments.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }
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




    void getallimages(){
        DocumentEnums.garbagecollect();
        for(int i=0;i<imgdoc.length;i++){
            if(i==DocumentEnums.btncnicfront){
                if(imgdoc[i]!=null){

                    Toast.makeText(getBaseContext(), "Cnic Front Image Uploaded", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getBaseContext(), "Please Insert Cnic Front Image ", Toast.LENGTH_SHORT)
                            .show();
                }
            }else if(i==DocumentEnums.btncnicback){
                if(imgdoc[i]!=null){
                    Toast.makeText(getBaseContext(), "Cnic  Back Image Uploaded", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getBaseContext(), "Please Insert Cnic  Back Image ", Toast.LENGTH_SHORT)
                            .show();
                }
            }else if(i==DocumentEnums.btnvehiclepaper){
                if(imgdoc[i]!=null){
                    Toast.makeText(getBaseContext(), "Vehicle Paper Image Uploaded", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getBaseContext(), "Please Insert Vehicle Paper Image ", Toast.LENGTH_SHORT)
                            .show();
                }
            }else if(i==DocumentEnums.btnletterofconcert){
                if(imgdoc[i]!=null){
                    Toast.makeText(getBaseContext(), "Letter Of Concert Image Uploaded", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getBaseContext(), "Please Insert Letter Of Concert Image ", Toast.LENGTH_SHORT)
                            .show();
                }
            }else if(i==DocumentEnums.btnvehicletaxpaper){
                if(imgdoc[i]!=null){
                    Toast.makeText(getBaseContext(), "Vehicle Tax Paper Image Uploaded", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getBaseContext(), "Please Insert Vehicle Tax Paper Image ", Toast.LENGTH_SHORT)
                            .show();
                }
            }else if(i==DocumentEnums.btnvaliddrivingfront){
                if(imgdoc[i]!=null){
                    Toast.makeText(getBaseContext(), "Valid Driving Front Image Uploaded", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getBaseContext(), "Please Insert Valid Driving Front Image ", Toast.LENGTH_SHORT)
                            .show();
                }
            }else if(i==DocumentEnums.btnvaliddrivingback){
                if(imgdoc[i]!=null){
                    Toast.makeText(getBaseContext(), "Valid Driving Back Image Uploaded", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getBaseContext(), "Please Insert Vehicle Valid Driving Back Image ", Toast.LENGTH_SHORT)
                            .show();
                }
            }
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
        intent = new Intent(Intent.ACTION_PICK,
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
        DocumentEnums.garbagecollect();
        try {

            if (null != data&& resultCode == RESULT_OK) {
                intent=data;
                imgdoc[requestCode] = (Bitmap) data.getExtras().get("data");
//Bitmap bmp=(Bitmap) data.getExtras().get("refimg");
                // savebitmap((Bitmap) data.getExtras().get("refimg"));
                if(DriverDocuments.btnindex==0){
                    btncnicfront.setBackground(getResources().getDrawable(R.drawable.btnshape));
                }else if(btnindex==1){
                    btncnicback.setBackground(getResources().getDrawable(R.drawable.btnshape));

                }else if(btnindex==2){
                    btnvehiclepaper.setBackground(getResources().getDrawable(R.drawable.btnshape));

                }else if(btnindex==3){
                    btnletterofconcert.setBackground(getResources().getDrawable(R.drawable.btnshape));

                }else if(btnindex==4){
                    btnvehicletaxpaper.setBackground(getResources().getDrawable(R.drawable.btnshape));

                }else if(btnindex==5){

                    btnvaliddrivingfront.setBackground(getResources().getDrawable(R.drawable.btnshape));
                }else if(btnindex==6){

                    btnvaliddrivingback.setBackground(getResources().getDrawable(R.drawable.btnshape));
                }
                //    Toast.makeText(DriverDocuments.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ignored) {

        }
        try{
            if ( resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();

                intent=data;

                imgdoc[requestCode] = MediaStore.Images.Media.getBitmap(this.getContentResolver(), URI);
                if(DriverDocuments.btnindex==0){
                    btncnicfront.setBackground(getResources().getDrawable(R.drawable.btnshape));
                }else if(btnindex==1){
                    btncnicback.setBackground(getResources().getDrawable(R.drawable.btnshape));

                }else if(btnindex==2){
                    btnvehiclepaper.setBackground(getResources().getDrawable(R.drawable.btnshape));

                }else if(btnindex==3){
                    btnletterofconcert.setBackground(getResources().getDrawable(R.drawable.btnshape));

                }else if(btnindex==4){
                    btnvehicletaxpaper.setBackground(getResources().getDrawable(R.drawable.btnshape));

                }else if(btnindex==5){

                    btnvaliddrivingfront.setBackground(getResources().getDrawable(R.drawable.btnshape));
                }else if(btnindex==6){

                    btnvaliddrivingback.setBackground(getResources().getDrawable(R.drawable.btnshape));
                }
        /*
                Intent intent = getIntent();
                Bitmap bitmap = (Bitmap) data.getParcelableExtra("refimg");
                   savebitmap(bitmap);
  */    //          Toast.makeText(this, "Image Saved", Toast.LENGTH_LONG)
                //              .show();
             /*   String[] FILE = { MediaStore.Images.Media.DATA };


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

               cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();
imgcnicfront= BitmapFactory.decodeFile(ImageDecode);
                String a="";
                //imageViewLoad.setImageBitmap(BitmapFactory
             //           .decodeFile(ImageDecode));
*/
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
}
