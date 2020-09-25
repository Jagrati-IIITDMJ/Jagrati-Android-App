package com.example.jagratiapp.student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.jagratiapp.R;
import com.example.jagratiapp.StartPage;
import com.google.android.material.navigation.NavigationView;


public class StudentHomePage extends AppCompatActivity {

    private Button quiz;

    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);

        Toolbar toolbar = findViewById(R.id.student_toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.student_drawer_layout);
        NavigationView navigationView = findViewById(R.id.student_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.student_sign_out_nav:
                        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("state", false);
                        editor.apply();

                        startActivity(new Intent(StudentHomePage.this, StartPage.class));
                        finish();
                        break;

                }
                return false;
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.draweropen,R.string.drawerclosed);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.jag1));

        drawer.addDrawerListener(toggle);
        toggle.syncState();


        quiz = findViewById(R.id.student_quiz);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentHomePage.this,StudentQuizListPage.class));
            }
        });


    }
}