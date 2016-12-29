package com.kerk12.smartcityassistant;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.NameList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TransferQueue;

/**
 * Created by kerk12 on 12/24/16.
 */

public class TravelWaypoint {
    private String Name;
    private LatLng Location;
    //private boolean isFinalDestination = false;



    public LatLng getLocation() {
        return Location;
    }

    public void setLocation(LatLng location) {
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public TravelWaypoint(String Name, LatLng Location){
        this.Name= Name;
        this.Location = Location;
    }

}
