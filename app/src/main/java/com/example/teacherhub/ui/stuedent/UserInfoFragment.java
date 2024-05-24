package com.example.teacherhub.ui.stuedent;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserInfoFragment extends Fragment {

    private TextView usernameTextView, emailTextView, roleTextView;
    private RequestQueue requestQueue;
    private Student studentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Student) {
            studentActivity = (Student) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_info_fragment, container, false);

        usernameTextView = view.findViewById(R.id.usernameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        roleTextView = view.findViewById(R.id.roleTextView);
        Button changePasswordButton = view.findViewById(R.id.changePasswordButton);
        Button changeNicknameButton = view.findViewById(R.id.changeNicknameButton);
        Button changeEmailButton = view.findViewById(R.id.changeEmailButton);

        requestQueue = Volley.newRequestQueue(requireContext());

        fetchUserData();

        changePasswordButton.setOnClickListener(v -> studentActivity.navigateToChangePasswordFragment());
        changeNicknameButton.setOnClickListener(v -> studentActivity.navigateToChangeNicknameFragment());
        changeEmailButton.setOnClickListener(v -> studentActivity.navigateToChangeEmailFragment());

        return view;
    }

    private void fetchUserData() {
        String token = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("token", null);
        if (token == null) return;

        String userId = "extracted_from_token";
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/users/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String username = response.getString("username");
                        String email = response.getString("email");
                        String role = response.getString("id_role");

                        usernameTextView.setText(username);
                        emailTextView.setText(email);
                        roleTextView.setText(role);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error fetching user data", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json;charset=UTF-8");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}

