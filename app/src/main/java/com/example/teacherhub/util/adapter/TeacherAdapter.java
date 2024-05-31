package com.example.teacherhub.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacherhub.R;
import com.example.teacherhub.models.Course;
import com.example.teacherhub.models.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {
    private final List<Teacher> Teachers;
    private final Context context;

    private  static TeacherAdapter.OnButtonClickListener buttonClickListener;

    public TeacherAdapter(List<Teacher> Teachers, Context context, OnButtonClickListener onButtonClickListener) {
        this.Teachers = Teachers;
        this.context = context;
        buttonClickListener = onButtonClickListener;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listteacher_card, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher Teacher = Teachers.get(position);
        holder.name.setText(Teacher.getName());
        ArrayList<Course> cours =  Teacher.getCurses();
        StringBuilder coursesText = new StringBuilder();
        for (int i = 0; i < cours.size() ; i++){
            if (i != 0) {
                coursesText.append(", ");
            }
            coursesText.append(cours.get(i).getName());
        }
        holder.list.setText(coursesText);
    }

    @Override
    public int getItemCount() {return Teachers.size();}

    public static class TeacherViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView list;
        Button btnModify, btnDelete, btnAddCourse, btnDeleteCourse;
        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTeacher);
            list = itemView.findViewById(R.id.listcurses);
            btnModify = itemView.findViewById(R.id.btnModifyTeacher);
            btnDelete = itemView.findViewById(R.id.btnDeleteTeacher);
            btnAddCourse = itemView.findViewById(R.id.btnAddcourse);
            btnDeleteCourse = itemView.findViewById(R.id.btnDeleteCourse);

            btnModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(buttonClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            buttonClickListener.onModifyButtonClick(position);
                        }
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(buttonClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            buttonClickListener.onDeleteButtonClick(position);
                        }
                    }
                }
            });

            btnAddCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(buttonClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            buttonClickListener.onAddCourseButtonClick(position);
                        }
                    }
                }
            });

            btnDeleteCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(buttonClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            buttonClickListener.onDeleteCourseButtonClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnButtonClickListener {
        void onModifyButtonClick(int position);
        void onDeleteButtonClick(int position);
        void onAddCourseButtonClick (int position);
        void onDeleteCourseButtonClick (int position);
    }

}
