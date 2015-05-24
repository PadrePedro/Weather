package com.pedroid.weather.api;

/**
 * Created by pedro on 5/21/15.
 */
public interface RequestListener {

    void onSuccess(Request request);
    void onFailure(Request request, String reason);
}
