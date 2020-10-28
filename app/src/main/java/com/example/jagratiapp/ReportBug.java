package com.example.jagratiapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jagratiapp.model.BugReport;
import com.example.jagratiapp.model.Classes;
import com.example.jagratiapp.ui.ReportBugAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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
import java.util.List;
import java.util.Locale;

public class ReportBug extends AppCompatActivity {


    private FirebaseUser user;
    private String username;
    private String formattedDate;
    private EditText bugDescription;
    private Button reportButton;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference2;





    private ReportBugAdapter reportBugAdapter;
    private RecyclerView recyclerView;
    private List<BugReport> bugList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bug);
        user  = FirebaseAuth.getInstance().getCurrentUser();
        collectionReference2 = db.collection("User");

        bugList = new ArrayList<>();
        bugList.clear();

        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));

        MaterialToolbar toolbar = findViewById(R.id.report_bug_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportBug.super.onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerview_report_Bug);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.fab_report_bug);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
            }
        });


        db.collection("Bugs").orderBy("timeStamp").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(ReportBug.this, "It's noting there", Toast.LENGTH_SHORT).show();
                }else {
                    for (QueryDocumentSnapshot bugDocumentSnapshot : queryDocumentSnapshots) {
                        BugReport bugReport = bugDocumentSnapshot.toObject(BugReport.class);
                        bugList.add(bugReport);

                    }
                }
                reportBugAdapter = new ReportBugAdapter(ReportBug.this,bugList);
                recyclerView.setAdapter(reportBugAdapter);


            }
        });

    }

    private void createPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_report_bug, null);
        builder.setView(view);

        bugDescription = view.findViewById(R.id.bug_description);
        reportButton = view.findViewById(R.id.save_report_bug);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bugDescription.getText().toString().isEmpty()){
                    saveReport();
                }else {
                    Snackbar.make(view,"Empty Not Allowed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ImageView cancelPopup = view.findViewById(R.id.report_Bug_cancel_popup);
        cancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void saveReport() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);

       collectionReference2.document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
               username = value.get("username").toString();
           }
       });

        final BugReport bugReport = new BugReport();
        bugReport.setDescription(bugDescription.getText().toString());
        bugReport.setTimeStamp(formattedDate);
        bugReport.setUsername(username);
        db.collection("Bugs").add(bugReport).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                List<BugReport> newBuglist = bugList;
                newBuglist.add(bugReport);
                reportBugAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


    }
}