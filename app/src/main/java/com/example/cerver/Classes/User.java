package com.example.cerver.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cerver.Services.FirebaseService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User {
    private String Id;
    private String Email;
    private UserDetails UserDetails = null;

    public User(String id, String email) {
        Id = id;
        Email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void getUserDetails(FirebaseService.UserDetailsCallback firebaseCallback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_details").child(Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = new UserDetails(
                        dataSnapshot.child("user_id").getValue(String.class),
                        dataSnapshot.child("name").getValue(String.class),
                        dataSnapshot.child("age").getValue(String.class),
                        dataSnapshot.child("weight").getValue(String.class),
                        dataSnapshot.child("height").getValue(String.class),
                        dataSnapshot.child("sex").getValue(String.class)
                );
                firebaseCallback.onCallback(userDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setUserDetails(UserDetails userDetails) {
        UserDetails = userDetails;
    }
}
