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

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private final List<Course> cours;
    private final Context context;
    private static OnButtonClickListener ButtonClickListener;

    public CourseAdapter(List<Course> cours, Context context, OnButtonClickListener onButtonClickListener) {
        this.cours = cours;
        this.context = context;
        ButtonClickListener = onButtonClickListener;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Button  btnModify, btnDelete;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameCurse);
            btnModify = itemView.findViewById(R.id.btnModifyCourse);
            btnDelete = itemView.findViewById(R.id.btnDeleteCourse);

            btnModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ButtonClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            ButtonClickListener.onModifyButtonClick(position);
                        }
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ButtonClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            ButtonClickListener.onDeleteButtonClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listcourse_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position){
        Course course = cours.get(position);
        holder.name.setText(course.getName());
    }

    @Override
    public int getItemCount() {return cours.size();}


    public interface  OnButtonClickListener{
        void onModifyButtonClick(int position);
        void onDeleteButtonClick(int position);
    }


}
