package com.kerk12.smartcityassistant;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        MainMenuFragment f = new MainMenuFragment();
        getFragmentManager().beginTransaction().add(R.id.frameContainerMainMenu, f, null).commit();
    }
}
