package com.pedroid.weather.api;

/**
 * Created by pedro on 5/22/15.
 */
public interface IConditionsRequest extends IRequest {

    final String CURRENT_LOCATION = "__current_location__";

    enum TempUnit {CELSIUS, FAHRENHEIT};
    enum VelocityUnit {KPH, MPH};
    enum ThreadCount {ONE, MULTI};

    void setLocation(String location);
    void setLocation(float lat, float lng);
    String getLocation();
    String getConditions();
    double getTemperature(TempUnit unit);
    double getWindVelocity(VelocityUnit unit);
    String getWindDirection();
}
