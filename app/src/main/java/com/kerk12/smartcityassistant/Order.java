package com.kerk12.smartcityassistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerk12 on 16/2/2017.
 */

public class Order {
    private static List<Dish> basket = new ArrayList<Dish>();
    public enum PaymentMethod{CREDIT_CARD,CASH}

    private PaymentMethod paymentMethod;

    public static double getFinalPrice(){
        double sum = 0;
        for (Dish d:basket){
            sum +=d.getPrice();
        }
        return sum;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public static List<Dish> getBasket() {
        return basket;
    }

    public static void setBasket(List<Dish> basket) {
        Order.basket = basket;
    }

    public static void ClearOrder(){
        basket = new ArrayList<Dish>();
    }

    public static void AddDish(Dish dish){
        basket.add(dish);
    }
}
