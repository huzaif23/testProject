package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.DriverBooking;
import com.example.mangotech2.a123ngo.DriverDocuments;
import com.example.mangotech2.a123ngo.DriverHome;
import com.example.mangotech2.a123ngo.DriverRoute;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.Signin;
import com.google.android.gms.drive.Drive;

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


public class DriverBookingConfirmed extends AsyncTask<String, Void, String> {
    String Error_Message="";
    DriverBooking driverBooking;
    public DriverBookingConfirmed(DriverBooking driverBooking){
        this.driverBooking=driverBooking;
    }

    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... strings) {
        StringBuffer sb = new StringBuffer("");
        try{

            URL url = new URL(DocumentEnums.ApiUrl+"/api/Booking/BookingConfirmedByNearByDriver");

            String booking_detail_id=strings[0];
            String driver_id=strings[1];
            String vehicle_id=strings[2];

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("booking_detail_id", Integer.parseInt(booking_detail_id));
            postDataParams.put("driver_id", Integer.parseInt(driver_id));
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
            String exc=e1.getMessage().toString();
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

        //   driverBooking.showresponse(result);
        if(Error_Message!=""){

            driverBooking.showresponse(Error_Message);
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
                Object pickup_latitudes = vals.get("pickup_latitude");
                String pickup_latitude = pickup_latitudes.toString();
                driverBooking.PickupLatitude=pickup_latitude;
                Object pickup_longitudes = vals.get("pickup_longitude");
                String pickup_longitude = pickup_longitudes.toString();
                DriverBooking.PickupLongitude=pickup_longitude;
                Object PassengerNames = vals.get("PassengerName");
                String PassengerName = PassengerNames.toString();
                DriverBooking.PassengerName=PassengerName;
                Object PassengerPhoneNumbers = vals.get("PassengerPhoneNumber");
                String PassengerPhoneNumber = PassengerPhoneNumbers.toString();
                DriverBooking.PassengerPhoneNo=PassengerPhoneNumber;
                if(!DriverBooking.IsBookLater.equals("1")){
                    DriverRoute.fromtrips=false;
                    driverBooking.bookinconfirmed();
                }else{
                    Object booking_dates = vals.get("booking_date");
                    String booking_date = booking_dates.toString();
                    DriverBooking.booking_date=booking_date;
                    Object booking_times = vals.get("booking_time");
                    String booking_time = booking_times.toString();
                    DriverBooking.booking_time=booking_time;
                    Object dropoff_latitudes = vals.get("dropoff_latitude");
                    String dropoff_latitude = dropoff_latitudes.toString();
                    DriverBooking.dropoff_latitude=dropoff_latitude;
                    Object dropoff_longitudes = vals.get("dropoff_longitude");
                    String dropoff_longitude = dropoff_longitudes.toString();
                    DriverBooking.dropoff_longitude=dropoff_longitude;

                    Object booking_details_ids = vals.get("booking_details_id");
                    String booking_details_id = booking_details_ids.toString();
                    DriverBooking.booking_details_id=booking_details_id;

                    DriverHome.Intitializedrivermytrpobject();

                    DriverHome.contextdriverhome.sbooking_date.add(booking_date);
                    DriverHome.contextdriverhome.sbooking_status_id.add(booking_details_id);
                    DriverHome.contextdriverhome.sbooking_time.add(booking_time);
                    DriverHome.contextdriverhome.spickup_latitude.add(pickup_latitude);
                    DriverHome.contextdriverhome.spickup_longitude.add(pickup_longitude);
                    DriverHome.contextdriverhome.sdropoff_latitude.add(dropoff_latitude);
                    DriverHome.contextdriverhome.sdropoff_longitude.add(dropoff_longitude);
                    DriverHome.contextdriverhome.sfirst_name.add(PassengerName);
                    DriverHome.contextdriverhome.sphone_num.add(PassengerPhoneNumber);



                    driverBooking.laterbookinconfirmed();
                }
                  }else {
                driverBooking.showresponse(Error_Message);
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
            Error_Message=DocumentEnums.interneterror;
          //  driverBooking.showresponse(e.getMessage().toString()+" ");
            driverBooking.showresponse(Error_Message);
        }}
    }



}
