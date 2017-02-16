package com.kerk12.smartcityassistant;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerk12 on 3/2/2017.
 */

public class SmartDeviceManager {
    private static List<SmartDevice> deviceList = null;

    //Private constructor to block instantiation
    private SmartDeviceManager(){}

    public static List<SmartDevice> getDeviceList(Context context){
        if (deviceList == null){
            deviceList = new ArrayList<SmartDevice>();
            SmartDevice sdev1 = new SmartDevice("Φώτα Μπάνιου", true, "Μπάνιο");
            sdev1.setCategory(SmartDevice.DeviceCategory.LIGHTING);
            SmartDevice sdev2 = new SmartDevice("Καφετιέρα", false, "Κουζίνα");
            sdev2.setCategory(SmartDevice.DeviceCategory.GENERIC);
            SmartDevice sdev3 = new SmartDevice("Τηλεόραση", false, "Σαλόνι");
            DeviceExtraSetting volume = new DeviceExtraSetting("Ένταση", DeviceExtraSetting.SLIDER);
            DeviceExtraSetting channel = new DeviceExtraSetting("Κανάλι", DeviceExtraSetting.NUM_UP_DOWN);

            sdev3.AddExtraSetting(volume);
            sdev3.AddExtraSetting(channel);
            sdev3.setCategory(SmartDevice.DeviceCategory.ENTERTAINMENT);
            addSmartDevice(sdev1);
            addSmartDevice(sdev2);
            addSmartDevice(sdev3);

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
