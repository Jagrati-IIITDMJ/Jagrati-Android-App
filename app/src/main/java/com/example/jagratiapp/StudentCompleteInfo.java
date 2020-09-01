package com.example.jagratiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentCompleteInfo extends AppCompatActivity {
    private String studID;
    private String classID;
    private String groupID;
    private DocumentReference documentReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView villageName;
    private TextView className;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complete_info);
        //studID = getIntent().getStringExtra("studentID");
        villageName = findViewById(R.id.villageNameFinal);
        className = findViewById(R.id.classNameFinal);


        Bundle bundle = getIntent().getExtras();
        classID = bundle.getString("classID");
        groupID = bundle.getString("groupID");
        studID = bundle.getString("studentID");
        documentReference = db.collection("Classes").document(classID)
                .collection("Groups").document(groupID).collection("Students").document(studID);

        /*documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                villageName.setText(documentSnapshot.getVillageName());

            }
        });*/




    }
}