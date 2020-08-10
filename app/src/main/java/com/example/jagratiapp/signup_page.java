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
import com.google.firebase.firestore.ThrowOnExtraProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class signup_page extends AppCompatActivity {
    //ToDO: Commented wale abhi use nahi kar rahe
    private EditText email_edit_signup;
    private EditText password_edit_signup;
    private EditText cpassword_edit_signup;
    private EditText name_edit_signup;
    private ProgressBar progressBar_signup;
    private Button signup_button_signup;
    private EditText username_button_signup;



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

        firebaseAuth = FirebaseAuth.getInstance();

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);

        //starting from here

        email_edit_signup = findViewById(R.id.signup_email);
        password_edit_signup = findViewById(R.id.signup_pass);
        cpassword_edit_signup = findViewById(R.id.signup_pass_again);
        name_edit_signup = findViewById(R.id.signup_name);
        signup_button_signup = findViewById(R.id.signup_button);
        progressBar_signup = findViewById(R.id.signup_progress);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if(currentUser != null){
                    //user is alread log in
                    startActivity(new Intent(signup_page.this,HomePage.class));
                }

            }
        };

        signup_button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(email_edit_signup.getText().toString())
                        && !TextUtils.isEmpty(password_edit_signup.getText().toString())
                        && !TextUtils.isEmpty(name_edit_signup.getText().toString())
                        && !TextUtils.isEmpty(cpassword_edit_signup.getText().toString())){
                    String email = email_edit_signup.getText().toString().trim();
                    String password = password_edit_signup.getText().toString().trim();
                    String name = name_edit_signup.getText().toString().trim();
                    String cpassword = cpassword_edit_signup.getText().toString().trim();

                    createAccount(email,password,name);


                }else{
                    Toast.makeText(signup_page.this,"Empty fields are not allowed", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void createAccount(final String email, String password, final String name) {
        progressBar_signup.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        currentUser = firebaseAuth.getCurrentUser();
                        String currentUser_id = currentUser.getUid();
                        addUserInDatabase(currentUser_id,email,name);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar_signup.setVisibility(View.INVISIBLE);
                        Toast.makeText(signup_page.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addUserInDatabase(String userId,String email,String name) {
        Map<String,String> userObj = new HashMap<>();

        userObj.put("userId",userId);
        userObj.put("email",email);
        userObj.put("username",name);

        collectionReference.add(userObj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        progressBar_signup.setVisibility(View.INVISIBLE);


                        //
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        assert user != null;
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(signup_page.this,"Verification send to email",Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(signup_page.this,login_page.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(signup_page.this,"Nahi bheja verification",Toast.LENGTH_SHORT).show();
                            }
                        });



                        //

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(signup_page.this,"FUCK OFF, NOT SAVED TO DATABASE ",Toast.LENGTH_SHORT).show();
                    }
                });
    }





    @Override
    protected void onStart() {
        super.onStart();
            currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}