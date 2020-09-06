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
    private OnStudentListener onStudentListener;

    public AttendenceRecyclerAdapter(Context context, List<Students> studentsList, Map<String, Boolean> recordedAttendance,OnStudentListener onStudentListener) {
        this.context = context;
        this.studentsList = studentsList;
        this.recordedAttendance = recordedAttendance;
        this.onStudentListener = onStudentListener;
    }

    @NonNull
    @Override
    public AttendenceRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_card,parent,false);
        return new ViewHolder(view,onStudentListener);
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
        private OnStudentListener onStudentListener;

        public ViewHolder(@NonNull View itemView,OnStudentListener onStudentListener) {
            super(itemView);
            this.onStudentListener = onStudentListener;
            this.studentName = itemView.findViewById(R.id.student_name);
            this.villageName = itemView.findViewById(R.id.student_village);
            this.attendanceChecker = itemView.findViewById(R.id.attendance_checker);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            boolean state;
//            context.startActivity(new Intent(context, StudentCompleteInfo.class).putExtra("studentID",studentID)
//                    .putExtra("classID",classID)
//                    .putExtra("groupID",groupID));
//
            if (recordedAttendance.get(studentID)){
                state = false;
                attendanceChecker.setVisibility(View.INVISIBLE);
            }
            else {
                state = true;
                attendanceChecker.setVisibility(View.VISIBLE);
            }
            onStudentListener.onStudentClick(getAdapterPosition(),state);
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public interface OnStudentListener{
        void onStudentClick(int position,boolean state);
    }
}
