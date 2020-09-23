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
        View view = LayoutInflater.from(context).inflate(R.layout.group_cardview, parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRecyclerAdapter.ViewHolder holder, int position) {

        Groups groups = groupList.get(position);
        holder.groupname_List.setText(groups.getGroupName());
        holder.groupUid= groups.getUid();


    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView groupname_List;
        private String groupUid;
        private CardView group_further;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            groupname_List = itemView.findViewById(R.id.groupname_list);
            group_further = itemView.findViewById(R.id.group_further);
            group_further.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            context.startActivity(new Intent(context, StudentHolderActivity.class).putExtra("classid",classUid)
            .putExtra("groupid",groupUid));

        }

    }
    }

