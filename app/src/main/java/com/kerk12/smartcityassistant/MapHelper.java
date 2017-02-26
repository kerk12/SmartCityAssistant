package com.kerk12.smartcityassistant;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Class used for managing the requests needed for connecting the Directions API.
 * @see <a href="https://developers.google.com/maps/documentation/directions/start">https://developers.google.com/maps/documentation/directions/start</a>
 */
public class MapHelper {

    /**
     * Map with all the required request parameters.
     */
    private Map<String, String> reqMap = null;
    private Context c = null;
    private boolean configured = false;
    /**
     * List with all the waypoints recieved from the API.
     */
    private List<LatLng> travel = null;
    /**
     * The result string, in JSON form.
     */
    private String result;
    private boolean calculated = false;
    private String TransitMode = "driving";
    /**
     * List with the instructions.
     */
    private List<String> instructions = null;
    /**
     * List of the intermediate waypoints.
     */
    private List<LatLng> intermediatePoints = null;
    private String language = "en";

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

    /**
     * Public constructor for the MapHelper Class. It is required that the class is instantiated.
     * @param requestDirections The map with the directions.
     * @param TransitMode The transit mode.
     * @param c The application's context.
     */
    public MapHelper(Map<String, String> requestDirections, String TransitMode, Context c){
        reqMap = requestDirections;
        this.c = c;
        configured = true;
        this.TransitMode = TransitMode;
    }

    /**
     * Public constructor for the MapHelper Class. It is required that the class is instantiated.
     * @param requestDirections The map with the directions.
     * @param TransitMode The transit mode.
     * @param c The application's context.
     * @param language The result instructions' language.
     */
    public MapHelper(Map<String, String> requestDirections, String TransitMode,String language, Context c){
        reqMap = requestDirections;
        this.c = c;
        configured = true;
        this.TransitMode = TransitMode;
        this.language = language;
    }

    /**
     * Method used for making the final GET URL.
     * @return The GET URL.
     */
    private String makeGet() {

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
        if (this.TransitMode != TravelPlanner.DRIVING){
            sb.append("&mode="+this.TransitMode);
        }
        if (this.language != "en"){
            sb.append("&language="+this.language);
        }

        //DO NOT USE WITHOUT https://...
        //sb.append("&key="+c.getResources().getString(R.string.google_maps_key));

        return sb.toString();
    }


    private class DirectionsGetter extends AsyncTask<URL, Void, String>{

        /**
         * Gets the final result string.
         * @param reqURL The request's url.
         * @return The result string in JSON.
         * @throws IOException If the connection fails, or if it can't parse the result properly.
         */
        private String getDirections(URL reqURL) throws IOException {
            InputStream is;
            String content = null;

            HttpURLConnection conn = (HttpURLConnection) reqURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(false);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                is = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                is.close();
                content = sb.toString();
            }
            return content;
        }

        @Override
        protected String doInBackground(URL... urls) {
            try {
                String content = getDirections(urls[0]);
                Log.d("DirectionsGetter", content);
                result = content;
                return content;
            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    /**
     * Parses the final PolyLine points from the final string, along with the instructions and the travel duration.
     * @return True on Success, False on failure.
     * @throws MalformedURLException If no protocol is specified in the URL.
     * @throws ExecutionException If the AsyncTask fails.
     * @throws InterruptedException If the AsyncTask is interrupted.
     * @throws TimeoutException If the connection times out.
     */
    private boolean getPolylinePoints() throws MalformedURLException, ExecutionException, InterruptedException, TimeoutException {
        String url = makeGet();
        Log.d("MapHelper", url);
        URL directionsURL = new URL(url);
        DirectionsGetter task = new DirectionsGetter();
        task.execute(directionsURL);
        result = task.get();

        JSONObject directions = null;

        try {
            directions = new JSONObject(result);


            JSONArray routes = directions.getJSONArray("routes");
            JSONObject defRoute = routes.getJSONObject(0);
            JSONObject defLeg = defRoute.getJSONArray("legs").getJSONObject(0);
            String enc_Polyline = defRoute.getJSONObject("overview_polyline").getString("points");
            travel = PolyUtil.decode(enc_Polyline);
            TravelPlanner.setDuration(defLeg.getJSONObject("duration").getString("text"));
            calculated = true;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<String> GetInstructionsFromResult(){
        JSONObject directions = null;

        instructions = new ArrayList<String>();
        intermediatePoints = new ArrayList<LatLng>();
        try {
            directions = new JSONObject(result);
            JSONArray routes = directions.getJSONArray("routes");
            JSONObject defRoute = routes.getJSONObject(0);
            JSONArray legs = defRoute.getJSONArray("legs");
            JSONObject defLeg = legs.getJSONObject(0);
            JSONArray steps = defLeg.getJSONArray("steps");
            for (int i = 0; i < steps.length();i++){
                JSONObject step = steps.getJSONObject(i);
                instructions.add(step.getString("html_instructions"));
                JSONObject endp = step.getJSONObject("end_location");
                if (i < steps.length() - 1) {
                    LatLng endpoint = new LatLng(endp.getDouble("lat"), endp.getDouble("lng"));
                    intermediatePoints.add(endpoint);
                }
            }
            return instructions;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<LatLng> getRoute() throws InterruptedException, MalformedURLException, TimeoutException, InstantiationException, ExecutionException {
        if (!calculated){
            getPolylinePoints();
        }
        return travel;
    }

    public void execute() throws InterruptedException, MalformedURLException, TimeoutException, InstantiationException, ExecutionException {
        getPolylinePoints();
        if (TransitMode == TravelPlanner.TRANSIT) {
            instructions = GetInstructionsFromResult();
        }
    }

    public PolylineOptions getRoutePolyline() throws InterruptedException, MalformedURLException, TimeoutException, InstantiationException, ExecutionException {
        if (!calculated){
            getPolylinePoints();
        }
        if (travel != null) {
            PolylineOptions options = new PolylineOptions();
            Random rnd = new Random();
            int color = Color.BLUE;
            options.color(color);
            for (LatLng point : travel) {
                options.add(point);
                /*
                    Get a random color and add it to the route.
                    Taken from:
                    http://stackoverflow.com/questions/5280367/android-generate-random-color-on-click
                 */

            }
            return options;
        } else return null;
    }

    public List<String> getInstructions(){
        return instructions;
    }

    public List<LatLng> getIntermediatePoints(){
        return intermediatePoints;
    }

}
