package com.kerk12.smartcityassistant;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EOrderMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eorder_main);

        Fragment f = new EOrderRestaurantPicker();
        getFragmentManager().beginTransaction().add(R.id.eorder_fragment_container,f).commit();
    }
}
