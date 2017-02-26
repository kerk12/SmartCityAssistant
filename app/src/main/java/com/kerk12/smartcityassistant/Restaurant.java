package com.kerk12.smartcityassistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for modelling the restaurants used in EOrder.
 */
public class Restaurant {
    private String name;
    private String phone_number;
    private String location;
    private String cuisine;

    private List<Dish> dishes = new ArrayList<Dish>();


    public Restaurant( String name, String phone_number, String location) {
        this.name = name;
        this.phone_number = phone_number;
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void addDish(Dish d){
        dishes.add(d);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
}
