package com.kerk12.smartcityassistant;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.List;

import static com.kerk12.smartcityassistant.SmartDevice.DeviceCategory.LIGHTING;

/**
 * Class used for modelling smart devices. Used in SmartHome.
 */
public class SmartDevice {

    private String name;
    private boolean activated = false;
    private String location = "";
    private String error = "";

    private List<DeviceExtraSetting> extraSettings = new ArrayList<DeviceExtraSetting>();

    private DeviceCategory category = null;

    public enum DeviceCategory{LIGHTING, GENERIC, ENTERTAINMENT}

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

    public void setCategory(DeviceCategory category){
        if (category == DeviceCategory.LIGHTING || category == DeviceCategory.GENERIC || category == DeviceCategory.ENTERTAINMENT){
            this.category = category;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public DeviceCategory getCategory() {
        return category;
    }

    public void AddExtraSetting(DeviceExtraSetting extraSetting){
        this.extraSettings.add(extraSetting);
    }

    public List<DeviceExtraSetting> getExtraSettings(){
        return extraSettings;
    }

    public boolean hasExtraSettings(){
        if (extraSettings.size() > 0){
            return true;
        }
        return false;
    }

}
