package com.kerk12.smartcityassistant;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
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

    private OnClickListener myOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), SmartSchedulePlanner.class);
            startActivity(i);
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

        MapChoice.setOnClickListener(myOnClickListener);
        return v;
    }

}
