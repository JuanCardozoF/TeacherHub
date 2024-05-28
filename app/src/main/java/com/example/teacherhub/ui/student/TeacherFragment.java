package com.example.teacherhub.ui.student;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.Teacher;
import com.example.teacherhub.models.token;
import com.example.teacherhub.util.adapter.TeachersAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherFragment extends Fragment {

    private List<Teacher> teachers = new ArrayList<>();
    private RequestQueue requestQueue;
    private TeachersAdapter teacherAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        teacherAdapter = new TeachersAdapter(teachers);
        recyclerView.setAdapter(teacherAdapter);

        EditText searchInput = view.findViewById(R.id.search_input);
        Button searchButton = view.findViewById(R.id.search_button);
        Button pdfButton = view.findViewById(R.id.pdf_button);

        fetchTeachers();

        searchButton.setOnClickListener(v -> {
            // Implementar lógica de búsqueda aquí si es necesario
        });

        pdfButton.setOnClickListener(v -> {
            generatePDF(teachers);
        });

        return view;
    }

    private void fetchTeachers() {
        Context context = getContext();
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/teachers";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        teachers.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject teacherObject = response.getJSONObject(i);
                                String id = teacherObject.getString("id");
                                Teacher teacher = new Teacher(id, teacherObject.getString("name"));
                                teachers.add(teacher);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Error parsing teacher data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Error parsing teacher data", e);
                            }
                        }
                        teacherAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "Error fetching teachers: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error fetching teachers", error);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json;charset=UTF-8");
                headers.put("Authorization", "Bearer " + token.getInstanceToke().getTokenSring());
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }


    private void generatePDF(List<Teacher> teachers) {

    }
}

