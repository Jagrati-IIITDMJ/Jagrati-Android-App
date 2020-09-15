package com.example.jagratiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jagratiapp.QuestionAddPage;
import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Classes;
import com.example.jagratiapp.model.Quiz;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.ViewHolder> {
    private Context context;
    private List<Quiz> quizList;
    private String classid;

    public QuizListAdapter(Context context, List<Quiz> quizList,String classid) {
        this.context = context;
        this.quizList = quizList;
        this.classid =  classid;

    }

    public QuizListAdapter() {
    }

    @NonNull
    @Override
    public QuizListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListAdapter.ViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.quizName.setText(quiz.getQuizName());
        holder.quizDesCription.setText(quiz.getQuizDescription());
        holder.quizid = quiz.getQuizID();

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView quizName;
        private TextView quizDesCription;
        private String quizid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quizName_list);
            quizDesCription = itemView.findViewById(R.id.quizDescription_list);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, QuestionAddPage.class).putExtra("ClassID",classid).putExtra("quizid",quizid));
                }
            });



        }
    }
}
