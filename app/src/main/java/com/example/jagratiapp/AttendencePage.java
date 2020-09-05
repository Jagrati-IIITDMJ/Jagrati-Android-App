package com.example.jagratiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.ui.AttendenceRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AttendencePage extends AppCompatActivity implements View.OnClickListener{
    private String classUid;
    private String groupUid;
    private String className;
    private String groupName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private List<Students> studentsList;
    private RecyclerView attendenceRecyclerView;
    private AttendenceRecyclerAdapter attendenceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_page);

        attendenceRecyclerView = findViewById(R.id.attendence_recycler_view);
        attendenceRecyclerView.setHasFixedSize(true);
        attendenceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentsList = new ArrayList<>();
        final Map<String, Object> attendence = new HashMap<>();

        Bundle bundle = getIntent().getExtras();
        classUid = bundle.getString("classUid");
        groupUid = bundle.getString("groupUid");
        documentReference = db.collection("Classes").document(classUid)
                .collection("Groups").document(groupUid);

        names();


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String formattedDate = df.format(c);

        /*final boolean[] flag = {false};
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            if (formattedDate == documentSnapshot.getId()){
                                flag[0] = true;
                                break;
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/

     //   if (!flag[0]){
            documentReference.collection("Students").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()){
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    attendence.put(documentSnapshot.getId(),false);
                                }
                                documentReference.collection("Attendance").document(formattedDate).set(attendence);

                            }
                            else {
                                Toast.makeText(AttendencePage.this,"Access nahi hue",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




       // }
        /*else{
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
                                attendenceAdapter = new AttendenceRecyclerAdapter(AttendencePage.this,studentsList);
                                attendenceRecyclerView.setAdapter(attendenceAdapter);
                                //studentAdapter.notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(AttendencePage.this,"Kuch ni hain",Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }*/
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





    private void names() {
        db.collection("Classes").document(classUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null){
                                className = documentSnapshot.getString("className");

                            }
                        }
                    }
                });


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null){
                        groupName = documentSnapshot.getString("groupName");
                    }
                }
            }
        });
    }
}