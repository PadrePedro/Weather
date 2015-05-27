package com.pedroid.weather.api;

/**
 * Created by pedro on 5/22/15.
 *
 * Location request (currently not used)
 */
public interface ILocationRequest extends IRequest {
    void setSearch(String search);
    String getDescription();
}
