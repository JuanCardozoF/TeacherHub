package com.example.teacherhub.util.helpers;



import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.models.Teacher;
import com.example.teacherhub.models.User;
import com.example.teacherhub.models.token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class CrudHelper<T> {
    private Context context;
    private String url;
    private JSONObject jsonObject;

    public CrudHelper(Context context, String url, T object) {
        this.context = context;
        this.url = url;
        this.jsonObject = convertToJSON(object);
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setJsonObject(T jsonObject) {
        this.jsonObject = convertToJSON(jsonObject);
    }
    public void setContextC(Context context) {
        this.context = context;
    }

    public static <T> JSONObject convertToJSON (T object){
        if(object != null){
            Gson gson = new GsonBuilder().create();
            String jsonString = gson.toJson(object);
            try {
                return  new JSONObject(jsonString);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return  null;
    }

    public void Create(VolleyCallback<T> callback){
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                callback.onSuccess(null);
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    callback.onError("Error");
                }
            }){
            @Override
            public Map<String, String> getHeaders() {
                return getAuthorizationHeaders();
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void Read(TypeToken<ArrayList<T>> typeToken, VolleyCallback<T> callback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                Gson gson = new Gson();
                Type listType = typeToken.getType();
                ArrayList<T> objectList = gson.fromJson(response.toString(), listType);
                callback.onSuccess(objectList);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }, error -> {
            callback.onError(error.toString());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthorizationHeaders();
            }
        };
        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    public void update(VolleyCallback<T> callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                response -> {
                    callback.onSuccess(null);
                },
                error -> {
                    callback.onError("Error");
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthorizationHeaders();
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void delete(VolleyCallback<T> callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                response -> {
                    callback.onSuccess(null);
                },
                error -> {
                    callback.onError("Error");
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthorizationHeaders();
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void updateField(JSONObject jsonObject, VolleyCallback<T> callback) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                response -> {
                    callback.onSuccess(null);
                },
                error -> {
                    callback.onError("Error");
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthorizationHeaders();
            }
        };
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    public void addcourse(JSONObject object, VolleyCallback<T> callback){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                response -> {
                    callback.onSuccess(null);
                },
                error -> {
                    StringBuilder errorMessage = new StringBuilder("Error al procesar la solicitud");
                    if (error.networkResponse != null) {
                        errorMessage.append(": ").append(new String(error.networkResponse.data));
                    } else {
                        errorMessage.append(": ").append(error.getMessage());
                    };
                    callback.onError("Error");
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthorizationHeaders();
            }
        };

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


    public interface VolleyCallback<T> {

        void onSuccess(ArrayList<T> result);
        void onError(String error);

    }
    private Map<String, String> getAuthorizationHeaders() {
        Map<String, String> headers = new Hashtable<>();
        headers.put("Authorization", "Bearer " + token.getInstanceToke().getTokenSring());
        return headers;
    }
}
