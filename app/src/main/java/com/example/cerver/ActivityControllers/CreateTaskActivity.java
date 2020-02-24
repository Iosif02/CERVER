package com.example.cerver.ActivityControllers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.cerver.R;
import com.example.cerver.Services.TaskService;
import com.example.cerver.Validations.TaskValidations;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;
import java.util.Objects;

public class CreateTaskActivity extends AppCompatDialogFragment {
    private EditText title, description, difficulty, experience;
    private Bundle create;
    private TaskService taskService;
    private TaskValidations taskValidations;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_create_task, null);

        experience = view.findViewById(R.id.experience);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        difficulty = view.findViewById(R.id.difficulty);

        taskService = new TaskService();
        taskValidations = new TaskValidations();

        builder
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Create",null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
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
                        View taskLayout = getLayoutInflater().inflate(R.layout.fragment_task, null, false);
                        
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setErrors(errors);
                }
            }
        });

        return dialog;
    }

    private void setErrors(Map errors) {
        Object titleError = errors.get("title");
        Object descriptionError = errors.get("description");
        Object difficultyError = errors.get("difficulty");
        Object experienceError = errors.get("experience");

        if(experienceError != null) {
            experience.setError(experienceError.toString());
            experience.requestFocus();
        }
        if(difficultyError != null) {
            difficulty.setError(difficultyError.toString());
            difficulty.requestFocus();
        }
        if(descriptionError != null) {
            description.setError(descriptionError.toString());
            description.requestFocus();
        }
        if(titleError != null) {
            title.setError(titleError.toString());
            title.requestFocus();
        }
    }
}
