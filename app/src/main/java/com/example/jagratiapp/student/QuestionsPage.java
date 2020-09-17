package com.example.jagratiapp.student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jagratiapp.R;
import com.example.jagratiapp.StudentCompleteInfo;
import com.example.jagratiapp.model.Question;
import com.example.jagratiapp.model.QuizReport;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class QuestionsPage extends AppCompatActivity implements View.OnClickListener {

    private String quizId;
    //private List<Question> questionList;
    private Map<String,Question> questionList;
    private TextView question;
    private RadioButton optionA;
    private RadioButton optionB;
    private RadioButton optionC;
    private RadioButton optionD;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button previous;
    private Button next;
    private Button submit;
    private int questionNo = 0;
    private int totalquestion;
    private Map<String,String> answerList;
    private Iterator questionIterator;
    private String q;
    private QuizReport quizReport;



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private CollectionReference collectionToSaveReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_page);

        quizId = getIntent().getStringExtra("quizId");
        collectionReference = db.collection("Classes").document(StudentAPI.Instance().getClassUid())
                                .collection("Quizzes").document(quizId)
                                .collection("Question");

        collectionToSaveReport = db.collection("Classes").document(StudentAPI.Instance().getClassUid()).collection("Groups")
                .document(StudentAPI.Instance().getGroupUid()).collection("Students").document(StudentAPI.Instance().getRollno()).collection("Quizzes");


        question = findViewById(R.id.ques);
        optionA = findViewById(R.id.optionA_radio);
        optionB = findViewById(R.id.optionB_radio);
        optionC = findViewById(R.id.optionC_radio);
        optionD = findViewById(R.id.optionD_radio);
        radioGroup = findViewById(R.id.radio_group);
        previous = findViewById(R.id.previous);
        submit = findViewById(R.id.submit);
        next = findViewById(R.id.next);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        submit.setOnClickListener(this);
        answerList = new HashMap<>();
        questionList = new HashMap<>();

        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Question question = documentSnapshot.toObject(Question.class);
                                questionList.put(documentSnapshot.getId(), question);
                                //question.setQuestionId(documentSnapshot.getId());
                                //questionList.add(question);
                            }
                            totalquestion = questionList.size();
                            //Toast.makeText(QuestionsPage.this,totalquestion + "",Toast.LENGTH_SHORT).show();

                            if (!questionList.isEmpty()) {
                                questionIterator = questionList.entrySet().iterator();
                                Map.Entry obj = (Map.Entry) questionIterator.next();
                                Question question = (Question) obj.getValue();
                                q = (String) obj.getKey();
                                setQuestion(question);
                            }
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


                break;
            case R.id.next:
                if (questionIterator.hasNext()){
                    Map.Entry obj = (Map.Entry)questionIterator.next();
                    setQuestion((Question) obj.getValue());
                    q = (String) obj.getKey();
                }
                else {
                    Toast.makeText(QuestionsPage.this,"This is last question",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.submit:
                checkAnswer();
                break;
            default:
                break;
        }
    }

    private void setQuestion(Question question2){
        radioGroup.clearCheck();
        question.setText(question2.getQuestion());
        optionA.setText(question2.getOption1());
        optionB.setText(question2.getOption2());
        optionC.setText(question2.getOption3());
        optionD.setText(question2.getOption4());
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(QuestionsPage.this,radioButton.getText().toString()+" "+answerList.size(),Toast.LENGTH_SHORT).show();
        answerList.put(q,radioButton.getText().toString());
    }
    public void checkAnswer(){
         int result= 0;
        if(!answerList.isEmpty()) {
            Iterator it = answerList.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry obj = (Map.Entry)it.next();
                String ans = (String) obj.getValue();
                String id = (String) obj.getKey();

                Question question0 = questionList.get(id);
                String correctAns = question0.getCorrectOption();
                //Log.d("hew", "checkAnswer: "+ correctAns+ " "+ans,null);
                if (correctAns.trim().equals(ans.trim())){
                    result++;
                }
            }
        }

        quizReport = new QuizReport(answerList,result);
        collectionToSaveReport.document(quizId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(QuestionsPage.this, "Quiz Already Given", Toast.LENGTH_SHORT).show();
                            } else {
                                collectionToSaveReport.document(quizId).set(quizReport)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                startActivity(new Intent(QuestionsPage.this, StudentCompleteInfo.class)
                                                        .putExtra("classID", StudentAPI.Instance().getClassUid())
                                                        .putExtra("groupID", StudentAPI.Instance().getGroupUid())
                                                        .putExtra("studentID", StudentAPI.Instance().getRollno()));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(QuestionsPage.this, e.getMessage().trim(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                });






    }
}