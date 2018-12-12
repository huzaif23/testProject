package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mangotech2.a123ngo.DriverDetails;
import com.example.mangotech2.a123ngo.DriverDocuments;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.MainActivity;
import com.example.mangotech2.a123ngo.Signin;
import com.example.mangotech2.a123ngo.VehicleDetail;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class AddVehicle extends AsyncTask<String, Void, String> {
    DriverDocuments driverDocuments;
    String Error_Message="";
    public AddVehicle(DriverDocuments driverDocuments){
        this.driverDocuments=driverDocuments;
        Error_Message="";


    }
    protected void onPreExecute(){

    }
    String vehicle_type_id,vehicle_owner_id,vehicle_name,registration_number,vehicle_model,vehicle_color,driver_status,nic_number,
            user_id,is_active,created_by,Driving_lisence_back,driving_lisence_front,letterof_concert_from_owner,license,
            nic_back,nic_front,vehicle_tax_paper;

    String latitude,longitude;

    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "162.241.191.184";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "docftp";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="vKsb13&4";
    @Override
    protected String doInBackground(String... strings) {

        InputStream inputStream = null;
        String result = "";
        try{
            it.sauronsoftware.ftp4j.FTPClient client = new it.sauronsoftware.ftp4j.FTPClient();
            try {

                client.connect(FTP_HOST,21);
                client.login(FTP_USER, FTP_PASS);
                client.setType(it.sauronsoftware.ftp4j.FTPClient.TYPE_BINARY);
                client.changeDirectory("");
for(int i=0;i<7;i++) {
    File filename = new File(DriverDocuments.fileloc + "/" + driverDocuments.uid[i]+".jpg");
    //    client.upload(fileName, new MyTransferListener());
    client.upload(filename);
}
File filename = new File(DriverDetails.fileloc + "/" + DriverDetails.uid[0]+".jpg");
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



          //  String url = new String("http://192.168.15.140:8088/api/Vehicle/RegisterVehicle");
            URL url = new URL(DocumentEnums.ApiUrl+"/api/Vehicle/RegisterVehicle");
latitude=strings[0];
            longitude=strings[1];
            String city_id=strings[2];
            String Country_id=strings[3];
/*
             vehicle_type_id=strings[0];
             vehicle_owner_id=strings[1];
             vehicle_name=strings[2];
             registration_number=strings[3];
             vehicle_model=strings[4];
             vehicle_color=strings[5];
             driver_status=strings[6];
             nic_number=strings[7];
             user_id=strings[8];
             is_active=strings[9];
             created_by=strings[10];
             Driving_lisence_back=strings[11];
             driving_lisence_front=strings[12];
             letterof_concert_from_owner=strings[13];
             license=strings[14];
             nic_back=strings[15];
             nic_front=strings[16];
             vehicle_tax_paper=strings[17];
*/

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("driver_status",1);
            postDataParams.put("city",DriverDetails.city);
            postDataParams.put("country",DriverDetails.country);
            postDataParams.put("address",DriverDetails.address);
            postDataParams.put("is_active",1);
            postDataParams.put("nic_number", DriverDetails.cnic);
            postDataParams.put("user_id",Integer.parseInt(Signin.user_id));
            postDataParams.put("is_active",1);
            postDataParams.put("created_by",Integer.parseInt(Signin.user_id));
            postDataParams.put("Driving_lisence_back",DriverDocuments.uid[6].toString()+".jpg");
            postDataParams.put("driving_lisence_front",DriverDocuments.uid[5].toString()+".jpg");
            postDataParams.put("letterof_concert_from_owner",DriverDocuments.uid[3].toString()+".jpg");
            postDataParams.put("license","license");
            postDataParams.put("nic_back",DriverDocuments.uid[1].toString()+".jpg");
            postDataParams.put("nic_front",DriverDocuments.uid[0].toString()+".jpg");
            postDataParams.put("license_number",DriverDetails.licenseno);
            postDataParams.put("vehicle_type_id", VehicleDetail.vehicle_type_id);
            postDataParams.put("vehicle_owner_id",Integer.parseInt(Signin.user_id));
            postDataParams.put("registration_number",VehicleDetail.registrationnumber);
            postDataParams.put("vehicle_model",VehicleDetail.vehiclemodel);
            postDataParams.put("vehicle_color",VehicleDetail.vehiclecolor);
            postDataParams.put("vehicle_tax_paper",DriverDocuments.uid[4].toString()+".jpg");
            postDataParams.put("vehicle_name",VehicleDetail.vehiclename);
            postDataParams.put("current_latitude",latitude);
            postDataParams.put("current_longitude",longitude);
            postDataParams.put("city_id",Integer.parseInt(city_id));
            postDataParams.put("Country_id",Integer.parseInt(Country_id));
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setRequestProperty("Authorization","Bearer "+Signin.access_token);
            conn.addRequestProperty("Authorization","Bearer "+Signin.access_token);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            }
            else {
                Error_Message="False : "+responseCode;
                return new String("false : "+responseCode);
            }

        } catch (ProtocolException e1) {
            //  e1.printStackTrace();
            return null;
        } catch (IOException e1) {
            Error_Message=DocumentEnums.interneterror;
            return null;
        } catch (JSONException e1) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    @Override
    protected void onPostExecute(String result) {
        if(Error_Message==null){
            Error_Message="";
        }

//driverDocuments.showresponse(result);
        if(Error_Message!=""){
            driverDocuments.showresponse(Error_Message);
            Error_Message="";
        }else{

        try {
            JSONObject jsonObject = new JSONObject(result);
            Jsontohashmap.jsonmapvalue= new HashMap<String, Object>();

            Map<String, Object> vals=Jsontohashmap.jsonToMap(jsonObject);
            vals=Jsontohashmap.jsonmapvalue;
            Object error= vals.get("ErrorMessage");
            Error_Message  =error.toString();
            if(Error_Message.isEmpty()||Error_Message.toString()==""){
                Object userid = vals.get("user_id");
                DriverDocuments.user_id = userid.toString();
                Object driver_status = vals.get("driver_status");
                DriverDocuments.driver_status = driver_status.toString();
                Object vehicle_id = vals.get("vehicle_id");
                DriverDocuments.vehicle_id = vehicle_id.toString();
                Object vehicle_name = vals.get("vehicle_name");
                DriverDocuments.vehicle_name = vehicle_name.toString();
                Object registration_number = vals.get("registration_number");
                DriverDocuments.registration_number = registration_number.toString();
                SharedPreferences prefs =getDefaultSharedPreferences(driverDocuments.getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("driver_id", DriverDocuments.user_id);
                editor.putString("vehicle_id", DriverDocuments.vehicle_id);
                editor.putString("vehicle_name", DriverDocuments.vehicle_name);
                editor.putString("registration_number", DriverDocuments.registration_number);
                editor.putString("driver_status", DriverDocuments.driver_status);
                editor.commit();
                driverDocuments.gotodriverhome();
            }else {
                driverDocuments.showresponse(Error_Message);
            }
            //      HashMap<String,Object> results =
            //           new ObjectMapper().readValue(jsonObject,
            //JSONObject object = jsonObject.getJSONObject("IsSuccess");
           /* try {
                String IsSuccess = jsonObject.getString("IsSuccess");
                 Error_Message = jsonObject.getString("ErrorMessage");
                JSONArray user=jsonObject.getJSONObject("Response").getJSONArray("Users");
                for (int i = 0; i < user.length(); i++)
                {
                    String post_id = user.getJSONObject(i).getString("UserId");

                }

            }catch (Exception e){
                if(Error_Message!=null)
                mainActivity.showresponsefail(Error_Message);
            }*/
        } catch (JSONException e) {
       //     driverDocuments.showresponse(Error_Message+" "+e.getMessage());
        }
    }
    }




}
