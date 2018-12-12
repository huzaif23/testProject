package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.ForgetChangePassword;
import com.example.mangotech2.a123ngo.ForgetPasswordVerification;
import com.example.mangotech2.a123ngo.Signin;
import com.example.mangotech2.a123ngo.Verificationcode;

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
 * Created by mangotech2 on 10/16/2017.
 */

public class ForgetPasswordConfirm extends AsyncTask<String, Void, String>{
    ForgetPasswordVerification forgetPasswordVerification;
    public ForgetPasswordConfirm(ForgetPasswordVerification forgetPasswordVerification){
        this.forgetPasswordVerification=forgetPasswordVerification;
    }
    protected void onPreExecute(){

    }
    String error_code;

    @Override
    protected String doInBackground(String... strings) {
        StringBuffer sb = new StringBuffer("");
        try{
            URL url = new URL(DocumentEnums.ApiUrl+"/api/Account/ForgetPasswordConfirm");

            //  URL url = new URL("http://123ngo.somee.com/api/Account/ChangePassword");

            String RegistrationCode=strings[0];
            String UserId=strings[1];
            JSONObject postDataParams = new JSONObject();

            postDataParams.put("RegistrationCode", RegistrationCode);
            postDataParams.put("UserId", Integer.valueOf(UserId));
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
          //  conn.setRequestProperty("Authorization","Bearer "+Signin.access_token);
          //  conn.addRequestProperty("Authorization","Bearer "+Signin.access_token);
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
    String Error_Message;
    @Override
    protected void onPostExecute(String result) {
        if(Error_Message==null){
            Error_Message="";
        }

        Log.d("onpost",result,null);
      //  forgetPasswordVerification.showresponse(result+"");
        if(result.equals(null)){
         //   forgetPasswordVerification.showresponse(result+"");
        }else{
            if(Error_Message!=""){
                forgetPasswordVerification.showresponse(Error_Message);
                Error_Message="";
            }else{
            try {
                JSONObject jsonObject = new JSONObject(result);
                Jsontohashmap.jsonmapvalue= new HashMap<String, Object>();
                Map<String, Object> vals=Jsontohashmap.jsonToMap(jsonObject);
                vals=Jsontohashmap.jsonmapvalue;
                Object error= vals.get("ErrorMessage");
                Error_Message=String.valueOf(error).toString();
                if(Error_Message.isEmpty()||Error_Message.toString()=="") {
                        Object user_id = vals.get("UserId");
                    Object access_token = vals.get("AccessToken");
                    Signin.user_id = user_id.toString();
                        Signin.access_token = access_token.toString();
                    Object OldPassword = vals.get("OldPassword");
                    ForgetChangePassword.oldpassword = OldPassword.toString();
                        forgetPasswordVerification.verficationcodeaccepted();
                }else{
                    forgetPasswordVerification.showresponse(Error_Message+"");
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
                forgetPasswordVerification.showresponse(Error_Message +""+e.toString());
            }
        }
    }}
}
