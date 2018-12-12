package com.example.mangotech2.a123ngo;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;


public class GetDirectionsDataforvehicle extends AsyncTask<Object,String,String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration, distance;
    LatLng latLng;
    private int count;
    /*
        public GetDirectionsData(int count){
            this.count = count;
        }
      */
    @Override
    protected String doInBackground(Object... objects) {
        try {
            mMap = (GoogleMap)objects[0];
            url = (String)objects[1];
            latLng = (LatLng)objects[2];



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
            String[] directionsList;
            DataParser parser = new DataParser();
            directionsList = parser.parseDirections(s);
            displayDirection(directionsList);
        }catch (Exception e){}
    }
    List<LatLng> edgepoints;
    public void displayDirection(String[] directionsList) {
        edgepoints = new ArrayList<>();
        try {
            int count = directionsList.length;
            for (int i = 0; i < count; i++) {
                PolylineOptions options = new PolylineOptions();
                options.color(Color.DKGRAY);
                options.width(10);
                options.addAll(PolyUtil.decode(directionsList[i]));
//                edgepoints.addAll(PolyUtil.decode(directionsList[i]));
TripConfirmed.vehiclerunninglatlng.addAll(PolyUtil.decode(directionsList[i]));

                //    mMap.addPolyline(options);
            }
            if(TripConfirmed.contexttripconfirmed!=null) {

                if(!TripConfirmed.contexttripconfirmed.thread.isAlive()){
                TripConfirmed.contexttripconfirmed. thread.start();
                TripConfirmed.contexttripconfirmed. showshortresponse("threadstarted");
            }
            }

        } catch (Exception e) {

        }
    }
}

