package com.pedroid.weather.api.openweathermap;

import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.ILocationRequest;
import com.pedroid.weather.api.IRequest;
import com.pedroid.weather.api.Request;
import com.pedroid.weather.api.RequestListener;
import com.pedroid.weather.api.IWeatherRequestFactory;

/**
 * Created by pedro on 5/22/15.
 */
public class OpenWeatherMapRequestFactory implements IWeatherRequestFactory {

    @Override
    public IConditionsRequest getConditionsRequest(double lat, double lon, RequestListener listener) {
        return new ConditionsRequest(lat, lon, listener);
    }

    @Override
    public IConditionsRequest getConditionsRequest(String location, RequestListener listener) {
        return new ConditionsRequest(location, listener);
    }

    @Override
    public ILocationRequest getLocationRequest(String location, RequestListener listener) {
        return null;
    }
}
