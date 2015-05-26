package com.pedroid.weather.model;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.IConditionsRequest.TempUnit;
import com.pedroid.weather.api.IConditionsRequest.VelocityUnit;
import com.pedroid.weather.api.RequestProcessor;
import com.pedroid.weather.api.RequestProcessor.ThreadCount;
import com.pedroid.weather.utils.BroadcastUtils;
import com.pedroid.weather.utils.JsonUtils;

import java.util.List;

/**
 * Created by pedro on 5/23/15.
 */
public class Settings {

    private static Settings instance;
    private Context context;
    private TempUnit tempUnit = TempUnit.FAHRENHEIT;
    private VelocityUnit velocityUnit = VelocityUnit.MPH;
    private ThreadCount threadCount = ThreadCount.ONE;

    private final String PREF_TEMP = "pref.temperature";
    private final String PREF_VELOCITY = "pref.velocity";
    private final String PREF_THREADS = "pref.threads";
    private final String PREF_LOCATIONS = "pref.locations";

    private Settings(Context context) {
        this.context = context;
        tempUnit = TempUnit.values()[PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_TEMP, 0)];
        velocityUnit = VelocityUnit.values()[PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_VELOCITY, 0)];
        threadCount = ThreadCount.values()[PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_THREADS, 0)];
    }

    public static Settings getInstance(Context context) {
        if (instance == null) {
            instance = new Settings(context);
        }
        return instance;
    }

    public TempUnit getTempUnit() {
        return tempUnit;
    }

    public String getTempUnitString() {
        return tempUnit == TempUnit.FAHRENHEIT ? "F\u00B0" : "C\u00B0";
    }

    public void setTempUnit(TempUnit unit) {
        tempUnit = unit;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(PREF_TEMP, unit.ordinal()).commit();
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastUtils.MSG_REDRAW));
    }

    public VelocityUnit getVelocityUnit() {
        return velocityUnit;
    }

    public String getVelocityUnitString() {
        if (velocityUnit == VelocityUnit.MPH) {
            return "MPH";
        }
        else if (velocityUnit == VelocityUnit.KPH) {
            return "KPH";
        }
        return "";
    }

    public void setVelocityUnit(VelocityUnit unit) {
        velocityUnit = unit;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(PREF_VELOCITY, unit.ordinal()).commit();
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BroadcastUtils.MSG_REDRAW));
    }

    public void setLocations(List location) {
        String json = JsonUtils.serialize(location);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_LOCATIONS, json).commit();
    }

    public List getLocations() {
        String def = "[\"" + IConditionsRequest.CURRENT_LOCATION + "\"]";
        String json = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LOCATIONS, def);
        return JsonUtils.deserializeList(json);
    }

    public ThreadCount getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(ThreadCount count) {
        threadCount = count;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(PREF_THREADS, count.ordinal()).commit();
    }

}
