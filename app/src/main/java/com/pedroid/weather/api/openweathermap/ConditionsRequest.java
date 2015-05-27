package com.pedroid.weather.api.openweathermap;

import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.Request;
import com.pedroid.weather.utils.JsonUtils;
import com.pedroid.weather.api.IRequestListener;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pedro on 5/21/15.
 */
public class ConditionsRequest extends Request implements IConditionsRequest {

    private final String QUERY_BY_LOCATION = "http://api.openweathermap.org/data/2.5/weather?q=%s";
    private final String QUERY_BY_GEO = "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f";
    private String location;
    private Conditions conditions;
    private double lat, lon;

    private static class Coord {
        public float lon;
        public float lat;
    }

    private static class Main implements Serializable {
        public float temp;
        public float temp_min;
        public float temp_max;
        public float pressure;
        public int humidity;
    }
    private static class Wind implements Serializable {
        public float speed;
        public float deg;
    }
    private static class Weather implements Serializable {
        public int id;
        public String main;
        public String description;
        public String icon;
    }
    private static class Conditions implements Serializable {
        public Main main;
        public Wind wind;
        public String name;
        public List<Weather> weather;
        public String message;
        public int cod;
    }

    public ConditionsRequest(String location, IRequestListener listener) {
        super(listener);
        this.location = location;
    }

    public ConditionsRequest(double lat, double lon, IRequestListener listener) {
        super(listener);
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void doExecute() throws Exception {
        String queryString;
        if (location != null) {
            queryString = String.format(QUERY_BY_LOCATION, urlEncoder(location));
        }
        else if (lat != 0 && lon != 0) {
            queryString = String.format(QUERY_BY_GEO, lat, lon);
        }
        else {
            throw new IllegalArgumentException("location/lat/lon is null");
        }
        String json = httpGetRequest(queryString);
        conditions = JsonUtils.deserialize(json, Conditions.class);
        if (conditions == null || conditions.cod != 200) {
            throw new RuntimeException(conditions.message + " " + location);
        }
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void setLocation(float lat, float lng) {
        this.lat = lat;
        this.lon = lng;
    }

    @Override
    public String getLocation() {
        if (conditions != null && conditions.name != null)
            return conditions.name;
        return "";
    }

    @Override
    public double getTemperature(TempUnit unit) {
        float kelvin = 0;
        if (conditions != null && conditions.main != null)
            kelvin = conditions.main.temp;
        if (unit == TempUnit.FAHRENHEIT) {
            return kelvinToFahrenheit(kelvin);
        }
        else if (unit == TempUnit.CELSIUS) {
            return kelvinToCelsius(kelvin);
        }
        return 0;
    }


    @Override
    public double getWindVelocity(VelocityUnit unit) {
        if (conditions != null && conditions.wind != null) {
            if (unit == VelocityUnit.KPH)
                return conditions.wind.speed;
            else if (unit == VelocityUnit.MPH)
                return 0.621371 * conditions.wind.speed;
        }
        return 0;
    }

    @Override
    public String getWindDirection() {
        return "";
    }

    @Override
    public double getHiTemperature(TempUnit unit) {
        float kelvin;
        if (conditions != null && conditions.main != null) {
            kelvin = conditions.main.temp_max;
            if (unit == TempUnit.FAHRENHEIT) {
                return kelvinToFahrenheit(kelvin);
            }
            else if (unit == TempUnit.CELSIUS) {
                return kelvinToCelsius(kelvin);
            }
        }
        return 0;
    }

    @Override
    public double getLoTemperature(TempUnit unit) {
        float kelvin;
        if (conditions != null && conditions.main != null) {
            kelvin = conditions.main.temp_min;
            if (unit == TempUnit.FAHRENHEIT) {
                return kelvinToFahrenheit(kelvin);
            }
            else if (unit == TempUnit.CELSIUS) {
                return kelvinToCelsius(kelvin);
            }
        }
        return 0;
    }

    @Override
    public int getHumidity() {
        if (conditions != null && conditions.main != null) {
            return conditions.main.humidity;
        }
        return 0;
    }

    @Override
    public String getConditions() {
        String condition = null;
        if (conditions != null && conditions.weather != null && conditions.weather.size()>0) {
            Weather weather = conditions.weather.get(0);
            condition = weather.description;
        }
        return (condition==null) ? "" : condition;
    }

    private double kelvinToFahrenheit(float kelvin) {
        return (kelvin - 273.15)* 1.8000 + 32.00;
    }

    private double kelvinToCelsius(float kelvin) {
        return kelvin - 273.15;
    }
}
