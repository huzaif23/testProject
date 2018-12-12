package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.DriverDocuments;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;
import com.example.mangotech2.a123ngo.GetFreeRides;
import com.example.mangotech2.a123ngo.HomeMain;
import com.example.mangotech2.a123ngo.MainActivity;
import com.example.mangotech2.a123ngo.SigniUpSignIn;
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
import java.io.UnsupportedEncodingException;
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

/**
 * Created by mangotech2 on 10/16/2017.
 */

public class SignInJsonParsing extends AsyncTask<String, Void, String>{
    Signin signin;
    public   SignInJsonParsing(Signin signin){
        this.signin=signin;
    }
    protected void onPreExecute(){
        signin.loading();
    }
    String error_code;

    @Override
    protected String doInBackground(String... strings) {
        StringBuffer sb = new StringBuffer("");
        try{
            URL url = new URL(DocumentEnums.ApiUrl+"/api/Account/Login");

            //  URL url = new URL("http://123ngo.somee.com/api/Account/ChangePassword");

            String UserName=strings[0];
            String Password=strings[1];
            JSONObject postDataParams = new JSONObject();

            postDataParams.put("UserName", UserName);
            postDataParams.put("Password", Password);
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
                return new String("false : "+responseCode);
            }

        } catch (ProtocolException e1) {
            //  e1.printStackTrace();
            return sb.toString();
        } catch (IOException e1) {
            Error_Message="Internet Connection Problem";
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
    String Error_Message;
    @Override
    protected void onPostExecute(String result) {
        if(!result.equals(null)){
            signin.loaded();
            if(Error_Message==null){
                Error_Message="";
            }

            if (Error_Message != "") {
                signin.showresponse(Error_Message);
                Error_Message="";
            } else {
            if(result.equals(null)){
        //        signin.showresponse(result+"");
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Jsontohashmap.jsonmapvalue= new HashMap<String, Object>();
                    Map<String, Object> vals=Jsontohashmap.jsonToMap(jsonObject);
                    vals=Jsontohashmap.jsonmapvalue;
                    Object error= vals.get("ErrorMessage");
                    Error_Message=String.valueOf(error).toString();
                    if(Error_Message.isEmpty()||Error_Message.toString()=="") {
                        Object IsActive = vals.get("IsActive");
                        String Is_Active = IsActive.toString();
                        if(Is_Active.equals("0")||Is_Active.equals("false")){
                            Object status = vals.get("Status");
                            String Status = String.valueOf(status.toString());
                            Object user_id = vals.get("UserId");
                            signin.user_id = user_id.toString();
                            MainActivity.userid=user_id.toString();
                            signin.showresponse(Status);
                            signin.verifyuraccount();
                        }else {

                            Object user_id = vals.get("user_id");
                            Object access_token = vals.get("access_token");
                            signin.user_id = user_id.toString();
                            signin.access_token = access_token.toString();
                            Object first_names = vals.get("first_name");
                            String first_name = first_names.toString();
                            Object email_addresss = vals.get("email_address");
                            String email_address = email_addresss.toString();
                            Object phone_nums = vals.get("phone_num");
                            String phone_num = phone_nums.toString();
                            Object promotion_codes = vals.get("referral_code");
                            String promotion_code = promotion_codes.toString();
                            GetFreeRides.promotion_code=promotion_code;
                            signin.userName=first_name+"";
                            signin.emailaddress=email_address;
                            signin.phonenumber=phone_num;
                            Object isdriver = vals.get("is_driver");
                            String is_driver=String.valueOf(isdriver);
                            if(is_driver.equals("true")){
                                Object driver_status = vals.get("driver_status");
                                DriverDocuments.driver_status = driver_status.toString();
                                Object vehicle_name = vals.get("vehicle_name");
                                DriverDocuments.vehicle_name = vehicle_name.toString();
                                Object vehicle_id = vals.get("vehicle_id");
                                DriverDocuments.vehicle_id = vehicle_id.toString();
                                Object vehicle_type_id = vals.get("vehicle_type_id");
                                DriverDocuments.vehicle_type_id = vehicle_type_id.toString();
                                Object registration_number = vals.get("registration_number");
                                DriverDocuments.registration_number = registration_number.toString();
                                DriverDocuments.user_id=signin.user_id;
                                DriverDocuments.driver_id=signin.user_id;
                                SharedPreferences prefs =getDefaultSharedPreferences(signin.getApplicationContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("driver_id", DriverDocuments.driver_id);
                                editor.putString("vehicle_id", DriverDocuments.vehicle_id);
                                editor.putString("vehicle_name", DriverDocuments.vehicle_name);
                                editor.putString("registration_number", DriverDocuments.registration_number);
                                editor.putString("driver_status", DriverDocuments.driver_status);
                                editor.putString("vehicle_type_id", DriverDocuments.vehicle_type_id);
                                editor.commit();
                            }

                            signin.loginaccepted();
                        }
                    }else{

                        signin.showresponse(Error_Message +"");
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
                    signin.showresponse(Error_Message +""+e.toString());
                }
            }
            }
        }else{
         //   signin.showresponse(result);
        }
    }
}
