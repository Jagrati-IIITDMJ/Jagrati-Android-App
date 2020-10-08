package com.example.jagratiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.StudentHolderActivity;
import com.example.jagratiapp.model.Groups;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Groups> groupList;
    private String classUid;

    public GroupRecyclerAdapter(Context context, List<Groups> groupList,String classUid) {
        this.context = context;
        this.groupList = groupList;
        this.classUid = classUid;
    }

    @NonNull
    @Override
    public GroupRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_cardview, parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRecyclerAdapter.ViewHolder holder, int position) {

        Groups groups = groupList.get(position);
        holder.groupname_List.setText(groups.getGroupName());
        holder.groupUid= groups.getUid();
        holder.groupName= groups.getGroupName();


    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void edit(RecyclerView.ViewHolder viewHolder) {
    }

    public void removeItem(RecyclerView.ViewHolder viewHolder) {
        groupList.remove(viewHolder.getAdapterPosition());
        notifyItemRemoved(viewHolder.getAdapterPosition());

        Snackbar.make(viewHolder.itemView,"Item Deleted",Snackbar.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView groupname_List;
        private String groupUid;
        private CardView group_further;
        private String groupName;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            groupname_List = itemView.findViewById(R.id.classname_list);
            group_further = itemView.findViewById(R.id.classfurther);
            group_further.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            context.startActivity(new Intent(context, StudentHolderActivity.class).putExtra("classid",classUid)
            .putExtra("groupid",groupUid).putExtra("groupName",groupName));

        }



    }
    }

