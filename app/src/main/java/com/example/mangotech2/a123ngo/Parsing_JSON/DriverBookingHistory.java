package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.DriverHome;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.HomeMain;
import com.example.mangotech2.a123ngo.Signin;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


public class DriverBookingHistory extends AsyncTask<String, Void, String> {
    String Error_Message="";
    DriverHome driverHome;
    public DriverBookingHistory(DriverHome driverHome){
        this.driverHome=driverHome;

    }
    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... strings) {
        try{
            URL url = new URL(DocumentEnums.ApiUrl+"/api/Booking/DriverBookingHistory");

            //  URL url = new URL("http://123ngo.somee.com/api/Account/ChangePassword");

            String UserId=strings[0];
            String Counter=strings[1];

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("UserId", UserId);
            postDataParams.put("Counter", Counter);
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
        } catch (Exception e1) {
            return null;
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
            driverHome.showresponse(Error_Message);
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
                    JSONArray user = jsonObject.getJSONObject("Response").getJSONArray("Table");
                    for (int i = 0; i < user.length(); i++) {
                        //  mapdirection.fareval[i]= user.getJSONObject(i).getString("TotalPrice");
                        //   String post_id = user.getJSONObject(i).getString("TotalPrice");
                        //     MyTrip.booking_status.add(user.getJSONObject(i).getString("Status"));
                        driverHome.booking_date.add(user.getJSONObject(i).getString("booking_date"));
                        driverHome.booking_status_id.add(user.getJSONObject(i).getString("booking_status_id"));
                        driverHome.amount.add(user.getJSONObject(i).getString("amount"));
                        driverHome.pickup_latitude.add(user.getJSONObject(i).getString("pickup_latitude"));
                        driverHome.pickup_longitude.add(user.getJSONObject(i).getString("pickup_longitude"));
                        driverHome.dropoff_latitude.add(user.getJSONObject(i).getString("dropoff_latitude"));
                        driverHome.dropoff_longitude.add(user.getJSONObject(i).getString("dropoff_longitude"));

                    }
                //    driverHome.gotomytrip();
                } catch (Exception e) {
                //    driverHome.gotomytrip();
                    //     driverHome.showresponse(Error_Message);
                }
            } catch (Exception e) {
                Error_Message = e.getMessage().toString();
             //   driverHome.gotomytrip();
            }
        }
    }


}
