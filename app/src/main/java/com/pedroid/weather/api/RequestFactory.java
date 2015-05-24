package com.pedroid.weather.api;

import com.pedroid.weather.api.openweathermap.OpenWeatherMapRequestFactory;

/**
 * Created by pedro on 5/22/15.
 */
public class RequestFactory {

    private static IWeatherRequestFactory instance;

    public static IWeatherRequestFactory getInstance() {
        if (instance == null) {
            instance = new OpenWeatherMapRequestFactory();
        }
        return instance;
    }

//    @Override
//    public IConditionsRequest getConditionsRequest(String location, RequestListener listener) {
//        return null;
//    }
//
//    @Override
//    public ILocationRequest getLocationRequest(String location, RequestListener listener) {
//        return null;
//    }
}
