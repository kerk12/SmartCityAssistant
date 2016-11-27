package com.kerk12.smartcityassistant;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainMenuFragment extends Fragment {


    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);
        CardView MapChoice = (CardView) v.findViewById(R.id.MainMenuMapChoice);

        MapChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

}
