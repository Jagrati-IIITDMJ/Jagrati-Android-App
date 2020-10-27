package com.example.jagratiapp.student.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Question;
import com.example.jagratiapp.student.QuestionsPage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

import static android.graphics.Color.rgb;
import static android.graphics.Color.toArgb;

public class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.ViewHolder> {
    private List<Question> questonList;
    private Map<String,String> answerMap;
    private Context context;
    private String quizId;

    public SolutionAdapter(Context context,List<Question> questionList, Map<String, String> answerMap,String quizId) {
        this.questonList = questionList;
        this.answerMap = answerMap;
        this.context = context;
        this.quizId = quizId;
    }

    @NonNull
    @Override
    public SolutionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_solution_cardview,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final SolutionAdapter.ViewHolder holder, int position) {
        AppCompatRadioButton rb;
        rb = new AppCompatRadioButton(context);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.DKGRAY
                        , rgb(255, 0, 0),
                }
        );

        ColorStateList correctStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.DKGRAY
                        , rgb(0, 128, 0),
                }
        );

        Question question = questonList.get(position);
        String answer = question.getCorrectOption();

        if (question.getQuestionUri() == null){
            holder.question.setVisibility(View.VISIBLE);
            holder.question.setText((++position) +". " +(question.getQuestion()));
            holder.questionImage.setVisibility(View.GONE);
        }else {
            holder.questionImage.setVisibility(View.VISIBLE);
            holder.question.setVisibility(View.GONE);
            final long FIVE_MEGABYTE = 5 * 1024 * 1024;
            StorageReference storageReference  = FirebaseStorage.getInstance().getReference();
            storageReference.child("quizzes/" + quizId + "/" + question.getQuestionUri())
                    .getBytes(FIVE_MEGABYTE)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.questionImage.setImageBitmap(bm);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        holder.optionA.setText(question.getOption1());
        holder.optionA.setClickable(false);
        holder.optionB.setText(question.getOption2());
        holder.optionB.setClickable(false);
        holder.optionC.setText(question.getOption3());
        holder.optionC.setClickable(false);
        holder.optionD.setText(question.getOption4());
        holder.optionD.setClickable(false);

        String correctAnswer = question.getCorrectOption();

        if (correctAnswer.equals(question.getOption1())) {
            holder.optionA.setChecked(true);
            holder.optionA.setButtonTintList(correctStateList);
        }
        else if (correctAnswer.equals(question.getOption2())){
            holder.optionB.setChecked(true);
            holder.optionB.setButtonTintList(correctStateList);
        }

        else if (correctAnswer.equals(question.getOption3())) {
            holder.optionC.setChecked(true);
            holder.optionC.setButtonTintList(correctStateList);
        }
        else if (correctAnswer.equals(question.getOption4()))
        {
            holder.optionD.setChecked(true);
            holder.optionD.setButtonTintList(correctStateList);
        }


        if (answerMap != null && answerMap.containsKey(question.getQuestionId())) {
            answer = answerMap.get(question.getQuestionId());
            if (!correctAnswer.equals(answer)) {
                holder.result.setText("Incorrect Answer");
                holder.result.setTextColor(rgb(200, 0, 0));
                if (answer.equals(question.getOption1())) {
                    holder.optionA.setChecked(true);
                    holder.optionA.setButtonTintList(colorStateList);
                }
                else if (answer.equals(question.getOption2())) {
                    holder.optionB.setChecked(true);
                    holder.optionB.setButtonTintList(colorStateList);
                }
                else if (answer.equals(question.getOption3())) {
                    holder.optionC.setChecked(true);
                    holder.optionC.setButtonTintList(colorStateList);
                }
                else if (answer.equals(question.getOption4())) {
                    holder.optionD.setChecked(true);
                    holder.optionD.setButtonTintList(colorStateList);
                }
            }
            else {
                holder.result.setText("Correct Answer");
                holder.result.setTextColor(rgb(0, 128, 0));
            }
        }
        else {
            holder.result.setText("Not Given");
            holder.result.setTextColor(rgb(255, 0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return questonList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        private ImageView questionImage;
        private RadioButton optionA;
        private RadioButton optionB;
        private RadioButton optionC;
        private RadioButton optionD;
        private TextView result;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.question = itemView.findViewById(R.id.question);
            this.optionA = itemView.findViewById(R.id.optionA);
            this.optionB = itemView.findViewById(R.id.optionB);
            this.optionC = itemView.findViewById(R.id.optionC);
            this.optionD = itemView.findViewById(R.id.optionD);
            this.result = itemView.findViewById(R.id.correct_answer);
            this.questionImage = itemView.findViewById(R.id.question_image_solution);
        }
    }
}
