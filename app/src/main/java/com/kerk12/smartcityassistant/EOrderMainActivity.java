package com.kerk12.smartcityassistant;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EOrderMainActivity extends AppCompatActivity {

    public static String RESTAURANT_PICKER_TAG = "rest_picker";
    public static String DISH_PICKER_TAG = "dish_picker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eorder_main);

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
    protected void onPause() {
        super.onPause();
        finish();
    }
}
