package com.example.jagratiapp;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Question;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class QuestionAddPage extends AppCompatActivity implements View.OnClickListener {

    private String classid;
    private EditText question;
    private EditText option1;
    private EditText option2;
    private EditText option3;
    private EditText option4;
    private EditText correctOption;
    private Button savequesButton;
    private String quizid;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private RecyclerView recyclerView;
    private FloatingActionButton addQuestion;
    private List<Question> questionList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add_page);

        addQuestion = findViewById(R.id.fab_ques_add);
        recyclerView = findViewById(R.id.quesAdd_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        classid = bundle.getString("ClassID");
        quizid = bundle.getString("quizid");

        collectionReference = db.collection("Classes").document(classid).collection("Quizzes").document(quizid).collection("Question");

        addQuestion.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_ques_add:
                addQuestionPopup();
                break;

    }
}

    private void addQuestionPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_add_question,null);

        question = view.findViewById(R.id.QuesIn_ques_card_popup);
        option1 = view.findViewById(R.id.Opt1In_ques_card_popup);
        option2 = view.findViewById(R.id.Opt2In_ques_card_popup);
        option3 = view.findViewById(R.id.Opt3In_ques_card_popup);
        option4 = view.findViewById(R.id.Opt4In_ques_card_popup);
        correctOption = view.findViewById(R.id.AnsIn_ques_card_popup);
        savequesButton = view.findViewById(R.id.saveques_popup);

        savequesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(question.getText().toString().trim())
                        && !TextUtils.isEmpty(option1.getText().toString().trim())
                        && !TextUtils.isEmpty(option2.getText().toString().trim())
                        && !TextUtils.isEmpty(option3.getText().toString().trim())
                        && !TextUtils.isEmpty(option4.getText().toString().trim())
                        && !TextUtils.isEmpty(correctOption.getText().toString().trim())){
                    Question ques = new Question(question.getText().toString().trim(),option1.getText().toString().trim(),
                            option2.getText().toString().trim(),option3.getText().toString().trim(),option4.getText().toString().trim(),
                            correctOption.getText().toString().trim(),null);
                    saveQuestion(ques);


                }
            }
        });



        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void saveQuestion(Question ques) {
        final Question q = ques;

        collectionReference.add(q).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Question> newQuestionList = questionList;
                        newQuestionList.add(q);

                        dialog.dismiss();

                    }
                },600);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuestionAddPage.this,e.getMessage().toString().trim(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}