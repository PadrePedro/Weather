package com.pedroid.weather.api;

/**
 * Created by pedro on 5/21/15.
 */
public interface RequestListener {

    void onSuccess(IRequest request);
    void onFailure(IRequest request, String reason);
}
