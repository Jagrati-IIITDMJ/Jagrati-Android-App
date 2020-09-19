package com.example.jagratiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jagratiapp.student.StudentLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartPage extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button studentLogin;
    private Button volunteerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        studentLogin = findViewById(R.id.student_login);
        volunteerLogin = findViewById(R.id.volunteer_login);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        authStateListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (currentUser != null && currentUser.isEmailVerified()){
                    startActivity(new Intent(StartPage.this,HomePage.class));
                    finish();
                }
            }
        };

        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartPage.this, StudentLogin.class));
            }
        });

        volunteerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (StartPage.this,login_page.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}