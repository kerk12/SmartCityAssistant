package com.kerk12.smartcityassistant;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.message;

/**
 * Class used for managing the user's schedule. It manages all the waypoints that the user has set, along with the travel mode and other travel options.
 */
public class TravelPlanner {

    //The list of the waypoints.
    private static List<TravelWaypoint> waypoints = null;
    private static String TransitMode = "driving";

    public static final String DRIVING = "driving";
    public static final String WALKING = "walking";
    public static final String BICYCLING = "bicycling";
    public static final String TRANSIT = "transit";

    private static String ArrivalTime = null;
    private static String Duration = null;

    private static List<LatLng> intermediatePoints = null;
    private static List<String> instructions = null;

    public static boolean findParking = true;

    /**
     * Exception thrown when no Travel Waypoints have been added to the list.
     */
    public static class NoWaypointsSetException extends Exception{
        public NoWaypointsSetException() {
            super("No waypoints have been set.");
        }
    }

    //Block instantiation
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
            return waypoints;
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


    /**
     * Creates a HashMap that can be used with the MapHelper class.
     * @return The final HashMap.
     * @throws NoWaypointsSetException If no waypoints are set.
     */
    public static Map<String, String> makeHelperHashMap() throws NoWaypointsSetException {
        Map<String, String> mMap = new HashMap<String, String>();
        //Get the first waypoint, set it as origin.
        mMap.put("origin", LatLngAsString(waypoints.get(0).getLocation()));
        StringBuilder waypBuilder = new StringBuilder();
        boolean first = true;
        int limit;

        limit = waypoints.size() - 1;

        //Set the waypoints...
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
        //Finally, set the destination.
        mMap.put("destination", LatLngAsString(waypoints.get(limit).getLocation()));

        return mMap;
    }

    public static void SetTravelMode(String TravelModeNew){
        TransitMode = TravelModeNew;
    }

    /**
     * Returns all the route markers.
     * @return a list with all the route markers.
     */
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

    /**
     * Method used for changing the order of the waypoints.
     * @param index The waypoint's position on the list.
     * @param direction Use TravelPlanner.UP to move upwards the list, and TravelPlanner.DOWN to move downwards.
     */
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

    /**
     * Check if a waypoint already exists on the list, based on the name and its coordinates.
     * @param waypoint The waypoint
     * @return True if it exists, false if it doesn't exist.
     */
    public static boolean CheckIfWaypointExists(TravelWaypoint waypoint){
        for (TravelWaypoint wayp: TravelPlanner.getWaypoints()){
            if (waypoint.getLocation().equals(wayp.getLocation())){
                return true;
            }
            if (waypoint.getEntryTitle() != ""){
                if(waypoint.getEntryTitle() == wayp.getEntryTitle()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Delete a waypoint from the list.
     * @param position The index of the waypoint.
     * @throws NoWaypointsSetException If no waypoints are set.
     */
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

    public static void setIntermediatePoints(List<LatLng> intermediatePoints) {
        TravelPlanner.intermediatePoints = intermediatePoints;
    }

    /**
     * Returns the list with the instructions.
     * @return A list of instruction strings.
     */
    public static List<String> getInstructions() {
        return instructions;
    }

    /**
     * Set the instructions.
     * @param instructions The list with the instructions.
     */
    public static void setInstructions(List<String> instructions) {
        TravelPlanner.instructions = instructions;
    }

    /**
     * Get markers based on the intermediate points.
     * @return A list of markers.
     */
    public static List<MarkerOptions> GetIntermediatePointMarkers(){
        List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
        for (LatLng ip:intermediatePoints){
            MarkerOptions mop = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.intermediate_point_pin));
            mop.position(ip);
            markers.add(mop);
        }
        return markers;
    }

    public static String getArrivalTime() {
        return ArrivalTime;
    }

    public static void setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public static String getDuration() {
        return Duration;
    }

    public static void setDuration(String duration) {
        Duration = duration;
    }

    /**
     * Check if a given time is illegal (if it's set earlier than a time already set).
     * @param c The calendar.
     * @return
     */
    public static boolean CheckForIllegalTime(Calendar c){
        Calendar last = null;
        int firstPosition = 0;
        //Find the first waypoint with an arrival time and break...
        for (int i = 0; i < getNumOfWaypoints(); i++){
            TravelWaypoint wp = getWaypoints().get(i);
            if (wp.getArrivalTime() !=null){
                last = wp.getArrivalTime();
                firstPosition = i;
                break;
            }
        }

        //If there wasn't any point with an arrival time, return true
        if (last == null){
            return true;
        }

        //Check the rest of the points...
        for (int i = firstPosition + 1; i < getNumOfWaypoints(); i++){
            TravelWaypoint wp = getWaypoints().get(i);
            if (wp.getArrivalTime() != null){
                if (wp.getArrivalTime().getTimeInMillis() > last.getTimeInMillis()){
                    last = wp.getArrivalTime();
                } else {
                    return false;
                }
            }
        }

        if (c.getTimeInMillis() > last.getTimeInMillis()){
            return true;
        }
        return false;
    }
}
