package com.example.teacherhub.util.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacherhub.R;
import com.example.teacherhub.models.Teacher;

import java.util.List;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeacherViewHolder> {

    private List<Teacher> teachers;

    public TeachersAdapter(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher teacher = teachers.get(position);
        holder.nameTextView.setText(teacher.getName());
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    static class TeacherViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.teacher_name);
        }
    }
}
