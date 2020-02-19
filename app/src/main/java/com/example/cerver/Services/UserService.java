package com.example.cerver.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserService extends Service {
    public UserService() {
    }
    public String ERROR = "error";
    public String SUCCESS = "success";
    FirebaseFirestore fstore;
    DatabaseReference reff;

    public String createUser(String email, String pwd, final String userName, final String userAge, final String userWeight, final String userHeight, final String userSex) {
        FirebaseAuth mFirebaseAuth;
        mFirebaseAuth = FirebaseAuth.getInstance();
        final String[] result = {SUCCESS};

        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                try {
                    if(!task.isSuccessful()) {
                        result[0] = ERROR;
                        throw new Exception("An Error Occurred! Please Try Again!");
                    } else {
                        String id = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                        createUserDetails(id, userName, userAge, userWeight, userHeight, userSex);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return result[0];
    }

    public void createUserDetails(String userId, String userName, String userAge, String userWeight, String userHeight, String userSex) {
        reff = FirebaseDatabase.getInstance().getReference().child("user_details");

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", userName);
        userDetails.put("age", userAge);
        userDetails.put("weight", userWeight);
        userDetails.put("height", userHeight);
        userDetails.put("sex", userSex);

        reff.child(userId).setValue(userDetails);
    }

    public boolean updateUserDetails(String userId, String userName, String userAge, String userWeight, String userHeight) {
        reff = FirebaseDatabase.getInstance().getReference().child("user_details").child(userId);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", userName);
        userDetails.put("age", userAge);
        userDetails.put("weight", userWeight);
        userDetails.put("height", userHeight);

        reff.setValue(userDetails);

        return true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
