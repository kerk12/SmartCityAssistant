package com.kerk12.smartcityassistant;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by kerk12 on 14/2/2017.
 */

public class EOrderRestaurantPicker extends Fragment {

    private List<Restaurant> mRestaurants = null;

    private RecyclerView RestaurantRecycler;
    private RestaurantAdapter mAdapter;
    private LinearLayoutManager lm;

    private int selectedRestaurant = -1;

    private TextView restaurantName, restaurantLocation, restaurantCuisine;
    private LinearLayout details_view;

    private Button pickButton;

    private class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder>{

        private List<Restaurant> mList;
        public RestaurantAdapter(List<Restaurant> mList){
            this.mList = mList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_recycler_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Restaurant restaurant = mList.get(position);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedRestaurant = position;
                    UpdateUI();
                }
            });
            holder.name.setText(restaurant.getName());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{

            private TextView name;
            private RelativeLayout item;

            public ViewHolder(View itemView) {
                super(itemView);
                item = (RelativeLayout) itemView.findViewById(R.id.restaurant_item);
                name = (TextView) itemView.findViewById(R.id.restaurant_name_on_holder);
            }
        }
    }

    private void UpdateUI(){
        Restaurant restaurant = Kitchen.getRestaurants(getActivity()).get(selectedRestaurant);
        details_view.setVisibility(View.VISIBLE);
        restaurantName.setText(restaurant.getName());
        restaurantLocation.setText(restaurant.getLocation());
        restaurantCuisine.setText(restaurant.getCuisine());
        pickButton.setEnabled(true);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRestaurants = Kitchen.getRestaurants(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_picker, container, false);

        RestaurantRecycler = (RecyclerView) v.findViewById(R.id.restaurant_picker);
        lm = new LinearLayoutManager(getActivity());
        RestaurantRecycler.setLayoutManager(lm);
        mAdapter = new RestaurantAdapter(Kitchen.getRestaurants(getActivity()));
        RestaurantRecycler.setAdapter(mAdapter);

        /**
         * Details
         */
        details_view = (LinearLayout) v.findViewById(R.id.details_view);
        restaurantName = (TextView) v.findViewById(R.id.restaurant_name_details);
        restaurantLocation = (TextView) v.findViewById(R.id.restaurant_location_details);
        restaurantCuisine = (TextView) v.findViewById(R.id.restaurant_cuisine_details);

        /**
         * Pick button
         */
        pickButton = (Button) v.findViewById(R.id.restaurant_pick_button);
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EOrderDishPicker fragNew = EOrderDishPicker.newInstance(selectedRestaurant);
                getFragmentManager().beginTransaction().add(R.id.eorder_fragment_container, fragNew).addToBackStack(null).commit();
            }
        });
        return v;
    }
}
