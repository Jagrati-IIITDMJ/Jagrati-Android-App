package com.example.jagratiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
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
        findViews();
        MaterialToolbar toolbar = findViewById(R.id.attendance_toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(HomePage.this,"Home Page",Toast.LENGTH_SHORT).show();

//        Fade fade = new Fade();
//        View decor = getWindow().getDecorView();
//        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
//        fade.excludeTarget(decor.findViewById(R.id.home_page_logo),true);
//        fade.excludeTarget(android.R.id.statusBarBackground, true);
//        fade.excludeTarget(android.R.id.navigationBarBackground, true);
//
//        getWindow().setEnterTransition(fade);
//        getWindow().setExitTransition(fade);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        ChangeClipBounds changeClipBounds = new ChangeClipBounds();
//        changeClipBounds.addTarget(R.id.classes);
//        getWindow().setSharedElementEnterTransition(changeClipBounds);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.draweropen,R.string.drawerclosed);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.jag1));


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
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        HomePage.this, classes, ViewCompat.getTransitionName(classes)
                );
                startActivity(intent, options.toBundle());
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

        classes = findViewById(R.id.classes);
        quizzes = findViewById(R.id.quiz);


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