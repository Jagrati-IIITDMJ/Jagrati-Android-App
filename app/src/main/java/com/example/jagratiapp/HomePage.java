package com.example.jagratiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private Button signout_button;

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
    }

    private void signOut(){
        firebaseAuth.signOut();
        startActivity(new Intent(HomePage.this,login_page.class));
        finish();
    }

    private void findViews() {
        signout_button = findViewById(R.id.signoutButton);
    }
}