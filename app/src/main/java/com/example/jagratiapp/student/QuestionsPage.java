package com.example.jagratiapp.student;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class QuestionsPage extends AppCompatActivity implements View.OnClickListener {

    private String quizId;
    private List<Question> questionList;
    private TextView question;
    private TextView optionA;
    private TextView optionB;
    private TextView optionC;
    private TextView optionD;
    private ImageView tickA;
    private ImageView tickB;
    private ImageView tickC;
    private ImageView tickD;
    private Button previous;
    private Button next;
    private int questionNo = 0;
    private int totalquestion;


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

        //Toast.makeText(QuestionsPage.this,quizId,Toast.LENGTH_SHORT).show();
        questionList = new ArrayList<>();
        question = findViewById(R.id.ques);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        tickA = findViewById(R.id.tickA);
        tickB = findViewById(R.id.tickB);
        tickC = findViewById(R.id.tickC);
        tickD = findViewById(R.id.tickD);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);

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
                         totalquestion = questionList.size();
                         if (!questionList.isEmpty()){
                             setQuestion(0);
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
                     else {
                       //  Toast.makeText(QuestionsPage.this,"Kuch ni hain  kfdd",Toast.LENGTH_SHORT).show();
                     }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.previous :
                if (questionNo > 0){
                    questionNo--;
                    setQuestion(questionNo);
                }
                else {
                    Toast.makeText(QuestionsPage.this,"This is first question",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.next:
                if (questionNo < (totalquestion-1)){
                    questionNo++;
                    setQuestion(questionNo);
                }
                else {
                    Toast.makeText(QuestionsPage.this,"This is last question",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void setQuestion(int questionNo){
        question.setText(questionList.get(questionNo).getQuestion());
        optionA.setText(questionList.get(questionNo).getOption1());
        optionB.setText(questionList.get(questionNo).getOption2());
        optionC.setText(questionList.get(questionNo).getOption3());
        optionD.setText(questionList.get(questionNo).getOption4());
    }

    public void checkButton(View v){

    }
}