package com.kerk12.smartcityassistant;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
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
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Dish d = mList.get(position);
            holder.name.setText(d.getName());
            holder.price.setText(String.valueOf(d.getPrice()));
            holder.remove_imageview.setImageResource(R.drawable.delete);
            holder.remove_imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Order.RemoveItem(position);
                    UpdateAdapter();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{

            private TextView name, price;
            private ImageView remove_imageview;
            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.dish_name_on_holder);
                price = (TextView) itemView.findViewById(R.id.price_name_on_holder);
                remove_imageview = (ImageView) itemView.findViewById(R.id.remove_cart_item);
            }
        }
    }

    private RecyclerView reviewRecycler;
    private ReviewAdapter mAdapter;
    private EditText name, email, address, phone, CreditCardNumber, CreditCardExp,CreditCardCVC;
    private TextView totalPrice;
    private RadioGroup paymentMethod;
    private LinearLayout creditCardInfo;

    private enum PaymentMethod{CASH,CREDIT_CARD};

    private void DisplayTotalPrice(){
        DecimalFormat df = new DecimalFormat("#,##");
        totalPrice.setText(df.format(Order.getFinalPrice()));
    }

    private void UpdateAdapter(){
        mAdapter = new ReviewAdapter(Order.getBasket());
        reviewRecycler.setAdapter(mAdapter);
    }

    private PaymentMethod pm = PaymentMethod.CASH;
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
        CreditCardNumber = (EditText) v.findViewById(R.id.review_cred_card_number);
        CreditCardExp = (EditText) v.findViewById(R.id.review_cred_card_exp);
        CreditCardCVC = (EditText) v.findViewById(R.id.review_cred_card_cvc);
        creditCardInfo = (LinearLayout) v.findViewById(R.id.credit_card_info);
        paymentMethod = (RadioGroup) v.findViewById(R.id.payment_method);
        paymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_cash_on_delivery:
                        creditCardInfo.setVisibility(View.GONE);
                        pm = PaymentMethod.CASH;
                        break;
                    case R.id.choice_credit_card:
                        creditCardInfo.setVisibility(View.VISIBLE);
                        pm = PaymentMethod.CREDIT_CARD;
                        break;
                }
            }
        });

        reviewRecycler = (RecyclerView) v.findViewById(R.id.cart_review);
        mAdapter = new ReviewAdapter(Order.getBasket());
        reviewRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewRecycler.setAdapter(mAdapter);

        totalPrice = (TextView) v.findViewById(R.id.total_price);
        DisplayTotalPrice();
        return v;
    }
}
