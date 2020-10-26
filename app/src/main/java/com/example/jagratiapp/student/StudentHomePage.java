package com.example.jagratiapp.student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.jagratiapp.R;
import com.example.jagratiapp.StartPage;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;


public class StudentHomePage extends AppCompatActivity {

    private MaterialCardView quiz;
    private MaterialCardView info;

    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_page);

        Toolbar toolbar = findViewById(R.id.student_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));

//        drawer = findViewById(R.id.student_drawer_layout);
//        NavigationView navigationView = findViewById(R.id.student_nav_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId())
//                {
//                    case R.id.student_sign_out_nav:
//                        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putBoolean("state", false);
//                        editor.apply();
//
//                        startActivity(new Intent(StudentHomePage.this, StartPage.class));
//                        finish();
//                        break;
//
//                }
//                return false;
//            }
//        });






        quiz = findViewById(R.id.student_quiz);
        info = findViewById(R.id.shome_info);

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentHomePage.this,StudentQuizListPage.class));
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentHomePage.this,StudentProfile.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.logout_student) {
            SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("state", false);
            editor.apply();

            startActivity(new Intent(StudentHomePage.this, StartPage.class));
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }
}