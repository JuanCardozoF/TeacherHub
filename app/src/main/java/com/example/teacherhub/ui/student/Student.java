package com.example.teacherhub.ui.student;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.teacherhub.R;
import com.example.teacherhub.databinding.ActivityStudentBinding;
import com.example.teacherhub.ui.admin.CoursesFragment;


public class Student extends AppCompatActivity {
    ActivityStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        replaceFragment(new CoursesFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new CoursesFragment();
                    break;
                case R.id.teachers:
                    selectedFragment = new TeacherFragment();
                    break;
                case R.id.profile:
                    selectedFragment = new ProfileFragment();
                    break;
                default:
                    return false;
            }
            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
