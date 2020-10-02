package com.example.jagratiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.student.StudentHomePage;
import com.example.jagratiapp.student.StudentLogin;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class StartPage extends AppCompatActivity {
    private Button studentLogin;
    private Button volunteerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        View decorView = getWindow().getDecorView();

        int uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        studentLogin = findViewById(R.id.student_login);
        volunteerLogin = findViewById(R.id.volunteer_login);

        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartPage.this, StudentLogin.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(StartPage.this,
                        findViewById(R.id.splash_logo),"splash_logo");
                startActivity(intent,optionsCompat.toBundle());
            }
        });

        volunteerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartPage.this,login_page.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(StartPage.this,
                        findViewById(R.id.splash_logo),"splash_logo");
                startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //    @Override
//    protected void onRestart() {
//        super.onRestart();
//        firebaseAuth.addAuthStateListener(authStateListener);
//        //studentLogin
////        boolean state = prefs.getState("stateOfLogin",getApplicationContext());
////        if(state) {
////            startActivity(new Intent(StartPage.this, StudentLogin.class));
////            finish();
////        }
//        final SharedPreferences SharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
//        boolean state =SharedPreferences.getBoolean("state", false);
//        String username = SharedPreferences.getString("username","");
//
//
//
//        if(state && username != null) {
//            login(username);
//        }
//
//    }


    @Override
    protected void onResume() {
        View decorView = getWindow().getDecorView();

        int uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        super.onResume();
    }
}