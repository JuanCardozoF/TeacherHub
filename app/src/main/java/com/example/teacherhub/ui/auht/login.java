package com.example.teacherhub.ui.auht;

import android.content.Intent;
import android.os.Bundle;
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

public class login extends AppCompatActivity {

    EditText Email;
    EditText passwd;

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

    public void login(){
        String url = "https://spr-test-deploy.onrender.com/auth/login";
        final String email = Email.getText().toString();
        final String password = passwd.getText().toString();
        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("email",email);
            jsonParam.put("password",password);
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParam, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String token = response.getString("token");
                        JSONObject body = JwtUtil.decoded(token);
                        String user_role = body.getString("user_role");
                        String user_id = body.getString("user_id");
                        String sub = body.getString("sub");
                        token myToken = new token(user_role,user_id,sub, token);

                        Class<?> targetActivity;
                        if ("ADMIN".equals(myToken.getUser_role())) {
                            targetActivity = Admin.class;
                        } else {
                            targetActivity = Student.class;
                        }
                        startActivity(new Intent(getApplicationContext(), targetActivity));

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al procesar la respuesta" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), "Error al procesar la respuesta" + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(this).add(stringRequest);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error al procesar la respuesta" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}