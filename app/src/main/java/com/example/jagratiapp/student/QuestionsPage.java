package com.example.jagratiapp.student;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private Button previous;
    private Button next;
    private Button submit;
    private int questionNo = 0;
    private int totalquestion;
    private Map<String,String> answerList;
    private Iterator questionIterator;


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

//        Toast.makeText(QuestionsPage.this,quizId,Toast.LENGTH_SHORT).show();
//        //questionList = new ArrayList<>();
        question = findViewById(R.id.ques);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
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
                                Toast.makeText(QuestionsPage.this, " " + question.getQuestion(), LENGTH_SHORT).show();
                                setQuestion(question);
                                //documentReference.collection("Attendance").document(formattedDate).update(obj.getKey().toString(),obj.getValue());
                            } else {
                                question.setVisibility(View.INVISIBLE);
                                optionA.setVisibility(View.INVISIBLE);
                                optionB.setVisibility(View.INVISIBLE);
                                optionC.setVisibility(View.INVISIBLE);
                                optionD.setVisibility(View.INVISIBLE);
                                Toast.makeText(QuestionsPage.this, "Kuch ni hain", Toast.LENGTH_SHORT).show();
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
                }
                else {
                    Toast.makeText(QuestionsPage.this,"This is last question",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.submit:
                checkAnswer();
            default:
                break;
        }
    }

    private void setQuestion(Question question2){
        question.setText(question2.getQuestion());
        optionA.setText(question2.getOption1());
        optionB.setText(question2.getOption2());
        optionC.setText(question2.getOption3());
        optionD.setText(question2.getOption4());
    }

    public void checkButton(View v){

        int radioId = radioGroup.getCheckedRadioButtonId();
        optionA = findViewById(radioId);
        Map.Entry obj = (Map.Entry)questionIterator;
        String id = (String) obj.getKey();
        answerList.put(id,optionA.getText().toString());
        Toast.makeText(QuestionsPage.this,optionA.getText() + "", Toast.LENGTH_SHORT).show();

    }
    public void checkAnswer(){
        int result = 0;
        if(!answerList.isEmpty()) {
            Iterator it = answerList.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry obj = (Map.Entry)it.next();
                String ans = (String) obj.getValue();
                String id = (String) obj.getKey();
                Question question = questionList.get(id);
                String correctAns = question.getCorrectOption();
                if (correctAns == ans){
                    result++;
                }
            }
        }

    }
}