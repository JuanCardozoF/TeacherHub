package com.example.teacherhub.ui.student;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.Course;
import com.example.teacherhub.models.Teacher;

import com.example.teacherhub.util.adapter.Courses2Adapter;
import com.example.teacherhub.util.adapter.CoursesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherCoursesActivity extends AppCompatActivity {

    private TextView teacherName;
    private RecyclerView coursesList;
    private Courses2Adapter coursesAdapter;
    private List<Course> courseList;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_courses);

        teacherName = findViewById(R.id.teacher_name);
        coursesList = findViewById(R.id.courses_list);
        requestQueue = Volley.newRequestQueue(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("teacher")) {
            Teacher teacher = (Teacher) extras.getSerializable("teacher");
            Toast.makeText(this, "Id: " + teacher.getId(), Toast.LENGTH_SHORT).show();
            Log.d("TeacherCoursesActivity", "Id: " + teacher.getId());

            if (teacher != null) {
                setupRecyclerView(teacher);
                getCoursesByTeacherId(teacher.getId());
                courseList = new ArrayList<>();
                coursesAdapter = new Courses2Adapter(TeacherCoursesActivity.this, courseList, teacher);

                coursesList.setLayoutManager(new LinearLayoutManager(this));
                coursesList.setAdapter(coursesAdapter);
            }
        }




    }

    private void setupRecyclerView(Teacher teacher) {
        teacherName.setText(teacher.getName());
    }

    private void displayCourses(List<Course> courses) {
        courseList.clear();
        courseList.addAll(courses);
        coursesAdapter.notifyDataSetChanged();
    }

    private void getCoursesByTeacherId(String teacherId) {
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/teacherSubject/" + teacherId;

        String token = com.example.teacherhub.models.token.getInstanceToke().getTokenSring();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Course> courses = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONObject subjectObject = jsonObject.getJSONObject("subject");
                                String subjectName = subjectObject.getString("name");

                                String subjectId = jsonObject.getString("id");
                                Course course = new Course(subjectId , subjectName);
                                courses.add(course);
                            }

                            courseList.clear();
                            courseList.addAll(courses);

                            if (coursesAdapter != null) {
                                coursesAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("TeacherCoursesActivity", "coursesAdapter es nulo");
                            }
                        } catch (JSONException e) {
                            Log.e("TeacherCoursesActivity", "Error parsing JSON response: " + e.getMessage(), e);
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TeacherCoursesActivity", "Error making API request: " + error.getMessage(), error);
                        Toast.makeText(TeacherCoursesActivity.this, "Error haciendo la solicitud a la API", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(request);
    }


}

