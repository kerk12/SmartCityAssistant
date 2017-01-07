package com.kerk12.smartcityassistant;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.message;

/**
 * Created by kerk12 on 12/24/16.
 */

public class TravelPlanner {

    private static List<TravelWaypoint> waypoints = null;
    //private static TravelWaypoint finalDestination = null;
    private static String TransitMode = "driving";

    public static final String DRIVING = "driving";
    public static final String WALKING = "walking";
    public static final String BICYCLING = "bicycling";
    public static final String TRANSIT = "transit";


    private static List<LatLng> intermediatePoints = null;
    private static List<String> instructions = null;


    public static class NoWaypointsSetException extends Exception{
        public NoWaypointsSetException() {
            super("No waypoints have been set.");
        }
    }

    private TravelPlanner() {

    }

    /**
     * Initializes the list that the TravelPlanner uses. Needs to be called prior to execution of every method in the class.
     */
    private static void initializeTravelPlanner(){
        if ( waypoints == null) {
            waypoints = new ArrayList<TravelWaypoint>();
        }
    }

    /**
     * Method to get the Waypoint List.
     *
     * If a final destination is set, appends it to the list.
     *
     * @return The list of waypoints.
     */
    public static List<TravelWaypoint> getWaypoints(){
            initializeTravelPlanner();
            //DEBUG ONLY
//            TravelWaypoint point1, point2, point3;
//            point1 = new TravelWaypoint("Point 1", new LatLng(12.123,12.123));
//            point2 = new TravelWaypoint("Point 1", new LatLng(12.123,12.123));
//            point3 = new TravelWaypoint("Point 1", new LatLng(12.123,12.123));
//            waypoints.add(point1);
//            waypoints.add(point2);
//            waypoints.add(point3);
//        if (ExistsFinalDestination()) {
//            List<TravelWaypoint> newWaypList = waypoints;
//            newWaypList.add(finalDestination);
//            return newWaypList;
//        } else {
            return waypoints;
//        }
    }

    /**
     * Returns the string form of the LatLng object passed.
     * Example:
     *     (new LatLng(12.345,45.678)
     *     the output will be:
     *     "12.345,45.678"
     *
     * @param latlng The LatLng object for conversion.
     * @return A string with the latlng value, see example...
     */
    private static String LatLngAsString(LatLng latlng){
        double lat = latlng.latitude;
        double lng = latlng.longitude;

        return String.valueOf(lat)+","+String.valueOf(lng);
    }

    /**
     * Adds a waypoint to the list
     * @param waypoint The TravelWaypoint object.
     */
    public static void AddWaypoint(TravelWaypoint waypoint){
        waypoints.add(waypoint);
    }

    /**
     * Returns the number of the waypoints in the list
     * @return # of waypoints.
     */
    public static int getNumOfWaypoints(){
        initializeTravelPlanner();
        return waypoints.size();
    }

    /**
     * Return the origin waypoint
     * @return The first TravelWaypoint on the list
     * @throws NoWaypointsSetException is thrown when no waypoints have been set in the list.
     */
    public static TravelWaypoint getOrigin() throws NoWaypointsSetException {
        if (getNumOfWaypoints() > 0){
            return waypoints.get(0);
        } else {
            throw new NoWaypointsSetException();
        }
    }


    /**
     * Get the final destination
     * @return The final destination TravelWaypoint
     * @throws NoWaypointsSetException When no waypoint exists on the list
     */
    public static TravelWaypoint getFinalDestination() throws NoWaypointsSetException {
            if (waypoints.size() > 2){
                return waypoints.get(waypoints.size() - 1);
            } else throw new NoWaypointsSetException();

        }



    public static Map<String, String> makeHelperHashMap() throws NoWaypointsSetException {
        Map<String, String> mMap = new HashMap<String, String>();
        mMap.put("origin", LatLngAsString(waypoints.get(0).getLocation()));
        StringBuilder waypBuilder = new StringBuilder();
        boolean first = true;
        int limit;

        limit = waypoints.size() - 1;

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
            mMap.put("destination", LatLngAsString(waypoints.get(limit).getLocation()));

        return mMap;
    }

    public static void SetTravelMode(String TravelModeNew){
        TransitMode = TravelModeNew;
    }

    public static List<MarkerOptions> getRouteMarkers(){
        List<MarkerOptions> mList = new ArrayList<MarkerOptions>();
        for (TravelWaypoint wayp: getWaypoints()){
            MarkerOptions mops = new MarkerOptions();
            mops.position(wayp.getLocation());
            mops.title(wayp.getName());
            mList.add(mops);
        }
        return mList;
    }

    public static final String UP = "up";
    public static final String DOWN = "down";

    public static void Move(int index, String direction){
            switch (direction){
                case "up":
                    Collections.swap(waypoints, index, index-1);
                    break;
                case "down":
                    Collections.swap(waypoints, index, index + 1);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

    public static boolean CheckIfWaypointExists(TravelWaypoint waypoint){
        for (TravelWaypoint wayp: TravelPlanner.getWaypoints()){
            if (waypoint.getLocation().equals(wayp.getLocation())){
                return true;
            }
        }
        return false;
    }

    public static void DeleteWaypoint(int position) throws NoWaypointsSetException {
        if (getNumOfWaypoints() == 0){
            throw new NoWaypointsSetException();
        } else {
            waypoints.remove(position);
        }
    }

    public static String getTravelMode(){
        return TransitMode;
    }

    public static List<LatLng> getIntermediatePoints() {
        return intermediatePoints;
    }

    public static void setIntermediatePoints(List<LatLng> intermediatePoints) {
        TravelPlanner.intermediatePoints = intermediatePoints;
    }

    public static List<String> getInstructions() {
        return instructions;
    }

    public static void setInstructions(List<String> instructions) {
        TravelPlanner.instructions = instructions;
    }

    public static List<MarkerOptions> GetIntermediatePointMarkers(){
        List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
        for (LatLng ip:intermediatePoints){
            MarkerOptions mop = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.intermediate_point_pin));
            mop.position(ip);
            markers.add(mop);
        }
        return markers;
    }
}
