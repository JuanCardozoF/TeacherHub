package com.example.teacherhub.ui.student;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.Course;
import com.example.teacherhub.models.token;
import com.example.teacherhub.util.adapter.CoursesAdapter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CourseFragment() {
        // Required empty public constructor
    }

    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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

    private List<Course> courseList = new ArrayList<>();
    private List<Course> filteredCourseList = new ArrayList<>();
    private RequestQueue requestQueue;
    private CoursesAdapter coursesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        coursesAdapter = new CoursesAdapter(filteredCourseList);
        recyclerView.setAdapter(coursesAdapter);

        EditText searchInput = view.findViewById(R.id.search_input);
        Button searchButton = view.findViewById(R.id.search_button);
        Button pdfButton = view.findViewById(R.id.pdf_button);

        fetchTeachers();

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            filterCourses(query);
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        pdfButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                generatePDF(filteredCourseList);
            } else {
                Toast.makeText(getContext(), "Permiso de escritura en almacenamiento no concedido", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void fetchTeachers() {
        Context context = getContext();
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/subjects";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        courseList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject courseObject = response.getJSONObject(i);
                                String id = courseObject.getString("id");
                                Course course = new Course(id, courseObject.getString("name"));
                                courseList.add(course);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Error parsing subject data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Error parsing subject data", e);
                            }
                        }
                        filteredCourseList.clear();
                        filteredCourseList.addAll(courseList);
                        coursesAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "Error fetching subjects: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error fetching subjects", error);
            }
        }) {
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

    private void filterCourses(String query) {
        filteredCourseList.clear();
        if (query.isEmpty()) {
            filteredCourseList.addAll(courseList);
        } else {
            for (Course course : courseList) {
                if (course.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredCourseList.add(course);
                }
            }
        }
        coursesAdapter.notifyDataSetChanged();
    }

    private void generatePDF(List<Course> courses) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Courses.pdf";
        File file = new File(path);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);


            document.add(new Paragraph("Lista de Cursos").setFontSize(20).setBold());


            for (Course course : courses) {
                document.add(new Paragraph(course.getName()));
            }

            document.close();
            Toast.makeText(getContext(), "PDF generado en " + path, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al generar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
