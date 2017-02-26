package com.kerk12.smartcityassistant;

/**
 * Class used for modelling Elders. Used in ElderMonitoring.
 */
public class Elder {
    private String name, location;

    /**
     * GOOD: The elder is fine
     * NEEDS_ATTENTION: Needs help ASAP
     * NEEDS_ATTENTION_FINE: The elder is fine but needs something else.
     * EMERGENCY: An ambulance has been called.
     */
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
