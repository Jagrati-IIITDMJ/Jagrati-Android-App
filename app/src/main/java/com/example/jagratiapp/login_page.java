package com.example.jagratiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.example.jagratiapp.student.StudentLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//hi this an artificial comment!

public class login_page extends AppCompatActivity{

    private Button signUpButtonLogin;
    private Button loginButtonLogin;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;
    private Button loginSwitch;
    private Button forgotPassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        findViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            //  fade.excludeTarget(R.id.appBar, true);
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(fade);
            getWindow().setExitTransition(fade);
        }


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


        Toast.makeText(login_page.this,"login Page",Toast.LENGTH_SHORT).show();

       firebaseAuth = FirebaseAuth.getInstance();
       //currentUser = firebaseAuth.getCurrentUser();
//       authStateListener =  new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (currentUser != null && currentUser.isEmailVerified()){
//                    startActivity(new Intent(login_page.this,HomePage.class));
//                    hideProgressDialog();
//                    finishAffinity();
//                }
//            }
//        };

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(TextUtils.isEmpty(emailEditText.getText().toString())){
                        emailEditText.setError("Empty Field Not Allowed");
                        emailEditText.requestFocus();

                    }
                    else if(TextUtils.isEmpty(passwordEditText.getText().toString())){
                        passwordEditText.setError("Enter Password");
                        passwordEditText.requestFocus();
                    }
                    else {
                        checkLogin();
                    }
                    return true;
                }
                return false;
            }
        });


        loginButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(emailEditText.getText().toString())){
                    emailEditText.setError("Empty Field Not Allowed");
                    emailEditText.requestFocus();

                }
                else if(TextUtils.isEmpty(passwordEditText.getText().toString())){
                        passwordEditText.setError("Enter Password");
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


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailEditText.getText().toString().trim()))
                {
                firebaseAuth.sendPasswordResetEmail(emailEditText.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login_page.this,"Check Email for password reset",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_page.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });}
                else{
                    emailEditText.setError("Empty Field Not Allowed");
                    emailEditText.requestFocus();
                }

            }
        });

        loginSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_page.this, StudentLogin.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(login_page.this,
                        findViewById(R.id.login_ui),"login_switch");
                startActivity(intent,optionsCompat.toBundle());
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
                    if (!isActivityTransitionRunning())
                    {
                        finishAffinity();
                    }
                }

            }
        });



    }

    public void checkLogin(){

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
            progressDialog.setMessage("Logging In..");
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
                                
                                Toast.makeText(login_page.this, "You need to verify", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(login_page.this, "NULL", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_page.this,e.getMessage().toString().trim(),Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                });
    }





    public void findViews(){
        loginButtonLogin = findViewById(R.id.login_button_login);
        signUpButtonLogin = findViewById(R.id.signUp_button_login);
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginSwitch = findViewById(R.id.vstudent_login);
        forgotPassword = findViewById(R.id.forget_password_button_login);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}