package com.example.teacherhub.ui.student;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teacherhub.R;
import com.example.teacherhub.models.grade;
import com.example.teacherhub.util.adapter.CommentAdapter;
import com.example.teacherhub.util.helpers.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TeacherProfileActivity extends AppCompatActivity {

    private TextView teacherName, courseName;
    private ImageView profileImage;
    private EditText commentInput, gradeInput;
    private Button submitButton;
    private RecyclerView commentsRecyclerView;
    private ProgressBar loadingSpinner;

    private String teacherSubjectId;
    private String teacherId, courseId, userId;
    private List<grade> gradesList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private int page = 0;
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        teacherName = findViewById(R.id.teacherName);
        courseName = findViewById(R.id.courseName);
        profileImage = findViewById(R.id.profileImage);
        commentInput = findViewById(R.id.commentInput);
        gradeInput = findViewById(R.id.gradeInput);
        submitButton = findViewById(R.id.submitButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        loadingSpinner = findViewById(R.id.loadingSpinner);

        Intent intent = getIntent();
        teacherId = intent.getStringExtra("teacherId");
        courseId = intent.getStringExtra("courseId");
        userId = intent.getStringExtra("userId");
        token = com.example.teacherhub.models.token.getInstanceToke().getTokenSring();

        teacherName.setText(intent.getStringExtra("teacherName"));
        courseName.setText(intent.getStringExtra("courseName"));

        commentAdapter = new CommentAdapter(gradesList);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);

        fetchTeacherSubjectId();
        fetchComments();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitGrade();
            }
        });
    }

    private void fetchTeacherSubjectId() {
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/teacherSubject/" + teacherId + "/" + courseId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            teacherSubjectId = response.getString("data");
                            Log.e(TAG, "teachersubject id " + teacherSubjectId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        NetworkUtils.getInstance(this).addToRequestQueue(request);
    }

    private void fetchComments() {
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/grades/" + teacherSubjectId + "?page=" + page + "&size=10";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            gradesList.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject gradeObj = data.getJSONObject(i);
                                grade grade = new grade(
                                        gradeObj.getString("id"),
                                        gradeObj.getString("idTeacherSubject"),
                                        gradeObj.getString("comment"),
                                        gradeObj.getBoolean("isPositive"),
                                        gradeObj.getDouble("note")
                                );
                                gradesList.add(grade);
                            }
                            commentAdapter.notifyDataSetChanged();
                            loadingSpinner.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        NetworkUtils.getInstance(this).addToRequestQueue(request);
    }

    private void submitGrade() {
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/grades";
        String comment = commentInput.getText().toString();
        String gradeStr = gradeInput.getText().toString();

        if (comment.isEmpty()) {
            Log.e(TAG, "Comment is empty");
            return;
        }
        if (gradeStr.isEmpty()) {
            Log.e(TAG, "Grade is empty");
            return;
        }
        if (teacherSubjectId == null) {
            Log.e(TAG, "TeacherSubjectId is null");
            return;
        }
        if (userId == null) {
            Log.e(TAG, "UserId is null");
            return;
        }

        double note;
        try {
            note = Double.parseDouble(gradeStr);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Grade is not a valid number");
            return;
        }

        boolean isPositive = note > 3;

        JSONObject grade = new JSONObject();
        try {
            grade.put("id", UUID.randomUUID().toString());
            grade.put("idStudent", userId);
            grade.put("idTeacherSubject", teacherSubjectId);
            grade.put("comment", comment);
            grade.put("note", note);
            grade.put("isPositive", isPositive);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, grade,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        fetchComments();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error in submitGrade: " + error.getMessage());
                if (error.networkResponse != null) {
                    Log.e(TAG, "Error response code: " + error.networkResponse.statusCode);
                    Log.e(TAG, "Error response data: " + new String(error.networkResponse.data));
                }
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        NetworkUtils.getInstance(this).addToRequestQueue(request);
    }
}

