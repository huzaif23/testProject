package com.example.mangotech2.a123ngo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.Parsing_JSON.BookingContinueForDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.SignOutDriver;
import com.example.mangotech2.a123ngo.Parsing_JSON.UserBookingHistory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class DriverDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String array_spinner[];
    EditText etcity,etaddress,etcnic,etlicenseno;
    Spinner spncountry;
    LinearLayout btndriverimg;
  public  static  String country,city,address,cnic,licenseno;
    static Bitmap[] imgdoc;
    public static  String[] uid;
    Intent intent;
    public  static  int i;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
   public static String profileimageuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_driver_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imgdoc=new Bitmap[7];
        uid=new String[1];
btndriverimg=(LinearLayout)findViewById(R.id.btndriverimg);
        btndriverimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog( DocumentEnums.btndriverimg);
            }
        });
        array_spinner= new String[]{"Pakistan","Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Anguilla", "Antigua", "Argentina",
                "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia &amp; Herzegovina", "Botswana", "Brazil", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Cape Verde", "Cayman Islands", "Chad", "Chile", "China", "Colombia", "Congo", "Cook Islands", "Costa Rica", "Cote D Ivoire", "Croatia", "Cruise Ship",
                "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Estonia", "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Polynesia", "French West Indies", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea Bissau", "Guyana", "Haiti", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man",
                "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kuwait", "Kyrgyz Republic", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Mauritania", "Mauritius", "Mexico", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Namibia", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Norway", "Oman",
                 "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "Saint Pierre &amp; Miquelon", "Samoa", "San Marino", "Satellite", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "South Africa", "South Korea", "Spain", "Sri Lanka", "St Kitts &amp; Nevis", "St Lucia", "St Vincent", "St. Lucia", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania",
                "Thailand", "Timor L'Este", "Togo", "Tonga", "Trinidad &amp; Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks &amp; Caicos", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "Uruguay", "Uzbekistan", "Venezuela", "Vietnam", "Virgin Islands (US)", "Yemen", "Zambia", "Zimbabwe"};

        Spinner ss = (Spinner) findViewById(R.id.spncountry);
        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.spinner_layout, array_spinner);
        ss.setAdapter(adapter);

        spncountry=(Spinner)findViewById(R.id.spncountry);
        etcity=(EditText)findViewById(R.id.etcity);
        etaddress=(EditText)findViewById(R.id.etaddress);
        etcnic=(EditText)findViewById(R.id.etcnic);
        etlicenseno=(EditText)findViewById(R.id.etlicenseno);
    }

    public void btnnext(View v){
        DocumentEnums.garbagecollect();

        country=spncountry.getSelectedItem().toString();
        city=etcity.getText().toString();
        address=etaddress.getText().toString();
        cnic=etcnic.getText().toString();
        licenseno=etlicenseno.getText().toString();

        if(country!=null && country!="" &&!city.isEmpty()&& city!=null && city!="" &&!address.isEmpty()&& address!=null && address!="" &&!cnic.isEmpty()&& cnic!=null && cnic!="" &&!licenseno.isEmpty() && licenseno!=null && licenseno!=""){
        getallimages();
        for(i=0;i<1;i++) {
            uid[i]= UUID.randomUUID().toString();
            ImageResizer(imgdoc[i]);
            profileimageuid=uid[i];
        }
        Intent n=new Intent(DriverDetails.this,VehicleDetail.class);
        startActivity(n);
        finish();
    }else{
            Toast.makeText(this,"Please Provide all details",Toast.LENGTH_LONG).show();
        }
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
        for(int i=0;i<imgdoc.length;i++){
                if(imgdoc[i]!=null){

                    Toast.makeText(getBaseContext(), "Driver Image Uploaded", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getBaseContext(), "Please Insert Driver Image ", Toast.LENGTH_SHORT)
                            .show();
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
        i=0;
        try {

            if (null != data&& resultCode == RESULT_OK) {
                intent=data;
                imgdoc[i] = (Bitmap) data.getExtras().get("data");
//Bitmap bmp=(Bitmap) data.getExtras().get("refimg");
                // savebitmap((Bitmap) data.getExtras().get("refimg"));
                    btndriverimg.setBackground(getResources().getDrawable(R.drawable.btnshape));
            }
        } catch (Exception ignored) {
String msg=ignored.getMessage().toString();
        }
        try{
            if ( resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();

                intent=data;

                imgdoc[requestCode] = MediaStore.Images.Media.getBitmap(this.getContentResolver(), URI);
                   btndriverimg.setBackground(getResources().getDrawable(R.drawable.btnshape));
                  }
        } catch (Exception e) {

            String msg=e.getMessage().toString();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        try{
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_Home) {
                Intent n=new Intent(this,HomeMain.class);
                startActivity(n);
                finish();

            } else if (id == R.id.nav_MyTrips) {
                HomeMain.initializemytripsobject();
                new UserBookingHistory(HomeMain.contexthomemain).execute(Signin.user_id);
                finish();

            } else if (id == R.id.nav_MyProfile) {
                Intent n=new Intent(this,Settings.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_GetFREERides) {
                Intent n=new Intent(this,GetFreeRides.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_Settings) {
                Intent n=new Intent(this,MainSettings.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_Language) {
                Intent n=new Intent(this,Language.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_Contactus) {
                Intent n=new Intent(this,Contactusdialog.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_Help) {
                //      Intent n=new Intent(this,Settings.class);
                //    startActivity(n);
            } else if (id == R.id.nav_Rates) {
                Intent n=new Intent(this,Rates.class);
                startActivity(n);
                finish();
            } else if (id == R.id.nav_DrivethroughUs) {
            /*    SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());

                if(!DriverDocuments.driver_id.equals("no driver_id") &&!DriverDocuments.driver_status.equals("no driver_status")){
                    new BookingContinueForDriver(HomeMain.contexthomemain).execute(DriverDocuments.driver_id);

                    //gotodriverhome();
                }else
                {

                    Intent n=new Intent(this,DriverDetails.class);
                    startActivity(n);

                }
                // finish();
           */ }else if (id == R.id.nav_Logout) {

                SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("access_token");
                editor.remove("driver_id");
                editor.remove("vehicle_name");
                editor.remove("registration_number");
                editor.remove("driver_status");
                editor.remove("user_id");
                editor.commit();
                if(DriverDocuments.driver_id!="no driver_id"&&DriverDocuments.driver_id!=null&&DriverDocuments.driver_id!=""){
                    new SignOutDriver().execute(DriverDocuments.driver_id);
                }
                DriverDocuments.driver_id=null;
                Intent n=new Intent(this,SigniUpSignIn.class);
                startActivity(n);
                finish();
            }else if (id == R.id.nav_Logout) {

                SharedPreferences prefs =getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("access_token");
                editor.commit();
                Intent n=new Intent(this,SigniUpSignIn.class);
                startActivity(n);
                finish();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }catch (Exception e){
            //showresponse(e.getMessage());
        }

        return true;

    }
}
