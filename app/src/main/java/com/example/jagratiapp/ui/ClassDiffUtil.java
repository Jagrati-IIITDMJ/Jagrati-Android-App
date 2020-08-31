package com.example.jagratiapp.ui;

import com.example.jagratiapp.model.Classes;
import com.example.jagratiapp.model.Groups;
import com.example.jagratiapp.model.Students;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class ClassDiffUtil extends DiffUtil.Callback {
    private List<Classes> oldclassList;
    private List<Classes> newclassList;

    public ClassDiffUtil(List<Classes> oldclassList, List<Classes> newclassList) {
        this.oldclassList = oldclassList;
        this.newclassList = newclassList;
    }

    @Override
    public int getOldListSize() {
        return oldclassList.size();
    }

    @Override
    public int getNewListSize() {
        return newclassList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldclassList.get(oldItemPosition).equals(newclassList.get(newItemPosition));
    }
}
