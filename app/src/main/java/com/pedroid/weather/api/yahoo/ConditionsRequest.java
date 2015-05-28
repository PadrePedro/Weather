package com.pedroid.weather.api.yahoo;

import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.IRequestListener;
import com.pedroid.weather.api.Request;
import com.pedroid.weather.utils.JsonUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pedro on 5/26/15.
 *
 * Yahoo Weather request object
 */
public class ConditionsRequest extends Request implements IConditionsRequest {

    private String location;
    private double lat, lon;
    private Conditions conditions;

    private final String REVERSE_GEOCODE = "http://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1";
    private final String QUERY_BY_LOCATION = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22#####%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    private final String QUERY_BY_GEO = "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f";

    // json mappings
    private static class Forecast implements Serializable {
        public int high;
        public int low;
    }

    private static class Location implements Serializable {
        public String city;
        public String country;
        public String region;
    }

    private static class Wind implements Serializable {
        public int direction;
        public int speed;
        public int chill;
    }

    private static class Condition implements Serializable {
        public String text;
        public int temp;
    }

    private static class Item implements Serializable {
        public Condition condition;
        public List<Forecast> forecast;
    }

    private static class Atmosphere implements Serializable {
        public int humidity;
    }

    private static class Channel implements Serializable {
        public Location location;
        public Wind wind;
        public Atmosphere atmosphere;
        public Item item;
    }

    private static class Results implements Serializable {
        public Channel channel;
    }

    private static class Query implements Serializable {
        public int count;
        public Results results;
    }

    private static class Conditions implements Serializable {
        public Query query;
    }

    private static class Address implements Serializable {
        public String city;
        public String state;
        public String country;
    }

    private static class Place implements Serializable {
        public Address address;
    }

    /**
     * Request by location
     *
     * @param location
     * @param listener
     */
    public ConditionsRequest(String location, IRequestListener listener) {
        super(listener);
        this.location = location;
    }

    /**
     * Request by geo code
     *
     * @param lat
     * @param lon
     * @param listener
     */
    public ConditionsRequest(double lat, double lon, IRequestListener listener) {
        super(listener);
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void doExecute() throws Exception {
        String queryString;
        if (lat != 0 && lon != 0) {
            location = reverseGeoCode(lat, lon);
        }
        if (location != null) {
            queryString = QUERY_BY_LOCATION.replace("#####", urlEncoder(location));
        }
        else {
            throw new IllegalArgumentException("location/lat/lon is null");
        }
        String json = httpGetRequest(queryString);
        conditions = JsonUtils.deserialize(json, Conditions.class);
        if (conditions == null || conditions.query == null || conditions.query.count == 0) {
            throw new RuntimeException(location + " not found");
        }
    }

    /**
     * Given a location name, return corresponding geocode.
     * This is needed becuse Yahoo doesn't support weather request by lat/lon.
     *
     * @param lat
     * @param lon
     * @return location name (city)
     * @throws Exception
     */
    private String reverseGeoCode(double lat, double lon) throws Exception {
        String query = String.format(REVERSE_GEOCODE, lat, lon);
        String json = httpGetRequest(query);
        Place place = JsonUtils.deserialize(json, Place.class);
        return place.address.city;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void setLocation(float lat, float lng) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String getLocation() {
        Location location = getLocationData();
        if (location != null && location.city != null) {
            return location.city;
        }
        return "";
    }

    @Override
    public String getConditions() {
        Condition condition = getCondition();
        if (condition != null && condition.text != null) {
            return condition.text;
        }
        return "";
    }

    @Override
    public double getTemperature(TempUnit unit) {
        Condition condition = getCondition();
        if (condition != null) {
            return convert(unit, condition.temp);
        }
        return 0;
    }

    @Override
    public double getHiTemperature(TempUnit unit) {
        Forecast forecast = getForecast();
        if (forecast != null) {
            return convert(unit, forecast.high);
        }
        return 0;
    }

    @Override
    public double getLoTemperature(TempUnit unit) {
        Forecast forecast = getForecast();
        if (forecast != null) {
            return convert(unit, forecast.low);
        }
        return 0;
    }

    @Override
    public double getWindVelocity(VelocityUnit unit) {
        Wind wind = getWind();
        if (wind != null) {
            return convert(unit, wind.speed);
        }
        return 0;
    }

    @Override
    public String getWindDirection() {
        return null;
    }

    @Override
    public int getHumidity() {
        Atmosphere atmosphere = getAtmosphere();
        if (atmosphere != null) {
            return atmosphere.humidity;
        }
        return 0;
    }

    /**
     * Accessor helper functions
     *
     * @return
     */
    private Channel getChannel() {
        if (    conditions != null &&
                conditions.query != null &&
                conditions.query.results != null &&
                conditions.query.results.channel != null)
            return conditions.query.results.channel;
        return null;
    }

    private Item getItem() {
        Channel channel = getChannel();
        if (channel != null)
            return channel.item;
        return null;
    }

    private Condition getCondition() {
        Item item = getItem();
        if (item != null)
            return item.condition;
        return null;
    }

    private Forecast getForecast() {
        Item item = getItem();
        if (item != null && item.forecast != null && item.forecast.size() > 0) {
            return item.forecast.get(0);
        }
        return null;
    }

    private Wind getWind() {
        Channel channel = getChannel();
        if (channel != null) {
            return channel.wind;
        }
        return null;
    }

    private Atmosphere getAtmosphere() {
        Channel channel = getChannel();
        if (channel != null) {
            return channel.atmosphere;
        }
        return null;
    }

    private Location getLocationData() {
        Channel channel = getChannel();
        if (channel != null) {
            return channel.location;
        }
        return null;
    }

    private int convert(VelocityUnit unit, int velocity) {
        if (unit == VelocityUnit.MPH) {
            return velocity;
        }
        else if (unit == VelocityUnit.KPH) {
            return (int)(velocity * 1.60934);
        }
        return 0;
    }

    private int convert(TempUnit unit, int tempFahrenheit) {
        if (unit == TempUnit.FAHRENHEIT) {
            return tempFahrenheit;
        }
        else if (unit == TempUnit.CELSIUS) {
            return (int)((tempFahrenheit - 32) * (5.0/9.0));
        }
        return 0;
    }
}
