package com.kerk12.smartcityassistant;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class EOrderMainActivity extends AppCompatActivity {

    public static String RESTAURANT_PICKER_TAG = "rest_picker";
    public static String DISH_PICKER_TAG = "dish_picker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eorder_main);

        getSupportActionBar().setTitle(R.string.OnlineOrder);
        Fragment f = new EOrderRestaurantPicker();
        getFragmentManager().beginTransaction().add(R.id.eorder_fragment_container,f, RESTAURANT_PICKER_TAG).commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /**
         * If the DishPicker is not in the backstack, clear the order.
         */
        int BackStackEntries = getFragmentManager().getBackStackEntryCount();
        boolean flag = false;
        for (int i = 0; i < BackStackEntries; i++){
            if (getFragmentManager().getBackStackEntryAt(i).getName().equals("DishPicker")){
                flag = true;
                break;
            }
        }
        if (!flag){
            Order.ClearOrder();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.copied_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.help_menu_choice:
                if (!MainMenuFragment.checkNetworkConnectivity(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_not_connected), Toast.LENGTH_LONG).show();
                    return false;
                }
                    Intent i = new Intent(getApplicationContext(), HelpActivity.class);
                    startActivity(i);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
