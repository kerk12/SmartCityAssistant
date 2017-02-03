package com.kerk12.smartcityassistant;

/**
 * Created by kerk12 on 3/2/2017.
 */

public class SmartDevice {
    private String name;
    private boolean activated = false;
    public SmartDevice(String name, boolean activated){
        this.name = name;
        this.activated = activated;
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
}
