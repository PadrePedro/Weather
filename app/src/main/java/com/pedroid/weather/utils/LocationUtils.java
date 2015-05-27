package com.pedroid.weather.utils;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by pedro on 5/26/15.
 */
public class LocationUtils {

    public boolean isLocationServicesEnabled(Context context) {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

}
