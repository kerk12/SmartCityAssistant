package com.kerk12.smartcityassistant;

import android.content.Context;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by kerk12 on 9/12/2016.
 */

public class MapHelper {

    private Map<String, String> reqMap = null;
    private Context c = null;
    private boolean configured = false;

    /**
     * Public constructor for the MapHelper Class. It is required that the class is instantiated.
     * @param requestDirections The map with the directions.
     * @param c The application's context.
     */
    public MapHelper(Map<String, String> requestDirections, Context c){
        reqMap = requestDirections;
        this.c = c;
        configured = true;
    }

    private String makeGet() throws InstantiationException {

        StringBuilder sb = new StringBuilder();

        //The base of the url. It always returns json, so no need to specify the format...
        String BaseURL = "http://maps.googleapis.com/maps/api/directions/json?";
        sb.append(BaseURL);

        //Get every entry in the map, and append it to the final url.
        boolean first = true;
        for (Map.Entry<String, String> entry : reqMap.entrySet()){
            //If it is the first element, don't append an ampersand.
            if (first){
                first = false;
            } else {
                sb.append("&");
            }
            // Append as element_key=something
            /*For example:
                origin=Monastiraki
                destination=Cap+Cap
            */
            sb.append(entry.getKey() + "="+ entry.getValue().replaceAll(" ", "+"));
        }
        sb.append("&key="+c.getResources().getString(R.string.google_maps_key));

        return sb.toString();
    }

    public String getDirections() throws InstantiationException, MalformedURLException {
        if (!configured){
            throw new InstantiationException("Please configure the MapHelper class first");
        }
        String url = makeGet();
        //Log.d("MapHelper", url);
        URL directionsURL = new URL(url);

        return null;
    }
}
