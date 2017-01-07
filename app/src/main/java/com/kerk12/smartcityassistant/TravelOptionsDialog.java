package com.kerk12.smartcityassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by kgiannakis on 3/1/2017.
 */

public class TravelOptionsDialog extends DialogFragment {

    private RadioButton driving, cycling, walking, transit;
    private CheckBox parking;
    private String PresetMode = null;

    public interface TravelOptionsListener{
        public void OnTravelOptionsCommit();
    }

    TravelOptionsListener l;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        l = (TravelOptionsListener) context;
    }

    private View CreateInterface() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_travel_options, null);

        driving = (RadioButton) v.findViewById(R.id.driving_rb);
        walking = (RadioButton) v.findViewById(R.id.walking_rb);
        cycling = (RadioButton) v.findViewById(R.id.bicycling_rb);
        transit = (RadioButton) v.findViewById(R.id.transit_rb);

        if (TravelPlanner.getNumOfWaypoints() > 2){
            transit.setEnabled(false);
            transit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_more_than_2_wp_allowed), Toast.LENGTH_SHORT).show();
                }
            });
        }
        parking = (CheckBox) v.findViewById(R.id.parking_cb);

        driving.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                parking.setEnabled(isChecked);
            }
        });

        if (TravelPlanner.getTravelMode() != TravelPlanner.DRIVING) {
            parking.setEnabled(false);
        } else {
            parking.setChecked(TravelPlanner.findParking);
        }

        if (PresetMode != null) {
            switch (PresetMode) {
                case TravelPlanner.BICYCLING:
                    cycling.setChecked(true);
                    break;
                case TravelPlanner.TRANSIT:
                    transit.setChecked(true);
                    break;
            }
        }
        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (TravelPlanner.getTravelMode() != TravelPlanner.DRIVING){
            PresetMode = TravelPlanner.getTravelMode();
        }

        View v = CreateInterface();

        AlertDialog.Builder bob = new AlertDialog.Builder(getActivity());
        bob.setView(v);
        bob.setMessage(getResources().getString(R.string.TravelOptions));
        bob.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (driving.isChecked()) {
                    TravelPlanner.SetTravelMode(TravelPlanner.DRIVING);
                } else if (walking.isChecked()) {
                    TravelPlanner.SetTravelMode(TravelPlanner.WALKING);
                } else if (cycling.isChecked()) {
                    TravelPlanner.SetTravelMode(TravelPlanner.BICYCLING);
                } else if (transit.isChecked()) {
                    TravelPlanner.SetTravelMode(TravelPlanner.TRANSIT);
                }

                TravelPlanner.findParking = parking.isChecked();
                l.OnTravelOptionsCommit();
            }
        });
        bob.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return bob.create();

    }
}