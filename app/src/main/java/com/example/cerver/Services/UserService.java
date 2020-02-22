package com.example.cerver.Services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cerver.Classes.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    String ROLE_ADMIN = "1";
    String ROLE_USER = "2";

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
                        String userId = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                        result[0] = createUserDetails(userId, userName, userAge, userWeight, userHeight, userSex);
                        setUserRole(userId);
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

        UserDetails userDetails = new UserDetails(userName, userAge, userWeight, userHeight, userSex);

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

    private void setUserRole(String userId) {
        reff = FirebaseDatabase.getInstance().getReference().child("user_roles").child(userId);

        Map<String, Object> role = new HashMap<>();
        role.put("role", ROLE_USER);

        reff.setValue(role);
    }

    public void isAdmin(FirebaseService.isAdminCallback firebaseCallback, String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_roles").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String role = dataSnapshot.child("role").getValue(String.class);
                if(role != null && role.equals(ROLE_ADMIN))
                    firebaseCallback.onCallback(true);
                else
                    firebaseCallback.onCallback(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
