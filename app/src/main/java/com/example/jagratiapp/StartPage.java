package com.example.jagratiapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.example.jagratiapp.student.StudentLogin;
import com.google.protobuf.Api;

public class StartPage extends AppCompatActivity {

    private Button studentLogin;
    private Button volunteerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        View decorView = getWindow().getDecorView();


      //  Toast.makeText(StartPage.this,"Start Page",Toast.LENGTH_SHORT).show();

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
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
                    if (!isActivityTransitionRunning())
                    {
                        finishAffinity();
                    }
                }


            }
        });

        volunteerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartPage.this,login_page.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(StartPage.this,
                        findViewById(R.id.splash_logo),"splash_logo");
                startActivity(intent, options.toBundle());
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
                    if (!isActivityTransitionRunning())
                    {
                        finishAffinity();
                    }
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
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