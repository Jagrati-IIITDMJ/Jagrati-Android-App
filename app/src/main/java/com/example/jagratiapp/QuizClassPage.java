package com.example.jagratiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Classes;
import com.example.jagratiapp.ui.QuizClassAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuizClassPage extends AppCompatActivity {
    private RecyclerView quizClassRecyclerView;
    private List<Classes> classesList;
    private QuizClassAdapter quizClassAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Classes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_class_page);

        MaterialToolbar toolbar = findViewById(R.id.quiz_class_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        quizClassRecyclerView = findViewById(R.id.quiz_class_recyclerview);
        quizClassRecyclerView.setHasFixedSize(true);
        quizClassRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classesList = new ArrayList<>();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot classDocumentSnapshot : queryDocumentSnapshots) {
                        Classes classes = classDocumentSnapshot.toObject(Classes.class);
                        classes.setuId(classDocumentSnapshot.getId());
                        classesList.add(classes);
                    }
                    quizClassAdapter = new QuizClassAdapter(QuizClassPage.this, classesList);
                    quizClassRecyclerView.setAdapter(quizClassAdapter);
                } else {
                    Toast.makeText(QuizClassPage.this, "It's noting there", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}