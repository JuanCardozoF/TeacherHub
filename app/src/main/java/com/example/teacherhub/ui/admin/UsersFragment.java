package com.example.teacherhub.ui.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import java.util.UUID;

import com.example.teacherhub.R;
import com.example.teacherhub.models.User;
import com.example.teacherhub.util.adapter.UserAdapter;
import com.example.teacherhub.util.helpers.AlertHelper;
import com.example.teacherhub.util.helpers.CrudHelper;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String BASE_URL = "https://spr-test-deploy.onrender.com";
    private static final String USERS_URL = BASE_URL + "/teacherhub/api/users";
    private static final String REGISTER_URL = BASE_URL + "/auth/register";
    private static  RecyclerView recyclerView;

    private final CrudHelper<User> userCrudHelper = new CrudHelper<>(null, USERS_URL, null);

    private String mParam1;
    private String mParam2;

    public UsersFragment() {
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
        userCrudHelper.setContextC(getContext());
        fetchUsers();

        Button addButton = root.findViewById(R.id.botonAgregar);
        addButton.setOnClickListener(v -> createNewUser());

        return root;
    }

    private void createNewUser() {
        AlertHelper.InputAlert3(getContext(), "Crear usuario", "Nickname", "", "Email", "", "Contraseña", "Password", "Cancelar",
                (name, email, password) -> {
                    User newUser = new User(UUID.randomUUID().toString(), name, email, password, false, "1");
                    userCrudHelper.setJsonObject(newUser);
                    userCrudHelper.setUrl(REGISTER_URL);
                    userCrudHelper.Create(new CrudHelper.VolleyCallback<User>() {
                        @Override
                        public void onSuccess(ArrayList<User> result) {
                            fetchUsers();
                        }
                        @Override
                        public void onError(String error) {

                        }
                    });
                }
        );
    }

    private void fetchUsers() {
        ArrayList<User> users = new ArrayList<>();
        Context context = getContext();
        TypeToken<ArrayList<User>> typeToken = new TypeToken<ArrayList<User>>() {};

        userCrudHelper.setUrl(USERS_URL);
        userCrudHelper.Read(typeToken, new CrudHelper.VolleyCallback<User>() {
            @Override
            public void onSuccess(ArrayList<User> userList) {
                users.addAll(userList);
                setupRecyclerView(users, context);
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(ArrayList<User> users, Context context) {
        UserAdapter userAdapter = new UserAdapter(users, context, new UserAdapter.OnButtonClickListener() {
            @Override
            public void onModifyButtonClick(int position) {
                User currentUser = users.get(position);
                showModifyUserDialog(context, currentUser);
            }

            @Override
            public void onDeleteButtonClick(int position) {
                User currentUser = users.get(position);
                deleteUser(currentUser.getId());
            }
        });
        recyclerView.setAdapter(userAdapter);
    }

    private void showModifyUserDialog(Context context, User user) {
        AlertHelper.InputAlert3(context, "Modificar usuario", "Nickname", user.getUsername(), "Email", user.getEmail(), "Id_rol", "",
                "Modificar", (nickname, email, idRole) -> modifyUser(nickname, email, idRole, user));
    }


    private void modifyUser(String nickname, String email, String idRole, User user) {
        user.setUsername(nickname);
        user.setEmail(email);
        user.setId_role(idRole);
        userCrudHelper.setJsonObject(user);
        userCrudHelper.setUrl(USERS_URL);
        userCrudHelper.update(new CrudHelper.VolleyCallback<User>() {
            @Override
            public void onSuccess(ArrayList<User> result) {
                fetchUsers();
            }
            @Override
            public void onError(String error) {

            }
        });
    }

    private void deleteUser(String id) {
        String url = USERS_URL + "/" + id;
        userCrudHelper.setUrl(url);
        userCrudHelper.delete(new CrudHelper.VolleyCallback<User>() {
            @Override
            public void onSuccess(ArrayList<User> result) {
                fetchUsers();
            }
            @Override
            public void onError(String error) {
            }
        });
    }
}