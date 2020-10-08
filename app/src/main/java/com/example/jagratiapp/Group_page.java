package com.example.jagratiapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Groups;
import com.example.jagratiapp.ui.GroupRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Group_page extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
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
    private Paint p = new Paint();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));
        

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

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT){
                    createWarningPopup(groupList.get(viewHolder.getAdapterPosition()).getGroupName(),viewHolder);
                }
                else {
                    groupRecyclerAdapter.notifyDataSetChanged();
                    Toast.makeText(Group_page.this,"Edit",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){

                        View view = viewHolder.itemView;

                        Toast.makeText(Group_page.this,"sfsdfsd", Toast.LENGTH_SHORT).show();
                        final EditText groupName =view.findViewById(R.id.classname_list);
                        final ImageButton save = view.findViewById(R.id.save_class);
                        groupName.setEnabled(true);

                        save.setVisibility(View.VISIBLE);
                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int position = viewHolder.getAdapterPosition();
                                groupList.get(position).setGroupName(groupName.getText().toString().trim());
                                groupRecyclerAdapter.notifyItemChanged(position);
                                FirebaseFirestore.getInstance().collection("Classes").document(classUid).collection("Groups").document(groupList.get(position).getUid()).update("groupName",groupName.getText().toString().trim());
                                save.setVisibility(View.GONE);
                                groupName.setEnabled(false);
                            }
                        });

//                        p.setColor(Color.parseColor("#388E3C"));
//                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
//                        c.drawRect(background,p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tick);
//                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
//                        //c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tick1);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        //c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

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

    private void createWarningPopup(String name, final RecyclerView.ViewHolder viewHolder) {
        Button yes,no;
        TextView warningMessage;
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.delete_popup,null);

        yes = view.findViewById(R.id.confirm_delete);
        no= view.findViewById(R.id.cancel_delete);
        warningMessage = view.findViewById(R.id.warning_delete);
        warningMessage.setText("You want to delete " + name +" Are your sure you want to delete confirm it   ");
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(Group_page.this,"Deleted",Toast.LENGTH_SHORT).show();
                collectionReference.document(String.valueOf(groupList.get(viewHolder.getAdapterPosition()).getUid())).delete() ;
                groupRecyclerAdapter.removeItem(viewHolder);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupRecyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                dialog.dismiss();
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