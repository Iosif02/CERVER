package com.example.cerver.ActivityControllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cerver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    LinearLayout linearLayout;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = findViewById(R.id.login);
        tvSignUp = findViewById(R.id.sign_up);
        emailId = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if( validateForm() == 0 ) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login unsuccessful. Please try again!", Toast.LENGTH_SHORT).show();
                            } else {
                                findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);
                                findViewById(R.id.loading).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ) {
                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Please log in!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    protected int validateForm() {
        int error = 0;
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        if( pwd.isEmpty() ) {
            password.setError("Please enter password");
            password.requestFocus();
            error++;
        }
        if( email.isEmpty() ) {
            emailId.setError("Please enter email");
            emailId.requestFocus();
            error++;
        }
        return error;
    }
}
