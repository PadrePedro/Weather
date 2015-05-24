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
 */
public abstract class Request implements IRequest {

    private RequestListener listener;
    private final String UTF8 = "UTF-8";
    private final String TAG = getClass().getSimpleName();

    public Request(RequestListener listener) {
        this.listener = listener;
    }
    public void setListener(RequestListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {

        try {
            doExecute();
        }
        catch (Exception e) {
            if (listener != null)
                listener.onFailure(this, e.getMessage());
        }
        finally {
            if (listener != null)
                listener.onSuccess(this);
        }
    }

    protected String urlEncoder(String url) {
        try {
            return URLEncoder.encode(url, UTF8);
        }
        catch (UnsupportedEncodingException e) {
            return url;
        }
    }

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
