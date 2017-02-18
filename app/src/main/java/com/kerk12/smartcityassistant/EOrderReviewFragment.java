package com.kerk12.smartcityassistant;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kerk12 on 17/2/2017.
 */

public class EOrderReviewFragment extends Fragment {

    private class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

        private List<Dish> mList;

        public ReviewAdapter(List<Dish> mList) {
            this.mList = mList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recycler_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Dish d = mList.get(position);
            holder.name.setText(d.getName());
            holder.price.setText(String.valueOf(d.getPrice()));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{

            private TextView name, price;
            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.dish_name_on_holder);
                price = (TextView) itemView.findViewById(R.id.price_name_on_holder);
            }
        }
    }

    private EditText name, email, address, phone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review, container, false);

        name = (EditText) v.findViewById(R.id.review_name);
        email = (EditText) v.findViewById(R.id.review_email);
        address = (EditText) v.findViewById(R.id.review_address);
        phone = (EditText) v.findViewById(R.id.review_phone);

        return v;
    }
}
