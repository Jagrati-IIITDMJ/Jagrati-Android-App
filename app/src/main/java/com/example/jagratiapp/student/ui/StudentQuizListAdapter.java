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

import java.text.MessageFormat;
import java.util.List;

public class StudentQuizListAdapter extends RecyclerView.Adapter<StudentQuizListAdapter.ViewHolder> {
    private Context context;
    private List<Quiz> quizList;

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
    public void onBindViewHolder(@NonNull StudentQuizListAdapter.ViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
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
        private int noOfQues;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quizName_list);
            quizDesCription = itemView.findViewById(R.id.quizDescription_list);
            quizTime = itemView.findViewById(R.id.quizTime_list);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(noOfQues>0) {
                        context.startActivity(new Intent(context, QuestionsPage.class).putExtra("quizId", quizid));
                        Toast.makeText(context, ""+ quizList.size(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Quiz Not ready yet", Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }
    }
}
