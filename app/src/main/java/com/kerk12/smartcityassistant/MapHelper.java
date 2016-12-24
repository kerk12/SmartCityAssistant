package com.kerk12.smartcityassistant;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by kerk12 on 9/12/2016.
 */

public class MapHelper {

    private Map<String, String> reqMap = null;
    private Context c = null;
    private boolean configured = false;
    private List<LatLng> travel = null;
    private String result;
    private boolean calculated = false;

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
        //DO NOT USE WITHOUT https://...
        //sb.append("&key="+c.getResources().getString(R.string.google_maps_key));

        return sb.toString();
    }

    private class DirectionsGetter extends AsyncTask<URL, Void, String>{

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
                //TODO whatever...
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }
    private boolean getDirectionsAsPolyline() throws InstantiationException, MalformedURLException, ExecutionException, InterruptedException, TimeoutException {
        String url = makeGet();
        Log.d("MapHelper", url);
        URL directionsURL = new URL(url);
        DirectionsGetter task = new DirectionsGetter();
        task.execute(directionsURL);
        result = task.get(10000, TimeUnit.MILLISECONDS);

        JSONObject directions = null;

        try {
            directions = new JSONObject(result);


            JSONArray routes = directions.getJSONArray("routes");
            JSONObject defRoute = routes.getJSONObject(0);
            String enc_Polyline = defRoute.getJSONObject("overview_polyline").getString("points");
            travel = PolyUtil.decode(enc_Polyline);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void CalculatePolyline() throws InterruptedException, MalformedURLException, TimeoutException, InstantiationException, ExecutionException {
        getDirectionsAsPolyline();
    }

    public List<LatLng> getRoute() throws InterruptedException, MalformedURLException, TimeoutException, InstantiationException, ExecutionException {
        if (!calculated){
            CalculatePolyline();
        }
        return travel;
    }

    public PolylineOptions getRoutePolyline() throws InterruptedException, MalformedURLException, TimeoutException, InstantiationException, ExecutionException {
        if (!calculated){
            CalculatePolyline();
        }
        if (travel != null) {
            PolylineOptions options = new PolylineOptions();
            for (LatLng point : travel) {
                options.add(point);
            }
            return options;
        } else return null;
    }
}
