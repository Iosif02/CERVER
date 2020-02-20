package com.example.cerver.Services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserService extends Service {
    DatabaseReference reff;

    public DatabaseReference createUser(String email, String pwd, final String userName, final String userAge, final String userWeight, final String userHeight, final String userSex) {
        FirebaseAuth mFirebaseAuth;
        mFirebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference[] result = new DatabaseReference[1];

        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if(!task.isSuccessful()) {
                        throw new Exception("An Error Occurred! Please Try Again!");
                    } else {
                        String id = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                        result[0] = createUserDetails(id, userName, userAge, userWeight, userHeight, userSex);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return result[0];
    }

    public DatabaseReference createUserDetails(String userId, String userName, String userAge, String userWeight, String userHeight, String userSex) {
        reff = FirebaseDatabase.getInstance().getReference().child("user_details");

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", userName);
        userDetails.put("age", userAge);
        userDetails.put("weight", userWeight);
        userDetails.put("height", userHeight);
        userDetails.put("sex", userSex);

        reff.child(userId).setValue(userDetails);
        return reff;
    }

    public DatabaseReference updateUserDetails(String userId, String userName, String userAge, String userWeight, String userHeight, Bitmap profileImage) {
        reff = FirebaseDatabase.getInstance().getReference().child("user_details").child(userId);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", userName);
        userDetails.put("age", userAge);
        userDetails.put("weight", userWeight);
        userDetails.put("height", userHeight);

        reff.setValue(userDetails);

        if(profileImage != null) {
            updateProfileImage(profileImage, userId);
        }

        return reff;
    }

    public void updateProfileImage(Bitmap bitmap, String userId) {
        StorageReference reference = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(reference);
            }
        });
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "Img Success " + uri);
                setUserProfileUri(uri);
            }
        });
    }

    private void setUserProfileUri(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();

        if(user != null)
            user.updateProfile(request);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
