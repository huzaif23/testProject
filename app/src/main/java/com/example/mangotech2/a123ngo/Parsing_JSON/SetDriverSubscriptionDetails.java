package com.example.mangotech2.a123ngo.Parsing_JSON;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mangotech2.a123ngo.DriverSubscription;
import com.example.mangotech2.a123ngo.Enum.DocumentEnums;
import com.example.mangotech2.a123ngo.Signin;
import com.example.mangotech2.a123ngo.VehicleDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class SetDriverSubscriptionDetails extends AsyncTask<String, Void, String> {
    DriverSubscription driverSubscription;
    String Error_Message="";
    public SetDriverSubscriptionDetails(DriverSubscription driverSubscription){
        this.driverSubscription=driverSubscription;
        Error_Message="";


    }
    protected void onPreExecute(){

    }

    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "162.241.191.184";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "docftp";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="vKsb13&4";
    @Override
    protected String doInBackground(String... strings) {

        InputStream inputStream = null;
        String result = "";
        try{
            it.sauronsoftware.ftp4j.FTPClient client = new it.sauronsoftware.ftp4j.FTPClient();
            try {

                client.connect(FTP_HOST,21);
                client.login(FTP_USER, FTP_PASS);
                client.setType(it.sauronsoftware.ftp4j.FTPClient.TYPE_BINARY);
                client.changeDirectory("");
for(int i=0;i<1;i++) {
    File filename = new File(driverSubscription.fileloc + "/" + driverSubscription.uid[i]+".jpg");
    //    client.upload(fileName, new MyTransferListener());
    client.upload(filename);
}
File filename = new File(driverSubscription.fileloc + "/" + driverSubscription.uid[0]+".jpg");
                //    client.upload(fileName, new MyTransferListener());
                client.upload(filename);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    client.disconnect(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }



          //  String url = new String("http://192.168.15.140:8088/api/Vehicle/RegisterVehicle");
            URL url = new URL(DocumentEnums.ApiUrl+"/api/Driver/SetDriverSubscriptionDetails");
           String  subscription_url=strings[0];
           String  subscription_amount=strings[1];
           String  user_id=strings[2];

            JSONObject postDataParams = new JSONObject();
           postDataParams.put("user_id",user_id);
           postDataParams.put("subscription_amount",subscription_amount);
           postDataParams.put("subscription_url",subscription_url+".jpg");
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setRequestProperty("Authorization","Bearer "+ Signin.access_token);
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

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

        if(Error_Message!=""){
            driverSubscription.showresponse(Error_Message);
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
                driverSubscription.showresponse("Thanks for uploading documents after verification you will be subscribed");
                driverSubscription.gotohome();
                }else {
                driverSubscription.showresponse(Error_Message);
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
       //     driverSubscription.showresponse(Error_Message+" "+e.getMessage());
        }
    }
    }




}
