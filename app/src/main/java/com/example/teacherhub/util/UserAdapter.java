package com.example.teacherhub.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teacherhub.R;
import com.example.teacherhub.models.user;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<user> userList;
    private final Context context;

    public UserAdapter(List<user> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        user user = userList.get(position);
        holder.nickname.setText(user.getUsername());
        holder.email.setText(user.getEmail());
        holder.activeStatus.setText(user.getIs_active() ? "Active" : "Inactive");
        holder.activeStatus.setTextColor(user.getIs_active() ?
                ContextCompat.getColor(context, android.R.color.holo_green_dark) :
                ContextCompat.getColor(context, android.R.color.holo_red_dark));
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nickname, email, activeStatus;
        Button btnModify, btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.Nick);
            email = itemView.findViewById(R.id.Correo);
            activeStatus = itemView.findViewById(R.id.ActiveStatus);
            btnModify = itemView.findViewById(R.id.btnModify);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

