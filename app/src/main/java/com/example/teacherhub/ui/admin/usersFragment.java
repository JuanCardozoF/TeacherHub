package com.example.teacherhub.ui.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;

import com.example.teacherhub.R;
import com.example.teacherhub.models.user;
import com.example.teacherhub.util.UserAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link usersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class usersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public usersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment usersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static usersFragment newInstance(String param1, String param2) {
        usersFragment fragment = new usersFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_users, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //falta llenar con lo del backend
        ArrayList<user> userList = new ArrayList<user>();
        userList.add(new user("codelia", "juan","jaun♣asd","123", true,"2" ));
        userList.add(new user("johndoe", "pepito", "juan@asdda","123", false,"2"));

        UserAdapter userAdapter = new UserAdapter(userList, getContext());
        recyclerView.setAdapter(userAdapter);

        return  root;
    }
}