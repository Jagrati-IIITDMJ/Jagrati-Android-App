package com.example.jagratiapp.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class StudentLogin extends AppCompatActivity {
    private EditText usernameEditText;
    private Button loginButton;
    private boolean state;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Students");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        usernameEditText = findViewById(R.id.username_student_login);
        loginButton = findViewById(R.id.login_button_student_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(username))
                    login(username);
                else {
                    Toast.makeText(StudentLogin.this,"Beta ji Roll no dalo",Toast.LENGTH_SHORT).show();
                }
            }
        });

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

                                                    SharedPreferences prefs = getSharedPreferences("login",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = prefs.edit();
                                                    editor.putBoolean("state", true);
                                                    editor.putString("username", username);
                                                    editor.apply();
                                                    startActivity(new Intent(StudentLogin.this,StudentHomePage.class));
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
                else {
                    Toast.makeText(StudentLogin.this,"Username does not exists",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}