package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.DriverBooking;
import com.example.mangotech2.a123ngo.DriverHome;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.HomeMain;
import com.example.mangotech2.a123ngo.Signin;
import com.example.mangotech2.a123ngo.Splash;

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


public class UpdateDriverLocation extends AsyncTask<String, Void, String> {

    String Error_Message="";
    static String current_location_latitude,current_location_longitude;
    public UpdateDriverLocation(){

    }
    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... strings) {
        StringBuffer sb = new StringBuffer("");
        if(isconnected()==true && strings[0]!="0"&&strings[1]!="0") {
        try{

            URL url = new URL(DocumentEnums.ApiUrl+"/api/Vehicle/UpdateVehicleLocation");

            current_location_latitude=strings[0];
            current_location_longitude=strings[1];
            String vehicle_id=strings[2];
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("current_location_latitude", current_location_latitude);
            postDataParams.put("current_location_longitude", current_location_longitude);
            postDataParams.put("vehicle_id", Integer.parseInt(vehicle_id));

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
              //  Error_Message="False : "+responseCode;
                return sb.toString();
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

        }}else {
        }
        return sb.toString();
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

        if(Error_Message!=""){
        }
/*
        try {
            JSONObject jsonObject = new JSONObject(result);
            Jsontohashmap.jsonmapvalue= new HashMap<String, Object>();

            Map<String, Object> vals=Jsontohashmap.jsonToMap(jsonObject);
            vals=Jsontohashmap.jsonmapvalue;
            Object error= vals.get("ErrorMessage");
            Error_Message  =error.toString();
        } catch (JSONException e) {
            Error_Message=e.getMessage().toString();
        }
  */
    }

    public  boolean isconnected(){
        boolean isConnected=false;
        try{
        ConnectivityManager conMgr = (ConnectivityManager) Splash.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
            isConnected=true;
            // notify user you are online

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            isConnected=false;
            // notify user you are not online
        }
        }catch(Exception e){

        }
        return  isConnected;
    }



}
