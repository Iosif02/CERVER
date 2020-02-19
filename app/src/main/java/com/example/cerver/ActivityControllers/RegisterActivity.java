package com.example.cerver.ActivityControllers;

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
import com.example.cerver.Validations.SignUpValidations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText emailId, password, name, age, weight, height;
    Button btnSignUp;
    Spinner sex;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    UserService userService;
    SignUpValidations signUpValidations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signUpValidations = new SignUpValidations();
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

                Map errors = signUpValidations.validate(email, pwd, userName, userAge, userWeight, userHeight, userSex[0]);
                if(errors.isEmpty()) {
                    DatabaseReference result = userService.createUser(email, pwd, userName, userAge, userWeight, userHeight, userSex[0]);
                    if(result != null) {
                        findViewById(R.id.linearLayout).setVisibility(View.INVISIBLE);
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                    }
                } else {
                    validateForm(errors);
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

    protected void validateForm(Map errors) {
        Object passwordError = errors.get("password");
        Object emailError = errors.get("email");
        Object nameError = errors.get("name");
        Object ageError = errors.get("age");
        Object heightError = errors.get("height");
        Object weightError = errors.get("weight");
        Object sexError = errors.get("sex");

        if(passwordError != null) {
            password.setError(passwordError.toString());
            password.requestFocus();
        }
        if(weightError != null) {
            weight.setError(weightError.toString());
            weight.requestFocus();
        }
        if(heightError != null) {
            height.setError(heightError.toString());
            height.requestFocus();
        }
        if(sexError != null) {
            sex.requestFocus();
            TextView errorText = (TextView)sex.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText(sexError.toString());
        }
        if(ageError != null) {
            age.setError(ageError.toString());
            age.requestFocus();
        }
        if(nameError != null) {
            name.setError(nameError.toString());
            name.requestFocus();
        }
        if(emailError != null) {
            emailId.setError(emailError.toString());
            emailId.requestFocus();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
