package com.example.jagratiapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jagratiapp.R;
import com.example.jagratiapp.model.BugReport;
import com.example.jagratiapp.model.Classes;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportBugAdapter extends RecyclerView.Adapter<ReportBugAdapter.ViewHolder> {
    private Context context;
    private List<BugReport> bugReports;

    public ReportBugAdapter(Context context, List<BugReport> bugReports) {
        this.context = context;
        this.bugReports = bugReports;
    }

    @NonNull
    @Override
    public ReportBugAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bug_report_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportBugAdapter.ViewHolder holder, int position) {
        BugReport bugReport = bugReports.get(position);
        holder.timeStamp.setText(bugReport.getTimeStamp());
        holder.username.setText("Raised by "+bugReport.getUsername());
        holder.description.setText(new StringBuilder().append("Description :").append(bugReport.getDescription()).toString());

    }

    @Override
    public int getItemCount() {
        return bugReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView description;
        private TextView username;
        private TextView timeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.bug_description_card);
            username = itemView.findViewById(R.id.username_bug_card);
            timeStamp = itemView.findViewById(R.id.date_added);

        }
    }
}
