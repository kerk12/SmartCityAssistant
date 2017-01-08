package com.kerk12.smartcityassistant;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/**
 * Created by kerk12 on 12/24/16.
 */

public class TravelWaypoint {
    private String Name;
    private LatLng Location;
    //private boolean isFinalDestination = false;
    private Calendar ArrivalTime = null;


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

    public TravelWaypoint(String Name, LatLng Location, Calendar arrivalTime){
        this.Name= Name;
        this.Location = Location;
        this.ArrivalTime = arrivalTime;
    }

    public Calendar getArrivalTime() {
        return ArrivalTime;
    }

    public String getParsedArrivalTime(){
        if (getArrivalTime() == null){
            return null;
        } else {
            String s;
            if (getArrivalTime().get(Calendar.MINUTE) < 10) {
                s = String.valueOf(getArrivalTime().get(Calendar.HOUR_OF_DAY)) + ":0" + String.valueOf(getArrivalTime().get(Calendar.MINUTE));
            } else {
                s = String.valueOf(getArrivalTime().get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(getArrivalTime().get(Calendar.MINUTE));
            }
            return s;
        }
    }


    public void setArrivalTime(Calendar arrivalTime) {
        ArrivalTime = arrivalTime;
    }
}
