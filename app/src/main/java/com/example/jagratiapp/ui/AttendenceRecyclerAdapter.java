package com.example.jagratiapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Students;

import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
//import static com.example.jagratiapp.R.drawable.ic_wrong_checkbox;

public class AttendenceRecyclerAdapter extends RecyclerView.Adapter<AttendenceRecyclerAdapter.ViewHolder>  {
    private Context context;
    private List<Students> studentsList;
    private Map<String,Boolean> recordedAttendance;
    private OnStudentListener onStudentListener;
    private boolean today;

    public AttendenceRecyclerAdapter(Context context, List<Students> studentsList, Map<String, Boolean> recordedAttendance, OnStudentListener onStudentListener,boolean today) {
        this.context = context;
        this.studentsList = studentsList;
        this.recordedAttendance = recordedAttendance;
        this.onStudentListener = onStudentListener;
        this.today = today;
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
        holder.classID = student.getClassID();
        holder.groupID = student.getGroupID();

        if(student.getUid()==null)
            Toast.makeText(context, "phat gai", LENGTH_SHORT).show();

        boolean attendanceState = recordedAttendance.get(holder.studentID);

        if (attendanceState){
            holder.attendanceChecker.setVisibility(View.VISIBLE);
        }

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
        private CardView card1;
        private CardView card2;

        private OnStudentListener onStudentListener;


        public ViewHolder(@NonNull View itemView,OnStudentListener onStudentListener) {
            super(itemView);
            this.onStudentListener = onStudentListener;
            this.studentName = itemView.findViewById(R.id.student_name);
            this.villageName = itemView.findViewById(R.id.student_village);
            this.attendanceChecker = itemView.findViewById(R.id.attendance_checker);
            card1 = itemView.findViewById(R.id.card1);
            card2 = itemView.findViewById(R.id.card2);
            card1.setCardBackgroundColor(Color.TRANSPARENT);
            card1.setCardElevation(0);
            card2.setCardElevation(0);
            card2.setCardBackgroundColor(Color.TRANSPARENT);

            if (today)
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
