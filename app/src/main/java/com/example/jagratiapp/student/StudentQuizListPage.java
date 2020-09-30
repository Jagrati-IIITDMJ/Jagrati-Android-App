package com.example.jagratiapp.student;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Quiz;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.example.jagratiapp.student.ui.StudentQuizListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentQuizListPage extends AppCompatActivity {
    private List<Quiz> quizlist;
    private RecyclerView recyclerView;
    private StudentQuizListAdapter studentQuizListAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_list_page);
        Toast.makeText(StudentQuizListPage.this,StudentAPI.Instance().getClassUid(),Toast.LENGTH_SHORT).show();
        collectionReference = db.collection("Classes").document(StudentAPI.Instance().getClassUid()).collection("Quizzes");
        recyclerView = findViewById(R.id.student_quiz_list_recyclerview);
        quizlist = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot DocumentSnapshot : queryDocumentSnapshots) {
                        Quiz quiz = DocumentSnapshot.toObject(Quiz.class);
                        quiz.setQuizID(DocumentSnapshot.getId());
                        quizlist.add(quiz);
                    }
                    studentQuizListAdapter = new StudentQuizListAdapter(StudentQuizListPage.this, quizlist);
                    recyclerView.setAdapter(studentQuizListAdapter);
                } else {
                    Toast.makeText(StudentQuizListPage.this, "Dalo nayi quiz", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



}