package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.DriverBooking;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.Signin;
import com.example.mangotech2.a123ngo.TripConfirmed;
import com.example.mangotech2.a123ngo.mapdirection;

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


public class PassengerBookingConfirm extends AsyncTask<String, Void, String> {
    String Error_Message="";
    mapdirection mapdirections;
    public  PassengerBookingConfirm(mapdirection mapdirections){
        this.mapdirections=mapdirections;
        mapdirections.loading();
    }

    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... strings) {

        StringBuffer sb;
        sb = new StringBuffer("");
        try{
            URL url = new URL(DocumentEnums.ApiUrl+"/api/Booking/BookingConfirmedFromPassenger");

            String user_id=strings[0];
            String booking_status_id=strings[1];
            String created_date=strings[2];
            String CreatedBy=strings[3];
            String BookingDate=strings[4];
            String BookingTime=strings[5];
            String vehicleTypeId=strings[6];
            String PickupCity=strings[7];
            String PickupCountry=strings[8];
            String DropoffCity=strings[9];
            String DropoffCountry=strings[10];
            String PickupLatitude=strings[11];
            String PickupLongitude=strings[12];
            String DropoffLatitude=strings[13];
            String DropoffLongitude=strings[14];
            String EstimatedFares=strings[15];
            String DistanceInKm=strings[16];
            String PickUpLocationName=strings[17];
            String DropoffLocationName=strings[18];
            String PickupCityId=strings[19];
            String PickupCountryId=strings[20];
            String DropoffCityId=strings[21];
            String DropoffCountryId=strings[22];
          String   PromoCode=strings[23];
            String IsBookLater=strings[24];

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("user_id", Integer.parseInt(user_id));
            postDataParams.put("booking_status_id", Integer.parseInt(booking_status_id));
            postDataParams.put("created_date", created_date);
            postDataParams.put("CreatedBy", Integer.parseInt(CreatedBy));
            postDataParams.put("BookingDate", BookingDate);
            postDataParams.put("BookingTime", BookingTime);
            postDataParams.put("vehicleTypeId", Integer.parseInt(vehicleTypeId)+1);
            //      postDataParams.put("PickupCity", PickupCity);
            //      postDataParams.put("PickupCountry", PickupCountry);
            //      postDataParams.put("DropoffCity", DropoffCity);
            //      postDataParams.put("DropoffCountry", DropoffCountry);
            postDataParams.put("PickupLatitude", PickupLatitude);
            postDataParams.put("PickupLongitude", PickupLongitude);
            postDataParams.put("DropoffLatitude", DropoffLatitude);
            postDataParams.put("DropoffLongitude", DropoffLongitude);
            postDataParams.put("EstimatedFares", EstimatedFares);
            postDataParams.put("DistanceInKm", DistanceInKm);
            postDataParams.put("PickUpLocationName", PickUpLocationName);
            postDataParams.put("DropoffLocationName", DropoffLocationName);
            postDataParams.put("PickupCityId", Integer.parseInt(PickupCityId));
            postDataParams.put("PickupCountryId", Integer.parseInt(PickupCountryId));
            postDataParams.put("DropoffCityId", Integer.parseInt(DropoffCityId));
            postDataParams.put("DropoffCountryId", Integer.parseInt(DropoffCountryId));
            postDataParams.put("PromoCode", PromoCode);
            postDataParams.put("IsBookLater", Integer.parseInt(IsBookLater));
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

        //   mapdirections.loaded();
   //     mapdirections.showresponse(result+" ");
        if(Error_Message!=""){
            mapdirections.showresponse(Error_Message);
            Error_Message="";
        }else{

        try {
            JSONObject jsonObject = new JSONObject(result);
            Jsontohashmap.jsonmapvalue= new HashMap<String, Object>();
            Map<String, Object> vals=Jsontohashmap.jsonToMap(jsonObject);
            vals=Jsontohashmap.jsonmapvalue;
            Object error= vals.get("ErrorMessage");

            //  Error_Message  =error.toString();
            if(Error_Message.isEmpty()||Error_Message.toString()==""){
                Object BookingDetailId= vals.get("BookingDetailId");
                Object BookingStatusId= vals.get("BookingStatusId");
                //    String userId = id.toString();
                TripConfirmed.BookingDetailId=String.valueOf(BookingDetailId);
                TripConfirmed.BookingStatusId=String.valueOf(BookingStatusId);

                mapdirections.bookingconfirmed();

            }else {
                mapdirections.showresponse(Error_Message+" ");
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
            Error_Message=e.getMessage().toString();
        //    mapdirections.showresponse(result);
        }
    }}



}
