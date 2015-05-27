package com.pedroid.weather.api;

/**
 * Created by pedro on 5/22/15.
 */
public interface IRequestFactory {
    IConditionsRequest getConditionsRequest(String location, IRequestListener listener);
    IConditionsRequest getConditionsRequest(double lat, double lon, IRequestListener listener);
    ILocationRequest getLocationRequest(String location, IRequestListener listener);
}
