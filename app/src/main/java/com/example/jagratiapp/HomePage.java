package com.example.jagratiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Button classes;
    private Button quizzes;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DrawerLayout drawer;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private Button yes;
    private Button no;



    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        classes = findViewById(R.id.classes);
        quizzes = findViewById(R.id.quiz);
        MaterialToolbar toolbar = findViewById(R.id.attendance_toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(HomePage.this,"Home Page",Toast.LENGTH_SHORT).show();


        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.draweropen,R.string.drawerclosed);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.jag1));

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));


        drawer.addDrawerListener(toggle);
        toggle.syncState();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        authStateListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (currentUser == null){
                    startActivity(new Intent(HomePage.this,StartPage.class));
                    finishAffinity();
                }
            }
        };


        classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, Classes_page.class);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        HomePage.this, classes, ViewCompat.getTransitionName(classes)
//                );
                startActivity(intent);
            }
        });



        quizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this, QuizClassPage.class));
            }
        });



    }





    private void findViews() {

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.profile_nav:
                startActivity(new Intent(HomePage.this,VolunteerProfile.class));
                break;

            case R.id.sign_out_nav:
                createSignOutPop();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createSignOutPop() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.singout_pop,null);

        yes = view.findViewById(R.id.confirml_signout);
        no= view.findViewById(R.id.cancel_signout);

        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();
                startActivity(new Intent(HomePage.this,StartPage.class));
                finish();
                dialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            {

                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
    }

    @Override
    protected void onStop() {


        super.onStop();
        

    }
}