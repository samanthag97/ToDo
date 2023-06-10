package com.example.todo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddNewTask;
import com.example.todo.MainActivity;
import com.example.todo.R;
import com.example.todo.model.ToDoModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> toDoList;
    private MainActivity mainActivity;
    private FirebaseFirestore firestore;


    public ToDoAdapter(MainActivity mainActivity, List<ToDoModel> toDoList){
        this.toDoList = toDoList;
        this.mainActivity = mainActivity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(mainActivity)
                .inflate(R.layout.task_layout,parent,false);
        firestore = FirebaseFirestore.getInstance();
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        //databaseHandler.openDatabase();
        ToDoModel item = toDoList.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    firestore.collection("task").document(item.TaskId).update("status",1);
                }else{
                    firestore.collection("task").document(item.TaskId).update("status",0);
                }
            }
        });
    }

    public int getItemCount(){
        return toDoList.size();
    }

    private boolean toBoolean(int status){
        return status!=0;
    }

    public void setTasks(List<ToDoModel> toDoList){
        this.toDoList = toDoList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return mainActivity;
    }

    public void deleteTask(int position){
        ToDoModel toDoModel = toDoList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        toDoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position){
        ToDoModel toDoModel = toDoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task",toDoModel.getTask());
        bundle.putString("id",toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(mainActivity.getSupportFragmentManager(), addNewTask.getTag());

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;

        ViewHolder(View view){
            super(view);
            checkBox = view.findViewById(R.id.checkbox);
        }

    }


}
