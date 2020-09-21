package com.example.jagratiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.student.StudentHomePage;
import com.example.jagratiapp.student.StudentLogin;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class StartPage extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Button studentLogin;
    private Button volunteerLogin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        studentLogin = findViewById(R.id.student_login);
        volunteerLogin = findViewById(R.id.volunteer_login);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        authStateListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (currentUser != null && currentUser.isEmailVerified()){
                    startActivity(new Intent(StartPage.this,HomePage.class));
                    finish();
                }

            }
        };



        studentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartPage.this, StudentLogin.class));
            }
        });

        volunteerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (StartPage.this,login_page.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

//
        final SharedPreferences SharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        boolean state =SharedPreferences.getBoolean("state", false);
        String username = SharedPreferences.getString("username","");

        if(state && username != null) {
            login(username);
        }



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
    private void login(final String username) {
        collectionReference.document(username).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()){
                    db.collection("Students").document(username).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    String classUid = documentSnapshot.getString("classUid");
                                    String groupUid = documentSnapshot.getString("groupUid");

                                    db.collection("Classes").document(classUid).collection("Groups").document(groupUid).collection("Students").document(username).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    StudentAPI studentAPI = StudentAPI.Instance();
                                                    Students student = documentSnapshot.toObject(Students.class);
                                                    studentAPI.setGuardianName(student.getGuardianName());
                                                    studentAPI.setStudentName(student.getStudentName());
                                                    studentAPI.setClassUid(student.getClassID());
                                                    studentAPI.setMobileNo(student.getMobileNo());
                                                    studentAPI.setGroupUid(student.getGroupID());
                                                    studentAPI.setRollno(student.getRollno());
                                                    studentAPI.setVillageName(student.getVillageName());

                                                    startActivity(new Intent(StartPage.this,StudentHomePage.class));
                                                    finishAffinity();


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}