package com.kerk12.smartcityassistant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SmartSchedulePlanner extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;
    LocationManager lm = null;
    //    EditText MapOrigin;
//    Button GetDirectionsButton;
    private List<LatLng> travel;
    private FloatingActionButton addWaypointButton;
    private FrameLayout addWaypointL;
    PlaceAutocompleteFragment addWaypointFragment;
    private RecyclerView SPRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager SPMan;
    private CheckBox isFinalDestination;

    private Polyline route;
    private List<MarkerOptions> markers;

    private Place selectedPlace = null;

    private class SPAdapter extends RecyclerView.Adapter<SPAdapter.ViewHolder> {

        private List<TravelWaypoint> mList;

        public SPAdapter(List<TravelWaypoint> waypoints) {
            mList = waypoints;
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {

            public TextView WaypointName;

            public ViewHolder(View itemView) {
                super(itemView);
                WaypointName = (TextView) itemView.findViewById(R.id.waypoint_name);
            }
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sp_recycler_item, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.WaypointName.setText(mList.get(position).getName());
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

    private void UpdateMap(){
        if (TravelPlanner.getNumOfWaypoints() >= 1){
            markers = TravelPlanner.getRouteMarkers();
            for (MarkerOptions mop: markers){
                mMap.addMarker(mop);
            }
        }
        if (TravelPlanner.getNumOfWaypoints() >= 2) {
            try {
                MapHelper helper = new MapHelper(TravelPlanner.makeHelperHashMap(), getApplicationContext());
                PolylineOptions opt = helper.getRoutePolyline();
                //TODO set the color and the start and end markers
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
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_schedule_planner);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        MapOrigin = (EditText) findViewById(R.id.MapOrigin);
//        GetDirectionsButton = (Button) findViewById(R.id.SubmitButton);
//
//        GetDirectionsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String mapOrigin = MapOrigin.getText().toString();
//                Map<String, String> mapDirs = new HashMap<String, String>();
//                mapDirs.put("origin", mapOrigin);
//                //TODO Get input from the user...
//                mapDirs.put("destination", "Acropolis");
//                MapHelper helper = new MapHelper(mapDirs, getApplicationContext());
//                Polyline tr;
//                try {
//                    tr = mMap.addPolyline(helper.getRoutePolyline());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
        SPRecyclerView = (RecyclerView) findViewById(R.id.SchedulePlannerRecyclerview);
        SPMan = new LinearLayoutManager(this);
        SPRecyclerView.setLayoutManager(SPMan);
        mAdapter = new SPAdapter(TravelPlanner.getWaypoints());
        SPRecyclerView.setAdapter(mAdapter);


        addWaypointFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.AddPlaceFr);

        isFinalDestination = (CheckBox) findViewById(R.id.isFinalDestinationCB);
        isFinalDestination.setText(getResources().getString(R.string.isFinalDestination));
        isFinalDestination.setTextColor(getResources().getColor(R.color.colorPrimary));


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
                if (selectedPlace == null){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.NoSelectedPlace), Toast.LENGTH_SHORT).show();
                    return;
                }
                TravelWaypoint wayp;
                if (!TravelPlanner.ExistsFinalDestination()) {
                    wayp = new TravelWaypoint(selectedPlace.getName().toString(), selectedPlace.getLatLng(), isFinalDestination.isChecked());
                } else {
                    wayp = new TravelWaypoint(selectedPlace.getName().toString(), selectedPlace.getLatLng());
                }
                TravelPlanner.AddWaypoint(wayp);

                SPRecyclerView.setAdapter(new SPAdapter(TravelPlanner.getWaypoints()));
                SPRecyclerView.invalidate();

                UpdateMap();
                if (TravelPlanner.ExistsFinalDestination()) {
                    isFinalDestination.setEnabled(false);
                }
                addWaypointFragment.setText("");
                selectedPlace = null;
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
        Location loc = lm.getLastKnownLocation(lm.getBestProvider(crit, false));
        if (loc != null) {
            LatLng currPos = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currPos, 15));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
