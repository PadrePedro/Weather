package com.pedroid.weather.api;

/**
 * Created by pedro on 5/22/15.
 */
public interface IRequest extends Runnable {

    void doExecute() throws Exception;
}
