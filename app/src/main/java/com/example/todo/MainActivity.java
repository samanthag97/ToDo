package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.todo.adapter.ToDoAdapter;
import com.example.todo.model.ToDoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.example.todo.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ActivityDrawerBase implements DialogCloseListener{

    ActivityMainBinding activityMainBinding;
    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;
    private List<ToDoModel> taskList;
    private FloatingActionButton floatingActionButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private Query query;
    private ListenerRegistration listenerRegistration;

    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle(getString(R.string.homepage));

        recyclerView = findViewById(R.id.taskRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton = findViewById(R.id.floatingActionB);
        firebaseAuth = FirebaseAuth.getInstance();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        firestore = FirebaseFirestore.getInstance();

        taskList = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(MainActivity.this, taskList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(toDoAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        showData();
        recyclerView.setAdapter(toDoAdapter);

    }

    private void showData() {
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        else {
            String collectionPath = firebaseAuth.getCurrentUser().getUid();
            query = firestore.collection(collectionPath).orderBy("time", Query.Direction.ASCENDING);

            listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.d(TAG, "Error:" + error.getMessage());
                    } else {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                String id = documentChange.getDocument().getId();
                                ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                                taskList.add(toDoModel);
                                toDoAdapter.notifyDataSetChanged();
                            }
                        }
                        listenerRegistration.remove();
                    }
                }
            });

        }

    }


    @Override
    public void dialogClose(DialogInterface dialogInterface) {
        taskList.clear();
        showData();
        toDoAdapter.notifyDataSetChanged();
    }
}