package com.kerk12.smartcityassistant;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kgiannakis on 15/2/2017.
 */

public class EOrderDishPicker extends Fragment {

    public static final String ARG_KEY = "com.kerk12.smartcityassistant.dishpicker";
    Restaurant restaurant = null;

    public EOrderDishPicker newInstance(int restaurant){
        EOrderDishPicker fragNew = new EOrderDishPicker();
        Bundle b = new Bundle();
        b.putInt(ARG_KEY, restaurant);
        fragNew.setArguments(b);
        return fragNew;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurant = Kitchen.getRestaurants(getActivity()).get(getArguments().getInt(ARG_KEY));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
