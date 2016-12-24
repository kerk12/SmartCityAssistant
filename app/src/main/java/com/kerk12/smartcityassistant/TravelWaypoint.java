package com.kerk12.smartcityassistant;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.NameList;

import java.util.concurrent.TransferQueue;

/**
 * Created by kerk12 on 12/24/16.
 */

public class TravelWaypoint {
    private String mName;
    private LatLng mLocation;
    private boolean isFinalDestination = false;

    public TravelWaypoint(String Name, LatLng Location){
        mName= Name;
        mLocation = Location;
    }

    public TravelWaypoint(String Name, LatLng Location, boolean isFinalDestination){
        mName= Name;
        mLocation = Location;
        this.isFinalDestination = isFinalDestination;
    }

    public boolean isFinalDestination(){
        return this.isFinalDestination;
    }

}
