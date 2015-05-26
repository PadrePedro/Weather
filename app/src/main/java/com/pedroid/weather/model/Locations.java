package com.pedroid.weather.model;

import com.pedroid.weather.api.IRequest;
import com.pedroid.weather.api.IRequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 5/22/15.
 */
public class Locations implements IRequestListener {

    private static Locations instance;
    private IRequestListener listener;
    private ArrayList<String> locations;

    private Locations() {
        locations = new ArrayList<>();
        locations.add("Los Angeles");
        locations.add("New York");
    }

    public static Locations getInstance() {
        if (instance == null) {
            instance = new Locations();
        }
        return instance;
    }

    public void setRequestListener(IRequestListener listener) {
        this.listener = listener;
    }

    public List<String> getLocations() {
        return locations;
    }

    @Override
    public void onSuccess(IRequest request) {
        if (listener != null) {
            listener.onSuccess(request);
        }
    }

    @Override
    public void onFailure(IRequest request, String reason) {

    }
}
