package com.example.cerver.Services;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cerver.Classes.UserDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseService extends Application {

    @Override
    public void onCreate() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onCreate();
    }

    public interface UserDetailsCallback {
        void onCallback(UserDetails userDetails);
    }

    public interface isAdminCallback {
        void onCallback(boolean isAdmin);
    }

}
