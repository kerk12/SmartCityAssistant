package com.kerk12.smartcityassistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerk12 on 12/24/16.
 */

public class TravelPlanner {

    private static List<TravelWaypoint> waypoints = null;
    private static TravelWaypoint finalDestination = null;

    private TravelPlanner() {

    }

    public static List<TravelWaypoint> getWaypoints(){
        if ( waypoints == null){
            waypoints = new ArrayList<TravelWaypoint>();
        }
        return waypoints;
    }

    public static void AddWaypoint(TravelWaypoint waypoint){
        if (waypoint.isFinalDestination()) {
            if (finalDestination == null) {
                finalDestination = waypoint;
            } else {
                //TODO Exception handling...
            }
        }
        waypoints.add(waypoint);
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

    public static TravelWaypoint getFinalDestination(){
        return finalDestination;
    }
}
