package com.example.jagratiapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jagratiapp.model.Students;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class StudentCompleteInfo extends AppCompatActivity {
    private static final String TAG = "StudentCompleteInfo";
    private String studID;
    private String classID;
    private String groupID;
    private DocumentReference studentReference;
    private CollectionReference attendanceReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView villageName;
    private TextView className;
    private TextView studentName;
    private TextView guardianName;
    private TextView rollNo;
    private TextView attendance;
    private int count = 0;


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

        Bundle bundle = getIntent().getExtras();
        classID = bundle.getString("classID");
        groupID = bundle.getString("groupID");
        studID = bundle.getString("studentID");
        studentReference = db.collection("Classes").document(classID)
                .collection("Groups").document(groupID).collection("Students").document(studID);

        attendanceReference = db.collection("Classes").document(classID)
                .collection("Groups").document(groupID).collection("Attendance");

        studentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Students student =  documentSnapshot.toObject(Students.class);
                villageName.setText(String.format("Village Name: %s", student.getVillageName()));
                className.setText(String.format("Class Name: %s", student.getClassName()));
                studentName.setText(student.getStudentName());
                rollNo.setText(String.format("Roll No: %s", student.getRollno()));
                guardianName.setText(String.format("Guardian Name: %s", student.getGuardianName()));
            }
        });

        attendanceReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String,Object> obj = (document.getData());
                                if(obj.get(rollNo) == "true")
                                    count++;
                            }
                            Log.d(TAG, "" + count);
                        }
                    }
                });

    }
}