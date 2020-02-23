package com.example.cerver.ui.tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cerver.ActivityControllers.CreateTaskActivity;
import com.example.cerver.Classes.Task;
import com.example.cerver.R;
import com.example.cerver.Services.FirebaseService;
import com.example.cerver.Services.TaskService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TaskFragment extends Fragment {

    private TaskService taskService;
    private LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TaskViewModel shareViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_task, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fab);

        linearLayout = root.findViewById(R.id.task_list);

        taskService = new TaskService();

        taskService.getTasks(new FirebaseService.getTasksCallback() {
            @Override
            public void onCallback(ArrayList tasksList) {
                Log.d("All tasks", tasksList.toString());
                getTasks(container, tasksList);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTaskActivity selectAvatar = new CreateTaskActivity();
                selectAvatar.show(getFragmentManager(), null);
            }
        });

        return root;
    }

    private void getTasks(ViewGroup container, ArrayList tasksList) {
        for(Object objectTask : tasksList) {

            View taskLayout = getLayoutInflater().inflate(R.layout.partial_task, container, false);

            TextView title = taskLayout.findViewById(R.id.title);
            TextView description = taskLayout.findViewById(R.id.description);
            TextView difficulty = taskLayout.findViewById(R.id.difficulty);

            Task task = (Task)objectTask;
            title.setText(task.getTitle());
            description.setText(task.getDescription());
            difficulty.setText(task.getDifficulty());

            linearLayout.addView(taskLayout);
        }
    }


}