package com.example.jagratiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.ui.AttendenceRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class AttendencePage extends AppCompatActivity implements View.OnClickListener{
    private Button submitButton;
    private String classUid;
    private String groupUid;
    private String className;
    private String groupName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private List<Students> studentsList;
    private RecyclerView attendenceRecyclerView;
    private AttendenceRecyclerAdapter attendenceAdapter;
    private boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_page);

        submitButton = findViewById(R.id.submitAttendance);
        attendenceRecyclerView = findViewById(R.id.attendence_recycler_view);
        attendenceRecyclerView.setHasFixedSize(true);
        attendenceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentsList = new ArrayList<>();
        final Map<String, Boolean> attendence = new HashMap<>();

        Bundle bundle = getIntent().getExtras();
        classUid = bundle.getString("classUid");
        groupUid = bundle.getString("groupUid");
        documentReference = db.collection("Classes").document(classUid)
                .collection("Groups").document(groupUid);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String formattedDate = df.format(c);

        documentReference.collection("Students").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                Students student = documentSnapshot.toObject(Students.class);
                                student.setUid(documentSnapshot.getId());
                                studentsList.add(student);
                            }
                        }
                        else {
                            Toast.makeText(AttendencePage.this,"Kuch ni hain", LENGTH_SHORT).show();
                        }
                    }
                });


        documentReference.collection("Attendance").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            if (formattedDate.equals(documentSnapshot.getId())) {
                                flag = true;
                                break;
                            }
                        }
                        Toast.makeText(AttendencePage.this,""+ flag, LENGTH_SHORT).show();
                        if (!flag){

                            documentReference.collection("Students").get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (!queryDocumentSnapshots.isEmpty()){
                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                    attendence.put(documentSnapshot.getId(),true);
                                                }
                                                documentReference.collection("Attendance").document(formattedDate).set(attendence);
                                            }
                                            else {
                                                Toast.makeText(AttendencePage.this,"Access nahi hue", LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                    }
                });




            final Map<String,Boolean> recordedAttendance = new HashMap<>();
            documentReference.collection("Attendance").document(formattedDate).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            for (int i=0;i<studentsList.size();i++){
                                recordedAttendance.put(studentsList.get(i).getUid(),documentSnapshot.getBoolean(studentsList.get(i).getUid()));
                            }
                            Log.d("newlist", "onSuccess: "+recordedAttendance);
                            attendenceAdapter = new AttendenceRecyclerAdapter(AttendencePage.this,studentsList,recordedAttendance);
                            attendenceRecyclerView.setAdapter(attendenceAdapter);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

}