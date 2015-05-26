package com.pedroid.weather.model;

import com.pedroid.weather.api.IConditionsRequest;

import java.util.HashMap;

/**
 * Created by pedro on 5/25/15.
 *
 * Caches IConditionsRequest objects
 */
public class RequestCache {

    private static RequestCache instance;
    private HashMap<String,IConditionsRequest> map;

    private RequestCache() {
        map = new HashMap<>();
    }

    /**
     * Get singleton instance
     *
     * @return RequestCache instance
     */
    public static RequestCache getInstance() {
        if (instance == null) {
            instance = new RequestCache();
        }
        return instance;
    }

    /**
     * Gets IConditionsRequest object from cache
     *
     * @param location Location of IConditionsRequest
     * @return IConditionsRequest object, or null if not found
     */
    public IConditionsRequest getConditions(String location) {
        return map.get(location);
    }

    /**
     * Adds IConditionsRequest object to the cache
     *
     * @param location Location search term
     * @param request IConditionsRequest object
     */
    public void putConditions(String location, IConditionsRequest request) {
        map.put(location, request);
    }

    /**
     * Clears the cache
     */
    public void clear() {
        map.clear();
    }
}
