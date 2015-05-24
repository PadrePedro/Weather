package com.pedroid.weather.model;

import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.IConditionsRequest.TempUnit;
import com.pedroid.weather.api.IConditionsRequest.VelocityUnit;
/**
 * Created by pedro on 5/23/15.
 */
public class Settings {

    private static TempUnit tempUnit = TempUnit.FAHRENHEIT;
    private static VelocityUnit velocityUnit = VelocityUnit.MPH;

    public static TempUnit getTempUnit() {
        return tempUnit;
    }

    public static void setTempUnit(TempUnit unit) {
        tempUnit = unit;
    }

    public static VelocityUnit getVelocityUnit() {
        return velocityUnit;
    }

    public static void setVelocityUnit(VelocityUnit unit) {
        velocityUnit = unit;
    }

}
