package com.example.jagratiapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jagratiapp.Group_page;
import com.example.jagratiapp.R;
import com.example.jagratiapp.model.Groups;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Groups> groupList;

    public GroupRecyclerAdapter(Context context, List<Groups> groupList) {
        this.context = context;
        this.groupList = groupList;}

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
      //  holder.groupUid= groups.getUid();


    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupname_List;
        String groupUid;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            groupname_List = itemView.findViewById(R.id.groupname_list);


        }


        }
    }

