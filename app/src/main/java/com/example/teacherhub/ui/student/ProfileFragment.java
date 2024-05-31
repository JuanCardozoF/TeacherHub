package com.example.teacherhub.ui.student;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.User;
import com.example.teacherhub.models.token;
import com.example.teacherhub.util.helpers.CrudHelper;
import com.example.teacherhub.util.jwt.JwtUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private boolean showPassword = false;
    private boolean showNickname = false;
    private boolean showEmail = false;
    private User user;

    private ImageView profileImageView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView roleTextView;
    private View passwordModal;
    private View nicknameModal;
    private View emailModal;
    private EditText passwordInput;
    private EditText nicknameInput;
    private EditText emailInput;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static final String BASE_URL = "https://spr-test-deploy.onrender.com";
    private static final String USERS_URL = BASE_URL + "/teacherhub/api/users";
    private final CrudHelper<User> userCrudHelper = new CrudHelper<>(null, USERS_URL, null);
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        initializeButtons(view);
        fetchUser();
    }

    private void initializeViews(View view) {
        profileImageView = view.findViewById(R.id.profileImageView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        roleTextView = view.findViewById(R.id.roleTextView);
        passwordModal = view.findViewById(R.id.passwordModal);
        nicknameModal = view.findViewById(R.id.nicknameModal);
        emailModal = view.findViewById(R.id.emailModal);
        passwordInput = view.findViewById(R.id.passwordInput);
        nicknameInput = view.findViewById(R.id.nicknameInput);
        emailInput = view.findViewById(R.id.emailInput);
    }

    private void initializeButtons(View view) {
        Button showPasswordButton = view.findViewById(R.id.showPasswordButton);
        Button showNicknameButton = view.findViewById(R.id.showNicknameButton);
        Button showEmailButton = view.findViewById(R.id.showEmailButton);
        Button closePasswordButton = view.findViewById(R.id.closePasswordButton);
        Button closeNicknameButton = view.findViewById(R.id.closeNicknameButton);
        Button closeEmailButton = view.findViewById(R.id.closeEmailButton);
        Button savePasswordButton = view.findViewById(R.id.savePasswordButton);
        Button saveNicknameButton = view.findViewById(R.id.saveNicknameButton);
        Button saveEmailButton = view.findViewById(R.id.saveEmailButton);

        showPasswordButton.setOnClickListener(v -> handleShowModal(passwordModal));
        showNicknameButton.setOnClickListener(v -> handleShowModal(nicknameModal));
        showEmailButton.setOnClickListener(v -> handleShowModal(emailModal));
        closePasswordButton.setOnClickListener(v -> handleCloseModal(passwordModal));
        closeNicknameButton.setOnClickListener(v -> handleCloseModal(nicknameModal));
        closeEmailButton.setOnClickListener(v -> handleCloseModal(emailModal));
        savePasswordButton.setOnClickListener(v -> handleSave(passwordInput.getText().toString(), "password"));
        saveNicknameButton.setOnClickListener(v -> handleSave(nicknameInput.getText().toString(), "nickname"));
        saveEmailButton.setOnClickListener(v -> handleSave(emailInput.getText().toString(), "email"));
    }

    private void handleShowModal(View modal) {
        modal.setVisibility(View.VISIBLE);
    }

    private void handleCloseModal(View modal) {
        modal.setVisibility(View.GONE);
    }



    private void handleSave(String value, String type) {
        final String mytoken = token.getInstanceToke().getTokenSring();
        String userId = user.getId();
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/users/" + userId;

        JSONObject jsonBody = new JSONObject();
        try {
            switch (type) {
                case "nickname":
                    jsonBody.put("nickname", value);
                    break;
                case "password":
                    jsonBody.put("password", value);
                    break;
                case "email":
                    jsonBody.put("email", value);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                response -> {
                    // Manejar la respuesta del servidor
                    System.out.println("Respuesta del servidor: " + response.toString());
                    // Aquí podrías actualizar la UI con los nuevos datos
                    updateUI();
                },
                error -> {
                    // Manejar el error
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        if (statusCode == 405) {
                            // Método no permitido
                            Toast.makeText(getContext(), "Error: Método no permitido (405)", Toast.LENGTH_SHORT).show();
                        } else {
                            // Otro error de red
                            Toast.makeText(getContext(), "Error de red: " + statusCode, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Error desconocido
                        Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_SHORT).show();
                    }
                    error.printStackTrace();
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + mytoken);
                return headers;
            }
        };



        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }





    private void updateField(String userId, JSONObject jsonObject) {
        userCrudHelper.updateField(jsonObject, new CrudHelper.VolleyCallback<User>() {
            @Override
            public void onSuccess(ArrayList<User> result) {}

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), "Error actualizando " + jsonObject.keys().next(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUser() {
        try {
            final String mytoken = token.getInstanceToke().getTokenSring();
            JSONObject decodedToken = JwtUtil.decoded(mytoken);
            String userId = decodedToken.optString("user_id");
            String url = "https://spr-test-deploy.onrender.com/teacherhub/api/users/" + userId;
            RequestQueue queue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        user = new User(
                                response.optString("nickname"),
                                response.optString("email"),
                                response.optString("id_role"),
                                "",
                                true,
                                response.optString("user_id")
                        );
                        updateUI();
                    },
                    error -> {
                        Toast.makeText(getContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json;charset=UTF-8");
                    headers.put("Access-Control-Allow-Origin", "*");
                    headers.put("Authorization", "Bearer " + mytoken);
                    return headers;
                }
            };

            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        if (user != null) {
            usernameTextView.setText(user.getUsername());
            emailTextView.setText(user.getEmail());
            roleTextView.setText(user.getId_role());
        }
    }


}

