package com.example.jagratiapp.ui;

import androidx.recyclerview.widget.DiffUtil;

import com.example.jagratiapp.model.Students;

import java.util.List;

public class StudentDiffUtil extends DiffUtil.Callback {
    private List<Students> oldStudentList;
    private List<Students> newStudentList;

    public StudentDiffUtil(List<Students> oldStudentList, List<Students> newStudentList) {
        this.oldStudentList = oldStudentList;
        this.newStudentList = newStudentList;
    }

    @Override
    public int getOldListSize() {
        return oldStudentList.size();
    }

    @Override
    public int getNewListSize() {
        return newStudentList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldStudentList.get(oldItemPosition).equals(newStudentList.get(newItemPosition));
    }
}
