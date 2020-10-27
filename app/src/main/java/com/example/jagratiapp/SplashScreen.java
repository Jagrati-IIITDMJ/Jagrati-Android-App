package com.example.jagratiapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.student.StudentHomePage;
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

public class SplashScreen extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Students");
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    boolean flag1 = false, flag2 = false;
    private ConstraintLayout root;
    private ImageView splash_logo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        View decorView = getWindow().getDecorView();

        int uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        root = findViewById(R.id.splash);
        splash_logo = findViewById(R.id.splash_logo);

                if (currentUser != null && currentUser.isEmailVerified()){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                flag1 = true;
                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreen.this, splash_logo, "splash_logo");
                                startActivity(new Intent(SplashScreen.this,HomePage.class),optionsCompat.toBundle());
                                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
                                    if (!isActivityTransitionRunning())
                                    {
                                        finishAffinity();
                                    }
                                }


                            }
                        },1000);
                    }
                else {
                    flag1 = false;
                }

        final SharedPreferences SharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        boolean state =SharedPreferences.getBoolean("state", false);
        String username = SharedPreferences.getString("username","");

        if(state && username != null) {
            flag2 = true;
            login(username);
        }
        else{
            flag2 = false;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!flag1 && !flag2){
                    final ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreen.this,root,"splash");
                    startActivity(new Intent(SplashScreen.this,StartPage.class),optionsCompat.toBundle());
                    finishAffinity();

                }
            }
        },1000);


       // Toast.makeText(SplashScreen.this,"" +flag1 + flag2,Toast.LENGTH_SHORT).show();

    }


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

                                                    startActivity(new Intent(SplashScreen.this, StudentHomePage.class));
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
        View decorView = getWindow().getDecorView();

        int uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        super.onResume();
    }
    //    private final class LongOperation extends AsyncTask<Void, Void, Integer> {
//
//@Override
//protected
//
//        @Override
//        protected Integer doInBackground(Void... voids) {
//            return 0;
//        }
//    }
}