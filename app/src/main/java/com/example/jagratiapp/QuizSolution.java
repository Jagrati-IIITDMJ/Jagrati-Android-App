package com.example.jagratiapp;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.jagratiapp.model.Question;
import com.example.jagratiapp.student.ui.SolutionAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class QuizSolution extends AppCompatActivity {
    private String classid;
    private String groupid;
    private String quizId;
    private String studentRollno;
    private List<Question> questionList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    DocumentReference solutionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_solution);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        classid = bundle.getString("classId");
        groupid = bundle.getString("groupId");
        quizId = bundle.getString("quizId");
        studentRollno = bundle.getString("rollNo");

        collectionReference = db.collection("Classes").document(classid).collection("Quizzes").document(quizId).collection("Question");


        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Question q = documentSnapshot.toObject(Question.class);
                        questionList.add(q);
                    }
                }
            }
        });

        solutionReference = db.collection("Classes").document(classid)
                                .collection("Groups").document(groupid)
                                .collection("Students").document(studentRollno)
                                .collection("Quizzes").document(quizId);

        solutionReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Map<String,String> answerList = (Map<String, String>) documentSnapshot.get("answerList");
                            SolutionAdapter solutionAdapter = new SolutionAdapter(QuizSolution.this,questionList,answerList);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
}