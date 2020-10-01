package com.example.jagratiapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Question;

import java.util.List;

import static android.graphics.Color.rgb;

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
        holder.question.setText("Ques." +(++position) + " " + ques.getQuestion());
        holder.option1.setText("1. " + ques.getOption1());
        holder.option2.setText("2. " + ques.getOption2());
        holder.option3.setText("3. " + ques.getOption3());
        holder.option4.setText("4. " + ques.getOption4());

        if (ques.getCorrectOption().equals(ques.getOption1()))
            holder.option1.setTextColor(rgb(0, 128, 0));
        else if (ques.getCorrectOption().equals(ques.getOption2()))
            holder.option2.setTextColor(rgb(0, 128, 0));
        else if (ques.getCorrectOption().equals(ques.getOption3()))
            holder.option3.setTextColor(rgb(0, 128, 0));
        else if (ques.getCorrectOption().equals(ques.getOption4()))
            holder.option4.setTextColor(rgb(0, 128, 0));

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.QuesIn_ques_card);
            option1 = itemView.findViewById(R.id.Opt1In_ques_card);
            option2 = itemView.findViewById(R.id.Opt2In_ques_card);
            option3 = itemView.findViewById(R.id.Opt3In_ques_card);
            option4 = itemView.findViewById(R.id.Opt4In_ques_card);

        }
    }
}
