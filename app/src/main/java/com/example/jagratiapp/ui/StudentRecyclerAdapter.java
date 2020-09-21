package com.example.jagratiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.StudentCompleteInfo;

import java.util.List;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.ViewHolder>  {
    private Context context;
    private List<Students> studentsListAdapter;

    public StudentRecyclerAdapter(Context context, List<Students> studentsList) {
        this.context = context;
        this.studentsListAdapter = studentsList;
    }

    @NonNull
    @Override
    public StudentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Students student = studentsListAdapter.get(position);
        holder.studentName.setText(student.getStudentName());
        holder.villageName.setText(student.getVillageName());
        holder.studentID = student.getRollno();
        holder.classID = student.getClassID();
        holder.groupID = student.getGroupID();

    }

    @Override
    public int getItemCount() {
        return studentsListAdapter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        private TextView studentName;
        private TextView villageName;
        private String studentID;
        private String classID;
        private String groupID;
        private CardView card1;
        private CardView card2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name);
            villageName = itemView.findViewById(R.id.student_village);
            card1 = itemView.findViewById(R.id.card1);
            card2 = itemView.findViewById(R.id.card2);
            card1.setCardBackgroundColor(Color.TRANSPARENT);
            card1.setCardElevation(0);
            card2.setCardElevation(0);
            card2.setCardBackgroundColor(Color.TRANSPARENT);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, StudentCompleteInfo.class).putExtra("studentID",studentID)
            .putExtra("classID",classID)
            .putExtra("groupID",groupID));


        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
