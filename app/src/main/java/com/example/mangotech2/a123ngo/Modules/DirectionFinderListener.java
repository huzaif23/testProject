package com.example.mangotech2.a123ngo.Modules;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import com.example.mangotech2.a123ngo.Modules.Route;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);

    void onMarkerDrag(Marker arg0);

    void onMarkerDragEnd(Marker arg0);

    void onMarkerDragStart(Marker arg0);
}
