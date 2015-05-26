package com.pedroid.weather.api;

/**
 * Created by pedro on 5/22/15.
 *
 * Interface for conditions request
 */
public interface IConditionsRequest extends IRequest {

    final String CURRENT_LOCATION = "__current_location__";

    enum TempUnit {FAHRENHEIT, CELSIUS};
    enum VelocityUnit {MPH, KPH};

    /**
     * Sets the location for weather conditions search
     *
     * @param location location value
     */
    void setLocation(String location);

    /**
     * Sets the location for weather conditions search by geo-coordinates
     *
     * @param lat
     * @param lng
     */
    void setLocation(float lat, float lng);

    /**
     * Gets the location name.
     *
     * @return location name, which may be different than search location.
     */
    String getLocation();

    /**
     * Gets the current conditions description (i.e. cloudy)
     *
     * @return current conditions
     */
    String getConditions();

    /**
     * Gets the current temperature
     *
     * @param unit F or C
     * @return temperature
     */
    double getTemperature(TempUnit unit);

    /**
     * Gets the hi temperature of the day
     *
     * @param unit F or C
     * @return hi temperature
     */
    double getHiTemperature(TempUnit unit);

    /**
     * Gets the lo temperature of the day
     *
     * @param unit F or C
     * @return lo temperature
     */
    double getLoTemperature(TempUnit unit);

    /**
     * Gets the wind velocity
     *
     * @param unit MPH or KPH
     * @return wind velocity
     */
    double getWindVelocity(VelocityUnit unit);
    String getWindDirection();

    /**
     * Gets the humidity value
     * @return humidity, as a percent value (0-100)
     */
    int getHumidity();
}
