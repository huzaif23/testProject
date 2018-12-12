package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.*;
import com.example.mangotech2.a123ngo.DriverBookingConfirmed;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;

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


public class UpdateRideStatus extends AsyncTask<String, Void, String> {
    DriverRoute driverRoute;
    String Error_Message="";
    public UpdateRideStatus(com.example.mangotech2.a123ngo.DriverRoute driverRoute){
        this.driverRoute=driverRoute;
        driverRoute.loading();
    }
    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... strings) {

        StringBuffer sb = new StringBuffer("");
        try{
            URL url = new URL(DocumentEnums.ApiUrl+"/api/Booking/UpdateRideStatus");

            String user_id=strings[0];
            String BookingDetailId=strings[1];
            String BookingStatusId=strings[2];
            String RideStartTime=strings[3];
            String RideEndTime=strings[3];
            String UpdatedBy=strings[3];
            String UpdatedDate=strings[3];

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("user_id", Integer.parseInt(user_id));
            postDataParams.put("BookingDetailId", Integer.parseInt(BookingDetailId));
            postDataParams.put("BookingStatusId", Integer.parseInt(BookingStatusId));
            postDataParams.put("RideStartTime", RideStartTime);
            postDataParams.put("RideEndTime", RideEndTime);
            postDataParams.put("UpdatedBy", UpdatedBy);
            postDataParams.put("UpdatedDate", UpdatedDate);

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
            return ""+sb;
        } catch (IOException e1) {
            Error_Message=DocumentEnums.interneterror;
            return ""+sb;
        } catch (JSONException e1) {
            return ""+sb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ""+sb.toString();

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
        //     driverRoute.showresponse(result);


        if(Error_Message==null){
            Error_Message="";
        }

        if (Error_Message != "") {
            driverRoute.showresponse(Error_Message);
            Error_Message = "";
        } else {

            try {
                JSONObject jsonObject = new JSONObject(result);
                Jsontohashmap.jsonmapvalue = new HashMap<String, Object>();
                Map<String, Object> vals = Jsontohashmap.jsonToMap(jsonObject);
                vals = Jsontohashmap.jsonmapvalue;
                Object error = vals.get("ErrorMessage");
                Error_Message = error.toString();
                if (Error_Message.isEmpty() || Error_Message.toString() == "") {

                    driverRoute.dropOffLatitude = vals.get("DropOffLatitude").toString();
                    driverRoute.dropOffLongitude = vals.get("DropOffLongitude").toString();
                    driverRoute.ridestarted();
                } else {
                    driverRoute.showresponse(Error_Message);
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
                Error_Message = e.getMessage().toString();
            } catch (Exception e) {
                Error_Message = e.getMessage().toString();
            }
        }
    }


}
