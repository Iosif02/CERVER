package com.example.cerver.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cerver.R;
import com.example.cerver.Services.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText emailId, password, name, age, weight, height;
    Button btnSignUp;
    Spinner sex;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String userSex = null;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btnSignUp = findViewById(R.id.sign_up);
        tvSignIn = findViewById(R.id.login);
        sex = findViewById(R.id.sex);
        emailId = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        final String[] userSex = {""};
        userService = new UserService();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailId.getText().toString();
                final String pwd = password.getText().toString();
                final String userName = name.getText().toString();
                final String userAge = age.getText().toString();
                final String userWeight = weight.getText().toString();
                final String userHeight = height.getText().toString();

                if(validateForm(email, pwd, userName, userAge, userWeight, userHeight, userSex[0])) {
                    String result = userService.createUser(email, pwd, userName, userAge, userWeight, userHeight, userSex[0]);
                    if(result.equals(userService.SUCCESS)) {
                        Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(i);
                    }
                }

            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ) {
                    Toast.makeText(RegisterActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(RegisterActivity.this, "Please sign up!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userSex[0] = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected boolean validateForm(String email, String pwd, String userName, String userAge, String userWeight, String userHeight, String userSex) {
        boolean error = false;
        if(pwd.isEmpty()) {
            password.setError("Please enter your password");
            password.requestFocus();
            error = true;
        }
        if(userWeight.isEmpty()) {
            weight.setError("Please enter your weight");
            weight.requestFocus();
            error = true;
        }
        if(userHeight.isEmpty()) {
            height.setError("Please enter your height");
            height.requestFocus();
            error = true;
        }
        if(userSex.isEmpty() || userSex.equals("Sex")) {
            sex.requestFocus();
            TextView errorText = (TextView)sex.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Please enter your sex");
            error = true;
        }
        if(userAge.isEmpty()) {
            age.setError("Please enter your age");
            age.requestFocus();
            error = true;
        }
        if(userName.isEmpty()) {
            name.setError("Please enter your name");
            name.requestFocus();
            error = true;
        }
        if(email.isEmpty()) {
            emailId.setError("Please enter email");
            emailId.requestFocus();
            error = true;
        }

        return !error;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
