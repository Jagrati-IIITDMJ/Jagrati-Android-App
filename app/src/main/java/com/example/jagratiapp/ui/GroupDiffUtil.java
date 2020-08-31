package com.example.jagratiapp.ui;

import com.example.jagratiapp.model.Groups;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class GroupDiffUtil extends DiffUtil.Callback{
    private List<Groups> oldGroupList;
    private List<Groups> newGroupList;

    public GroupDiffUtil(List<Groups> oldGroupList, List<Groups> newGroupList) {
        this.oldGroupList = oldGroupList;
        this.newGroupList = newGroupList;
    }

    @Override
    public int getOldListSize() {
        return oldGroupList.size();
    }

    @Override
    public int getNewListSize() {
        return newGroupList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldGroupList.get(oldItemPosition).equals(newGroupList.get(newItemPosition));
    }
}
