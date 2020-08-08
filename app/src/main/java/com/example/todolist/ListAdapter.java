package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    ArrayList<ToDoList> toDoLists;
    Context context;
    Database database;

    public ListAdapter(ArrayList<ToDoList> toDoLists, Context context) {
        this.toDoLists = toDoLists;
        this.context = context;
        database = new Database(context,"TODOLIST",null,1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.task_info,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ToDoList task = toDoLists.get(position);
        holder.name.setText(toDoLists.get(position).getName());
        holder.detail.setText(toDoLists.get(position).getDetail());
        holder.time.setText(toDoLists.get(position).getTime());
        holder.date.setText(toDoLists.get(position).getDate());
        holder.current_time.setText(toDoLists.get(position).getCurrentDate_time());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),add_update_task.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task",task);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = toDoLists.get(position).getId();
                database.delete(id);
                toDoLists.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,toDoLists.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,detail,date,time,current_time;
        ImageButton edit,delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            detail = itemView.findViewById(R.id.detail);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            current_time = itemView.findViewById(R.id.current_time);
            edit = itemView.findViewById(R.id.btnEdit);
            delete = itemView.findViewById(R.id.btnDelete);
        }
    }
    
}
