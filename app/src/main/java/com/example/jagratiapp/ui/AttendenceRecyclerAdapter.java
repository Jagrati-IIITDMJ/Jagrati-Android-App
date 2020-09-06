package com.example.jagratiapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Students;

import java.util.List;
import java.util.Map;

public class AttendenceRecyclerAdapter extends RecyclerView.Adapter<AttendenceRecyclerAdapter.ViewHolder>  {
    private Context context;
    private List<Students> studentsList;
    private Map<String,Boolean> recordedAttendance;

    public AttendenceRecyclerAdapter(Context context, List<Students> studentsList, Map<String, Boolean> recordedAttendance) {
        this.context = context;
        this.studentsList = studentsList;
        this.recordedAttendance = recordedAttendance;
    }

    @NonNull
    @Override
    public AttendenceRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Students student = studentsList.get(position);
        holder.studentName.setText(student.getStudentName());
        holder.villageName.setText(student.getVillageName());
        holder.studentID = student.getUid();
        boolean attendanceState = recordedAttendance.get(holder.studentID);
        if (attendanceState){
            holder.attendanceChecker.setVisibility(View.VISIBLE);
        }
        holder.classID = student.getClassID();
        holder.groupID = student.getGroupID();

    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        private TextView studentName;
        private TextView villageName;
        private ImageView attendanceChecker;
        private String studentID;
        private String classID;
        private String groupID;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name);
            villageName = itemView.findViewById(R.id.student_village);
            attendanceChecker = itemView.findViewById(R.id.attendance_checker);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
//            context.startActivity(new Intent(context, StudentCompleteInfo.class).putExtra("studentID",studentID)
//                    .putExtra("classID",classID)
//                    .putExtra("groupID",groupID));

            if (recordedAttendance.get(studentID)){
                recordedAttendance.put(studentID,false);
            }
            else {
                recordedAttendance.put(studentID,true);
            }

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
