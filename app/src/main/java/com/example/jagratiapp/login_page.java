package com.example.jagratiapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

//hi this an artificial comment!

public class login_page extends AppCompatActivity {
    private ImageView img;
    private TextView forget_pass;
    private Button loginBut;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        img = findViewById(R.id.login_background);
        img.setImageAlpha(50);

        loginBut = findViewById(R.id.login_but);

        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_page.this,signup_page.class));
            }
        });

        forget_pass = findViewById(R.id.forget_pass);
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(login_page.this, "Working",Toast.LENGTH_SHORT).show();
            }
        });
    }
}