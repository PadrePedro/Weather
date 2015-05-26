package com.pedroid.weather.api;

/**
 * Created by pedro on 5/21/15.
 */
public interface IRequestListener {

    void onSuccess(final IRequest request);
    void onFailure(final IRequest request, String reason);
}
