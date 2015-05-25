package com.pedroid.weather.model;

import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.IConditionsRequest.TempUnit;
import com.pedroid.weather.api.IConditionsRequest.VelocityUnit;
import com.pedroid.weather.api.IConditionsRequest.ThreadCount;
/**
 * Created by pedro on 5/23/15.
 */
public class Settings {

    private static TempUnit tempUnit = TempUnit.FAHRENHEIT;
    private static VelocityUnit velocityUnit = VelocityUnit.MPH;
    private static ThreadCount threadCount = IConditionsRequest.ThreadCount.ONE;

    public static TempUnit getTempUnit() {
        return tempUnit;
    }

    public static String getTempUnitString() {
        return tempUnit == TempUnit.FAHRENHEIT ? "F\u00B0" : "C\u00B0";
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

    public static ThreadCount getThreadCount() {
        return threadCount;
    }

    public static void setThreadCount(ThreadCount count) {
        threadCount = count;
    }

}
