package com.example.jagratiapp.student.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Quiz;
import com.example.jagratiapp.student.QuestionsPage;
import com.example.jagratiapp.student.SolutionPage;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;
import java.util.List;

public class StudentQuizListAdapter extends RecyclerView.Adapter<StudentQuizListAdapter.ViewHolder> {
    private Context context;
    private List<Quiz> quizList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionToCheckQuiz = db.collection("Classes").document(StudentAPI.Instance().getClassUid()).collection("Groups")
            .document(StudentAPI.Instance().getGroupUid()).collection("Students").document(StudentAPI.Instance().getRollno()).collection("Quizzes");;



    public StudentQuizListAdapter(Context context, List<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public StudentQuizListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentQuizListAdapter.ViewHolder holder, int position) {
        final Quiz quiz = quizList.get(position);
        collectionToCheckQuiz.document(quiz.getQuizID()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                holder.checkIfGiven = true;
                                holder.marks.setText(MessageFormat.format("Marks: {0} Out of {1}", document.get("marks"), quiz.getNumberOfQues()));
                                holder.marks.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
        holder.quizName.setText(quiz.getQuizName());
        holder.quizDesCription.setText(quiz.getQuizDescription());
        holder.quizid = quiz.getQuizID();
        holder.noOfQues = quiz.getNumberOfQues();
        holder.quizTime.setText(MessageFormat.format("Time: {0} min", quiz.getQuesTime()));



    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView quizName;
        private TextView quizDesCription;
        private String quizid;
        private TextView quizTime;
        private TextView marks;
        private int noOfQues;
        private boolean checkIfGiven = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quizName_list);
            quizDesCription = itemView.findViewById(R.id.quizDescription_list);
            quizTime = itemView.findViewById(R.id.quizTime_list);
            marks = itemView.findViewById(R.id.marks_obtained_list);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(noOfQues>0 && !checkIfGiven) {
                        context.startActivity(new Intent(context, QuestionsPage.class).putExtra("quizId", quizid));
                        Toast.makeText(context, ""+ quizList.size(), Toast.LENGTH_SHORT).show();
                    }
                    else if(checkIfGiven){
                        context.startActivity(new Intent(context, SolutionPage.class).putExtra("classId",StudentAPI.Instance().getClassUid())
                        .putExtra("groupId",StudentAPI.Instance().getGroupUid())
                        .putExtra("quizId",quizid)
                        .putExtra("studentRollNo",StudentAPI.Instance().getRollno()));
                    }else{
                        Toast.makeText(context, "Quiz Not ready yet", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }
    }
}
