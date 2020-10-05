package com.example.jagratiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.regex.Pattern;

public class VolunteerProfile extends AppCompatActivity {

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


        documentReference.get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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



                }
                else{
                    error.setText("Empty Fields are not allowed");
                }


            }
        });
    }

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


}
