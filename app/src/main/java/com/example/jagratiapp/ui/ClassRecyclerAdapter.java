package com.example.jagratiapp.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Classes;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassRecyclerAdapter extends RecyclerView.Adapter<ClassRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Classes> classesList;
    private Button item;

    public ClassRecyclerAdapter(Context context, List<Classes> classesList) {
        this.context = context;
        this.classesList = classesList;
    }

    @NonNull
    @Override
    public ClassRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_cardview,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassRecyclerAdapter.ViewHolder holder, int position) {

        Classes classes = classesList.get(position);
        holder.classname_List.setText(classes.getClassName());
        holder.classUid = classes.getuId();

    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView classname_List;
        String classUid;

        public ViewHolder(@NonNull final View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            classname_List = itemView.findViewById(R.id.classname_list);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,classUid,Toast.LENGTH_SHORT).show();


                }
            });
        }
    }

}
