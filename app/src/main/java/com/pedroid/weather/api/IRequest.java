package com.pedroid.weather.api;

/**
 * Created by pedro on 5/22/15.
 *
 * Interface for request ojbects
 */
public interface IRequest extends Runnable {

    /**
     * Defines the work for the request
     *
     * @throws Exception
     */
    void doExecute() throws Exception;
}
