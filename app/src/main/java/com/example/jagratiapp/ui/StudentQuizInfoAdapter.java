package com.example.jagratiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.StudentCompleteInfo;
import com.example.jagratiapp.model.Quiz;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.student.SolutionPage;
import com.example.jagratiapp.student.Util.StudentAPI;
import com.example.jagratiapp.student.ui.StudentQuizListAdapter;

import java.text.MessageFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentQuizInfoAdapter extends RecyclerView.Adapter<StudentQuizInfoAdapter.ViewHolder> {
    private Context context;
    private List<Quiz> quizList;
    private Students student;

    public StudentQuizInfoAdapter(Context context, List<Quiz> quizList,Students student) {
        this.context = context;
        this.quizList = quizList;
        this.student = student;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Quiz quiz = quizList.get(position);
        holder.quizName.setText(quiz.getQuizName());
        holder.quizDesCription.setText(quiz.getQuizDescription());
        holder.quizid = quiz.getQuizID();
        holder.noOfQues = quiz.getNumberOfQues();
        holder.quizTime.setText(MessageFormat.format("Time: {0} min", quiz.getQuesTime()));
        holder.marks.setText(MessageFormat.format("Marks: {0} Out of {1}", quiz.getMarks(), quiz.getNumberOfQues()));
        holder.marks.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView quizName;
        private TextView quizDesCription;
        private String quizid;
        private TextView quizTime;
        private TextView marks;
        private int noOfQues;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quizName_list);
            quizDesCription = itemView.findViewById(R.id.quizDescription_list);
            quizTime = itemView.findViewById(R.id.quizTime_list);
            marks = itemView.findViewById(R.id.marks_obtained_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, SolutionPage.class).putExtra("classId", student.getClassID())
                            .putExtra("groupId",student.getGroupID())
                            .putExtra("quizId",quizid)
                            .putExtra("studentRollNo",student.getRollno()));
                }
            });
        }
    }
}
