package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.Signin;

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


public class SignOutDriver extends AsyncTask<String, Void, String> {

    String Error_Message="";
   static String user_id,Is_driver;
    public SignOutDriver(){

    }
    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... strings) {
        StringBuffer sb = new StringBuffer("");
        try{
            URL url = new URL(DocumentEnums.ApiUrl+"/api/Account/SignOut");
            user_id=strings[0];
            Is_driver=strings[1];
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("user_id",Integer.parseInt( user_id));
          postDataParams.put("Is_driver", Integer.parseInt(Is_driver));
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

        if (Error_Message != "") {

            Error_Message = "";
        } else {


            try {
                JSONObject jsonObject = new JSONObject(result);
                Jsontohashmap.jsonmapvalue = new HashMap<String, Object>();

                Map<String, Object> vals = Jsontohashmap.jsonToMap(jsonObject);
                vals = Jsontohashmap.jsonmapvalue;
                Object error = vals.get("ErrorMessage");
                Error_Message = error.toString();
            } catch (JSONException e) {
                Error_Message = e.getMessage().toString();
            }
        }
    }


}
