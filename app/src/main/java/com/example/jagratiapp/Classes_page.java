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
import android.transition.Fade;
import android.view.View;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Classes;
import com.example.jagratiapp.ui.ClassRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Classes_page extends AppCompatActivity {

    private ImageButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText classname;
    private Button saveButton;
    private Button cancel;
    private List<Classes> classesList;
    private RecyclerView recyclerView;
    private ClassRecyclerAdapter classRecyclerAdapter;
    private Context ctx = this;
    private ConstraintLayout constraintLayout;
    private Paint p = new Paint();


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Classes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_page);
        fab = findViewById(R.id.fab_class_page);

        classesList = new ArrayList<>();
        constraintLayout = findViewById(R.id.classpaget);

        final MaterialToolbar toolbar = findViewById(R.id.class_page_toolbar);
        setSupportActionBar(toolbar);


        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerview_classes_page);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT){
                    createWarningPopup(classesList.get(viewHolder.getAdapterPosition()).getClassName(),viewHolder);
                }
                else {
                    //classRecyclerAdapter.notifyDataSetChanged();
//                    View view = viewHolder.itemView;
//                    TextView className = view.findViewById(R.id.classname_list);
//                    className.setText("dfsdfsdfsdg");
                    classRecyclerAdapter.edit(viewHolder);
                    Toast.makeText(Classes_page.this,"Edit",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tick);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        //c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        TextView className = itemView.findViewById(R.id.classname_list);
                        className.setText("dfsdfsdfsdg");
//                        p.setColor(Color.parseColor("#D32F2F"));
//                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
//                        c.drawRect(background,p);
//                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tick1);
//                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        //c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
            }
        });


        collectionReference.orderBy("className").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(Classes_page.this, "It's noting there", Toast.LENGTH_SHORT).show();
                }else {
                    for (QueryDocumentSnapshot classDocumentSnapshot : queryDocumentSnapshots) {
                        Classes classes = classDocumentSnapshot.toObject(Classes.class);
                        classes.setuId(classDocumentSnapshot.getId());
                        classesList.add(classes);
                    }
                }
                classRecyclerAdapter = new ClassRecyclerAdapter(Classes_page.this, classesList);
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(ctx,
                        R.anim.layout_animation_fall_down);
                recyclerView.setLayoutAnimation(controller);
                recyclerView.setAdapter(classRecyclerAdapter);
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

            yes = view.findViewById(R.id.confirml_signout);
            no= view.findViewById(R.id.cancel_signout);
            warningMessage = view.findViewById(R.id.warning);
            warningMessage.setText("You want to delete " + name +" Are your sure you want to delete confirm it   ");
            builder.setView(view);
            dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Toast.makeText(Classes_page.this,"Deleted",Toast.LENGTH_SHORT).show();
                    collectionReference.document(String.valueOf(classesList.get(viewHolder.getAdapterPosition()).getuId())).delete() ;
                    classRecyclerAdapter.removeItem(viewHolder);
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    classRecyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    dialog.dismiss();
                }
            });
    }

    private void createPopup() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_classes,null);

        classname = view.findViewById(R.id.class_name_pop);
        saveButton = view.findViewById(R.id.saveClass_pop);
        cancel = view.findViewById(R.id.cancelClasspop);

        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!classname.getText().toString().isEmpty()){
                    saveclass(view);
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

    private void saveclass(View view) {
        final Classes classes = new Classes();
        classes.setClassName(classname.getText().toString().trim());
        collectionReference.add(classes).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(final DocumentReference documentReference) {
                // to add delay
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Classes> newClassList = classesList;
                        classes.setuId(documentReference.getId());
                        newClassList.add(classes);

//                        ClassDiffUtil diffUtil = new ClassDiffUtil(classesList,newClassList);
//                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtil);
//                        diffResult.dispatchUpdatesTo(classRecyclerAdapter);
                        classRecyclerAdapter.notifyDataSetChanged();

                  dialog.dismiss();

                    }
                },1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Classes_page.this,e.getMessage().toString().trim(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
