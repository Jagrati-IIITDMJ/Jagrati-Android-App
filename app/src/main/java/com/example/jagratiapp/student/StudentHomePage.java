package com.example.jagratiapp.student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jagratiapp.R;
import com.example.jagratiapp.StartPage;


public class StudentHomePage extends AppCompatActivity {

    private Button quiz;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);


        quiz = findViewById(R.id.student_quiz);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentHomePage.this,StudentQuizListPage.class));
            }
        });

        logout = findViewById(R.id.student_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("state", false);
                editor.apply();

                startActivity(new Intent(StudentHomePage.this, StartPage.class));
                finish();

            }
        });
    }
}