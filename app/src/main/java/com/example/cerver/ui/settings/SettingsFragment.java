package com.example.cerver.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cerver.Classes.User;
import com.example.cerver.Classes.UserDetails;
import com.example.cerver.R;
import com.example.cerver.Services.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    EditText name, age, height, weight;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        FirebaseUser fUser = mFirebaseAuth.getCurrentUser();

        name = root.findViewById(R.id.name);
        age = root.findViewById(R.id.age);
        height = root.findViewById(R.id.height);
        weight = root.findViewById(R.id.weight);

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

        return root;
    }
}