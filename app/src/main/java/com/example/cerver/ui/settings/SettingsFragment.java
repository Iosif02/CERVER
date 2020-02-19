package com.example.cerver.ui.settings;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cerver.Classes.User;
import com.example.cerver.Classes.UserDetails;
import com.example.cerver.R;
import com.example.cerver.Services.FirebaseService;
import com.example.cerver.Services.UserService;
import com.example.cerver.Validations.UpdateDetailsValidations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class SettingsFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private EditText name, age, height, weight;
    private UserService userService;
    private UpdateDetailsValidations updateDetailsValidations;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        FirebaseUser fUser = mFirebaseAuth.getCurrentUser();
        userService = new UserService();
        updateDetailsValidations = new UpdateDetailsValidations();

        name = root.findViewById(R.id.name);
        age = root.findViewById(R.id.age);
        height = root.findViewById(R.id.height);
        weight = root.findViewById(R.id.weight);
        Button btnUpdate = root.findViewById(R.id.update);

        if(fUser != null) {
            String email = fUser.getEmail();
            String id = fUser.getUid();

            User user = new User(id, email);
            user.getUserDetails(new FirebaseService.UserDetailsCallback() {
                @Override
                public void onCallback(UserDetails userDetails) {
                    if(userDetails != null) {
                        name.setText(userDetails.getName());
                        age.setText(userDetails.getAge());
                        height.setText(userDetails.getHeight());
                        weight.setText(userDetails.getWeight());
                    }
                }
            });
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userAge = age.getText().toString();
                String userHeight = height.getText().toString();
                String userWeight = weight.getText().toString();

                Map errors = updateDetailsValidations.validate(userName, userAge, userWeight, userHeight);
                Log.d("Errors", errors.toString());
                if(errors.isEmpty()) {
                    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                    if(mFirebaseUser != null) {
                        boolean result = userService.updateUserDetails(mFirebaseUser.getUid(), userName, userAge, userWeight, userHeight);
                        if(result) {
                            Toast.makeText(getActivity(), "User details updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    setErrors(errors);
                }

            }
        });

        return root;
    }

    private void setErrors(Map errors) {
        Object nameError = errors.get("name");
        Object ageError = errors.get("age");
        Object heightError = errors.get("height");
        Object weightError = errors.get("weight");
        if(nameError != null) {
            name.setError(nameError.toString());
            name.requestFocus();
        }
        if(ageError != null) {
            age.setError(ageError.toString());
            age.requestFocus();
        }
        if(heightError != null) {
            height.setError(heightError.toString());
            height.requestFocus();
        }
        if(weightError != null) {
            weight.setError(weightError.toString());
            weight.requestFocus();
        }
    }
}