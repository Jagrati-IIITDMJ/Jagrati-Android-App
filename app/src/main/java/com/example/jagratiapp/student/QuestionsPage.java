package com.example.jagratiapp.student;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Question;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionsPage extends AppCompatActivity {

    private String quizId;
    private List<Question> questionList;
    private TextView question;
    private TextView optionA;
    private TextView optionB;
    private TextView optionC;
    private TextView optionD;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_page);

        quizId = getIntent().getStringExtra("quizId");
        collectionReference = db.collection("Classes").document(StudentAPI.Instance().getClassUid())
                                .collection("Quizzes").document(quizId)
                                .collection("Question");

        Toast.makeText(QuestionsPage.this,quizId,Toast.LENGTH_SHORT).show();
        questionList = new ArrayList<>();
        question = findViewById(R.id.ques);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);

        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                     if (!queryDocumentSnapshots.isEmpty()){
                         for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                             Question question = documentSnapshot.toObject(Question.class);
                             question.setQuestionId(documentSnapshot.getId());
                             questionList.add(question);
                         }
                     }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        if (!questionList.isEmpty()){
            question.setText(questionList.get(0).getQuestion());
            optionA.setText(questionList.get(0).getOption1());
            optionB.setText(questionList.get(0).getOption2());
            optionB.setText(questionList.get(0).getOption3());
            optionB.setText(questionList.get(0).getOption4());
        }
        else {
            question.setVisibility(View.INVISIBLE);
            optionA.setVisibility(View.INVISIBLE);
            optionB.setVisibility(View.INVISIBLE);
            optionC.setVisibility(View.INVISIBLE);
            optionD.setVisibility(View.INVISIBLE);
            Toast.makeText(QuestionsPage.this,"Kuch ni hain",Toast.LENGTH_SHORT).show();
        }

    }
}