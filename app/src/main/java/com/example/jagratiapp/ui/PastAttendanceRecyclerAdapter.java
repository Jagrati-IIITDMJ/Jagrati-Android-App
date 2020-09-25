package com.example.jagratiapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Students;

import java.util.List;
import java.util.Map;

public class PastAttendanceRecyclerAdapter extends RecyclerView.Adapter<PastAttendanceRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<String> recordedAttendance;
    private Map<String,Students> studentsMap;

    public PastAttendanceRecyclerAdapter(Context context, List<String> recordedAttendance, Map<String, Students> studentsMap) {
        this.context = context;
        this.recordedAttendance = recordedAttendance;
        this.studentsMap = studentsMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String studentId = recordedAttendance.get(position);

        if(studentsMap.get(studentId) != null){
            Students students = studentsMap.get(studentId);
            holder.studentName.setText(students.getStudentName());
            holder.villageName.setText(students.getVillageName());
            holder.studentID = students.getUid();
            holder.classID = students.getClassID();
            holder.groupID = students.getGroupID();

            holder.attendanceChecker.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return recordedAttendance.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView studentName;
        private TextView villageName;
        private ImageView attendanceChecker;
        private String studentID;
        private String classID;
        private String groupID;
        private CardView card1;
        private CardView card2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.studentName = itemView.findViewById(R.id.student_name);
            this.villageName = itemView.findViewById(R.id.student_village);
            this.attendanceChecker = itemView.findViewById(R.id.attendance_checker);
            card1 = itemView.findViewById(R.id.card1);
            card2 = itemView.findViewById(R.id.card2);
            card1.setCardBackgroundColor(Color.TRANSPARENT);
            card1.setCardElevation(0);
            card2.setCardElevation(0);
            card2.setCardBackgroundColor(Color.TRANSPARENT);


        }
    }
}
