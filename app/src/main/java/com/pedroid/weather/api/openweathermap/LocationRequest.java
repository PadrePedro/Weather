package com.pedroid.weather.api.openweathermap;

import com.pedroid.weather.api.ILocationRequest;
import com.pedroid.weather.api.Request;
import com.pedroid.weather.api.RequestListener;

/**
 * Created by pedro on 5/22/15.
 */
public class LocationRequest extends Request implements ILocationRequest {

    public LocationRequest(RequestListener listener) {
        super(listener);
    }

    @Override
    public void doExecute() throws Exception {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setSearch(String search) {

    }
}
