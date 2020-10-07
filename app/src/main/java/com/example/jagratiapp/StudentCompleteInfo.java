package com.example.jagratiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Groups;
import com.example.jagratiapp.model.Quiz;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.student.StudentQuizListPage;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.example.jagratiapp.student.ui.StudentQuizListAdapter;
import com.example.jagratiapp.ui.StudentQuizInfoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentCompleteInfo extends AppCompatActivity {
    private static final String TAG = "StudentCompleteInfo";
    private String studID;
    private String classID;
    private String groupID;
    private DocumentReference studentReference;
    private CollectionReference attendanceReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextInputEditText villageName;
    private TextInputEditText className;
    private TextInputEditText studentName;
    private TextInputEditText guardianName;
    private TextInputEditText phone;
    private TextView rollNo;
    private TextView attendance;
    private ImageButton edit;
    private Button save;
    private int present = 0;
    private int totalDays = 0;
    private StudentQuizInfoAdapter studentQuizInfoAdapter;
    private RecyclerView recyclerView;
    private Map<String, Long> givenQuizzes;
    private List<Quiz> quizlist;
    private Students student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complete_info);
        studentName = findViewById(R.id.student_name_info);
        rollNo = findViewById(R.id.roll_no_info);
        villageName = findViewById(R.id.village_name_info);
        className = findViewById(R.id.class_name_info);
        guardianName = findViewById(R.id.guardian_name_info);
        attendance = findViewById(R.id.attendance_info);
        phone = findViewById(R.id.student_phone_info);
        edit = findViewById(R.id.student_info_edit);
        save = findViewById(R.id.student_info_save);

        studentName.setEnabled(false);
        villageName.setEnabled(false);
        className.setEnabled(false);
        guardianName.setEnabled(false);
        phone.setEnabled(false);
        save.setVisibility(View.GONE);


        MaterialToolbar toolbar = findViewById(R.id.student_info_complete_toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(getTitle());

        Bundle bundle = getIntent().getExtras();
        classID = bundle.getString("classID");
        groupID = bundle.getString("groupID");
        studID = bundle.getString("studentID");

        recyclerView = findViewById(R.id.student_info_quiz_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentReference = db.collection("Classes").document(classID)
                .collection("Groups").document(groupID).collection("Students").document(studID);

        attendanceReference = db.collection("Classes").document(classID)
                .collection("Groups").document(groupID).collection("Attendance");

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                studentName.setEnabled(true);
                villageName.setEnabled(true);
                className.setEnabled(true);
                guardianName.setEnabled(true);
                phone.setEnabled(true);
                save.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                studentReference.update("studentName",studentName.getText().toString().trim());
                studentReference.update("guardianName",guardianName.getText().toString().trim());
                studentReference.update("mobileNo",phone.getText().toString().trim());
                studentReference.update("villageName",villageName.getText().toString().trim());



                studentName.setEnabled(false);
                villageName.setEnabled(false);
                className.setEnabled(false);
                guardianName.setEnabled(false);
                phone.setEnabled(false);
                save.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        studentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                student =  documentSnapshot.toObject(Students.class);
                villageName.setText(student.getVillageName());
                className.setText(student.getClassName());
                studentName.setText(student.getStudentName());
                phone.setText(student.getMobileNo());
                rollNo.setText(student.getRollno());
                guardianName.setText(student.getGuardianName());
            }
        });


        attendanceReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot groupDocumentSnapshot : queryDocumentSnapshots){
                    totalDays++;
                    if(groupDocumentSnapshot.getBoolean(studID)){
                        present++;
                    }
                }
                attendance.setText(MessageFormat.format("Attendance: {0} Out of {1} days", present, totalDays));
            }
        });

        givenQuizzes = new HashMap<String, Long>();
        studentReference.collection("Quizzes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    givenQuizzes.put(documentSnapshot.getId(), (Long) documentSnapshot.get("marks"));
                }
            }
        });
        quizlist = new ArrayList<>();
        db.collection("Classes").document(classID).collection("Quizzes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty() && givenQuizzes != null) {
                            for (QueryDocumentSnapshot DocumentSnapshot : queryDocumentSnapshots) {
                                if(givenQuizzes.get(DocumentSnapshot.getId()) != null) {
                                    Quiz quiz = DocumentSnapshot.toObject(Quiz.class);
                                    quiz.setQuizID(DocumentSnapshot.getId());
                                    quiz.setMarks(givenQuizzes.get(DocumentSnapshot.getId()));
                                    quizlist.add(quiz);
                                }
                            }
                            studentQuizInfoAdapter = new StudentQuizInfoAdapter(StudentCompleteInfo.this, quizlist,student);
                            recyclerView.setAdapter(studentQuizInfoAdapter);

                        }

                    }
                });

    }
}