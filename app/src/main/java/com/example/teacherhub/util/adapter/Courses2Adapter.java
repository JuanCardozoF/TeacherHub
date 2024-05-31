package com.example.teacherhub.util.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacherhub.R;
import com.example.teacherhub.models.Course;
import com.example.teacherhub.models.Teacher;
import com.example.teacherhub.ui.student.TeacherProfileActivity;

import java.util.List;

public class Courses2Adapter extends RecyclerView.Adapter<Courses2Adapter.CourseViewHolder> {

    private Context context;
    private List<Course> courseList;
    private Teacher teacher;
    public Courses2Adapter(Context context, List<Course> courseList, Teacher teacher) {
        this.context = context;
        this.courseList = courseList;
        this.teacher = teacher;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);

        holder.courseName.setText(course.getName());

        Log.d("Courses2Adapter", "Binding course with ID: " + course.getId() + " and name: " + course.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TeacherProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("teacherId", teacher.getId());
                bundle.putString("courseId", course.getId());
                Log.e("TeacherCoursesActivity", "course id"+ teacher.getId());
                Log.e("TeacherCoursesActivity", "course id"+ course.getId());

                bundle.putString("teacherName", teacher.getName());
                bundle.putString("courseName", course.getName());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name);
        }
    }
}
