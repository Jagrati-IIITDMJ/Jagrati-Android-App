package com.example.jagratiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Students;
import com.example.jagratiapp.StudentCompleteInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Students student = studentsListAdapter.get(position);
        holder.studentName.setText(student.getStudentName());
        holder.villageName.setText(student.getVillageName());
        holder.studentID = student.getRollno();
        holder.classID = student.getClassID();
        holder.groupID = student.getGroupID();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if (student.getStudent_dp() != null) {
            Toast.makeText(context,"hhhhhhhhhh",Toast.LENGTH_SHORT).show();
            final long FIVE_MEGABYTE = 5 * 1024 * 1024;
            Bitmap bitmap = null;
            storageReference.child("students/" + student.getRollno() + ".jpg")
                    .getBytes(FIVE_MEGABYTE)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Toast.makeText(context,"sb mst h",Toast.LENGTH_SHORT).show();
                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            holder.student_dp.setImageBitmap(bm);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,"Kuch to gadabad h",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
            Toast.makeText(context,"Kuch to gadabasdfsdfdsfd h",Toast.LENGTH_SHORT).show();

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
        private ImageView student_dp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.student_name);
            villageName = itemView.findViewById(R.id.student_village);
            student_dp = itemView.findViewById(R.id.student_card_imageView);
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
