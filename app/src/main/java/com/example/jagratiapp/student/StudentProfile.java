package com.example.jagratiapp.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StudentProfile extends AppCompatActivity {
    private TextInputEditText name;
    private TextInputEditText rollNo;
    private TextInputEditText className;
    private TextInputEditText parentName;
    private TextInputEditText phone;
    private TextInputEditText Attendance;
    private TextInputEditText village;
    private ImageView dp;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        name = findViewById(R.id.sname);
        rollNo = findViewById(R.id.sroll);
        className = findViewById(R.id.sclass);
        parentName = findViewById(R.id.sparent);
        phone = findViewById(R.id.sphone);
        Attendance = findViewById(R.id.sdays);
        village = findViewById(R.id.svillage);
        dp = findViewById(R.id.sstudent_dp);

        StudentAPI studentAPI = StudentAPI.Instance();

        name.setText(studentAPI.getStudentName());
        rollNo.setText(studentAPI.getRollno());
//        className.setText();
        parentName.setText(studentAPI.getGuardianName());
        phone.setText(studentAPI.getMobileNo());
        village.setText(studentAPI.getVillageName());


    }

}