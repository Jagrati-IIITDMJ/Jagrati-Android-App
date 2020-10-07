package com.example.jagratiapp.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.Classes_page;
import com.example.jagratiapp.R;
import com.example.jagratiapp.StudentHolderActivity;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.ui.StudentDiffUtil;
import com.example.jagratiapp.ui.StudentRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

public class StudentsFragment extends Fragment {

    private String classid;
    private String groupid;
    private FloatingActionButton addStudent;
    private String className;
    private String groupName;
    private Button takeAttendence;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private Button saveButton;
    private EditText studentNameEditText;
    private EditText guardianNameEditText;
    private EditText mobileNoEditText;
    private EditText villageNameEditText;
    private EditText rollnoEditText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private List<Students> studentsList;
    private RecyclerView studentRecyclerView;
    private StudentRecyclerAdapter studentAdapter;
    private String formattedDate;

    public StudentsFragment() {
        // Required empty public constructor
    }

    public static StudentsFragment newInstance(String classid,String groupid) {
        StudentsFragment fragment = new StudentsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classid", classid);
        bundle.putString("groupid",groupid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        studentRecyclerView.setHasFixedSize(true);
//        studentRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        studentsList = new ArrayList<>();
        Bundle bundle=getArguments();
        if(bundle!=null){
                classid = bundle.getString("classid");
                groupid = bundle.getString("groupid");
        }

        documentReference = db.collection("Classes").document(classid)
                .collection("Groups").document(groupid);

        names();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_students, container, false);
     
        addStudent = view.findViewById(R.id.add_student_button);
        studentRecyclerView = view.findViewById(R.id.student_recycler_view);
        studentRecyclerView.setHasFixedSize(true);
        studentRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        documentReference.collection("Students").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(getContext(), "It's noting there", Toast.LENGTH_SHORT).show();
                        }else {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Students student = documentSnapshot.toObject(Students.class);
                                student.setUid(documentSnapshot.getId());
                                if(documentSnapshot.getString("student_dp")!=null)
                                    student.setStudent_dp(documentSnapshot.getString("student_dp"));
                                studentsList.add(student);

                            }
                        }
                            studentAdapter = new StudentRecyclerAdapter(getContext(), studentsList);
                            studentRecyclerView.setAdapter(studentAdapter);



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudentPopup();
            }
        });

        return view;
    }


    private void addStudentPopup() {
        builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.add_student_popup, null);

        saveButton = view.findViewById(R.id.save_student);
        studentNameEditText = view.findViewById(R.id.student_name_popup);
        guardianNameEditText = view.findViewById(R.id.guardian_name_popup);
        mobileNoEditText = view.findViewById(R.id.mobile_no_popup);
        villageNameEditText = view.findViewById(R.id.village_name_popup);
        rollnoEditText = view.findViewById(R.id.roll_no_popup);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studentName = studentNameEditText.getText().toString().trim();
                String guardianName = guardianNameEditText.getText().toString().trim();
                String mobileNo = mobileNoEditText.getText().toString().trim();
                String villageName = villageNameEditText.getText().toString().trim();
                String rollno = rollnoEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(studentName)
                        && !TextUtils.isEmpty(guardianName)
                        && !TextUtils.isEmpty(mobileNo)
                        && !TextUtils.isEmpty(villageName)
                        && !TextUtils.isEmpty(rollno)) {
                    Students student = new Students(classid, groupid, studentName, className, groupName, guardianName, mobileNo, villageName,rollno);
                    saveStudent(student);
                }
            }
        });
    }

    private void saveStudent(final Students student) {
        db.collection("Students").document(student.getRollno())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.exists()) {
                            documentReference.collection("Students").document(student.getRollno()).set(student)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Map<String, String> outsideStudent = new HashMap<>();
                                            outsideStudent.put("classUid", classid);
                                            outsideStudent.put("groupUid", groupid);
                                            db.collection("Students").document(student.getRollno()).set(outsideStudent)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getContext(), "Student Saved", Toast.LENGTH_SHORT).show();
                                                            Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    List<Students> newStudentList = studentsList;
                                                                    student.setUid(documentReference.getId());
                                                                    newStudentList.add(student);

                                                                    studentAdapter.notifyDataSetChanged();
                                                                    dialog.dismiss();
                                                                }
                                                            }, 600);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Roll no Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void names() {
        db.collection("Classes").document(classid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null) {
                                className = documentSnapshot.getString("className");

                            }
                        }
                    }
                });


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        groupName = documentSnapshot.getString("groupName");
                    }
                }
            }
        });
    }
}