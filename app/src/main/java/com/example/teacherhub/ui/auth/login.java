package com.example.teacherhub.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.token;
import com.example.teacherhub.ui.admin.Admin;
import com.example.teacherhub.ui.student.Student;
import com.example.teacherhub.util.JwtUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    EditText Email;
    EditText passwd;
    private static final Map<String, Class<?>> roleActivityMap = new HashMap<>();

    static {
        roleActivityMap.put("ADMIN", Admin.class);
        roleActivityMap.put("USER", Student.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button Blogin = findViewById(R.id.login);
        Button Bregister = findViewById(R.id.Aregis);
        Email = findViewById(R.id.email);
        passwd = findViewById(R.id.passwd);

        Blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }

    public void login() {
        String url = "https://spr-test-deploy.onrender.com/auth/login";
        final String email = Email.getText().toString();
        final String password = passwd.getText().toString();

        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("email", email);
            jsonParam.put("password", password);

            Log.d("LoginActivity", "Request payload: " + jsonParam.toString());

            JsonObjectRequest stringRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("LoginActivity", "Response: " + response.toString());

                                if (response.has("token")) {
                                    String token = response.getString("token");
                                    Log.d("LoginActivity", "Token: " + token);

                                    JSONObject body = JwtUtil.decoded(token);
                                    Log.d("LoginActivity", "Decoded body: " + body.toString());

                                    if (body.has("user_role") && body.has("user_id") && body.has("sub")) {
                                        String user_role = body.getString("user_role");
                                        String user_id = body.getString("user_id");
                                        String sub = body.getString("sub");

                                        token myToken = new token(user_role, user_id, sub, token);

                                        Log.d("LoginActivity", "User role: " + user_role);
                                        Log.d("LoginActivity", "User ID: " + user_id);
                                        Log.d("LoginActivity", "Sub: " + sub);

                                        Class<?> targetActivity = roleActivityMap.get(myToken.getUser_role());
                                        if (targetActivity == null) {
                                            targetActivity = login.class; // or any default activity you want
                                            Log.w("LoginActivity", "Unknown role, defaulting to login activity");
                                        }

                                        startActivity(new Intent(getApplicationContext(), targetActivity));
                                    } else {
                                        String missingKeysMessage = "Token does not contain expected data";
                                        Log.e("LoginActivity", missingKeysMessage);
                                        Toast.makeText(getApplicationContext(), missingKeysMessage, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    String missingTokenMessage = "Response does not contain token";
                                    Log.e("LoginActivity", missingTokenMessage);
                                    Toast.makeText(getApplicationContext(), missingTokenMessage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                String errorMessage = "Error parsing token: " + e.getMessage();
                                Log.e("LoginActivity", errorMessage, e);
                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            String errorMessage = "Error al procesar volley";
                            if (volleyError.networkResponse != null) {
                                errorMessage += ": " + new String(volleyError.networkResponse.data);
                            } else {
                                errorMessage += ": " + volleyError.getMessage();
                            }
                            Log.e("LoginActivity", errorMessage, volleyError);
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
            );

            Volley.newRequestQueue(this).add(stringRequest);
        } catch (JSONException e) {
            String jsonErrorMessage = "Error al procesar el json: " + e.getMessage();
            Log.e("LoginActivity", jsonErrorMessage, e);
            Toast.makeText(getApplicationContext(), jsonErrorMessage, Toast.LENGTH_SHORT).show();
        }
    }


}