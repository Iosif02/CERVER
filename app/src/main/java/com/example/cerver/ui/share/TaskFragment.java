package com.example.cerver.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cerver.R;
import com.example.cerver.Services.TaskService;
import com.example.cerver.Validations.TaskValidations;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

public class TaskFragment extends Fragment {

    private EditText title, description, difficulty, experience;
    private Button create;
    private TaskService taskService;
    private TaskValidations taskValidations;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TaskViewModel shareViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_task, container, false);

        title = root.findViewById(R.id.title);
        description = root.findViewById(R.id.description);
        difficulty = root.findViewById(R.id.difficulty);
        experience = root.findViewById(R.id.experience);
        create = root.findViewById(R.id.create);

        taskService = new TaskService();
        taskValidations = new TaskValidations();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = title.getText().toString();
                String taskDescription = description.getText().toString();
                String taskDifficulty = difficulty.getText().toString();
                String taskExperience = experience.getText().toString();

                Map errors = taskValidations.validate(taskTitle, taskDescription, taskDifficulty, taskExperience);
                if(errors.isEmpty()) {
                    DatabaseReference result = taskService.createTask(taskTitle, taskDescription, taskDifficulty, taskExperience);
                    if(result != null) {
                        Toast.makeText(getActivity(), "Task created", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setErrors(errors);
                }
            }
        });
        return root;
    }

    private void setErrors(Map errors) {
        Object titleError = errors.get("title");
        Object descriptionError = errors.get("description");
        Object difficultyError = errors.get("difficulty");
        Object experienceError = errors.get("experience");

        if(titleError != null) {
            title.setError(titleError.toString());
            title.requestFocus();
        }
        if(descriptionError != null) {
            description.setError(descriptionError.toString());
            description.requestFocus();
        }
        if(difficultyError != null) {
            difficulty.setError(difficultyError.toString());
            difficulty.requestFocus();
        }
        if(experienceError != null) {
            experience.setError(experienceError.toString());
            experience.requestFocus();
        }
    }
}