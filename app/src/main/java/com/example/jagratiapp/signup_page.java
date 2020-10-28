package com.example.jagratiapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup_page extends AppCompatActivity {

    private EditText email_edit_signup;
    private EditText password_edit_signup;
    private EditText cpassword_edit_signup;
    private EditText name_edit_signup;
    private ProgressBar progressBar_signup;
    private Button signup_button_signup;

    //Firebase authorisation
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private  FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("User");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));

        firebaseAuth = FirebaseAuth.getInstance();

        email_edit_signup = findViewById(R.id.signup_email);
        password_edit_signup = findViewById(R.id.signup_pass);
        cpassword_edit_signup = findViewById(R.id.signup_pass_again);
        name_edit_signup = findViewById(R.id.signup_name);
        signup_button_signup = findViewById(R.id.signup_button);
        progressBar_signup = findViewById(R.id.signup_progress);

        final String email = email_edit_signup.getText().toString().trim();
        final String password = password_edit_signup.getText().toString().trim();
        final String name = name_edit_signup.getText().toString().trim();
        final String cpassword = cpassword_edit_signup.getText().toString().trim();

        signup_button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !cpassword.isEmpty()){
                    if (password != cpassword){
                        cpassword_edit_signup.setError("password doesn't match");
                    }
                    else
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
                        String currentUser_id = currentUser.getUid().toString();
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
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        collectionReference.document(user.getUid().toString()).set(userObj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar_signup.setVisibility(View.INVISIBLE);

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
                                Toast.makeText(signup_page.this,"Retry after some time",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup_page.this,"NOT SAVED TO DATABASE",Toast.LENGTH_SHORT).show();
            }
        });
    }

}