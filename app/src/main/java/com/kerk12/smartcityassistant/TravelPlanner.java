package com.kerk12.smartcityassistant;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.message;

/**
 * Created by kerk12 on 12/24/16.
 */

public class TravelPlanner {

    private static List<TravelWaypoint> waypoints = null;
    private static TravelWaypoint finalDestination = null;

    public static class NoWaypointsSetException extends Exception{
        public NoWaypointsSetException() {
            super("No waypoints have been set.");
        }
    }

    private TravelPlanner() {

    }

    public static List<TravelWaypoint> getWaypoints(){
        if ( waypoints == null){
            waypoints = new ArrayList<TravelWaypoint>();
            //DEBUG ONLY
//            TravelWaypoint point1, point2, point3;
//            point1 = new TravelWaypoint("Point 1", new LatLng(12.123,12.123));
//            point2 = new TravelWaypoint("Point 1", new LatLng(12.123,12.123));
//            point3 = new TravelWaypoint("Point 1", new LatLng(12.123,12.123));
//            waypoints.add(point1);
//            waypoints.add(point2);
//            waypoints.add(point3);
        }
        return waypoints;
    }

    private static String LatLngAsString(LatLng latlng){
        double lat = latlng.latitude;
        double lng = latlng.longitude;

        return String.valueOf(lat)+","+String.valueOf(lng);
    }

    public static void AddWaypoint(TravelWaypoint waypoint){
        if (waypoint.isFinalDestination()) {
            if (finalDestination == null) {
                finalDestination = waypoint;
            } else {
                //TODO Exception handling...
            }
        } else {
            waypoints.add(waypoint);
        }
    }

    public static int getNumOfWaypoints(){
        return waypoints.size();
    }

    public static TravelWaypoint getOrigin() throws InstantiationException {
        if (getNumOfWaypoints() > 0){
            return waypoints.get(0);
        } else {
            throw new InstantiationException("No waypoints added.");
        }
    }

    public static boolean ExistsFinalDestination(){
        if (finalDestination == null){
            return false;
        } return true;
    }

    public static TravelWaypoint getFinalDestination() throws NoWaypointsSetException {
        if (finalDestination != null) {
            return finalDestination;
        } else {
            if (waypoints.size() > 2){
                return waypoints.get(waypoints.size() - 1);
            } else throw new NoWaypointsSetException();

        }

    }

    public static Map<String, String> makeHelperMap() throws NoWaypointsSetException {
        Map<String, String> mMap = new HashMap<String, String>();
        mMap.put("origin", LatLngAsString(waypoints.get(0).getLocation()));
        StringBuilder waypBuilder = new StringBuilder();
        boolean first = true;
        int limit;
        if (ExistsFinalDestination()) {
            limit = waypoints.size();
        } else {
            limit = waypoints.size() - 1;
        }
        if (waypoints.size() > 2) {
            for (int i = 1; i < limit; i++) {
                if (first) {
                    first = false;
                } else {
                    waypBuilder.append("|");
                }
                waypBuilder.append(LatLngAsString(waypoints.get(i).getLocation()));
            }
            mMap.put("waypoints", waypBuilder.toString());
        }
        if (ExistsFinalDestination()){
            mMap.put("destination", LatLngAsString(getFinalDestination().getLocation()));
        } else {
            mMap.put("destination", LatLngAsString(waypoints.get(limit).getLocation()));
        }
        return mMap;
    }

}
