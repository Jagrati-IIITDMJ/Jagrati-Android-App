package com.example.jagratiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.QuizListPage;
import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Classes;

import java.util.List;

public class QuizClassAdapter extends RecyclerView.Adapter<QuizClassAdapter.ViewHolder>{
    private Context context;
    private List<Classes> classList;
    private Button classFurther;

    public QuizClassAdapter(Context context, List<Classes> classList) {
        this.context = context;
        this.classList = classList;
    }

    @NonNull
    @Override
    public QuizClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_cardview,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizClassAdapter.ViewHolder holder, int position) {
        Classes classes = classList.get(position);
        holder.classNameList.setText(classes.getClassName());
        holder.classid = classes.getuId();

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView classNameList;
        private String classid;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context = ctx;
            classNameList = itemView.findViewById(R.id.classname_list);
            classFurther = itemView.findViewById(R.id.class_further);
            classFurther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(classid != null)
                    context.startActivity(new Intent(context, QuizListPage.class).putExtra("ClassID",classid));
                    else {
                        Toast.makeText(context, "Null", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
