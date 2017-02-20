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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by kgiannakis on 15/2/2017.
 */

public class EOrderDishPicker extends Fragment {

    public static final String ARG_KEY = "com.kerk12.smartcityassistant.dishpicker";
    Restaurant restaurant = null;

    private DishAdapter mAdapter;

    private RecyclerView DishRecycler;

    private TextView dishName,dishType,dishPrice;
    private Button AddToCart, GotoReview;
    private int selectedDish;
    private class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder>{

        private List<Dish> mDishes;
        public DishAdapter(List<Dish> dishes){
            mDishes = dishes;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_recycler_item, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Dish d = mDishes.get(position);
            holder.name.setText(d.getName());
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedDish = position;
                    AddToCart.setEnabled(true);
                    UpdateUI();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDishes.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{
            public RelativeLayout item;
            public TextView name, category;
            public ViewHolder(View itemView) {
                super(itemView);
                item = (RelativeLayout) itemView.findViewById(R.id.dish_item);
                name = (TextView) itemView.findViewById(R.id.dish_name_on_holder);
            }
        }
    }

    private void UpdateUI() {
        Dish d = restaurant.getDishes().get(selectedDish);
        dishName.setText(d.getName());
        dishPrice.setText(String.valueOf(d.getPrice()));

    }

    private void UpdateCartButton(){
        //ViewCartButton.setText("Καλάθι("+String.valueOf(Order.getOrderItemCount())+" Αντικείμενο/α)");

    }

    public EOrderDishPicker(){

    }
    public static EOrderDishPicker newInstance(int restaurant){
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
        View v = inflater.inflate(R.layout.fragment_dish_picker, container,false);

        DishRecycler = (RecyclerView) v.findViewById(R.id.dish_picker);
        mAdapter = new DishAdapter(restaurant.getDishes());
        DishRecycler.setAdapter(mAdapter);
        DishRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        dishName = (TextView) v.findViewById(R.id.dish_name_details);
        //dishType = (TextView) v.findViewById(R.id.dish_type_details);
        dishPrice = (TextView) v.findViewById(R.id.dish_price_details);
        AddToCart = (Button) v.findViewById(R.id.add_to_cart_button);
        //ViewCartButton = (Button) v.findViewById(R.id.cart_button);
        AddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Order.AddDish(restaurant.getDishes().get(selectedDish));
            }
        });

        GotoReview = (Button) v.findViewById(R.id.goto_review);
        GotoReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Order.getOrderItemCount() == 0){
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_items_in_cart), Toast.LENGTH_LONG).show();
                    return;
                }
                EOrderReviewFragment fragNew = new EOrderReviewFragment();
                getFragmentManager().beginTransaction().replace(R.id.eorder_fragment_container, fragNew,"Review").addToBackStack("Review").commit();
            }
        });
        return v;
    }
}
