package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.GetFreeRides;
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

public class ConfirmRegistrationSO extends AsyncTask<String, Void, String>{
    Verificationcode verificationcode;
    public ConfirmRegistrationSO(Verificationcode verificationcode){
        this.verificationcode=verificationcode;
    }
    protected void onPreExecute(){

    }
    String error_code;

    @Override
    protected String doInBackground(String... strings) {
        StringBuffer sb = new StringBuffer("");
        try{
            URL url = new URL(DocumentEnums.ApiUrl+"/api/Account/ConfirmRegistration");

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
     //   verificationcode.showresponse(result+"");
        if(result.equals(null)){
          //  verificationcode.showresponse(result+"");
        }else{
            if(Error_Message!=""){
                verificationcode.showresponse(Error_Message);
                Error_Message="";
            }else {
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
                    Object status = vals.get("Status");
                    String Status = status.toString();
                    if(Is_Active.equals("0")){
verificationcode.showresponse(Status);
                    }else {

                        verificationcode.showresponse(Status);
                        Object user_id = vals.get("user_id");
                        Object access_token = vals.get("access_token");
                        Signin.user_id = user_id.toString();
                        Signin.access_token = access_token.toString();
                        Object first_names = vals.get("first_name");
                        String first_name = first_names.toString();
                        Object email_addresss = vals.get("email_address");
                        String email_address = email_addresss.toString();
                        Object phone_nums = vals.get("phone_num");
                        String phone_num = phone_nums.toString();
                        Object promotion_codes = vals.get("referral_code");
                        String promotion_code = promotion_codes.toString();
                        GetFreeRides.promotion_code=promotion_code;
                        Signin.userName=first_name;
                        Signin.emailaddress=email_address;
                        Signin.phonenumber=phone_num;
                        verificationcode.signindatatopreferences();
                        verificationcode.verficationcodeaccepted();
                    }
                }else{

                 //   verificationcode.showresponse(result+"");
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
                verificationcode.showresponse(Error_Message +""+e.toString());
            }
        }
    }
    }
}
