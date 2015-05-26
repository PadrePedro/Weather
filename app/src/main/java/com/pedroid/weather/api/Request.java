package com.pedroid.weather.api;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by pedro on 5/21/15.
 *
 * Base request runner, whichs executes a requests and reports back the success/failure status
 *
 */
public abstract class Request implements IRequest {

    private IRequestListener listener;
    private final String UTF8 = "UTF-8";
    private final String TAG = getClass().getSimpleName();

    public Request(IRequestListener listener) {
        this.listener = listener;
    }
    public void setListener(IRequestListener listener) {
        this.listener = listener;
    }

    /**
     * Calls doExecute() method on subclass and reports back the status to the listener
     */
    @Override
    public void run() {

        try {
            doExecute();
        }
        catch (Exception e) {
            if (listener != null) {
                listener.onFailure(this, e.getMessage());
                return;
            }
        }
        if (listener != null)
            listener.onSuccess(this);
    }

    /**
     * URL encoder
     *
     * @param url
     * @return encoded url
     */
    protected String urlEncoder(String url) {
        try {
            return URLEncoder.encode(url, UTF8);
        }
        catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    /**
     * GET request
     *
     * @param path path to get accessed
     * @return response string from the GET request
     * @throws Exception
     */
    protected String httpGetRequest(String path) throws Exception {
        Log.d(TAG, "httpGetRequest " + path);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(path);
        HttpResponse response = httpClient.execute(httpGet);
        if (response != null) {
            String message = EntityUtils.toString(response.getEntity(), UTF8);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Log.d(TAG, message);
                return message;
            }
            else {
                Log.e(TAG, "empty response");
            }
        }
        return null;
    }
}
