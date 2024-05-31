package com.example.teacherhub.ui.auth;

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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.User;
import com.example.teacherhub.ui.student.Student;
import com.example.teacherhub.util.helpers.AlertHelper;
import com.example.teacherhub.util.helpers.Empty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class Register extends AppCompatActivity {
    EditText email, passwd, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.emailR);
        passwd = findViewById(R.id.passwdR);
        nickname = findViewById(R.id.nickname);
        EditText conpasswdText = findViewById(R.id.Conpasswd);

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasErrors = Empty.checkEmptyField(email) ||
                        Empty.checkEmptyField(nickname) ||
                        Empty.checkEmptyField(passwd);
                if (!hasErrors) {
                    if (!conpasswdText.getText().toString().equals(passwd.getText().toString())) {
                        conpasswdText.setError("La contraseña no coincide");
                    } else {
                        register();
                    }
                }
            }
        });
    }

    public void register() {
        String url = "https://spr-test-deploy.onrender.com/auth/register";
        final String Email = email.getText().toString();
        final String Nickname = nickname.getText().toString();
        final String Password = passwd.getText().toString();
        final String id = UUID.randomUUID().toString();

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("id", id);
            jsonParam.put("email", Email);
            jsonParam.put("nickname", Nickname);
            jsonParam.put("password", Password);
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParam, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    showDialog(id);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            Volley.newRequestQueue(this).add(stringRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    public void confirmationCode(String id, String code) {
        String url = "https://spr-test-deploy.onrender.com/auth/verifyCode";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("studentId", id);
            jsonParams.put("verificationCode", code);
        } catch (JSONException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                User user = new User(id, "","","", true,"1");
                Toast.makeText(Register.this, "Validado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this, Student.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "Code is not valid", Toast.LENGTH_SHORT).show();
                showDialog(id);
            }
        }) {
            @Override
            public byte[] getBody() {
                return jsonParams.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new Hashtable<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void showDialog(String id){
        AlertHelper.InputAlert(Register.this, "Validación de cuenta", "Code ","Type the code","OK", new AlertHelper.OnInputListener() {
            @Override
            public void onInput(String input) {
                confirmationCode(id, input);
            }
        });
    }
}