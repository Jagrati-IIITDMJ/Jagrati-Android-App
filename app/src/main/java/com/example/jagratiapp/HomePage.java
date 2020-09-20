package com.example.jagratiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.jagratiapp.model.Volunteer;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Button classes;
    private Button quizzes;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button yes;
    private Button no;
    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
         classes = findViewById(R.id.classes);
         quizzes = findViewById(R.id.quiz);
         firebaseAuth = FirebaseAuth.getInstance();
         MaterialToolbar toolbar = findViewById(R.id.home_toolbar);
         setSupportActionBar(toolbar);
         drawer= findViewById(R.id.drawer_layout);

         ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.draweropen,
                 R.string.drawerclosed);
         drawer.addDrawerListener(toggle);

         toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, Classes_page.class));
            }
        });


        quizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,QuizClassPage    .class));
            }
        });
    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.profile_nav:
//                startActivity(new Intent(HomePage.this, VolunteerProfile.class));
//
//                break;
//
//            case R.id.sign_out_nav:
//                createSignoutPop();
//                break;
//        }
//
//        return true;
//    }


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
                firebaseAuth.signOut();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.profile_nav)
        {
            startActivity(new Intent(this, VolunteerProfile.class));
        }
        else if(id==R.id.sign_out_nav)
        {
            createSignoutPop();
        }
        else
            Toast.makeText(this,"working",Toast.LENGTH_SHORT).show();

        return false;
    }
}