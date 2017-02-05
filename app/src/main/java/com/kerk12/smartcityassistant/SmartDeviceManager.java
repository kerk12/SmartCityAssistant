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
            //DEBUG ONLY
            for (int i = 0; i < 10; i++){
                SmartDevice sdev = new SmartDevice("Smart Device " + String.valueOf(i), false, "Σαλόνι");
                addSmartDevice(sdev);
            }
            SmartDevice sdev = new SmartDevice("Καφετιέρα", false, "Σαλόνι");
            sdev.setError("Λείπει ο καφές.");
            addSmartDevice(sdev);

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


    public static void TogglePower(int position){
        SmartDevice devToActivate = deviceList.get(position);
        if (devToActivate.isActivated()){
            devToActivate.setActivated(false);
        } else {
            devToActivate.setActivated(true);
        }
        deviceList.set(position, devToActivate);

    }

    public static void SetPower(int position, boolean activated){
        SmartDevice devToActivate = deviceList.get(position);
        devToActivate.setActivated(activated);
        deviceList.set(position, devToActivate);

    }
}
