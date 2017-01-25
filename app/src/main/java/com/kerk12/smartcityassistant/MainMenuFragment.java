package com.kerk12.smartcityassistant;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainMenuFragment extends Fragment {

    private boolean checkNetworkConnectivity(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connMgr.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()){
            return true;
        } else {
            return false;
        }
    }

    private final int LOCATION_ACCESS = 1;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case LOCATION_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent i = new Intent(getActivity(), SmartSchedulePlanner.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.location_access_needed), Toast.LENGTH_LONG).show();
                }
                break;
            default:

                break;
        }
    }

    private OnClickListener SmartSchedulePlannerOCL = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!checkNetworkConnectivity()){
                Toast.makeText(getActivity(), getResources().getString(R.string.internet_not_connected), Toast.LENGTH_LONG).show();
                return;
            }
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_ACCESS);
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_ACCESS);
                }
            } else {

                Intent i = new Intent(getActivity(), SmartSchedulePlanner.class);
                startActivity(i);
            }
        }
    } ;



    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);
        CardView MapChoice = (CardView) v.findViewById(R.id.MainMenuMapChoice);

        MapChoice.setOnClickListener(SmartSchedulePlannerOCL);
        return v;
    }

}
