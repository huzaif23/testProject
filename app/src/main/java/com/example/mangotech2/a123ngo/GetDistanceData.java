package com.example.mangotech2.a123ngo;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.mangotech2.a123ngo.mapdirection.Dropoffloc;


public class GetDistanceData extends AsyncTask<String,String,String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration, distance;
    LatLng originlatLng,destinationlatlng;
    private int count;

    public GetDistanceData(){
    }

@Override
    protected String doInBackground(String... objects) {
        try {
            url = (String)objects[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        try {

            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonObject1 = (JSONArray) jsonObject.get("rows");
            JSONObject jsonObject2 = (JSONObject)jsonObject1.get(0);
            JSONArray jsonObject3 = (JSONArray)jsonObject2.get("elements");
            JSONObject elementObj = (JSONObject) jsonObject3.get(0);
            JSONObject distanceObj = (JSONObject)elementObj.get("distance");
            String distance = distanceObj.getString("text");
            mapdirection.strdistance=distance.replace(" km", "").trim();
        //    mapdirection.strdistance=distance.replace(" m", "").trim();
            JSONObject durationObj = (JSONObject)elementObj.get("duration");
            String duration = durationObj.getString("text");
         //   mapdirection.strtime=duration.replace(" mins", "").trim();

            double value = Double.parseDouble(duration.replace(" mins", "").trim());
         value=value*60;
            mapdirection.strtime=String.valueOf(value);
        }catch (Exception e){
            String error=e.getMessage();
        }
    }
}

