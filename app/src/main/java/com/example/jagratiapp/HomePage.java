package com.example.jagratiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private ImageView background_img_signup;
    private Button signout_button;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        background_img_signup = findViewById(R.id.background);
        background_img_signup.setImageAlpha(50);

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
        firebaseAuth.getInstance().signOut();
    }

    private void findViews() {
        signout_button = findViewById(R.id.signoutButton);
    }
}