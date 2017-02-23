package com.kerk12.smartcityassistant;

/**
 * Created by kgiannakis on 21/2/2017.
 */

public class Elder {
    private String name, location;
    public enum Condition{GOOD, NEEDS_ATTENTION, NEEDS_ATTENTION_FINE, EMERGENCY};
    private Condition condition;

    public Elder(String name, String location) {
        this.name = name;
        this.location = location;
        this.condition = Condition.GOOD;
    }
    public Elder(String name, String location, Condition condition) {
        this.name = name;
        this.location = location;
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
