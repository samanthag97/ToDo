package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.todo.adapter.ToDoAdapter;
import com.example.todo.model.ToDoModel;
import com.google.android.material.navigation.NavigationView;

import com.example.todo.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActivityDrawerBase {

    ActivityMainBinding activityMainBinding;
    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;

    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle(getString(R.string.homepage));

        taskList = new ArrayList<>();

        recyclerView = findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toDoAdapter = new ToDoAdapter(this);
        recyclerView.setAdapter(toDoAdapter);

        ToDoModel toDoModel = new ToDoModel();
        toDoModel.setTask("Test task");
        toDoModel.setStatus(0);
        toDoModel.setId(1);

        taskList.add(toDoModel);
        taskList.add(toDoModel);
        taskList.add(toDoModel);
        taskList.add(toDoModel);
        taskList.add(toDoModel);

        toDoAdapter.setTasks(taskList);

    }
}