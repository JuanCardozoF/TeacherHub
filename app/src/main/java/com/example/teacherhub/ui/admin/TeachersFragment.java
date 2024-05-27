package com.example.teacherhub.ui.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.teacherhub.R;
import com.example.teacherhub.models.Course;
import com.example.teacherhub.models.Teacher;
import com.example.teacherhub.models.token;
import com.example.teacherhub.util.adapter.CourseAdapter;
import com.example.teacherhub.util.adapter.TeacherAdapter;
import com.example.teacherhub.util.helpers.AlertHelper;
import com.example.teacherhub.util.helpers.CrudHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeachersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeachersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String BASE_URL = "https://spr-test-deploy.onrender.com";
    private static final String TEACHER_URL = BASE_URL + "/teacherhub/api/teachers";
    private final CrudHelper<Teacher> teacherCrudHelper = new CrudHelper<>(null, TEACHER_URL, null);
    private static RecyclerView recyclerView;

    private String mParam1;
    private String mParam2;

    public TeachersFragment() {
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
        teacherCrudHelper.setContextC(getContext());
        TextView name = root.findViewById(R.id.textView3);
        name.setText("Teachers");

        fetchTeacher();

        Button addButton = root.findViewById(R.id.botonAgregar);
        addButton.setOnClickListener(v -> {
            createNewUser();
        });

        addButton.setText("Agregar profesor");

        return root;
    }

    private void createNewUser() {
        teacherCrudHelper.setUrl(TEACHER_URL);
        AlertHelper.InputAlert(getContext(), "Crear materia", "name", "Crear", "Cancelar", new AlertHelper.OnInputListener() {
            @Override
            public void onInput(String input) {
                Teacher teacher = new Teacher(UUID.randomUUID().toString(),input);
                teacherCrudHelper.setJsonObject(teacher);
                teacherCrudHelper.Create(new CrudHelper.VolleyCallback<Teacher>() {
                    @Override
                    public void onSuccess(ArrayList<Teacher> result) {
                        fetchTeacher();
                    }
                    @Override
                    public void onError(String error) {
                    }
                });
            }
        });
    }

    private void fetchTeacher() {
        ArrayList<Teacher> teachers = new ArrayList<>();
        Context context = getContext();
        TypeToken<ArrayList<Teacher>> typeToken = new TypeToken<ArrayList<Teacher>>() {};

        teacherCrudHelper.setUrl(TEACHER_URL);
        teacherCrudHelper.Read(typeToken, new CrudHelper.VolleyCallback<Teacher>() {
            @Override
            public void onSuccess(ArrayList<Teacher> result) {
                teachers.addAll(result);
                setupRecyclerView(teachers, context);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, "Error al obtener los profesores", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(ArrayList<Teacher> teachers, Context context) {
        TeacherAdapter teacherAdapter = new TeacherAdapter(teachers, context, new TeacherAdapter.OnButtonClickListener() {
            @Override
            public void onModifyButtonClick(int position) {
                Teacher teacher = teachers.get(position);
                showModifyDialog(context, teacher);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                Teacher teacher = teachers.get(position);
                deleteUser(teacher.getId());
            }

            @Override
            public void onAddCourseButtonClick(int position) {
                fetchCourses(context, teachers.get(position).getId());
            }
        });

        recyclerView.setAdapter(teacherAdapter);
    }

    private void showModifyDialog(Context context, Teacher teacher) {
        AlertHelper.InputAlert(context, "Modificar materia", "nombre", "Modificar", "Cancelar", new AlertHelper.OnInputListener() {
            @Override
            public void onInput(String input) {
                modifyUser(input, teacher);
            }
        });
    }

    private void fetchCourses(Context context, String teacherId) {
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/subjects";
        CrudHelper<Course> courseCrudHelper = new CrudHelper<>(context, url, null);
        TypeToken<ArrayList<Course>> typeToken = new TypeToken<ArrayList<Course>>() {};

        courseCrudHelper.Read(typeToken, new CrudHelper.VolleyCallback<Course>() {
            @Override
            public void onSuccess(ArrayList<Course> result) {
                showAddCourseDialog(context, result, teacherId);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(context, "Error al obtener los cursos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddCourseDialog(Context context, ArrayList<Course> courses, String teacherId) {
        AlertHelper.DropdownAlert(context, "Añadir curso", courses, "Agregar", "Cancelar", new AlertHelper.OnDropdownSelectedListener() {
            @Override
            public void onDropdownSelected(String selectedOption) {
                addCourses(selectedOption, teacherId);
            }
        });
    }

    private void modifyUser(String name,Teacher teacher) {
        teacher.setName(name);
        teacherCrudHelper.setJsonObject(teacher);
        teacherCrudHelper.setUrl(TEACHER_URL);
        teacherCrudHelper.update(new CrudHelper.VolleyCallback<Teacher>() {
            @Override
            public void onSuccess(ArrayList<Teacher> result) {
                fetchTeacher();
            }
            @Override
            public void onError(String error) {

            }
        });

    }

    private void deleteUser(String id) {
        String url = TEACHER_URL + "/" + id;
        teacherCrudHelper.setUrl(url);
        teacherCrudHelper.delete(new CrudHelper.VolleyCallback<Teacher>() {
            @Override
            public void onSuccess(ArrayList<Teacher> result) {
                fetchTeacher();
            }
            @Override
            public void onError(String error) {
            }
        });
    }

    private void addCourses(String idS, String idT) {
        String url = BASE_URL + "/teacherhub/api/teacherSubject";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("id", UUID.randomUUID().toString());
            jsonParams.put("idTeacher", idT);
            jsonParams.put("idSubject", idS);
        } catch (JSONException e) {
            Log.e("AddCourse", "Error al crear los parámetros JSON", e);
            Toast.makeText(getContext(), "Error al crear los parámetros JSON", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Se ha agregado el curso correctamente", Toast.LENGTH_SHORT).show();
                        fetchTeacher();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error al procesar la solicitud";
                        if (error.networkResponse != null) {
                            errorMessage += ": " + new String(error.networkResponse.data);
                        } else {
                            errorMessage += ": " + error.getMessage();
                        }
                        Log.e("AddCourse", errorMessage, error);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return teacherCrudHelper.getAuthorizationHeaders();
            }
        };

        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest);
    }

}