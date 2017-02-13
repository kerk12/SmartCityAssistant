package com.kerk12.smartcityassistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgiannakis on 13/2/2017.
 */

public class DishMapping {
    private int RestaurantID;
    private List<Integer> mappedDishes = new ArrayList<Integer>();

    public DishMapping(int restaurantID, List<Integer> mappedDishes) {
        RestaurantID = restaurantID;
        this.mappedDishes = mappedDishes;
    }

    public int getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        RestaurantID = restaurantID;
    }

    public List<Integer> getMappedDishes() {
        return mappedDishes;
    }

    public void setMappedDishes(List<Integer> mappedDishes) {
        this.mappedDishes = mappedDishes;
    }
}