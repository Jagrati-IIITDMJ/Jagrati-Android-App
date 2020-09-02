package com.example.jagratiapp;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jagratiapp.model.Classes;
import com.example.jagratiapp.model.Groups;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.ui.ClassRecyclerAdapter;
import com.example.jagratiapp.ui.GroupDiffUtil;
import com.example.jagratiapp.ui.GroupRecyclerAdapter;
import com.example.jagratiapp.ui.StudentDiffUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText groupname;
    private Button saveButton;
    private List<Groups> groupList;
    private String classUid;
    private GroupRecyclerAdapter groupRecyclerAdapter;
    private CollectionReference collectionReference;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        fab = findViewById(R.id.fab_group_page);
        classUid = getIntent().getStringExtra("DocId");

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

        //Yaha add kiya hai onStart wala
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot groupDocumentSnapshot : queryDocumentSnapshots) {
                        Groups group = groupDocumentSnapshot.toObject(Groups.class);
                        //isko hatana mat
                        group.setUid(groupDocumentSnapshot.getId());
                        groupList.add(group);
                    }
                    groupRecyclerAdapter = new GroupRecyclerAdapter(Group_page.this, groupList,classUid);
                    recyclerView.setAdapter(groupRecyclerAdapter);
                    //groupRecyclerAdapter.notifyDataSetChanged();



                } else {
                    Toast.makeText(Group_page.this, "It's nothing there", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        //Yaha tak kiya hai

    }

    private void createPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_group,null);

        groupname = view.findViewById(R.id.group_name_pop);
        saveButton = view.findViewById(R.id.saveGroup_pop);

        builder.setView(view);
        dialog = builder.create();
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
                        //groupRecyclerAdapter.notifyDataSetChanged();


                       // GroupDiffUtil diffUtil = new GroupDiffUtil(groupList,newGroupList);
                       // DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);

                       /* diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
                            @Override
                            public void onInserted(int position, int count) {
                                //groupRecyclerAdapter.notifyItemInserted(position);
                            }

                            @Override
                            public void onRemoved(int position, int count) {
                               // groupRecyclerAdapter.notifyItemRangeRemoved(position,count);

                            }

                            @Override
                            public void onMoved(int fromPosition, int toPosition) {
                                //groupRecyclerAdapter.notifyItemMoved(fromPosition,toPosition);

                            }

                            @Override
                            public void onChanged(int position, int count, @Nullable Object payload) {
                               // groupRecyclerAdapter.notifyItemChanged(position);

                            }
                        });*/
                        dialog.dismiss();


                    }
                },600);
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