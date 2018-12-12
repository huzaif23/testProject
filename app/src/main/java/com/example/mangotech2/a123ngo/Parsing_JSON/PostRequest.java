package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.MainActivity;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.io.*;

import java.util.*;

import javax.net.ssl.HttpsURLConnection;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by mangotech2 on 10/13/2017.
 */

public class PostRequest extends AsyncTask<String, Void, String> {
    MainActivity mainActivity;
    String Error_Message="";
    public   PostRequest(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }
    protected void onPreExecute(){
        mainActivity.loading();
    }
    @Override
    protected String doInBackground(String... strings) {
        StringBuffer sb = new StringBuffer("");
        try{

            URL url = new URL(DocumentEnums.ApiUrl+"/api/Account/Register");

            String emailAddress=strings[0];
            String password=strings[1];
            String phoneNumber=strings[2];
            String firstName=strings[3];
            String lastName=strings[3];
            String imeiNo=strings[4];
            String FCMToken=strings[5];

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("EmailAddress", emailAddress);
            postDataParams.put("Password", password);
            postDataParams.put("PhoneNumber", phoneNumber);
            postDataParams.put("FirstName", firstName);
            postDataParams.put("LastName", lastName);
            postDataParams.put("ImeiNo", imeiNo);
            postDataParams.put("FCMToken",FCMToken);
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                Error_Message="False : "+responseCode;
                return new String("false : "+responseCode);

            }

        } catch (ProtocolException e1) {
            Error_Message=e1.getMessage();
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
        mainActivity.loaded();
        if(Error_Message==null){
            Error_Message="";
        }

        if (Error_Message != "") {
            mainActivity.showresponse(Error_Message);
        } else {

            try {
                JSONObject jsonObject = new JSONObject(result);
                Jsontohashmap.jsonmapvalue = new HashMap<String, Object>();

                Map<String, Object> vals = Jsontohashmap.jsonToMap(jsonObject);
                vals = Jsontohashmap.jsonmapvalue;
                Object error = vals.get("ErrorMessage");
                Error_Message = error.toString();
                if (Error_Message.isEmpty() || Error_Message.toString() == "") {
                    Object code = vals.get("RegistrationCode");
                    String reg_code = code.toString();
                    Object id = vals.get("UserId");
                    String userId = id.toString();
                    MainActivity.userid = userId;
                    Signin.user_id=userId;
                    Object emailAddress = vals.get("EmailAddress");
                    String EmailAddress = emailAddress.toString();
                    Object accessa_token = vals.get("access_token");
                    String access_token = accessa_token.toString();
          /*      SharedPreferences prefs =getDefaultSharedPreferences(mainActivity.getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("access_token", access_token);
             editor.commit();
            */
                    mainActivity.showresponse("UserId = " + userId + " Code=" + reg_code);
                    if (userId != null && reg_code != null) {
                        mainActivity.reg_code = reg_code;
                        mainActivity.gotoverificationcode();
                    }
                } else {
                    mainActivity.showresponse(Error_Message);
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
                mainActivity.showresponse(Error_Message + " " + e.getMessage());
            }
        }

    }

}
