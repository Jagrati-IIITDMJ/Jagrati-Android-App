package com.example.jagratiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class HomePage extends AppCompatActivity {

    private ImageView background_img_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        background_img_signup = findViewById(R.id.background);
        background_img_signup.setImageAlpha(50);
    }
}