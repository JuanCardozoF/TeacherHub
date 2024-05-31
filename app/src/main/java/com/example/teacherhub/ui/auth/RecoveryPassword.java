package com.example.teacherhub.ui.auth;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.User;
import com.example.teacherhub.models.token;
import com.example.teacherhub.util.adapter.TeacherAdapter;
import com.example.teacherhub.util.helpers.AlertHelper;
import com.example.teacherhub.util.helpers.CrudHelper;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.bouncycastle.cert.ocsp.Req;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecoveryPassword extends AppCompatActivity {

    EditText email;
    private static final String BASE_URL = "https://spr-test-deploy.onrender.com";
    private static final String USERS_URL = BASE_URL + "/teacherhub/api/users";
    private  static final String RECOVERY_URL = BASE_URL + "/auth/generateCode";
    private static final String T = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX3JvbGUiOiJBRE1JTiIsInVzZXJfaWQiOiIxODFkMGE1Ny0zODc5LTRiZWItODk4Zi03MWQzYjAxNjFmZjAiLCJzdWIiOiJqdWRjYXJkb3pvZkB1ZGlzdHJpdGFsLmVkdS5jbyIsImlhdCI6MTcxNzE2NTY0NywiZXhwIjoxNzE3MjUyMDQ3fQ.uvQUMO-9eNfT8pGXM8gvHE52L1t93Tp31Vzyg_SZAs4";
    CrudHelper<User> userCrudHelper = new CrudHelper<>(this, USERS_URL, null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recovery_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = findViewById(R.id.txtEmailRecovery);
        Button recovery = findViewById(R.id.btnRecovery);

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findUserByEmail();
            }
        });
    }

    public void findUserByEmail(){
        token t = new token("","","",T);
        TypeToken<ArrayList<User>> typeToken = new TypeToken<ArrayList<User>>() {};
        userCrudHelper.Read(typeToken, new CrudHelper.VolleyCallback<User>() {
            @Override
            public void onSuccess(ArrayList<User> result) {
                User user = null;
                for (User u : result) {
                    if (u.getEmail().equals(email.getText().toString())) {
                        user = u;
                        break;
                    }
                }
                if(user == null){
                    Toast.makeText(RecoveryPassword.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }else {
                    recoveryPassword(user);
                }
            }
            @Override
            public void onError(String error) {

            }
        });
    }

    public void recoveryPassword(User user){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("studentId",user.getId());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RECOVERY_URL, jsonParams, result ->{
                Toast.makeText(this, "Se ha envido un codigo", Toast.LENGTH_SHORT).show();
                changePassword(user);
            }, error ->{
                Toast.makeText(this, "No se ha podido enviar el codigo", Toast.LENGTH_SHORT).show();
            });
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePassword(User user){
        String url = "https://spr-test-deploy.onrender.com/auth/updatePassword";
        AlertHelper.InputAlert3(this, "Recordar contraseña", "Contrseña", "", "confirm password", "", "code", "Password2", "Cambiar", new AlertHelper.OnTripleInputListener() {
            @Override
            public void onTripleInput(String email, String pass, String code) {
                JSONObject params = new JSONObject();
                try {
                    params.put("studentId",user.getId());
                    params.put("newPassword", pass);
                    params.put("verificationCode",code);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, result->{
                    Toast.makeText(RecoveryPassword.this, "Se ha cambido la contrseña", Toast.LENGTH_SHORT).show();
                }, error->{
                    Toast.makeText(RecoveryPassword.this, "No se podido cambiar la conteseña", Toast.LENGTH_SHORT).show();
                });
                Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
            }
        });

    }
}
