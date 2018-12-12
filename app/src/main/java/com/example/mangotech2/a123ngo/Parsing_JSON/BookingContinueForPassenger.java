package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.DriverBooking;
import com.example.mangotech2.a123ngo.DriverRoute;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.HomeMain;
import com.example.mangotech2.a123ngo.Signin;
import com.example.mangotech2.a123ngo.Splash;
import com.example.mangotech2.a123ngo.TripConfirmed;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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

/**
 * Created by mangotech2 on 10/13/2017.
 */

public class BookingContinueForPassenger extends AsyncTask<String, Void, String> {
    Splash splash;
    String Error_Message="";
    public BookingContinueForPassenger(Splash splash){
        this.splash=splash;
    }
    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... strings) {
        StringBuffer sb = new StringBuffer("");
        try{

            URL url = new URL(DocumentEnums.ApiUrl+"/api/Booking/BookingContinueForPassenger");
            String user_id=strings[0];
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("user_id", user_id);
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ Signin.access_token);
            conn.addRequestProperty("Authorization","Bearer "+Signin.access_token);
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
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
                splash.gotohome();
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
        } catch (Exception e1) {
            return sb.toString();
        }



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

        //  splash.gotohome();
        if(result==null||result==""){
            splash.gotohome();
        }else{
            if(Error_Message!=""){
                splash.showresponse(Error_Message);
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
                    TripConfirmed.BookingDetailId = vals.get("booking_details_id").toString();
                    TripConfirmed.driver_id = vals.get("driver_id").toString();
                    TripConfirmed.vehicle_id = vals.get("vehicle_id").toString();
                    TripConfirmed.first_name = vals.get("first_name").toString();
                    TripConfirmed.phone_num = vals.get("phone_num").toString();
                    TripConfirmed.vehicle_name = vals.get("vehicle_name").toString();
                    TripConfirmed.registration_number = vals.get("registration_number").toString();
                    TripConfirmed.vehicle_color = vals.get("vehicle_color").toString();
                    TripConfirmed.vehicle_model = vals.get("vehicle_model").toString();
                    TripConfirmed.current_location_latitude = vals.get("current_location_latitude").toString();
                    TripConfirmed.current_location_longitude = vals.get("current_location_longitude").toString();
                    TripConfirmed.pickup_latitude = vals.get("pickup_latitude").toString();
                    TripConfirmed.pickup_longitude = vals.get("pickup_longitude").toString();
                    TripConfirmed.dropoff_latitude = vals.get("dropoff_latitude").toString();
                    TripConfirmed.dropoff_longitude = vals.get("dropoff_longitude").toString();
                    String ridestarttime = vals.get("ride_start_time").toString();


                    splash.gototripconfirmed();
                    if(ridestarttime=="null"||ridestarttime==null||ridestarttime==""){
                        if(TripConfirmed.contexttripconfirmed!=null) {
                            TripConfirmed.contexttripconfirmed.directionpath();
                        }
                    }else{

                        if(TripConfirmed.contexttripconfirmed!=null) {
                            TripConfirmed.contexttripconfirmed.directionpathtodropoff();
                        }
                    }
                }else {
                    splash.gotohome();
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
                splash.showresponse(Error_Message+" "+e.getMessage());
            }
        }
        }
    }



}
