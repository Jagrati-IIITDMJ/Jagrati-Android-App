package com.example.jagratiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.Group_page;
import com.example.jagratiapp.R;
import com.example.jagratiapp.StudentCompleteInfo;
import com.example.jagratiapp.model.Classes;
import com.example.jagratiapp.model.Students;

import java.io.Serializable;
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
        holder.studentID = student.getUid();
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
            card1 = itemView.findViewById(R.id.student_card1);
            card2 = itemView.findViewById(R.id.student_card2);

//            card1.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
////            card2.setCardBackgroundColor(Color.TRANSPARENT);
            studentName = itemView.findViewById(R.id.student_name);
            villageName = itemView.findViewById(R.id.student_village);

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
