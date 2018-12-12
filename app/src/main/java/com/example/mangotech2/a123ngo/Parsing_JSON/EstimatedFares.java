package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.ChangePassword;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.Signin;
import com.example.mangotech2.a123ngo.mapdirection;

import org.json.JSONArray;
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


public class EstimatedFares extends AsyncTask<String, Void, String> {
    mapdirection mapdirectin;
    String Error_Message="";
    public EstimatedFares(mapdirection mapdirectin){
        this.mapdirectin=mapdirectin;
    }
    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... strings) {
        try{

            URL url = new URL(DocumentEnums.ApiUrl+"/api/Booking/CalculateEstimatedFares");

            String user_id=strings[0];
            String timeInSeconds=strings[1];
            String distanceInKilometers=strings[2];
            String pickup_city=strings[3];
            String pickup_country=strings[4];
            String dropoff_city=strings[5];
            String dropoff_country=strings[6];
            String pickup_latitude=strings[7];
            String pickup_longitude=strings[8];
            String dropoff_latitude=strings[9];
            String dropoff_longitude=strings[10];
            String created_by=strings[11];
            String created_date=strings[12];
            String pickup_cityId=strings[13];
            String pickup_countryId=strings[14];
            String dropoff_cityId=strings[15];
            String dropoff_countryId=strings[16];

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("user_id", Integer.parseInt(user_id));
            postDataParams.put("timeInSeconds", timeInSeconds);
            postDataParams.put("distanceInKilometers", distanceInKilometers);
          //  postDataParams.put("pickup_city", pickup_city);
          //  postDataParams.put("pickup_country", pickup_country);
          //  postDataParams.put("dropoff_city", dropoff_city);
           // postDataParams.put("dropoff_country", dropoff_country);
            postDataParams.put("pickup_latitude", pickup_latitude);
            postDataParams.put("pickup_longitude", pickup_longitude);
            postDataParams.put("dropoff_latitude", dropoff_latitude);
            postDataParams.put("dropoff_longitude", dropoff_longitude);
            postDataParams.put("created_by", created_by);
            postDataParams.put("created_date", created_date);
            postDataParams.put("pickup_cityId", Integer.parseInt(pickup_cityId));
            postDataParams.put("pickup_countryId", Integer.parseInt(pickup_countryId));
            postDataParams.put("dropoff_cityId", Integer.parseInt(dropoff_cityId));
            postDataParams.put("dropoff_countryId", Integer.parseInt(dropoff_countryId));
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


        if (Error_Message != "") {
            mapdirectin.showresponse(Error_Message);
            Error_Message="";
        } else {
            try {


                JSONObject jsonObject = new JSONObject(result);
         /*   Jsontohashmap.jsonmapvalue= new HashMap<String, Object>();

            Map<String, Object> vals=Jsontohashmap.jsonToMap(jsonObject);
            vals=Jsontohashmap.jsonmapvalue;
            Object error= vals.get("ErrorMessage");
            Error_Message  =error.toString();
            if(Error_Message.isEmpty()||Error_Message.toString()==""){
                Object code = vals.get("base_price");
                String reg_code = code.toString();
                Object id = vals.get("UserId");
                String userId = id.toString();
                if (userId != null && reg_code != null) {
                    //     verificationcode.reg_code = reg_code;
                    //    verificationcode.gotoverificationcode();
                }

            }else {
            }
*/

                //      HashMap<String,Object> results =
                //           new ObjectMapper().readValue(jsonObject,
                // JSONObject object = jsonObject.getJSONObject("IsSuccess");
                try {
                    String IsSuccess = jsonObject.getString("IsSuccess");
                    Error_Message = jsonObject.getString("ErrorMessage");
                    JSONArray user = jsonObject.getJSONObject("Response").getJSONArray("VehicleFares");
                    for (int i = 0; i < user.length(); i++) {
                        mapdirection.fareval[i] = user.getJSONObject(i).getString("TotalPrice");
                        //   String post_id = user.getJSONObject(i).getString("TotalPrice");
                    }

                    mapdirection.tvfareestimate.setText(mapdirection.fareval[Integer.parseInt(mapdirection.vehicle_type_id)]);
                } catch (Exception e) {
                    Error_Message = e.getMessage().toString();
                }
            } catch (Exception e) {
                Error_Message = e.getMessage().toString();
            }
        }

    }

}
