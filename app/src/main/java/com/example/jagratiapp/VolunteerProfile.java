package com.example.jagratiapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class VolunteerProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 20;
    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText batch;
    private TextInputEditText phone;
    private TextInputEditText days;
    private TextInputEditText subject;
    private ImageButton edit;
    private TextView error;
    private Button save;
    private Button cancel;
    private ImageButton student_upload_dp;
    private ImageView student_dp;
    private Uri filePath;


    FirebaseAuth firebaseAuth ;
    FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("User");
    DocumentReference documentReference = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));

        findViews();
        MaterialToolbar toolbar = findViewById(R.id.volunteer_dp_toolbar);
        setSupportActionBar(toolbar);
        student_upload_dp.setVisibility(View.GONE);
        name.setEnabled(false);
        email.setEnabled(false);
        batch.setEnabled(false);
        phone.setEnabled(false);
        days.setEnabled(false);
        subject.setEnabled(false);
        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        error.setVisibility(View.GONE);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        String id = currentUser.getUid();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name.setText(documentSnapshot.getString("username"));
                        email.setText(documentSnapshot.getString("email"));

                        if (documentSnapshot.getString("batch")!=null)
                            batch.setText(documentSnapshot.getString("batch"));

                        if(documentSnapshot.getString("phone")!=null)
                            phone.setText(documentSnapshot.getString("phone"));

                        if(documentSnapshot.getString("days")!=null)
                            days.setText(documentSnapshot.getString("days"));

                        if (documentSnapshot.getString("subject")!=null)
                            subject.setText(documentSnapshot.getString("subject"));

                        if (documentSnapshot.getString("volunteer_dp") != null) {
                           // Toast.makeText(VolunteerProfile.this,"hhhhhhhhhh",Toast.LENGTH_SHORT).show();
                            final long FIVE_MEGABYTE = 5 * 1024 * 1024;
                            Bitmap bitmap = null;
                            storageReference.child("volunteers/" + currentUser.getUid() + ".jpg")
                                    .getBytes(FIVE_MEGABYTE)
                                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                           // Toast.makeText(VolunteerProfile.this,"sb mst h",Toast.LENGTH_SHORT).show();
                                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            student_dp.setImageBitmap(bm);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(VolunteerProfile.this,"Kuch to gadabad h",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                error.setVisibility(View.VISIBLE);
                student_upload_dp.setVisibility(View.VISIBLE);
                name.setEnabled(true);
//                email.setEnabled(true);
                batch.setEnabled(true);
                phone.setEnabled(true);
                days.setEnabled(true);
                subject.setEnabled(true);
                error.setText("");
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setProfile();

                    }
                });


            }

            private void setProfile() {
                if(!TextUtils.isEmpty(name.getText().toString().trim())
                    &&!TextUtils.isEmpty(batch.getText().toString().trim())
                    &&!TextUtils.isEmpty(phone.getText().toString().trim())
                    &&!TextUtils.isEmpty(days.getText().toString().trim())
                    &&!TextUtils.isEmpty(subject.getText().toString().trim()))
                {
                    documentReference.update("username",name.getText().toString().trim());
                    documentReference.update("batch",batch.getText().toString().trim());
                    documentReference.update("phone",phone.getText().toString().trim());
                    documentReference.update("days",days.getText().toString().trim());
                    documentReference.update("subject",subject.getText().toString().trim());

                    name.setEnabled(false);
                    email.setEnabled(false);
                    batch.setEnabled(false);
                    phone.setEnabled(false);
                    days.setEnabled(false);
                    subject.setEnabled(false);
                    save.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    error.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                    student_upload_dp.setVisibility(View.GONE);


                }
                else{
                    error.setText("Empty Fields are not allowed");
                }


            }
        });

        student_upload_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage()
    {

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

            StorageReference ref = storageReference.child("volunteers/" + currentUser.getUid() +".jpg" );

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.setTitle("Profile Uploaded");
                                    progressDialog.dismiss();
                                    documentReference.update("volunteer_dp",currentUser.getUid());
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(VolunteerProfile.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
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


//    private void editBasicInfoFunction() {
//
//
//
//                if (!TextUtils.isEmpty(email)
//                        && !TextUtils.isEmpty(branch)
//                        && !TextUtils.isEmpty(batch)) {
//                    String id = currentUser.getUid();
//
//                    documentReference.update("email", email);
//                } else {
//                    Snackbar.make(view, "SB bharna hain chodu", Snackbar.LENGTH_SHORT).show();
//                }
//            }

    private void findViews(){

        name = findViewById(R.id.vname);
        batch = findViewById(R.id.vbatch);
        email = findViewById(R.id.vemail);
        phone = findViewById(R.id.vphone);
        days =findViewById(R.id.vdays);
        subject =findViewById(R.id.vsubject);
        save = findViewById(R.id.vsave);
        cancel = findViewById(R.id.vcancel);
        edit = findViewById(R.id.vedit);
        error = findViewById(R.id.verror);
        student_upload_dp = findViewById(R.id.student_info_upload_dp);
        student_dp = findViewById(R.id.student_dp);

    }



}
