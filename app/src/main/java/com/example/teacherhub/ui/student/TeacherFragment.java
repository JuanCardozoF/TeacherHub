package com.example.teacherhub.ui.student;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.Teacher;
import com.example.teacherhub.models.token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
public class TeacherFragment extends Fragment {

    private List<Teacher> teachers = new ArrayList<>();
    private RequestQueue requestQueue;
    private LinearLayout teacherContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());

        teacherContainer = view.findViewById(R.id.teacher_container);

        EditText searchInput = view.findViewById(R.id.search_input);
        Button searchButton = view.findViewById(R.id.search_button);
        Button pdfButton = view.findViewById(R.id.pdf_button);

        fetchTeachers();

        searchButton.setOnClickListener(v -> {

        });

        pdfButton.setOnClickListener(v -> {
            generatePDF(teachers);
        });

        return view;
    }

    private void fetchTeachers() {
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/teachers";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        teachers.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject teacherObject = response.getJSONObject(i);
                                int id = teacherObject.getInt("id");
                                String name = teacherObject.getString("name");
                                // Agregar más campos según sea necesario
                                Teacher teacher = new Teacher(getString(id), name);
                                teachers.add(teacher);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        displayTeachers();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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

    private void displayTeachers() {
        teacherContainer.removeAllViews();
        if (teachers.isEmpty()) {
            TextView emptyView = new TextView(getContext());
            emptyView.setText("Loading teachers...");
            teacherContainer.addView(emptyView);
        } else {
            for (Teacher teacher : teachers) {
                TextView teacherView = new TextView(getContext());
                teacherView.setText(teacher.getName());
                teacherContainer.addView(teacherView);
            }
        }
    }



    private void generatePDF(List<Teacher> teachers) {

    }
}
