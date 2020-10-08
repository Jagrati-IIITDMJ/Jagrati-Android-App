package com.example.jagratiapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.QuestionAddPage;
import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Quiz;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.MessageFormat;
import java.util.List;

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
        holder.quizDescription.setText(quiz.getQuizDescription());
        holder.quizTime.setText(MessageFormat.format("Time: {0} min", quiz.getQuesTime()));
        holder.quizid = quiz.getQuizID();



    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView quizName;
        private TextView quizDescription;
        private String quizid;
        private TextView quizTime;


        long down,up;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quizName = itemView.findViewById(R.id.quizName_list);
            quizDescription = itemView.findViewById(R.id.quizDescription_list);
            quizTime = itemView.findViewById(R.id.quizTime_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, QuestionAddPage.class).putExtra("ClassID",classid).putExtra("quizid",quizid).putExtra("quizName",quizName.getText().toString()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final View view1 = LayoutInflater.from(context).inflate(R.layout.onlongpress_popup,null);
                    builder.setView(view1);
                    final Dialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    Button delete = view1.findViewById(R.id.delete_longpress);
                    Button edit = view1.findViewById(R.id.edit_longpress);

                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            createPopup(quizid,getAdapterPosition());
                        }
                    });
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            createWarningPopup(getAdapterPosition());
                        }
                    });
                    return false;
                }

            });
        }

        private void createPopup(final String quizid, final int adapterPosition) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View view4 = LayoutInflater.from(context).inflate(R.layout.popup_addquiz,null);

            final EditText quizNameED = view4.findViewById(R.id.quizName_pop);
            final EditText quizDescriptionED = view4.findViewById(R.id.quizDescription_pop);
            final EditText quizTimeED = view4.findViewById(R.id.quizTime_pop);
            Button addQuiz = view4.findViewById(R.id.savequiz_pop);

            quizNameED.setText(quizList.get(adapterPosition).getQuizName());
            quizDescriptionED.setText(quizList.get(adapterPosition).getQuizDescription());
            quizTimeED.setText(String.valueOf(quizList.get(adapterPosition).getQuesTime()));

            builder.setView(view4);
            final Dialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            addQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String quizName = quizNameED.getText().toString();
                    final String quizDescription = quizDescriptionED.getText().toString();
                    final String quizTime = quizTimeED.getText().toString();

                    if(!quizName.isEmpty()
                            && !quizDescription.isEmpty()
                        && !quizTime.isEmpty()){
                        final Quiz quiz = new Quiz(quizName,quizDescription,Integer.parseInt(quizTime));
                        quiz.setQuizID(quizid);
                        FirebaseFirestore.getInstance().collection("Classes").document(classid)
                                .collection("Quizzes").document(quizid)
                                .set(quiz)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        quizList.get(adapterPosition).setQuizName(quizName);
                                        quizList.get(adapterPosition).setQuizDescription(quizDescription);
                                        quizList.get(adapterPosition).setQuesTime(Integer.parseInt(quizTime));
                                        notifyItemChanged(adapterPosition);
                                        Snackbar.make(view4,"Quiz Updated",Snackbar.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(view4,"Quiz Updated",Snackbar.LENGTH_SHORT).show();
                                    }
                                });

                    }else
                    {
                        Snackbar.make(view,"Empty Not Allowed",Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void createWarningPopup(final int adapterPosition) {
            Button yes,no;
            TextView warningMessage;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.delete_popup,null);

            yes = view.findViewById(R.id.confirm_delete);
            no= view.findViewById(R.id.cancel_delete);
            warningMessage = view.findViewById(R.id.warning_delete);
            warningMessage.setText("You want to delete " );
            builder.setView(view);
            final Dialog dialog = builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("Classes").document(classid)
                            .collection("Quizzes").document(quizid)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                    quizList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(adapterPosition);
                    dialog.dismiss();
                }
            });
        }
    }

}
