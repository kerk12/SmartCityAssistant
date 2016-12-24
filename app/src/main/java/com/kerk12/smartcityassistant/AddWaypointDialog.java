package com.kerk12.smartcityassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by kerk12 on 12/24/16.
 */

public class AddWaypointDialog extends DialogFragment {

    private Place SelectedPlace;
    private static View rootView = null;
    FrameLayout frCont;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        if (rootView == null){
            rootView = inflater.inflate(R.layout.add_place_dialog, null);

        }
        builder.setView(rootView);
        PlaceAutocompleteFragment placeFr = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.AddPlaceFr2);
        placeFr.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("AddPlace", place.getName().toString());
                SelectedPlace = place;

            }

            @Override
            public void onError(Status status) {

            }
        });
        builder.setPositiveButton(R.string.AddPlace, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddWaypointDialog.this.getDialog().dismiss();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddWaypointDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }



}
