package com.kerk12.smartcityassistant;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for holding and managing the restaurants, dishes and dish mappings.
 */
public class Kitchen {
    private static List<Restaurant> restaurants = null;
    private static List<Dish> dishes = null;
    private static List<DishMapping> mappings = null;
    private static boolean initialized = false;

    /**
     * Parses restaurants from the JSON Dataset.
     * @param object The initial JSONObject
     * @return A list of restaurants, parsed.
     * @throws JSONException In case something fails
     */
    private static List<Restaurant> parseRestaurantsFromJSON(JSONObject object) throws JSONException {
        List<Restaurant> mRestaurants = new ArrayList<Restaurant>();
        JSONArray restaurants = object.getJSONArray("restaurants");
        for (int i = 0; i < restaurants.length();i++){
            JSONObject restaurant = restaurants.getJSONObject(i);
            Restaurant rnew = new Restaurant(restaurant.getString("name"), restaurant.getString("phone_number"), restaurant.getString("location"));
            rnew.setCuisine(restaurant.getString("cuisine"));
            mRestaurants.add(rnew);
        }
        return mRestaurants;
    }

    /**
     * Parses dishes from the JSON Dataset.
     * @param object The initial JSONObject
     * @return A list of dishes, parsed.
     * @throws JSONException In case something fails
     */
    private static List<Dish> parseDishesFromJSON(JSONObject object) throws JSONException {
        List<Dish> mDishes = new ArrayList<Dish>();
        JSONArray dishes = object.getJSONArray("dishes");
        for (int i = 0; i < dishes.length(); i++){
            JSONObject dish = dishes.getJSONObject(i);
            Dish mDish = new Dish(dish.getString("name"), dish.getDouble("price"));
            mDishes.add(mDish);
        }
        return mDishes;
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private Kitchen(){

    }

    private static void InitializeFromDataset(Context c) throws JSONException {
        FileToString f2s = new FileToString(R.raw.restaurant_dataset);
        String dataset = f2s.convert(c);
        JSONObject DatasetObject = null;
            DatasetObject = new JSONObject(dataset);
            restaurants = parseRestaurantsFromJSON(DatasetObject);
            dishes = parseDishesFromJSON(DatasetObject);
            mappings = parseMappingsFromJSON(DatasetObject);
            MapDishes();
        initialized = true;
    }

    /**
     * Parses mappings from the JSON Dataset.
     * @param object The initial JSONObject
     * @return A list of mappings, parsed.
     * @throws JSONException In case something fails
     */
    private static List<DishMapping> parseMappingsFromJSON(JSONObject object) throws JSONException{
        List<DishMapping> mMappings = new ArrayList<DishMapping>();
        JSONArray mappings = object.getJSONArray("mappings");
        for (int i = 0; i <mappings.length(); i++){
            JSONObject map = mappings.getJSONObject(i);
            List<Integer> mappedDishes = new ArrayList<Integer>();
            JSONArray mappingsJSON = map.getJSONArray("mapped_dishes");
            for (int j = 0 ; j < mappingsJSON.length(); j++){
                mappedDishes.add(mappingsJSON.getInt(j));
            }
            DishMapping mapping = new DishMapping(map.getInt("restaurant_id"), mappedDishes);
            mMappings.add(mapping);
        }
        return mMappings;
    }

    /**
     * Maps the dishes to each restaurant, according to the dataset.
     */
    private static void MapDishes(){
        for (DishMapping map: mappings){
            int rest_id = map.getRestaurantID();
            Restaurant restaurant = restaurants.get(rest_id);
            for (int dish_id:map.getMappedDishes()){
                Dish dish = dishes.get(dish_id);
                restaurant.addDish(dish);
            }
            updateRestaurant(restaurant, rest_id);
        }
    }

    /**
     * Update the restaurant on the given position.
     * @param restaurant
     * @param position
     */
    private static void updateRestaurant(Restaurant restaurant, int position) {
        restaurants.set(position, restaurant);
    }

    /**
     * Get all restaurants. If the lists haven't been initialized yet, initialize them.
     * @param context The context. Needed when accessing the file.
     * @return A list with all the restaurants.
     */
    public static List<Restaurant> getRestaurants(Context context){
        if (!initialized){
            try {
                InitializeFromDataset(context);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return restaurants;
    }
}
