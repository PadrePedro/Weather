package com.pedroid.weather.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by pedro on 5/23/15.
 */
public class BroadcastUtils {

    public static void broadcastString(Context context, String key, String value) {
        Intent intent = new Intent(key);
        intent.putExtra("STRING", value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
