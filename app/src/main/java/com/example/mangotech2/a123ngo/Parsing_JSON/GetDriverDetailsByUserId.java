package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mangotech2.a123ngo.DriverDetails;
import com.example.mangotech2.a123ngo.DriverDocuments;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.Home;
import com.example.mangotech2.a123ngo.HomeMain;
import com.example.mangotech2.a123ngo.Signin;
import com.example.mangotech2.a123ngo.VehicleDetail;

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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class GetDriverDetailsByUserId extends AsyncTask<String, Void, String> {
    HomeMain homeMain;
    String Error_Message="";
    public GetDriverDetailsByUserId(HomeMain homeMain){
        this.homeMain=homeMain;
        Error_Message="";


    }
    protected void onPreExecute(){

    }
    String vehicle_type_id,vehicle_owner_id,vehicle_name,registration_number,vehicle_model,vehicle_color,driver_status,nic_number,
            user_id,is_active,created_by,Driving_lisence_back,driving_lisence_front,letterof_concert_from_owner,license,
            nic_back,nic_front,vehicle_tax_paper;

    String latitude,longitude;

    @Override
    protected String doInBackground(String... strings) {

        StringBuffer sb = new StringBuffer("");
        URL url = null;
try{
          //  String url = new String("http://192.168.15.140:8088/api/Vehicle/RegisterVehicle");

            url = new URL(DocumentEnums.ApiUrl+"/api/Driver/GetDriverDetailsByUserId");

        user_id=strings[0];


            JSONObject postDataParams = new JSONObject();
            postDataParams.put("user_id",user_id);
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
    return sb.toString();
        } catch (IOException e1) {
            Error_Message=DocumentEnums.interneterror;
    return sb.toString();
        } catch (JSONException e1) {
    return sb.toString();
        } catch (Exception e) {
    return sb.toString();
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
            homeMain.showresponse(Error_Message);
            Error_Message="";
            homeMain.gotodriverdetails();
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
                SharedPreferences prefs =getDefaultSharedPreferences(homeMain.getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("driver_id", DriverDocuments.user_id);
                editor.putString("vehicle_id", DriverDocuments.vehicle_id);
                editor.putString("vehicle_name", DriverDocuments.vehicle_name);
                editor.putString("registration_number", DriverDocuments.registration_number);
                editor.putString("driver_status", DriverDocuments.driver_status);
                editor.commit();
                homeMain.checkdriverbooking();
            }else {
                homeMain.gotodriverdetails();
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
            homeMain.gotodriverdetails();
       //     driverDocuments.showresponse(Error_Message+" "+e.getMessage());
        }
    }
    }




}
