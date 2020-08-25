package com.example.jagratiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jagratiapp.Util.VolunteerAPI;
import com.example.jagratiapp.model.Volunteer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.List;

public class VolunteerProfile extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView branchTextView;
    private TextView batchTextView;
    private TextView contactNoTextView;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText branchEditText;
    private EditText batchEditText;
    private EditText contactNoEditText;
    private ImageView edit_name;
    private ImageView edit_basic_info;
    private ImageView edit_contact_no;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private ImageView basic_info_save_btn;
    private ImageView name_save_btn;
    private ImageView contact_no_save_btn;

    FirebaseAuth firebaseAuth ;
    FirebaseUser currentUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("User");
    DocumentReference documentReference = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);
        findViews();

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        String id = currentUser.getUid();

//        collectionReference.whereEqualTo("userId" , id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
//                    Log.d("documentID", "onSuccess: " + snapshot.getId());
//
//                }
//            }
//        });
        documentReference.get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        nameTextView.setText(documentSnapshot.getString("username"));
                        emailTextView.setText(documentSnapshot.getString("email"));

//                        Volunteer volunteer = querySnapshot.toObject(Volunteer.class);
//                        name.setText(volunteer.getName());
//                        email.setText(volunteer.getMailId());
//                        branch.setText(volunteer.getBranch());
//                        batch.setText(volunteer.getBatch());
//                        contactNo.setText(volunteer.getContactNo());
//
//                        branchTextView.setText(querySnapshot.getString("branch"));
//                        batchTextView.setText(querySnapshot.getString("batch"));
//                        contactNoTextView.setText(querySnapshot.getString("contact_no"));
//
//                        edit_name.setOnClickListener(VolunteerProfile.this);
//                        edit_basic_info.setOnClickListener(VolunteerProfile.this);
//                        edit_contact_no.setOnClickListener(VolunteerProfile.this);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        edit_basic_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VolunteerProfile.this,"yes",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findViews(){

        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        branchTextView = findViewById(R.id.branch);
        batchTextView = findViewById(R.id.batch);
        contactNoTextView = findViewById(R.id.contact_no);
        edit_name = findViewById(R.id.edit_name);
        edit_basic_info = findViewById(R.id.edit_basic_info);
        edit_contact_no = findViewById(R.id.edit_contact_no);

    }

    private void editBasicInfoFunction() {
        builder = new AlertDialog.Builder(this);
        View view =getLayoutInflater().inflate(R.layout.basic_info_popup,null);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();


        emailEditText = view.findViewById(R.id.email);
        branchEditText = view.findViewById(R.id.branch);
        batchEditText = view.findViewById(R.id.batch);
        basic_info_save_btn = view.findViewById(R.id.edit_basic_info);

        basic_info_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String branch = branchEditText.getText().toString().trim();
                String batch = batchEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(email)
                        && !TextUtils.isEmpty(branch)
                        && !TextUtils.isEmpty(batch)) {
                    String id = currentUser.getUid();

                    documentReference.update("email", email);
                } else {
                    Snackbar.make(view, "SB bharna hain chodu", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
