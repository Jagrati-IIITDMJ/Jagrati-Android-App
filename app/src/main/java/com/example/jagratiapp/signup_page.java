package com.example.jagratiapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class signup_page extends AppCompatActivity {
    //ToDO: Commented wale abhi use nahi kar rahe
    private ImageView img;
    private EditText email;
    private EditText password;
    private EditText cpassword;
    private ProgressBar progressBar;
    private Button signup;
    private EditText username;
//    private EditText Eotp;
//    private EditText phoneno;
//    private Button getotp;

//    private TextView errortext;
//    private TextView countertext;



    //Firebase authorisation
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private  FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        img = findViewById(R.id.signup_background);
        img.setImageAlpha(50);

        firebaseAuth = FirebaseAuth.getInstance();
//        countertext = findViewById(R.id.countertext);
//        getotp = findViewById(R.id.sign_get_otp);

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
//        Eotp = findViewById(R.id.signup_otp);
//        Eotp.setVisibility(View.INVISIBLE);

        //starting from here



        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.signup_pass);
        cpassword = findViewById(R.id.signup_pass_again);
        username = findViewById(R.id.sign_up_username);
        signup = findViewById(R.id.sign_up_button);
        progressBar = findViewById(R.id.signup_progress);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if(currentUser != null){
                    //user is alread log in
                }
                else
                {
                    // no user yet
                }
            }
        };






        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(email.getText().toString())
                        && !TextUtils.isEmpty(password.getText().toString())
                        && !TextUtils.isEmpty(username.getText().toString())){

                    String email_f = email.getText().toString().trim();
                    String password_f = password.getText().toString().trim();
                    String username_f = username.getText().toString().trim();
                    createAccount(email_f,password_f,username_f);


                }else{
                    Toast.makeText(signup_page.this,"Empty not allowed", Toast.LENGTH_LONG).show();
                }

            }
        });











//        getotp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new CountDownTimer(30000, 1000) {
//
//                    public void onTick(long millisUntilFinished) {
//                        countertext.setText("Create OTP again in " + millisUntilFinished / 1000 + " seconds");
//                        getotp.setEnabled(false);
//                        Eotp.setVisibility(View.VISIBLE);
//                    }
//
//
//                    public void onFinish() {
//                        countertext.setText("Get OTP again");
//                        getotp.setEnabled(true);
//                    }
////                }.start();
//
//            }
//        });



    }

    private void createAccount(String email, String password, final String username) {
        if(!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(username)){


            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        final String currentUserId = currentUser.getUid();

                        //create a user map so we can create a user in the user collection
                        Map<String ,String> userObj = new HashMap<>();
                        userObj.put("UserId", currentUserId);
                        userObj.put("Username",username);

                        // save to firestore
                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(Objects.requireNonNull(task.getResult()).exists()){
                                            progressBar.setVisibility(View.INVISIBLE);

                                            Intent intent = new Intent(signup_page.this,login_page.class);
                                            startActivity(intent);

                                        }else
                                        {

                                        }

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(signup_page.this,"Failed", Toast.LENGTH_LONG).show();
                            }
                        });



                    }else{
                        //something is wrong
                        progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(signup_page.this,"Not added", Toast.LENGTH_LONG).show();

                    }

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }else {
            Toast.makeText(signup_page.this,"Not sufficient info", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
            currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}