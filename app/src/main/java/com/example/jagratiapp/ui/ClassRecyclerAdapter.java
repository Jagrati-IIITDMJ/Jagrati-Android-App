package com.example.jagratiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jagratiapp.Group_page;
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
        holder.classNameList.setText(classes.getClassName());
        holder.classUid = classes.getuId();

    }

    public int getItemCount() {
        return classesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView classNameList;
        private String classUid;

        private Button classFurther;
        private Button edit;
        private Button delete;

        public ViewHolder(@NonNull final View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            classNameList = itemView.findViewById(R.id.classname_list);
            classFurther = itemView.findViewById(R.id.class_further);
            classFurther.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.class_further:
                    goToGroup(classUid,context);
            }
        }

        private void goToGroup(String classUid, Context context) {
            if(classUid != null) {
                context.startActivity(new Intent(context, Group_page.class).putExtra("DocId", classUid));
            }else {
                Toast.makeText(context,"Null hai boi",Toast.LENGTH_LONG).show();
            }


        }
    }

}
