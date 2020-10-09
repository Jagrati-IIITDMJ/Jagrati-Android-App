package com.example.jagratiapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Quiz;
import com.example.jagratiapp.ui.QuizListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuizListPage extends AppCompatActivity {
    private String classid;
    private List<Quiz> quizlist;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText quizName;
    private EditText quizTime;
    private EditText quizDescription;
    private Button addQuiz;
    private QuizListAdapter quizListAdapter;
    private String className;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference     collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list_page);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));

        classid = getIntent().getStringExtra("ClassID");
        className = getIntent().getStringExtra("className");
        MaterialToolbar toolbar = findViewById(R.id.quiz_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(className);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        collectionReference = db.collection("Classes").document(classid).collection("Quizzes");

        fab = findViewById(R.id.fab_quizList_page);

        quizlist = new ArrayList<>();
        recyclerView = findViewById(R.id.quiz_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
            }
        });

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot DocumentSnapshot : queryDocumentSnapshots) {
                        Quiz quiz = DocumentSnapshot.toObject(Quiz.class);
                        quiz.setQuizID(DocumentSnapshot.getId());
                        quizlist.add(quiz);
                    }


                } else {
                    Toast.makeText(QuizListPage.this, "Enter new Quiz", Toast.LENGTH_SHORT).show();
                }
                quizListAdapter = new QuizListAdapter(QuizListPage.this, quizlist,classid);
                recyclerView.setAdapter(quizListAdapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




    }

    private void createPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_addquiz,null);

        quizName = view.findViewById(R.id.quizName_pop);
        quizDescription = view.findViewById(R.id.quizDescription_pop);
        addQuiz = view.findViewById(R.id.savequiz_pop);
        quizTime = view.findViewById(R.id.quizTime_pop);

        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        addQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!quizName.getText().toString().isEmpty()
                && !quizDescription.getText().toString().isEmpty()
                && !quizTime.getText().toString().isEmpty()){
                    addQuizToDatabase(view);
                }else
                {
                    Snackbar.make(view,"Empty Not Allowed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addQuizToDatabase(View view) {
        final Quiz quiz = new Quiz();
        quiz.setQuizName(quizName.getText().toString());
        quiz.setQuizDescription(quizDescription.getText().toString());
        quiz.setQuesTime(Integer.parseInt(quizTime.getText().toString()));

        collectionReference.add(quiz).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        List<Quiz> newQuizList = quizlist;
                        quiz.setQuizID(documentReference.getId());
                        newQuizList.add(quiz);
                        quizListAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                },1);

            }
        });
    }

}