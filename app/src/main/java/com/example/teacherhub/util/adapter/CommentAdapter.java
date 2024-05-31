package com.example.teacherhub.util.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacherhub.R;
import com.example.teacherhub.models.grade;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<grade> gradeList;

    public CommentAdapter(List<grade> gradeList) {
        this.gradeList = gradeList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        grade grade = gradeList.get(position);
        holder.commentText.setText(grade.getComment());
        holder.noteText.setText(String.valueOf(grade.getNote()));
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText, noteText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            noteText = itemView.findViewById(R.id.noteText);
        }
    }
}