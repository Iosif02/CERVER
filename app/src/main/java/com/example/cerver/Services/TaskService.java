package com.example.cerver.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.cerver.Classes.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
}
