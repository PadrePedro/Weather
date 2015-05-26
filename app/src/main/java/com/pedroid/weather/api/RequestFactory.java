package com.pedroid.weather.api;

import com.pedroid.weather.api.openweathermap.OpenWeatherMapRequestFactory;

/**
 * Created by pedro on 5/22/15.
 *
 * Factory that produces an implementation of IWeatherRequestFactory
 */
public class RequestFactory {

    private static IWeatherRequestFactory instance;

    /**
     * Gets singleton instance
     *
     * @return IWeatherRequestFactory implementation
     */
    public static IWeatherRequestFactory getInstance() {
        if (instance == null) {
            instance = new OpenWeatherMapRequestFactory();
        }
        return instance;
    }

}
