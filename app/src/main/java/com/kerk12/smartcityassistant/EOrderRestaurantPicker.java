package com.kerk12.smartcityassistant;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public void onBindViewHolder(ViewHolder holder, int position) {
            Restaurant restaurant = mList.get(position);
            holder.name.setText(restaurant.getName());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{

            private TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.restaurant_name_on_holder);
            }
        }
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
        return v;
    }
}
