package com.example.jagratiapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private ImageView profile_button;
    private ImageButton signout_button;
    private Button classes;
    private Button quizzes;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button yes;
    private Button no;

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
                createSignoutPop();

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
                startActivity(new Intent(HomePage.this,QuizClassPage    .class));
            }
        });



    }

    private void createSignoutPop() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.singout_pop,null);
        yes = view.findViewById(R.id.confirml_signout);
        no = view.findViewById(R.id.cancel_signout);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,login_page.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    private void signOut(){
        firebaseAuth.signOut();
        startActivity(new Intent(HomePage.this,login_page.class));
        finish();
    }


    private void findViews() {
        signout_button = findViewById(R.id.signoutButton);
        classes = findViewById(R.id.classes);
        profile_button = findViewById(R.id.profile);
        quizzes = findViewById(R.id.quiz);
    }
}