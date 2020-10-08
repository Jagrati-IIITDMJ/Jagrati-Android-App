package com.example.jagratiapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Question;
import com.example.jagratiapp.ui.QuestionAddAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionAddPage extends AppCompatActivity implements View.OnClickListener {

    private String classid;
    private EditText question;
    private EditText option1;
    private EditText option2;
    private EditText option3;
    private EditText option4;
    private RadioButton option1RadioButton;
    private RadioButton option2RadioButton;
    private RadioButton option3RadioButton;
    private RadioButton option4RadioButton;
    private Button savequesButton;
    private String quizid;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private RecyclerView recyclerView;
    private ImageButton addQuestion;
    private List<Question> questionList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private DocumentReference documentToAddNumOfQues;
    private QuestionAddAdapter questionAddAdapter;
    private String correctOption;
    private String quizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add_page);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        classid = bundle.getString("ClassID");
        quizid = bundle.getString("quizid");
        quizName = bundle.getString("quizName");

        MaterialToolbar toolbar = findViewById(R.id.quiz_add_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(quizName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addQuestion = findViewById(R.id.fab_ques_add);
        recyclerView = findViewById(R.id.quesAdd_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionList = new ArrayList<>();



        collectionReference = db.collection("Classes").document(classid).collection("Quizzes").document(quizid).collection("Question");
        documentToAddNumOfQues = db.collection("Classes").document(classid).collection("Quizzes").document(quizid);

        addQuestion.setOnClickListener(this);

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Question q = documentSnapshot.toObject(Question.class);
                        q.setQuestionId(documentSnapshot.getId());
                        questionList.add(q);
                    }
                }
                questionAddAdapter = new QuestionAddAdapter(QuestionAddPage.this, questionList,classid,quizid);
                recyclerView.setAdapter(questionAddAdapter);

            }
        });


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
        option1RadioButton = view.findViewById(R.id.optionA_question_card_popup);
        option2RadioButton = view.findViewById(R.id.optionB_question_card_popup);
        option3RadioButton = view.findViewById(R.id.optionC_question_card_popup);
        option4RadioButton = view.findViewById(R.id.optionD_question_card_popup);
        savequesButton = view.findViewById(R.id.saveques_popup);

        savequesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(question.getText().toString().trim())
                        && !TextUtils.isEmpty(option1.getText().toString().trim())
                        && !TextUtils.isEmpty(option2.getText().toString().trim())
                        && !TextUtils.isEmpty(option3.getText().toString().trim())
                        && !TextUtils.isEmpty(option4.getText().toString().trim())
                        && (option1RadioButton.isChecked()
                        || option2RadioButton.isChecked()
                        || option3RadioButton.isChecked()
                        || option4RadioButton.isChecked())){
                    dialog.dismiss();
                    if (option1RadioButton.isChecked())
                        correctOption = option1.getText().toString();
                    else if (option2RadioButton.isChecked())
                        correctOption = option2.getText().toString();
                    else if (option3RadioButton.isChecked())
                        correctOption = option3.getText().toString();
                    else if (option4RadioButton.isChecked())
                        correctOption = option4.getText().toString();

                    Question ques = new Question(question.getText().toString().trim(),option1.getText().toString().trim(),
                            option2.getText().toString().trim(),option3.getText().toString().trim(),option4.getText().toString().trim(),
                            correctOption,null);


                    saveQuestion(ques);
                }
                else {
                    Toast.makeText(QuestionAddPage.this,"sab dalo",Toast.LENGTH_SHORT).show();
                }
            }
        });



        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void saveQuestion(Question ques) {
        final Question q = ques;

        collectionReference.add(q).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                        List<Question> newQuestionList = questionList;
                        q.setQuestionId(documentReference.getId());
                        newQuestionList.add(q);
                        questionAddAdapter.notifyDataSetChanged();
                        documentToAddNumOfQues.update("numberOfQues",newQuestionList.size());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuestionAddPage.this,e.getMessage().toString().trim(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}