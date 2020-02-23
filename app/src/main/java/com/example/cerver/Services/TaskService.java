package com.example.cerver.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cerver.Classes.Task;
import com.example.cerver.Classes.UserDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    DatabaseReference reff;

    public DatabaseReference createTask(String taskTitle, String taskDescription, String taskDifficulty, String taskExperience) {
        reff = FirebaseDatabase.getInstance().getReference().child("tasks");
        Task task = new Task(taskTitle, taskDescription, taskDifficulty, taskExperience);

        String id = reff.push().getKey();
        reff.child(id).setValue(task);

        return reff;
    }

    public void getTasks(FirebaseService.getTasksCallback firebaseCallback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tasks");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Task> tasks = new ArrayList<Task>();
                String title, description, difficulty, experience;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    title = snapshot.child("title").getValue(String.class);
                    description = snapshot.child("description").getValue(String.class);
                    difficulty = snapshot.child("difficulty").getValue(String.class);
                    experience = snapshot.child("experience").getValue(String.class);

                    Task task = new Task(title, description, difficulty, experience);

                    tasks.add(task);
                }
                firebaseCallback.onCallback(tasks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
