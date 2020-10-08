package com.example.jagratiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.Group_page;
import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Classes;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ClassRecyclerAdapter extends RecyclerView.Adapter<ClassRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Classes> classesList;


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
        holder.classp = classes.getClassName();
        holder.classNameList.setText(classes.getClassName());
        holder.classUid = classes.getuId();
    }

    public int getItemCount() {
        return classesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private EditText classNameList;
        private String classUid;
        private MaterialCardView classfurther;
        private String classp;


        private ImageButton save_edit;


        public ViewHolder(@NonNull final View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            classNameList = itemView.findViewById(R.id.classname_list);
            classfurther = itemView.findViewById(R.id.classfurther);

            save_edit = itemView.findViewById(R.id.save_class);
            classNameList.setEnabled(false);

            classfurther.setOnClickListener(this);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context,"working",Toast.LENGTH_SHORT).show();
//                    goToGroup(classUid,context);
//                }
//            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.classfurther:
                    goToGroup(classUid,context);
            }
        }

        private void goToGroup(String classUid, Context context) {
            if(classUid != null) {
                Intent intent = new Intent(context,Group_page.class);
                intent.putExtra("DocId",classUid);
                intent.putExtra("class_name",classp);
                context.startActivity(intent);
            }else {
                Toast.makeText(context,"Null hai boi",Toast.LENGTH_LONG).show();
            }


        }
    }

    public void removeItem(RecyclerView.ViewHolder viewHolder) {
        classesList.remove(viewHolder.getAdapterPosition());
        notifyItemRemoved(viewHolder.getAdapterPosition());

        Snackbar.make(viewHolder.itemView,"Item Deleted",Snackbar.LENGTH_SHORT).show();
    }

    public void edit(final RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        View view = viewHolder.itemView;


        final EditText className =view.findViewById(R.id.classname_list);
        ImageButton save = view.findViewById(R.id.save_class);
        className.setEnabled(true);


        save.setVisibility(View.VISIBLE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Toast.makeText(context,position,Toast.LENGTH_SHORT).show();
//            classesList.get(position).setClassName(className.getText().toString().trim());
//            notifyItemChanged(position);
//                FirebaseFirestore.getInstance().collection("Classes").document(classesList.get(position).getuId()).update("className",className.getText().toString().trim());
            }
        });


        
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(viewHolder.itemView,"Pata ni kya ho rha",Snackbar.LENGTH_SHORT).show();
//            }
//        });
    }


}
