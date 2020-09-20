package com.example.jagratiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private ImageView profile_button;
    private Button signout_button;
    private Button classes;
    private Button quizzes;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        findViews();

        firebaseAuth = FirebaseAuth.getInstance();
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();

            }
        });

        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, Classes_page.class));
            }
        });

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,VolunteerProfile.class));
            }
        });

        quizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, QuizClassPage.class));
            }
        });



    }

    private void signOut(){
            firebaseAuth.signOut();
            startActivity(new Intent(HomePage.this,StartPage.class));
            finish();

    }


    private void findViews() {
        signout_button = findViewById(R.id.signoutButton);
        classes = findViewById(R.id.classes);
        profile_button = findViewById(R.id.profile);
        quizzes = findViewById(R.id.quiz);
    }

}