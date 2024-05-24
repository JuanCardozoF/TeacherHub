package com.example.teacherhub.ui.stuedent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangeNicknameFragment extends Fragment {

    private EditText nicknameEditText;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_nickname, container, false);

        nicknameEditText = view.findViewById(R.id.nicknameEditText);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button exitButton = view.findViewById(R.id.exitButton);

        requestQueue = Volley.newRequestQueue(requireContext());

        saveButton.setOnClickListener(v -> saveNickname());
        exitButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    private void saveNickname() {
        String newNickname = nicknameEditText.getText().toString();
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/users/update/nickname"; // Ajustar el endpoint según sea necesario

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("nickname", newNickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    Toast.makeText(requireContext(), "Nickname updated successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                },
                error -> Toast.makeText(requireContext(), "Error updating nickname", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json;charset=UTF-8");
                headers.put("Authorization", "Bearer " + requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("token", null));
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}
