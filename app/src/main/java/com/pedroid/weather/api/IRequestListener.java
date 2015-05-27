package com.pedroid.weather.api;

/**
 * Created by pedro on 5/21/15.
 *
 * Request listener
 */
public interface IRequestListener {

    /**
     * Called upon successful completion of request
     *
     * @param request original request object
     */
    void onSuccess(final IRequest request);

    /**
     * Called upon request failure
     *
     * @param request original request object
     * @param reason  reason for failure
     */
    void onFailure(final IRequest request, String reason);
}
