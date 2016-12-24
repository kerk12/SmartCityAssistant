package com.kerk12.smartcityassistant;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SmartSchedulePlanner extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;
    LocationManager lm = null;
    EditText MapOrigin;
    Button GetDirectionsButton;
    private List<LatLng> travel;
    private FloatingActionButton addWaypointButton;
    private FrameLayout addWaypointL;
    PlaceAutocompleteFragment addWaypointFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_schedule_planner);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MapOrigin = (EditText) findViewById(R.id.MapOrigin);
        GetDirectionsButton = (Button) findViewById(R.id.SubmitButton);

        GetDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mapOrigin = MapOrigin.getText().toString();
                Map<String, String> mapDirs = new HashMap<String, String>();
                mapDirs.put("origin", mapOrigin);
                //TODO Get input from the user...
                mapDirs.put("destination", "Acropolis");
                MapHelper helper = new MapHelper(mapDirs, getApplicationContext());
                Polyline tr;
                try {
                    tr = mMap.addPolyline(helper.getRoutePolyline());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            }
        });

        addWaypointL = (FrameLayout) findViewById(R.id.AddWaypointContainer);
        addWaypointFragment = new PlaceAutocompleteFragment();

        addWaypointFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("AddWaypoint", place.getAddress().toString());
                addWaypointL.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().detach(addWaypointFragment).commit();
            }

            @Override
            public void onError(Status status) {

            }
        });

        addWaypointButton = (FloatingActionButton) findViewById(R.id.AddWaypointButton);
        addWaypointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AddWaypointDialog wpDialog = new AddWaypointDialog();
//                wpDialog.show(getFragmentManager(), "Add Waypoint");
                getFragmentManager().beginTransaction().add(R.id.AddWaypointContainer,addWaypointFragment).commit();
                addWaypointL.setVisibility(View.VISIBLE);
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
}
