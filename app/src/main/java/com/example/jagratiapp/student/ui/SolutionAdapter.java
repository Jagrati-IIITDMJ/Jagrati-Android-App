package com.example.jagratiapp.student.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Question;

import java.util.List;
import java.util.Map;

public class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.ViewHolder> {
    private List<Question> questonList;
    private Map<String,String> answerList;
    private Context context;

    public SolutionAdapter(Context context,List<Question> questionList, Map<String, String> answerList) {
        this.questonList = questionList;
        this.answerList = answerList;
        this.context = context;
    }

    @NonNull
    @Override
    public SolutionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_solution_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionAdapter.ViewHolder holder, int position) {
        Question question = questonList.get(position);
        String answer = answerList.get(question.getQuestionId());
        String correctAnswer = answerList.get(question.getQuestionId());

        holder.optionA.setText(question.getOption1());
        holder.optionB.setText(question.getOption2());
        holder.optionC.setText(question.getOption3());
        holder.optionD.setText(question.getOption4());
        if (correctAnswer.equals(question.getOption1()))
            holder.optionA.setChecked(true);
        if (correctAnswer.equals(question.getOption2()))
            holder.optionB.setChecked(true);
        if (correctAnswer.equals(question.getOption3()))
            holder.optionC.setChecked(true);
        if (correctAnswer.equals(question.getOption4()))
            holder.optionD.setChecked(true);
        if (answer!= null){
            if (!correctAnswer.equals(answer)){
                if (answer.equals(question.getOption1()))
                    holder.optionA.setChecked(true);
                if (answer.equals(question.getOption2()))
                    holder.optionB.setChecked(true);
                if (answer.equals(question.getOption3()))
                    holder.optionC.setChecked(true);
                if (answer.equals(question.getOption4()))
                    holder.optionD.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        private RadioButton optionA;
        private RadioButton optionB;
        private RadioButton optionC;
        private RadioButton optionD;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.question = itemView.findViewById(R.id.question);
            this.optionA = itemView.findViewById(R.id.optionA);
            this.optionB = itemView.findViewById(R.id.optionB);
            this.optionC = itemView.findViewById(R.id.optionC);
            this.optionD = itemView.findViewById(R.id.optionD);

        }
    }
}
