package com.example.jagratiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//hi this an artificial comment!

public class login_page extends AppCompatActivity{
    private TextView forgetPasswordLogin;
    private Button signUpButtonLogin;
    private Button loginButtonLogin;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        findViews();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

       firebaseAuth = FirebaseAuth.getInstance();
       //currentUser = firebaseAuth.getCurrentUser();
       authStateListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (currentUser != null && currentUser.isEmailVerified()){
                    startActivity(new Intent(login_page.this,HomePage.class));
                    hideProgressDialog();
                    finishAffinity();
                }
            }
        };


        loginButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(emailEditText.getText().toString())){
                    emailEditText.setError("kuch likh toh");
                    emailEditText.requestFocus();

                }
                else if(TextUtils.isEmpty(passwordEditText.getText().toString())){
                        passwordEditText.setError("Password papa dalenge");
                        passwordEditText.requestFocus();
                    }
                    else {
                    checkLogin();
                }
            }
        });

        signUpButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_page.this,signup_page.class));
                finish();
            }
        });



    }

    public void checkLogin(){

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
            progressDialog.setMessage("Logging In .....");
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                         currentUser = firebaseAuth.getCurrentUser();
                        if(currentUser != null) {
                            if (currentUser.isEmailVerified()) {

                                startActivity(new Intent(login_page.this, HomePage.class));
//                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                //to finish StartPage activity
                              finishAffinity();
                        //firebaseAuth.addAuthStateListener(authStateListener);
                                Toast.makeText(login_page.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            } else {
                                
                                Toast.makeText(login_page.this, "Babes you need to verify", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(login_page.this, "NULL hai boi", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_page.this,e.getMessage().toString().trim(),Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public void findViews(){
        loginButtonLogin = findViewById(R.id.login_button_login);
        signUpButtonLogin = findViewById(R.id.signUp_button_login);
        forgetPasswordLogin = findViewById(R.id.forget_password_button_login);
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
    }

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
    ////
//    @Override
//    protected void onStart() {
//        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener);
//    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}