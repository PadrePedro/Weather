package com.pedroid.weather.api;

/**
 * Created by pedro on 5/22/15.
 */
public interface IWeatherRequestFactory {
    IConditionsRequest getConditionsRequest(String location, RequestListener listener);
    IConditionsRequest getConditionsRequest(double lat, double lon, RequestListener listener);
    ILocationRequest getLocationRequest(String location, RequestListener listener);
}
