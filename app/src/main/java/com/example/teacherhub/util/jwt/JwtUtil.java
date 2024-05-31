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
    public static String getStudentId(String token) {
        try {
            JSONObject decodedToken = JwtUtil.decoded(token);
            return decodedToken.getString("user_id");
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNickname(String token) {
        try {
            JSONObject decodedToken = JwtUtil.decoded(token);
            return decodedToken.getString("nickname");
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

}
