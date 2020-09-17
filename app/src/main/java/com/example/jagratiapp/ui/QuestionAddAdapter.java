package com.example.jagratiapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Question;
import com.example.jagratiapp.model.Students;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionAddAdapter extends RecyclerView.Adapter<QuestionAddAdapter.ViewHolder> {
    private Context context;
    private List<Question> questionList;

    public QuestionAddAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    public QuestionAddAdapter(){
    }

    @NonNull
    @Override
    public QuestionAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAddAdapter.ViewHolder holder, int position) {
        Question ques = questionList.get(position);
        holder.question.setText(ques.getQuestion());
        holder.option1.setText(ques.getOption1());
        holder.option2.setText(ques.getOption2());
        holder.option3.setText(ques.getOption3());
        holder.option4.setText(ques.getOption4());
        holder.correctanswer.setText(ques.getCorrectOption());

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView question;
        private TextView option1;
        private TextView option2;
        private TextView option3;
        private TextView option4;
        private TextView correctanswer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.QuesIn_ques_card);
            option1 = itemView.findViewById(R.id.Opt1In_ques_card);
            option2 = itemView.findViewById(R.id.Opt2In_ques_card);
            option3 = itemView.findViewById(R.id.Opt3In_ques_card);
            option4 = itemView.findViewById(R.id.Opt4In_ques_card);
            correctanswer = itemView.findViewById(R.id.AnsIn_ques_card);

        }
    }
}
