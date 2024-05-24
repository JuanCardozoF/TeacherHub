package com.example.teacherhub.ui.stuedent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.teacherhub.R;

public class Student extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadFragment(new UserInfoFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void navigateToChangePasswordFragment() {
        loadFragment(new ChangePasswordFragment());
    }

    public void navigateToChangeNicknameFragment() {
        loadFragment(new ChangeNicknameFragment());
    }

    public void navigateToChangeEmailFragment() {
        loadFragment(new ChangeEmailFragment());
    }
}
