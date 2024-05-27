package com.example.teacherhub.ui.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teacherhub.R;
import com.example.teacherhub.models.Course;
import com.example.teacherhub.models.User;
import com.example.teacherhub.util.adapter.CourseAdapter;
import com.example.teacherhub.util.adapter.UserAdapter;
import com.example.teacherhub.util.helpers.AlertHelper;
import com.example.teacherhub.util.helpers.CrudHelper;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String BASE_URL = "https://spr-test-deploy.onrender.com";
    private static final String COURSE_URL = BASE_URL + "/teacherhub/api/subjects";
    private final CrudHelper<Course> courseCrudHelper = new CrudHelper<>(null, COURSE_URL, null);

    private  static  RecyclerView recyclerView;

    private String mParam1;
    private String mParam2;

    public CoursesFragment() {
        // Required empty public constructor
    }
    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
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
        View root = inflater.inflate(R.layout.fragment_all, container, false);
        recyclerView = root.findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseCrudHelper.setContextC(getContext());
        TextView name = root.findViewById(R.id.textView3);
        name.setText("Courses");

        fetchCourses();

        Button addButton = root.findViewById(R.id.botonAgregar);
        addButton.setOnClickListener(v -> {
            createNewUser();
        });

        addButton.setText("Agregar curso");

        return root;
    }

    private void createNewUser() {
        courseCrudHelper.setUrl(COURSE_URL);
        AlertHelper.InputAlert(getContext(), "Crear materia", "name", "Crear", "Cancelar", new AlertHelper.OnInputListener() {
            @Override
            public void onInput(String input) {
                Course course = new Course(UUID.randomUUID().toString(), input);
                courseCrudHelper.setJsonObject(course);
                courseCrudHelper.Create(new CrudHelper.VolleyCallback<Course>() {
                    @Override
                    public void onSuccess(ArrayList<Course> result) {
                        fetchCourses();
                    }
                    @Override
                    public void onError(String error) {
                    }
                });
            }
        });
    }

    private void fetchCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        Context context = getContext();
        TypeToken<ArrayList<Course>> typeToken = new TypeToken<ArrayList<Course>>() {};

        courseCrudHelper.setUrl(COURSE_URL);
        courseCrudHelper.Read(typeToken, new CrudHelper.VolleyCallback<Course>() {
            @Override
            public void onSuccess(ArrayList<Course> result) {
                courses.addAll(result);
                setupRecyclerView(courses, context);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(ArrayList<Course> courses, Context context) {
        CourseAdapter courseAdapter = new CourseAdapter(courses, context, new CourseAdapter.OnButtonClickListener() {
            @Override
            public void onModifyButtonClick(int position) {
                Course currentCourse = courses.get(position);
                showModifyCourseDialog(context, currentCourse);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                Course currentCourse = courses.get(position);
                deleteCourse(currentCourse.getId());
            }
        });
        recyclerView.setAdapter(courseAdapter);
    }

    private void showModifyCourseDialog(Context context, Course course) {
        AlertHelper.InputAlert(context, "Modificar materia", "nombre", "Modificar", "Cancelar", new AlertHelper.OnInputListener() {
            @Override
            public void onInput(String input) {
                modifyCourse(input, course);
            }
        });
    }


    private void modifyCourse(String name, Course course) {
        course.setName(name);
        courseCrudHelper.setJsonObject(course);
        courseCrudHelper.setUrl(COURSE_URL);
        courseCrudHelper.update(new CrudHelper.VolleyCallback<Course>() {
            @Override
            public void onSuccess(ArrayList<Course> result) {
                fetchCourses();
            }
            @Override
            public void onError(String error) {

            }
        });
    }

    private void deleteCourse(String id) {
        String url = COURSE_URL + "/" + id;
        courseCrudHelper.setUrl(url);
        courseCrudHelper.delete(new CrudHelper.VolleyCallback<Course>() {
            @Override
            public void onSuccess(ArrayList<Course> result) {
                fetchCourses();
            }
            @Override
            public void onError(String error) {
            }
        });
    }
}