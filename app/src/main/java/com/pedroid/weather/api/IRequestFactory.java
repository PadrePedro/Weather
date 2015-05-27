package com.pedroid.weather.api;

/**
 * Created by pedro on 5/22/15.
 *
 * Interface for request factory
 */
public interface IRequestFactory {
    /**
     * Conditions request by location name
     *
     * @param location
     * @param listener
     * @return request object
     */
    IConditionsRequest getConditionsRequest(String location, IRequestListener listener);

    /**
     * Conditions request by lat/lon
     *
     * @param lat
     * @param lon
     * @param listener
     * @return request object
     */
    IConditionsRequest getConditionsRequest(double lat, double lon, IRequestListener listener);

    /**
     * Location request
     *
     * @param location
     * @param listener
     * @return
     */
    ILocationRequest getLocationRequest(String location, IRequestListener listener);
}
