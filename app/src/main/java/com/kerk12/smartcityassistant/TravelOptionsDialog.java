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
import android.widget.RadioButton;

/**
 * Created by kgiannakis on 3/1/2017.
 */

public class TravelOptionsDialog extends DialogFragment {

    private RadioButton driving, cycling, walking, transit;
    private CheckBox parking;

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

        parking = (CheckBox) v.findViewById(R.id.parking_cb);
        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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