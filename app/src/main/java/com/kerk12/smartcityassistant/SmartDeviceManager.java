package com.kerk12.smartcityassistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerk12 on 3/2/2017.
 */

public class SmartDeviceManager {
    private static List<SmartDevice> deviceList = null;

    //Private constructor to block instantiation
    private SmartDeviceManager(){}

    public static List<SmartDevice> getDeviceList(){
        if (deviceList == null){
            deviceList = new ArrayList<SmartDevice>();
        }
        return deviceList;
    }

    public static void addSmartDevice(SmartDevice devNew){
        for(SmartDevice dev:deviceList){
            if (dev.getName() == devNew.getName()){
                //TODO Throw exception
                return;
            }
        }
        deviceList.add(devNew);
    }

    public static void ActivateDevice(int position){
        SmartDevice devToActivate = deviceList.get(position);
        if (devToActivate.isActivated()){
            //TODO Throw exception... or not
            return;
        }

        devToActivate.setActivated(true);
        deviceList.set(position, devToActivate);
    }
    public static void DeactivateDevice(int position){
        SmartDevice devToActivate = deviceList.get(position);
        if (!devToActivate.isActivated()){
            //TODO Throw exception... or not
            return;
        }

        devToActivate.setActivated(false);
        deviceList.set(position, devToActivate);
    }
}
