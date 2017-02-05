package com.kerk12.smartcityassistant;

/**
 * Created by kerk12 on 3/2/2017.
 */

public class SmartDevice {
    private String name;
    private boolean activated = false;
    private String location = "";
    private String error = "";

    public SmartDevice(String name, boolean activated){
        this.name = name;
        this.activated = activated;
    }
    public SmartDevice(String name, boolean activated, String location){
        this.name = name;
        this.activated = activated;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean hasError(){
        if (error != ""){
            return true;
        }
        return false;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
