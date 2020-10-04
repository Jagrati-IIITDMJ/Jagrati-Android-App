package com.example.jagratiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewDebug;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jagratiapp.model.Classes;
import com.example.jagratiapp.model.Groups;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.ui.ClassRecyclerAdapter;
import com.example.jagratiapp.ui.GroupDiffUtil;
import com.example.jagratiapp.ui.GroupRecyclerAdapter;
import com.example.jagratiapp.ui.StudentDiffUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Group_page extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText groupname;
    private Button saveButton;
    private Button cancel;
    private List<Groups> groupList;
    private String classUid;
    private GroupRecyclerAdapter groupRecyclerAdapter;
    private CollectionReference collectionReference;
    private Context ctx;
    private MaterialToolbar toolbar;
    private String classp;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        ctx = Group_page.this;
        toolbar = findViewById(R.id.class_page_toolbar);
        fab = findViewById(R.id.fab_group_page);
        classUid = getIntent().getStringExtra("DocId");
        classp = getIntent().getStringExtra("class_name");
        MaterialToolbar toolbar = findViewById(R.id.group_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(classp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       // getActionBar().setDisplayHomeAsUpEnabled(true);

        assert classUid != null;
        collectionReference = db.collection("Classes").document(classUid).collection("Groups");


        groupList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview_group_page);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                createPopup();
            }
        });



        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(Group_page.this, "It's nothing there", Toast.LENGTH_SHORT).show();
                }else {
                    for (QueryDocumentSnapshot groupDocumentSnapshot : queryDocumentSnapshots) {
                        Groups group = groupDocumentSnapshot.toObject(Groups.class);
                        group.setUid(groupDocumentSnapshot.getId());
                        groupList.add(group);
                    }
                }
                    groupRecyclerAdapter = new GroupRecyclerAdapter(Group_page.this, groupList,classUid);
                    LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(ctx,
                            R.anim.layout_animation_fall_down);

                    recyclerView.setLayoutAnimation(controller);
                    recyclerView.setAdapter(groupRecyclerAdapter);
                    recyclerView.scheduleLayoutAnimation();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void createPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_group,null);

        groupname = view.findViewById(R.id.group_name_pop);
        saveButton = view.findViewById(R.id.saveGroup_pop);
        cancel = view.findViewById(R.id.cancelGrouppop);

        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groupname.getText().toString().isEmpty()){
                    saveGroup(view);
                }else
                {
                    Snackbar.make(view,"Empty Not Allowed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void saveGroup(View view) {
        final Groups group = new Groups();
        group.setGroupName(groupname.getText().toString().trim());
        collectionReference.add(group).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                // to add delay
               new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Groups> newGroupList = groupList;
                        group.setUid(documentReference.getId());
                        newGroupList.add(group);

//                        GroupDiffUtil diffUtil = new GroupDiffUtil(groupList,newGroupList);
//                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
//                        diffResult.dispatchUpdatesTo(groupRecyclerAdapter);
                        groupRecyclerAdapter.notifyDataSetChanged();
                        dialog.dismiss();

                   }
                },60);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Group_page.this,e.getMessage().toString().trim(),Toast.LENGTH_SHORT).show();

            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();


    }
}