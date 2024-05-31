package com.example.teacherhub.util.helpers;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkUtils {

    private static NetworkUtils instance;
    private RequestQueue requestQueue;

    private NetworkUtils(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkUtils(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void addToRequestQueue(com.android.volley.Request<T> request) {
        getRequestQueue().add(request);
    }
}