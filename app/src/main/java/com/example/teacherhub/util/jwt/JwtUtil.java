package com.example.teacherhub.util.jwt;

import android.util.Base64;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JwtUtil {
    public static JSONObject decoded(@NonNull String JWTEncoded) throws UnsupportedEncodingException, JSONException {
        try {
            String[] split = JWTEncoded.split("\\.");
            String body = getJson(split[1]);
            return new JSONObject(body);
        } catch (UnsupportedEncodingException | JSONException e) {
            throw new IllegalArgumentException("Token is invalid: " + e.getMessage(), e);
        }
    }

    @NonNull
    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

}
