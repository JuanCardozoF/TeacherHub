package com.example.teacherhub.ui.student;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.token;
import com.example.teacherhub.util.JwtUtil;
import com.example.teacherhub.models.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private boolean showPassword = false;
    private boolean showNickname = false;
    private boolean showEmail = false;
    private user user;

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

        Button showPasswordButton = view.findViewById(R.id.showPasswordButton);
        Button showNicknameButton = view.findViewById(R.id.showNicknameButton);
        Button showEmailButton = view.findViewById(R.id.showEmailButton);
        Button closePasswordButton = view.findViewById(R.id.closePasswordButton);
        Button closeNicknameButton = view.findViewById(R.id.closeNicknameButton);
        Button closeEmailButton = view.findViewById(R.id.closeEmailButton);
        Button savePasswordButton = view.findViewById(R.id.savePasswordButton);
        Button saveNicknameButton = view.findViewById(R.id.saveNicknameButton);
        Button saveEmailButton = view.findViewById(R.id.saveEmailButton);

        showPasswordButton.setOnClickListener(v -> handleShowPassword());
        showNicknameButton.setOnClickListener(v -> handleShowNickname());
        showEmailButton.setOnClickListener(v -> handleShowEmail());
        closePasswordButton.setOnClickListener(v -> handleClosePassword());
        closeNicknameButton.setOnClickListener(v -> handleCloseNickname());
        closeEmailButton.setOnClickListener(v -> handleCloseEmail());
        savePasswordButton.setOnClickListener(v -> handleClosePassword());
        saveNicknameButton.setOnClickListener(v -> handleCloseNickname());
        saveEmailButton.setOnClickListener(v -> handleCloseEmail());


        fetchUser();

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
                        user = new user (
                                response.optString("nickname"),
                                response.optString("email"),
                                response.optString("id_role"),
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
            // Manejo de la excepción
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

    private void handleShowPassword() {
        showPassword = true;
        passwordModal.setVisibility(View.VISIBLE);
    }

    private void handleClosePassword() {
        showPassword = false;
        passwordModal.setVisibility(View.GONE);
    }

    private void handleShowNickname() {
        showNickname = true;
        nicknameModal.setVisibility(View.VISIBLE);
    }

    private void handleCloseNickname() {
        showNickname = false;
        nicknameModal.setVisibility(View.GONE);
    }

    private void handleShowEmail() {
        showEmail = true;
        emailModal.setVisibility(View.VISIBLE);
    }

    private void handleCloseEmail() {
        showEmail = false;
        emailModal.setVisibility(View.GONE);
    }
}
