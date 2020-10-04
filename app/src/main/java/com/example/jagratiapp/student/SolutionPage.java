package com.example.jagratiapp.student;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Question;
import com.example.jagratiapp.student.ui.SolutionAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolutionPage extends AppCompatActivity {
    private String classId;
    private String groupId;
    private String quizId;
    private String studentRollNo;
    private List<Question> questionList ;
    private Map<String,String> answerMap;
    private SolutionAdapter solutionAdapter;
    private RecyclerView solutionRecyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_page);

        MaterialToolbar toolbar = findViewById(R.id.student_holder_toolbar);
        setSupportActionBar(toolbar);


        Bundle bundle = getIntent().getExtras();
        classId = getIntent().getStringExtra("classId");
        groupId = bundle.getString("groupId");
        quizId = bundle.getString("quizId");
        studentRollNo = bundle.getString("studentRollNo");

        solutionRecyclerView = findViewById(R.id.student_quiz_solution_recycler_view);
        solutionRecyclerView.setHasFixedSize(true);
        solutionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionList = new ArrayList<>();

        documentReference = db.collection("Classes").document(classId)
                              .collection("Groups").document(groupId)
                              .collection("Students").document(studentRollNo)
                              .collection("Quizzes").document(quizId);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        answerMap = (Map<String, String>) documentSnapshot.get("answerList");

//                        Iterator it = answerMap.entrySet().iterator();
//                        while (it.hasNext()){
//                            Map.Entry obj = (Map.Entry)it.next();
//                            Toast.makeText(SolutionPage.this,obj.getValue() +"",Toast.LENGTH_SHORT).show();
//                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        db.collection("Classes").document(classId)
                .collection("Quizzes").document(quizId)
                .collection("Question")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                Question question = documentSnapshot.toObject(Question.class);
                                question.setQuestionId(documentSnapshot.getId());
                                //Toast.makeText(SolutionPage.this,question.getQuestionId(),Toast.LENGTH_SHORT).show();
                                questionList.add(question);
                            }
                            //Toast.makeText(SolutionPage.this,answerMap.size() +" " + questionList.size() ,Toast.LENGTH_SHORT).show();
                        }
                        solutionAdapter = new SolutionAdapter(SolutionPage.this,questionList,answerMap);
                        solutionRecyclerView.setAdapter(solutionAdapter);
                        solutionAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}