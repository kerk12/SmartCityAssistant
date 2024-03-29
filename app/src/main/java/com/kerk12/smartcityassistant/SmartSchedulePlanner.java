package com.kerk12.smartcityassistant;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SmartSchedulePlanner extends AppCompatActivity implements OnMapReadyCallback, TravelOptionsDialog.TravelOptionsListener, TimePickerDialFrag.TimePickerDialFragListener {

    public static GoogleMap mMap;
    LocationManager lm = null;
    private List<LatLng> travel;
    private FloatingActionButton addWaypointButton;
    private FrameLayout addWaypointL;
    PlaceAutocompleteFragment addWaypointFragment;
    private RecyclerView SPRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager SPMan;

    private Polyline route;
    private List<MarkerOptions> markers;

    private Place selectedPlace = null;
    private Button travelOptionsButton;

    private TextView instructions;
    private TextView duration;

    private Calendar TimeSet = null;
    private Button ArrivalTimeButton;
    private EditText newWaypointTitle;


    @Override
    public void OnTravelOptionsCommit() {
        UpdateMap();
    }

    /**
     * Parse the time to HH:MM in a String
     * @param c The calendar to be parsed
     * @return The string with the parsed.
     */
    private String getParsedTime(Calendar c){
        if (c== null){
            return null;
        } else {
            String s;
            if (c.get(Calendar.MINUTE) < 10) {
                s = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ":0" + String.valueOf(c.get(Calendar.MINUTE));
            } else {
                s = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(c.get(Calendar.MINUTE));
            }
            return s;
        }
    }

    /**
     * Get the time from the selector fragment.
     * @param hourOfDay The hour of the day
     * @param minute The minute of the day.
     */
    @Override
    public void onTimeSelected(int hourOfDay, int minute) {
        TimeSet = Calendar.getInstance();
        TimeSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        TimeSet.set(Calendar.MINUTE, minute);

        ArrivalTimeButton.setText(getString(R.string.arrival_time)+ ": "+getParsedTime(TimeSet));
    }


    private class SPAdapter extends RecyclerView.Adapter<SPAdapter.ViewHolder> {

        private List<TravelWaypoint> mList;

        public void UpdateAdapter(){
            mAdapter = null;
            mAdapter = new SPAdapter(TravelPlanner.getWaypoints());
            SPRecyclerView.setAdapter(mAdapter);
        }

        public SPAdapter(List<TravelWaypoint> waypoints) {
            mList = waypoints;
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {

            public TextView EntryTitle, WaypointName, ArrivalTime;
            public ImageButton up, down, del;

            public ViewHolder(View itemView) {
                super(itemView);
                up = (ImageButton) itemView.findViewById(R.id.send_up);
                down = (ImageButton) itemView.findViewById(R.id.send_down);
                del = (ImageButton) itemView.findViewById(R.id.del);
                up.setImageResource(R.drawable.arrow_up);
                down.setImageResource(R.drawable.arrow_down);
                del.setImageResource(R.drawable.delete);
                EntryTitle = (TextView) itemView.findViewById(R.id.waypoint_title);
                WaypointName = (TextView) itemView.findViewById(R.id.waypoint_name);
                ArrivalTime = (TextView) itemView.findViewById(R.id.arrival_time);

            }


        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_recycler_item, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (mList.get(position).getEntryTitle() != null && !mList.get(position).getEntryTitle().equals("")){
                holder.EntryTitle.setText(mList.get(position).getEntryTitle());
            }

            holder.WaypointName.setText(mList.get(position).getName());
            if (mList.get(position).getArrivalTime() != null){
                holder.ArrivalTime.setText(getResources().getString(R.string.arrival_time)+": "+ mList.get(position).getParsedArrivalTime());
            } else if (position == 0){
                holder.ArrivalTime.setVisibility(View.GONE);
            } else {
                holder.ArrivalTime.setText(getResources().getString(R.string.arrival_time)+": Μη ορισμένη");
            }
            final int pos = position;
            if (pos == 0){
                holder.up.setEnabled(false);
            }
            if (pos == TravelPlanner.getNumOfWaypoints() - 1){
                holder.down.setEnabled(false);
            }

            holder.up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TravelPlanner.getWaypoints().get(position).getArrivalTime() != null){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.IllegalMoveAttempt), Toast.LENGTH_LONG).show();
                        return;
                    }
                    TravelPlanner.Move(pos,TravelPlanner.UP);
                    mAdapter.notifyDataSetChanged();
                    UpdateMap();
                    UpdateAdapter();
                }
            });
            holder.down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TravelPlanner.getWaypoints().get(position).getArrivalTime() != null){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.IllegalMoveAttempt), Toast.LENGTH_LONG).show();
                        return;
                    }
                    TravelPlanner.Move(pos,TravelPlanner.DOWN);
                    mAdapter.notifyDataSetChanged();
                    UpdateMap();
                    UpdateAdapter();
                }
            });
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TravelPlanner.DeleteWaypoint(position);
                    } catch (TravelPlanner.NoWaypointsSetException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                    UpdateMap();
                    UpdateAdapter();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void RefreshDataset() {
            mList = TravelPlanner.getWaypoints();
            notifyDataSetChanged();
        }
    }

    private TravelOptionsDialog.TravelOptionsListener l;

    /**
     * Returns a random parking spot, given the list of the points consisting the polyline.
     * @param travelPoints The PolyLine points.
     * @return A random Marker.
     */
    private MarkerOptions getRandomParkingSpot(List<LatLng> travelPoints){
        int max = travelPoints.size();
        int min = 0;
        if (travelPoints.size() <= 10){
            min = travelPoints.size() - 5;
        } else {
            min = travelPoints.size() - 3;
        }
        //TODO Add more
        Random rn = new Random();
        int range = max - min + 1;
        int randomNum =  rn.nextInt(range) + min - 1;

        LatLng parking = travelPoints.get(randomNum);
        MarkerOptions mops = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_spot_pin));
        mops.position(parking);
        mops.title(getResources().getString(R.string.parking_spot));
        return mops;
    }

    private String ParseInstructions(List<String> instructions){
        StringBuilder sb = new StringBuilder();
        for (String instruction: instructions){
            sb.append(instruction+ "\n");
        }
        return sb.toString();
    }

    /**
     * Method used for updating the map fragment along with the UI in general.
     */
    private void UpdateMap(){
        //Clear the map
        mMap.clear();
        //Add the markers.
        if (TravelPlanner.getNumOfWaypoints() >= 1){
            markers = TravelPlanner.getRouteMarkers();
            for (MarkerOptions mop: markers){
                mMap.addMarker(mop);
            }
        }
        if (TravelPlanner.getNumOfWaypoints() >= 2) {
            try {
                MapHelper helper;

                //Make a new MapHelper object, depending on the situation. Then, execute it.
                //2 Points, Transit set as the Travel mode
                if (TravelPlanner.getNumOfWaypoints() == 2 && TravelPlanner.getTravelMode() == TravelPlanner.TRANSIT) {
                    helper = new MapHelper(TravelPlanner.makeHelperHashMap(), TravelPlanner.getTravelMode(), "el", getApplicationContext());
                } else if (TravelPlanner.getNumOfWaypoints() > 2 && TravelPlanner.getTravelMode() == TravelPlanner.TRANSIT) { // More than two points, Transit set as the travel mode.
                    TravelPlanner.SetTravelMode(TravelPlanner.DRIVING);
                    helper = new MapHelper(TravelPlanner.makeHelperHashMap(), TravelPlanner.getTravelMode(), "el", getApplicationContext());

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_more_than_2_wp_allowed_switched), Toast.LENGTH_LONG).show();
                } else { //Everything else...
                    helper = new MapHelper(TravelPlanner.makeHelperHashMap(), TravelPlanner.getTravelMode(), "el", getApplicationContext());
                }
                helper.execute();

                //Get the instructions and the intermediate points, if the Travel mode is set to Transit.
                if (TravelPlanner.getTravelMode() == TravelPlanner.TRANSIT){
                    TravelPlanner.setInstructions(helper.getInstructions());
                    TravelPlanner.setIntermediatePoints(helper.getIntermediatePoints());
                }

                //Get the polyline
                PolylineOptions opt = helper.getRoutePolyline();

                //Set the parking spot, if available.
                if (TravelPlanner.findParking && (TravelPlanner.getTravelMode() == TravelPlanner.DRIVING || TravelPlanner.getTravelMode() == TravelPlanner.BICYCLING)) {
                    mMap.addMarker(getRandomParkingSpot(helper.getRoute()));
                }

                //If nothing is returned, then no route is available
                if (opt == null){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_routes), Toast.LENGTH_SHORT).show();
                    return;
                }
                //Add the route to the map
                route = mMap.addPolyline(opt);
            } catch (TravelPlanner.NoWaypointsSetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //Update the UI by adding the instructions, the intermediate points, and the duration.
            if (TravelPlanner.getTravelMode() == TravelPlanner.TRANSIT){
                if(TravelPlanner.getNumOfWaypoints() >=2) {
                    instructions.setText(ParseInstructions(TravelPlanner.getInstructions()));
                    instructions.setVisibility(View.VISIBLE);
                    List<MarkerOptions> intermP = TravelPlanner.GetIntermediatePointMarkers();
                    for (MarkerOptions ip : intermP) {
                        mMap.addMarker(ip);
                    }
                    if (TravelPlanner.getNumOfWaypoints() == 2) {
                        duration.setText(getResources().getString(R.string.travel_duration)+ TravelPlanner.getDuration());
                        duration.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                instructions.setText("");
                instructions.setVisibility(View.INVISIBLE);
                duration.setVisibility(View.GONE);
            }



        } else {
            duration.setText(getResources().getString(R.string.travel_duration));
        }
    }





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_schedule_planner);
        getSupportActionBar().setTitle(R.string.SchedulePlanner);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SPRecyclerView = (RecyclerView) findViewById(R.id.SchedulePlannerRecyclerview);
        SPMan = new LinearLayoutManager(this);
        SPRecyclerView.setLayoutManager(SPMan);
        mAdapter = new SPAdapter(TravelPlanner.getWaypoints());
        SPRecyclerView.setAdapter(mAdapter);

        instructions = (TextView) findViewById(R.id.instructions);
        duration = (TextView) findViewById(R.id.duration);

        newWaypointTitle = (EditText) findViewById(R.id.new_entry_title);

        addWaypointFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.AddPlaceFr);

        /**
         * When a place is selected, update the selected place.
         */
        addWaypointFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //Log.d("AddWaypoint", place.getAddress().toString());
                selectedPlace = place;
            }

            @Override
            public void onError(Status status) {

            }
        });

        addWaypointButton = (FloatingActionButton) findViewById(R.id.AddWaypointButton);
        addWaypointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If no destination is selected.
                if (selectedPlace == null){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.NoSelectedPlace), Toast.LENGTH_SHORT).show();
                    return;
                }
                TravelWaypoint wayp = null;

                //Check the time if it isn't null, and if so, check if illegal.
                if (TimeSet != null){
                    if (TravelPlanner.CheckForIllegalTime(TimeSet)) {
                        wayp = new TravelWaypoint(selectedPlace.getName().toString(), selectedPlace.getLatLng(), TimeSet);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.IllegalTimeGiven), Toast.LENGTH_LONG).show();
                    }
                } else {
                    wayp = new TravelWaypoint(selectedPlace.getName().toString(), selectedPlace.getLatLng());
                }
                if (wayp == null) return;
                if (newWaypointTitle.getText() != null && newWaypointTitle.getText().toString() != "") {
                    wayp.setEntryTitle(newWaypointTitle.getText().toString());
                }
                //Check if the waypoint already exists.
                if (TravelPlanner.CheckIfWaypointExists(wayp)){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.WaypointAlrExists), Toast.LENGTH_SHORT).show();
                    return;
                }

                //Finally, add the waypoint to the planner.
                TravelPlanner.AddWaypoint(wayp);

                SPRecyclerView.setAdapter(new SPAdapter(TravelPlanner.getWaypoints()));
                SPRecyclerView.invalidate();

                UpdateMap();
                //Reset everything, so it can be reused.
                addWaypointFragment.setText("");
                newWaypointTitle.setText("");
                ArrivalTimeButton.setText(getString(R.string.arrival_time));
                selectedPlace = null;
                TimeSet = null;
            }
        });

        travelOptionsButton = (Button) findViewById(R.id.TravelOptions);
        travelOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelOptionsDialog travelOpts = new TravelOptionsDialog();

                travelOpts.show(getFragmentManager(), "TravelOpts");

            }
        });

        ArrivalTimeButton = (Button) findViewById(R.id.WantedTime);
        ArrivalTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment f = new TimePickerDialFrag();
                f.show(getFragmentManager(),"asdf2");
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);

        if (lm == null) {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        Criteria crit = new Criteria();
        List<String> providers = lm.getProviders(true);
        Location loc = null;
        //Get the last known location
        for (String provider:providers){
            loc = lm.getLastKnownLocation(provider);
            if (loc!= null){
                break;
            }
        }


        if (loc != null) {
            //Get the last known location, and get its address, by using the Geocoder.
            LatLng currPos = new LatLng(loc.getLatitude(), loc.getLongitude());
            Geocoder coder = new Geocoder(getApplicationContext(), Locale.getDefault());

            if (TravelPlanner.getNumOfWaypoints() == 0) {
                List<Address> addr;
                try {
                    addr = coder.getFromLocation(currPos.latitude, currPos.longitude, 1);
                    TravelWaypoint start = new TravelWaypoint(addr.get(0).getAddressLine(0), new LatLng(addr.get(0).getLatitude(), addr.get(0).getLongitude()));
                    TravelPlanner.AddWaypoint(start);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.AddressDetected) + addr.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();

                    SPRecyclerView.invalidate();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (TravelPlanner.getNumOfWaypoints() > 0){
                UpdateMap();
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPos, 16));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*
        The options menu.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.copied_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.help_menu_choice:
                if (!MainMenuFragment.checkNetworkConnectivity(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_not_connected), Toast.LENGTH_LONG).show();
                    return false;
                }
                Intent i = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
