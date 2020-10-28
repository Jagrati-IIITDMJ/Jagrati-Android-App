package com.example.jagratiapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Quiz;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.ui.StudentQuizInfoAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class StudentCompleteInfo extends AppCompatActivity {
    private static final String TAG = "StudentCompleteInfo";
    private static final int PICK_IMAGE_REQUEST = 22;
    private String studID;
    private String classID;
    private Uri filePath;
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
    private StorageReference storageReference;
    private ImageButton edit;
    private ImageButton upload;
    private ImageView student_dp;
    private ImageView save;
    private ImageButton call;

    private int present = 0;
    private int totalDays = 0;
    private StudentQuizInfoAdapter studentQuizInfoAdapter;
    private RecyclerView recyclerView;
    private Map<String, Long> givenQuizzes;
    private List<Quiz> quizlist;
    private Students student;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complete_info);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));


        studentName = findViewById(R.id.student_name_info);
        student_dp = findViewById(R.id.student_dp_1);
        rollNo = findViewById(R.id.roll_no_info);
        villageName = findViewById(R.id.village_name_info);
        className = findViewById(R.id.class_name_info);
        guardianName = findViewById(R.id.guardian_name_info);
        attendance = findViewById(R.id.attendance_info);
        phone = findViewById(R.id.student_phone_info);
        edit = findViewById(R.id.student_info_edit);
        upload = findViewById(R.id.student_upload_photo_1);
        save = findViewById(R.id.student_info_save);
        call= findViewById(R.id.student_call);

        storageReference = FirebaseStorage.getInstance().getReference();

        upload.setVisibility(View.GONE);
        studentName.setEnabled(false);
        villageName.setEnabled(false);
        className.setEnabled(false);
        guardianName.setEnabled(false);
        phone.setEnabled(false);




        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check = ContextCompat.checkSelfPermission(StudentCompleteInfo.this, Manifest.permission.CALL_PHONE);
                if(check == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText().toString().trim()));
                startActivity(intent);}
                else
                {
                    ActivityCompat.requestPermissions(
                            StudentCompleteInfo.this, new String[]{Manifest.permission.CALL_PHONE},1);                    ;
                }
            }
        });

        MaterialToolbar toolbar = findViewById(R.id.student_info_complete_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitle("Student Info");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Bundle bundle = getIntent().getExtras();
        classID = bundle.getString("classID");
        groupID = bundle.getString("groupID");
        studID = bundle.getString("studentID");

        recyclerView = findViewById(R.id.student_info_quiz_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentReference = db.collection("Classes").document(classID)
                .collection("Groups").document(groupID).collection("Students").document(studID);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentName.setEnabled(true);
                villageName.setEnabled(true);
                className.setEnabled(true);
                guardianName.setEnabled(true);
                phone.setEnabled(true);
                call.setVisibility(View.GONE);
                upload.setVisibility(View.VISIBLE);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectImage();
                    }
                });

                edit.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
//                edit.setImageDrawable(getDrawable(R.drawable.ic_tick));
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
                upload.setVisibility(View.GONE);
                call.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);

            }
        });

        studentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                student =  documentSnapshot.toObject(Students.class);
                villageName.setText(student.getVillageName());
                className.setText(student.getClassName());
                studentName.setText(student.getStudentName());
                phone.setText(student.getMobileNo());
                rollNo.setText("Roll No. - " + student.getRollno());
                guardianName.setText(student.getGuardianName());
                if (documentSnapshot.getString("student_dp") != null) {
                    // Toast.makeText(StudentCompleteInfo.this,"hhhhhhhhhh",Toast.LENGTH_SHORT).show();
                    final long FIVE_MEGABYTE = 5 * 1024 * 1024;
                    Bitmap bitmap = null;
                    storageReference.child("students/" + student.getRollno() + ".jpg")
                            .getBytes(FIVE_MEGABYTE)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Toast.makeText(StudentCompleteInfo.this,"sb mst h",Toast.LENGTH_SHORT).show();
                                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    student_dp.setImageBitmap(bm);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StudentCompleteInfo.this,"Kuch to gadabad h",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                // else
                //  Toast.makeText(StudentCompleteInfo.this,"Kuch to gadabasdfsdfdsfd h",Toast.LENGTH_SHORT).show();
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
                            studentQuizInfoAdapter = new StudentQuizInfoAdapter(StudentCompleteInfo.this, quizlist,student,classID,groupID);
                            recyclerView.setAdapter(studentQuizInfoAdapter);

                        }
                        else {
                            Toast.makeText(StudentCompleteInfo.this,"No quiz given",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        attendanceReference = db.collection("Classes").document(classID)
                .collection("Groups").document(groupID).collection("Attendance");


        attendanceReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot groupDocumentSnapshot : queryDocumentSnapshots){
                    if (groupDocumentSnapshot.getBoolean(studID) != null){
                        totalDays++;
                        if(groupDocumentSnapshot.getBoolean(studID)){
                            present++;
                        }
                    }
                }
                attendance.setText(MessageFormat.format("Attendance: {0}/{1}", present, totalDays));
            }
        });


    }


    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                student_dp.setImageBitmap(bitmap);
                uploadImage();
//                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("students/" + student.getRollno() +".jpg" );

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.setTitle("Profile Uploaded");
                                    progressDialog.dismiss();
                                    studentReference.update("student_dp",student.getRollno());
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(StudentCompleteInfo.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                            });
        }
    }
}