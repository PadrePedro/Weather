package com.pedroid.weather.model;

import com.pedroid.weather.api.IConditionsRequest;
import com.pedroid.weather.api.IRequest;

import java.util.HashMap;

/**
 * Created by pedro on 5/25/15.
 */
public class RequestCache {

    private static RequestCache instance;
    private HashMap<String,IConditionsRequest> map;

    private RequestCache() {
        map = new HashMap<>();
    }

    public static RequestCache getInstance() {
        if (instance == null) {
            instance = new RequestCache();
        }
        return instance;
    }

    public IConditionsRequest getConditions(String location) {
        return map.get(location);
    }

    public void putConditions(String location, IConditionsRequest request) {
        map.put(location, request);
    }

    public void clear() {
        map.clear();
    }
}
