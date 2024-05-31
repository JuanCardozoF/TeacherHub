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
import android.widget.TextView;
import android.widget.Toast;

import com.example.teacherhub.R;
import com.example.teacherhub.models.Course;
import com.example.teacherhub.models.TeacherCourse;
import com.example.teacherhub.models.Teacher;
import com.example.teacherhub.util.adapter.TeacherAdapter;
import com.example.teacherhub.util.helpers.AlertHelper;
import com.example.teacherhub.util.helpers.CrudHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        fetchTeachers();

        Button addButton = root.findViewById(R.id.botonAgregar);
        addButton.setText("Agregar profesor");
        addButton.setOnClickListener(v -> createNewUser());

        return root;
    }

    private void createNewUser() {
        teacherCrudHelper.setUrl(TEACHER_URL);
        AlertHelper.InputAlert(getContext(), "Crear profesor", "name", "Type the new teacher:", "Create", input -> {
            Teacher newTeacher = new Teacher(UUID.randomUUID().toString(), input);
            teacherCrudHelper.setJsonObject(newTeacher);
            teacherCrudHelper.Create(new CrudHelper.VolleyCallback<Teacher>() {
                @Override
                public void onSuccess(ArrayList<Teacher> result) {
                    fetchTeachers();
                }

                @Override
                public void onError(String error) {
                    showToast("Error al crear profesor");
                }
            });
        });
    }

    private void fetchTeachers() {
        teacherCrudHelper.setUrl(TEACHER_URL);
        teacherCrudHelper.Read(new TypeToken<ArrayList<Teacher>>() {}, new CrudHelper.VolleyCallback<Teacher>() {
            @Override
            public void onSuccess(ArrayList<Teacher> result) {
                setupRecyclerView(result, getContext());
            }

            @Override
            public void onError(String error) {
                showToast("Error al obtener los profesores");
            }
        });
    }

    private void setupRecyclerView(ArrayList<Teacher> teachers, Context context) {
        TeacherAdapter teacherAdapter = new TeacherAdapter(teachers, context, new TeacherAdapter.OnButtonClickListener() {
            @Override
            public void onModifyButtonClick(int position) {
                showModifyDialog(context, teachers.get(position));
            }

            @Override
            public void onDeleteButtonClick(int position) {
                deleteUser(teachers.get(position).getId(), null, "");
            }

            @Override
            public void onAddCourseButtonClick(int position) {
                fetchCourses(context, teachers.get(position).getId(), teachers.get(position));
            }

            @Override
            public void onDeleteCourseButtonClick(int position) {
                showAddCourseDialog(context, teachers.get(position).getCurses(), teachers.get(position).getId(), "Eliminar", "Eliminar", true, teachers.get(position));
            }
        });

        recyclerView.setAdapter(teacherAdapter);
    }

    private void showModifyDialog(Context context, Teacher teacher) {
        AlertHelper.InputAlert(context, "Modificar profesor", "nombre", "Type the new name", "Modify", input -> modifyUser(input, teacher));
    }

    private void fetchCourses(Context context, String teacherId, Teacher teacher) {
        String url = "https://spr-test-deploy.onrender.com/teacherhub/api/subjects";
        CrudHelper<Course> courseCrudHelper = new CrudHelper<>(context, url, null);

        courseCrudHelper.Read(new TypeToken<ArrayList<Course>>() {}, new CrudHelper.VolleyCallback<Course>() {
            @Override
            public void onSuccess(ArrayList<Course> result) {
                List<String> teacherCourseIds = teacher.getCurses().stream()
                        .map(Course::getId)
                        .collect(Collectors.toList());
                result.removeIf(course -> teacherCourseIds.contains(course.getId()));
                showAddCourseDialog(context, result, teacherId, "Agregar curso", "Agregar", false, null);
            }

            @Override
            public void onError(String error) {
                showToast("Error al obtener los cursos");
            }
        });
    }

    private void showAddCourseDialog(Context context, ArrayList<Course> courses, String teacherId, String title, String btnPositive, boolean delete, Teacher teacher) {
        AlertHelper.DropdownAlert(context, title, courses, btnPositive, "Cancelar", selectedOption -> {
            if (delete) {
                deleteUser(teacherId, teacher, selectedOption);
            } else {
                addCourses(selectedOption, teacherId, false);
            }
        });
    }

    private void modifyUser(String name, Teacher teacher) {
        teacher.setName(name);
        teacherCrudHelper.setJsonObject(teacher);
        teacherCrudHelper.setUrl(TEACHER_URL);
        teacherCrudHelper.update(new CrudHelper.VolleyCallback<Teacher>() {
            @Override
            public void onSuccess(ArrayList<Teacher> result) {
                showToast("Se ha modificado el usuario");
                fetchTeachers();
            }

            @Override
            public void onError(String error) {
                showToast("Error al modificar profesor");
            }
        });
    }

    private void deleteUser(String id, Teacher teacher, String idCourse) {
        String url = TEACHER_URL + "/" + id;
        teacherCrudHelper.setUrl(url);
        teacherCrudHelper.delete(new CrudHelper.VolleyCallback<Teacher>() {
            @Override
            public void onSuccess(ArrayList<Teacher> result) {
                if (teacher != null) {
                    deleteCourse(teacher, idCourse);
                } else {
                    fetchTeachers();
                    showToast("Se ha eliminado el usuario");
                }
            }

            @Override
            public void onError(String error) {
                showToast("Error al eliminar profesor");
            }
        });
    }

    private void addCourses(String idS, String idT, boolean delete) {
        String url = BASE_URL + "/teacherhub/api/teacherSubject";
        TeacherCourse params = new TeacherCourse(UUID.randomUUID().toString(), idT, idS);
        JSONObject jsonParams = null;
        try {
            jsonParams = new JSONObject(new Gson().toJson(params));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        teacherCrudHelper.setUrl(url);
        teacherCrudHelper.addcourse(jsonParams, new CrudHelper.VolleyCallback<Teacher>() {
            @Override
            public void onSuccess(ArrayList<Teacher> result) {
                if(!delete){
                    showToast("Se ha añadido el curso");
                }
                fetchTeachers();
            }

            @Override
            public void onError(String error) {
                showToast("No se ha podido añadir el curso");
            }
        });
    }

    private void deleteCourse(Teacher teacher, String idCourse) {
        teacher.getCurses().removeIf(course -> course.getId().equals(idCourse));
        teacherCrudHelper.setJsonObject(teacher);
        teacherCrudHelper.setUrl(TEACHER_URL);
        teacherCrudHelper.Create(new CrudHelper.VolleyCallback<Teacher>() {
            @Override
            public void onSuccess(ArrayList<Teacher> result) {
                for (Course course : teacher.getCurses()) {
                    addCourses(course.getId(), teacher.getId(), true);
                }
                showToast("Se ha eliminado el curso");
            }

            @Override
            public void onError(String error) {
                showToast("Error al eliminar curso del profesor");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}
