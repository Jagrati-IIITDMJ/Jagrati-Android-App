package com.example.jagratiapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.model.Question;
import com.example.jagratiapp.ui.QuestionAddAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.net.InternetDomainName;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionAddPage extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST =69;
    private String classid;
    private ImageButton questionImage;
    private Uri filePath;
    private EditText question;
    private EditText option1;
    private EditText option2;
    private EditText option3;
    private EditText option4;
    private RadioButton option1RadioButton;
    private RadioButton option2RadioButton;
    private RadioButton option3RadioButton;
    private RadioButton option4RadioButton;
    private Button savequesButton;
    private String quizid;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private RecyclerView recyclerView;
    private FloatingActionButton addQuestion;
    private List<Question> questionList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;
    private DocumentReference documentToAddNumOfQues;
    private QuestionAddAdapter questionAddAdapter;
    private String correctOption;
    private String quizName;
    private RadioGroup imageRadioGroup;
    private int radioId = 0;
    private Switch quizState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add_page);


        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appbar));

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        classid = bundle.getString("ClassID");
        quizid = bundle.getString("quizid");
        quizName = bundle.getString("quizName");

        MaterialToolbar toolbar = findViewById(R.id.quiz_add_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(quizName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addQuestion = findViewById(R.id.fab_ques_add);
        recyclerView = findViewById(R.id.quesAdd_recyclerview);
        quizState = findViewById(R.id.quizState);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionList = new ArrayList<>();

        collectionReference = db.collection("Classes").document(classid).collection("Quizzes").document(quizid).collection("Question");
        documentToAddNumOfQues = db.collection("Classes").document(classid).collection("Quizzes").document(quizid);

        documentToAddNumOfQues.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getBoolean("quizState") != null){
                            if (documentSnapshot.getBoolean("quizState")){
                                quizState.setChecked(true);
                            }
                            else {
                                quizState.setChecked(false);
                            }
                        }
                        else {
                            quizState.setChecked(false);
                        }
                    }
                });
        quizState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    documentToAddNumOfQues.update("quizState",true);
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(QuestionAddPage.this,"true",Toast.LENGTH_LONG).show();
//                        }
//                    })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(QuestionAddPage.this,"true sd",Toast.LENGTH_LONG).show();
//                                }
//                            });

                }
                else {
                    documentToAddNumOfQues.update("quizState",false);
                    Toast.makeText(QuestionAddPage.this,"false",Toast.LENGTH_LONG).show();
                }
            }
        });

        addQuestion.setOnClickListener(this);

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Question q = documentSnapshot.toObject(Question.class);
                        q.setQuestionId(documentSnapshot.getId());
                        questionList.add(q);
                    }
                }
                questionAddAdapter = new QuestionAddAdapter(QuestionAddPage.this, questionList,classid,quizid);
                recyclerView.setAdapter(questionAddAdapter);

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_ques_add:
                addQuestionPopup();
                break;

    }
}

    private void addQuestionPopup() {
       builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
       final View view = getLayoutInflater().inflate(R.layout.popup_add_question,null);

        questionImage = view.findViewById(R.id.question_imageButton);
        question = view.findViewById(R.id.QuesIn_ques_card_popup);
        option1 = view.findViewById(R.id.Opt1In_ques_card_popup);
        option2 = view.findViewById(R.id.Opt2In_ques_card_popup);
        option3 = view.findViewById(R.id.Opt3In_ques_card_popup);
        option4 = view.findViewById(R.id.Opt4In_ques_card_popup);
        option1RadioButton = view.findViewById(R.id.optionA_question_card_popup);
        option2RadioButton = view.findViewById(R.id.optionB_question_card_popup);
        option3RadioButton = view.findViewById(R.id.optionC_question_card_popup);
        option4RadioButton = view.findViewById(R.id.optionD_question_card_popup);
        savequesButton = view.findViewById(R.id.saveques_popup);
        ImageView cancelPopup = view.findViewById(R.id.cancel_popup);
        cancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        option1RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                int id = R.id.optionA_question_card_popup;
                checkButton(view,id);
            }
        });

        option2RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                int id = R.id.optionB_question_card_popup;
                checkButton(view,id);
            }
        });

        option3RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                int id = R.id.optionC_question_card_popup;
                checkButton(view,id);
            }
        });

        option4RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                int id = R.id.optionD_question_card_popup;
                checkButton(view,id);
            }
        });

        savequesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(question.getText().toString().trim())
                        && !TextUtils.isEmpty(option1.getText().toString().trim())
                        && !TextUtils.isEmpty(option2.getText().toString().trim())
                        && !TextUtils.isEmpty(option3.getText().toString().trim())
                        && !TextUtils.isEmpty(option4.getText().toString().trim())
                        && (option1RadioButton.isChecked()
                        || option2RadioButton.isChecked()
                        || option3RadioButton.isChecked()
                        || option4RadioButton.isChecked())){
                    dialog.dismiss();
                    if (option1RadioButton.isChecked())
                        correctOption = option1.getText().toString();
                    else if (option2RadioButton.isChecked())
                        correctOption = option2.getText().toString();
                    else if (option3RadioButton.isChecked())
                        correctOption = option3.getText().toString();
                    else if (option4RadioButton.isChecked())
                        correctOption = option4.getText().toString();

                    Question ques = new Question(question.getText().toString().trim(),option1.getText().toString().trim(),
                            option2.getText().toString().trim(),option3.getText().toString().trim(),option4.getText().toString().trim(),
                            correctOption,null);


                    saveQuestion(ques);
                }
                else {
                    Toast.makeText(QuestionAddPage.this,"Empty not allowed",Toast.LENGTH_SHORT).show();
                }
            }
        });



        builder.setView(view);
        dialog = builder.create();
       dialog.getWindow();
        dialog.show();

        questionImage.setOnClickListener(new View.OnClickListener() {
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
                dialog.dismiss();
                imagePopup(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void imagePopup(Bitmap bitmap){
        ImageView questionImage;
        Button saveQuestion;

        final View view1 = getLayoutInflater().inflate(R.layout.popup_add_question_image,null);
        builder.setView(view1);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        questionImage = view1.findViewById(R.id.add_question_image);
        saveQuestion = view1.findViewById(R.id.image_saveques_popup);
        imageRadioGroup = view1.findViewById(R.id.image_radio_group);
        ImageView cancelPopup = view1.findViewById(R.id.cancel_image_popup);

        cancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        questionImage.setImageBitmap(bitmap);
        saveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageQuestion(view1);
            }
        });

    }

    private void saveImageQuestion(final View view1){
        Question question = new Question();
        int radioId = imageRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = view1.findViewById(radioId);
        if (radioButton != null) {
            Toast.makeText(QuestionAddPage.this, radioId + " " + radioButton.getText(), Toast.LENGTH_SHORT).show();
            question.setCorrectOption(radioButton.getText().toString());
            question.setOption1("A");
            question.setOption2("B");
            question.setOption3("C");
            question.setOption4("D");
            uploadImage(question);
        }
    }

    private void uploadImage(final Question question)
    {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final String randomUri = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("quizzes/" + quizid + "/" + randomUri );

            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    String uri = randomUri;
                                    Toast.makeText(QuestionAddPage.this,uri+"",Toast.LENGTH_SHORT).show();
                                    progressDialog.setTitle("Question Uploaded");
                                    progressDialog.dismiss();
                                    question.setQuestionUri(uri);
                                    saveQuestion(question);
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
//                            Toast.makeText(StudentCompleteInfo.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
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


    private void saveQuestion(Question ques) {
        final Question q = ques;

        collectionReference.add(q).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                        List<Question> newQuestionList = questionList;
                        q.setQuestionId(documentReference.getId());
                        newQuestionList.add(q);
                        questionAddAdapter.notifyDataSetChanged();
                        documentToAddNumOfQues.update("numberOfQues",newQuestionList.size());
                        dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuestionAddPage.this,e.getMessage().toString().trim(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void checkButton(View view,int id){

        RadioButton clickedRadioButton = view.findViewById(id);
        clickedRadioButton.setChecked(true);
        RadioButton checkedRadioButton = view.findViewById(radioId);
        if (checkedRadioButton != null){
            checkedRadioButton.setChecked(false);
        }
        radioId = id;
    }
}