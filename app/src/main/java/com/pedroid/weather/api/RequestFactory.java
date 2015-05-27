package com.pedroid.weather.api;

import com.pedroid.weather.api.openweathermap.OpenWeatherMapRequestFactory;
import com.pedroid.weather.api.yahoo.YahooRequestFactory;

/**
 * Created by pedro on 5/22/15.
 *
 * Factory that produces an implementation of IWeatherRequestFactory
 */
public class RequestFactory {

    public enum Factory {OPEN_WEATHER_MAP, YAHOO};

    private static IRequestFactory instance;

    /**
     * Gets singleton instance
     *
     * @return IRequestFactory implementation
     */
    public static IRequestFactory getInstance() {
        return instance;
    }

    /**
     * Sets the API factory
     *
     * @param factory
     */
    public static void setFactory(Factory factory) {
        if (factory == Factory.OPEN_WEATHER_MAP) {
            instance = new OpenWeatherMapRequestFactory();
        }
        else if (factory == Factory.YAHOO) {
            instance = new YahooRequestFactory();
        }
    }

}
