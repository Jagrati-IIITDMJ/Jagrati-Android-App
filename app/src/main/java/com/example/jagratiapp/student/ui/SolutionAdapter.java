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
    private Map<String,String> answerMap;
    private Context context;

    public SolutionAdapter(Context context,List<Question> questionList, Map<String, String> answerMap) {
        this.questonList = questionList;
        this.answerMap = answerMap;
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
        String answer = "";
        Question question = questonList.get(position);

        holder.question.setText(question.getQuestion());
        holder.optionA.setText(question.getOption1());
        holder.optionB.setText(question.getOption2());
        holder.optionC.setText(question.getOption3());
        holder.optionD.setText(question.getOption4());

        String correctAnswer = question.getCorrectOption();

        if (correctAnswer.equals(question.getOption1()))
            holder.optionA.setChecked(true);
        else if (correctAnswer.equals(question.getOption2()))
            holder.optionB.setChecked(true);
        else if (correctAnswer.equals(question.getOption3()))
            holder.optionC.setChecked(true);
        else if (correctAnswer.equals(question.getOption4()))
            holder.optionD.setChecked(true);

        if (answerMap.get(question.getQuestionId()) != null)
            answer = answerMap.get(question.getQuestionId());
//        if (answer != null){
            if (!correctAnswer.equals(answer)){
                if (answer.equals(question.getOption1()))
                    holder.optionA.setChecked(true);
                if (answer.equals(question.getOption2()))
                    holder.optionB.setChecked(true);
                if (answer.equals(question.getOption3()))
                    holder.optionC.setChecked(true);
                if (answer.equals(question.getOption4()))
                    holder.optionD.setChecked(true);
            //}
        }

            holder.optionA.setEnabled(false);
        holder.optionB.setEnabled(false);
        holder.optionC.setEnabled(false);
        holder.optionD.setEnabled(false);


    }

    @Override
    public int getItemCount() {
        return questonList.size();
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
