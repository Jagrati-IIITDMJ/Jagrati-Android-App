package com.example.jagratiapp.student;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Quiz;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.example.jagratiapp.student.ui.StudentQuizListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
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

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));

        MaterialToolbar toolbar = findViewById(R.id.student_list_toolbar);
        setSupportActionBar(toolbar);
        View decorView = getWindow().getDecorView();
        int uiOptions =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_VISIBLE;


        decorView.setSystemUiVisibility(uiOptions);
//        Toast.makeText(StudentQuizListPage.this,StudentAPI.Instance().getClassUid(),Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        if(studentQuizListAdapter != null){
            studentQuizListAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(studentQuizListAdapter);
        }

    }
}