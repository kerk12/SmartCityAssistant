package com.kerk12.smartcityassistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for managing the order. Used in EOrder.
 */
public class Order {
    private static List<Dish> basket = new ArrayList<Dish>();
    public enum PaymentMethod{CREDIT_CARD,CASH}

    private PaymentMethod paymentMethod;

    /**
     * Returns the final price of the order.
     * @return The final price.
     */
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

    /**
     * Clears the order basket.
     */
    public static void ClearOrder(){
        basket = new ArrayList<Dish>();
    }

    /**
     * Add a dish to the order basket.
     * @param dish The dish to be added.
     */
    public static void AddDish(Dish dish){
        basket.add(dish);
    }

    /**
     * Returns the number of items in the basket.
     * @return The number of items in the basket.
     */
    public static int getOrderItemCount(){
        return basket.size();
    }

    /**
     * Remove an item from the basket, at the given index.
     * @param index The index of the item.
     */
    public static void RemoveItem(int index){
        basket.remove(index);
    }

}
