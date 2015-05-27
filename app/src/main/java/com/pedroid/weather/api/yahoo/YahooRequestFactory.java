package com.pedroid.weather.api.yahoo;

/**
 * Created by pedro on 5/26/15.
 */

import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.ILocationRequest;
import com.pedroid.weather.api.IRequestListener;
import com.pedroid.weather.api.IRequestFactory;

/**
 * Created by pedro on 5/22/15.
 *
 *  Request factory for Yahoo Weather
 *
 */
public class YahooRequestFactory implements IRequestFactory {

    @Override
    public IConditionsRequest getConditionsRequest(double lat, double lon, IRequestListener listener) {
        return new ConditionsRequest(lat, lon, listener);
    }

    @Override
    public IConditionsRequest getConditionsRequest(String location, IRequestListener listener) {
        return new ConditionsRequest(location, listener);
    }

    @Override
    public ILocationRequest getLocationRequest(String location, IRequestListener listener) {
        return null;
    }
}
